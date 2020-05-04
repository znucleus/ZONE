//# sourceURL=training_special_document_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "quill", "KaTeX", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, _, tools, Handlebars, Swal, navActive, Quill, KaTeX) {

        window.katex = KaTeX;

        /*
         ajax url.
         */
        var ajax_url = {
            update: '/web/training/special/document/update',
            page: '/web/menu/training/special'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            title: '#title',
            quill: '#quill',
            content: '#content'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        var page_param = {
            paramTrainingSpecialDocumentId: $('#paramTrainingSpecialDocumentId').val()
        };

        /*
         参数
         */
        var param = {
            title: '',
            content: '',
            trainingSpecialDocumentId: ''
        };

        // 初始化富文本框
        var quill = new Quill(param_id.quill, {
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
            param.title = $(param_id.title).val();
            param.content = JSON.stringify(quill.getContents());
            param.trainingSpecialDocumentId = page_param.paramTrainingSpecialDocumentId;
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
            initMaxLength();
        }

        function initQuill() {
            quill.setContents(JSON.parse($(param_id.content).val()));
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
        }

        // 检验标题
        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 200) {
                tools.validErrorDom(param_id.title, '标题200个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
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
            if (title.length <= 0 || title.length > 200) {
                Messenger().post({
                    message: '标题200个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
            } else {
                validContent();
            }
        }

        /**
         * 检验内容
         */
        function validContent() {
            var content = quill.getText(0, quill.getLength());
            if (content.length <= 1) {
                Messenger().post({
                    message: '内容不能为空',
                    type: 'error',
                    showCloseButton: true
                });
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