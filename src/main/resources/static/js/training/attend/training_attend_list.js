//# sourceURL=training_attend_list.js
require(["jquery",  "tools", "handlebars", "nav.active", "messenger", "jquery.address", "jquery.simple-pagination", "flatpickr-zh"],
    function ($, tools, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/attend/data',
            configure_data: web_path + '/web/training/attend/configure/data',
            configure_release: web_path + '/web/training/attend/configure/release',
            release: '/web/training/attend/release',
            page: '/web/menu/training/attend'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val()
        };

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 2,
            displayedPages: 3,
            orderColumnName: 'publishDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                attendDate: '',
                trainingReleaseId: page_param.paramTrainingReleaseId
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            ATTEND_DATE: 'TRAINING_ATTEND_DATE_SEARCH'
        };

        /*
         参数id
         */
        var param_id = {
            attendDate: '#search_attend_date'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.attendDate).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.ATTEND_DATE, $(param_id.attendDate).val());
            }
        }

        /*
         搜索
         */
        $('#search').click(function () {
            refreshSearch();
            init();
        });

        /*
         重置
         */
        $('#reset_search').click(function () {
            cleanParam();
            refreshSearch();
            init();
        });

        $('#refresh').click(function () {
            init();
        });

        $(param_id.attendDate).flatpickr({
            "locale": "zh",
            "mode": "range",
            onClose: function () {
                refreshSearch();
                init();
            }
        });

        $('#customRelease').click(function () {
            $.address.value(ajax_url.release + '/' + page_param.paramTrainingReleaseId);
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#attend-template").html());
            $(tableData).html(template(data));
        }

        /*
         列表
         */
        $(tableData).delegate('.list', "click", function () {
            $.address.value(ajax_url.list + '/' + $(this).attr('data-id'));
        });

        init();
        initSearchInput();

        /**
         * 初始化数据
         */
        function init() {
            initSearchContent();
            tools.dataLoading();
            $.get(ajax_url.data, param, function (data) {
                tools.dataEndLoading();
                createPage(data);
                listData(data);
            });
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var attendDate = null;
            var params = {
                attendDate: '',
                trainingReleaseId: page_param.paramTrainingReleaseId
            };
            if (typeof(Storage) !== "undefined") {
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
            }
            if (attendDate !== null) {
                params.attendDate = attendDate;
            } else {
                params.attendDate = $(param_id.attendDate).val();
            }

            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var attendDate = null;
            if (typeof(Storage) !== "undefined") {
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
            }
            if (attendDate !== null) {
                $(param_id.attendDate).val(attendDate);
            }
        }

        /**
         * 创建分页
         * @param data 数据
         */
        function createPage(data) {
            $('#pagination').pagination({
                pages: data.page.totalPages,
                displayedPages: data.page.displayedPages,
                hrefTextPrefix: '',
                prevText: '<',
                nextText: '>',
                cssStyle: '',
                listStyle: 'pagination',
                onPageClick: function (pageNumber, event) {
                    // Callback triggered when a page is clicked
                    // Page number is given as an optional parameter
                    nextPage(pageNumber);
                }
            });
        }

        /**
         * 下一页
         * @param pageNumber 当前页
         */
        function nextPage(pageNumber) {
            param.pageNum = pageNumber;
            tools.dataLoading();
            $.get(ajax_url.data, param, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

        $('#configureRelease').click(function () {
            $.get(ajax_url.configure_data + '/' + page_param.paramTrainingReleaseId, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    configureData(data);
                    $('#configureReleaseModal').modal('show');
                }
            });
        });


        /**
         * 列表数据
         * @param data 数据
         */
        function configureData(data) {
            var template = Handlebars.compile($("#configure-data-template").html());

            Handlebars.registerHelper('week_day', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.weekDay(this.weekDay)));
            });
            $('#dataTable > tbody').html(template(data));
        }

        $('#dataTable').delegate('.release', "click", function () {
            sendConfigureReleaseAjax($(this).attr('data-id'));
        });

        /**
         * 配置发布ajax
         * @param trainingConfigureId
         */
        function sendConfigureReleaseAjax(trainingConfigureId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.configure_release,
                data: {trainingConfigureId: trainingConfigureId},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        init();
                        $('#configureReleaseModal').modal('hide');
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