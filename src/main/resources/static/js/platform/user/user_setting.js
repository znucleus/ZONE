//# sourceURL=user_setting.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "bootstrap",
        "csrf", "select2-zh-CN", "jquery-ui", "jquery-toggles"],
    function ($, _, tools, Swal, moment) {

        moment.locale('zh-cn');

        var ajax_url = {
            users_update: web_path + '/user/update',
            check_password: web_path + '/user/check/password',
            system_configure: web_path + '/anyone/data/configure',
            check_username: web_path + '/anyone/check/username',
            check_mobile: web_path + '/anyone/check/mobile',
            send_mobile: web_path + '/anyone/send/mobile',
            check_mobile_verification_code: web_path + '/anyone/check/mobile/code',
            obtain_mobile_code_valid: web_path + '/anyone/data/mobile/code',
            users_password_update: web_path + '/user/password/update'
        };

        var param_id = {
            username: '#username',
            defaultUsername: '#defaultUsername',
            email: '#email',
            defaultEmail: '#defaultEmail',
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
            editEmail: {
                text: '修改',
                id: '#editEmail'
            },
            updateEmail: {
                tip: '更新中...',
                text: '确定',
                id: '#updateEmail'
            },
            cancelUpdateEmail: {
                text: '取消',
                id: '#cancelUpdateEmail'
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
            password: {
                tip: '更新中...',
                text: '保存',
                id: '#updatePassword'
            },
            info: {
                tip: '修改中...',
                text: '修改',
                id: '#updateInfo'
            }
        };

        var param = {
            username: '',
            email: '',
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

        init();

        function init() {
            initSystemConfigure();
        }

        function initSystemConfigure() {
            $.get(ajax_url.system_configure, function (data) {
                configure.FORBIDDEN_REGISTER = data.FORBIDDEN_REGISTER;
            });
        }

        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.email = _.trim($(param_id.email).val());
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

        $(button_id.editEmail.id).click(function () {
            $(this).prop('hidden', true);
            $(button_id.updateEmail.id).prop('hidden', false);
            $(button_id.cancelUpdateEmail.id).prop('hidden', false);
            $(param_id.email).prop('readonly', false).removeClass('form-control-plaintext').addClass('form-control');
        });

        $(button_id.cancelUpdateEmail.id).click(function () {
            cancelUpdateEmail();
        });

        function cancelUpdateEmail() {
            $(button_id.cancelUpdateEmail.id).prop('hidden', true);
            $(button_id.updateEmail.id).prop('hidden', true);
            $(button_id.editEmail.id).prop('hidden', false);
            $(param_id.email).prop('readonly', true).removeClass('form-control').addClass('form-control-plaintext').val($(param_id.defaultEmail).val());
            tools.validSuccessDom(param_id.email);
        }

        $(button_id.updateEmail.id).click(function () {
            initParam();
            var email = param.email;
            if (email === '') {
                tools.validErrorDom(param_id.email, '邮箱不能为空');
            } else {
                if (!tools.regex.email.test(email)) {
                    tools.validErrorDom(param_id.email, '邮箱格式不正确');
                } else {
                    tools.validSuccessDom(param_id.email);
                    tools.buttonLoading(button_id.updateEmail.id, button_id.updateEmail.tip);
                    $.post(ajax_url.users_update, {
                        name: 'email',
                        value: email
                    }, function (data) {
                        tools.buttonEndLoading(button_id.updateEmail.id, button_id.updateEmail.text);
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
    });