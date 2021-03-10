//# sourceURL=calendar_look.js
require(["jquery", "tools", "nav.active", "jquery.address", "js-year-calendar.zh-CN", "select2-zh-CN"],
    function ($, tools, navActive) {
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
                        $('#week').html('<span class="tx-bold tx-20 tx-primary">' + tools.weekDay(calendar.week) + '</span>');
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
            var nowDateArr = calendar.nowDate.split('-');
            var startDateArr = calendar.startDate.split('-');
            var endDateArr = calendar.endDate.split('-');
            var holidayStartDateArr = calendar.holidayStartDate.split('-');
            var holidayEndDateArr = calendar.holidayEndDate.split('-');
            new Calendar('#calendar', {
                language: 'zh-CN',
                style: 'background',
                dataSource: [
                    {
                        startDate: new Date(Number(startDateArr[0]), Number(startDateArr[1]) - 1, Number(startDateArr[2])),
                        endDate: new Date(Number(endDateArr[0]), Number(endDateArr[1]) - 1, Number(endDateArr[2]))
                    },
                    {
                        startDate: new Date(Number(holidayStartDateArr[0]), Number(holidayStartDateArr[1]) - 1, Number(holidayStartDateArr[2])),
                        endDate: new Date(Number(holidayEndDateArr[0]), Number(holidayEndDateArr[1]) - 1, Number(holidayEndDateArr[2]))
                    },
                    {
                        startDate: new Date(Number(nowDateArr[0]), Number(nowDateArr[1]) - 1, Number(nowDateArr[2])),
                        endDate: new Date(Number(nowDateArr[0]), Number(nowDateArr[1]) - 1, Number(nowDateArr[2]))
                    }
                ]
            });
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