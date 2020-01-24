//# sourceURL=organize_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            update: web_path + '/web/data/organize/update',
            check_name: web_path + '/web/data/organize/check/edit/name',
            check_staff: web_path + '/web/data/organize/check/add/staff',
            page: '/web/menu/data/organize'
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
            science: '#science',
            grade: '#grade',
            organizeName: '#organizeName',
            staff:'#staff'
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
            gradeId: '',
            organizeId:'',
            organizeName: '',
            staff:'',
            organizeIsDel: ''
        };

        var page_param = {
            paramSchoolId:$('#paramSchoolId').val(),
            paramCollegeId:$('#paramCollegeId').val(),
            paramDepartmentId:$('#paramDepartmentId').val(),
            paramScienceId:$('#paramScienceId').val(),
            paramGradeId:$('#paramGradeId').val(),
            paramOrganizeId:$('#paramOrganizeId').val(),
            collegeId: $('#collegeId').val()
        };

        var init_configure = {
            init_school: false,
            init_college: false,
            init_department:false,
            init_science:false,
            init_grade:false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.schoolId = $(param_id.schoolId).val();
            if (Number(page_param.collegeId) === 0) {
                param.collegeId = $(param_id.college).val();
            } else {
                param.collegeId = page_param.collegeId;
            }
            param.departmentId = $(param_id.department).val();
            param.scienceId = $(param_id.science).val();
            param.gradeId = $(param_id.grade).val();
            param.organizeId = page_param.paramOrganizeId;
            param.organizeName = _.trim($(param_id.organizeName).val());
            param.staff = _.trim($(param_id.staff).val());
            var organizeIsDel = $('input[name="organizeIsDel"]:checked').val();
            param.organizeIsDel = _.isUndefined(organizeIsDel) ? 0 : organizeIsDel;
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
                    var sl =  $(param_id.college).select2({data: data.results});
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

        function initScience(departmentId) {
            if (Number(departmentId) > 0) {
                $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                    $(param_id.science).html('<option label="请选择专业"></option>');
                    var sl =  $(param_id.science).select2({data: data.results});
                    if (!init_configure.init_science) {
                        sl.val(page_param.paramScienceId).trigger("change");
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
                    var sl =  $(param_id.grade).select2({data: data.results});
                    if (!init_configure.init_grade) {
                        sl.val(page_param.paramGradeId).trigger("change");
                        init_configure.init_grade = true;
                    }
                });
            } else {
                $(param_id.grade).html('<option label="请选择年级"></option>');
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
            $(param_id.organizeName).maxlength({
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
            initScience(0);
            initGrade(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();
            initDepartment(v);
            initScience(0);
            initGrade(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        $(param_id.department).change(function () {
            var v = $(this).val();
            initScience(v);
            initGrade(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
            }
        });

        $(param_id.science).change(function () {
            var v = $(this).val();
            initGrade(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.science);
            }
        });

        $(param_id.grade).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.grade);
            }
        });

        $(param_id.organizeName).blur(function () {
            initParam();
            var organizeName = param.organizeName;
            if (organizeName.length <= 0 || organizeName.length > 200) {
                tools.validErrorDom(param_id.organizeName, '班级名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.organizeName);
                    } else {
                        tools.validErrorDom(param_id.organizeName, data.msg);
                    }
                });
            }
        });

        $(param_id.staff).blur(function () {
            initParam();
            var staff = param.staff;
            if (staff.length > 0) {
                $.post(ajax_url.check_staff, param, function (data) {
                    if (data.state) {
                        $('#staffHelp').text(data.staff.realName + " " + data.staff.mobile);
                        tools.validSuccessDom(param_id.staff);
                    } else {
                        $('#staffHelp').text('');
                        tools.validErrorDom(param_id.staff, data.msg);
                    }
                });
            } else {
                $('#staffHelp').text('');
                tools.validSuccessDom(param_id.staff);
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
                validScienceId();
            }
        }

        /**
         * 检验专业id
         */
        function validScienceId() {
            var scienceId = param.scienceId;
            if (Number(scienceId) <= 0) {
                tools.validSelect2ErrorDom(param_id.science, '请选择专业');
            } else {
                tools.validSelect2SuccessDom(param_id.science);
                validGradeId();
            }
        }

        function validGradeId() {
            var grade = param.grade;
            if (Number(grade) <= 0) {
                tools.validSelect2ErrorDom(param_id.grade, '请选择年级');
            } else {
                tools.validSelect2SuccessDom(param_id.grade);
                validOrganizeName();
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validOrganizeName() {
            var organizeName = param.organizeName;
            if (organizeName.length <= 0 || organizeName.length > 200) {
                tools.validErrorDom(param_id.organizeName, '班级名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.organizeName);
                        validStaff();
                    } else {
                        tools.validErrorDom(param_id.organizeName, data.msg);
                    }
                });
            }
        }

        function validStaff(){
            var staff = param.staff;
            if (staff.length > 0) {
                $.post(ajax_url.check_staff, param, function (data) {
                    if (data.state) {
                        $('#staffHelp').text(data.staff.realName + " " + data.staff.mobile);
                        tools.validSuccessDom(param_id.staff);
                        sendAjax();
                    } else {
                        $('#staffHelp').text('');
                        tools.validErrorDom(param_id.staff, data.msg);
                    }
                });
            } else {
                tools.validSuccessDom(param_id.staff);
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