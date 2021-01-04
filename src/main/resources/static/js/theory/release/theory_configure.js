//# sourceURL=theory_configure.js
require(["jquery", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger", "jquery.address"],
    function ($, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/theory/release/configure/data',
                add: web_path + '/web/theory/release/configure/add',
                edit: web_path + '/web/theory/release/configure/edit',
                del: web_path + '/web/theory/release/configure/delete',
                page: '/web/menu/theory/release'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            paramTheoryReleaseId: $('#paramTheoryReleaseId').val()
        };

        init();

        function init() {
            initData();
        }

        function initData() {
            $.get(getAjaxUrl().data + '/' + page_param.paramTheoryReleaseId, function (data) {
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

            Handlebars.registerHelper('week_day', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.weekDay(this.weekDay)));
            });
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
            $.address.value(getAjaxUrl().add + '/' + page_param.paramTheoryReleaseId);
        });


        tableElement.delegate('.edit', "click", function () {
            $.address.value(getAjaxUrl().edit + '/' + $(this).attr('data-id'));
        });

        tableElement.delegate('.del', "click", function () {
            configure_del($(this).attr('data-id'));
        });

        /*
         删除
         */
        function configure_del(theoryConfigureId) {
            Swal.fire({
                title: "确定删除吗？",
                text: "配置删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelAjax(theoryConfigureId);
                }
            });
        }

        /**
         * 删除ajax
         * @param theoryConfigureId
         */
        function sendDelAjax(theoryConfigureId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {theoryConfigureId: theoryConfigureId},
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