//# sourceURL=training_document_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "quill", "KaTeX", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Handlebars, Swal, navActive, Quill, KaTeX) {

        window.katex = KaTeX;

        /*
         ajax url.
         */
        var ajax_url = {
            update: '/web/training/document/update',
            page: '/web/menu/training/document'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            documentTitle: '#documentTitle',
            content: '#content',
            trainingDocumentContent: '#trainingDocumentContent',
            isOriginal: '#isOriginal',
            origin: '#origin'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val(),
            paramTrainingDocumentId: $('#paramTrainingDocumentId').val()
        };

        /*
         参数
         */
        var param = {
            documentTitle: '',
            trainingDocumentContent: '',
            isOriginal: '',
            origin: '',
            trainingReleaseId: '',
            trainingDocumentId: ''
        };

        // 初始化富文本框
        var quill = new Quill(param_id.content, {
            modules: {
                formula: true,
                syntax: true,
                toolbar: '#toolbar-container'
            },
            placeholder: '内容',
            theme: 'snow'
        });

        /**
         * 初始化参数
         */
        function initParam() {
            param.documentTitle = $(param_id.documentTitle).val();
            param.trainingDocumentContent = JSON.stringify(quill.getContents());
            var isOriginal = $('input[name="isOriginal"]:checked').val();
            param.isOriginal = _.isUndefined(isOriginal) ? 0 : isOriginal;
            param.origin = $(param_id.origin).val();
            param.trainingReleaseId = page_param.paramTrainingReleaseId;
            param.trainingDocumentId = page_param.paramTrainingDocumentId;
        }

        /*
        初始化数据
        */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initQuill();
            initOriginal();
            initMaxLength();
        }

        function initQuill() {
            quill.setContents(JSON.parse($(param_id.trainingDocumentContent).val()));
        }

        function initOriginal() {
            initParam();
            if (Number(param.isOriginal) === 0) {
                $(param_id.origin).parent().css('display', '');
            }
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.documentTitle).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        // 检验实习标题
        $(param_id.documentTitle).blur(function () {
            initParam();
            var documentTitle = param.documentTitle;
            if (documentTitle.length <= 0 || documentTitle.length > 200) {
                tools.validErrorDom(param_id.documentTitle, '标题200个字符以内');
            } else {
                tools.validSuccessDom(param_id.documentTitle);
            }
        });

        $(param_id.isOriginal).click(function () {
            initParam();
            var isOriginal = param.isOriginal;
            if (Number(isOriginal) === 0) {
                $(param_id.origin).parent().css('display', '');
            } else {
                $(param_id.origin).parent().css('display', 'none');
                $(param_id.origin).val('');
            }
        });

        $(param_id.origin).blur(function () {
            initParam();
            var origin = param.origin;
            if (origin.length <= 0 || origin.length > 500) {
                tools.validErrorDom(param_id.origin, '来源500个字符以内');
            } else {
                tools.validSuccessDom(param_id.origin);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validDocumentTitle();
        });

        function validDocumentTitle() {
            var documentTitle = param.documentTitle;
            if (documentTitle.length <= 0 || documentTitle.length > 200) {
                Messenger().post({
                    message: '标题200个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
            } else {
                validTrainingDocumentContent();
            }
        }

        /**
         * 检验内容
         */
        function validTrainingDocumentContent() {
            var content = quill.getText(0, quill.getLength());
            if (content.length <= 1) {
                Messenger().post({
                    message: '内容不能为空',
                    type: 'error',
                    showCloseButton: true
                });
            } else {
                validOrigin();
            }
        }

        function validOrigin() {
            var isOriginal = param.isOriginal;
            if (Number(isOriginal) === 0) {
                var origin = param.origin;
                if (origin.length <= 0 || origin.length > 500) {
                    Messenger().post({
                        message: '来源500个字符以内',
                        type: 'error',
                        showCloseButton: true
                    });
                } else {
                    sendAjax();
                }
            } else {
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