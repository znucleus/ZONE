//# sourceURL=internship_review_authorize.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/review/authorize/paging',
                save: web_path + '/web/internship/review/authorize/save',
                del: web_path + '/web/internship/review/authorize/delete',
                page: '/web/menu/internship/review'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val()
        };

        /*
        参数
        */
        var param = {
            orderColumnName: 'username',
            orderDir: 'asc',
            extraSearch: JSON.stringify({
                internshipReleaseId: page_param.paramInternshipReleaseId
            })
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
            var param_id = '#username';
            var username = _.trim($(param_id).val());
            if (username.length <= 0 || username.length > 64) {
                tools.validErrorDom(param_id, '账号64个字符以内');
            } else {
                tools.validSuccessDom(param_id);
                sendAjax();
            }
        }

        function sendAjax() {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().save,
                data: $('#app_form').serialize(),
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
        function authorize_del(username) {
            Swal.fire({
                title: "确定删除吗？",
                text: "用户删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelAjax(username);
                }
            });
        }

        /**
         * 删除ajax
         * @param username
         */
        function sendDelAjax(username) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {internshipReleaseId: page_param.paramInternshipReleaseId, username: username},
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