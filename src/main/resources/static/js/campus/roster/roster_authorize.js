//# sourceURL=roster_authorize.js
require(["jquery", "lodash", "tools", "moment-with-locales", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger", "flatpickr-zh"],
    function ($, _, tools, moment, Handlebars, Swal, navActive) {

        moment.locale('zh-cn');

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/campus/roster/authorize/data',
                save: web_path + '/web/campus/roster/authorize/save',
                del: web_path + '/web/campus/roster/authorize/delete',
                page: '/web/menu/campus/roster'
            };
        }

        navActive(getAjaxUrl().page);

        /*
        参数
        */
        var param = {
            orderColumnName: 'validDate',
            orderDir: 'desc'
        };

        init();

        function init() {
            initData();
        }

        function initData() {
            $.get(getAjaxUrl().data, param, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    listData(data);
                }
            });
        }

        var tableElement = $('#dataTable');

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#data_template").html());
            $('#dataTable > tbody').html(template(data));
            $('#totalSize').text(data.page.totalSize);
            tableElement.tablesaw().data("tablesaw").refresh();
        }

        $('#validDate').flatpickr({
            locale: "zh",
            enableTime: true,
            dateFormat: "Y-m-d H:i"
        });

        $('#expireDate').flatpickr({
            locale: "zh",
            enableTime: true,
            dateFormat: "Y-m-d H:i"
        });

        $('#refresh').click(function () {
            init();
        });

        /*
         添加
         */
        $('#authorize_add').click(function () {
            $('#addModal').modal('show');
        });

        $('#save').click(function () {
            validUsername();
        });


        function validUsername() {
            var param_id = '#targetUsername';
            var username = _.trim($(param_id).val());
            if (username.length <= 0 || username.length > 64) {
                tools.validErrorDom(param_id, '账号64个字符以内');
            } else {
                tools.validSuccessDom(param_id);
                validValidDate();
            }
        }

        function validValidDate() {
            var param_id = '#validDate';
            var validDate = _.trim($(param_id).val());
            if (validDate.length <= 0) {
                tools.validErrorDom(param_id, '请选择生效时间');
            } else {
                tools.validSuccessDom(param_id);
                validExpireDate();
            }
        }

        function validExpireDate() {
            var param_id = '#expireDate';
            var expireDate = _.trim($(param_id).val());
            if (expireDate.length <= 0) {
                tools.validErrorDom(param_id, '请选择失效时间');
            } else {
                var validDate = _.trim($('#validDate').val() + ":00");
                if (moment(expireDate + ":00", 'YYYY-MM-DD HH:mm:ss').isSameOrAfter(moment(validDate, 'YYYY-MM-DD HH:mm:ss'))) {
                    tools.validSuccessDom(param_id);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id, '失效时间应大于或等于生效时间');
                }
            }
        }

        function sendAjax() {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().save,
                data: {
                    'targetUsername': $('#targetUsername').val(),
                    'validDate': $('#validDate').val() + ":00",
                    'expireDate': $('#expireDate').val() + ":00"
                },
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        init();
                        $('#addModal').modal('hide');
                    }
                },
                error: function (XMLHttpRequest) {
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        tableElement.delegate('.del', "click", function () {
            authorize_del($(this).attr('data-id'));
        });

        /*
         删除
         */
        function authorize_del(authoritiesId) {
            Swal.fire({
                title: "确定删除吗？",
                text: "权限删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelAjax(authoritiesId);
                }
            });
        }

        /**
         * 删除ajax
         * @param authoritiesId
         */
        function sendDelAjax(authoritiesId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {id: authoritiesId},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        init();
                    }
                },
                error: function (XMLHttpRequest) {
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }
    });