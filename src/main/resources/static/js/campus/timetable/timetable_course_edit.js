//# sourceURL=timetable_course_edit.js
require(["jquery", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "flatpickr-zh"],
    function ($, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/campus/timetable/course/update',
            page: '/web/menu/campus/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var page_param = {
            campusCourseDataId: $('#campusCourseDataId').val(),
            weekDay: $('#weekDayParam').val(),
            bgColor: $('#bgColorParam').val()
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
            weekDay: '#weekDay',
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
            campusCourseDataId: '',
            organizeName: '',
            courseName: '',
            buildingName: '',
            startWeek: '',
            endWeek: '',
            weekDay: '',
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
            param.campusCourseDataId = page_param.campusCourseDataId;
            param.organizeName = $(param_id.organizeName).val();
            param.courseName = $(param_id.courseName).val();
            param.buildingName = $(param_id.buildingName).val();
            param.startWeek = $(param_id.startWeek).val();
            param.endWeek = $(param_id.endWeek).val();
            param.weekDay = $(param_id.weekDay).val();

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

        init();

        function init() {
            initData();
        }

        function initData() {
            $('#weekDay').val(page_param.weekDay);
            $('#bgColor').val(page_param.bgColor);
        }

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
            if (organizeName.length <= 0 || organizeName.length > 50) {
                tools.validErrorDom(param_id.organizeName, '班级名50个字符以内');
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
            if (courseName.length <= 0 || courseName.length > 80) {
                tools.validErrorDom(param_id.courseName, '课程名80个字符以内');
            } else {
                tools.validSuccessDom(param_id.courseName);
                validWeekDay();
            }
        }

        /**
         * 检验星期
         */
        function validWeekDay() {
            var weekDay = param.weekDay;
            if (Number(weekDay) <= 0) {
                tools.validErrorDom(param_id.weekDay, '请选择上课星期');
            } else {
                tools.validSuccessDom(param_id.weekDay);
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