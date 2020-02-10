//# sourceURL=internship_apply_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address",
        "select2-zh-CN", "flatpickr-zh", "bootstrap-maxlength"],
    function ($, _, tools, Handlebars, Swal, navActive) {
        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/internship/apply/update',
            obtain_staff_data: web_path + '/web/internship/apply/staff',
            obtain_internship_file_data: web_path + '/users/data/internship_file',
            download: web_path + '/web/internship/apply/download',
            page: '/web/menu/internship/apply',
            my_page: '/web/internship/apply/my'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            studentId: '#studentId',
            username: '#username',
            realName: '#realName',
            sex: '#sex',
            qqMailbox: '#qqMailbox',
            parentContactPhone: '#parentContactPhone',
            staff: '#staff',
            companyName: '#companyName',
            companyAddress: '#companyAddress',
            companyContact: '#companyContact',
            companyMobile: '#companyMobile',
            startTime: '#startTime',
            endTime: '#endTime'
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
            studentId: '',
            username: '',
            realName: '',
            qqMailbox: '',
            parentContactPhone: '',
            staffId: '',
            companyName: '',
            companyAddress: '',
            companyContact: '',
            companyMobile: '',
            startTime: '',
            endTime: ''
        };

        var page_param = {
            paramSex: $('#paramSex').val(),
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            paramHeadmaster: $('#paramHeadmaster').val(),
            paramHeadmasterTel: $('#paramHeadmasterTel').val(),
            paramInternshipApplyState: $('#paramInternshipApplyState').val()
        };

        var init_configure = {
            init_staff: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.studentId = $(param_id.studentId).val();
            param.username = $(param_id.username).val();
            param.realName = $(param_id.realName).val();
            param.qqMailbox = $(param_id.qqMailbox).val();
            param.parentContactPhone = $(param_id.parentContactPhone).val();
            param.staffId = $(param_id.staff).val();
            param.companyName = $(param_id.companyName).val();
            param.companyAddress = $(param_id.companyAddress).val();
            param.companyContact = $(param_id.companyContact).val();
            param.companyMobile = $(param_id.companyMobile).val();
            param.startTime = $(param_id.startTime).val();
            param.endTime = $(param_id.endTime).val();
        }

        init();

        function init() {
            initParam();
            initInternshipFile();
            initStaff();
            initSelect2();
            initEditRange();

            $(param_id.sex).val(page_param.paramSex);
        }

        function initStaff() {
            $.get(ajax_url.obtain_staff_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                var sl = $(param_id.staff).select2({
                    data: data.results
                });

                if (!init_configure.init_staff) {
                    var staffId = '';
                    var realHeadmaster = page_param.paramHeadmaster + ' ' + page_param.paramHeadmasterTel;
                    for (var i = 0; i < data.results.length; i++) {
                        if (data.results[i].text === realHeadmaster) {
                            staffId = data.results[i].id;
                            break;
                        }
                    }
                    sl.val(staffId).trigger("change");
                    init_configure.init_staff = true;
                }
            });
        }

        function initInternshipFile() {
            $.get(ajax_url.obtain_internship_file_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                fileShow(data);
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.companyName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.companyAddress).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        function initEditRange() {
            var internshipApplyState = Number(page_param.paramInternshipApplyState);
            if (internshipApplyState === 5) { // 基本信息修改状态，不允许修改单位信息
                $(param_id.companyName).attr("readonly", true);
                $(param_id.companyAddress).attr("readonly", true);
                $(param_id.companyContact).attr("readonly", true);
                $(param_id.companyMobile).attr("readonly", true);
                initInternshipTime();// 初始化时间选择
            } else if (internshipApplyState === 7) {// 单位信息修改状态，不允许修改基本信息
                $(param_id.realName).attr("readonly", true);
                $(param_id.qqMailbox).attr("readonly", true);
                $(param_id.parentContactPhone).attr("readonly", true);
                $(param_id.startTime).attr("readonly", true);
                $(param_id.endTime).attr("readonly", true);
                $(param_id.sex).attr("disabled", true);
                $(param_id.staff).attr("disabled", true);
                initMaxLength();
            } else {
                initInternshipTime();// 初始化时间选择
                initMaxLength();
            }
        }

        /**
         * 初始化实习时间
         */
        function initInternshipTime() {
            $(param_id.startTime).flatpickr({
                "locale": "zh"
            });

            $(param_id.endTime).flatpickr({
                "locale": "zh"
            });
        }

        /**
         * 文件显示
         * @param data 数据
         */
        function fileShow(data) {
            var template = Handlebars.compile($("#file-template").html());

            Handlebars.registerHelper('translationSize', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.toSize(this.fileSize)));
            });

            Handlebars.registerHelper('lastPath', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(this.relativePath));
            });

            $('#fileShow').append(template(data));
        }

        /*
         下载附件
         */
        $('#fileShow').delegate('.downloadFile', "click", function () {
            var id = $(this).attr('data-file-id');
            window.location.href = ajax_url.download + '/' + id;
        });

        $(param_id.qqMailbox).blur(function () {
            initParam();
            var qqMailbox = param.qqMailbox;
            if (qqMailbox.length <= 0 || qqMailbox.length > 100) {
                tools.validErrorDom(param_id.qqMailbox, 'qq邮箱100个字符以内');
            } else {
                if (_.endsWith(qqMailbox, '@qq.com')) {
                    tools.validSuccessDom(param_id.qqMailbox);
                } else {
                    tools.validErrorDom(param_id.qqMailbox, '请填写正确的qq邮箱');
                }
            }
        });

        $(param_id.parentContactPhone).blur(function () {
            initParam();
            var parentContactPhone = param.parentContactPhone;
            var regex = /^1[0-9]{10}$/;
            var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
            if (!regex.test(parentContactPhone)) {
                if (!isPhone.test(parentContactPhone)) {
                    tools.validErrorDom(param_id.parentContactPhone, '请填写正确的联系方式');
                } else {
                    tools.validSuccessDom(param_id.parentContactPhone);
                }
            } else {
                tools.validSuccessDom(param_id.parentContactPhone);
            }
        });

        $(param_id.staff).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.staff);
            }
        });

        $(param_id.companyName).blur(function () {
            initParam();
            var companyName = param.companyName;
            if (companyName.length <= 0 || companyName.length > 200) {
                tools.validErrorDom(param_id.companyName, '实习单位200个字符以内');
            } else {
                tools.validSuccessDom(param_id.companyName);
            }
        });

        $(param_id.companyAddress).blur(function () {
            initParam();
            var companyAddress = param.companyAddress;
            if (companyAddress.length <= 0 || companyAddress.length > 500) {
                tools.validErrorDom(param_id.companyAddress, '实习单位地址500个字符以内');
            } else {
                tools.validSuccessDom(param_id.companyAddress);
            }
        });

        $(param_id.companyContact).blur(function () {
            initParam();
            var companyContact = param.companyContact;
            if (companyContact.length <= 0 || companyContact.length > 10) {
                tools.validErrorDom(param_id.companyContact, '实习单位联系人10个字符以内');
            } else {
                tools.validSuccessDom(param_id.companyContact);
            }
        });

        $(param_id.companyMobile).blur(function () {
            initParam();
            var companyMobile = param.companyMobile;
            var regex = /^1[0-9]{10}$/;
            var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
            if (!regex.test(companyMobile)) {
                if (!isPhone.test(companyMobile)) {
                    tools.validErrorDom(param_id.companyMobile, '请填写正确的联系方式');
                } else {
                    tools.validSuccessDom(param_id.companyMobile);
                }
            } else {
                tools.validSuccessDom(param_id.companyMobile);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validStudentName();
        });

        function validStudentName() {
            var realName = param.realName;
            if (realName.length <= 0 || realName.length > 15) {
                tools.validErrorDom(param_id.realName, '姓名15个字符以内');
            } else {
                tools.validSuccessDom(param_id.realName);
                validQqMailbox();
            }
        }

        function validQqMailbox() {
            var qqMailbox = param.qqMailbox;
            if (qqMailbox.length <= 0 || qqMailbox.length > 100) {
                tools.validErrorDom(param_id.qqMailbox, 'qq邮箱100个字符以内');
            } else {
                if (_.endsWith(qqMailbox, '@qq.com')) {
                    tools.validSuccessDom(param_id.qqMailbox);
                    validParentContactPhone();
                } else {
                    tools.validErrorDom(param_id.qqMailbox, '请填写正确的qq邮箱');
                }
            }
        }

        function validParentContactPhone() {
            var parentContactPhone = param.parentContactPhone;
            var regex = /^1[0-9]{10}$/;
            var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
            if (!regex.test(parentContactPhone)) {
                if (!isPhone.test(parentContactPhone)) {
                    tools.validErrorDom(param_id.parentContactPhone, '请填写正确的联系方式');
                } else {
                    tools.validSuccessDom(param_id.parentContactPhone);
                    validStaff();
                }
            } else {
                tools.validSuccessDom(param_id.parentContactPhone);
                validStaff();
            }
        }

        function validStaff() {
            var staffId = param.staffId;
            if (Number(staffId) <= 0) {
                tools.validSelect2ErrorDom(param_id.staff, '请选择班主任');
            } else {
                tools.validSelect2SuccessDom(param_id.staff);
                validCompanyName();
            }
        }

        function validCompanyName() {
            var companyName = param.companyName;
            if (companyName.length <= 0 || companyName.length > 200) {
                tools.validErrorDom(param_id.companyName, '实习单位200个字符以内');
            } else {
                tools.validSuccessDom(param_id.companyName);
                validCompanyAddress();
            }
        }

        function validCompanyAddress() {
            var companyAddress = param.companyAddress;
            if (companyAddress.length <= 0 || companyAddress.length > 500) {
                tools.validErrorDom(param_id.companyAddress, '实习单位地址500个字符以内');
            } else {
                tools.validSuccessDom(param_id.companyAddress);
                validCompanyContact();
            }
        }

        function validCompanyContact() {
            var companyContact = param.companyContact;
            if (companyContact.length <= 0 || companyContact.length > 10) {
                tools.validErrorDom(param_id.companyContact, '实习单位联系人10个字符以内');
            } else {
                tools.validSuccessDom(param_id.companyContact);
                validCompanyMobile();
            }
        }

        function validCompanyMobile() {
            var companyMobile = param.companyMobile;
            var regex = /^1[0-9]{10}$/;
            var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
            if (!regex.test(companyMobile)) {
                if (!isPhone.test(companyMobile)) {
                    tools.validErrorDom(param_id.companyMobile, '请填写正确的联系方式');
                } else {
                    tools.validSuccessDom(param_id.companyMobile);
                    sendAjax();
                }
            } else {
                tools.validSuccessDom(param_id.companyMobile);
                sendAjax();
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.update,
                data: $('#app_form').serialize(),
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.my_page);
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