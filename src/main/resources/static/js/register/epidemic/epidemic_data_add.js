//# sourceURL=epidemic_data_add.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/register/epidemic/data/save',
            page: '/web/menu/register/epidemic'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            epidemicStatus: '#epidemicStatus',
            address: '#address',
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
            epidemicRegisterReleaseId: '',
            epidemicStatus: '',
            location: '',
            address: '',
            remark:'',
            epidemicRegisterDataId:''
        };

        var page_param = {
            paramEpidemicRegisterReleaseId: $('#paramEpidemicRegisterReleaseId').val(),
            paramEpidemicStatus: $('#paramEpidemicStatus').val(),
            paramEpidemicRegisterDataId: $('#paramEpidemicRegisterDataId').val(),
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.epidemicRegisterReleaseId = page_param.paramEpidemicRegisterReleaseId;
            param.epidemicStatus = $(param_id.epidemicStatus).val();
            param.address = _.trim($(param_id.address).val());
            param.remark = $(param_id.remark).val();
            param.epidemicRegisterDataId = page_param.paramEpidemicRegisterDataId;

            //判断是否支持 获取本地位置
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (res) {
                    param.location = res.coords.latitude + "," + res.coords.longitude;
                });
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
            if(page_param.paramEpidemicRegisterDataId !== ''){
                initEpidemicStatus();
            }
            initMaxLength();
        }

        function initEpidemicStatus() {
            $(param_id.epidemicStatus).val(page_param.paramEpidemicStatus);
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.address).maxlength({
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

        $(param_id.address).blur(function () {
            initParam();
            var address = param.address;
            if (address.length <= 0 || address.length > 300) {
                tools.validErrorDom(param_id.address, '当前位置300个字符以内');
            } else {
                tools.validSuccessDom(param_id.address);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validAddress();
        });

        function validAddress() {
            var address = param.address;
            if (address.length <= 0 || address.length > 300) {
                tools.validErrorDom(param_id.address, '当前位置300个字符以内');
            } else {
                tools.validSuccessDom(param_id.address);
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