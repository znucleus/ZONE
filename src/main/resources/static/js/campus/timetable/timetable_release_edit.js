//# sourceURL=timetable_release_blank.js
require(["jquery", "tools", "moment-with-locales", "sweetalert2", "nav.active", "messenger", "select2-zh-CN", "jquery.address", "bootstrap-maxlength", "flatpickr-zh"],
    function ($, tools, moment, Swal, navActive) {
        /*
        ajax url.
        */
        var ajax_url = {
            update: web_path + '/web/campus/timetable/update',
            page: '/web/menu/campus/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
        参数id
        */
        var param_id = {
            title: '#title',
            schoolYear: '#schoolYear',
            semester: '#semester',
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

        var page_param = {
            campusCourseReleaseId: $('#campusCourseReleaseId').val(),
            schoolYear: $('#schoolYearParam').val(),
            semester: $('#semesterParam').val()
        };

        /*
         参数
         */
        var param = {
            campusCourseReleaseId: '',
            title: '',
            schoolYear: '',
            semester: '',
            startDate: '',
            endDate: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.campusCourseReleaseId = page_param.campusCourseReleaseId;
            param.title = $(param_id.title).val();
            param.schoolYear = $(param_id.schoolYear).val();
            param.semester = $(param_id.semester).val();
            param.startDate = $(param_id.startDate).val();
            param.endDate = $(param_id.endDate).val();
        }

        init();

        function init() {
            initMaxLength();
            initSchoolYear();
            initSemester();
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

        function initSchoolYear() {
            $(param_id.schoolYear).html('<option value="" label="请选择学年"></option>');
            var date = new Date();
            var year = date.getFullYear();
            var yearArr = [];
            for (var i = year + 2; i >= year - 6; i--) {
                yearArr.push({
                    id: (i - 1) + '-' + (i),
                    text: (i - 1) + '-' + (i) + '学年'
                });
            }

            var sl = $(param_id.schoolYear).select2({data: yearArr});
            sl.val(page_param.schoolYear).trigger("change");
        }

        function initSemester() {
            $(param_id.semester).val(page_param.semester);
        }

        $('.flatpickr').flatpickr({
            locale: "zh"
        });

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
            }
        });

        $(param_id.schoolYear).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.schoolYear);
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
                validSchoolYear();
            }
        }

        function validSchoolYear() {
            var schoolYear = param.schoolYear;
            if (schoolYear === '') {
                tools.validSelect2ErrorDom(param_id.schoolYear, '请选择学年');
            } else {
                tools.validSelect2SuccessDom(param_id.schoolYear);
                validSemester();
            }
        }

        function validSemester() {
            var semester = param.semester;
            if (semester === '') {
                tools.validErrorDom(param_id.semester, '请选择学期');
            } else {
                tools.validSuccessDom(param_id.semester);
                validStartDate();
            }
        }

        function validStartDate() {
            var startDate = param.startDate;
            if (startDate.length <= 0) {
                tools.validErrorDom(param_id.startDate, '请选择开学日期');
            } else {
                tools.validSuccessDom(param_id.startDate);
                validEndDate();
            }
        }

        function validEndDate() {
            var endDate = param.endDate;
            if (endDate.length <= 0) {
                tools.validErrorDom(param_id.endDate, '请选择结束日期');
            } else {
                var startDate = param.startDate;
                if (moment(endDate, 'YYYY-MM-DD').isSameOrAfter(moment(startDate, 'YYYY-MM-DD'))) {
                    tools.validSuccessDom(param_id.endDate);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id.endDate, '开始日期应小于或等于结束日期');
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