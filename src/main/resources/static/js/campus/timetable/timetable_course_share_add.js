//# sourceURL=timetable_course_share_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "check.all"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            courses: web_path + '/anyone/campus/timetable/share-courses',
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
            shareId: '#shareId'
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
            shareId: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.shareId = $(param_id.shareId).val();
        }

        var globalData = [];

        $('#search').click(function () {
            initParam();
            $.get(ajax_url.courses + '/' + param.shareId, function (data) {
                if (data.state) {
                    var term = data.release.term;
                    var t;
                    if (Number(term) === 0) {
                        t = '上学期';
                    } else if (Number(term) === 1) {
                        t = '下学期';
                    }
                    $('#shareIdHelp').text('由 ' + data.release.publisher + ' 分享，分享次数: ' + data.release.shareNumber + ' 学期: ' + data.release.startYear + '~' + data.release.endYear + ' ' + t);
                    tools.validCustomerSingleSuccessDom(param_id.shareId);
                    globalData = data.listResult;
                    generateDataHtml(data);
                } else {
                    tools.validCustomerSingleErrorDom(param_id.shareId, data.msg);
                    $('#shareIdHelp').text('');
                }
            });
        });


        function generateDataHtml(data) {
            var template = Handlebars.compile($("#data-template").html());
            Handlebars.registerHelper('weekDay', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.weekday(this.weekDay)));
            });
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
                data.push({
                    campusCourseReleaseId: page_param.campusCourseReleaseId,
                    organizeName: g.organizeName,
                    courseName: g.courseName,
                    buildingName: g.buildingName,
                    startWeek: g.startWeek,
                    endWeek: g.endWeek,
                    startTime: g.startTime,
                    endTime: g.endTime,
                    teacherName: g.teacherName,
                    weekDay: g.weekDay
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