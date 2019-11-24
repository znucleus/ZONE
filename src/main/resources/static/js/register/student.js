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
        "select2": web_path + "/plugins/select2/js/select2.min",
        "select2-zh-CN": web_path + "/plugins/select2/js/i18n/zh-CN.min",
        "jquery.entropizer": web_path + "/plugins/jquery-entropizer/js/jquery-entropizer.min",
        "entropizer": web_path + "/plugins/jquery-entropizer/js/entropizer.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "select2-zh-CN": {
            deps: ["jquery", "select2"]
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
require(["jquery", "requirejs-domready", "lodash", "tools", "bootstrap", "csrf", "select2-zh-CN",
        "jquery.entropizer"],
    function ($, domready, _, tools) {
        domready(function () {
            //This function is called once the DOM is ready.
            //It will be safe to query the DOM and manipulate
            //DOM nodes in this function.
            var ajax_url = {
                obtain_school_data: web_path + '/anyone/data/school',
                obtain_college_data: web_path + '/anyone/data/college',
                obtain_department_data: web_path + '/anyone/data/department',
                obtain_science_data: web_path + '/anyone/data/science',
                obtain_grade_data: web_path + '/anyone/data/grade',
                obtain_organize_data: web_path + '/anyone/data/organize',
                system_configure: web_path + '/anyone/data/configure',
                check_username: web_path + '/anyone/check/username',
                check_email: web_path + '/anyone/check/email',
                check_student_number: web_path + '/anyone/check/student/number',
                check_mobile: web_path + '/anyone/check/mobile',
                send_mobile: web_path + '/anyone/send/mobile',
                check_mobile_verification_code: web_path + '/anyone/check/mobile/code',
                obtain_mobile_code_valid: web_path + '/anyone/data/mobile/code',
                register_student: web_path + '/anyone/data/register/student',
                register_success: web_path + '/tip/register/success'
            };

            var param_id = {
                school: '#school',
                college: '#college',
                department: '#department',
                science: '#science',
                grade: '#grade',
                organize: '#organize',
                username: '#username',
                email: '#email',
                realName: '#realName',
                studentNumber: '#studentNumber',
                mobile: '#mobile',
                verificationCode: '#verificationCode',
                password: '#password',
                okPassword: '#okPassword',
                agreeProtocol: '#agreeProtocol'
            };

            var button_id = {
                register: {
                    id: '#register',
                    text: '注 册',
                    tip: '注册中...'
                }
            };

            var param = {
                schoolId: '',
                collegeId: '',
                departmentId: '',
                scienceId: '',
                gradeId: '',
                organizeId: '',
                username: '',
                email: '',
                realName: '',
                studentNumber: '',
                mobile: '',
                verificationCode: '',
                password: '',
                okPassword: '',
                agreeProtocol: ''
            };

            var configure = {
                FORBIDDEN_REGISTER: ''
            };

            var global_param = {
                password_strong: 0
            };

            function initParam() {
                param.schoolId = $(param_id.school).val();
                param.collegeId = $(param_id.college).val();
                param.departmentId = $(param_id.department).val();
                param.scienceId = $(param_id.science).val();
                param.gradeId = $(param_id.grade).val();
                param.organizeId = $(param_id.organize).val();
                param.username = _.trim($(param_id.username).val());
                param.email = _.trim($(param_id.email).val());
                param.realName = _.trim($(param_id.realName).val());
                param.studentNumber = _.trim($(param_id.studentNumber).val());
                param.mobile = _.trim($(param_id.mobile).val());
                param.verificationCode = _.trim($(param_id.verificationCode).val());
                param.password = _.trim($(param_id.password).val());
                param.okPassword = _.trim($(param_id.okPassword).val());
                var agreeProtocol = $('input[name="agreeProtocol"]:checked').val();
                param.agreeProtocol = _.isUndefined(agreeProtocol) ? 0 : agreeProtocol;
            }

            $(param_id.school).change(function () {
                var v = $(this).val();
                initCollege(v);
                initDepartment(0);
                initScience(0);
                initGrade(0);
                initOrganize(0);

                if (Number(v) > 0) {
                    tools.validSelect2SuccessDom(param_id.school);
                }
            });

            $(param_id.college).change(function () {
                var v = $(this).val();
                initDepartment(v);
                initScience(0);
                initGrade(0);
                initOrganize(0);

                if (Number(v) > 0) {
                    tools.validSelect2SuccessDom(param_id.college);
                }
            });

            $(param_id.department).change(function () {
                var v = $(this).val();
                initScience(v);
                initGrade(0);
                initOrganize(0);

                if (Number(v) > 0) {
                    tools.validSelect2SuccessDom(param_id.department);
                }
            });

            $(param_id.science).change(function () {
                var v = $(this).val();
                initGrade(v);
                initOrganize(0);

                if (Number(v) > 0) {
                    tools.validSelect2SuccessDom(param_id.science);
                }
            });

            $(param_id.grade).change(function () {
                var v = $(this).val();
                initOrganize(v);

                if (Number(v) > 0) {
                    tools.validSelect2SuccessDom(param_id.grade);
                }
            });

            $(param_id.organize).change(function () {
                var v = $(this).val();

                if (Number(v) > 0) {
                    tools.validSelect2SuccessDom(param_id.organize);
                }
            });

            init();

            function init() {
                initSystemConfigure();
                initSelect2();
                initSchool();
                initPassword();
            }

            function initSystemConfigure() {
                $.get(ajax_url.system_configure, function (data) {
                    configure.FORBIDDEN_REGISTER = data.FORBIDDEN_REGISTER;
                });
            }

            function initSelect2() {
                $('.select2-show-search').select2({
                    language: "zh-CN"
                });
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

            function initSchool() {
                $.get(ajax_url.obtain_school_data, function (data) {
                    $(param_id.school).select2({
                        data: data.results
                    });
                });
            }

            function initCollege(schoolId) {
                if (Number(schoolId) > 0) {
                    $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                        $(param_id.college).html('<option label="请选择院"></option>');
                        $(param_id.college).select2({data: data.results});
                    });
                } else {
                    $(param_id.college).html('<option label="请选择院"></option>');
                }
            }

            function initDepartment(collegeId) {
                if (Number(collegeId) > 0) {
                    $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                        $(param_id.department).html('<option label="请选择系"></option>');
                        $(param_id.department).select2({data: data.results});
                    });
                } else {
                    $(param_id.department).html('<option label="请选择系"></option>');
                }
            }

            function initScience(departmentId) {
                if (Number(departmentId) > 0) {
                    $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                        $(param_id.science).html('<option label="请选择专业"></option>');
                        $(param_id.science).select2({data: data.results});
                    });
                } else {
                    $(param_id.science).html('<option label="请选择专业"></option>');
                }
            }

            function initGrade(scienceId) {
                if (Number(scienceId) > 0) {
                    $.get(ajax_url.obtain_grade_data, {scienceId: scienceId}, function (data) {
                        $(param_id.grade).html('<option label="请选择年级"></option>');
                        $(param_id.grade).select2({data: data.results});
                    });
                } else {
                    $(param_id.grade).html('<option label="请选择年级"></option>');
                }
            }

            function initOrganize(gradeId) {
                if (Number(gradeId) > 0) {
                    $.get(ajax_url.obtain_organize_data, {gradeId: gradeId}, function (data) {
                        $(param_id.organize).html('<option label="请选择班级"></option>');
                        $(param_id.organize).select2({data: data.results});
                    });
                } else {
                    $(param_id.organize).html('<option label="请选择班级"></option>');
                }
            }

            $(param_id.username).blur(function () {
                initParam();
                var username = param.username.toUpperCase();
                if (username !== '') {
                    // 禁止注册系统账号
                    var forbiddenRegister = configure.FORBIDDEN_REGISTER.split(',');
                    var isForbidden = false;
                    for (var i = 0; i < forbiddenRegister.length; i++) {
                        if (forbiddenRegister[i] === username) {
                            isForbidden = true;
                            break;
                        }
                    }
                    if (isForbidden) {
                        tools.validErrorDom(param_id.username, '账号已被注册');
                        return;
                    }
                    // 只能是字母或数字
                    var regex = tools.regex.username;
                    if (!regex.test(username)) {
                        tools.validErrorDom(param_id.username, '账号1~20位英文或数字');
                        return;
                    }

                    $.post(ajax_url.check_username, {username: username}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.username);
                        } else {
                            tools.validErrorDom(param_id.username, data.msg);
                        }
                    });
                } else {
                    tools.validSuccessDom(param_id.username);
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

            $(param_id.realName).blur(function () {
                initParam();
                var realName = param.realName;
                if (realName !== '') {
                    tools.validSuccessDom(param_id.realName);
                } else {
                    tools.validSuccessDom(param_id.realName);
                }
            });

            $(param_id.studentNumber).blur(function () {
                initParam();
                var studentNumber = param.studentNumber;
                if (studentNumber !== '') {
                    var regex = tools.regex.studentNumber;
                    if (!regex.test(studentNumber)) {
                        tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                        return;
                    }

                    $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.studentNumber);
                        } else {
                            tools.validErrorDom(param_id.studentNumber, data.msg);
                        }
                    });
                } else {
                    tools.validSuccessDom(param_id.studentNumber);
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

            $(param_id.password).blur(function () {
                initParam();
                var password = param.password;
                if (password !== '') {
                    var regex = tools.regex.password;
                    if (!regex.test(password)) {
                        tools.validErrorDom(param_id.password, '密码为6-16位任意字母或数字，以及下划线');
                    } else {
                        if (global_param.password_strong < 55) {
                            tools.validErrorDom(param_id.password, '密码过于简单');
                        } else {
                            tools.validSuccessDom(param_id.password);
                        }
                    }
                } else {
                    tools.validSuccessDom(param_id.password);
                }
            });

            $(param_id.okPassword).blur(function () {
                initParam();
                var okPassword = param.okPassword;
                if (okPassword !== '') {
                    var password = param.password;
                    var regex = tools.regex.password;
                    if (!regex.test(password)) {
                        tools.validErrorDom(param_id.password, '密码为6-16位任意字母或数字，以及下划线');
                    } else {
                        if (global_param.password_strong < 55) {
                            tools.validErrorDom(param_id.password, '密码过于简单');
                        } else {
                            if (okPassword !== password) {
                                tools.validErrorDom(param_id.okPassword, '密码不一致');
                            } else {
                                tools.validSuccessDom(param_id.okPassword);
                            }
                        }
                    }
                } else {
                    tools.validSuccessDom(param_id.okPassword);
                }
            });

            $(button_id.register.id).click(function () {
                initParam();
                validSchool();
            });

            function validSchool() {
                var schoolId = param.schoolId;
                if (Number(schoolId) > 0) {
                    tools.validSelect2SuccessDom(param_id.school);
                    validCollege();
                } else {
                    tools.validSelect2ErrorDom(param_id.school, '请选择您所在的学校');
                }
            }

            function validCollege() {
                var collegeId = param.collegeId;
                if (Number(collegeId) > 0) {
                    tools.validSelect2SuccessDom(param_id.college);
                    validDepartment();
                } else {
                    tools.validSelect2ErrorDom(param_id.college, '请选择您所在的院');
                }
            }

            function validDepartment() {
                var departmentId = param.departmentId;
                if (Number(departmentId) > 0) {
                    tools.validSelect2SuccessDom(param_id.department);
                    validScience();
                } else {
                    tools.validSelect2ErrorDom(param_id.department, '请选择您所在的系');
                }
            }

            function validScience() {
                var scienceId = param.scienceId;
                if (Number(scienceId) > 0) {
                    tools.validSelect2SuccessDom(param_id.science);
                    validGrade();
                } else {
                    tools.validSelect2ErrorDom(param_id.science, '请选择您所在的专业');
                }
            }

            function validGrade() {
                var gradeId = param.gradeId;
                if (Number(gradeId) > 0) {
                    tools.validSelect2SuccessDom(param_id.grade);
                    validOrganize();
                } else {
                    tools.validSelect2ErrorDom(param_id.grade, '请选择您所在的年级');
                }
            }

            function validOrganize() {
                var organizeId = param.organizeId;
                if (Number(organizeId) > 0) {
                    tools.validSelect2SuccessDom(param_id.organize);
                    validUsername();
                } else {
                    tools.validSelect2ErrorDom(param_id.organize, '请选择您所在的班级');
                }
            }

            function validUsername() {
                var username = param.username.toUpperCase();
                if (username !== '') {
                    // 禁止注册系统账号
                    var forbiddenRegister = configure.FORBIDDEN_REGISTER.split(',');
                    var isForbidden = false;
                    for (var i = 0; i < forbiddenRegister.length; i++) {
                        if (forbiddenRegister[i] === username) {
                            isForbidden = true;
                            break;
                        }
                    }
                    if (isForbidden) {
                        tools.validErrorDom(param_id.username, '账号已被注册');
                        return;
                    }
                    // 只能是字母或数字
                    var regex = tools.regex.username;
                    if (!regex.test(username)) {
                        tools.validErrorDom(param_id.username, '账号1~20位英文或数字');
                        return;
                    }

                    $.post(ajax_url.check_username, {username: username}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.username);
                            validEmail();
                        } else {
                            tools.validErrorDom(param_id.username, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.username, "您的账号？");
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
                            validRealName();
                        } else {
                            tools.validErrorDom(param_id.email, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.email, "您的邮箱？");
                }
            }

            function validRealName() {
                var realName = param.realName;
                if (realName !== '') {
                    tools.validSuccessDom(param_id.realName);
                    validStudentNumber();
                } else {
                    tools.validErrorDom(param_id.realName, "您的姓名？");
                }
            }

            function validStudentNumber() {
                var studentNumber = param.studentNumber;
                if (studentNumber !== '') {
                    var regex = tools.regex.studentNumber;
                    if (!regex.test(studentNumber)) {
                        tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                        return;
                    }

                    $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.studentNumber);
                            validMobile();
                        } else {
                            tools.validErrorDom(param_id.studentNumber, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.studentNumber, "您的学号？");
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
                            validPassword();
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

            function validPassword() {
                var password = param.password;
                if (password !== '') {
                    var regex = tools.regex.password;
                    if (!regex.test(password)) {
                        tools.validErrorDom(param_id.password, '密码为6-16位任意字母或数字，以及下划线');
                    } else {
                        if (global_param.password_strong < 55) {
                            tools.validErrorDom(param_id.password, '密码过于简单');
                        } else {
                            tools.validSuccessDom(param_id.password);
                            validOkPassword();
                        }
                    }
                } else {
                    tools.validErrorDom(param_id.password, "请设置您的密码");
                }
            }

            function validOkPassword() {
                var okPassword = param.okPassword;
                if (okPassword !== '') {
                    var password = param.password;
                    var regex = tools.regex.password;
                    if (!regex.test(password)) {
                        tools.validErrorDom(param_id.password, '密码为6-16位任意字母或数字，以及下划线');
                    } else {
                        if (global_param.password_strong < 55) {
                            tools.validErrorDom(param_id.password, '密码过于简单');
                        } else {
                            if (okPassword !== password) {
                                tools.validErrorDom(param_id.okPassword, '密码不一致');
                            } else {
                                tools.validSuccessDom(param_id.okPassword);
                                validAgreeProtocol();
                            }
                        }
                    }
                } else {
                    tools.validErrorDom(param_id.okPassword, "请确认您的密码");
                }
            }

            function validAgreeProtocol() {
                var agreeProtocol = param.agreeProtocol;
                if (Number(agreeProtocol) === 1) {
                    $('#agreeProtocolError').text('');
                    sendAjax();
                } else {
                    $('#agreeProtocolError').text("您需要同意入站协议。");
                }
            }

            function sendAjax() {
                // 显示遮罩
                tools.buttonLoading(button_id.register.id, button_id.register.tip);
                $.post(ajax_url.register_student, param, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.register.id, button_id.register.text);
                    var globalError = $('#globalError');
                    if (data.state) {
                        globalError.text('');
                        window.location.href = ajax_url.register_success;
                    } else {
                        globalError.text(data.msg);
                    }
                });
            }
        });
    });