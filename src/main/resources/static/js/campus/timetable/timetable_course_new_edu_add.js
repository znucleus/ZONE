//# sourceURL=timetable_course_new_edu_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "check.all", "select2-zh-CN"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            semesters: web_path + '/web/campus/timetable/new-edu/semesters',
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
            password: '#password',
            schoolYear: '#schoolYear'
        };

        var button_id = {
            query: {
                id: '#query',
                text: '查询',
                tip: '查询中...'
            },
            okSchoolYear: {
                id: '#okSchoolYear',
                text: '确定',
                tip: '查询中...'
            },
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            },
            cancel: {
                id: '#cancel'
            }
        };

        /*
         参数
         */
        var param = {
            username: '',
            password: '',
            schoolYear: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.password = _.trim($(param_id.password).val());
            param.schoolYear = $(param_id.schoolYear).val();
        }

        $(param_id.username).blur(function () {
            initParam();
            var username = param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        });

        $(param_id.password).blur(function () {
            initParam();
            var password = param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        });

        init();

        function init() {
            initSelect2();
        }

        $(button_id.query.id).click(function () {
            initParam();
            validUsername('query');
        });

        function initSchoolYear() {
            $('#queryError').text('');
            tools.buttonLoading(button_id.query.id, button_id.query.tip);
            $.get(ajax_url.semesters, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.query.id, button_id.query.text);
                if (data.results.length > 0) {
                    var schoolYearSelect2 = $(param_id.schoolYear).select2({data: data.results});
                    $.each(data.results, function (i, v) {
                        if (v.selected) {
                            schoolYearSelect2.val(v.id).trigger("change");
                        }
                    });
                    $('#saveForm').css('display', '');
                    $('#queryForm').css('display', 'none');
                    $('#appForm').css('display', 'none');
                } else {
                    $('#queryError').text('未查询到数据，请检查账号密码或稍后重试');
                }
            });

        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(button_id.okSchoolYear.id).click(function () {
            initParam();
            validUsername('save');
        });

        $(button_id.cancel.id).click(function () {
            $('#saveForm').css('display', 'none');
            $('#appForm').css('display', 'none');
            $('#queryForm').css('display', '');
        });

        function validUsername(type) {
            var username = param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
                validPassword(type);
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        }

        function validPassword(type) {
            var password = param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
                if (type === 'save') {
                    validSchoolYear();
                } else {
                    initSchoolYear();
                }

            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        }

        function validSchoolYear() {
            var schoolYear = param.schoolYear;
            if (schoolYear !== '') {
                tools.validSuccessDom(param_id.password);
                queryData();
            } else {
                tools.validErrorDom(param_id.password, '请选择学年学期');
            }
        }


        var globalData = [];

        function queryData() {
            $('#saveError').text('');
            tools.buttonLoading(button_id.okSchoolYear.id, button_id.okSchoolYear.tip);
            $.get(ajax_url.data, param, function (data) {
                tools.buttonEndLoading(button_id.okSchoolYear.id, button_id.okSchoolYear.text);
                if (data.state) {
                    globalData = data.listResult;
                    generateDataHtml(data);
                    $('#saveForm').css('display', '');
                    $('#appForm').css('display', '');
                } else {
                    $('#saveError').text(data.msg);
                }
            });
        }

        function generateDataHtml(data) {
            var template = Handlebars.compile($("#data-template").html());
            Handlebars.registerHelper('weekday', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.weekday(Number(this.weekday))));
            });
            $('#selectData').html(template(data));
            // 调用全选插件
            $.fn.check({checkall_name: "checkall", checkbox_name: "check"});
            $('#checkall').prop('checked', false);
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
                data.push({
                    campusCourseReleaseId: page_param.campusCourseReleaseId,
                    organizeName: g.lessonName,
                    courseName: g.courseName,
                    buildingName: g.room,
                    startWeek: g.startWeek,
                    endWeek: g.endWeek,
                    startTime: g.startTime,
                    endTime: g.endTime,
                    teacherName: g.teachers,
                    weekday: g.weekday
                });
            }

            if (data.length > 0) {
                sendAjax(data);
            } else {
                Messenger().post("未发现有选中的课程!");
            }
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