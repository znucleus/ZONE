//# sourceURL=training_document.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "messenger", "jquery.address", "jquery.simple-pagination", "jquery-labelauty"],
    function ($, _, tools, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/report/training/data',
            generate: web_path + '/web/training/report/generate',
            page: '/web/menu/training/report'
        };

        navActive(ajax_url.page);

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 2,
            displayedPages: 3,
            orderColumnName: 'releaseTime',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                title: '',
                dataRange: 0,
                publisher: ''
            })
        };

        var report_param = {
            type: -1
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'TRAINING_REPORT_TITLE_SEARCH',
            DATA_RANGE: 'TRAINING_REPORT_DATA_RANGE_SEARCH',
            PUBLISHER: 'TRAINING_REPORT_PUBLISHER_SEARCH'
        };

        /*
         参数id
         */
        var param_id = {
            title: '#search_title',
            dataRange: '#dataRange',
            publisher: '#search_publisher'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.title).val('');
            $(param_id.publisher).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.TITLE, $(param_id.title).val());
                sessionStorage.setItem(webStorageKey.DATA_RANGE, _.isUndefined($("input[name='dataRange']:checked").val()) ? '0' : '1');
                sessionStorage.setItem(webStorageKey.PUBLISHER, $(param_id.publisher).val());
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

        $(param_id.title).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.dataRange).click(function () {
            refreshSearch();
            init();
        });

        $(param_id.publisher).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#release-template").html());
            $(tableData).html(template(data));
        }

        var senior = $('#senior');

        /*
        实训归档封面(基础信息)
         */
        $('#fileBase').click(function () {
            senior.prev().addClass('col-md-12').removeClass('col-md-5');
            senior.css('display', 'none').removeClass('col-md-7');
            window.location.href = ajax_url.generate + '/0/0';
        });

        /*
        实训归档封面(课程信息)
         */
        $('#fileSenior').click(function () {
            report_param.type = 1;
            senior.prev().removeClass('col-md-12').addClass('col-md-5');
            senior.css('display', '').addClass('col-md-7');
        });

        /*
        实训归档封面(基础信息)
         */
        $('#situationBase').click(function () {
            senior.prev().addClass('col-md-12').removeClass('col-md-5');
            senior.css('display', 'none').removeClass('col-md-7');
            window.location.href = ajax_url.generate + '/2/0';
        });

        /*
        实训情况汇总表(课程信息)
        */
        $('#situationSenior').click(function () {
            report_param.type = 3;
            senior.prev().removeClass('col-md-12').addClass('col-md-5');
            senior.css('display', '').addClass('col-md-7');
        });

        /*
        普通模板(基础信息)
         */
        $('#reportBase').click(function () {
            senior.prev().addClass('col-md-12').removeClass('col-md-5');
            senior.css('display', 'none').removeClass('col-md-7');
            window.location.href = ajax_url.generate + '/4/0';
        });

        /*
        高级模板(课程信息)
        */
        $('#reportSenior').click(function () {
            report_param.type = 5;
            senior.prev().removeClass('col-md-12').addClass('col-md-5');
            senior.css('display', '').addClass('col-md-7');
        });

        /*
         生成
        */
        $(tableData).delegate('.generate', "click", function () {
            senior.prev().addClass('col-md-12').removeClass('col-md-5');
            senior.css('display', 'none').removeClass('col-md-7');
            window.location.href = ajax_url.generate + '/' + report_param.type + '/' + $(this).attr('data-id');
        });

        init();
        initSearchInput();
        initLabelauty();

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
            var title = null;
            var dataRange = null;
            var publisher = null;
            var params = {
                title: '',
                dataRange: 0,
                publisher: ''
            };
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
                publisher = sessionStorage.getItem(webStorageKey.PUBLISHER);
            }
            if (title !== null) {
                params.title = title;
            } else {
                params.title = $(param_id.title).val();
            }

            if (dataRange !== null) {
                params.dataRange = dataRange;
            } else {
                params.dataRange = _.isUndefined($("input[name='dataRange']:checked").val()) ? 0 : 1;
            }

            if (publisher !== null) {
                params.publisher = publisher;
            } else {
                params.publisher = $(param_id.publisher).val();
            }
            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var title = null;
            var dataRange = null;
            var publisher = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
                publisher = sessionStorage.getItem(webStorageKey.PUBLISHER);
            }
            if (title !== null) {
                $(param_id.title).val(title);
            }

            if (dataRange !== null && Number(dataRange) === 1) {
                $(param_id.dataRange).prop('checked', true);
            }

            if (publisher !== null) {
                $(param_id.publisher).val(publisher);
            }
        }

        function initLabelauty() {
            $(".labelauty").labelauty();
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

    });