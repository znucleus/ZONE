//# sourceURL=calendar_look.js
require(["jquery", "tools", "nav.active", "moment", "jquery.address", "select2-zh-CN"],
    function ($, tools, navActive, moment) {
        /*
         ajax url.
        */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_school_calendar_data: web_path + '/web/educational/calendars',
            data: web_path + '/web/educational/calendar/look',
            list: '/web/educational/calendar/list',
            authorize: '/web/educational/calendar/authorize/add',
            page: '/web/menu/educational/calendar'
        };

        navActive(ajax_url.page);

        moment.locale('zh-cn');

        /*
        参数id
        */
        var param_id = {
            school: '#school',
            college: '#college',
            schoolCalendar: '#schoolCalendar'
        };

        var page_param = {
            collegeId: $('#collegeId').val()
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL: 'CALENDAR_SCHOOL_SEARCH',
            COLLEGE: 'CALENDAR_COLLEGE_SEARCH',
            SCHOOL_CALENDAR: 'CALENDAR_SCHOOL_CALENDAR_SEARCH'
        };

        var init_configure = {
            init_college: false,
            init_school_calendar: false
        };

        init();

        function init() {
            if (Number(page_param.collegeId) > 0) {
                initSchoolCalendar(page_param.collegeId);
            } else {
                initSchool();
            }
            initSelect2();
            initData();
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                var sl = $(param_id.school).select2({
                    data: data.results
                });

                var schoolId = null;
                if (localStorage) {
                    schoolId = localStorage.getItem(webStorageKey.SCHOOL);
                }
                if (schoolId !== null) {
                    sl.val(schoolId).trigger("change");
                }

            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    var sl = $(param_id.college).select2({data: data.results});
                    if (!init_configure.init_college) {
                        var collegeId = null;
                        if (localStorage) {
                            collegeId = localStorage.getItem(webStorageKey.COLLEGE);
                        }
                        if (collegeId !== null) {
                            sl.val(collegeId).trigger("change");
                        }
                        init_configure.init_college = true;
                    }
                });
            } else {
                $(param_id.college).html('<option label="请选择院"></option>');
            }
        }

        function initSchoolCalendar(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_school_calendar_data, {collegeId: collegeId}, function (data) {
                    $(param_id.schoolCalendar).html('<option label="请选择校历"></option>');
                    var sl = $(param_id.schoolCalendar).select2({data: data.results});

                    if (!init_configure.init_school_calendar) {


                        var schoolCalendar = null;
                        if (localStorage) {
                            schoolCalendar = localStorage.getItem(webStorageKey.SCHOOL_CALENDAR);
                        } else {
                            if (data.results.length > 0) {
                                sl.val(data.results[0].id).trigger("change");
                            }
                        }
                        if (schoolCalendar !== null) {
                            sl.val(schoolCalendar).trigger("change");
                        } else {
                            if (data.results.length > 0) {
                                sl.val(data.results[0].id).trigger("change");
                            }
                        }
                        init_configure.init_school_calendar = true;
                    }
                });
            } else {
                $(param_id.schoolCalendar).html('<option label="请选择校历"></option>');
            }
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);

            if (localStorage) {
                localStorage.setItem(webStorageKey.SCHOOL, v);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();
            initSchoolCalendar(v);

            if (localStorage) {
                localStorage.setItem(webStorageKey.COLLEGE, v);
            }
        });

        $(param_id.schoolCalendar).change(function () {
            var v = $(this).val();
            initData(v);

            if (localStorage) {
                localStorage.setItem(webStorageKey.SCHOOL_CALENDAR, v);
            }
        });

        function initData(calendarId) {
            if (calendarId && calendarId.length > 0) {
                $.get(ajax_url.data, {calendarId: calendarId}, function (data) {
                    if (data.state) {
                        var calendar = data.calendar;
                        $('#title').text(calendar.title);
                        $('#schoolName').text('学校：' + calendar.schoolName);
                        $('#collegeName').text('院：' + calendar.collegeName);
                        var semester = '';
                        if (calendar.semester === 1) {
                            semester = '第一学期';
                        } else if (calendar.semester === 2) {
                            semester = '第二学期';
                        } else if (calendar.semester === 3) {
                            semester = '第三学期';
                        }
                        $('#schoolYear').text('学年：' + calendar.schoolYear + ' ' + semester);
                        $('#openDate').text('开学：' + calendar.startDate + ' ~ ' + calendar.endDate + ' 共' + calendar.openWeeks + '周');
                        $('#holidayDate').text('放假：' + calendar.holidayStartDate + ' ~ ' + calendar.holidayEndDate + ' 共' + calendar.holidayWeeks + '周');
                        var nowDate = calendar.nowDate.split('-');
                        $('#nowDate').html('<span class="tx-bold tx-20 tx-primary">' + nowDate[0] + '</span> 年 <span class="tx-bold tx-20 tx-primary">' + nowDate[1] + '</span> 月 <span class="tx-bold tx-20 tx-primary">' + nowDate[2] + '</span> 日')
                        $('#week').html('<span class="tx-bold tx-20 tx-primary">' + tools.weekday(calendar.week) + '</span>');
                        $('#weeks').html('第 <span class="tx-bold tx-20 tx-purple"><em >' + calendar.weeks + '</em></span> 周');
                        $('#remark').text('备注：' + calendar.remark);
                        $('#releaseTimeStr').text(calendar.releaseTimeStr);

                        initCalendar(calendar);
                    } else {
                        $('#title').text('');
                        $('#schoolName').text('');
                        $('#collegeName').text('');
                        $('#schoolYear').text('');
                        $('#openDate').text('');
                        $('#holidayDate').text('');
                        $('#nowDate').html('')
                        $('#week').html('');
                        $('#weeks').html('');
                        $('#remark').text('');
                        $('#releaseTimeStr').text('');
                    }
                });
            }

        }

        function initCalendar(calendar) {
            $('#data').empty();
            var weeks = 1;// 周数
            var m = moment(calendar.startDate);
            var ms = moment(calendar.startDate);
            var last = moment(calendar.endDate);
            var nowDate = moment(calendar.nowDate);
            var days = last.diff(m, 'days');
            var weekday = m.weekday();

            var wk = weekday === 0 ? 7 : weekday;

            days += (wk - 1);// 补足第一行天数
            ms.subtract(wk, 'd');
            var cols = 7;
            var rows = Math.ceil(days / cols);
            var dateStop = false;
            for (var i = 0; i < rows; i++) {
                var html = '<tr>';
                html += '<th scope="row">第' + weeks + '周</th>';
                for (var j = 0; j < cols; j++) {
                    if (i === 0 && j === 0) {
                        for (var k = 0; k < wk - 1; k++) {
                            ms.add(1, 'd');
                            html += '<th class="text-muted">' + ms.format('MM.DD') + '</th>';
                        }
                        j = wk - 1;
                    } else {
                        m.add(1, 'd');
                    }

                    if (dateStop) {
                        html += '<th class="text-muted">' + m.format('MM.DD') + '</th>';
                    } else {
                        dateStop = m.isSame(last);
                        if(m.isSame(nowDate)){
                            html += '<th class="text-white bg-primary">' + m.format('MM.DD') + '</th>';
                        } else {
                            var lastWeekday = m.weekday();
                            if(lastWeekday === 6 || lastWeekday === 0){
                                html += '<th class="text-success">' + m.format('MM.DD') + '</th>';
                            } else {
                                html += '<th>' + m.format('MM.DD') + '</th>';
                            }

                        }

                    }
                }

                html += '</tr>';
                $('#data').append(html);
                weeks++;
            }
        }


        $('#list').click(function () {
            $.address.value(ajax_url.list);
        });

        /*
       权限分配
       */
        $('#authorize').click(function () {
            $.address.value(ajax_url.authorize);
        });

    });