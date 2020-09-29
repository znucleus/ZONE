//# sourceURL=timetable_data.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "select2-zh-CN", "jquery-labelauty"],
    function ($, _, tools, Handlebars, navActive, Swal) {
        /*
         ajax url.
        */
        var ajax_url = {
            data: web_path + '/web/educational/timetable/search',
            course_name: web_path + '/web/educational/timetable/course_name',
            attend_class: web_path + '/web/educational/timetable/attend_class',
            classroom: web_path + '/web/educational/timetable/classroom',
            teacher_name: web_path + '/web/educational/timetable/teacher_name',
            uniques: web_path + '/web/educational/timetable/uniques',
            sync_data: web_path + '/web/educational/timetable/sync',
            page: '/web/menu/educational/timetable'
        };

        navActive(ajax_url.page);

        /*
        参数
        */
        var param = {
            courseName: '',
            attendClass: '',
            classroom: '',
            teacherName: '',
            teacherNumber: '',
            startYear: '',
            endYear: '',
            semester: '',
            identification: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            COURSE_NAME: 'EDUCATIONAL_TIMETABLE_COURSE_NAME_SEARCH',
            ATTEND_CLASS: 'EDUCATIONAL_TIMETABLE_ATTEND_CLASS_SEARCH',
            CLASSROOM: 'EDUCATIONAL_TIMETABLE_CLASSROOM_SEARCH',
            TEACHER_NAME: 'EDUCATIONAL_TIMETABLE_TEACHER_NAME_SEARCH',
            TEACHER_NUMBER: 'EDUCATIONAL_TIMETABLE_TEACHER_NUMBER_SEARCH',
            START_YEAR: 'EDUCATIONAL_TIMETABLE_START_YEAR_SEARCH',
            END_YEAR: 'EDUCATIONAL_TIMETABLE_END_YEAR_SEARCH',
            SEMESTER: 'EDUCATIONAL_TIMETABLE_SEMESTER_SEARCH',
            IDENTIFICATION: 'EDUCATIONAL_TIMETABLE_IDENTIFICATION_SEARCH',
        };

        /*
         参数id
         */
        var param_id = {
            courseName: '#search_course_name',
            attendClass: '#search_attend_class',
            classroom: '#search_classroom',
            teacherName: '#search_teacher_name',
            teacherNumber: '#search_teacher_number'
        };

        var courseNameSelect2;
        var attendClassSelect2;
        var classroomSelect2;
        var teacherNameSelect2;

        /*
        清空参数
        */
        function cleanParam() {
            courseNameSelect2.val('').trigger("change");
            attendClassSelect2.val('').trigger("change");
            classroomSelect2.val('').trigger("change");
            teacherNameSelect2.val('').trigger("change");
            $(param_id.teacherNumber).val('');
        }

        function initParam() {
            param.courseName = $(param_id.courseName).val();
            param.attendClass = $(param_id.attendClass).val();
            param.classroom = $(param_id.classroom).val();
            param.teacherName = $(param_id.teacherName).val();
            param.teacherNumber = $(param_id.teacherNumber).val();

            var dataTime = $("input[name='dataTime']:checked").val();
            if (dataTime && dataTime !== '') {
                var dt = dataTime.split('|');
                param.startYear = dt[0]
                param.endYear = dt[1];
                param.semester = dt[2];
                param.identification = dt[3];
            }
        }

        var init_configure = {
            init_course_name: false,
            init_attend_class: false,
            init_classroom: false,
            init_teacher_name: false,
            init_uniques: false
        };

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            initParam();
            if (localStorage) {
                localStorage.setItem(webStorageKey.COURSE_NAME, param.courseName != null ? param.courseName : '');
                localStorage.setItem(webStorageKey.ATTEND_CLASS, param.attendClass != null ? param.attendClass : '');
                localStorage.setItem(webStorageKey.CLASSROOM, param.classroom != null ? param.classroom : '');
                localStorage.setItem(webStorageKey.TEACHER_NAME, param.teacherName != null ? param.teacherName : '');
                localStorage.setItem(webStorageKey.TEACHER_NUMBER, param.teacherNumber);
                localStorage.setItem(webStorageKey.START_YEAR, param.startYear);
                localStorage.setItem(webStorageKey.END_YEAR, param.endYear);
                localStorage.setItem(webStorageKey.SEMESTER, param.semester);
                localStorage.setItem(webStorageKey.IDENTIFICATION, param.identification);
            }
        }

        /*
        搜索
        */
        $('#search').click(function () {
            refreshSearch();
            initData();
        });

        /*
         重置
         */
        $('#reset_search').click(function () {
            cleanParam();
            refreshSearch();
            initData();
        });

        $('#refresh').click(function () {
            initData();
        });

        $(param_id.courseName).on('select2:select', function (e) {
            refreshSearch();
            initData();
        });

        $(param_id.attendClass).on('select2:select', function (e) {
            refreshSearch();
            initData();
        });

        $(param_id.classroom).on('select2:select', function (e) {
            refreshSearch();
            initData();
        });

        $(param_id.teacherName).on('select2:select', function (e) {
            refreshSearch();
            initData();
        });

        $(param_id.teacherNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                initData();
            }
        });

        $('#dataTime').delegate('.labelauty', "click", function () {
            refreshSearch();
            initData();
            initCourseName();
            initAttendClass();
            initClassroom();
            initTeacherName();
        });

        init();
        initSearchInput();

        /**
         * 初始化数据
         */
        function init() {
            initDataTime();
        }

        function initDataTime() {
            tools.dataLoading();
            $.get(ajax_url.uniques, function (data) {
                tools.dataEndLoading();
                $.each(data.listResult, function (i, val) {
                    var startYear = val.startYear;
                    var endYear = val.endYear;
                    var semester = val.semester;
                    var identification = val.identification;
                    var groupData = startYear + '|' + endYear + '|' + semester + '|' + identification;

                    var se = '下学期';
                    if (semester === '1') {
                        se = '上学期';
                    }

                    $('#dataTime').append('<input class="labelauty" name="dataTime" value="' + groupData + '" type="radio" data-labelauty="' + startYear + ' ' + se + '"/>');
                });

                if (!init_configure.init_uniques) {
                    if (localStorage) {
                        var id = localStorage.getItem(webStorageKey.IDENTIFICATION);
                        var dcs = $('#dataTime').children();
                        for (var i = 0; i < dcs.length; i++) {
                            var v = $(dcs[i]).val();
                            if (id === v.split('|')[3]) {
                                $(dcs[i]).prop('checked', true);
                                init_configure.init_uniques = true;
                            }
                        }

                    }
                }

                if (!init_configure.init_uniques) {
                    $($('#dataTime').children()[0]).prop('checked', true);
                }

                initSearchContent();
                if (data.listResult && data.listResult.length > 0) {
                    initData();
                    initCourseName();
                    initAttendClass();
                    initClassroom();
                    initTeacherName();
                }
                initSelect2();
                initLabelauty();
            });
        }

        function initData() {
            tools.dataLoading();
            $.get(ajax_url.data, {extraSearch: JSON.stringify(param)}, function (data) {
                tools.dataEndLoading();
                generateData(data);
            });
        }

        function initCourseName() {
            $.get(ajax_url.course_name + '/' + param.identification, function (data) {
                $(param_id.courseName).html('<option label="请选择课程"></option>');
                courseNameSelect2 = $(param_id.courseName).select2({data: data.results});

                if (!init_configure.init_course_name) {
                    if (localStorage) {
                        var courseName = localStorage.getItem(webStorageKey.COURSE_NAME);
                        courseNameSelect2.val(courseName).trigger("change");
                    }
                    init_configure.init_course_name = true;
                }
            });
        }

        function initAttendClass() {
            $.get(ajax_url.attend_class + '/' + param.identification, function (data) {
                $(param_id.attendClass).html('<option label="请选择班级"></option>');
                attendClassSelect2 = $(param_id.attendClass).select2({data: data.results});

                if (!init_configure.init_attend_class) {
                    if (localStorage) {
                        var attendClass = localStorage.getItem(webStorageKey.ATTEND_CLASS);
                        attendClassSelect2.val(attendClass).trigger("change");
                    }
                    init_configure.init_attend_class = true;
                }
            });
        }

        function initClassroom() {
            $.get(ajax_url.classroom + '/' + param.identification, function (data) {
                $(param_id.classroom).html('<option label="请选择教室"></option>');
                classroomSelect2 = $(param_id.classroom).select2({data: data.results});

                if (!init_configure.init_classroom) {
                    if (localStorage) {
                        var classroom = localStorage.getItem(webStorageKey.CLASSROOM);
                        classroomSelect2.val(classroom).trigger("change");
                    }
                    init_configure.init_classroom = true;
                }
            });
        }

        function initTeacherName() {
            $.get(ajax_url.teacher_name + '/' + param.identification, function (data) {
                $(param_id.teacherName).html('<option label="请选择教师"></option>');
                teacherNameSelect2 = $(param_id.teacherName).select2({data: data.results});

                if (!init_configure.init_teacher_name) {
                    if (localStorage) {
                        var teacherName = localStorage.getItem(webStorageKey.TEACHER_NAME);
                        teacherNameSelect2.val(teacherName).trigger("change");
                    }
                    init_configure.init_teacher_name = true;
                }
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            initParam();

            var courseName = null;
            var attendClass = null;
            var classroom = null;
            var teacherName = null;
            var teacherNumber = null;
            var startYear = null;
            var endYear = null;
            var semester = null;
            var identification = null;
            if (localStorage) {
                courseName = localStorage.getItem(webStorageKey.COURSE_NAME);
                attendClass = localStorage.getItem(webStorageKey.ATTEND_CLASS);
                classroom = localStorage.getItem(webStorageKey.CLASSROOM);
                teacherName = localStorage.getItem(webStorageKey.TEACHER_NAME);
                teacherNumber = localStorage.getItem(webStorageKey.TEACHER_NUMBER);
                startYear = localStorage.getItem(webStorageKey.START_YEAR);
                endYear = localStorage.getItem(webStorageKey.END_YEAR);
                semester = localStorage.getItem(webStorageKey.SEMESTER);
                identification = localStorage.getItem(webStorageKey.IDENTIFICATION);
            }
            if (courseName !== null) {
                param.courseName = courseName;
            }

            if (attendClass !== null) {
                param.attendClass = attendClass;
            }

            if (classroom !== null) {
                param.classroom = classroom;
            }

            if (teacherName !== null) {
                param.teacherName = teacherName;
            }

            if (teacherNumber !== null) {
                param.teacherNumber = teacherNumber;
            }

            if (startYear !== null) {
                param.startYear = startYear;
            }

            if (endYear !== null) {
                param.endYear = endYear;
            }

            if (semester !== null) {
                param.semester = semester;
            }

            if (identification !== null) {
                param.identification = identification;
            }

        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var courseName = null;
            var attendClass = null;
            var classroom = null;
            var teacherName = null;
            var teacherNumber = null;
            var startYear = null;
            var endYear = null;
            var semester = null;
            var identification = null;
            if (localStorage) {
                courseName = localStorage.getItem(webStorageKey.COURSE_NAME);
                attendClass = localStorage.getItem(webStorageKey.ATTEND_CLASS);
                classroom = localStorage.getItem(webStorageKey.CLASSROOM);
                teacherName = localStorage.getItem(webStorageKey.TEACHER_NAME);
                teacherNumber = localStorage.getItem(webStorageKey.TEACHER_NUMBER);
                startYear = localStorage.getItem(webStorageKey.START_YEAR);
                endYear = localStorage.getItem(webStorageKey.END_YEAR);
                semester = localStorage.getItem(webStorageKey.SEMESTER);
                identification = localStorage.getItem(webStorageKey.IDENTIFICATION);
            }
            if (courseName !== null) {
                $(param_id.courseName).val(courseName);
            }

            if (attendClass !== null) {
                $(param_id.attendClass).val(attendClass);
            }

            if (classroom !== null) {
                $(param_id.classroom).val(classroom);
            }

            if (teacherName !== null) {
                $(param_id.teacherName).val(teacherName);
            }

            if (teacherNumber !== null) {
                $(param_id.teacherNumber).val(teacherNumber);
            }

            if (startYear && startYear !== '' && endYear && endYear !== '' && semester && semester !== ''
                && identification && identification !== '') {
                var dtv = startYear + '|' + endYear + '|' + semester + '|' + identification;
                var dts = $("input[name='dataTime']");
                for (var i = 0; i < dts.length; i++) {
                    if (dtv === $(dts[i]).val()) {
                        $(dts[i]).prop('checked', true);
                    }
                }
            }

        }

        function initLabelauty() {
            $(".labelauty").labelauty();
        }

        $('#sync').click(function () {
            Swal.fire({
                title: "确定全量同步吗？",
                text: "课表全量同步！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#dcbc46',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    syncData();
                }
            });
        });

        function syncData() {
            $.get(ajax_url.sync_data, function (data) {
                Swal.fire(data.state ? '正在同步' : '同步失败', data.msg, data.state ? 'success' : 'error');
            });
        }

        function cleanData() {
            // 清空之前的数据
            for (var i = 1; i <= 7; i++) {
                for (var j = 1; j <= 6; j++) {
                    $('#week_' + i + '_' + j).empty();
                }
            }
        }

        function generateData(data) {
            cleanData();
            $.each(data.listResult, function (i, v) {
                var w = 0;
                if (_.trim(v.week) === '星期一') {
                    w = 1;
                }

                if (_.trim(v.week) === '星期二') {
                    w = 2;
                }

                if (_.trim(v.week) === '星期三') {
                    w = 3;
                }

                if (_.trim(v.week) === '星期四') {
                    w = 4;
                }

                if (_.trim(v.week) === '星期五') {
                    w = 5;
                }

                if (_.trim(v.week) === '星期六') {
                    w = 6;
                }

                if (_.trim(v.week) === '星期天') {
                    w = 7;
                }

                for (var j = 1; j <= 6; j++) {
                    week(w, j, v);
                }
            })
        }

        function week(w, j, timetableElastic) {
            var classTime = timetableElastic.classTime;
            var overTime = timetableElastic.overTime;
            var classTimePrefix = Number(classTime.split(':')[0]);
            var overTimePrefix = Number(overTime.split(':')[0]);
            var isRight = false;
            switch (j) {
                case 1:
                    isRight = check1(classTimePrefix, overTimePrefix);
                    break;
                case 2:
                    isRight = check2(classTimePrefix, overTimePrefix);
                    break;
                case 3:
                    isRight = check3(classTimePrefix, overTimePrefix);
                    break;
                case 4:
                    isRight = check4(classTimePrefix, overTimePrefix);
                    break;
                case 5:
                    isRight = check5(classTimePrefix, overTimePrefix);
                    break;
                case 6:
                    isRight = check6(classTimePrefix, overTimePrefix);
                    break;
            }
            if (isRight) {
                generateDataHtml('#week_' + w + '_' + j, timetableElastic);
            }
        }

        /**
         * 第一节区间判断
         * @param classTimePrefix
         * @param overTimePrefix
         * @returns {boolean}
         */
        function check1(classTimePrefix, overTimePrefix) {
            var isRight = false;

            if (classTimePrefix < 8 || (classTimePrefix >= 8 && classTimePrefix < 10)) {
                if (overTimePrefix > 10 || (overTimePrefix >= 8 && overTimePrefix <= 10)) {
                    isRight = true;
                }
            }

            return isRight;
        }

        /**
         * 第二节区间判断
         * @param classTimePrefix
         * @param overTimePrefix
         * @returns {boolean}
         */
        function check2(classTimePrefix, overTimePrefix) {
            var isRight = false;

            if (classTimePrefix < 10 || (classTimePrefix >= 10 && classTimePrefix < 12)) {
                if (overTimePrefix > 12 || (overTimePrefix >= 10 && overTimePrefix <= 12)) {
                    isRight = true;
                }
            }

            return isRight;
        }

        /**
         * 第三节区间判断
         * @param classTimePrefix
         * @param overTimePrefix
         * @returns {boolean}
         */
        function check3(classTimePrefix, overTimePrefix) {
            var isRight = false;
            if (classTimePrefix < 14 || (classTimePrefix >= 14 && classTimePrefix < 16)) {
                if (overTimePrefix > 16 || (overTimePrefix >= 14 && overTimePrefix <= 16)) {
                    isRight = true;
                }
            }

            return isRight;
        }

        /**
         * 第四节区间判断
         * @param classTimePrefix
         * @param overTimePrefix
         * @returns {boolean}
         */
        function check4(classTimePrefix, overTimePrefix) {
            var isRight = false;
            if (classTimePrefix < 16 || (classTimePrefix >= 16 && classTimePrefix < 18)) {
                if (overTimePrefix > 18 || (overTimePrefix >= 16 && overTimePrefix <= 18)) {
                    isRight = true;
                }
            }

            return isRight;
        }

        /**
         * 第五节区间判断
         * @param classTimePrefix
         * @param overTimePrefix
         * @returns {boolean}
         */
        function check5(classTimePrefix, overTimePrefix) {
            var isRight = false;
            if (classTimePrefix < 18 || (classTimePrefix >= 18 && classTimePrefix < 22)) {
                if (overTimePrefix > 22 || (overTimePrefix >= 18 && overTimePrefix <= 22)) {
                    isRight = true;
                }
            }


            return isRight;
        }

        /**
         * 异常区间判断
         * @param classTimePrefix
         * @param overTimePrefix
         * @returns {boolean}
         */
        function check6(classTimePrefix, overTimePrefix) {
            var isRight = false;
            if (classTimePrefix >= 22 || (classTimePrefix > overTimePrefix)) {
                isRight = true;
            }

            return isRight;
        }

        function generateDataHtml(target, timetableElastic) {
            var template = Handlebars.compile($("#timetable-template").html());
            $(target).html(template(timetableElastic));
        }
    });