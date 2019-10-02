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
        "sweetalert2": web_path + "/plugins/sweetalert2/sweetalert2.all.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "sweetalert2": {
            deps: ["jquery", "css!" + web_path + "/plugins/sweetalert2/sweetalert2.min"]
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
require(["jquery", "requirejs-domready", "lodash", "sweetalert2", "tools", "bootstrap", "csrf"],
    function ($, domready, _, Swal, tools) {
        domready(function () {
            //This function is called once the DOM is ready.
            //It will be safe to query the DOM and manipulate
            //DOM nodes in this function.

            var ajax_url = {
                login: web_path + '/login',
                backstage: web_path + '/web/menu/backstage',
                student_register: web_path + '/register/student',
                staff_register: web_path + '/register/staff',
                question_feedback: web_path + '/anyone/data/feedback'
            };

            var param_id = {
                username: '#username',
                password: '#password',
                feedbackRealName: '#feedbackRealName',
                feedbackUserEmail: '#feedbackUserEmail',
                simpleDes: '#simpleDes',
                detailDes: '#detailDes'
            };

            var button_id = {
                login: {
                    id: '#login',
                    text: '登 录',
                    tip: '登录中...'
                },
                feedbackSave: {
                    id: '#feedbackSave',
                    text: '提交',
                    tip: '提交中...'
                }
            };

            var param = {
                username: '',
                password: '',
                feedbackRealName: '',
                feedbackUserEmail: '',
                simpleDes: '',
                detailDes: ''
            };

            function initParam() {
                param.username = _.trim($(param_id.username).val());
                param.password = _.trim($(param_id.password).val());
                param.feedbackRealName = _.trim($(param_id.feedbackRealName).val());
                param.feedbackUserEmail = _.trim($(param_id.feedbackUserEmail).val());
                param.simpleDes = _.trim($(param_id.simpleDes).val());
                param.detailDes = _.trim($(param_id.detailDes).val());
            }

            $(param_id.username).keyup(function (event) {
                if (event.keyCode === 13) {
                    validUsername();
                }
            });

            $(param_id.password).keyup(function (event) {
                if (event.keyCode === 13) {
                    validUsername();
                }
            });

            $(button_id.login.id).click(function () {
                validUsername();
            });

            function validUsername() {
                initParam();
                var username = param.username;
                if (username !== '') {
                    tools.validSuccessDom(param_id.username);
                    validPassword();
                } else {
                    tools.validErrorDom(param_id.username, "您的账号？");
                }
            }

            function validPassword() {
                initParam();
                var password = param.password;
                if (tools.regex.password.test(password)) {
                    tools.validSuccessDom(param_id.password);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id.password, "密码不正确。");
                }
            }

            /*
             错误码
             */
            var error_code = {
                AU_ERROR_CODE: '1', // 权限异常
                OK_CODE: '2', // 全部正确
                SCHOOL_IS_DEL_CODE: '3', // 用户所在院校或班级被注销或本人学生，教职工信息不存在
                USERNAME_IS_NOT_EXIST_CODE: '4', //  用户不存在
                USERNAME_IS_BLANK: '5', // 账号/邮箱/手机号为空
                PASSWORD_IS_BLANK: '6', // 密码为空
                EMAIL_IS_NOT_VALID: '7', // 邮箱未验证
                USERNAME_IS_ENABLES: '8', //  账号已被注销
                USERNAME_ACCOUNT_NON_EXPIRED: '9', // 账号过期
                USERNAME_CREDENTIALS_NON_EXPIRED: '10', //  账号是否凭证过期
                USERNAME_ACCOUNT_NON_LOCKED: '11', //  账号是否被锁
                INIT_SERVE_ERROR: '12', //  初始化服务异常
                USER_TYPE_IS_BLANK: '13' //  用户类型是否为空
            };

            function sendAjax() {
                // 显示遮罩
                tools.buttonLoading(button_id.login.id, button_id.login.tip);
                $.post(ajax_url.login, param, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.login.id, button_id.login.text);
                    var globalError = $('#globalError');
                    switch (data) {
                        case error_code.AU_ERROR_CODE:
                            globalError.text('密码错误');
                            break;
                        case error_code.OK_CODE:
                            globalError.text('');
                            var url = window.location.href;
                            var toBackstage = ajax_url.backstage;
                            // 登录后直接去刚刚被弹出的地方
                            if (url.indexOf('#') !== -1) {
                                toBackstage += url.substring(url.lastIndexOf('#'));
                            }
                            window.location.href = toBackstage;
                            break;
                        case error_code.SCHOOL_IS_DEL_CODE:
                            globalError.text('您所在院校可能已被注销或未查询到您本人身份信息');
                            break;
                        case error_code.USERNAME_IS_NOT_EXIST_CODE:
                            globalError.text('用户不存在');
                            break;
                        case error_code.USERNAME_IS_BLANK:
                            globalError.text('请填写账号，邮箱或手机号');
                            break;
                        case error_code.PASSWORD_IS_BLANK:
                            globalError.text('密码不能为空');
                            break;
                        case error_code.EMAIL_IS_NOT_VALID:
                            globalError.text('您的邮箱未验证无法登录');
                            break;
                        case error_code.USERNAME_IS_ENABLES:
                            globalError.text('您的账号已被注销，请联系管理员');
                            break;
                        case error_code.USERNAME_ACCOUNT_NON_EXPIRED:
                            globalError.text('您的账号已过期，请联系管理员');
                            break;
                        case error_code.USERNAME_CREDENTIALS_NON_EXPIRED:
                            globalError.text('您的账号凭证已过期，请联系管理员');
                            break;
                        case error_code.USERNAME_ACCOUNT_NON_LOCKED:
                            globalError.text('您的账号已被锁，请联系管理员');
                            break;
                        case error_code.INIT_SERVE_ERROR:
                            globalError.text('系统初始化服务异常，请联系管理员');
                            break;
                        case error_code.USER_TYPE_IS_BLANK:
                            globalError.text('您的用户类型查询为空，请联系管理员');
                            break;
                        default:
                            globalError.text('系统登录异常，请联系管理员');
                    }
                });
            }

            $('#student').click(function () {
                window.location.href = ajax_url.student_register;
            });

            $('#staff').click(function () {
                window.location.href = ajax_url.staff_register;
            });

            $(button_id.feedbackSave.id).click(function () {
                initParam();
                validFeedbackRealName();
            });

            function validFeedbackRealName() {
                var feedbackRealName = param.feedbackRealName;
                if (feedbackRealName !== '') {
                    tools.validSuccessDom(param_id.feedbackRealName);
                    validFeedbackUserEmail();
                } else {
                    tools.validErrorDom(param_id.feedbackRealName, "您的姓名？");
                }
            }

            function validFeedbackUserEmail() {
                var feedbackUserEmail = param.feedbackUserEmail;
                if (feedbackUserEmail !== '') {
                    var regex = tools.regex.email;
                    if (regex.test(feedbackUserEmail)) {
                        tools.validSuccessDom(param_id.feedbackUserEmail);
                        validSimpleDes();
                    } else {
                        tools.validErrorDom(param_id.feedbackUserEmail, '邮箱格式不正确');
                    }
                } else {
                    tools.validErrorDom(param_id.feedbackUserEmail, "您的邮箱？");
                }
            }

            function validSimpleDes() {
                var simpleDes = param.simpleDes;
                if (simpleDes !== '') {
                    tools.validSuccessDom(param_id.simpleDes);
                    validDetailDes();
                } else {
                    tools.validErrorDom(param_id.simpleDes, "请简单描述您的问题。");
                }
            }

            function validDetailDes() {
                var detailDes = param.detailDes;
                if (detailDes !== '') {
                    tools.validSuccessDom(param_id.detailDes);
                    sendFeedbackAjax();
                } else {
                    tools.validErrorDom(param_id.detailDes, "请详细描述您的问题。");
                }
            }

            function sendFeedbackAjax() {
                tools.buttonLoading(button_id.feedbackSave.id, button_id.feedbackSave.tip);
                $.post(ajax_url.question_feedback, $('#feedbackForm').serialize(), function (data) {
                    tools.buttonEndLoading(button_id.feedbackSave.id, button_id.feedbackSave.text);
                    var feedbackError = $('#feedbackError');
                    if (data.state) {
                        feedbackError.text('');
                        $('#modalQuestion').modal('hide');
                        Swal.fire({
                            type: 'success',
                            text: data.msg,
                            showConfirmButton: false,
                            timer: 1500
                        });
                    } else {
                        feedbackError.text(data.msg);
                    }
                });
            }

        });
    });