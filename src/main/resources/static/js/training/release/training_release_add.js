//# sourceURL=training_release_add.js
require(["jquery", "lodash", "tools", "moment-with-locales", "handlebars", "sweetalert2",
        "nav.active", "messenger", "jquery.address", "select2-zh-CN", "flatpickr-zh", "bootstrap-maxlength"],
    function ($, _, tools, moment, Handlebars, Swal, navActive) {

        moment.locale('zh-cn');

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/schools',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            obtain_organize_data: web_path + '/anyone/data/organize',
            obtain_course_data: web_path + '/users/data/course',
            save: web_path + '/web/training/release/save',
            page: '/web/menu/training/release'
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
            course: '#course',
            startDate: '#startDate',
            endDate: '#endDate'
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
            title: '',
            schoolId: '',
            collegeId: '',
            departmentId: '',
            scienceId: '',
            gradeId: '',
            organizeId: '',
            courseId: '',
            startDate: '',
            endDate: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.title = _.trim($(param_id.title).val());
            param.schoolId = $(param_id.school).val();
            param.collegeId = $(param_id.college).val();
            param.departmentId = $(param_id.department).val();
            param.scienceId = $(param_id.science).val();
            param.gradeId = $(param_id.grade).val();
            param.organizeId = $(param_id.organize).val();
            param.courseId = $(param_id.course).val();
            param.startDate = $(param_id.startDate).val();
            param.endDate = $(param_id.endDate).val();
        }

        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initSchool();
            initSelect2();
            initMaxLength();
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

        function initCourse(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_course_data, {collegeId: collegeId}, function (data) {
                    $(param_id.course).html('<option label="请选择课程"></option>');
                    $(param_id.course).select2({data: data.results});
                });
            } else {
                $(param_id.course).html('<option label="请选择课程"></option>');
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

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
            }
        });

        $(param_id.startDate).flatpickr({
            "locale": "zh"
        });

        $(param_id.endDate).flatpickr({
            "locale": "zh"
        });

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
            initCourse(v);

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

        $(param_id.course).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.course);
            }
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
                validSchoolId();
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
                validGradeId();
            }
        }

        function validGradeId() {
            var gradeId = param.gradeId;
            if (Number(gradeId) <= 0) {
                tools.validSelect2ErrorDom(param_id.grade, '请选择年级');
            } else {
                tools.validSelect2SuccessDom(param_id.grade);
                validOrganizeId();
            }
        }

        function validOrganizeId() {
            var organizeId = param.organizeId;
            if (Number(organizeId) <= 0) {
                tools.validSelect2ErrorDom(param_id.organize, '请选择班级');
            } else {
                tools.validSelect2SuccessDom(param_id.organize);
                validCourseId();
            }
        }

        function validCourseId() {
            var courseId = param.courseId;
            if (Number(courseId) <= 0) {
                tools.validSelect2ErrorDom(param_id.course, '请选择课程');
            } else {
                tools.validSelect2SuccessDom(param_id.course);
                validStartDate();
            }
        }

        function validStartDate() {
            var startDate = param.startDate;
            if (startDate.length <= 0) {
                tools.validErrorDom(param_id.startDate, '请选择开课时间');
            } else {
                tools.validSuccessDom(param_id.startDate);
                validEndDate();
            }
        }

        function validEndDate() {
            var endDate = param.endDate;
            if (endDate.length <= 0) {
                tools.validErrorDom(param_id.endDate, '请选择结课时间');
            } else {
                var startDate = param.startDate;
                if (moment(endDate).isSameOrAfter(startDate)) {
                    tools.validSuccessDom(param_id.endDate);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id.endDate, '结课时间应大于或等于开课时间');
                }
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