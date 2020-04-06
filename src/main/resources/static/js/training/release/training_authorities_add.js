//# sourceURL=training_authorities_add.js
require(["jquery", "tools", "moment-with-locales", "handlebars", "sweetalert2",
        "nav.active", "messenger", "jquery.address", "flatpickr-zh"],
    function ($, tools, moment, Handlebars, Swal, navActive) {

        moment.locale('zh-cn');

        /*
         ajax url.
         */
        var ajax_url = {
            check_username: web_path + '/anyone/check/exist/username',
            save: web_path + '/web/training/release/authorities/save',
            back: '/web/training/release/authorities',
            page: '/web/menu/training/release'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            trainingReleaseId: '#trainingReleaseId',
            username: '#username',
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
            trainingReleaseId: '',
            username: '',
            validDate: '',
            expireDate: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.trainingReleaseId = $(param_id.trainingReleaseId).val();
            param.username = $(param_id.username).val();
            param.validDate = $(param_id.validDate).val();
            param.expireDate = $(param_id.expireDate).val();
        }

        $(param_id.validDate).flatpickr({
            "locale": "zh",
            "enableTime": true,
            "dateFormat": "Y-m-d H:i:S",
            "time_24hr": true
        });

        $(param_id.expireDate).flatpickr({
            "locale": "zh",
            "enableTime": true,
            "dateFormat": "Y-m-d H:i:S",
            "time_24hr": true
        });

        $(param_id.username).blur(function () {
            initParam();
            var username = param.username;
            if (username.length <= 0 || username.length > 64) {
                tools.validErrorDom(param_id.username, '账号64个字符以内');
            } else {
                $.post(ajax_url.check_username, {username: username}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.username);
                    } else {
                        tools.validErrorDom(param_id.username, data.msg);
                    }
                });
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validUsername();
        });

        function validUsername() {
            var username = param.username;
            if (username.length <= 0 || username.length > 64) {
                tools.validErrorDom(param_id.username, '账号64个字符以内');
            } else {
                $.post(ajax_url.check_username, {username: username}, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.username);
                        validValidDate();
                    } else {
                        tools.validErrorDom(param_id.username, data.msg);
                    }
                });
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
                var validDate = param.validDate;
                if (moment(expireDate, 'YYYY-MM-DD HH:mm:ss').isSameOrAfter(moment(validDate, 'YYYY-MM-DD HH:mm:ss'))) {
                    tools.validSuccessDom(param_id.expireDate);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id.expireDate, '失效时间应大于或等于生效时间');
                }
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
                data: $('#app_form').serialize(),
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.back + '/' + param.trainingReleaseId);
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