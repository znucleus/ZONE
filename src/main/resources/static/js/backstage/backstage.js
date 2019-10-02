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
        "bootstrap": web_path + "/assets/js/bootstrap.bundle.min",
        "bootstrap-switch": web_path + "/plugins/bootstrap-switch/js/bootstrap-switch.min",
        "waves": web_path + "/plugins/waves/waves.min",
        "jquery.nicescroll": web_path + "/assets/js/jquery.nicescroll",
        "jquery.core": web_path + "/assets/js/jquery.core",
        "jquery.app": web_path + "/assets/js/jquery.app",
        "hammerjs": web_path + "/plugins/hammer/hammer.min",
        "jquery.showLoading": web_path + "/plugins/loading/js/jquery.showLoading.min",
        "jquery.address": web_path + "/plugins/jquery-address/jquery.address-1.6.min",
        "ajax.loading.view": web_path + "/js/util/ajax.loading.view",
        "nav.active": web_path + "/js/util/nav.active",
        "dropify": web_path + "/plugins/fileuploads/js/dropify.min",
        "moment": web_path + "/plugins/moment/moment.min",
        "moment-with-locales": web_path + "/plugins/moment/moment-with-locales.min",
        "x-editable": web_path + "/plugins/x-editable/js/bootstrap-editable.min",
        "sweetalert2": web_path + "/plugins/sweetalert2/sweetalert2.all.min",
        "jquery.entropizer": web_path + "/plugins/jquery-entropizer/js/jquery-entropizer.min",
        "entropizer": web_path + "/plugins/jquery-entropizer/js/entropizer.min",
        "select2": web_path + "/plugins/select2/js/select2.min",
        "select2-zh-CN": web_path + "/plugins/select2/js/i18n/zh-CN.min",
        "bootstrap-datepicker": web_path + "/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min",
        "bootstrap-datepicker-zh-CN": web_path + "/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min",
        "bootstrap-inputmask": web_path + "/plugins/bootstrap-inputmask/bootstrap-inputmask.min",
        "jquery.simple-pagination": web_path + "/plugins/jquery-simple-pagination/jquery.simplePagination.min",
        "bootstrap-timepicker": web_path + "/plugins/timepicker/bootstrap-timepicker.min",
        "Handsontable": web_path + "/plugins/handsontable/handsontable.full.min",
        "Handsontable-zh-CN": web_path + "/plugins/handsontable/languages/all.min",
        "jquery-ui/widget": web_path + "/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.min",
        "jquery.iframe-transport": web_path + "/plugins/jquery-file-upload/js/jquery.iframe-transport.min",
        "jquery.fileupload-process": web_path + "/plugins/jquery-file-upload/js/jquery.fileupload-process.min",
        "jquery.fileupload": web_path + "/plugins/jquery-file-upload/js/jquery.fileupload.min",
        "jquery.fileupload-validate": web_path + "/plugins/jquery-file-upload/js/jquery.fileupload-validate.min",
        "tablesaw": web_path + "/plugins/tablesaw/tablesaw.jquery.min",
        "jquery-labelauty":web_path + "/plugins/jquery-labelauty/jquery-labelauty.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "bootstrap": {
            deps: ["jquery"]
        },
        "bootstrap-switch": {
            deps: ["jquery", "css!" + web_path + "/plugins/bootstrap-switch/css/bootstrap-switch.min"]
        },
        "waves": {
            deps: ["jquery", "css!" + web_path + "/plugins/waves/waves.min", "bootstrap"]
        },
        "jquery.nicescroll": {
            deps: ["jquery", "hammerjs"]
        },
        "jquery.core": {
            deps: ["jquery", "waves", "jquery.nicescroll", "bootstrap-switch"]
        },
        "jquery.app": {
            deps: ["jquery", "hammerjs"]
        },
        "messenger": {
            deps: ["jquery"]
        },
        "jquery.showLoading": {
            deps: ["jquery"]
        },
        "jquery.address": {
            deps: ["jquery"]
        },
        "dropify": {
            deps: ["jquery", "css!" + web_path + "/plugins/fileuploads/css/dropify.min"]
        },
        "x-editable": {
            deps: ["jquery", "css!" + web_path + "/plugins/x-editable/css/bootstrap-editable.min"]
        },
        "sweetalert2": {
            deps: ["jquery", "css!" + web_path + "/plugins/sweetalert2/sweetalert2.min"]
        },
        "jquery.entropizer": {
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-entropizer/css/jquery-entropizer.min"]
        },
        "select2-zh-CN": {
            deps: ["jquery", "select2", "css!" + web_path + "/plugins/select2/css/select2.min"]
        },
        "bootstrap-datepicker-zh-CN": {
            deps: ["jquery", "bootstrap-datepicker", "css!" + web_path + "/plugins/bootstrap-datepicker/css/bootstrap-datepicker.min"]
        },
        "jquery.simple-pagination": {
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-simple-pagination/simplePagination.min"]
        },
        "bootstrap-timepicker": {
            deps: ["jquery", "css!" + web_path + "/plugins/timepicker/bootstrap-timepicker.min"]
        },
        "Handsontable": {
            deps: ["css!" + web_path + "/plugins/handsontable/handsontable.full.min"]
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
        "tablesaw": {
            deps: ["jquery", "css!" + web_path + "/plugins/tablesaw/tablesaw.min"]
        },
        "jquery-labelauty":{
            deps: ["jquery", "css!" + web_path + "/plugins/jquery-labelauty/jquery-labelauty.min"]
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
require(["jquery", "requirejs-domready", "lodash", "tools", "ajax.loading.view", "nav.active", "moment-with-locales",
        "handlebars", "csrf", "jquery.core", "jquery.app", "jquery.address"],
    function ($, domready, _, tools, loadingView, navActive, moment, Handlebars) {
        domready(function () {
            Messenger.options = {
                extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
                theme: 'flat'
            };

            moment.locale('zh-cn');

            /*
             动态链接点击效果
             */
            $('.dy_href').click(function () {
                $.address.title($(this).text() + '-' + $('#webAppName').text());
                var href = $(this).attr('href');
                if(href !== 'javascript:;' && href !== '#'){
                    navActive(href.substring(1));
                }
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
                    if(!isInitAddress){
                        isInitOnAddress = false;
                    }
                    if(!isInitOnAddress){
                        loadingView(event.value, '#page-wrapper', web_path);
                    } else {
                        isInitOnAddress = false;
                    }
                }
            });

            function initAddressRefresh(){
                if(!isInitAddress){
                    var url = window.location.href;
                    if (url.lastIndexOf('#') > 0) {
                        isInitOnAddress = true;
                        loadingView(url.substring(url.lastIndexOf('#') + 1), '#page-wrapper', web_path);
                    }
                }
            }

            var ajax_url = {
                user_notify: web_path + '/user/data/notify',
                user_notify_detail: web_path + '/user/notify/detail',
                user_notify_read: web_path + '/user/notify/read',
                system_notify: web_path + '/user/system/notify'
            };

            var user_notify_param_id = {
                userNotifyIcon: '#userNotifyIcon',
                userNotifyNum: '#userNotifyNum',
                userNotifyData: '#userNotifyData'
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

            init();

            function init() {
                initUserNotify();
                initSystemNotify();
                initAddressRefresh();
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
                            $(user_notify_param_id.userNotifyIcon).addClass('noti-icon-badge');
                            $(user_notify_param_id.userNotifyNum).text(data.page.totalSize);
                        } else {
                            $(user_notify_param_id.userNotifyIcon).removeClass('noti-icon-badge');
                            $(user_notify_param_id.userNotifyNum).text('');
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

            var userNotifyData = $('#userNotifyData');

            $('#userNotifyModal').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget); // Button that triggered the modal
                var id = button.data('id'); // Extract info from data-* attributes

                $.ajax({
                    type: 'GET',
                    url: ajax_url.user_notify_detail + '/' + id,
                    success: function (data) {
                        $('#userNotifyModalTitle').text(data.userNotify.notifyTitle);
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

            function generateUserNotifyHtml(data) {
                var html = '';
                if (data.page.totalSize <= 0) {
                    html = '<h6 class="text-center">无消息</h6>';
                }
                $.each(data.listResult, function (i, n) {
                    html += '<a href="javascript:" class="dropdown-item notify-item" data-toggle="modal" data-target="#userNotifyModal" data-id="' + n.userNotifyId + '">';
                    html += '<div class="notify-icon bg-' + n.notifyType + '">';
                    if (n.notifyType === 'success') {
                        html += '<i class="icon-bubble"></i>';
                    } else if (n.notifyType === 'info') {
                        html += '<i class="icon-cup"></i>';
                    } else if (n.notifyType === 'warning') {
                        html += '<i class="icon-energy"></i>';
                    } else if (n.notifyType === 'danger') {
                        html += '<i class="icon-shield"></i>';
                    }
                    html += '</div>';
                    html += '<p class="notify-details">' + n.notifyTitle + '<small class="text-muted">' + moment(n.createDateStr, "YYYY-MM-DD HH:mm:ss").fromNow() + '</small></p>' + '</a>';
                });
                userNotifyData.html(html);
            }

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

                Handlebars.registerHelper('notify_title', function () {
                    return new Handlebars.SafeString(this.notifyTitle);
                });

                Handlebars.registerHelper('notify_content', function () {
                    return new Handlebars.SafeString(this.notifyContent);
                });

                $('#systemNotifyData').html(template(data));
            }
        });
    });