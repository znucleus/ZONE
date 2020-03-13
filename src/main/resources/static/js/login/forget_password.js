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
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min"
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
require(["jquery", "requirejs-domready", "lodash", "tools", "bootstrap", "csrf"],
    function ($, domready, _, tools) {
        domready(function () {
            //This function is called once the DOM is ready.
            //It will be safe to query the DOM and manipulate
            //DOM nodes in this function.

            var ajax_url = {
                forget_password_mail: web_path + '/forget_password/mail',
                forget_password_mobile: web_path + '/forget_password/mobile',
                forget_password_dynamic_password: web_path + '/forget_password/dynamic_password',
                forget_password_success: web_path + '/tip/forget_password/success',
                reset_password_mobile: web_path + '/anyone/reset_password/mobile',
                reset_password_dynamic_password: web_path + '/anyone/reset_password/dynamic_password',
                send_mobile: web_path + '/anyone/send/mobile',
                check_mobile_verification_code: web_path + '/anyone/check/mobile/code',
                check_exist_username: web_path + '/anyone/check/exist/username'
            };

            var param_id = {
                verificationMode: '#verificationMode',
                email: '#email',
                mobile: '#mobile',
                verificationCode: '#verificationCode',
                username: '#username',
                dynamicPassword: '#dynamicPassword'
            };

            var button_id = {
                sure: {
                    id: '#sure',
                    text: '确 定',
                    tip: '验证中...'
                }
            };

            var param = {
                verificationMode: '',
                email: '',
                mobile: '',
                verificationCode: '',
                username: '',
                dynamicPassword: ''
            };

            function initParam() {
                param.verificationMode = Number($(param_id.verificationMode).val());
                param.email = _.trim($(param_id.email).val());
                param.mobile = _.trim($(param_id.mobile).val());
                param.verificationCode = _.trim($(param_id.verificationCode).val());
                param.username = _.trim($(param_id.username).val());
                param.dynamicPassword = _.trim($(param_id.dynamicPassword).val());
            }

            $(param_id.verificationMode).change(function () {
                var v = Number($(this).val());
                if (v === 0) {
                    $(param_id.email).parent().css('display', 'block');
                    $(param_id.mobile).parent().css('display', 'none');
                    $(param_id.verificationCode).parent().parent().css('display', 'none');
                    $(param_id.username).parent().css('display', 'none');
                    $(param_id.dynamicPassword).parent().css('display', 'none');
                } else if (v === 1) {
                    $(param_id.email).parent().css('display', 'none');
                    $(param_id.mobile).parent().css('display', 'block');
                    $(param_id.verificationCode).parent().parent().css('display', 'block');
                    $(param_id.username).parent().css('display', 'none');
                    $(param_id.dynamicPassword).parent().css('display', 'none');
                } else {
                    $(param_id.email).parent().css('display', 'none');
                    $(param_id.mobile).parent().css('display', 'none');
                    $(param_id.verificationCode).parent().parent().css('display', 'none');
                    $(param_id.username).parent().css('display', 'block');
                    $(param_id.dynamicPassword).parent().css('display', 'block');
                }
            });

            $(param_id.email).keyup(function (event) {
                if (event.keyCode === 13) {
                    validEmail();
                }
            });

            $(param_id.mobile).keyup(function (event) {
                if (event.keyCode === 13) {
                    validMobile();
                }
            });

            $(param_id.verificationCode).keyup(function (event) {
                if (event.keyCode === 13) {
                    validMobile();
                }
            });

            $(param_id.username).keyup(function (event) {
                if (event.keyCode === 13) {
                    validUsername();
                }
            });

            $(param_id.dynamicPassword).keyup(function (event) {
                if (event.keyCode === 13) {
                    validUsername();
                }
            });

            /**
             * 获取手机验证码
             */
            var InterValObj; //timer变量，控制时间
            var count = 120; //间隔函数，1秒执行
            var curCount;//当前剩余秒数
            var btnId = '#sendCode';
            $(btnId).click(function () {
                initParam();
                var mobile = param.mobile;
                var regex = tools.regex.mobile;
                if (regex.test(mobile)) {
                    $.get(ajax_url.send_mobile, {mobile: mobile}, function (data) {
                        var verificationCodeHelp = $('#verificationCodeHelp');
                        if (data.state) {
                            curCount = count;
                            //设置button效果，开始计时
                            $(btnId).attr("disabled", "true");
                            $(btnId).text(curCount + "秒");
                            InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
                            $(param_id.verificationCode).removeClass('is-invalid');
                            verificationCodeHelp.removeClass('text-danger').addClass('text-muted').text(data.msg);
                        } else {
                            $(param_id.verificationCode).addClass('is-invalid');
                            verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text(data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.mobile, '手机号不正确');
                }
            });

            //timer处理函数
            function SetRemainTime() {
                if (curCount === 0) {
                    window.clearInterval(InterValObj);//停止计时器
                    $(btnId).removeAttr("disabled");//启用按钮
                    $(btnId).text("获取");
                } else {
                    curCount--;
                    $(btnId).text(curCount + "秒");
                }
            }

            $(button_id.sure.id).click(function () {
                initParam();
                var verificationMode = param.verificationMode;
                if (verificationMode === 0) {
                    validEmail();
                } else if (verificationMode === 1) {
                    validMobile()
                } else {
                    validUsername();
                }

            });

            function validEmail() {
                initParam();
                var email = param.email;
                if (email !== '') {
                    if (tools.regex.email.test(email)) {
                        tools.validSuccessDom(param_id.email);
                        sendMailAjax();
                    } else {
                        tools.validErrorDom(param_id.email, "邮箱格式不正确");
                    }
                } else {
                    tools.validErrorDom(param_id.email, "请填写您的邮箱");
                }
            }

            function validMobile() {
                initParam();
                var mobile = param.mobile;
                if (mobile !== '') {
                    if (tools.regex.mobile.test(mobile)) {
                        tools.validSuccessDom(param_id.mobile);
                        validVerificationCode();
                    } else {
                        tools.validErrorDom(param_id.mobile, "手机号不正确");
                    }
                } else {
                    tools.validErrorDom(param_id.mobile, "请填写您的手机号");
                }
            }

            function validVerificationCode() {
                initParam();
                var verificationCode = param.verificationCode;
                var mobile = param.mobile;
                var verificationCodeHelp = $('#verificationCodeHelp');
                if (verificationCode !== '') {
                    $.post(ajax_url.check_mobile_verification_code, {
                        mobile: mobile,
                        verificationCode: verificationCode
                    }, function (data) {
                        if (data.state) {
                            $(param_id.verificationCode).removeClass('is-invalid');
                            verificationCodeHelp.removeClass('text-danger').addClass('text-muted').text('');
                            sendMobileAjax();
                        } else {
                            $(param_id.verificationCode).addClass('is-invalid');
                            verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text(data.msg);
                        }
                    });
                } else {
                    $(param_id.verificationCode).addClass('is-invalid');
                    verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text("请填写验证码");
                }
            }

            function validUsername() {
                initParam();
                var username = param.username;
                if (username !== '') {
                    $.post(ajax_url.check_exist_username, {
                        username: param.username
                    }, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.username);
                            validDynamicPassword();
                        } else {
                            tools.validErrorDom(param_id.username, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.username, "请填写账号");
                }
            }

            function validDynamicPassword() {
                initParam();
                var dynamicPassword = param.dynamicPassword;
                if (dynamicPassword !== '') {
                    tools.validSuccessDom(param_id.dynamicPassword);
                    sendDynamicPassword();
                } else {
                    tools.validErrorDom(param_id.dynamicPassword, "请填写动态密码");
                }
            }

            function sendMailAjax() {
                // 显示遮罩
                tools.buttonLoading(button_id.sure.id, button_id.sure.tip);
                $.post(ajax_url.forget_password_mail, param, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.sure.id, button_id.sure.text);
                    var globalError = $('#globalError');
                    if (data.state) {
                        globalError.text('');
                        window.location.href = ajax_url.forget_password_success;
                    } else {
                        globalError.text(data.msg);
                    }
                });
            }

            function sendMobileAjax() {
                // 显示遮罩
                tools.buttonLoading(button_id.sure.id, button_id.sure.tip);
                $.post(ajax_url.forget_password_mobile, param, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.sure.id, button_id.sure.text);
                    var globalError = $('#globalError');
                    if (data.state) {
                        globalError.text('');
                        window.location.href = ajax_url.reset_password_mobile;
                    } else {
                        globalError.text(data.msg);
                    }
                });
            }

            function sendDynamicPassword() {
                // 显示遮罩
                tools.buttonLoading(button_id.sure.id, button_id.sure.tip);
                $.post(ajax_url.forget_password_dynamic_password, param, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.sure.id, button_id.sure.text);
                    var globalError = $('#globalError');
                    if (data.state) {
                        globalError.text('');
                        window.location.href = ajax_url.reset_password_dynamic_password;
                    } else {
                        globalError.text(data.msg);
                    }
                });
            }

        });
    });