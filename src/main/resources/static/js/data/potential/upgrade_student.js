//# sourceURL=upgrade_student.js
require(["jquery", "lodash", "tools", "sweetalert2", "select2-zh-CN"],
    function ($, _, tools, Swal) {

        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            obtain_organize_data: web_path + '/anyone/data/organize',
            check_student_number: web_path + '/anyone/check-student-number',
            upgrade: web_path + '/users/type/upgrade/student'
        };

        var param_id = {
            school: '#school',
            college: '#college',
            department: '#department',
            science: '#science',
            grade: '#grade',
            organize: '#organize',
            studentNumber: '#studentNumber'
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
            scienceId: '',
            gradeId: '',
            organizeId: '',
            studentNumber: ''
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
            param.scienceId = $(param_id.science).val();
            param.gradeId = $(param_id.grade).val();
            param.organizeId = $(param_id.organize).val();
            param.studentNumber = _.trim($(param_id.studentNumber).val());
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);
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
                var schoolSelect2 = $(param_id.school).select2({
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
                    var collegeSelect2 = $(param_id.college).select2({data: data.results});
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

        function initGrade(scienceId) {
            if (Number(scienceId) > 0) {
                $.get(ajax_url.obtain_grade_data, {scienceId: scienceId}, function (data) {
                    $(param_id.grade).html('<option label="请选择年级"></option>');
                    $(param_id.grade).select2({data: data.results});
                });
            } else {
                $(param_id.grade).html('<option label="请选择年级"></option>');
            }
        }

        function initOrganize(gradeId) {
            if (Number(gradeId) > 0) {
                $.get(ajax_url.obtain_organize_data, {gradeId: gradeId}, function (data) {
                    $(param_id.organize).html('<option label="请选择班级"></option>');
                    $(param_id.organize).select2({data: data.results});
                });
            } else {
                $(param_id.organize).html('<option label="请选择班级"></option>');
            }
        }

        $(param_id.studentNumber).blur(function () {
            initParam();
            var studentNumber = param.studentNumber;
            if (studentNumber !== '') {
                var regex = tools.regex.studentNumber;
                if (!regex.test(studentNumber)) {
                    tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                    return;
                }

                $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.studentNumber);
                    } else {
                        tools.validErrorDom(param_id.studentNumber, data.msg);
                    }
                });
            } else {
                tools.validSuccessDom(param_id.studentNumber);
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
                validScience();
            } else {
                tools.validSelect2ErrorDom(param_id.department, '请选择您所在的系');
            }
        }

        function validScience() {
            var scienceId = param.scienceId;
            if (Number(scienceId) > 0) {
                tools.validSelect2SuccessDom(param_id.science);
                validGrade();
            } else {
                tools.validSelect2ErrorDom(param_id.science, '请选择您所在的专业');
            }
        }

        function validGrade() {
            var gradeId = param.gradeId;
            if (Number(gradeId) > 0) {
                tools.validSelect2SuccessDom(param_id.grade);
                validOrganize();
            } else {
                tools.validSelect2ErrorDom(param_id.grade, '请选择您所在的年级');
            }
        }

        function validOrganize() {
            var organizeId = param.organizeId;
            if (Number(organizeId) > 0) {
                tools.validSelect2SuccessDom(param_id.organize);
                validStudentNumber();
            } else {
                tools.validSelect2ErrorDom(param_id.organize, '请选择您所在的班级');
            }
        }

        function validStudentNumber() {
            var studentNumber = param.studentNumber;
            if (studentNumber !== '') {
                var regex = tools.regex.studentNumber;
                if (!regex.test(studentNumber)) {
                    tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                    return;
                }

                $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.studentNumber);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.studentNumber, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.studentNumber, "您的学号？");
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