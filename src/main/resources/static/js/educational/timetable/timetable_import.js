//# sourceURL=timetable_import.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/educational/timetable/import/save',
            page: '/web/menu/educational/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            username: '#username',
            password: '#password'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '确定',
                tip: '导入中...'
            }
        };

        /*
         参数
         */
        var param = {
            username: '',
            password: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.password = _.trim($(param_id.password).val());
        }

        $(param_id.username).blur(function (){
            initParam();
           var username =  param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        });

        $(param_id.password).blur(function (){
            initParam();
            var password =  param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        });

        $(button_id.save.id).click(function (){
            initParam();
            validUsername();
        });

        function validUsername() {
            var username = param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
                validPassword();
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        }

        function validPassword() {
            var password = param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        }

        function sendAjax() {
            $('#globalError').val('');
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.get(ajax_url.save, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                if(data.state){
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $.address.value(ajax_url.page);
                        }
                    });
                } else {
                    $('#globalError').text(data.msg);
                }
            });
        }

    });