//# sourceURL=student_achievement_query.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "select2-zh-CN", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            query: web_path + '/web/achievement/student/query/data',
            query_history_old: '/web/achievement/student/query/history/old',
            query_history: '/web/achievement/student/query/history/new',
            page: '/web/menu/achievement/student/query'
        };

        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            username: '#username',
            password: '#password'
        };

        var button_id = {
            query: {
                id: '#query',
                text: '查询',
                tip: '查询中...'
            }
        };

        /*
         参数
         */
        var param = {
            username: '#username',
            password: '#password'
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.password = _.trim($(param_id.password).val());
        }

        $('#passwordFill').click(function () {
            $(param_id.password).val($(param_id.username).val());
        });

        $('#historyOld').click(function () {
            $.address.value(ajax_url.query_history_old);
        });

        $('#historyNew').click(function () {
            $.address.value(ajax_url.query_history);
        });

        $(button_id.query.id).click(function () {
            initParam();
            validUsername();
        });

        function validUsername() {
            var username = param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
                validPassword();
            } else {
                tools.validErrorDom(param_id.username, '请填写账号');
            }
        }

        function validPassword() {
            var password = param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.password, '请填写密码');
            }
        }

        function sendAjax() {
            $('#queryError').text('');
            tools.buttonLoading(button_id.query.id, button_id.query.tip);
            $.post(ajax_url.query, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.query.id, button_id.query.text);
                if (data.state) {
                    $.address.value(ajax_url.query_history + "?studentNumber=" + param.username);
                } else {
                    $('#queryError').text(data.msg);
                }
            });
        }

    });