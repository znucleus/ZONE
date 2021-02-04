//# sourceURL=internship_release_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "select2-zh-CN", "flatpickr-zh", "jquery.fileupload-validate"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_internship_type_data: web_path + '/users/data/internship-type',
            file_upload_url: web_path + '/web/internship/release/upload/file',
            delete_file_url: web_path + '/web/internship/release/delete/file',
            save: web_path + '/web/internship/release/save',
            page: '/web/menu/internship/release'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            internshipType: '#internshipType',
            isTimeLimit: '#isTimeLimit',
            teacherDistributionTime: '#teacherDistributionTime',
            time: '#time',
            school: '#school',
            college: '#college',
            department: '#department',
            science: '#science'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        /*
         参数
         */
        var param = {
            internshipTypeId: '',
            isTimeLimit: '',
            teacherDistributionTime: '',
            time: '',
            schoolId: '',
            collegeId: '',
            departmentId: '',
            scienceId: '',
            internshipReleaseIsDel: '',
            files: ''
        };

        var page_param = {
            collegeId: $('#collegeId').val()
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.internshipTypeId = $(param_id.internshipType).val();
            var isTimeLimit = $('input[name="isTimeLimit"]:checked').val();
            param.isTimeLimit = _.isUndefined(isTimeLimit) ? 0 : isTimeLimit;
            param.teacherDistributionTime = $(param_id.teacherDistributionTime).val();
            param.time = $(param_id.time).val();
            param.schoolId = $(param_id.school).val();
            if (Number(page_param.collegeId) === 0) {
                param.collegeId = $(param_id.college).val();
            } else {
                param.collegeId = page_param.collegeId;
            }
            param.departmentId = $(param_id.department).val();
            param.scienceId = $(param_id.science).val();
            var internshipReleaseIsDel = $('input[name="internshipReleaseIsDel"]:checked').val();
            param.internshipReleaseIsDel = _.isUndefined(internshipReleaseIsDel) ? 0 : internshipReleaseIsDel;

            var f = $('.file');
            var p = [];
            for (var i = 0; i < f.length; i++) {
                p.push({
                    originalFileName: $(f[i]).attr('data-original-file-name'),
                    newName: $(f[i]).attr('data-new-name'),
                    relativePath: $(f[i]).attr('data-file-path'),
                    ext: $(f[i]).attr('data-ext'),
                    fileSize: $(f[i]).attr('data-size'),
                    contentType: $(f[i]).attr('data-content-type')
                });
            }
            if (p.length > 0) {
                param.files = JSON.stringify(p);
            } else {
                param.files = '';
            }
        }

        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            if (Number(page_param.collegeId) === 0) {
                initSchool();
            } else {
                initDepartment(page_param.collegeId);
            }
            initInternshipType();
            initSelect2();
        }

        function initInternshipType() {
            $.get(ajax_url.obtain_internship_type_data, function (data) {
                $(param_id.internshipType).select2({
                    data: data.results
                });
            });
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                $(param_id.school).select2({
                    data: data.results
                });
            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    $(param_id.college).select2({data: data.results});
                });
            } else {
                $(param_id.college).html('<option label="请选择院"></option>');
            }
        }

        function initDepartment(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                    $(param_id.department).html('<option label="请选择系"></option>');
                    $(param_id.department).select2({data: data.results});
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
            }
        }

        function initScience(departmentId) {
            if (Number(departmentId) > 0) {
                $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                    $(param_id.science).html('<option label="请选择专业"></option>');
                    $(param_id.science).select2({data: data.results});
                });
            } else {
                $(param_id.science).html('<option label="请选择专业"></option>');
            }
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(param_id.isTimeLimit).click(function () {
            initParam();
            var isTimeLimit = param.isTimeLimit;
            if (Number(isTimeLimit) === 0) {
                // 不需要填写时间
                $(param_id.teacherDistributionTime).parent().css('display', 'none');
                $(param_id.time).parent().css('display', 'none');
            } else {
                $(param_id.teacherDistributionTime).parent().css('display', '');
                $(param_id.time).parent().css('display', '');
            }
        });

        $(param_id.teacherDistributionTime).flatpickr({
            "locale": "zh",
            "mode": "range"
        });

        $(param_id.time).flatpickr({
            "locale": "zh",
            "mode": "range"
        });

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);
            initDepartment(0);
            initScience(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();
            initDepartment(v);
            initScience(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        $(param_id.department).change(function () {
            var v = $(this).val();
            initScience(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
            }
        });

        $(param_id.science).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.science);
            }
        });

        // 上传组件
        $('#fileupload').fileupload({
            url: ajax_url.file_upload_url,
            dataType: 'json',
            maxFileSize: 500000000,// 500MB
            formAcceptCharset: 'utf-8',
            messages: {
                maxFileSize: '单文件上传仅允许500MB大小'
            },
            done: function (e, data) {
                Messenger().post({
                    message: data.result.msg,
                    type: data.result.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.result.state) {
                    fileShow(data.result);
                }
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#progress').find('.progress-bar').css(
                    'width',
                    progress + '%'
                );
            }
        }).on('fileuploadadd', function (evt, data) {
            var isOk = true;
            var $this = $(this);
            var validation = data.process(function () {
                return $this.fileupload('process', data);
            });
            validation.fail(function (data) {
                isOk = false;
                Messenger().post({
                    message: '上传失败: ' + data.files[0].error,
                    type: 'error',
                    showCloseButton: true
                });
            });
            return isOk;
        });

        /**
         * 文件显示
         * @param data 数据
         */
        function fileShow(data) {
            var template = Handlebars.compile($("#file-template").html());

            Handlebars.registerHelper('translationSize', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.toSize(this.fileSize)));
            });

            Handlebars.registerHelper('lastPath', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(this.relativePath));
            });

            $('#fileShow').append(template(data));
        }

        /*
         删除附件
         */
        $('#fileShow').delegate('.clearFile', "click", function () {
            var path = $(this).attr('data-file-path');
            var obj = $(this);
            $.post(ajax_url.delete_file_url, {filePath: path}, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    obj.parent().parent().remove();
                }
            });
        });


        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validInternshipType();
        });

        function validInternshipType() {
            var internshipTypeId = param.internshipTypeId;
            if (Number(internshipTypeId) <= 0) {
                tools.validSelect2ErrorDom(param_id.internshipType, '请选择实习类型');
            } else {
                tools.validSelect2SuccessDom(param_id.internshipType);
                validIsTimeLimit();
            }
        }

        function validIsTimeLimit() {
            var isTimeLimit = param.isTimeLimit;
            if (Number(isTimeLimit) === 0) {
                validSchoolId();
            } else {
                validTeacherDistributionTime();
            }
        }

        function validTeacherDistributionTime() {
            var teacherDistributionTime = param.teacherDistributionTime;
            if (teacherDistributionTime.length <= 0) {
                tools.validErrorDom(param_id.teacherDistributionTime, '请选择指导教师分配时间');
            } else {
                tools.validSuccessDom(param_id.teacherDistributionTime);
                validTime();
            }
        }

        function validTime() {
            var time = param.time;
            if (time.length <= 0) {
                tools.validErrorDom(param_id.time, '请选择实习申请时间');
            } else {
                tools.validSuccessDom(param_id.time);
                if (Number(page_param.collegeId) === 0) {
                    validSchoolId();
                } else {
                    validDepartmentId();
                }
            }
        }

        /**
         * 检验学校id
         */
        function validSchoolId() {
            var schoolId = param.schoolId;
            if (Number(schoolId) <= 0) {
                tools.validSelect2ErrorDom(param_id.school, '请选择学校');
            } else {
                tools.validSelect2SuccessDom(param_id.school);
                validCollegeId();
            }
        }

        /**
         * 检验院id
         */
        function validCollegeId() {
            var collegeId = param.collegeId;
            if (Number(collegeId) <= 0) {
                tools.validSelect2ErrorDom(param_id.college, '请选择院');
            } else {
                tools.validSelect2SuccessDom(param_id.college);
                validDepartmentId();
            }
        }

        function validDepartmentId() {
            var departmentId = param.departmentId;
            if (Number(departmentId) <= 0) {
                tools.validSelect2ErrorDom(param_id.department, '请选择系');
            } else {
                tools.validSelect2SuccessDom(param_id.department);
                validScienceId();
            }
        }

        function validScienceId() {
            var scienceId = param.scienceId;
            if (Number(scienceId) <= 0) {
                tools.validSelect2ErrorDom(param_id.science, '请选择专业');
            } else {
                tools.validSelect2SuccessDom(param_id.science);
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
                url: ajax_url.save,
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