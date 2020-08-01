//# sourceURL=upgrade_staff.js
require(["jquery", "lodash", "tools", "sweetalert2", "select2-zh-CN"],
    function ($, _, tools, Swal) {

        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            check_staff_number: web_path + '/anyone/check/staff/number',
            upgrade: web_path + '/users/type/upgrade/staff'
        };

        var param_id = {
            school: '#school',
            college: '#college',
            department: '#department',
            staffNumber: '#staffNumber'
        };

        var button_id = {
            upgrade: {
                tip: '升级中...',
                text: '升级',
                id: '#upgrade'
            }
        };

        var param = {
            schoolId: '',
            collegeId: '',
            departmentId: '',
            staffNumber: ''
        };

        var page_param = {
            schoolId: $('#schoolIdParam').val(),
            collegeId: $('#collegeIdParam').val()
        };

        var init_configure = {
            init_school: false,
            init_college: false
        };

        function initParam() {
            param.schoolId = $(param_id.school).val();
            param.collegeId = $(param_id.college).val();
            param.departmentId = $(param_id.department).val();
            param.staffNumber = _.trim($(param_id.staffNumber).val());
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

        init();

        function init() {
            initSelect2();
            initSchool();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                var schoolSelect2 =  $(param_id.school).select2({
                    data: data.results
                });

                if (!init_configure.init_school) {
                    schoolSelect2.val(Number(page_param.schoolId)).trigger("change");
                    init_configure.init_school = true;
                }
            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    var collegeSelect2 =  $(param_id.college).select2({data: data.results});
                    if (!init_configure.init_college) {
                        collegeSelect2.val(Number(page_param.collegeId)).trigger("change");
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
                    $(param_id.department).select2({data: data.results});
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
            }
        }

        $(param_id.staffNumber).blur(function () {
            initParam();
            var staffNumber = param.staffNumber;
            if (staffNumber !== '') {
                var regex = tools.regex.staffNumber;
                if (!regex.test(staffNumber)) {
                    tools.validErrorDom(param_id.staffNumber, '工号至少1位数字');
                    return;
                }

                $.post(ajax_url.check_staff_number, {staffNumber: staffNumber}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.staffNumber);
                    } else {
                        tools.validErrorDom(param_id.staffNumber, data.msg);
                    }
                });
            } else {
                tools.validSuccessDom(param_id.staffNumber);
            }
        });

        $(button_id.upgrade.id).click(function () {
            initParam();
            validSchool();
        });

        function validSchool() {
            var schoolId = param.schoolId;
            if (Number(schoolId) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
                validCollege();
            } else {
                tools.validSelect2ErrorDom(param_id.school, '请选择您所在的学校');
            }
        }

        function validCollege() {
            var collegeId = param.collegeId;
            if (Number(collegeId) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
                validDepartment();
            } else {
                tools.validSelect2ErrorDom(param_id.college, '请选择您所在的院');
            }
        }

        function validDepartment() {
            var departmentId = param.departmentId;
            if (Number(departmentId) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
                validStaffNumber();
            } else {
                tools.validSelect2ErrorDom(param_id.department, '请选择您所在的系');
            }
        }

        function validStaffNumber() {
            var staffNumber = param.staffNumber;
            if (staffNumber !== '') {
                var regex = tools.regex.staffNumber;
                if (!regex.test(staffNumber)) {
                    tools.validErrorDom(param_id.staffNumber, '工号至少1位数字');
                    return;
                }

                $.post(ajax_url.check_staff_number, {staffNumber: staffNumber}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.staffNumber);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.staffNumber, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.staffNumber, "您的工号？");
            }
        }

        function sendAjax() {
            // 显示遮罩
            tools.buttonLoading(button_id.upgrade.id, button_id.upgrade.tip);
            $.post(ajax_url.upgrade, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.upgrade.id, button_id.upgrade.text);
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    Swal.fire('升级失败', data.msg, 'error');
                }
            });
        }
    });