//# sourceURL=users_setting.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "bootstrap",
        "csrf", "select2-zh-CN", "jquery.entropizer"],
    function ($, _, tools, Swal, moment) {

        moment.locale('zh-cn');

        var ajax_url = {
            users_update: web_path + '/users/update',
            check_password: web_path + '/users/check/password',
            system_configure: web_path + '/anyone/data/configure',
            check_email: web_path + '/users/check/email',
            check_mobile: web_path + '/users/check/mobile',
            send_mobile: web_path + '/anyone/send/mobile',
            check_mobile_verification_code: web_path + '/anyone/check/mobile/code',
            obtain_mobile_code_valid: web_path + '/anyone/data/mobile/code',
            users_password_update: web_path + '/users/password/update'
        };

        var param_id = {
            username: '#username',
            defaultUsername: '#defaultUsername',
            emailPassword: '#emailPassword',
            email: '#email',
            mobilePassword: '#mobilePassword',
            mobile: '#mobile',
            idCard: '#idCard',
            defaultIdCard: '#defaultIdCard',
            verificationCode: '#verificationCode',
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
            }
        };

        var param = {
            username: '',
            emailPassword: '',
            email: '',
            mobilePassword: '',
            mobile: '',
            idCard: '',
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

        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.emailPassword = _.trim($(param_id.emailPassword).val());
            param.email = _.trim($(param_id.email).val());
            param.mobilePassword = _.trim($(param_id.mobilePassword).val());
            param.mobile = _.trim($(param_id.mobile).val());
            param.verificationCode = _.trim($(param_id.verificationCode).val());
            param.idCard = _.trim($(param_id.idCard).val());
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
                password: param.emailPassword
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
            $(param_id.idCard).prop('readonly', false).removeClass('form-control-plaintext').addClass('form-control');
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
                            cancelUpdateEmail();
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
                password: param.mobilePassword
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
                        tools.validErrorDom(param_id.newPassword, '密码为6-16位任意字母或数字，以及下划线');
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
                        tools.validErrorDom(param_id.confirmPassword, '密码为6-16位任意字母或数字，以及下划线');
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