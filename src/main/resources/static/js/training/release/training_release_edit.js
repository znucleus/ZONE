//# sourceURL=training_release_edit.js
require(["jquery", "lodash", "tools", "moment-with-locales", "handlebars", "sweetalert2",
        "nav.active", "messenger", "jquery.address", "select2-zh-CN", "flatpickr-zh", "bootstrap-maxlength"],
    function ($, _, tools, moment, Handlebars, Swal, navActive) {

        moment.locale('zh-cn');

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_course_data: web_path + '/users/data/course',
            update: web_path + '/web/training/release/update',
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
            trainingReleaseId: '',
            title: '',
            courseId: '',
            startDate: '',
            endDate: ''
        };

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val(),
            paramCourseId: $('#paramCourseId').val(),
            paramCollegeId: $('#paramCollegeId').val()
        };

        var init_configure = {
            init_course: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.trainingReleaseId = page_param.paramTrainingReleaseId;
            param.title = _.trim($(param_id.title).val());
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
            initCourse(page_param.paramCollegeId);
            initSelect2();
            initMaxLength();
        }

        function initCourse(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_course_data, {collegeId: collegeId}, function (data) {
                    $(param_id.course).html('<option label="请选择课程"></option>');
                    var sl = $(param_id.course).select2({data: data.results});

                    if (!init_configure.init_course) {
                        sl.val(page_param.paramCourseId).trigger("change");
                        init_configure.init_course = true;
                    }
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