//# sourceURL=science_edit.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {
        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/schools',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            update: '/web/data/science/update',
            check_name: web_path + '/web/data/science/check/edit/name',
            check_code: web_path + '/web/data/science/check/edit/code',
            page: '/web/menu/data/science'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            school: '#school',
            college: '#college',
            department: '#department',
            scienceName: '#scienceName',
            scienceCode: '#scienceCode'
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
            schoolId: '',
            collegeId: '',
            departmentId: '',
            scienceId: '',
            scienceName: '',
            scienceCode: '',
            scienceIsDel: ''
        };

        var page_param = {
            paramSchoolId: $('#paramSchoolId').val(),
            paramCollegeId: $('#paramCollegeId').val(),
            paramDepartmentId: $('#paramDepartmentId').val(),
            paramScienceId: $('#paramScienceId').val(),
            collegeId: $('#collegeId').val()

        };

        var init_configure = {
            init_school: false,
            init_college: false,
            init_department: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.schoolId = $(param_id.school).val();
            if (Number(page_param.collegeId) === 0) {
                param.collegeId = $(param_id.college).val();
            } else {
                param.collegeId = page_param.collegeId;
            }
            param.departmentId = $(param_id.department).val();
            param.scienceId = page_param.paramScienceId;
            param.scienceName = _.trim($(param_id.scienceName).val());
            param.scienceCode = _.trim($(param_id.scienceCode).val());
            var scienceIsDel = $('input[name="scienceIsDel"]:checked').val();
            param.scienceIsDel = _.isUndefined(scienceIsDel) ? 0 : scienceIsDel;
        }

        /*
         初始化页面
         */
        init();

        /**
         * 初始化数据
         */
        function init() {
            if (Number(page_param.collegeId) === 0) {
                initSchool();
            } else {
                initDepartment(page_param.collegeId);
            }
            initSelect2();
            initMaxLength();
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                var sl = $(param_id.school).select2({
                    data: data.results
                });

                if (!init_configure.init_school) {
                    sl.val(page_param.paramSchoolId).trigger("change");
                    init_configure.init_school = true;
                }
            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    var sl = $(param_id.college).select2({data: data.results});
                    if (!init_configure.init_college) {
                        sl.val(page_param.paramCollegeId).trigger("change");
                        init_configure.init_college = true;
                    }
                });
            } else {
                $(param_id.college).html('<option label="请选择院"></option>');
            }
        }

        function initDepartment(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                    $(param_id.department).html('<option label="请选择系"></option>');
                    var sl = $(param_id.department).select2({data: data.results});
                    if (!init_configure.init_department) {
                        sl.val(page_param.paramDepartmentId).trigger("change");
                        init_configure.init_department = true;
                    }
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
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
            $(param_id.scienceName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.scienceCode).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);
            initDepartment(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();
            initDepartment(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        $(param_id.department).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
            }
        });

        /*
         即时检验系名
         */
        $(param_id.scienceName).blur(function () {
            initParam();
            var scienceName = param.scienceName;
            if (scienceName.length <= 0 || scienceName.length > 200) {
                tools.validErrorDom(param_id.scienceName, '专业名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.scienceName);
                    } else {
                        tools.validErrorDom(param_id.scienceName, data.msg);
                    }
                });
            }
        });

        /*
         即时检验系代码
         */
        $(param_id.scienceCode).blur(function () {
            initParam();
            var scienceCode = param.scienceCode;
            if (scienceCode.length <= 0 || scienceCode.length > 20) {
                tools.validErrorDom(param_id.scienceCode, '专业代码20个字符以内');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.scienceCode);
                    } else {
                        tools.validErrorDom(param_id.scienceCode, data.msg);
                    }
                });
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            if (Number(page_param.collegeId) === 0) {
                validSchoolId();
            } else {
                validDepartmentId();
            }
        });

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

        /**
         * 检验系id
         */
        function validDepartmentId() {
            var departmentId = param.departmentId;
            if (Number(departmentId) <= 0) {
                tools.validSelect2ErrorDom(param_id.department, '请选择系');
            } else {
                tools.validSelect2SuccessDom(param_id.department);
                validScienceName();
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validScienceName() {
            var scienceName = param.scienceName;
            if (scienceName.length <= 0 || scienceName.length > 200) {
                tools.validErrorDom(param_id.scienceName, '专业名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.scienceName);
                        validScienceCode();
                    } else {
                        tools.validErrorDom(param_id.scienceName, data.msg);
                    }
                });
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validScienceCode() {
            initParam();
            var scienceCode = param.scienceCode;
            if (scienceCode.length <= 0 || scienceCode.length > 20) {
                tools.validErrorDom(param_id.scienceCode, '专业代码200个字符以内');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.scienceCode);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.scienceCode, data.msg);
                    }
                });
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