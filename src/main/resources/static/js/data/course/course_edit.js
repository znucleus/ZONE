//# sourceURL=course_edit.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            update: web_path + '/web/data/course/update',
            check_name: web_path + '/web/data/course/check-edit-name',
            page: '/web/menu/data/course'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            school: '#school',
            college: '#college',
            courseName: '#courseName',
            courseCredit: '#courseCredit',
            courseHours: '#courseHours',
            courseType: '#courseType',
            schoolYear: '#schoolYear',
            term: '#term',
            courseCode: '#courseCode',
            courseBrief: '#courseBrief'
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
            courseId: '',
            courseName: '',
            courseCredit: '',
            courseHours: '',
            courseType: '',
            schoolYear: '',
            term: '',
            courseCode: '',
            courseBrief: '',
            courseIsDel: ''
        };

        var page_param = {
            collegeId: $('#collegeId').val(),
            paramSchoolId: $('#paramSchoolId').val(),
            paramCollegeId: $('#paramCollegeId').val(),
            paramCourseId: $('#paramCourseId').val(),
            paramCourseType: $('#paramCourseType').val(),
            paramSchoolYear: $('#paramSchoolYear').val(),
            paramTerm: $('#paramTerm').val()
        };

        var init_configure = {
            init_school: false,
            init_college: false
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
            param.courseId = page_param.paramCourseId;
            param.courseName = _.trim($(param_id.courseName).val());
            param.courseCredit = _.trim($(param_id.courseCredit).val());
            param.courseHours = _.trim($(param_id.courseHours).val());
            param.courseType = $(param_id.courseType).val();
            param.schoolYear = $(param_id.schoolYear).val();
            param.term = $(param_id.term).val();
            param.courseCode = _.trim($(param_id.courseCode).val());
            param.courseBrief = _.trim($(param_id.courseBrief).val());
            var courseIsDel = $('input[name="courseIsDel"]:checked').val();
            param.courseIsDel = _.isUndefined(courseIsDel) ? 0 : courseIsDel;
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
                initSelect2();
            }
            initMaxLength();
            initCourseType();
            initSchoolYear();
            initTerm();
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

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.courseName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.courseBrief).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        function initCourseType() {
            $(param_id.courseType).val(page_param.paramCourseType);
        }

        function initSchoolYear() {
            $(param_id.schoolYear).val(page_param.paramSchoolYear);
        }

        function initTerm() {
            $(param_id.term).val(page_param.paramTerm);
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        $(param_id.courseName).blur(function () {
            initParam();
            var courseName = param.courseName;
            if (courseName.length <= 0 || courseName.length > 100) {
                tools.validErrorDom(param_id.courseName, '课程名100个字符以内');
            } else {
                $.get(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.courseName);
                    } else {
                        tools.validErrorDom(param_id.courseName, data.msg);
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
                validCourseName();
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
                validCourseName();
            }
        }

        function validCourseName() {
            var courseName = param.courseName;
            if (courseName.length <= 0 || courseName.length > 100) {
                tools.validErrorDom(param_id.courseName, '课程名100个字符以内');
            } else {
                $.get(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.courseName);
                        validCourseType();
                    } else {
                        tools.validErrorDom(param_id.courseName, data.msg);
                    }
                });
            }
        }

        function validCourseType() {
            var courseType = param.courseType;
            if (Number(courseType) >= 0) {
                tools.validSuccessDom(param_id.courseType);
                validSchoolYear();
            } else {
                tools.validErrorDom(param_id.courseType, '请选择课程类型');
            }
        }

        function validSchoolYear() {
            var schoolYear = param.schoolYear;
            if (Number(schoolYear) >= 0) {
                tools.validSuccessDom(param_id.schoolYear);
                validTerm();
            } else {
                tools.validErrorDom(param_id.schoolYear, '请选择年级');
            }
        }

        function validTerm() {
            var term = param.term;
            if (Number(term) >= 0) {
                tools.validSuccessDom(param_id.term);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.term, '请选择学期');
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