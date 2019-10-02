//# sourceURL=user_setting.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "bootstrap", "dropify", "x-editable",
        "csrf", "jquery.entropizer", "select2-zh-CN", "bootstrap-datepicker-zh-CN", "bootstrap-inputmask"],
    function ($, _, tools, Swal, moment) {

        moment.locale('zh-cn');

        //modify buttons style
        $.fn.editableform.buttons =
            '<button type="submit" class="btn btn-primary btn-sm editable-submit waves-effect waves-light"><i class="zmdi zmdi-check"></i></button>' +
            '<button type="button" class="btn editable-cancel btn-sm btn-secondary waves-effect"><i class="zmdi zmdi-close"></i></button>';

        $.fn.editableform.errorBlockClass = 'text-danger';

        var ajax_url = {
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_grade_data: web_path + '/anyone/data/grade',
            obtain_organize_data: web_path + '/anyone/data/organize',
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/political_landscape',
            obtain_academic_title_data: web_path + '/anyone/data/academic_title',
            users_update: web_path + '/user/update',
            check_password: web_path + '/user/check/password',
            system_configure: web_path + '/anyone/data/configure',
            check_username: web_path + '/anyone/check/username',
            check_mobile: web_path + '/anyone/check/mobile',
            send_mobile: web_path + '/anyone/send/mobile',
            check_mobile_verification_code: web_path + '/anyone/check/mobile/code',
            check_student_number: web_path + '/user/check/student/number',
            check_staff_number: web_path + '/user/check/staff/number',
            obtain_mobile_code_valid: web_path + '/anyone/data/mobile/code',
            users_password_update: web_path + '/user/password/update',
            users_avatar_upload: web_path + '/user/avatar/upload',
            users_avatar_delete: web_path + '/user/avatar/delete',
            student_update_school: web_path + '/user/student/update/school',
            staff_update_school: web_path + '/user/staff/update/school',
            student_update_info: web_path + '/user/student/update/info',
            staff_update_info: web_path + '/user/staff/update/info'
        };

        var param_id = {
            username: '#username',
            realName: '#realName',
            email: '#email',
            mobile: '#mobile',
            idCard: '#idCard',
            verificationCode: '#verificationCode',
            oldPassword: '#oldPassword',
            newPassword: '#newPassword',
            confirmPassword: '#confirmPassword',
            avatar: '.dropify-filename-inner',
            department: '#department',
            science: '#science',
            grade: '#grade',
            organize: '#organize',
            studentNumber: '#studentNumber',
            sex: '#sex',
            birthday: '#birthday',
            nationId: '#nation',
            politicalLandscapeId: '#politicalLandscape',
            dormitoryNumber: '#dormitoryNumber',
            parentName: '#parentName',
            parentContactPhone: '#parentContactPhone',
            familyResidence: '#familyResidence',
            placeOrigin: '#placeOrigin',
            staffNumber: '#staffNumber',
            post: '#post',
            academicTitleId: '#academicTitle'
        };

        var button_id = {
            saveMobile: {
                id: '#saveMobile',
                text: '保存',
                tip: '保存中...'
            },
            password: {
                tip: '更新中...',
                text: '更新密码',
                id: '#updatePassword'
            },
            avatar: {
                tip: '更新中...',
                text: '更新',
                id: '#updateAvatar'
            },
            saveSchool: {
                tip: '保存中...',
                text: '保存',
                id: '#saveSchool'
            },
            info: {
                tip: '更新中...',
                text: '更新信息',
                id: '#updateInfo'
            }
        };

        var param = {
            mobile: '',
            verificationCode: '',
            oldPassword: '',
            newPassword: '',
            confirmPassword: '',
            avatar: '',
            departmentId: '',
            scienceId: '',
            gradeId: '',
            organizeId: '',
            studentNumber: '',
            sex: '',
            birthday: '',
            nationId: '',
            politicalLandscapeId: '',
            dormitoryNumber: '',
            parentName: '',
            parentContactPhone: '',
            familyResidence: '',
            placeOrigin: '',
            staffNumber: '',
            post: '',
            academicTitleId: ''
        };

        var page_param = {
            username: $('#username').val(),
            isStudent: Number($('#isStudentParam').val()),
            isStaff: Number($('#isStaffParam').val())
        };

        var configure = {
            FORBIDDEN_REGISTER: ''
        };

        var init_configure = {
            init_department: false,
            init_science: false,
            init_grade: false,
            init_organize: false,
            init_birthday: false
        };

        var global_param = {
            password_strong: 0
        };

        function initParam() {
            param.mobile = _.trim($(param_id.mobile).val());
            param.verificationCode = _.trim($(param_id.verificationCode).val());
            param.oldPassword = $(param_id.oldPassword).val();
            param.newPassword = $(param_id.newPassword).val();
            param.confirmPassword = $(param_id.confirmPassword).val();
            param.avatar = $(param_id.avatar).text();
            param.departmentId = $(param_id.department).val();
            param.scienceId = $(param_id.science).val();
            param.gradeId = $(param_id.grade).val();
            param.organizeId = $(param_id.organize).val();
            param.studentNumber = _.trim($(param_id.studentNumber).val());
            param.sex = $(param_id.sex).val();
            param.birthday = $(param_id.birthday).val();
            param.nationId = $(param_id.nationId).val();
            param.politicalLandscapeId = $(param_id.politicalLandscapeId).val();
            param.dormitoryNumber = $(param_id.dormitoryNumber).val();
            param.parentName = _.trim($(param_id.parentName).val());
            param.parentContactPhone = _.trim($(param_id.parentContactPhone).val());
            param.familyResidence = _.trim($(param_id.familyResidence).val());
            param.placeOrigin = _.trim($(param_id.placeOrigin).val());
            param.staffNumber = _.trim($(param_id.staffNumber).val());
            param.post = _.trim($(param_id.post).val());
            param.academicTitleId = $(param_id.academicTitleId).val();
        }

        $(param_id.department).change(function () {
            var v = $(this).val();
            initScience(v);
            initGrade(0);
            initOrganize(0);

            if (Number(v) > 0) {
                tools.validSuccessDom(param_id.department);
            }
        });

        $(param_id.science).change(function () {
            var v = $(this).val();
            initGrade(v);
            initOrganize(0);

            if (Number(v) > 0) {
                tools.validSuccessDom(param_id.science);
            }
        });

        $(param_id.grade).change(function () {
            var v = $(this).val();
            initOrganize(v);

            if (Number(v) > 0) {
                tools.validSuccessDom(param_id.grade);
            }
        });

        $(param_id.organize).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSuccessDom(param_id.organize);
            }
        });

        $('#editSchool').click(function () {
            initDepartment($('#collegeId').val());
            initScience($('#departmentId').val());
            initGrade($('#scienceId').val());
            initOrganize($('#gradeId').val());
            $('#schoolModal').modal('show');
        });

        init();

        function init() {
            initSystemConfigure();
            initPassword();
            initSelect2();

            if (page_param.isStudent === 1 || page_param.isStaff === 1) {
                initSex();
                initNation();
                initPoliticalLandscapeId();
            }

            if (page_param.isStaff === 1) {
                initAcademicTitle();
            }
        }

        function initSystemConfigure() {
            $.get(ajax_url.system_configure, function (data) {
                configure.FORBIDDEN_REGISTER = data.FORBIDDEN_REGISTER;
            });
        }

        function initPassword() {
            // 密码强度检测
            $('#meter2').entropizer({
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

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initDepartment(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                    $(param_id.department).html('<option label="请选择系"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.department).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });

                    if (!init_configure.init_department) {
                        $(param_id.department).val($('#departmentId').val());
                        init_configure.init_department = true;
                    }
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
            }
        }

        function initScience(departmentId) {
            if (Number(departmentId) > 0) {
                $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                    $(param_id.science).html('<option label="请选择专业"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.science).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });
                    if (!init_configure.init_science) {
                        $(param_id.science).val($('#scienceId').val());
                        init_configure.init_science = true;
                    }
                });
            } else {
                $(param_id.science).html('<option label="请选择专业"></option>');
            }
        }

        function initGrade(scienceId) {
            if (Number(scienceId) > 0) {
                $.get(ajax_url.obtain_grade_data, {scienceId: scienceId}, function (data) {
                    $(param_id.grade).html('<option label="请选择年级"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.grade).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });
                    if (!init_configure.init_grade) {
                        $(param_id.grade).val($('#gradeId').val());
                        init_configure.init_grade = true;
                    }
                });
            } else {
                $(param_id.grade).html('<option label="请选择年级"></option>');
            }
        }

        function initOrganize(gradeId) {
            if (Number(gradeId) > 0) {
                $.get(ajax_url.obtain_organize_data, {gradeId: gradeId}, function (data) {
                    $(param_id.organize).html('<option label="请选择班级"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.organize).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });
                    if (!init_configure.init_organize) {
                        $(param_id.organize).val($('#organizeId').val());
                        init_configure.init_organize = true;
                    }
                });
            } else {
                $(param_id.organize).html('<option label="请选择班级"></option>');
            }
        }

        function initSex() {
            $(param_id.sex).val($('#sexParam').val());
        }

        function initNation() {
            $.get(ajax_url.obtain_nation_data, function (data) {
                var sl = $(param_id.nationId).select2({
                    data: data.results
                });

                sl.val($('#nationParam').val()).trigger("change");
            });
        }

        function initPoliticalLandscapeId() {
            $.get(ajax_url.obtain_political_landscape_data, function (data) {
                $(param_id.politicalLandscapeId).html('<option label="请选择政治面貌"></option>');
                $.each(data.results, function (i, n) {
                    $(param_id.politicalLandscapeId).append('<option value="' + n.id + '">' + n.text + '</option>');
                });
                $(param_id.politicalLandscapeId).val($('#politicalLandscapeParam').val());
            });
        }

        function initAcademicTitle() {
            $.get(ajax_url.obtain_academic_title_data, function (data) {
                $(param_id.academicTitleId).html('<option label="请选择职称"></option>');
                $.each(data.results, function (i, n) {
                    $(param_id.academicTitleId).append('<option value="' + n.id + '">' + n.text + '</option>');
                });
                $(param_id.academicTitleId).val($('#academicTitleParam').val());
            });
        }

        //Inline editables
        $(param_id.username).editable({
            type: 'text',
            url: ajax_url.users_update,
            name: 'username',
            title: '输入账号',
            mode: 'inline',
            inputclass: 'form-control-sm',
            emptytext: '您的账号',
            validate: function (value) {
                var v = _.trim(value);
                if (v === '') {
                    return '账号不能为空';
                }

                // 禁止注册系统账号
                var forbiddenRegister = configure.FORBIDDEN_REGISTER.split(',');
                var isForbidden = false;
                for (var i = 0; i < forbiddenRegister.length; i++) {
                    if (forbiddenRegister[i] === value) {
                        isForbidden = true;
                        break;
                    }
                }
                if (isForbidden) {
                    return '账号已被注册';
                }
                // 只能是字母或数字
                var regex = tools.regex.username;
                if (!regex.test(value)) {
                    return '账号1~20位英文或数字';
                }
            },
            success: function (response, newValue) {
                if (!response.state) {
                    return response.msg;
                } else {
                    Swal.fire({
                        title: response.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                }

            },
            error: function (response, newValue) {
                return response.statusText;
            }
        });

        //Inline editables
        $(param_id.realName).editable({
            type: 'text',
            url: ajax_url.users_update,
            name: 'realName',
            title: '输入姓名',
            mode: 'inline',
            inputclass: 'form-control-sm',
            emptytext: '您的姓名',
            validate: function (value) {
                var v = _.trim(value);
                if (v === '') {
                    return '姓名不能为空';
                }

                if (v.length > 30) {
                    return '姓名30个字符以内';
                }
            },
            success: function (response, newValue) {
                if (!response.state) {
                    return response.msg;
                } else {
                    Swal.fire({
                        title: response.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                }
            },
            error: function (response, newValue) {
                return response.statusText;
            }
        });

        $(param_id.email).editable({
            type: 'email',
            url: ajax_url.users_update,
            name: 'email',
            title: '输入邮箱',
            mode: 'inline',
            inputclass: 'form-control-sm',
            emptytext: '您的邮箱',
            validate: function (value) {
                var v = _.trim(value);
                if (v === '') {
                    return '邮箱不能为空';
                }

                if (!tools.regex.email.test(v)) {
                    return '邮箱格式不正确';
                }
            },
            success: function (response, newValue) {
                if (!response.state) {
                    return response.msg;
                } else {
                    Swal.fire({
                        title: response.msg,
                        text: "验证邮件已发至您的邮箱，请您及时验证。",
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                }
            },
            error: function (response, newValue) {
                return response.statusText;
            }
        });

        $(param_id.idCard).editable({
            type: 'text',
            url: ajax_url.users_update,
            name: 'idCard',
            title: '输入身份证号',
            mode: 'inline',
            inputclass: 'form-control-sm',
            emptytext: '您的身份证号',
            validate: function (value) {
                var v = _.trim(value);
                if (v === '') {
                    return '身份证号不能为空';
                }

                if (!tools.regex.idCard.test(v)) {
                    return '身份证号格式不正确';
                }
            },
            success: function (response, newValue) {
                if (!response.state) {
                    return response.msg;
                } else {
                    Swal.fire({
                        title: response.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                }
            },
            error: function (response, newValue) {
                return response.statusText;
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
            validMobile();
        });

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
                pk: $('#usernameParam').val(),
                name: 'mobile',
                value: param.mobile
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
            if (_.trim(oldPassword) !== '') {
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
            if (_.trim(newPassword) !== '') {
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
            if (_.trim(confirmPassword) !== '') {
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
                    $(param_id.oldPassword).val('');
                    $(param_id.newPassword).val('');
                    $(param_id.confirmPassword).val('');
                    Swal.fire('更新密码成功', '', 'success')
                } else {
                    Swal.fire('更新密码失败', data.msg, 'error')
                }
            });
        }

        var drEvent = $('.dropify').dropify({
            messages: {
                'default': '点击或拖拽文件到这里',
                'replace': '点击或拖拽文件到这里来替换文件',
                'remove': '移除',
                'error': '文件上传错误'
            },
            error: {
                'fileSize': '文件过大，超过1MB.',
                'maxWidth': '图片最大宽带: 500px.',
                'maxHeight': '图片最大高度 500px.',
                'imageFormat': '仅允许上传 (jpg,png,gif,jpeg,bmp).',
                'fileExtension': '仅允许上传 (jpg,png,gif,jpeg,bmp).'
            }
        });

        drEvent.on('dropify.afterClear', function (event, element) {
            initParam();
            var avatar = param.avatar;
            var curAvatar = $('#avatar').val();
            var fileName = curAvatar.substring(curAvatar.lastIndexOf('/') + 1);
            if (avatar === fileName) {
                $.get(ajax_url.users_avatar_delete, function (data) {
                    if (data.state) {
                        Swal.fire('移除成功', '', 'success')
                    } else {
                        Swal.fire('移除失败', data.msg, 'error')
                    }
                });
            }

        });

        $(button_id.avatar.id).click(function () {
            initParam();
            var avatar = param.avatar;
            var curAvatar = $('#avatar').val();
            var fileName = curAvatar.substring(curAvatar.lastIndexOf('/') + 1);
            var globalAvatarError = $('#globalAvatarError');
            if (avatar !== fileName) {
                globalAvatarError.text('');
                var file = $('.dropify-render').children(0);
                var base64 = $(file).attr('src');
                if (!_.isUndefined(base64)) {
                    tools.buttonLoading(button_id.avatar.id, button_id.avatar.tip);
                    $.post(ajax_url.users_avatar_upload, {
                        'file': base64,
                        'fileName': $('.dropify-filename-inner').text()
                    }, function (data) {
                        tools.buttonEndLoading(button_id.avatar.id, button_id.avatar.text);
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
                            Swal.fire('更新失败', data.msg, 'error');
                        }
                    });
                } else {
                    globalAvatarError.text('请选择头像');
                }
            } else {
                globalAvatarError.text('请选择头像');
            }

        });

        $(button_id.saveSchool.id).click(function () {
            initParam();
            validDepartment();
        });

        function validDepartment() {
            var departmentId = param.departmentId;
            if (Number(departmentId) > 0) {
                tools.validSuccessDom(param_id.department);
                if (page_param.isStudent === 1) {
                    validScience();
                } else {
                    // 不一样才更新
                    if (Number(departmentId) !== Number($('#departmentId').val())) {
                        updateSchool();
                        tools.validSuccessDom(param_id.department);
                    } else {
                        tools.validErrorDom(param_id.department, '系信息未发生改变');
                    }
                }

            } else {
                tools.validErrorDom(param_id.department, '请选择您所在的系');
            }
        }

        function validScience() {
            var scienceId = param.scienceId;
            if (Number(scienceId) > 0) {
                tools.validSuccessDom(param_id.science);
                validGrade();
            } else {
                tools.validErrorDom(param_id.science, '请选择您所在的专业');
            }
        }

        function validGrade() {
            var gradeId = param.gradeId;
            if (Number(gradeId) > 0) {
                tools.validSuccessDom(param_id.grade);
                validOrganize();
            } else {
                tools.validErrorDom(param_id.grade, '请选择您所在的年级');
            }
        }

        function validOrganize() {
            var organizeId = param.organizeId;
            if (Number(organizeId) > 0) {
                // 不一样才更新
                if (Number(organizeId) !== Number($('#organizeId').val())) {
                    updateSchool();
                    tools.validSuccessDom(param_id.organize);
                } else {
                    tools.validErrorDom(param_id.organize, '班级信息未发生改变');
                }
            } else {
                tools.validErrorDom(param_id.organize, '请选择您所在的班级');
            }
        }

        function updateSchool() {
            var url = "";
            if (page_param.isStudent === 1) {
                url = ajax_url.student_update_school;
            } else {
                url = ajax_url.staff_update_school;
            }
            // 显示遮罩
            tools.buttonLoading(button_id.saveSchool.id, button_id.saveSchool.tip);
            $.post(url, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.saveSchool.id, button_id.saveSchool.text);
                var globalError = $('#globalSchoolError');
                if (data.state) {
                    globalError.text('');
                    $('#schoolModal').modal('hide');
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

        $(param_id.birthday).datepicker({
            autoclose: true,
            format: 'yyyy-mm-dd',
            orientation: 'top left',
            language: 'zh-CN'
        });

        $(param_id.birthday).focus(function () {
            if (!init_configure.init_birthday) {
                $(param_id.birthday).datepicker('update', moment((new Date().getFullYear() - 22) + "-01-07", "YYYY-DD-MM").toDate());
                init_configure.init_birthday = true;
            }
        });

        $(button_id.info.id).click(function () {
            initParam();

            if (page_param.isStudent === 1) {
                validStudentNumber();
            } else {
                validStaffNumber();
            }
        });

        function validStudentNumber() {
            var studentNumber = param.studentNumber;
            if (studentNumber !== '') {
                if (tools.regex.studentNumber.test(studentNumber)) {
                    $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.studentNumber);
                            validDormitoryNumber();
                        } else {
                            tools.validErrorDom(param_id.studentNumber, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                }
            } else {
                tools.validErrorDom(param_id.studentNumber, '学号不能为空');
            }
        }

        function validStaffNumber() {
            var staffNumber = param.staffNumber;
            if (staffNumber !== '') {
                if (tools.regex.staffNumber.test(staffNumber)) {
                    $.post(ajax_url.check_staff_number, {staffNumber: staffNumber}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.staffNumber);
                            validPost();
                        } else {
                            tools.validErrorDom(param_id.staffNumber, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.staffNumber, '工号至少1位数字');
                }
            } else {
                tools.validErrorDom(param_id.staffNumber, '工号不能为空');
            }
        }

        function validDormitoryNumber() {
            var dormitoryNumber = param.dormitoryNumber;
            if (dormitoryNumber !== '') {
                if (tools.regex.dormitoryNumber.test(dormitoryNumber)) {
                    tools.validSuccessDom(param_id.dormitoryNumber);
                    validParentName();
                } else {
                    tools.validErrorDom(param_id.dormitoryNumber, '宿舍号格式不正确');
                }
            } else {
                tools.validSuccessDom(param_id.dormitoryNumber);
                validParentName();
            }
        }

        function validPost() {
            var post = param.post;
            if (post !== '') {
                if (post.length <= 50) {
                    tools.validSuccessDom(param_id.post);
                    validFamilyResidence();
                } else {
                    tools.validErrorDom(param_id.post, '职务为50个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.post);
                validFamilyResidence();
            }
        }

        function validParentName() {
            var parentName = param.parentName;
            if (parentName !== '') {
                if (parentName.length <= 10) {
                    tools.validSuccessDom(param_id.parentName);
                    validParentContactPhone();
                } else {
                    tools.validErrorDom(param_id.parentName, '家长姓名为10个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.parentName);
                validParentContactPhone();
            }
        }

        function validParentContactPhone() {
            var parentContactPhone = param.parentContactPhone;
            if (parentContactPhone !== '') {
                if (tools.regex.mobile.test(parentContactPhone)) {
                    tools.validSuccessDom(param_id.parentContactPhone);
                    validFamilyResidence();
                } else {
                    tools.validErrorDom(param_id.parentContactPhone, '家长联系电话格式不正确');
                }
            } else {
                tools.validSuccessDom(param_id.parentContactPhone);
                validFamilyResidence();
            }
        }

        function validFamilyResidence() {
            var familyResidence = param.familyResidence;
            if (familyResidence !== '') {
                if (familyResidence.length <= 200) {
                    tools.validSuccessDom(param_id.familyResidence);
                    if (page_param.isStudent === 1) {
                        validPlaceOrigin();
                    } else {
                        updateInfo();
                    }

                } else {
                    tools.validErrorDom(param_id.familyResidence, '家庭居住地为200个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.familyResidence);
                if (page_param.isStudent === 1) {
                    validPlaceOrigin();
                } else {
                    updateInfo();
                }
            }
        }

        function validPlaceOrigin() {
            var placeOrigin = param.placeOrigin;
            if (placeOrigin !== '') {
                if (placeOrigin.length <= 200) {
                    tools.validSuccessDom(param_id.placeOrigin);
                    updateInfo();
                } else {
                    tools.validErrorDom(param_id.placeOrigin, '生源地为200个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.placeOrigin);
                updateInfo();
            }
        }

        function updateInfo() {
            var url = "";
            if (page_param.isStudent === 1) {
                url = ajax_url.student_update_info;
            } else {
                url = ajax_url.staff_update_info;
            }
            // 显示遮罩
            tools.buttonLoading(button_id.info.id, button_id.info.tip);
            $.post(url, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.info.id, button_id.info.text);
                var globalError = $('#globalInfoError');
                if (data.state) {
                    globalError.text('');
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
    });