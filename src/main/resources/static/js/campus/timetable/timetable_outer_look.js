requirejs.config({
    map: {
        '*': {
            'css': web_path + '/webjars/require-css/css.min.js' // or whatever the path to require-css is
        }
    },
    // pathsオプションの設定。"module/name": "path"を指定します。拡張子（.js）は指定しません。
    paths: {
        "csrf": web_path + "/js/util/csrf",
        "tools": web_path + "/js/util/tools",
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min",
        "jquery-loading": web_path + "/plugins/jquery-loading/jquery.loading.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "jquery-loading": {
            deps: ["jquery"]
        }
    }
});

/*
 捕获全局错误
 */
requirejs.onError = function (err) {
    console.log(err.requireType);
    if (err.requireType === 'timeout') {
        console.log('modules: ' + err.requireModules);
    }
    throw err;
};

require(["jquery", "requirejs-domready", "tools", "handlebars", "csrf", "bootstrap", "jquery-loading"],
    function ($, domready, tools, Handlebars) {
        domready(function () {

            var ajax_url = {
                courses: web_path + '/anyone/campus/timetable/share-courses',
            };

            var page_param = {
                campusCourseReleaseId: $('#campusCourseReleaseId').text()
            };

            /*
             web storage key.
            */
            var webStorageKey = {
                SHOW_SCREEN: 'CAMPUS_ANYONE_TIMETABLE_SHOW_SCREEN'
            };

            init();

            function init() {
                initData();
                initScreen();
                initMobileCarousel();
            }

            function initData() {
                tools.dataLoading();
                $.get(ajax_url.courses + '/' + page_param.campusCourseReleaseId, function (data) {
                    tools.dataEndLoading();
                    if (data.state) {
                        $('#title').text(data.release.title);
                        $('#publisher').text(data.release.publisher);
                        var semester = data.release.semester;
                        var t;
                        if (Number(semester) === 1) {
                            t = '第一学期';
                        } else if (Number(semester) === 2) {
                            t = '第二学期';
                        } else if (Number(semester) === 3) {
                            t = '第三学期';
                        }
                        $('#yearAndTerm').text(data.release.schoolYear + ' ' + t);
                        $('#week').text('今日 ' + tools.weekday(data.week));
                        $('#weeks').text('第' + data.curWeeks + '周（共' + data.totalWeeks + '周）');
                        $('#startDate').text('开始日期：' + data.release.startDate);
                        $('#endDate').text('结束日期：' + data.release.endDate);

                        $.each(data.listResult, function (i, v) {
                            $('#week' + v.weekday).append(defaultHtml(v));
                            $('#simpleWeek' + v.weekday).append(simpleHtml(v));
                        });
                    }
                });
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
        });
    });