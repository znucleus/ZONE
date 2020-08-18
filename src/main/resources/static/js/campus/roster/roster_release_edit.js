//# sourceURL=roster_release_edit.js
require(["jquery", "tools", "moment-with-locales", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "flatpickr-zh"],
    function ($, tools, moment, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/campus/roster/update',
            page: '/web/menu/campus/roster'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            rosterReleaseId:'#rosterReleaseId',
            title: '#title',
            startTime: '#startTime',
            endTime: '#endTime',
            remark: '#remark'
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
            rosterReleaseId:'',
            title: '',
            startTime: '',
            endTime: '',
            remark: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.rosterReleaseId = $(param_id.rosterReleaseId).val();
            param.title = $(param_id.title).val();
            var startTime = $(param_id.startTime).val();
            if (startTime.length > 0) {
                param.startTime = startTime + ":00";
            } else {
                param.startTime = startTime;
            }

            var endTime = $(param_id.endTime).val();
            if (endTime.length > 0) {
                param.endTime = endTime + ":00";
            } else {
                param.endTime = endTime;
            }
            param.remark = $(param_id.remark).val();
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
            $(param_id.title).maxlength({
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

        $(param_id.startTime).flatpickr({
            locale: "zh",
            enableTime: true,
            dateFormat: "Y-m-d H:i"
        });

        $(param_id.endTime).flatpickr({
            locale: "zh",
            enableTime: true,
            dateFormat: "Y-m-d H:i"
        });

        $(param_id.remark).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.remark);
            }
        });

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
            }
        });

        $(param_id.remark).blur(function () {
            initParam();
            var remark = param.remark;
            if (remark.length > 200) {
                tools.validErrorDom(param_id.remark, '备注200个字符以内');
            } else {
                tools.validSuccessDom(param_id.remark);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validTitle();
        });

        function validTitle() {
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
                validStartTime();
            }
        }

        function validStartTime() {
            var startTime = param.startTime;
            if (startTime.length <= 0) {
                tools.validErrorDom(param_id.startTime, '请选择开始填写时间');
            } else {
                tools.validSuccessDom(param_id.startTime);
                validEndTime();
            }
        }

        function validEndTime() {
            var endTime = param.endTime;
            if (endTime.length <= 0) {
                tools.validErrorDom(param_id.endTime, '请选择结束填写时间');
            } else {
                var startTime = param.startTime;
                if (moment(endTime, 'YYYY-MM-DD HH:mm:ss').isSameOrAfter(moment(startTime, 'YYYY-MM-DD HH:mm:ss'))) {
                    tools.validSuccessDom(param_id.endTime);
                    validRemark();
                } else {
                    tools.validErrorDom(param_id.endTime, '开始填写时间应大于或等于结束填写时间');
                }
            }
        }

        function validRemark() {
            var remark = param.remark;
            if (remark.length > 200) {
                tools.validErrorDom(param_id.remark, '备注200个字符以内');
            } else {
                tools.validSuccessDom(param_id.remark);
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