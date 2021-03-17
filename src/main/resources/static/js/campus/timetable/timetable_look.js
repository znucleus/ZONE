//# sourceURL=timetable_look.js
require(["jquery", "tools", "handlebars", "nav.active", "sweetalert2", "jquery.address", "select2-zh-CN", "messenger"],
    function ($, tools, Handlebars, navActive, Swal) {
        /*
         ajax url.
        */
        var ajax_url = {
            releases: web_path + '/web/campus/timetable/releases',
            release: web_path + '/web/campus/timetable/release',
            edit: '/web/campus/timetable/edit',
            del: web_path + '/web/campus/timetable/delete',
            course_add: '/web/campus/timetable/course/add',
            course_new_edu_add: '/web/campus/timetable/course/new-edu/add',
            course_share_add: '/web/campus/timetable/course/share/add',
            course_edit: '/web/campus/timetable/course/edit',
            course_del: web_path + '/web/campus/timetable/course/delete',
            courses: web_path + '/web/campus/timetable/courses',
            generate_ics:web_path + '/web/campus/timetable/course/generate-ics',
            page: '/web/menu/campus/timetable'
        };

        navActive(ajax_url.page);

        /*
        参数id
        */
        var param_id = {
            timetable: '#timetable'
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TIMETABLE: 'CAMPUS_TIMETABLE_TIMETABLE_SEARCH',
            SHOW_SCREEN: 'CAMPUS_TIMETABLE_SHOW_SCREEN',
            SHOW_EFFECTIVE_COURSE: 'CAMPUS_TIMETABLE_SHOW_EFFECTIVE_COURSE'
        };

        var init_configure = {
            init_simple_screen: false
        };

        init();

        function init() {
            initShowEffectiveCourse();
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

        var curWeeks = -1;

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

            var weekday = Number($('#weekday').val());
            $('#week' + weekday).addClass('table-primary');
            $('.carousel').carousel(weekday - 1);
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
                        var semester = data.mapResult.semester;
                        var t;
                        if (Number(semester) === 1) {
                            t = '第一学期';
                        } else if (Number(semester) === 2) {
                            t = '第二学期';
                        } else if (Number(semester) === 3) {
                            t = '第三学期';
                        }
                        $('#yearAndTerm').text(data.mapResult.schoolYear + ' ' + t);
                        $('#shareId').text(data.mapResult.campusCourseReleaseId);
                        $('#shareNumber').text(data.mapResult.shareNumber);
                        $('#qrCodeUrl').attr('src', web_path + '/' + data.mapResult.qrCodeUrl);
                        $('#week').text('今日 ' + tools.weekday(data.mapResult.week));
                        $('#weeks').text('第' + data.mapResult.curWeeks + '周（共' + data.mapResult.totalWeeks + '周）');
                        $('#startDate').text('开始日期：' + data.mapResult.startDate);
                        $('#endDate').text('结束日期：' + data.mapResult.endDate);

                        curWeeks = data.mapResult.curWeeks;
                        initCourseData(data.mapResult.campusCourseReleaseId);
                    } else {
                        $('#yearAndTerm').text('');
                        $('#shareId').text('');
                        $('#shareNumber').text('');
                        $('#qrCodeUrl').attr('src', '');
                        $('#week').text('');
                        $('#weeks').text('');
                        $('#startDate').text('');
                        $('#endDate').text('');
                    }
                });
            }
        }

        var courseData = [];

        function initCourseData(id) {
            $.get(ajax_url.courses + '/' + id, function (data) {
                for (var i = 1; i <= 7; i++) {
                    $('#week' + i).empty();
                    $('#simpleWeek' + i).empty();
                }
                if (data.state) {
                    courseData = data.listResult;
                    showEffectiveCourse();
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

        $('#exportIcs').click(function (){
            var schoolCalendar = $(param_id.schoolCalendar).val();
            if(schoolCalendar !== ''){
                var timetable = $(param_id.timetable).val();
                if(timetable !== ''){
                    $.get(ajax_url.generate_ics, {
                        campusCourseReleaseId:timetable,
                        calendarId:schoolCalendar
                    },function (data) {
                        if(data.state){
                            window.location.href = web_path + '/' + data.path;
                        } else {
                            Messenger().post({
                                message: data.msg,
                                type: 'error',
                                showCloseButton: true
                            });
                        }

                    });
                } else {
                    Messenger().post({
                        message: '请选择课表',
                        type: 'error',
                        showCloseButton: true
                    });
                }
            } else {
                Messenger().post({
                    message: '请选择校历',
                    type: 'error',
                    showCloseButton: true
                });
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
                $.each(courseData, function (i, v) {
                    // 显示有效课程
                    var startWeek = v.startWeek;
                    var endWeek = v.endWeek;
                    if (startWeek <= curWeeks && endWeek >= curWeeks) {
                        $('#week' + v.weekday).append(defaultHtml(v));
                        $('#simpleWeek' + v.weekday).append(simpleHtml(v));
                    }
                });
            } else {
                $.each(courseData, function (i, v) {
                    $('#week' + v.weekday).append(defaultHtml(v));
                    $('#simpleWeek' + v.weekday).append(simpleHtml(v));
                });
            }

            if ($('#manageCourse').hasClass('btn-outline-teal')) {
                $('.manageCourse').css('display', 'none');
            } else {
                $('.manageCourse').css('display', '');
            }
        }

    });