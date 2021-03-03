//# sourceURL=timetable_look.js
require(["jquery", "tools", "handlebars", "nav.active", "sweetalert2", "jquery.address", "select2-zh-CN", "messenger"],
    function ($, tools, Handlebars, navActive, Swal) {
        /*
         ajax url.
        */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_school_calendar_data: web_path + '/web/educational/calendars',
            data: web_path + '/web/educational/calendar/look',
            releases: web_path + '/web/campus/timetable/releases',
            release: web_path + '/web/campus/timetable/release',
            edit: '/web/campus/timetable/edit',
            del: web_path + '/web/campus/timetable/delete',
            course_add: '/web/campus/timetable/course/add',
            course_edu_attend_class_add: '/web/campus/timetable/course/edu/organize/add',
            course_edu_teacher_add: '/web/campus/timetable/course/edu/teacher/add',
            course_new_edu_add: '/web/campus/timetable/course/new-edu/add',
            course_share_add: '/web/campus/timetable/course/share/add',
            course_edit: '/web/campus/timetable/course/edit',
            course_del: '/web/campus/timetable/course/delete',
            courses: web_path + '/web/campus/timetable/courses',
            page: '/web/menu/campus/timetable'
        };

        navActive(ajax_url.page);

        /*
        参数id
        */
        var param_id = {
            school: '#school',
            college: '#college',
            schoolCalendar: '#schoolCalendar',
            timetable: '#timetable'
        };

        var page_param = {
            schoolId: $('#schoolId').val(),
            collegeId: $('#collegeId').val()
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL: 'CAMPUS_TIMETABLE_SCHOOL_SEARCH',
            COLLEGE: 'CAMPUS_TIMETABLE_COLLEGE_SEARCH',
            SCHOOL_CALENDAR: 'CAMPUS_TIMETABLE_SCHOOL_CALENDAR_SEARCH',
            TIMETABLE: 'CAMPUS_TIMETABLE_TIMETABLE_SEARCH',
            SHOW_SCREEN: 'CAMPUS_TIMETABLE_SHOW_SCREEN',
            SHOW_EFFECTIVE_COURSE: 'CAMPUS_TIMETABLE_SHOW_EFFECTIVE_COURSE'
        };

        var init_configure = {
            init_college: false,
            init_school_calendar: false,
            init_simple_screen: false
        };

        init();

        function init() {
            initShowEffectiveCourse();
            initSchool();
            initTimetable();
            initSelect2();
            initScreen();
            initMobileCarousel();
        }

        function initShowEffectiveCourse() {
            if (localStorage) {
                var showEffectiveCourse = localStorage.getItem(webStorageKey.SHOW_EFFECTIVE_COURSE);
                if (Number(showEffectiveCourse) === 1) {
                    $('#showEffectiveCourse').prop('checked', true);
                } else {
                    $('#showEffectiveCourse').prop('checked', false);
                }
            }
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
                } else {
                    sl.val(page_param.schoolId).trigger("change");
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
                        } else {
                            sl.val(page_param.collegeId).trigger("change");
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
                        }
                        if (schoolCalendar !== null) {
                            sl.val(schoolCalendar).trigger("change");
                        }
                        init_configure.init_school_calendar = true;
                    }
                });
            } else {
                $(param_id.schoolCalendar).html('<option label="请选择校历"></option>');
            }
        }

        var timetableSelect2 = null;

        function initTimetable() {
            $.get(ajax_url.releases, function (data) {
                timetableSelect2 = $(param_id.timetable).select2({
                    data: data.results
                });

                var campusCourseReleaseId = null;
                if (localStorage) {
                    campusCourseReleaseId = localStorage.getItem(webStorageKey.TIMETABLE);
                }
                if (campusCourseReleaseId !== null) {
                    timetableSelect2.val(campusCourseReleaseId).trigger("change");
                }

            });
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

        $(param_id.timetable).change(function () {
            var v = $(this).val();
            queryRelease(v);
            if (localStorage) {
                localStorage.setItem(webStorageKey.TIMETABLE, v);
            }

            if (v !== '') {
                tools.validSelect2SuccessDom(param_id.timetable);
            }
        });

        var effectiveCourseCalendarAjaxFinish = false;
        var curWeeks = -1;

        function initData(calendarId) {
            if (calendarId && calendarId.length > 0) {
                $.get(ajax_url.data, {calendarId: calendarId}, function (data) {
                    if (data.state) {
                        var calendar = data.calendar;
                        $('#title').text(calendar.title);
                        $('#schoolName').text('学校：' + calendar.schoolName);
                        $('#collegeName').text('院：' + calendar.collegeName);
                        var term = calendar.term === 0 ? '上学期' : '下学期';
                        $('#academicYear').text('学年：' + calendar.academicYear + ' ' + term);
                        $('#openDate').text('开学：' + calendar.startDate + ' ~ ' + calendar.endDate + ' 共' + calendar.openWeeks + '周');
                        $('#holidayDate').text('放假：' + calendar.holidayStartDate + ' ~ ' + calendar.holidayEndDate + ' 共' + calendar.holidayWeeks + '周');
                        var nowDate = calendar.nowDate.split('-');
                        $('#nowDate').html('<span class="tx-bold tx-20 tx-primary">' + nowDate[0] + '</span> 年 <span class="tx-bold tx-20 tx-primary">' + nowDate[1] + '</span> 月 <span class="tx-bold tx-20 tx-primary">' + nowDate[2] + '</span> 日')
                        $('#week').html('<span class="tx-bold tx-20 tx-primary">' + tools.weekDay(calendar.week) + '</span>');
                        $('#weeks').html('第 <span class="tx-bold tx-20 tx-purple"><em >' + calendar.weeks + '</em></span> 周');
                        $('#remark').text('备注：' + calendar.remark);
                        $('#releaseTimeStr').text(calendar.releaseTimeStr);

                        curWeeks = calendar.weeks;
                        effectiveCourseCalendarAjaxFinish = true;

                        showEffectiveCourse();

                    } else {
                        $('#title').text('');
                        $('#schoolName').text('');
                        $('#collegeName').text('');
                        $('#academicYear').text('');
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

        function initScreen() {
            if (localStorage) {
                var showScreen = localStorage.getItem(webStorageKey.SHOW_SCREEN);
                if (Number(showScreen) === 1) {
                    $('#simpleScreen').prop('checked', true);
                    $('#defaultData').css('display', 'none');
                    $('#simpleData').css('display', '');
                } else {
                    $('#simpleScreen').prop('checked', false);
                    $('#defaultData').css('display', '');
                    $('#simpleData').css('display', 'none');
                }
            }
        }

        function initMobileCarousel() {
            tools.listenTouchDirection(document.getElementById("carousel"), false, false, carouselPrev, false, carouselNext)
        }

        function carouselPrev() {
            $('#carousel').carousel('prev');
        }

        function carouselNext() {
            $('#carousel').carousel('next');
        }

        function queryRelease(id) {
            if (id !== '') {
                $.get(ajax_url.release + '/' + id, function (data) {
                    if (data.state) {
                        var term = data.release.term;
                        var t;
                        if (Number(term) === 0) {
                            t = '上学期';
                        } else if (Number(term) === 1) {
                            t = '下学期';
                        }
                        $('#yearAndTerm').text(data.release.startYear + '~' + data.release.endYear + ' ' + t);
                        $('#shareId').text(data.release.campusCourseReleaseId);
                        $('#shareNumber').text(data.release.shareNumber);
                        $('#qrCodeUrl').attr('src', web_path + '/' + data.release.qrCodeUrl);
                        initCourseData(data.release.campusCourseReleaseId);
                    } else {
                        $('#yearAndTerm').text('');
                        $('#shareId').text('');
                        $('#shareNumber').text('');
                        $('#qrCodeUrl').attr('src', '');
                    }
                });
            }
        }

        var courseData = [];
        var effectiveCourseDataAjaxFinish = false;

        function initCourseData(id) {
            $.get(ajax_url.courses + '/' + id, function (data) {
                for (var i = 1; i <= 7; i++) {
                    $('#week' + i).empty();
                    $('#simpleWeek' + i).empty();
                }
                if (data.state) {
                    courseData = data.listResult;
                    effectiveCourseDataAjaxFinish = true;
                    showEffectiveCourse();
                    $('#week' + data.weekDay).addClass('table-primary');
                    if (!init_configure.init_simple_screen) {
                        $('.carousel').carousel(data.weekDay - 1);
                        init_configure.init_simple_screen = true;
                    }
                }
            });
        }

        /**
         * 默认列表数据
         * @param d 数据
         */
        function defaultHtml(d) {
            var template = Handlebars.compile($("#default-course-template").html());
            Handlebars.registerHelper('bgColor', function () {
                var bgColor = this.bgColor;
                if (bgColor && bgColor !== '') {
                    bgColor += ' tx-white';
                }
                return new Handlebars.SafeString(Handlebars.escapeExpression(bgColor));
            });

            Handlebars.registerHelper('weekUnit', function () {
                var weekUnit = '';
                var startWeek = this.startWeek;
                var endWeek = this.endWeek;
                if (!endWeek || endWeek === '') {
                    weekUnit = startWeek + '周';
                } else {
                    weekUnit = startWeek + '-' + endWeek + '周';
                }
                return new Handlebars.SafeString(Handlebars.escapeExpression(weekUnit));
            });
            return template(d);
        }

        function simpleHtml(d) {
            var template = Handlebars.compile($("#simple-course-template").html());
            Handlebars.registerHelper('bgColor', function () {
                var bgColor = this.bgColor;
                if (!bgColor || bgColor === '') {
                    bgColor = 'bg-dark';
                }
                bgColor += ' tx-white';
                return new Handlebars.SafeString(Handlebars.escapeExpression(bgColor));
            });

            Handlebars.registerHelper('weekUnit', function () {
                var weekUnit = '';
                var startWeek = this.startWeek;
                var endWeek = this.endWeek;
                if (!endWeek || endWeek === '') {
                    weekUnit = startWeek + '周';
                } else {
                    weekUnit = startWeek + '-' + endWeek + '周';
                }
                return new Handlebars.SafeString(Handlebars.escapeExpression(weekUnit));
            });
            return template(d);
        }

        $('#shareQrCode').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $('#qrcodeModal').modal('show');
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }

        });

        $('#editTimetable').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $.address.value(ajax_url.edit + '/' + id);
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }
        });

        $('#delTimetable').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                Swal.fire({
                    title: "确定删除该课表吗？",
                    text: "课表删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        sendDelTimetableAjax(id);
                    }
                });
            } else {
                tools.validSelect2ErrorDom(param_id.timetable, '请选择课表');
            }
        });

        function sendDelTimetableAjax(id) {
            $.ajax({
                type: 'POST',
                url: ajax_url.del,
                data: {id: id},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        $('#yearAndTerm').text('');
                        $('#shareId').text('');
                        $('#shareNumber').text('');
                        $('#qrCodeUrl').attr('src', '');
                        timetableSelect2.val('').trigger("change");
                        $(param_id.timetable).empty();
                        for (var i = 1; i <= 7; i++) {
                            $('#week' + i).empty();
                            $('#simpleWeek' + i).empty();
                        }
                        initTimetable();
                    }
                },
                error: function (XMLHttpRequest) {
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        $('#addCourse').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $.address.value(ajax_url.course_add + '/' + id);
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }
        });

        $('#addEduOrganize').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $.address.value(ajax_url.course_edu_attend_class_add + '/' + id);
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }
        });

        $('#addEduTeacher').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $.address.value(ajax_url.course_edu_teacher_add + '/' + id);
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }
        });

        $('#addNewEdu').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $.address.value(ajax_url.course_new_edu_add + '/' + id);
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }
        });

        $('#addShareCourse').click(function () {
            var id = $('#shareId').text();
            if (id !== '') {
                $.address.value(ajax_url.course_share_add + '/' + id);
            } else {
                tools.validSelect2ErrorDom('#timetable', '请选择课表');
            }
        });

        $('.courseData').delegate('.edit', "click", function () {
            $.address.value(ajax_url.course_edit + '/' + $(this).attr('data-id'));
        });

        $('.courseData').delegate('.del', "click", function () {
            var id = $(this).attr('data-id');
            Swal.fire({
                title: "确定删除该课程吗？",
                text: "课程删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelCourseAjax(id);
                }
            });
        });

        function sendDelCourseAjax(id) {
            $.ajax({
                type: 'POST',
                url: ajax_url.course_del,
                data: {id: id},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        initCourseData($('#shareId').text());
                    }
                },
                error: function (XMLHttpRequest) {
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        $('#manageCourse').click(function () {
            if ($(this).hasClass('btn-outline-teal')) {
                $('.manageCourse').css('display', '');
                $(this).removeClass('btn-outline-teal').addClass('btn-teal');
            } else {
                $('.manageCourse').css('display', 'none');
                $(this).addClass('btn-outline-teal').removeClass('btn-teal');
            }
        });

        $('#simpleScreen').click(function () {
            if ($(this).prop('checked')) {
                $('#defaultData').css('display', 'none');
                $('#simpleData').css('display', '');

                if (localStorage) {
                    localStorage.setItem(webStorageKey.SHOW_SCREEN, "1");
                }
            } else {
                $('#defaultData').css('display', '');
                $('#simpleData').css('display', 'none');

                if (localStorage) {
                    localStorage.setItem(webStorageKey.SHOW_SCREEN, "0");
                }
            }
        });

        $('#showEffectiveCourse').click(function () {
            if ($(this).prop('checked')) {
                if (localStorage) {
                    localStorage.setItem(webStorageKey.SHOW_EFFECTIVE_COURSE, "1");
                }
            } else {
                if (localStorage) {
                    localStorage.setItem(webStorageKey.SHOW_EFFECTIVE_COURSE, "0");
                }
            }

            showEffectiveCourse();
        });

        function showEffectiveCourse() {
            // 显示有效课程
            for (var i = 1; i <= 7; i++) {
                $('#week' + i).empty();
                $('#simpleWeek' + i).empty();
            }
            if ($('#showEffectiveCourse').prop('checked')) {
                if (effectiveCourseCalendarAjaxFinish && effectiveCourseDataAjaxFinish) {
                    $.each(courseData, function (i, v) {
                        // 显示有效课程
                        var startWeek = v.startWeek;
                        var endWeek = v.endWeek;
                        if (startWeek <= curWeeks && endWeek >= curWeeks) {
                            $('#week' + v.weekDay).append(defaultHtml(v));
                            $('#simpleWeek' + v.weekDay).append(simpleHtml(v));
                        }
                    });
                } else {
                    Messenger().post({
                        message: '请先选择校历和课表',
                        type: 'error',
                        showCloseButton: true
                    });
                }
            } else {
                $.each(courseData, function (i, v) {
                    $('#week' + v.weekDay).append(defaultHtml(v));
                    $('#simpleWeek' + v.weekDay).append(simpleHtml(v));
                });
            }

            if ($('#manageCourse').hasClass('btn-outline-teal')) {
                $('.manageCourse').css('display', 'none');
            } else {
                $('.manageCourse').css('display', '');
            }
        }

    });