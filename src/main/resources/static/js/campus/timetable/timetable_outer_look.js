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
                courses: web_path + '/anyone/campus/timetable/share_courses',
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
                        var term = data.release.term;
                        var t;
                        if (Number(term) === 0) {
                            t = '上学期';
                        } else if (Number(term) === 1) {
                            t = '下学期';
                        }
                        $('#yearAndTerm').text(data.release.startYear + '~' + data.release.endYear + ' ' + t);
                        $('#week' + data.weekDay).addClass('table-primary');
                        $('.carousel').carousel(data.weekDay - 1);
                        $.each(data.listResult, function (i, v) {
                            $('#week' + v.weekDay).append(defaultHtml(v));
                            $('#simpleWeek' + v.weekDay).append(simpleHtml(v));
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