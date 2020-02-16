//# sourceURL=internship_regulate_edit.js
require(["jquery", "tools", "handlebars", "sweetalert2", "nav.active", "moment-with-locales", "messenger", "jquery.address",
        "flatpickr-zh", "bootstrap-maxlength"],
    function ($, tools, Handlebars, Swal, navActive, moment) {

        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/internship/regulate/update',
            page: '/web/menu/internship/regulate'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            internshipRegulateId: '#internshipRegulateId',
            studentName: '#studentName',
            studentNumber: '#studentNumber',
            studentTel: '#studentTel',
            internshipContent: '#internshipContent',
            internshipProgress: '#internshipProgress',
            reportDate: '#reportDate',
            reportWay: '#reportWay'
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
            internshipRegulateId: '',
            studentName: '',
            studentNumber: '',
            studentTel: '',
            internshipContent: '',
            internshipProgress: '',
            reportWay: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.internshipRegulateId = $(param_id.internshipRegulateId).val();
            param.studentName = $(param_id.studentName).val();
            param.studentNumber = $(param_id.studentNumber).val();
            param.studentTel = $(param_id.studentTel).val();
            param.internshipContent = $(param_id.internshipContent).val();
            param.internshipProgress = $(param_id.internshipProgress).val();
            param.reportWay = $(param_id.reportWay).val();
        }

        init();

        function init() {
            initParam();
            initMaxLength();
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.internshipContent).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.internshipProgress).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.reportWay).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.reportDate).flatpickr({
            "locale": "zh"
        });

        /*
         返回
         */
        $('#regulateList').click(function () {
            window.history.go(-1);
        });

        $(param_id.internshipContent).blur(function () {
            initParam();
            var internshipContent = param.internshipContent;
            if (internshipContent.length <= 0 || internshipContent.length > 200) {
                tools.validErrorDom(param_id.internshipContent, '实习内容200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipContent);
            }
        });

        $(param_id.internshipProgress).blur(function () {
            initParam();
            var internshipProgress = param.internshipProgress;
            if (internshipProgress.length <= 0 || internshipProgress.length > 200) {
                tools.validErrorDom(param_id.internshipProgress, '实习进展200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipProgress);
            }
        });

        $(param_id.reportWay).blur(function () {
            initParam();
            var reportWay = param.reportWay;
            if (reportWay.length <= 0 || reportWay.length > 200) {
                tools.validErrorDom(param_id.reportWay, '汇报途径20个字符以内');
            } else {
                tools.validSuccessDom(param_id.reportWay);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validInternshipContent();
        });

        /**
         * 检验实习内容
         */
        function validInternshipContent() {
            var internshipContent = param.internshipContent;
            if (internshipContent.length <= 0 || internshipContent.length > 200) {
                Messenger().post({
                    message: '实习内容200个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validErrorDom(param_id.internshipContent, '实习内容200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipContent);
                validInternshipProgress();
            }
        }

        /**
         * 检验实习进展
         */
        function validInternshipProgress() {
            var internshipProgress = param.internshipProgress;
            if (internshipProgress.length <= 0 || internshipProgress.length > 200) {
                Messenger().post({
                    message: '实习进展200个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validErrorDom(param_id.internshipProgress, '实习进展200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipProgress);
                validReportWay();
            }
        }

        /**
         * 检验汇报途径
         */
        function validReportWay() {
            var reportWay = param.reportWay;
            if (reportWay.length <= 0 || reportWay.length > 200) {
                Messenger().post({
                    message: '汇报途径20个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validErrorDom(param_id.reportWay, '汇报途径20个字符以内');
            } else {
                tools.validSuccessDom(param_id.reportWay);
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
                data: $('#app_form').serialize(),
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                window.history.go(-1);
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