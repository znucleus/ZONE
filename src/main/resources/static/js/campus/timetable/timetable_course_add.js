//# sourceURL=timetable_import.js
require(["jquery", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "flatpickr-zh"],
    function ($, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/campus/timetable/course/save',
            page: '/web/menu/campus/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var page_param = {
            campusCourseReleaseId: $('#campusCourseReleaseId').val()
        };

        /*
         参数id
         */
        var param_id = {
            organizeName: '#organizeName',
            courseName: '#courseName',
            buildingName: '#buildingName',
            startWeek: '#startWeek',
            endWeek: '#endWeek',
            weekday: '#weekday',
            startTime: '#startTime',
            endTime: '#endTime',
            teacherName: '#teacherName',
            bgColor: '#bgColor',
            remark: '#remark'
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
            campusCourseReleaseId: '',
            organizeName: '',
            courseName: '',
            buildingName: '',
            startWeek: '',
            endWeek: '',
            weekday: '',
            startTime: '',
            endTime: '',
            teacherName: '',
            bgColor: '',
            remark: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.campusCourseReleaseId = page_param.campusCourseReleaseId;
            param.organizeName = $(param_id.organizeName).val();
            param.courseName = $(param_id.courseName).val();
            param.buildingName = $(param_id.buildingName).val();
            param.startWeek = $(param_id.startWeek).val();
            param.endWeek = $(param_id.endWeek).val();
            param.weekday = $(param_id.weekday).val();

            var startTime = $(param_id.startTime).val();
            if (startTime.length > 0) {
                param.startTime = startTime + ":00";
            } else {
                param.startTime = startTime;
            }

            var endTime = $(param_id.endTime).val();
            if (endTime.length > 0) {
                param.endTime = endTime + ":00";
            } else {
                param.endTime = endTime;
            }

            param.teacherName = $(param_id.teacherName).val();
            param.bgColor = $(param_id.bgColor).val();
            param.remark = $(param_id.remark).val();
        }

        $('.flatpickr').flatpickr({
            locale: "zh",
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            time_24hr: true
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validOrganizeName();
        });

        /**
         * 检验班级名
         */
        function validOrganizeName() {
            var organizeName = param.organizeName;
            if (organizeName.length <= 0 || organizeName.length > 500) {
                tools.validErrorDom(param_id.organizeName, '班级名500个字符以内');
            } else {
                tools.validSuccessDom(param_id.organizeName);
                validCourseName();
            }
        }

        /**
         * 检验课程名
         */
        function validCourseName() {
            var courseName = param.courseName;
            if (courseName.length <= 0 || courseName.length > 220) {
                tools.validErrorDom(param_id.courseName, '课程名220个字符以内');
            } else {
                tools.validSuccessDom(param_id.courseName);
                validWeekday();
            }
        }

        /**
         * 检验星期
         */
        function validWeekday() {
            var weekday = param.weekday;
            if (Number(weekday) <= 0) {
                tools.validErrorDom(param_id.weekday, '请选择上课星期');
            } else {
                tools.validSuccessDom(param_id.weekday);
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