//# sourceURL=users_profile_student.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "bootstrap",
        "csrf", "select2-zh-CN", "flatpickr-zh", "bootstrap-inputmask"],
    function ($, _, tools, Swal, moment) {

        moment.locale('zh-cn');

        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            obtain_organize_data: web_path + '/anyone/data/organize',
            student_update_school: web_path + '/users/student/update/school',
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            check_student_number: web_path + '/users/check-student-number',
            student_update_info: web_path + '/users/student/update/info'
        };

        var param_id = {
            school: '#school',
            college: '#college',
            department: '#department',
            science: '#science',
            grade: '#grade',
            organize: '#organize',
            studentNumber: '#studentNumber',
            sex: '#sex',
            birthday: '#birthday',
            nationId: '#nation',
            politicalLandscapeId: '#politicalLandscape',
            dormitoryNumber: '#dormitoryNumber',
            parentName: '#parentName',
            parentContactPhone: '#parentContactPhone',
            familyResidence: '#familyResidence',
            placeOrigin: '#placeOrigin'
        };

        var button_id = {
            saveSchool: {
                tip: '保存中...',
                text: '保存',
                id: '#saveSchool'
            },
            info: {
                tip: '保存中...',
                text: '保存',
                id: '#updateInfo'
            }
        };

        var param = {
            schoolId: '',
            collegeId: '',
            departmentId: '',
            scienceId: '',
            gradeId: '',
            organizeId: '',
            studentNumber: '',
            sex: '',
            birthday: '',
            nationId: '',
            politicalLandscapeId: '',
            dormitoryNumber: '',
            parentName: '',
            parentContactPhone: '',
            familyResidence: '',
            placeOrigin: ''
        };

        var page_param = {
            schoolId: $('#schoolId').val(),
            collegeId: $('#collegeId').val(),
            departmentId: $('#departmentId').val(),
            scienceId: $('#scienceId').val(),
            gradeId: $('#gradeId').val(),
            organizeId: $('#organizeId').val(),
            sex: $('#sexParam').val(),
            nationId: $('#nationParam').val(),
            politicalLandscapeId: $('#politicalLandscapeParam').val()
        };

        var init_configure = {
            init_school: false,
            init_college: false,
            init_department: false,
            init_science: false,
            init_grade: false,
            init_organize: false
        };

        function initParam() {
            param.schoolId = $(param_id.school).val();
            param.collegeId = $(param_id.college).val();
            param.departmentId = $(param_id.department).val();
            param.scienceId = $(param_id.science).val();
            param.gradeId = $(param_id.grade).val();
            param.organizeId = $(param_id.organize).val();
            param.studentNumber = _.trim($(param_id.studentNumber).val());
            param.sex = $(param_id.sex).val();
            param.birthday = $(param_id.birthday).val();
            param.nationId = $(param_id.nationId).val();
            param.politicalLandscapeId = $(param_id.politicalLandscapeId).val();
            param.dormitoryNumber = $(param_id.dormitoryNumber).val();
            param.parentName = _.trim($(param_id.parentName).val());
            param.parentContactPhone = _.trim($(param_id.parentContactPhone).val());
            param.familyResidence = _.trim($(param_id.familyResidence).val());
            param.placeOrigin = _.trim($(param_id.placeOrigin).val());
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
            initSchool();
            initSelect2();

            initSex();
            initNation();
            initPoliticalLandscapeId();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                var sl = $(param_id.school).select2({data: data.results});

                if (!init_configure.init_school) {
                    sl.val(page_param.schoolId).trigger("change");
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
                        sl.val(page_param.collegeId).trigger("change");
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
                        sl.val(page_param.departmentId).trigger("change");
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
                    var sl = $(param_id.science).select2({data: data.results});

                    if (!init_configure.init_science) {
                        sl.val(page_param.scienceId).trigger("change");
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
                    var sl = $(param_id.grade).select2({data: data.results});

                    if (!init_configure.init_grade) {
                        sl.val(page_param.gradeId).trigger("change");
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
                    var sl = $(param_id.organize).select2({data: data.results});

                    if (!init_configure.init_organize) {
                        sl.val(page_param.organizeId).trigger("change");
                        init_configure.init_organize = true;
                    }
                });
            } else {
                $(param_id.organize).html('<option label="请选择班级"></option>');
            }
        }

        function initSex() {
            $(param_id.sex).val(page_param.sex);
        }

        function initNation() {
            $.get(ajax_url.obtain_nation_data, function (data) {
                var sl = $(param_id.nationId).select2({
                    data: data.results
                });

                sl.val(page_param.nationId).trigger("change");
            });
        }

        function initPoliticalLandscapeId() {
            $.get(ajax_url.obtain_political_landscape_data, function (data) {
                $(param_id.politicalLandscapeId).html('<option label="请选择政治面貌"></option>');
                $.each(data.results, function (i, n) {
                    $(param_id.politicalLandscapeId).append('<option value="' + n.id + '">' + n.text + '</option>');
                });
                $(param_id.politicalLandscapeId).val(page_param.politicalLandscapeId);
            });
        }

        $(button_id.saveSchool.id).click(function () {
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

        $(param_id.birthday).flatpickr({
            "locale": "zh",
            defaultDate: _.trim($(param_id.birthday).val()) !== '' ? moment(_.trim($(param_id.birthday).val()), "YYYY-MM-DD").toDate() : moment((new Date().getFullYear() - 26) + "-01-07", "YYYY-MM-DD").toDate()
        });

        $(button_id.info.id).click(function () {
            initParam();
            validStudentNumber();
        });

        function validStudentNumber() {
            var studentNumber = param.studentNumber;
            if (studentNumber !== '') {
                if (tools.regex.studentNumber.test(studentNumber)) {
                    $.get(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.studentNumber);
                            validDormitoryNumber();
                        } else {
                            tools.validErrorDom(param_id.studentNumber, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                }
            } else {
                tools.validErrorDom(param_id.studentNumber, '学号不能为空');
            }
        }

        function validDormitoryNumber() {
            var dormitoryNumber = param.dormitoryNumber;
            if (dormitoryNumber !== '') {
                if (tools.regex.dormitoryNumber.test(dormitoryNumber)) {
                    tools.validSuccessDom(param_id.dormitoryNumber);
                    validParentName();
                } else {
                    tools.validErrorDom(param_id.dormitoryNumber, '宿舍号格式不正确');
                }
            } else {
                tools.validSuccessDom(param_id.dormitoryNumber);
                validParentName();
            }
        }

        function validParentName() {
            var parentName = param.parentName;
            if (parentName !== '') {
                if (parentName.length <= 10) {
                    tools.validSuccessDom(param_id.parentName);
                    validParentContactPhone();
                } else {
                    tools.validErrorDom(param_id.parentName, '家长姓名为10个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.parentName);
                validParentContactPhone();
            }
        }

        function validParentContactPhone() {
            var parentContactPhone = param.parentContactPhone;
            if (parentContactPhone !== '') {
                if (tools.regex.mobile.test(parentContactPhone)) {
                    tools.validSuccessDom(param_id.parentContactPhone);
                    validFamilyResidence();
                } else {
                    tools.validErrorDom(param_id.parentContactPhone, '家长联系电话格式不正确');
                }
            } else {
                tools.validSuccessDom(param_id.parentContactPhone);
                validFamilyResidence();
            }
        }

        function validFamilyResidence() {
            var familyResidence = param.familyResidence;
            if (familyResidence !== '') {
                if (familyResidence.length <= 200) {
                    tools.validSuccessDom(param_id.familyResidence);
                    validPlaceOrigin();
                } else {
                    tools.validErrorDom(param_id.familyResidence, '家庭居住地为200个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.familyResidence);
                validPlaceOrigin();
            }
        }

        function validPlaceOrigin() {
            var placeOrigin = param.placeOrigin;
            if (placeOrigin !== '') {
                if (placeOrigin.length <= 200) {
                    tools.validSuccessDom(param_id.placeOrigin);
                    updateInfo();
                } else {
                    tools.validErrorDom(param_id.placeOrigin, '生源地为200个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.placeOrigin);
                updateInfo();
            }
        }

        function updateInfo() {
            // 显示遮罩
            tools.buttonLoading(button_id.info.id, button_id.info.tip);
            $.post(ajax_url.student_update_info, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.info.id, button_id.info.text);
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