//# sourceURL=timetable_course_new_edu_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "check.all", "select2-zh-CN"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/campus/timetable/course/new-edu/data',
            save: web_path + '/web/campus/timetable/course/batch-save',
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
            username: '#username',
            password: '#password'
        };

        var button_id = {
            loginQuery: {
                id: '#loginQuery',
                text: '登录查询',
                tip: '查询中...'
            },
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
            username: '',
            password: '',
            data: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.campusCourseReleaseId = page_param.campusCourseReleaseId;
            param.username = _.trim($(param_id.username).val());
            param.password = _.trim($(param_id.password).val());
        }

        $(button_id.loginQuery.id).click(function (){
           initParam();
            validUsername();
        });

        function validUsername() {
            var username = param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
                validPassword();
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        }

        function validPassword() {
            var password = param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
                initData();
            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        }

        var globalData = [];

        function initData() {
            $('#globalError').val('');
            $.get(ajax_url.data, param, function (data) {
                if(data.state){
                    globalData = data.listResult;
                    generateDataHtml(data);
                } else {
                    $('#globalError').val(data.msg);
                }
            });
        }

        function generateDataHtml(data) {
            var template = Handlebars.compile($("#data-template").html());
            $('#selectData').html(template(data));
            // 调用全选插件
            $.fn.check({checkall_name: "checkall", checkbox_name: "check"});
        }

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            buildParam();
        });

        /**
         * 组装数据
         */
        function buildParam() {
            var data = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                var id = $(ids[i]).val();
                var g = globalData[id];
                var weekDay = getWeekDay(g.week);
                if (weekDay > 0 && g.attendClass !== '' && g.courseName !== '') {
                    data.push({
                        campusCourseReleaseId: page_param.campusCourseReleaseId,
                        organizeName: g.attendClass,
                        courseName: g.courseName,
                        buildingName: g.classroom,
                        startWeek: g.openDate,
                        endWeek: g.closeDate,
                        startTime: getTime(g.classTime),
                        endTime: getTime(g.overTime),
                        teacherName: g.teacherName,
                        weekDay: weekDay
                    });
                }
            }

            if (data.length > 0) {
                sendAjax(data);
            } else {
                Messenger().post("未发现有选中的课程!");
            }
        }

        function getWeekDay(week) {
            var w = 0;
            if (_.trim(week) === '星期一') {
                w = 1;
            }

            if (_.trim(week) === '星期二') {
                w = 2;
            }

            if (_.trim(week) === '星期三') {
                w = 3;
            }

            if (_.trim(week) === '星期四') {
                w = 4;
            }

            if (_.trim(week) === '星期五') {
                w = 5;
            }

            if (_.trim(week) === '星期六') {
                w = 6;
            }

            if (_.trim(week) === '星期天') {
                w = 7;
            }

            return w;
        }

        function getTime(t) {
            var v = '';
            if (t !== '') {
                var trr = t.split(":")[0];
                if (Number(trr) < 10) {
                    v = "0" + t + ":00";
                } else {
                    v = t + ":00";
                }
            }
            return v;
        }

        /**
         * 发送数据到后台
         */
        function sendAjax(data) {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.save,
                data: {data: JSON.stringify(data)},
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