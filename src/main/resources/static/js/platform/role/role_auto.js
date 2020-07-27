//# sourceURL=role_auto.js
require(["jquery", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address"],
    function ($, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/platform/role/auto/save',
            page: '/web/menu/platform/role'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            userType: 'userType',
            roleId: '#roleId'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        init();

        function init() {
            var usersTypeIds = $('#usersTypeIds').val();
            var usersTypeIdsArr = usersTypeIds.split(",");
            var usersTypes = $('input[name="usersTypeId"]');
            for (var i = 0; i < usersTypeIdsArr.length; i++) {
                var di = usersTypeIdsArr[i];
                for (var j = 0; j < usersTypes.length; j++) {
                    if (di === $(usersTypes[j]).val()) {
                        $(usersTypes[j]).prop("checked", true);
                    }
                }
            }
        }

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            sendAjax();
        });

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