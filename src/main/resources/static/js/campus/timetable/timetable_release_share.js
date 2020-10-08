//# sourceURL=timetable_release_share.js
require(["jquery", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, tools, Swal, navActive) {
        /*
        ajax url.
        */
        var ajax_url = {
            save: web_path + '/web/campus/timetable/share/save',
            release: web_path + '/web/campus/timetable/release',
            page: '/web/menu/campus/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
        参数id
        */
        var param_id = {
            shareId: '#shareId'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '确定',
                tip: '导入中...'
            }
        };

        /*
         参数
         */
        var param = {
            shareId: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.shareId = $(param_id.shareId).val();
        }

        $('#search').click(function () {
            initParam();
            $.get(ajax_url.release + '/' + param.shareId, function (data) {
                if (data.state) {
                    var term = data.release.term;
                    var t;
                    if (Number(term) === 0) {
                        t = '上学期';
                    } else if (Number(term) === 1) {
                        t = '下学期';
                    }
                    $('#shareIdHelp').text('由 ' + data.release.publisher + ' 分享，分享次数: ' + data.release.shareNumber + ' 学期: ' + data.release.startYear + '~' + data.release.endYear + ' ' + t);
                    $('#save').css('display', '');
                    tools.validCustomerSingleSuccessDom(param_id.shareId);
                } else {
                    tools.validCustomerSingleErrorDom(param_id.shareId, data.msg);
                    $('#shareIdHelp').text('');
                    $('#save').css('display', 'none');
                }
            });
        });

        /*
        保存数据
        */
        $(button_id.save.id).click(function () {
            initParam();
            validShareId();
        });

        function validShareId() {
            var shareId = param.shareId;
            if (shareId === '') {
                tools.validCustomerSingleErrorDom(param_id.shareId, '请填写共享ID');
                $('#shareIdHelp').text('');
            } else {
                tools.validCustomerSingleSuccessDom(param_id.shareId);
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