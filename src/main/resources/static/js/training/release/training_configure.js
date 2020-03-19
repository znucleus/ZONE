//# sourceURL=training_configure.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/training/release/configure/data',
                save: web_path + '/web/training/release/configure/save',
                del: web_path + '/web/training/release/configure/delete',
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

            Handlebars.registerHelper('week_day', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(weekDay(this.weekDay)));
            });
            $('#dataTable > tbody').html(template(data));
            $('#totalSize').text(data.listResult.length);
            tableElement.tablesaw().data("tablesaw").refresh();
        }

        function weekDay(day) {
            var v = '';
            switch (day) {
                case 1:
                    v = '星期一';
                    break;
                case 2:
                    v = '星期二';
                    break;
                case 3:
                    v = '星期三';
                    break;
                case 4:
                    v = '星期四';
                    break;
                case 5:
                    v = '星期五';
                    break;
                case 6:
                    v = '星期六';
                    break;
                case 7:
                    v = '星期天';
                    break;
            }
            return v;
        }

        $('#refresh').click(function () {
            init();
        });

        /*
         添加
         */
        $('#add').click(function () {

        });


        tableElement.delegate('.del', "click", function () {
            configure_del($(this).attr('data-id'));
        });

        /*
         删除
         */
        function configure_del(trainingConfigureId) {
            Swal.fire({
                title: "确定删除吗？",
                text: "配置删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelAjax(trainingConfigureId);
                }
            });
        }

        /**
         * 删除ajax
         * @param trainingConfigureId
         */
        function sendDelAjax(trainingConfigureId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {trainingConfigureId: trainingConfigureId},
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