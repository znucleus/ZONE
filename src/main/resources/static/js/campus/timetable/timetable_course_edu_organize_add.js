//# sourceURL=timetable_course_edu_organize_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "check.all", "select2-zh-CN"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            attend_class: web_path + '/web/educational/timetable/attend_class',
            uniques: web_path + '/web/educational/timetable/uniques',
            data: web_path + '/web/educational/timetable/search',
            save: web_path + '/web/campus/timetable/course/batch_save',
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
            dataTime: '#dataTime',
            attendClass: '#attendClass'
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
            dataTime: '',
            attendClass: '',
            data: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.campusCourseReleaseId = page_param.campusCourseReleaseId;
            param.dataTime = $(param_id.dataTime).val();
            param.attendClass = $(param_id.attendClass).val();
        }


        init();

        /**
         * 初始化数据
         */
        function init() {
            initDataTime();
            initSelect2();
        }

        function initDataTime() {
            tools.dataLoading();
            $.get(ajax_url.uniques, function (data) {
                tools.dataEndLoading();
                if (data.state) {
                    $(param_id.dataTime).html('<option label="请选择学年学期"></option>');
                    var arr = [];
                    for (var i = 0; i < data.listResult.length; i++) {
                        var startYear = data.listResult[i].startYear;
                        var semester = data.listResult[i].semester;
                        var identification = data.listResult[i].identification;
                        var se = '下学期';
                        if (semester === '1') {
                            se = '上学期';
                        }

                        arr.push({
                            id: identification,
                            text: startYear + ' ' + se
                        });
                    }

                    $(param_id.dataTime).select2({data: arr});
                }
            });
        }

        function initAttendClass(identification) {
            if (identification !== '') {
                $.get(ajax_url.attend_class + '/' + identification, function (data) {
                    $(param_id.attendClass).html('<option label="请选择班级"></option>');
                    $(param_id.attendClass).select2({data: data.results});
                });
            } else {
                $(param_id.attendClass).html('<option label="请选择班级"></option>');
            }
        }

        var globalData = [];

        function initData(identification, attendClass) {
            if (identification !== '' && attendClass !== '') {
                $.get(ajax_url.data, {
                    extraSearch: JSON.stringify({
                        identification: identification,
                        attendClass: attendClass
                    })
                }, function (data) {
                    globalData = data.listResult;
                    generateDataHtml(data);
                });
            } else {
                globalData = [];
                $('#selectData').empty();
            }
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(param_id.dataTime).change(function () {
            var v = $(this).val();
            initAttendClass(v);
            initData('', '');
        });

        $(param_id.attendClass).change(function () {
            initParam();
            var v = $(this).val();
            initData(param.dataTime, v);
        });

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
            if(t !== ''){
                var trr = t.split(":")[0];
                if(Number(trr) < 10){
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