//# sourceURL=app_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/platform/app/save',
            page: '/web/menu/platform/app'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            clientId: '#clientId',
            secret: '#secret',
            appName: '#appName',
            oauthType:'#oauthType',
            webServerRedirectUri: '#webServerRedirectUri',
            remark: '#remark'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        /*
         参数
         */
        var param = {
            clientId: '',
            secret: '',
            appName: '',
            oauthType:'',
            webServerRedirectUri: '',
            remark: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.clientId = _.trim($(param_id.clientId).val());
            param.secret = _.trim($(param_id.secret).val());
            param.appName = _.trim($(param_id.appName).val());
            param.oauthType = Number($(param_id.oauthType).val());
            param.webServerRedirectUri = _.trim($(param_id.webServerRedirectUri).val());
            param.remark = _.trim($(param_id.remark).val());
        }

        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initMaxLength();
        }

        $(param_id.oauthType).change(function (){
            var v = Number($(this).val());
            if(v === 0){
                $(param_id.webServerRedirectUri).parent().css('display', '');
            } else {
                $(param_id.webServerRedirectUri).parent().css('display', 'none');
            }
        });

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.appName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.webServerRedirectUri).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.remark).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.appName).blur(function () {
            initParam();
            var appName = param.appName;
            if (appName.length <= 0 || appName.length > 100) {
                tools.validErrorDom(param_id.appName, '应用名100个字符以内');
            } else {
                tools.validSuccessDom(param_id.appName);
            }
        });

        $(param_id.webServerRedirectUri).blur(function () {
            initParam();
            var webServerRedirectUri = param.webServerRedirectUri;
            if (webServerRedirectUri.length <= 0 || webServerRedirectUri.length > 255) {
                tools.validErrorDom(param_id.webServerRedirectUri, '回调地址255个字符以内');
            } else {
                if (_.startsWith(webServerRedirectUri, 'http://') || _.startsWith(webServerRedirectUri, 'https://')) {
                    tools.validSuccessDom(param_id.webServerRedirectUri);
                } else {
                    tools.validErrorDom(param_id.webServerRedirectUri, '回调地址应以http://或https://开头');
                }
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validAppName();
        });

        /**
         * 添加时检验并提交数据
         */
        function validAppName() {
            var appName = param.appName;
            var oauthType = param.oauthType;
            if (appName.length <= 0 || appName.length > 100) {
                tools.validErrorDom(param_id.appName, '应用名100个字符以内');
            } else {
                tools.validSuccessDom(param_id.appName);
                if(oauthType === 0){
                    validWebServerRedirectUri();
                } else {
                    sendAjax();
                }
            }
        }

        function validWebServerRedirectUri() {
            var webServerRedirectUri = param.webServerRedirectUri;
            if (webServerRedirectUri.length <= 0 || webServerRedirectUri.length > 255) {
                tools.validErrorDom(param_id.webServerRedirectUri, '回调地址255个字符以内');
            } else {
                if (_.startsWith(webServerRedirectUri, 'http://') || _.startsWith(webServerRedirectUri, 'https://')) {
                    tools.validSuccessDom(param_id.webServerRedirectUri);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id.webServerRedirectUri, '回调地址应以http://或https://开头');
                }
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.save,
                data: param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.page);
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

    });