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
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min",
        "bracket": web_path + "/plugins/bracket/js/bracket.min",
        "perfect-scrollbar.jquery": web_path + "/plugins/perfect-scrollbar/js/perfect-scrollbar.jquery.min",
        "peity": web_path + "/plugins/peity/jquery.peity.min",
        "moment": web_path + "/plugins/moment/moment.min",
        "moment-with-locales": web_path + "/plugins/moment/moment-with-locales.min",
        "jquery.showLoading": web_path + "/plugins/loading/js/jquery.showLoading.min"
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
require(["jquery", "requirejs-domready", "moment-with-locales", "handlebars", "ajax.loading.view", "bootstrap", "bracket", "csrf"],
    function ($, domready, moment, Handlebars, loadingView) {
        domready(function () {
            Messenger.options = {
                extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
                theme: 'flat'
            };

            moment.locale('zh-cn');

            var ajax_url = {
                user_notify: web_path + '/user/data/notify',
                user_notify_detail: web_path + '/user/notify/detail',
                user_notify_read: web_path + '/user/notify/read',
                system_notify: web_path + '/user/system/notify'
            };

            var user_notify_param_id = {
                userNotifyIcon: '#userNotifyIcon',
                userNotifyData: '#userNotifyData'
            };

            var system_notify_param_id = {
                systemNotifyData: '#systemNotifyData'
            };

            var user_notify_param = {
                pageNum: 0,
                length: 3,
                displayedPages: 3,
                orderColumnName: 'createDate',
                orderDir: 'desc',
                extraSearch: JSON.stringify({
                    isSee: 0
                })
            };

            var modal_id = {
                userNotifyModal:'#userNotifyModal'
            };

            init();

            function init() {
                initUserNotify();
                initSystemNotify();
            }

            // 轮询扫描通知
            setInterval(initUserNotify, 30000);

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
                    var template = Handlebars.compile($("#user-notify-template").html());

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
                        $('#userNotifyModalTitle').text(data.userNotify.notifyTitle).addClass('text-' + data.userNotify.notifyType);
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
                $.post(ajax_url.user_notify_read + '/' + id);
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
        });
    });