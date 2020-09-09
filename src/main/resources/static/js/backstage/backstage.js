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
        "ajax.loading.view": web_path + "/js/util/ajax.loading.view",
        "nav.active": web_path + "/js/util/nav.active",
        "lodash_plugin": web_path + "/js/util/lodash_plugin",
        "workbook": web_path + "/js/util/workbook",
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min",
        "bracket": web_path + "/plugins/bracket/js/bracket.min",
        "perfect-scrollbar.jquery": web_path + "/plugins/perfect-scrollbar/js/perfect-scrollbar.jquery.min",
        "peity": web_path + "/plugins/peity/jquery.peity.min",
        "moment": web_path + "/plugins/moment/moment.min",
        "moment-with-locales": web_path + "/plugins/moment/moment-with-locales.min",
        "jquery.showLoading": web_path + "/plugins/loading/js/jquery.showLoading.min",
        "jquery-loading": web_path + "/plugins/jquery-loading/jquery.loading.min",
        "jquery.address": web_path + "/plugins/jquery-address/jquery.address-1.6.min",
        "jquery.simple-pagination": web_path + "/plugins/jquery-simple-pagination/jquery.simplePagination.min",
        "dropify": web_path + "/plugins/file-uploads/js/dropify.min",
        "sweetalert2": web_path + "/plugins/sweetalert2/sweetalert2.all.min",
        "select2": web_path + "/plugins/select2/js/select2.full.min",
        "select2-zh-CN": web_path + "/plugins/select2/js/i18n/zh-CN.min",
        "bootstrap-inputmask": web_path + "/plugins/bootstrap-inputmask/bootstrap-inputmask.min",
        "bootstrap-maxlength": web_path + "/plugins/bootstrap-maxlength/bootstrap-maxlength.min",
        "bootstrap-treeview": web_path + "/plugins/bootstrap-treeview/js/bootstrap-treeview.min",
        "bootstrap-duallistbox": web_path + "/plugins/bootstrap-duallistbox/jquery.bootstrap-duallistbox.min",
        "flatpickr": web_path + "/plugins/flatpickr/js/flatpickr.min",
        "flatpickr-zh": web_path + "/plugins/flatpickr/l10n/zh.min",
        "jquery.entropizer": web_path + "/plugins/jquery-entropizer/js/jquery-entropizer.min",
        "entropizer": web_path + "/plugins/jquery-entropizer/js/entropizer.min",
        "check.all": web_path + "/plugins/checkall/checkall.min",
        "jquery-labelauty": web_path + "/plugins/jquery-labelauty/jquery-labelauty.min",
        "responsive.bootstrap4": web_path + "/plugins/datatables/js/responsive.bootstrap4.min",
        "datatables.net-responsive": web_path + "/plugins/datatables/js/dataTables.responsive.min",
        "datatables.net": web_path + "/plugins/datatables/js/jquery.dataTables.min",
        "datatables.net-bs4": web_path + "/plugins/datatables/js/dataTables.bootstrap4.min",
        "tablesaw": web_path + "/plugins/tablesaw/tablesaw.jquery.min",
        "jquery-ui/widget": web_path + "/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.min",
        "jquery.iframe-transport": web_path + "/plugins/jquery-file-upload/js/jquery.iframe-transport.min",
        "jquery.fileupload-process": web_path + "/plugins/jquery-file-upload/js/jquery.fileupload-process.min",
        "jquery.fileupload": web_path + "/plugins/jquery-file-upload/js/jquery.fileupload.min",
        "jquery.fileupload-validate": web_path + "/plugins/jquery-file-upload/js/jquery.fileupload-validate.min",
        "KaTeX": web_path + "/plugins/KaTeX/katex.min",
        "highlight": web_path + "/plugins/highlight/highlight.min",
        "quill": web_path + "/plugins/quill/quill.min",
        "jquery.print": web_path + "/plugins/jquery-print/jquery.print.min",
        "jquery-toggles": web_path + "/plugins/jquery-toggles/toggles.min",
        "clipboard": web_path + "/plugins/clipboard/clipboard.min",
        "mfb": web_path + "/plugins/mfb/mfb.min",
        "modernizr.touch": web_path + "/plugins/mfb/lib/modernizr.touch.min",
        "js-year-calendar": web_path + "/plugins/js-year-calendar/js-year-calendar.min",
        "js-year-calendar.zh-CN": web_path + "/plugins/js-year-calendar/js-year-calendar.zh-CN.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "peity": {
            deps: ["jquery"]
        },
        "perfect-scrollbar.jquery": {
            deps: ["jquery"]
        },
        "bracket": {
            deps: ["jquery", "perfect-scrollbar.jquery", "peity"]
        },
        "messenger": {
            deps: ["jquery"]
        },
        "jquery.showLoading": {
            deps: ["jquery"]
        },
        "jquery-loading": {
            deps: ["jquery"]
        },
        "jquery.address": {
            deps: ["jquery"]
        },
        "jquery.simple-pagination": {
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-simple-pagination/simplePagination.min"]
        },
        "dropify": {
            deps: ["jquery", "css!" + web_path + "/plugins/file-uploads/css/dropify.min"]
        },
        "sweetalert2": {
            deps: ["jquery", "css!" + web_path + "/plugins/sweetalert2/sweetalert2.min"]
        },
        "select2-zh-CN": {
            deps: ["jquery", "select2"]
        },
        "flatpickr": {
            deps: ["jquery", "css!" + web_path + "/plugins/flatpickr/css/flatpickr.min"]
        },
        "flatpickr-zh": {
            deps: ["flatpickr"]
        },
        "jquery.entropizer": {
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-entropizer/css/jquery-entropizer.min"]
        },
        "check.all": {
            deps: ["jquery"]
        },
        "jquery-labelauty": {
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-labelauty/jquery-labelauty.min"]
        },
        "responsive.bootstrap4": {
            deps: ["datatables.net-responsive", "datatables.net-bs4", "datatables.net", "css!" + web_path + "/plugins/datatables/css/dataTables.bootstrap4.min",
                "css!" + web_path + "/plugins/datatables/css/responsive.bootstrap4.min"]
        },
        "tablesaw": {
            deps: ["jquery", "css!" + web_path + "/plugins/tablesaw/tablesaw.min"]
        },
        "bootstrap-treeview": {
            deps: ["jquery", "css!" + web_path + "/plugins/bootstrap-treeview/css/bootstrap-treeview.min"]
        },
        "bootstrap-duallistbox": {
            deps: ["jquery", "css!" + web_path + "/plugins/bootstrap-duallistbox/bootstrap-duallistbox.min"]
        },
        "jquery-ui/widget": {
            deps: ["jquery"]
        },
        "jquery.iframe-transport": {
            deps: ["jquery"]
        },
        "jquery.fileupload": {
            deps: ["jquery-ui/widget", "jquery.iframe-transport"]
        },
        "jquery.fileupload-process": {
            deps: ["jquery.fileupload"]
        },
        "jquery.fileupload-validate": {
            deps: ["jquery.fileupload-process", "css!" + web_path + "/plugins/jquery-file-upload/css/jquery.fileupload.min"]
        },
        "KaTeX": {
            deps: ["css!" + web_path + "/plugins/KaTeX/katex.min"]
        },
        "highlight": {
            deps: ["css!" + web_path + "/plugins/highlight/monokai-sublime.min"]
        },
        "quill": {
            deps: ["highlight", "css!" + web_path + "/plugins/quill/quill.bubble.min", "css!" + web_path + "/plugins/quill/quill.snow.min"]
        },
        "jquery.print": {
            deps: ["jquery"]
        },
        "jquery-toggles": {
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-toggles/toggles-full.min"]
        },
        "mfb": {
            deps: ["modernizr.touch", "css!" + web_path + "/plugins/mfb/mfb.min"]
        },
        "js-year-calendar": {
            deps: ["jquery", "css!" + web_path + "/plugins/js-year-calendar/js-year-calendar.min", "css!" + web_path + "/plugins/js-year-calendar/custom"]
        },
        "js-year-calendar.zh-CN": {
            deps: ["jquery", "js-year-calendar"]
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

// require(["module/name", ...], function(params){ ... });
require(["jquery", "requirejs-domready", "moment-with-locales", "handlebars", "ajax.loading.view", "nav.active",
        "bootstrap", "bracket", "csrf", "jquery.address", "mfb"],
    function ($, domready, moment, Handlebars, loadingView, navActive) {
        domready(function () {
            Messenger.options = {
                extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
                theme: 'flat'
            };

            moment.locale('zh-cn');

            /*
             动态链接点击效果
             */
            var bodyElement = $('body');
            bodyElement.delegate('.dy_href', "click", function (e) {
                $.address.title($(this).text() + '-' + $('#webAppName').text());
                var href = $(this).attr('href');
                if (href !== 'javascript:;' && href !== '#') {
                    navActive(href.substring(1));
                }

                // closing left sidebar
                $('footer').css('display', '');
                $('body').removeClass('show-left');
            });

            /*
             init jquery address.
             */
            var isInitAddress = false;
            var isInitOnAddress = false;
            $.address.init(function (event) {
                // 插件初始化,一般这里调用 $('.nav a').address(); 实现链接单击监听
                $('.dy_href').address();
                isInitAddress = true;
            }).change(function (event) {
                // 当页面地址更改的时候调用,即#号之后的地址更改
                if (event.value !== '/' && event.value !== '/javascript:;' && event.value !== '/#') {
                    if (!isInitAddress) {
                        isInitOnAddress = false;
                    }
                    if (!isInitOnAddress) {
                        loadingView(event.value, '#page-wrapper', web_path);
                    } else {
                        isInitOnAddress = false;
                    }
                }
            });

            function initAddressRefresh() {
                if (!isInitAddress) {
                    var url = window.location.href;
                    if (url.lastIndexOf('#') > 0) {
                        isInitOnAddress = true;
                        loadingView(url.substring(url.lastIndexOf('#') + 1), '#page-wrapper', web_path);
                    }
                }
            }

            var ajax_url = {
                user_notify: web_path + '/users/data/notify',
                user_notify_detail: web_path + '/users/notify/detail',
                user_notify_reads: web_path + '/users/notify/reads',
                system_notify: web_path + '/users/system/notify',
                upgrade_student: '/users/upgrade/student',
                upgrade_staff: '/users/upgrade/staff'
            };

            var user_notify_param_id = {
                userNotifyIcon: '#userNotifyIcon',
                userNotifyData: '#userNotifyData',
                userNotifyReads: '#userNotifyReads'
            };

            var system_notify_param_id = {
                systemNotifyData: '#systemNotifyData'
            };

            var user_notify_param = {
                pageNum: 0,
                length: 4,
                displayedPages: 4,
                orderColumnName: 'createDate',
                orderDir: 'desc',
                extraSearch: JSON.stringify({
                    isSee: 0,
                    needCount: 0
                })
            };

            var modal_id = {
                userNotifyModal: '#userNotifyModal'
            };

            init();

            function init() {
                initUserNotify();
                initSystemNotify();
                initAddressRefresh();
            }

            // 轮询扫描通知
            setInterval(initUserNotify, 60000);

            function initUserNotify() {
                $.ajax({
                    type: 'GET',
                    url: ajax_url.user_notify,
                    data: user_notify_param,
                    success: function (data) {
                        if (data.page.totalSize > 0) {
                            $(user_notify_param_id.userNotifyIcon).addClass('square-8');
                        } else {
                            $(user_notify_param_id.userNotifyIcon).removeClass('square-8');
                        }

                        generateUserNotifyHtml(data);
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

            function generateUserNotifyHtml(data) {
                if (data.page.totalSize <= 0) {
                    var html = '<p class="text-center pd-x-20 pd-y-15"><small>无任何消息</small></p>';
                    $(user_notify_param_id.userNotifyData).html(html);
                } else {
                    var template = Handlebars.compile($("#users-notify-template").html());

                    Handlebars.registerHelper('create_date', function () {
                        return new Handlebars.SafeString(moment(this.createDateStr, "YYYY-MM-DD HH:mm:ss").fromNow());
                    });

                    $(user_notify_param_id.userNotifyData).html(template(data));
                }
            }

            $(modal_id.userNotifyModal).on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget); // Button that triggered the modal
                var id = button.data('id'); // Extract info from data-* attributes

                $.ajax({
                    type: 'GET',
                    url: ajax_url.user_notify_detail + '/' + id,
                    success: function (data) {
                        $('#userNotifyModalTitle').text(data.userNotify.notifyTitle).removeClass().addClass('modal-title').addClass('text-' + data.userNotify.notifyType);
                        $('#userNotifyModalSendUserInfo').text(data.userNotify.realName + ' 于(' + data.userNotify.createDateStr + ') 发送：');
                        $('#userNotifyModalContent').text(data.userNotify.notifyContent);
                        $('#userNotifyModal').modal('show');
                    },
                    error: function (XMLHttpRequest) {
                        Messenger().post({
                            message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                            type: 'error',
                            showCloseButton: true
                        });
                    }
                });
            }).on('shown.bs.modal', function (event) {
                // 变为已读
                var button = $(event.relatedTarget); // Button that triggered the modal
                var id = button.data('id'); // Extract info from data-* attributes
                $.post(ajax_url.user_notify_reads, {userNotifyId: id});
            });

            $(user_notify_param_id.userNotifyReads).click(function () {
                var elements = $(user_notify_param_id.userNotifyData + ' > a');
                var ids = [];
                for (var i = 0; i < elements.length; i++) {
                    ids.push($(elements[i]).attr('data-id'));
                }
                if (ids.length > 0) {
                    $.post(ajax_url.user_notify_reads, {userNotifyId: ids.join(',')}, function (data) {
                        initUserNotify();
                    });
                }
            });

            function initSystemNotify() {
                $.ajax({
                    type: 'GET',
                    url: ajax_url.system_notify,
                    success: function (data) {
                        generateSystemNotifyHtml(data);
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

            function generateSystemNotifyHtml(data) {
                var template = Handlebars.compile($("#system-notify-template").html());
                $(system_notify_param_id.systemNotifyData).html(template(data));
            }

            $('#upgradeStudent').click(function () {
                $('#upgradeModal').modal('hide');
                $.address.value(ajax_url.upgrade_student);
            });

            $('#upgradeStaff').click(function () {
                $('#upgradeModal').modal('hide');
                $.address.value(ajax_url.upgrade_staff);
            });
        });
    });