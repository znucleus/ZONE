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
        "jquery.entropizer": web_path + "/plugins/jquery-entropizer/js/jquery-entropizer.min",
        "entropizer": web_path + "/plugins/jquery-entropizer/js/entropizer.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "bootstrap": {
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
require(["jquery", "requirejs-domready", "lodash", "tools", "bootstrap", "csrf", "jquery.entropizer"],
    function ($, domready, _, tools) {
        domready(function () {
            //This function is called once the DOM is ready.
            //It will be safe to query the DOM and manipulate
            //DOM nodes in this function.

            var ajax_url = {
                reset_password: web_path + '/reset_password',
                reset_password_success: web_path + '/tip/reset_password/success'
            };

            var param_id = {
                username: '#username',
                password: '#password',
                okPassword: '#okPassword',
                verificationMode: '#verificationMode',
                passwordResetKey: '#passwordResetKey'
            };

            var button_id = {
                sure: {
                    id: '#sure',
                    text: '确 定',
                    tip: '提交中...'
                }
            };

            var param = {
                username: '',
                password: '',
                okPassword: '',
                verificationMode: '',
                passwordResetKey: ''
            };

            var global_param = {
                password_strong: 0
            };

            function initParam() {
                param.username = _.trim($(param_id.username).val());
                param.password = _.trim($(param_id.password).val());
                param.okPassword = _.trim($(param_id.okPassword).val());
                param.verificationMode = _.trim($(param_id.verificationMode).val());
                param.passwordResetKey = _.trim($(param_id.passwordResetKey).val());
            }

            init();

            function init() {
                initPassword();
            }

            function initPassword() {
                // 密码强度检测
                $('#meter2').entropizer({
                    target: param_id.password,
                    update: function (data, ui) {
                        global_param.password_strong = data.percent;
                        ui.bar.css({
                            'background-color': data.color,
                            'width': data.percent + '%'
                        });
                    }
                });
            }

            $(param_id.password).keyup(function (event) {
                if (event.keyCode === 13) {
                    validPassword();
                }
            });

            $(param_id.okPassword).keyup(function (event) {
                if (event.keyCode === 13) {
                    validPassword();
                }
            });

            $(button_id.sure.id).click(function () {
                validPassword();
            });

            function validPassword() {
                initParam();
                var password = param.password;
                if (password !== '') {
                    if (tools.regex.password.test(password)) {
                        if (global_param.password_strong < 55) {
                            tools.validErrorDom(param_id.password, '密码过于简单');
                        } else {
                            tools.validSuccessDom(param_id.password);
                            validOkPassword();
                        }
                    } else {
                        tools.validErrorDom(param_id.password, "密码格式不正确");
                    }
                } else {
                    tools.validErrorDom(param_id.password, "请填写您的密码");
                }
            }

            function validOkPassword() {
                initParam();
                var okPassword = param.okPassword;
                var password = param.password;
                if (okPassword !== '') {
                    if (okPassword === password) {
                        tools.validSuccessDom(param_id.okPassword);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.okPassword, "密码不一致");
                    }
                } else {
                    tools.validErrorDom(param_id.okPassword, "请确认您的密码");
                }
            }

            function sendAjax() {
                // 显示遮罩
                tools.buttonLoading(button_id.sure.id, button_id.sure.tip);
                $.post(ajax_url.reset_password, param, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.sure.id, button_id.sure.text);
                    var globalError = $('#globalError');
                    if (data.state) {
                        globalError.text('');
                        window.location.href = ajax_url.reset_password_success;
                    } else {
                        globalError.text(data.msg);
                    }
                });
            }
        });
    });