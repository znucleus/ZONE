//# sourceURL=student_achievement_query.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "select2-zh-CN", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            query: web_path + '/web/achievement/student/query/data',
            captcha: web_path + '/web/achievement/student/query/captcha',
            query_history: '/web/achievement/student/query/history',
            page: '/web/menu/achievement/student/query'
        };

        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            yhdm: '#yhdm',
            yhmm: '#yhmm',
            yhlx: '#yhlx',
            yzm: '#yzm'
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
            yhdm: '',
            yhmm: '',
            yhlx: '',
            yzm: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.yhdm = _.trim($(param_id.yhdm).val());
            param.yhmm = _.trim($(param_id.yhmm).val());
            param.yhlx = _.trim($(param_id.yhlx).val());
            param.yzm = _.trim($(param_id.yzm).val());
        }

        init();

        function init() {
            changeCaptcha();
        }

        $('#captcha').click(function () {
            changeCaptcha();
        });

        $('#history').click(function (){
            $.address.value(ajax_url.query_history);
        });

        function changeCaptcha() {
            $('#captcha').attr('src', ajax_url.captcha + '?v=' + Math.random());
        }

        $(button_id.query.id).click(function () {
            initParam();
            validYhdm();
        });

        function validYhdm() {
            var yhdm = param.yhdm;
            if (yhdm !== '') {
                tools.validSuccessDom(param_id.yhdm);
                validYhmm();
            } else {
                tools.validErrorDom(param_id.yhdm, '请填写用户名');
            }
        }

        function validYhmm() {
            var yhmm = param.yhmm;
            if (yhmm !== '') {
                tools.validSuccessDom(param_id.yhmm);
                validYzm();
            } else {
                tools.validErrorDom(param_id.yhmm, '请填写密码');
            }
        }

        function validYzm() {
            var yzm = param.yzm;
            if (yzm !== '') {
                tools.validSuccessDom(param_id.yzm);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.yzm, '请填写验证码');
            }
        }

        function sendAjax() {
            $('#queryError').text('');
            tools.buttonLoading(button_id.query.id, button_id.query.tip);
            $.post(ajax_url.query, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.query.id, button_id.query.text);
                if (data.state) {
                    $.address.value(ajax_url.query_history);
                } else {
                    $('#queryError').text(data.msg);
                }
            });
        }

    });