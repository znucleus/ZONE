//# sourceURL=leaver_release_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger",
        "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            obtain_organize_data: web_path + '/anyone/data/organize',
            option_del: web_path + '/web/register/leaver/option/delete',
            option_update: web_path + '/web/register/leaver/option/update',
            update: web_path + '/web/register/leaver/release/update',
            page: '/web/menu/register/leaver'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            title: '#title',
            school: '#school',
            college: '#college',
            department: '#department',
            science: '#science',
            grade: '#grade',
            organize: '#organize',
            dataScope: '#dataScope',
            dataId: '#dataId'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            },
            okData: {
                id: '#okData',
                text: '确定',
                tip: '确定中...'
            }
        };

        /*
         参数
         */
        var param = {
            leaverRegisterReleaseId: '',
            title: '',
            optionContent: '',
            dataScope: '',
            dataId: ''
        };

        var page_param = {
            schoolId: $('#schoolId').val(),
            paramLeaverRegisterReleaseId: $('#paramLeaverRegisterReleaseId').val(),
            paramDataScope: $('#paramDataScope').val()
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.leaverRegisterReleaseId = page_param.paramLeaverRegisterReleaseId;
            param.title = _.trim($(param_id.title).val());

            var optionContents = [];
            var optionContent = $("input[name='optionContent']");
            for (var j = 0; j < optionContent.length; j++) {
                var content = _.trim($(optionContent[j]).val());
                if (content !== '') {
                    optionContents.push(content);
                }
            }
            param.optionContent = optionContents.join('|');

            param.dataScope = $(param_id.dataScope).val();
            var dataIds = [];
            var dataId = $('.dataId');
            for (var i = 0; i < dataId.length; i++) {
                dataIds.push(_.trim($(dataId[i]).text()));
            }
            param.dataId = dataIds.join(',');
        }

        $('#addOption').click(function () {
            var html = '<div class="col-md-12 mt-2">' +
                '<div class="input-group">' +
                '<input type="text" name="optionContent" class="form-control" placeholder="选项名">' +
                '<div class="input-group-append">' +
                '<button class="btn btn-outline-danger delOption" type="button"><i class="fa fa-minus"></i></button>' +
                '</div>' +
                '</div>' +
                '</div>';
            $('#options').append(html);
        });

        var options = '#options';

        $(options).delegate('.delOption', "click", function () {
            var id = _.trim($(this).attr('data-id'));
            if (id !== '') {
                var existOptionContent = $("input[name='existOptionContent']");
                if (existOptionContent.length <= 1) {
                    Messenger().post({
                        message: "需要至少保留一个选项",
                        type: 'error',
                        showCloseButton: true
                    });
                } else {
                    leaverOptionDel(id, this);
                }
            } else {
                $(this).parent().parent().parent().remove();
            }
        });

        function leaverOptionDel(id, obj) {
            Swal.fire({
                title: "确定删除吗？",
                text: "选项删除！删除后不可恢复。",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendLeaverOptionDelAjax(id, obj);
                }
            });
        }

        function sendLeaverOptionDelAjax(leaverRegisterOptionId, obj) {
            $.ajax({
                type: 'POST',
                url: ajax_url.option_del,
                data: {
                    leaverRegisterOptionId: leaverRegisterOptionId,
                    leaverRegisterReleaseId: page_param.paramLeaverRegisterReleaseId
                },
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });
                    if (data.state) {
                        $(obj).parent().parent().parent().remove();
                    }
                },
                error: function (XMLHttpRequest) {
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        $(options).delegate('.saveOption', "click", function () {
            var id = _.trim($(this).attr('data-id'));
            var optionContent = _.trim($(this).parent().prev().val());
            if (optionContent !== '') {
                sendLeaverOptionUpdateAjax(id, optionContent);
            } else {
                Messenger().post({
                    message: '请填写选项内容',
                    type: 'error',
                    showCloseButton: true
                });
            }

        });

        function sendLeaverOptionUpdateAjax(leaverRegisterOptionId, optionContent) {
            $.ajax({
                type: 'POST',
                url: ajax_url.option_update,
                data: {
                    optionContent: optionContent,
                    leaverRegisterOptionId: leaverRegisterOptionId,
                    leaverRegisterReleaseId: page_param.paramLeaverRegisterReleaseId
                },
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });
                },
                error: function (XMLHttpRequest) {
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        /*
        初始化数据
        */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initSelect2();
            if (Number(page_param.schoolId) > 0) {
                $(param_id.school).parent().parent().css('display', 'none');
                initCollege(page_param.schoolId);
            } else {
                initSchool();
            }

            initMaxLength();
            initDataScope();
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                $(param_id.school).select2({
                    data: data.results,
                    dropdownParent: $("#dataModal")
                });
            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    $(param_id.college).select2({
                        data: data.results,
                        dropdownParent: $("#dataModal")
                    });
                });
            } else {
                $(param_id.college).html('<option label="请选择院"></option>');
            }
        }

        function initDepartment(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                    $(param_id.department).html('<option label="请选择系"></option>');
                    $(param_id.department).select2({
                        data: data.results,
                        dropdownParent: $("#dataModal")
                    });
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
            }
        }

        function initScience(departmentId) {
            if (Number(departmentId) > 0) {
                $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                    $(param_id.science).html('<option label="请选择专业"></option>');
                    $(param_id.science).select2({
                        data: data.results,
                        dropdownParent: $("#dataModal")
                    });
                });
            } else {
                $(param_id.science).html('<option label="请选择专业"></option>');
            }
        }

        function initGrade(scienceId) {
            if (Number(scienceId) > 0) {
                $.get(ajax_url.obtain_grade_data, {scienceId: scienceId}, function (data) {
                    $(param_id.grade).html('<option label="请选择年级"></option>');
                    $(param_id.grade).select2({
                        data: data.results,
                        dropdownParent: $("#dataModal")
                    });
                });
            } else {
                $(param_id.grade).html('<option label="请选择年级"></option>');
            }
        }

        function initOrganize(gradeId) {
            if (Number(gradeId) > 0) {
                $.get(ajax_url.obtain_organize_data, {gradeId: gradeId}, function (data) {
                    $(param_id.organize).html('<option label="请选择班级"></option>');
                    $(param_id.organize).select2({
                        data: data.results,
                        dropdownParent: $("#dataModal")
                    });
                });
            } else {
                $(param_id.organize).html('<option label="请选择班级"></option>');
            }
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.title).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        function initDataScope() {
            $(param_id.dataScope).val(page_param.paramDataScope);
            dataScopeChange(page_param.paramDataScope);
        }

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
            }
        });

        var selectData = $('#selectData');
        $(param_id.dataScope).change(function () {
            var v = $(this).val();
            selectData.empty();

            $(param_id.school).val('').trigger('change');
            $(param_id.college).val('').trigger('change');
            $(param_id.department).val('').trigger('change');
            $(param_id.science).val('').trigger('change');
            $(param_id.grade).val('').trigger('change');
            $(param_id.organize).val('').trigger('change');

            dataScopeChange(v);
        });

        function dataScopeChange(v) {
            if (Number(v) === 1) {
                $(param_id.college).prop('disabled', false);
                $(param_id.department).prop('disabled', true);
                $(param_id.science).prop('disabled', true);
                $(param_id.grade).prop('disabled', true);
                $(param_id.organize).prop('disabled', true);
            } else if (Number(v) === 2) {
                $(param_id.college).prop('disabled', false);
                $(param_id.department).prop('disabled', false);
                $(param_id.science).prop('disabled', true);
                $(param_id.grade).prop('disabled', true);
                $(param_id.organize).prop('disabled', true);
            } else if (Number(v) === 3) {
                $(param_id.college).prop('disabled', false);
                $(param_id.department).prop('disabled', false);
                $(param_id.science).prop('disabled', false);
                $(param_id.grade).prop('disabled', true);
                $(param_id.organize).prop('disabled', true);
            } else if (Number(v) === 4) {
                $(param_id.college).prop('disabled', false);
                $(param_id.department).prop('disabled', false);
                $(param_id.science).prop('disabled', false);
                $(param_id.grade).prop('disabled', false);
                $(param_id.organize).prop('disabled', true);
            } else if (Number(v) === 5) {
                $(param_id.college).prop('disabled', false);
                $(param_id.department).prop('disabled', false);
                $(param_id.science).prop('disabled', false);
                $(param_id.grade).prop('disabled', false);
                $(param_id.organize).prop('disabled', false);
            } else {
                $(param_id.college).prop('disabled', true);
                $(param_id.department).prop('disabled', true);
                $(param_id.science).prop('disabled', true);
                $(param_id.grade).prop('disabled', true);
                $(param_id.organize).prop('disabled', true);
            }
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            if (Number(page_param.schoolId) > 0) {
                initCollege(page_param.schoolId);
            } else {
                initCollege(v);
            }
            initDepartment(0);
            initScience(0);
            initGrade(0);
            initOrganize(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();
            initDepartment(v);
            initScience(0);
            initGrade(0);
            initOrganize(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        $(param_id.department).change(function () {
            var v = $(this).val();
            initScience(v);
            initGrade(0);
            initOrganize(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
            }
        });

        $(param_id.science).change(function () {
            var v = $(this).val();
            initGrade(v);
            initOrganize(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.science);
            }
        });

        $(param_id.grade).change(function () {
            var v = $(this).val();
            initOrganize(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.grade);
            }
        });

        $(param_id.organize).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.organize);
            }
        });

        $(button_id.okData.id).click(function () {
            $('#dataModal').modal('hide');
            initParam();
            var dataScope = Number(param.dataScope);
            var dataIdStr = param.dataId;

            var dataId = '';
            var dataName = '';
            if (dataScope === 1) {
                dataId = $(param_id.college).find(':selected').val();
                dataName = $(param_id.college).find(':selected').text();
            } else if (dataScope === 2) {
                dataId = $(param_id.department).find(':selected').val();
                dataName = $(param_id.department).find(':selected').text();
            } else if (dataScope === 3) {
                dataId = $(param_id.science).find(':selected').val();
                dataName = $(param_id.science).find(':selected').text();
            } else if (dataScope === 4) {
                dataId = $(param_id.grade).find(':selected').val();
                dataName = $(param_id.grade).find(':selected').text();
            } else if (dataScope === 5) {
                dataId = $(param_id.organize).find(':selected').val();
                dataName = $(param_id.organize).find(':selected').text();
            }

            if (Number(dataId) > 0) {
                var hasDataId = false;
                var dataIds = dataIdStr.split(',');
                for (var i = 0; i < dataIds.length; i++) {
                    if (dataIds[i] === dataId) {
                        hasDataId = true;
                        break;
                    }
                }

                if (!hasDataId) {
                    tools.validCustomerSingleSuccessDom(param_id.dataId);
                    var template = Handlebars.compile($("#data_template").html());
                    var context =
                        {
                            listResult: [
                                {
                                    "dataId": dataId,
                                    "dataName": dataName
                                }
                            ]
                        };

                    selectData.append(template(context));
                }
            }
        });

        selectData.delegate('.del', "click", function () {
            $(this).parent().parent().remove();
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validTitle();
        });

        function validTitle() {
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
                validOptionContent();
            }
        }

        function validOptionContent() {
            var optionContent = param.optionContent;
            if (optionContent.length <= 0) {
                var existOptionContent = $("input[name='existOptionContent']");
                if (existOptionContent.length <= 0) {
                    $('#optionError').text('请添加选项');
                } else {
                    var existOptionContents = [];
                    for (var j = 0; j < existOptionContent.length; j++) {
                        var content = _.trim($(existOptionContent[j]).val());
                        if (content !== '') {
                            existOptionContents.push(content);
                        }
                    }
                    $('#optionError').text('');
                    validDataScope();
                }
            } else {
                $('#optionError').text('');
                validDataScope();
            }
        }

        function validDataScope() {
            var dataScope = param.dataScope;
            if (Number(dataScope) <= 0) {
                tools.validErrorDom(param_id.dataScope, '请选择数据域');
            } else {
                tools.validSuccessDom(param_id.dataScope);
                validDataId();
            }
        }

        function validDataId() {
            var dataId = param.dataId;
            if (dataId.length <= 0) {
                tools.validCustomerSingleErrorDom(param_id.dataId, '请选择数据');
            } else {
                tools.validCustomerSingleSuccessDom(param_id.dataId);
                sendAjax();
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.update,
                data: param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.page);
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

    });