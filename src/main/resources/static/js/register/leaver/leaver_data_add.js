//# sourceURL=leaver_data_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/register/leaver/data/save',
            page: '/web/menu/register/leaver'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            leaverAddress: '#leaverAddress',
            remark:'#remark'
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
            leaverRegisterReleaseId: '',
            leaverRegisterOptionId:'',
            leaverAddress: '',
            remark:''
        };

        var page_param = {
            leaverRegisterReleaseId: $('#leaverRegisterReleaseId').val()
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.leaverRegisterReleaseId = page_param.leaverRegisterReleaseId;
            param.leaverAddress = _.trim($(param_id.leaverAddress).val());

            var leaverRegisterOptionIds = [];
            var ids = $('input[name="leaverRegisterOptionId"]:checked');
            for (var i = 0; i < ids.length; i++) {
                leaverRegisterOptionIds.push($(ids[i]).val());
            }
            if(leaverRegisterOptionIds.length > 0){
                param.leaverRegisterOptionId = leaverRegisterOptionIds.join(',');
            }
        }

        /*
        初始化数据
        */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initMaxLength();
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.leaverAddress).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.remark).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.leaverAddress).blur(function () {
            initParam();
            var leaverAddress = param.leaverAddress;
            if (leaverAddress.length <= 0 || leaverAddress.length > 300) {
                tools.validErrorDom(param_id.leaverAddress, '离校活动地点300个字符以内');
            } else {
                tools.validSuccessDom(param_id.leaverAddress);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validLeaverAddress();
        });

        function validLeaverAddress() {
            var leaverAddress = param.leaverAddress;
            if (leaverAddress.length <= 0 || leaverAddress.length > 300) {
                tools.validErrorDom(param_id.leaverAddress, '离校活动地点300个字符以内');
            } else {
                tools.validSuccessDom(param_id.leaverAddress);
                validLeaverRegisterOptionId();
            }
        }

        function validLeaverRegisterOptionId() {
            var leaverRegisterOptionId = param.leaverRegisterOptionId;
            if (leaverRegisterOptionId.length <= 0) {
                $('#leaverRegisterOptionIdError').text('请选择离校选项');
            } else {
                $('#leaverRegisterOptionIdError').text('');
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