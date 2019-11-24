//# sourceURL=users_profile_student.js
require(["jquery", "lodash", "tools", "sweetalert2", "bootstrap",
        "csrf",  "select2-zh-CN", "bootstrap-datepicker-zh-CN", "bootstrap-inputmask"],
    function ($, _, tools, Swal) {

        var ajax_url = {
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            obtain_organize_data: web_path + '/anyone/data/organize',
            student_update_school: web_path + '/user/student/update/school'
        };

        var param_id = {
            department: '#department',
            science: '#science',
            grade: '#grade',
            organize: '#organize'
        };

        var button_id = {
            saveSchool: {
                tip: '保存中...',
                text: '保存',
                id: '#saveSchool'
            }
        };

        var param = {
            departmentId: '',
            scienceId: '',
            gradeId: '',
            organizeId: ''
        };

        var page_param = {
            collegeId: $('#collegeId').val(),
            departmentId: $('#departmentId').val(),
            scienceId: $('#scienceId').val(),
            gradeId: $('#gradeId').val(),
            organizeId: $('#organizeId').val()
        };

        var init_configure = {
            init_department: false,
            init_science: false,
            init_grade: false,
            init_organize: false
        };

        function initParam() {
            param.departmentId = $(param_id.department).val();
            param.scienceId = $(param_id.science).val();
            param.gradeId = $(param_id.grade).val();
            param.organizeId = $(param_id.organize).val();
        }

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
            initDepartment($('#collegeId').val());
            initScience($('#departmentId').val());
            initGrade($('#scienceId').val());
            initOrganize($('#gradeId').val());
            initSelect2();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initDepartment(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                    $(param_id.department).html('<option label="请选择系"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.department).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });

                    if (!init_configure.init_department) {
                        $(param_id.department).val($('#departmentId').val());
                        init_configure.init_department = true;
                    }
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
            }
        }

        function initScience(departmentId) {
            if (Number(departmentId) > 0) {
                $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                    $(param_id.science).html('<option label="请选择专业"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.science).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });
                    if (!init_configure.init_science) {
                        $(param_id.science).val($('#scienceId').val());
                        init_configure.init_science = true;
                    }
                });
            } else {
                $(param_id.science).html('<option label="请选择专业"></option>');
            }
        }

        function initGrade(scienceId) {
            if (Number(scienceId) > 0) {
                $.get(ajax_url.obtain_grade_data, {scienceId: scienceId}, function (data) {
                    $(param_id.grade).html('<option label="请选择年级"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.grade).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });
                    if (!init_configure.init_grade) {
                        $(param_id.grade).val($('#gradeId').val());
                        init_configure.init_grade = true;
                    }
                });
            } else {
                $(param_id.grade).html('<option label="请选择年级"></option>');
            }
        }

        function initOrganize(gradeId) {
            if (Number(gradeId) > 0) {
                $.get(ajax_url.obtain_organize_data, {gradeId: gradeId}, function (data) {
                    $(param_id.organize).html('<option label="请选择班级"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.organize).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });
                    if (!init_configure.init_organize) {
                        $(param_id.organize).val($('#organizeId').val());
                        init_configure.init_organize = true;
                    }
                });
            } else {
                $(param_id.organize).html('<option label="请选择班级"></option>');
            }
        }

        $(button_id.saveSchool.id).click(function () {
            initParam();
            validDepartment();
        });

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
                // 不一样才更新
                if (Number(organizeId) !== Number($('#organizeId').val())) {
                    updateSchool();
                    tools.validSelect2SuccessDom(param_id.organize);
                } else {
                    tools.validSelect2ErrorDom(param_id.organize, '班级信息未发生改变');
                }
            } else {
                tools.validSelect2ErrorDom(param_id.organize, '请选择您所在的班级');
            }
        }

        function updateSchool() {
            // 显示遮罩
            tools.buttonLoading(button_id.saveSchool.id, button_id.saveSchool.tip);
            $.post(ajax_url.student_update_school, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.saveSchool.id, button_id.saveSchool.text);
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
                    Swal.fire('更新失败', data.msg, 'error');
                }
            });
        }
    });