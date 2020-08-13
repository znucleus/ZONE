//# sourceURL=opens_add.js
require(["jquery", "tools", "sweetalert2", "nav.active", "quill", "KaTeX", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, tools, Swal, navActive, Quill, KaTeX) {

        window.katex = KaTeX;

        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/campus/opens/update',
            page: '/web/menu/campus/opens'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            openId: '#openId',
            title: '#title',
            content: '#content',
            textContent: '#textContent',
            jsonContent: '#jsonContent',
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
            openId: '',
            title: '',
            content: '',
            textContent: '',
            jsonContent: ''
        };

        var quill = new Quill(param_id.content, {
            modules: {
                formula: true,
                syntax: true,
                toolbar: '#toolbar-container'
            },
            placeholder: '开学内容',
            theme: 'snow'
        });

        /**
         * 初始化参数
         */
        function initParam() {
            param.openId = $(param_id.openId).val();
            param.title = $(param_id.title).val();
            param.jsonContent = JSON.stringify(quill.getContents());
            param.textContent = quill.getText(0, quill.getLength());
        }


        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            quill.setContents(JSON.parse($(param_id.jsonContent).val()));
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
        }

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
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
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
                validContent();
            }
        }

        function validContent() {
            var textContent = param.textContent;
            if (textContent.length <= 1) {
                Messenger().post({
                    message: '开学内容不能为空',
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