//# sourceURL=system_notify_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "flatpickr-zh"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/system/notify/save',
            page: '/web/menu/system/notify'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            notifyTitle: '#notifyTitle',
            notifyContent: '#notifyContent',
            notifyType: '#notifyType',
            validDate: '#validDate',
            expireDate: '#expireDate'
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
            notifyTitle: '',
            notifyContent: '',
            notifyType: '',
            validDate: '',
            expireDate: ''
        };


        /**
         * 初始化参数
         */
        function initParam() {
            param.notifyTitle = _.trim($(param_id.notifyTitle).val());
            param.notifyContent = _.trim($(param_id.notifyContent).val());
            param.notifyType = $(param_id.notifyType).val();
            param.validDate = $(param_id.validDate).val() !== '' ? $(param_id.validDate).val() + ':00' : '';
            param.expireDate = $(param_id.expireDate).val() !== '' ? $(param_id.expireDate).val() + ':00' : '';
        }

        init();

        function init() {
            initMaxLength();
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.notifyTitle).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.notifyContent).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.notifyTitle).blur(function () {
            initParam();
            var notifyTitle = param.notifyTitle;
            if (notifyTitle.length <= 0 || notifyTitle.length > 100) {
                tools.validErrorDom(param_id.notifyTitle, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.notifyTitle);
            }
        });

        $(param_id.notifyContent).blur(function () {
            initParam();
            var notifyContent = param.notifyContent;
            if (notifyContent.length <= 0 || notifyContent.length > 500) {
                tools.validErrorDom(param_id.notifyContent, '内容500个字符以内');
            } else {
                tools.validSuccessDom(param_id.notifyContent);
            }
        });

        $(param_id.validDate).flatpickr({
            "locale": "zh",
            "enableTime": true
        });

        $(param_id.expireDate).flatpickr({
            "locale": "zh",
            "enableTime": true
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validNotifyTitle();
        });

        /**
         * 添加时检验并提交数据
         */
        function validNotifyTitle() {
            var notifyTitle = param.notifyTitle;
            if (notifyTitle.length <= 0 || notifyTitle.length > 100) {
                tools.validErrorDom(param_id.notifyTitle, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.notifyTitle);
                validNotifyContent();
            }
        }

        function validNotifyContent() {
            var notifyContent = param.notifyContent;
            if (notifyContent.length <= 0 || notifyContent.length > 500) {
                tools.validErrorDom(param_id.notifyContent, '内容500个字符以内');
            } else {
                tools.validSuccessDom(param_id.notifyContent);
                validValidDate();
            }
        }

        function validValidDate() {
            var validDate = param.validDate;
            if (validDate.length <= 0) {
                tools.validErrorDom(param_id.validDate, '请选择生效时间');
            } else {
                tools.validSuccessDom(param_id.validDate);
                validExpireDate();
            }
        }

        function validExpireDate() {
            var expireDate = param.expireDate;
            if (expireDate.length <= 0) {
                tools.validErrorDom(param_id.expireDate, '请选择失效时间');
            } else {
                tools.validSuccessDom(param_id.expireDate);
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
                url: ajax_url.save,
                data: param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.page);
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