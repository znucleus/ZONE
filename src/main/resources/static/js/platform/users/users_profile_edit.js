//# sourceURL=users_profile_edit.js
require(["jquery", "lodash", "tools", "sweetalert2", "bootstrap", "dropify",
        "csrf"],
    function ($, _, tools, Swal) {

        var ajax_url = {
            users_update: web_path + '/users/update',
            users_avatar_upload: web_path + '/users/avatar/upload',
            users_avatar_delete: web_path + '/users/avatar/delete'
        };

        var param_id = {
            realName: '#realName',
            avatar: '.dropify-filename-inner'
        };

        var button_id = {
            avatar: {
                tip: '更新中...',
                text: '更新',
                id: '#updateAvatar'
            },
            info: {
                tip: '提交中...',
                text: '提交',
                id: '#updateInfo'
            }
        };

        var param = {
            avatar: '',
            realName: ''
        };

        var page_param = {
            avatar: $('#avatar').val()
        };

        function initParam() {
            param.realName = _.trim($(param_id.realName).val());
            param.avatar = $(param_id.avatar).text();
        }

        var drEvent = $('.dropify').dropify({
            messages: {
                'default': '点击或拖拽文件到这里',
                'replace': '点击或拖拽文件到这里来替换文件',
                'remove': '移除',
                'error': '文件上传错误'
            },
            error: {
                'fileSize': '文件过大，超过1MB.',
                'maxWidth': '图片最大宽带: 500px.',
                'maxHeight': '图片最大高度 500px.',
                'imageFormat': '仅允许上传 (jpg,png,gif,jpeg,bmp).',
                'fileExtension': '仅允许上传 (jpg,png,gif,jpeg,bmp).'
            }
        });

        drEvent.on('dropify.afterClear', function (event, element) {
            initParam();
            var avatar = param.avatar;
            var curAvatar = page_param.avatar;
            var fileName = curAvatar.substring(curAvatar.lastIndexOf('/') + 1);
            if (avatar === fileName) {
                $.get(ajax_url.users_avatar_delete, function (data) {
                    if (data.state) {
                        Swal.fire('移除成功', '', 'success')
                    } else {
                        Swal.fire('移除失败', data.msg, 'error')
                    }
                });
            }

        });

        $(button_id.avatar.id).click(function () {
            initParam();
            var avatar = param.avatar;
            var curAvatar = page_param.avatar;
            var fileName = curAvatar.substring(curAvatar.lastIndexOf('/') + 1);
            var globalAvatarError = $('#globalAvatarError');
            if (avatar !== fileName) {
                globalAvatarError.text('');
                var file = $('.dropify-render').children(0);
                var base64 = $(file).attr('src');
                if (!_.isUndefined(base64)) {
                    tools.buttonLoading(button_id.avatar.id, button_id.avatar.tip);
                    $.post(ajax_url.users_avatar_upload, {
                        'file': base64,
                        'fileName': $('.dropify-filename-inner').text()
                    }, function (data) {
                        tools.buttonEndLoading(button_id.avatar.id, button_id.avatar.text);
                        if (data.state) {
                            Swal.fire({
                                title: data.msg,
                                type: "success",
                                confirmButtonText: "确定",
                                preConfirm: function () {
                                    $('#logout').submit();
                                }
                            });
                        } else {
                            Swal.fire('更新失败', data.msg, 'error');
                        }
                    });
                } else {
                    globalAvatarError.text('请选择头像');
                }
            } else {
                globalAvatarError.text('请选择头像');
            }

        });

        $(button_id.info.id).click(function () {
            initParam();
            validRealName();
        });

        function validRealName() {
            var realName = param.realName;
            if (realName !== '') {
                tools.validSuccessDom(param_id.realName);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.realName, "您的姓名？");
            }
        }

        function sendAjax() {
            tools.buttonLoading(button_id.info.id, button_id.info.tip);
            $.post(ajax_url.users_update, {
                name: 'realName',
                value: param.realName
            }, function (data) {
                tools.buttonEndLoading(button_id.info.id, button_id.info.text);
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    Swal.fire('更新失败', data.msg, 'error');
                }
            });
        }


    });