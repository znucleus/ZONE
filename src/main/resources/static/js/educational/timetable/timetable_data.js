//# sourceURL=timetable_data.js
require(["jquery", "tools", "handlebars", "nav.active", "sweetalert2", "select2-zh-CN", "jquery-labelauty"],
    function ($, tools, Handlebars, navActive, Swal) {
        /*
         ajax url.
        */
        var ajax_url = {
            data: web_path + '/web/educational/timetable/data',
            course_name: web_path + '/web/educational/timetable/course_name',
            attend_class: web_path + '/web/educational/timetable/attend_class',
            classroom: web_path + '/web/educational/timetable/classroom',
            teacher_name: web_path + '/web/educational/timetable/teacher_name',
            uniques: web_path + '/web/educational/timetable/uniques',
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
                var dt = dataTime.split('\\|');
                param.startYear = dt[0];
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
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.COURSE_NAME, param.courseName != null ? param.courseName : '');
                sessionStorage.setItem(webStorageKey.ATTEND_CLASS, param.attendClass != null ? param.attendClass : '');
                sessionStorage.setItem(webStorageKey.CLASSROOM, param.classroom != null ? param.classroom : '');
                sessionStorage.setItem(webStorageKey.TEACHER_NAME, param.teacherName != null ? param.teacherName : '');
                sessionStorage.setItem(webStorageKey.TEACHER_NUMBER, param.teacherNumber);
                sessionStorage.setItem(webStorageKey.START_YEAR, param.startYear);
                sessionStorage.setItem(webStorageKey.END_YEAR, param.endYear);
                sessionStorage.setItem(webStorageKey.SEMESTER, param.semester);
                sessionStorage.setItem(webStorageKey.IDENTIFICATION, param.identification);
            }
        }

        /*
        搜索
        */
        $('#search').click(function () {
            refreshSearch();
            init();
        });

        /*
         重置
         */
        $('#reset_search').click(function () {
            cleanParam();
            refreshSearch();
            init();
        });

        $('#refresh').click(function () {
            init();
        });

        $(param_id.courseName).on('select2:select', function (e) {
            refreshSearch();
            init();
        });

        $(param_id.attendClass).on('select2:select', function (e) {
            refreshSearch();
            init();
        });

        $(param_id.classroom).on('select2:select', function (e) {
            refreshSearch();
            init();
        });

        $(param_id.teacherName).on('select2:select', function (e) {
            refreshSearch();
            init();
        });

        $(param_id.teacherNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(".labelauty").click(function () {
            var v = $(this).val();
            console.log(v);
        });

        init();
        initSearchInput();

        /**
         * 初始化数据
         */
        function init() {
            initDataTime();
            initSearchContent();
            initData();
            initCourseName();
            initAttendClass();
            initClassroom();
            initTeacherName();
            initSelect2();
        }

        function initDataTime() {
            $.get(ajax_url.uniques, function (data) {
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

                    if (!init_configure.init_uniques) {
                        if (typeof (Storage) !== "undefined") {
                            var id = sessionStorage.getItem(webStorageKey.IDENTIFICATION);
                            if (id === identification) {
                                $('#dataTime').append('<input class="labelauty" name="dataTime" value="' + groupData + '" type="radio" data-labelauty="' + startYear + ' ' + se + '" checked/>');
                                init_configure.init_uniques = true;
                            }
                        }
                    }

                    $('#dataTime').append('<input class="labelauty" name="dataTime" value="' + groupData + '" type="radio" data-labelauty="' + startYear + ' ' + se + '"/>');

                });

                if (!init_configure.init_uniques) {
                    $($('#dataTime').children()[0]).prop('checked', true);
                }

                initLabelauty();
            });
        }

        function initData() {
            tools.dataLoading();
            $.get(ajax_url.data, param, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

        function initCourseName() {
            $.get(ajax_url.course_name + '/' + param.identification, function (data) {
                $(param_id.courseName).html('<option label="请选择课程"></option>');
                courseNameSelect2 = $(param_id.courseName).select2({data: data.results});

                if (!init_configure.init_course_name) {
                    if (typeof (Storage) !== "undefined") {
                        var courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
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
                    if (typeof (Storage) !== "undefined") {
                        var attendClass = sessionStorage.getItem(webStorageKey.ATTEND_CLASS);
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
                    if (typeof (Storage) !== "undefined") {
                        var classroom = sessionStorage.getItem(webStorageKey.CLASSROOM);
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
                    if (typeof (Storage) !== "undefined") {
                        var teacherName = sessionStorage.getItem(webStorageKey.TEACHER_NAME);
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
            var courseName = null;
            var attendClass = null;
            var classroom = null;
            var teacherName = null;
            var teacherNumber = null;
            var startYear = null;
            var endYear = null;
            var semester = null;
            var identification = null;
            if (typeof (Storage) !== "undefined") {
                courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
                attendClass = sessionStorage.getItem(webStorageKey.ATTEND_CLASS);
                classroom = sessionStorage.getItem(webStorageKey.CLASSROOM);
                teacherName = sessionStorage.getItem(webStorageKey.TEACHER_NAME);
                teacherNumber = sessionStorage.getItem(webStorageKey.TEACHER_NUMBER);
                startYear = sessionStorage.getItem(webStorageKey.START_YEAR);
                endYear = sessionStorage.getItem(webStorageKey.END_YEAR);
                semester = sessionStorage.getItem(webStorageKey.SEMESTER);
                identification = sessionStorage.getItem(webStorageKey.IDENTIFICATION);
            }
            if (courseName !== null) {
                param.courseName = courseName;
            } else {
                initParam();
            }

            if (attendClass !== null) {
                param.attendClass = attendClass;
            } else {
                initParam();
            }

            if (classroom !== null) {
                param.classroom = classroom;
            } else {
                initParam();
            }

            if (teacherName !== null) {
                param.teacherName = teacherName;
            } else {
                initParam();
            }

            if (teacherNumber !== null) {
                param.teacherNumber = teacherNumber;
            } else {
                initParam();
            }

            if (startYear !== null) {
                param.startYear = startYear;
            } else {
                initParam();
            }

            if (endYear !== null) {
                param.endYear = endYear;
            } else {
                initParam();
            }

            if (semester !== null) {
                param.semester = semester;
            } else {
                initParam();
            }

            if (identification !== null) {
                param.identification = identification;
            } else {
                initParam();
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
            if (typeof (Storage) !== "undefined") {
                courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
                attendClass = sessionStorage.getItem(webStorageKey.ATTEND_CLASS);
                classroom = sessionStorage.getItem(webStorageKey.CLASSROOM);
                teacherName = sessionStorage.getItem(webStorageKey.TEACHER_NAME);
                teacherNumber = sessionStorage.getItem(webStorageKey.TEACHER_NUMBER);
                startYear = sessionStorage.getItem(webStorageKey.START_YEAR);
                endYear = sessionStorage.getItem(webStorageKey.END_YEAR);
                semester = sessionStorage.getItem(webStorageKey.SEMESTER);
                identification = sessionStorage.getItem(webStorageKey.IDENTIFICATION);
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
    });