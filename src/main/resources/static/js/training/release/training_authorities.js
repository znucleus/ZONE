//# sourceURL=training_authorities.js
require(["jquery", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger", "jquery.address"],
    function ($, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/training/release/authorities/data',
                add: web_path + '/web/training/release/authorities/add',
                edit: web_path + '/web/training/release/authorities/edit',
                del: web_path + '/web/training/release/authorities/delete',
                page: '/web/menu/training/release'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val()
        };

        init();

        function init() {
            initData();
        }

        function initData() {
            $.get(getAjaxUrl().data + '/' + page_param.paramTrainingReleaseId, function (data) {
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
            $('#totalSize').text(data.listResult.length);
            tableElement.tablesaw().data("tablesaw").refresh();
        }

        $('#refresh').click(function () {
            init();
        });

        /*
         添加
         */
        $('#add').click(function () {
            $.address.value(getAjaxUrl().add + '/' + page_param.paramTrainingReleaseId);
        });


        tableElement.delegate('.edit', "click", function () {
            $.address.value(getAjaxUrl().edit + '/' + $(this).attr('data-id'));
        });

        tableElement.delegate('.del', "click", function () {
            authorities_del($(this).attr('data-id'));
        });

        /*
         删除
         */
        function authorities_del(authoritiesId) {
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
                data: {authoritiesId: authoritiesId},
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