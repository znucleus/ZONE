//# sourceURL=training_special_edit.js
require(["jquery", "lodash", "tools", "nav.active", "sweetalert2", "dropify", "bootstrap-maxlength"],
    function ($, _, tools, navActive, Swal) {

        var ajax_url = {
            update: web_path + '/web/training/special/update',
            page: '/web/menu/training/special'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var param_id = {
            title: '#title',
            cover: '.dropify-filename-inner'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        var page_param = {
            cover: $('#cover').val(),
            paramTrainingSpecialId: $('#paramTrainingSpecialId').val()
        };

        var param = {
            trainingSpecialId: '',
            title: '',
            cover: '',
            file: '',
            fileName: ''
        };

        function initParam() {
            param.trainingSpecialId = page_param.paramTrainingSpecialId;
            param.title = _.trim($(param_id.title).val());
            param.cover = $(param_id.cover).text();
            param.file = $($('.dropify-render').children(0)).attr('src');
            param.fileName = $(param_id.cover).text()
        }

        init();

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
        }

        $('.dropify').dropify({
            messages: {
                'default': '点击或拖拽文件到这里',
                'replace': '点击或拖拽文件到这里来替换文件',
                'remove': '移除',
                'error': '文件上传错误'
            },
            error: {
                'fileSize': '文件过大，超过1MB.',
                'maxWidth': '图片最大宽带: 1300px.',
                'maxHeight': '图片最大高度 650px.',
                'imageFormat': '仅允许上传 (jpg,png,gif,jpeg,bmp).',
                'fileExtension': '仅允许上传 (jpg,png,gif,jpeg,bmp).'
            }
        });

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 200) {
                tools.validErrorDom(param_id.title, '标题200个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
            }
        });

        $(button_id.save.id).click(function () {
            initParam();
            validTitle();
        });

        function validTitle() {
            var title = param.title;
            if (title.length <= 0 || title.length > 200) {
                tools.validErrorDom(param_id.title, '标题200个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
                sendAjax();
            }
        }

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