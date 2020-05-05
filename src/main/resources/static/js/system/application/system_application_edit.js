//# sourceURL=system_application_edit.js
require(["jquery", "lodash", "tools", "sweetalert2", "handlebars", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Swal, Handlebars, nav_active) {

        /*
         ajax url.
         */
        var ajax_url = {
            pids: web_path + '/web/system/application/pids',
            update: web_path + '/web/system/application/update',
            check_name: web_path + '/web/system/application/check/edit/name',
            check_en_name: web_path + '/web/system/application/check/edit/en_name',
            check_url: web_path + '/web/system/application/check/edit/url',
            check_code: web_path + '/web/system/application/check/edit/code',
            back: '/web/menu/system/application'
        };

        // 刷新时选中菜单
        nav_active(ajax_url.back);

        /*
         参数id
         */
        var param_id = {
            applicationId: '#applicationId',
            applicationPid: '#applicationPid',
            applicationName: '#applicationName',
            applicationEnName: '#applicationEnName',
            applicationUrl: '#applicationUrl',
            applicationDataUrlStartWith: '#applicationDataUrlStartWith',
            applicationCode: '#applicationCode',
            icon: '#icon',
            applicationSort: '#applicationSort'
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
            applicationId: '',
            applicationPid: '',
            applicationName: '',
            applicationEnName: '',
            applicationUrl: '',
            applicationDataUrlStartWith: '',
            applicationCode: '',
            icon: '',
            applicationSort: ''
        };

        var page_param = {
            applicationPid: $('#applicationPidParam').val()
        };

        var init_configure = {
            applicationPid: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.applicationId = _.trim($(param_id.applicationId).val());
            param.applicationPid = _.trim($(param_id.applicationPid).val());
            param.applicationName = _.trim($(param_id.applicationName).val());
            param.applicationEnName = _.trim($(param_id.applicationEnName).val());
            param.applicationUrl = _.trim($(param_id.applicationUrl).val());
            param.applicationDataUrlStartWith = _.trim($(param_id.applicationDataUrlStartWith).val());
            param.applicationCode = _.trim($(param_id.applicationCode).val());
            param.icon = _.trim($(param_id.icon).val());
            param.applicationSort = _.trim($(param_id.applicationSort).val());
        }

        /**
         * 初始化父级菜单
         * @param data
         */
        function initApplicationPids(data) {
            var source = $("#application-parent-template").html();
            var template = Handlebars.compile(source);
            var html = template(data);
            $(param_id.applicationPid).html(html);

            if (!init_configure.applicationPid) {
                $('#applicationPid').val(page_param.applicationPid);
                init_configure.applicationPid = true;
            }
        }

        init();

        function init() {
            $.get(ajax_url.pids, function (data) {
                initApplicationPids(data);
            });

            initMaxLength();
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.applicationName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.applicationEnName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.applicationUrl).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.applicationCode).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        // 即时检验应用名
        $(param_id.applicationName).blur(function () {
            initParam();
            var applicationName = param.applicationName;
            if (applicationName.length <= 0 || applicationName.length > 30) {
                tools.validErrorDom(param_id.applicationName, '应用中文名为1~30个字符');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.applicationName);
                    } else {
                        tools.validErrorDom(param_id.applicationName, data.msg);
                    }
                });
            }
        });

        // 即时检验应用英文名
        $(param_id.applicationEnName).blur(function () {
            initParam();
            var applicationEnName = param.applicationEnName;
            if (applicationEnName.length <= 0 || applicationEnName.length > 100) {
                tools.validErrorDom(param_id.applicationEnName, '应用英文名为1~100个字符');
            } else {
                $.post(ajax_url.check_en_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.applicationEnName);
                    } else {
                        tools.validErrorDom(param_id.applicationEnName, data.msg);
                    }
                });
            }
        });

        // 即时检验应用链接
        $(param_id.applicationUrl).blur(function () {
            initParam();
            var applicationUrl = param.applicationUrl;
            if (applicationUrl.length <= 0 || applicationUrl.length > 300) {
                tools.validErrorDom(param_id.applicationUrl, '应用链接url为1~300个字符');
            } else {
                if (applicationUrl === '#') {
                    tools.validSuccessDom(param_id.applicationUrl);
                } else {
                    $.post(ajax_url.check_url, param, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.applicationUrl);
                        } else {
                            tools.validErrorDom(param_id.applicationUrl, data.msg);
                        }
                    });
                }
            }
        });

        // 即时检验应用识别码
        $(param_id.applicationCode).blur(function () {
            initParam();
            var applicationCode = param.applicationCode;
            if (applicationCode.length <= 0 || applicationCode.length > 100) {
                tools.validErrorDom(param_id.applicationCode, '应用识别码为1~100个字符');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.applicationCode);
                    } else {
                        tools.validErrorDom(param_id.applicationCode, data.msg);
                    }
                });
            }
        });

        /*
        保存数据
        */
        $(button_id.save.id).click(function () {
            initParam();
            validApplicationName();
        });

        /**
         * 检验应用名
         */
        function validApplicationName() {
            var applicationName = param.applicationName;
            if (applicationName.length <= 0 || applicationName.length > 30) {
                tools.validErrorDom(param_id.applicationName, '应用中文名为1~30个字符');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.applicationName);
                        validApplicationEnName();
                    } else {
                        tools.validErrorDom(param_id.applicationName, data.msg);
                    }
                });
            }
        }

        /**
         * 检验应用英文名
         */
        function validApplicationEnName() {
            var applicationEnName = param.applicationEnName;
            if (applicationEnName.length <= 0 || applicationEnName.length > 100) {
                tools.validErrorDom(param_id.applicationEnName, '应用英文名为1~100个字符');
            } else {
                $.post(ajax_url.check_en_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.applicationEnName);
                        validApplicationUrl();
                    } else {
                        tools.validErrorDom(param_id.applicationEnName, data.msg);
                    }
                });
            }
        }

        /**
         * 检验应用链接
         */
        function validApplicationUrl() {
            var applicationUrl = param.applicationUrl;
            if (applicationUrl.length <= 0 || applicationUrl.length > 300) {
                tools.validErrorDom(param_id.applicationUrl, '应用链接url为1~300个字符');
            } else {
                if (applicationUrl !== '#') {
                    $.post(ajax_url.check_url, param, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.applicationUrl);
                            validApplicationCode();
                        } else {
                            tools.validErrorDom(param_id.applicationUrl, data.msg);
                        }
                    });
                } else {
                    validApplicationCode();
                }
            }
        }

        /**
         * 检验应用识别码
         */
        function validApplicationCode() {
            var applicationCode = param.applicationCode;
            if (applicationCode.length <= 0 || applicationCode.length > 100) {
                tools.validErrorDom(param_id.applicationCode, '应用识别码为1~100个字符');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.applicationCode);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.applicationCode, data.msg);
                    }
                });
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
                                $.address.value(ajax_url.back);
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