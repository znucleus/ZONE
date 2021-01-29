//# sourceURL=users_setting.js
require(["jquery", "lodash", "tools", "sweetalert2", "clipboard", "bootstrap",
        "csrf", "select2-zh-CN", "jquery.entropizer", "jquery-toggles", "bootstrap"],
    function ($, _, tools, Swal, ClipboardJS) {

        $('[data-toggle="tooltip"]').tooltip();

        var ajax_url = {
            users_update: web_path + '/users/update',
            check_password: web_path + '/users/check-password',
            system_configure: web_path + '/anyone/data/configure',
            check_email: web_path + '/users/check-email',
            check_mobile: web_path + '/users/check-mobile',
            send_mobile: web_path + '/anyone/send/mobile',
            check_mobile_verification_code: web_path + '/anyone/check/mobile/code',
            obtain_mobile_code_valid: web_path + '/anyone/data/mobile/code',
            users_password_update: web_path + '/users/password/update',
            open_google_oauth: web_path + "/users/open-google-oauth",
            close_google_oauth: web_path + "/users/close-google-oauth"
        };

        var param_id = {
            username: '#username',
            defaultUsername: '#defaultUsername',
            emailPasswordMode: '#emailPasswordMode',
            emailPassword: '#emailPassword',
            emailPasswordDynamicPassword: '#emailPasswordDynamicPassword',
            email: '#email',
            mobilePasswordMode: '#mobilePasswordMode',
            mobilePassword: '#mobilePassword',
            mobilePasswordDynamicPassword: '#mobilePasswordDynamicPassword',
            mobile: '#mobile',
            idCard: '#idCard',
            defaultIdCard: '#defaultIdCard',
            plaintextIdCard: '#plaintextIdCard',
            verificationCode: '#verificationCode',
            googleOauthPassword: '#googleOauthPassword',
            googleOauthKey: '#googleOauthKey',
            closeGoogleOauthMode: '#closeGoogleOauthMode',
            closeGoogleOauthPassword: '#closeGoogleOauthPassword',
            closeGoogleOauthDynamicPassword: '#closeGoogleOauthDynamicPassword',
            oldPassword: '#oldPassword',
            newPassword: '#newPassword',
            confirmPassword: '#confirmPassword'
        };

        var button_id = {
            editUsername: {
                text: '修改',
                id: '#editUsername'
            },
            updateUsername: {
                tip: '更新中...',
                text: '确定',
                id: '#updateUsername'
            },
            cancelUpdateUsername: {
                text: '取消',
                id: '#cancelUpdateUsername'
            },
            editIdCard: {
                text: '修改',
                id: '#editIdCard'
            },
            updateIdCard: {
                tip: '更新中...',
                text: '确定',
                id: '#updateIdCard'
            },
            cancelUpdateIdCard: {
                text: '取消',
                id: '#cancelUpdateIdCard'
            },
            saveEmail: {
                text: '确定',
                tip: '保存中...',
                id: '#saveEmail'
            },
            saveMobile: {
                text: '确定',
                tip: '保存中...',
                id: '#saveMobile'
            },
            password: {
                tip: '更新中...',
                text: '保存',
                id: '#updatePassword'
            },
            googleOauthSwitch: {
                id: '#googleOauthSwitch'
            },
            openGoogleOauth: {
                tip: '开启中...',
                text: '确定',
                id: '#openGoogleOauth'
            },
            closeGoogleOauth: {
                tip: '关闭中...',
                text: '确定',
                id: '#closeGoogleOauth'
            }
        };

        var param = {
            username: '',
            emailPasswordMode: '',
            emailPassword: '',
            emailPasswordDynamicPassword: '',
            email: '',
            mobilePasswordMode: '',
            mobilePassword: '',
            mobilePasswordDynamicPassword: '',
            mobile: '',
            idCard: '',
            googleOauthPassword: '',
            googleOauthKey: '',
            closeGoogleOauthMode: '',
            closeGoogleOauthPassword: '',
            closeGoogleOauthDynamicPassword: '',
            verificationCode: '',
            oldPassword: '',
            newPassword: '',
            confirmPassword: '',
        };

        var configure = {
            FORBIDDEN_REGISTER: ''
        };

        var global_param = {
            password_strong: 0
        };

        init();

        function init() {
            initSystemConfigure();
            initPassword();
            initGoogleOauthSwitch();
        }

        function initSystemConfigure() {
            $.get(ajax_url.system_configure, function (data) {
                configure.FORBIDDEN_REGISTER = data.FORBIDDEN_REGISTER;
            });
        }

        function initPassword() {
            // 密码强度检测
            var meter = $('#meter2').entropizer({
                target: param_id.newPassword,
                update: function (data, ui) {
                    global_param.password_strong = data.percent;
                    ui.bar.css({
                        'background-color': data.color,
                        'width': data.percent + '%'
                    });
                }
            });
        }

        function initGoogleOauthSwitch() {
            $(button_id.googleOauthSwitch.id).toggles({
                on: $('#googleOauth').text() !== '未开启',
                height: 26,
                text: {
                    on: '开', // text for the ON position
                    off: '关' // and off
                }
            });

            new ClipboardJS('#copyGoogleOauthKey');
        }

        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.emailPasswordMode = Number($(param_id.emailPasswordMode).val());
            param.emailPassword = _.trim($(param_id.emailPassword).val());
            param.emailPasswordDynamicPassword = _.trim($(param_id.emailPasswordDynamicPassword).val());
            param.email = _.trim($(param_id.email).val());
            param.mobilePasswordMode = Number($(param_id.mobilePasswordMode).val());
            param.mobilePassword = _.trim($(param_id.mobilePassword).val());
            param.mobilePasswordDynamicPassword = _.trim($(param_id.mobilePasswordDynamicPassword).val());
            param.mobile = _.trim($(param_id.mobile).val());
            param.verificationCode = _.trim($(param_id.verificationCode).val());
            param.idCard = _.trim($(param_id.idCard).val());
            param.googleOauthPassword = _.trim($(param_id.googleOauthPassword).val());
            param.googleOauthKey = _.trim($(param_id.googleOauthKey).val());
            param.closeGoogleOauthMode = Number($(param_id.closeGoogleOauthMode).val());
            param.closeGoogleOauthPassword = _.trim($(param_id.closeGoogleOauthPassword).val());
            param.closeGoogleOauthDynamicPassword = _.trim($(param_id.closeGoogleOauthDynamicPassword).val());
            param.oldPassword = _.trim($(param_id.oldPassword).val());
            param.newPassword = _.trim($(param_id.newPassword).val());
            param.confirmPassword = _.trim($(param_id.confirmPassword).val());
        }

        $(button_id.editUsername.id).click(function () {
            $(this).prop('hidden', true);
            $(button_id.updateUsername.id).prop('hidden', false);
            $(button_id.cancelUpdateUsername.id).prop('hidden', false);
            $(param_id.username).prop('readonly', false).removeClass('form-control-plaintext').addClass('form-control');
        });

        $(button_id.cancelUpdateUsername.id).click(function () {
            cancelUpdateUsername();
        });

        function cancelUpdateUsername() {
            $(button_id.cancelUpdateUsername.id).prop('hidden', true);
            $(button_id.updateUsername.id).prop('hidden', true);
            $(button_id.editUsername.id).prop('hidden', false);
            $(param_id.username).prop('readonly', true).removeClass('form-control').addClass('form-control-plaintext').val($(param_id.defaultUsername).val());
            tools.validSuccessDom(param_id.username);
        }

        $(button_id.updateUsername.id).click(function () {
            initParam();
            var username = param.username;
            if (username === '') {
                tools.validErrorDom(param_id.username, '账号不能为空');
            } else {
                // 禁止注册系统账号
                var forbiddenRegister = configure.FORBIDDEN_REGISTER.split(',');
                var isForbidden = false;
                for (var i = 0; i < forbiddenRegister.length; i++) {
                    if (forbiddenRegister[i] === _.toUpper(username)) {
                        isForbidden = true;
                        break;
                    }
                }
                if (isForbidden) {
                    tools.validErrorDom(param_id.username, '账号已被注册');
                } else {
                    // 只能是字母或数字
                    var regex = tools.regex.username;
                    if (!regex.test(username)) {
                        tools.validErrorDom(param_id.username, '账号1~20位英文或数字');
                    } else {
                        tools.validSuccessDom(param_id.username);
                        tools.buttonLoading(button_id.updateUsername.id, button_id.updateUsername.tip);
                        $.post(ajax_url.users_update, {
                            name: 'username',
                            value: username
                        }, function (data) {
                            tools.buttonEndLoading(button_id.updateUsername.id, button_id.updateUsername.text);
                            if (data.state) {
                                cancelUpdateUsername();
                                Swal.fire({
                                    title: data.msg,
                                    type: "success",
                                    confirmButtonText: "确定",
                                    preConfirm: function () {
                                        $('#logout').submit();
                                    }
                                });
                            } else {
                                Swal.fire('更新失败', data.msg, 'error');
                            }
                        });
                    }
                }

            }
        });

        $(param_id.emailPasswordMode).change(function () {
            var v = Number($(this).val());
            if (v === 0) {
                $(param_id.emailPassword).parent().parent().css('display', '');
                $(param_id.emailPasswordDynamicPassword).parent().parent().css('display', 'none');
            } else {
                $(param_id.emailPassword).parent().parent().css('display', 'none');
                $(param_id.emailPasswordDynamicPassword).parent().parent().css('display', '');
            }
        });

        $(param_id.emailPassword).blur(function () {
            initParam();
            var emailPassword = param.emailPassword;
            if (emailPassword !== '') {
                $.post(ajax_url.check_password, {password: emailPassword}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.emailPassword);
                    } else {
                        tools.validErrorDom(param_id.emailPassword, '密码错误');
                    }
                });
            } else {
                tools.validSuccessDom(param_id.emailPassword);
            }
        });

        $(param_id.email).blur(function () {
            initParam();
            var email = param.email;
            if (email !== '') {
                var regex = tools.regex.email;
                if (!regex.test(email)) {
                    tools.validErrorDom(param_id.email, '邮箱格式不正确');
                    return;
                }

                $.post(ajax_url.check_email, {email: email}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.email);
                    } else {
                        tools.validErrorDom(param_id.email, data.msg);
                    }
                });
            } else {
                tools.validSuccessDom(param_id.email);
            }
        });

        $(button_id.saveEmail.id).click(function () {
            initParam();
            validEmailPassword();
        });

        function validEmailPassword() {
            var emailPasswordMode = param.emailPasswordMode;
            if (emailPasswordMode === 0) {
                var emailPassword = param.emailPassword;
                if (emailPassword !== '') {
                    $.post(ajax_url.check_password, {password: emailPassword}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.emailPassword);
                            validEmail();
                        } else {
                            tools.validErrorDom(param_id.emailPassword, '密码错误');
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.emailPassword, '请填写密码');
                }
            } else {
                var emailPasswordDynamicPassword = param.emailPasswordDynamicPassword;
                if (emailPasswordDynamicPassword !== '') {
                    tools.validSuccessDom(param_id.emailPasswordDynamicPassword);
                    validEmail();
                } else {
                    tools.validErrorDom(param_id.closeGoogleOauthDynamicPassword, "请填写动态密码");
                }
            }
        }

        function validEmail() {
            var email = param.email;
            if (email !== '') {
                var regex = tools.regex.email;
                if (!regex.test(email)) {
                    tools.validErrorDom(param_id.email, '邮箱格式不正确');
                    return;
                }

                $.post(ajax_url.check_email, {email: email}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.email);
                        saveEmail();
                    } else {
                        tools.validErrorDom(param_id.email, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.email, "您的邮箱？");
            }
        }

        function saveEmail() {
            // 显示遮罩
            tools.buttonLoading(button_id.saveEmail.id, button_id.saveEmail.tip);
            $.post(ajax_url.users_update, {
                name: 'email',
                value: param.email,
                password: param.emailPassword,
                mode: param.emailPasswordMode,
                dynamicPassword: param.emailPasswordDynamicPassword
            }, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.saveEmail.id, button_id.saveEmail.text);
                var globalError = $('#globalEmailError');
                if (data.state) {
                    globalError.text('');
                    $('#emailModal').modal('hide');
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    globalError.text(data.msg);
                }
            });
        }

        $(button_id.editIdCard.id).click(function () {
            $(this).prop('hidden', true);
            $(button_id.updateIdCard.id).prop('hidden', false);
            $(button_id.cancelUpdateIdCard.id).prop('hidden', false);
            $(param_id.idCard).prop('readonly', false).removeClass('form-control-plaintext').addClass('form-control').val($(param_id.plaintextIdCard).val());
        });

        $(button_id.cancelUpdateIdCard.id).click(function () {
            cancelUpdateIdCard();
        });

        function cancelUpdateIdCard() {
            $(button_id.cancelUpdateIdCard.id).prop('hidden', true);
            $(button_id.updateIdCard.id).prop('hidden', true);
            $(button_id.editIdCard.id).prop('hidden', false);
            $(param_id.idCard).prop('readonly', true).removeClass('form-control').addClass('form-control-plaintext').val($(param_id.defaultIdCard).val());
            tools.validSuccessDom(param_id.idCard);
        }

        $(button_id.updateIdCard.id).click(function () {
            initParam();
            var idCard = param.idCard;
            if (idCard === '') {
                tools.validErrorDom(param_id.idCard, '身份证号不能为空');
            } else {
                if (!tools.regex.idCard.test(idCard)) {
                    tools.validErrorDom(param_id.idCard, '身份证号格式不正确');
                } else {
                    tools.validSuccessDom(param_id.idCard);
                    tools.buttonLoading(button_id.updateIdCard.id, button_id.updateIdCard.tip);
                    $.post(ajax_url.users_update, {
                        name: 'idCard',
                        value: idCard
                    }, function (data) {
                        tools.buttonEndLoading(button_id.updateIdCard.id, button_id.updateIdCard.text);
                        if (data.state) {
                            cancelUpdateIdCard();
                            Swal.fire({
                                title: data.msg,
                                type: "success",
                                confirmButtonText: "确定",
                                preConfirm: function () {
                                    $('#logout').submit();
                                }
                            });
                        } else {
                            Swal.fire('更新失败', data.msg, 'error');
                        }
                    });
                }
            }
        });

        $(param_id.mobilePasswordMode).change(function () {
            var v = Number($(this).val());
            if (v === 0) {
                $(param_id.mobilePassword).parent().parent().css('display', '');
                $(param_id.mobilePasswordDynamicPassword).parent().parent().css('display', 'none');
            } else {
                $(param_id.mobilePassword).parent().parent().css('display', 'none');
                $(param_id.mobilePasswordDynamicPassword).parent().parent().css('display', '');
            }
        });

        $(param_id.mobilePassword).blur(function () {
            initParam();
            var mobilePassword = param.mobilePassword;
            if (mobilePassword !== '') {
                $.post(ajax_url.check_password, {password: mobilePassword}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.mobilePassword);
                    } else {
                        tools.validErrorDom(param_id.mobilePassword, '密码错误');
                    }
                });
            } else {
                tools.validSuccessDom(param_id.mobilePassword);
            }
        });

        $(param_id.mobile).blur(function () {
            initParam();
            var mobile = param.mobile;
            if (mobile !== '') {
                var regex = tools.regex.mobile;
                if (!regex.test(mobile)) {
                    tools.validErrorDom(param_id.mobile, '手机号不正确');
                    return;
                }

                $.post(ajax_url.check_mobile, {mobile: mobile}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.mobile);
                    } else {
                        tools.validErrorDom(param_id.mobile, data.msg);
                    }
                });
            } else {
                tools.validSuccessDom(param_id.mobile);
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
                $.post(ajax_url.check_mobile, {mobile: mobile}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.mobile);
                        $.get(ajax_url.send_mobile, {mobile: mobile}, function (data) {
                            var verificationCodeHelp = $('#verificationCodeHelp');
                            if (data.state) {
                                curCount = count;
                                //设置button效果，开始计时
                                $(btnId).attr("disabled", "true");
                                $(btnId).text(curCount + "秒");
                                InterValObj = window.setInterval(setRemainTime, 1000); //启动计时器，1秒执行一次
                                $(param_id.verificationCode).removeClass('is-invalid');
                                verificationCodeHelp.removeClass('text-danger').addClass('text-muted').text(data.msg);
                            } else {
                                $(param_id.verificationCode).addClass('is-invalid');
                                verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text(data.msg);
                            }
                        });
                    } else {
                        tools.validErrorDom(param_id.mobile, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.mobile, '手机号不正确');
            }
        });

        //timer处理函数
        function setRemainTime() {
            if (curCount === 0) {
                window.clearInterval(InterValObj);//停止计时器
                $(btnId).removeAttr("disabled");//启用按钮
                $(btnId).text("获取");
            } else {
                curCount--;
                $(btnId).text(curCount + "秒");
            }
        }

        $(param_id.verificationCode).blur(function () {
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
                    } else {
                        $(param_id.verificationCode).addClass('is-invalid');
                        verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text(data.msg);
                    }
                });
            } else {
                $(param_id.verificationCode).removeClass('is-invalid');
                verificationCodeHelp.removeClass('text-danger').addClass('text-muted').text('');
            }
        });

        $(button_id.saveMobile.id).click(function () {
            initParam();
            validMobilePassword();
        });

        function validMobilePassword() {
            var mobilePasswordMode = param.mobilePasswordMode;
            if (mobilePasswordMode === 0) {
                var mobilePassword = param.mobilePassword;
                if (mobilePassword !== '') {
                    $.post(ajax_url.check_password, {password: mobilePassword}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.mobilePassword);
                            validMobile();
                        } else {
                            tools.validErrorDom(param_id.mobilePassword, '密码错误');
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.mobilePassword, '请填写密码');
                }
            } else {
                var mobilePasswordDynamicPassword = param.mobilePasswordDynamicPassword;
                if (mobilePasswordDynamicPassword !== '') {
                    tools.validSuccessDom(param_id.mobilePasswordDynamicPassword);
                    validMobile();
                } else {
                    tools.validErrorDom(param_id.mobilePasswordDynamicPassword, "请填写动态密码");
                }
            }
        }

        function validMobile() {
            var mobile = param.mobile;
            if (mobile !== '') {
                var regex = tools.regex.mobile;
                if (!regex.test(mobile)) {
                    tools.validErrorDom(param_id.mobile, '手机号不正确');
                    return;
                }

                $.post(ajax_url.check_mobile, {mobile: mobile}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.mobile);
                        validVerificationCode();
                    } else {
                        tools.validErrorDom(param_id.mobile, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.mobile, "您的手机号？");
            }
        }

        function validVerificationCode() {
            var verificationCode = param.verificationCode;
            var mobile = param.mobile;
            var verificationCodeHelp = $('#verificationCodeHelp');
            if (verificationCode !== '') {
                $.post(ajax_url.obtain_mobile_code_valid, {
                    mobile: mobile
                }, function (data) {
                    if (data.state) {
                        $(param_id.verificationCode).removeClass('is-invalid');
                        verificationCodeHelp.removeClass('text-danger').addClass('text-muted').text('');
                        saveMobile();
                    } else {
                        $(param_id.verificationCode).addClass('is-invalid');
                        verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text(data.msg);
                    }
                });
            } else {
                $(param_id.verificationCode).addClass('is-invalid');
                verificationCodeHelp.addClass('text-danger').removeClass('text-muted').text('请填写验证码');
            }
        }

        function saveMobile() {
            // 显示遮罩
            tools.buttonLoading(button_id.saveMobile.id, button_id.saveMobile.tip);
            $.post(ajax_url.users_update, {
                name: 'mobile',
                value: param.mobile,
                password: param.mobilePassword,
                mode: param.mobilePasswordMode,
                dynamicPassword: param.mobilePasswordDynamicPassword
            }, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.saveMobile.id, button_id.saveMobile.text);
                var globalError = $('#globalMobileError');
                if (data.state) {
                    globalError.text('');
                    $('#mobileModal').modal('hide');
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    globalError.text(data.msg);
                }
            });
        }

        var googleOauthSwitch = true;
        $(button_id.googleOauthSwitch.id).on('toggle', function (e, active) {
            if (googleOauthSwitch) {
                var myToggle = $(button_id.googleOauthSwitch.id).data('toggles');
                if (active) {
                    $('#openGoogleOauthModal').modal('show');
                    googleOauthSwitch = false;
                    myToggle.toggle(false);
                } else {
                    $('#closeGoogleOauthModal').modal('show');
                    googleOauthSwitch = false;
                    myToggle.toggle(true);
                }
            }
        });

        $(param_id.googleOauthPassword).blur(function () {
            initParam();
            var googleOauthPassword = param.googleOauthPassword;
            if (googleOauthPassword !== '') {
                $.post(ajax_url.check_password, {password: googleOauthPassword}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.googleOauthPassword);
                    } else {
                        tools.validErrorDom(param_id.googleOauthPassword, '密码错误');
                    }
                });
            } else {
                tools.validSuccessDom(param_id.googleOauthPassword);
            }
        });

        $(button_id.openGoogleOauth.id).click(function () {
            initParam();
            validGoogleOauthPassword();
        });

        function validGoogleOauthPassword() {
            var googleOauthPassword = param.googleOauthPassword;
            if (googleOauthPassword !== '') {
                $.post(ajax_url.check_password, {password: googleOauthPassword}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.googleOauthPassword);
                        openGoogleOauth();
                    } else {
                        tools.validErrorDom(param_id.googleOauthPassword, '密码错误');
                    }
                });
            } else {
                tools.validErrorDom(param_id.googleOauthPassword, '请填写密码');
            }
        }

        function openGoogleOauth() {
            // 显示遮罩
            tools.buttonLoading(button_id.openGoogleOauth.id, button_id.openGoogleOauth.tip);
            $.post(ajax_url.open_google_oauth, {
                password: param.googleOauthPassword
            }, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.openGoogleOauth.id, button_id.openGoogleOauth.text);
                var globalError = $('#globalGoogleOauthError');
                if (data.state) {
                    globalError.text('');
                    var myToggle = $(button_id.googleOauthSwitch.id).data('toggles');
                    myToggle.toggle(true);
                    $('#googleOauth').text('已开启');
                    $(button_id.openGoogleOauth.id).css('display', 'none');
                    $(button_id.openGoogleOauth.id).next().text('关闭');
                    $(param_id.googleOauthPassword).parent().parent().css('display', 'none');
                    $(param_id.googleOauthKey).parent().parent().parent().css('display', '');
                    $(param_id.googleOauthKey).val(data.googleOauthKey);

                } else {
                    globalError.text(data.msg);
                }
            });
        }

        $('#openGoogleOauthModal').on('show.bs.modal', function (e) {
            googleOauthSwitch = false;
        }).on('hidden.bs.modal', function (e) {
            googleOauthSwitch = true;
        });

        $(param_id.closeGoogleOauthMode).change(function () {
            var v = Number($(this).val());
            if (v === 0) {
                $(param_id.closeGoogleOauthPassword).parent().parent().css('display', '');
                $(param_id.closeGoogleOauthDynamicPassword).parent().parent().css('display', 'none');
            } else {
                $(param_id.closeGoogleOauthPassword).parent().parent().css('display', 'none');
                $(param_id.closeGoogleOauthDynamicPassword).parent().parent().css('display', '');
            }
        });

        $(param_id.closeGoogleOauthPassword).blur(function () {
            initParam();
            var closeGoogleOauthPassword = param.closeGoogleOauthPassword;
            if (closeGoogleOauthPassword !== '') {
                $.post(ajax_url.check_password, {password: closeGoogleOauthPassword}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.closeGoogleOauthPassword);
                    } else {
                        tools.validErrorDom(param_id.closeGoogleOauthPassword, '密码错误');
                    }
                });
            } else {
                tools.validSuccessDom(param_id.closeGoogleOauthPassword);
            }
        });

        $(button_id.closeGoogleOauth.id).click(function () {
            initParam();
            validCloseGoogleOauthPassword();
        });

        function validCloseGoogleOauthPassword() {
            var closeGoogleOauthMode = param.closeGoogleOauthMode;
            if (closeGoogleOauthMode === 0) {
                var closeGoogleOauthPassword = param.closeGoogleOauthPassword;
                if (closeGoogleOauthPassword !== '') {
                    $.post(ajax_url.check_password, {password: closeGoogleOauthPassword}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.closeGoogleOauthPassword);
                            closeGoogleOauth();
                        } else {
                            tools.validErrorDom(param_id.closeGoogleOauthPassword, '密码错误');
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.closeGoogleOauthPassword, "请填写密码");
                }
            } else {
                var closeGoogleOauthDynamicPassword = param.closeGoogleOauthDynamicPassword;
                if (closeGoogleOauthDynamicPassword !== '') {
                    tools.validSuccessDom(param_id.closeGoogleOauthDynamicPassword);
                    closeGoogleOauth();
                } else {
                    tools.validErrorDom(param_id.closeGoogleOauthDynamicPassword, "请填写动态密码");
                }
            }
        }

        function closeGoogleOauth() {
            // 显示遮罩
            tools.buttonLoading(button_id.closeGoogleOauth.id, button_id.closeGoogleOauth.tip);
            $.post(ajax_url.close_google_oauth, {
                mode: param.closeGoogleOauthMode,
                password: param.closeGoogleOauthPassword,
                dynamicPassword: param.closeGoogleOauthDynamicPassword
            }, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.closeGoogleOauth.id, button_id.closeGoogleOauth.text);
                var globalError = $('#globalCloseGoogleOauthError');
                if (data.state) {
                    globalError.text('');
                    var myToggle = $(button_id.googleOauthSwitch.id).data('toggles');
                    myToggle.toggle(false);
                    $('#googleOauth').text('未开启');
                    $(button_id.openGoogleOauth.id).css('display', '');
                    $(button_id.openGoogleOauth.id).next().text('取消');
                    $(param_id.googleOauthPassword).parent().parent().css('display', '');
                    $(param_id.googleOauthKey).parent().parent().parent().css('display', 'none');
                    $(param_id.googleOauthKey).val('');

                    $('#closeGoogleOauthModal').modal('hide');
                    Swal.fire('关闭双因素认证', data.msg, 'success');

                } else {
                    globalError.text(data.msg);
                }
            });
        }

        $('#closeGoogleOauthModal').on('show.bs.modal', function (e) {
            googleOauthSwitch = false;
        }).on('hidden.bs.modal', function (e) {
            googleOauthSwitch = true;
        });

        $(button_id.password.id).click(function () {
            initParam();
            validOldPassword();
        });

        function validOldPassword() {
            var oldPassword = param.oldPassword;
            if (oldPassword !== '') {
                $.post(ajax_url.check_password, {password: oldPassword}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.oldPassword);
                        validNewPassword();
                    } else {
                        tools.validErrorDom(param_id.oldPassword, '密码错误');
                    }
                });
            } else {
                tools.validErrorDom(param_id.oldPassword, '请填写密码');
            }
        }

        function validNewPassword() {
            var newPassword = param.newPassword;
            var oldPassword = param.oldPassword;
            if (newPassword !== '') {
                if (newPassword !== oldPassword) {
                    if (tools.regex.password.test(newPassword)) {
                        if (global_param.password_strong < 55) {
                            tools.validErrorDom(param_id.newPassword, '密码过于简单');
                        } else {
                            tools.validSuccessDom(param_id.newPassword);
                            validConfirmPassword();
                        }
                    } else {
                        tools.validErrorDom(param_id.newPassword, '密码至少包含数字与字母，可使用特殊符号，长度6~20位');
                    }
                } else {
                    tools.validErrorDom(param_id.newPassword, '不能与旧密码一致');
                }
            } else {
                tools.validErrorDom(param_id.newPassword, '请填写新密码');
            }
        }

        function validConfirmPassword() {
            var newPassword = param.newPassword;
            var confirmPassword = param.confirmPassword;
            if (confirmPassword !== '') {
                if (newPassword === confirmPassword) {
                    if (tools.regex.password.test(confirmPassword)) {
                        tools.validSuccessDom(param_id.confirmPassword);
                        updatePassword();
                    } else {
                        tools.validErrorDom(param_id.confirmPassword, '密码至少包含数字与字母，可使用特殊符号，长度6~20位');
                    }
                } else {
                    tools.validErrorDom(param_id.confirmPassword, '密码不一致');
                }
            } else {
                tools.validErrorDom(param_id.confirmPassword, '请确认密码');
            }
        }

        function updatePassword() {
            tools.buttonLoading(button_id.password.id, button_id.password.tip);
            $.post(ajax_url.users_password_update, param, function (data) {
                tools.buttonEndLoading(button_id.password.id, button_id.password.text);
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    Swal.fire('更新密码失败', data.msg, 'error')
                }
            });
        }
    });