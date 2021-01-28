//# sourceURL=school_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/data/school/save',
            check_name: web_path + '/web/data/school/check-add/name',
            page: '/web/menu/data/school'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            schoolName: '#schoolName'
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
            schoolName: ''
        };


        /**
         * 初始化参数
         */
        function initParam() {
            param.schoolName = _.trim($(param_id.schoolName).val());
        }

        init();

        function init() {
            initMaxLength();
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.schoolName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        /*
         即时检验学校名
         */
        $(param_id.schoolName).blur(function () {
            initParam();
            var schoolName = param.schoolName;
            if (schoolName.length <= 0 || schoolName.length > 200) {
                tools.validErrorDom(param_id.schoolName, '学校名200个字符以内');
            } else {
                $.get(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.schoolName);
                    } else {
                        tools.validErrorDom(param_id.schoolName, data.msg);
                    }
                });
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validSchoolName();
        });

        /**
         * 添加时检验并提交数据
         */
        function validSchoolName() {
            var schoolName = param.schoolName;
            if (schoolName.length <= 0 || schoolName.length > 200) {
                tools.validErrorDom(param_id.schoolName, '学校名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.schoolName);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.schoolName, data.msg);
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