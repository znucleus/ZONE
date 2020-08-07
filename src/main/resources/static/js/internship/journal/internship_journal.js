//# sourceURL=internship_journal.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "messenger", "jquery.address", "jquery.simple-pagination", "jquery-labelauty"],
    function ($, _, tools, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/internship/journal/internship/data',
            journal: '/web/internship/journal/list',
            my_journal: '/web/internship/journal/my/list',
            add: '/web/internship/journal/add',
            page: '/web/menu/internship/journal'
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
                internshipTitle: '',
                dataRange: 0,
                internshipReleaseIsDel: 0
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            INTERNSHIP_TITLE: 'INTERNSHIP_JOURNAL_INTERNSHIP_TITLE_SEARCH',
            DATA_RANGE: 'INTERNSHIP_JOURNAL_DATA_RANGE_SEARCH',
            PAGE_NUM: 'INTERNSHIP_JOURNAL_PAGE_NUM'
        };

        /*
         参数id
         */
        var param_id = {
            internshipTitle: '#search_internship_title',
            dataRange: '#dataRange'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.internshipTitle).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.INTERNSHIP_TITLE, $(param_id.internshipTitle).val());
                sessionStorage.setItem(webStorageKey.DATA_RANGE, _.isUndefined($("input[name='dataRange']:checked").val()) ? '0' : '1');
                sessionStorage.setItem(webStorageKey.PAGE_NUM, "0");
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

        $(param_id.internshipTitle).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.dataRange).click(function () {
            refreshSearch();
            init();
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#internship-release-template").html());
            $(tableData).html(template(data));
        }

        /*
        日志列表
        */
        $(tableData).delegate('.journal_list', "click", function () {
            var id = $(this).attr('data-id');
            $.address.value(ajax_url.journal + "/" + id);
        });

        /*
        我的日志
        */
        $(tableData).delegate('.my_journal', "click", function () {
            var id = $(this).attr('data-id');
            $.address.value(ajax_url.my_journal + "/" + id);
        });

        /*
        写日志
        */
        $(tableData).delegate('.write_journal', "click", function () {
            var id = $(this).attr('data-id');
            $.address.value(ajax_url.add + '/' + id);
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
            var internshipTitle = null;
            var dataRange = null;
            var pageNum = null;
            var params = {
                internshipTitle: '',
                dataRange: 0,
                internshipReleaseIsDel: 0
            };
            if (typeof (Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
                pageNum = sessionStorage.getItem(webStorageKey.PAGE_NUM);
            }
            if (internshipTitle !== null) {
                params.internshipTitle = internshipTitle;
            } else {
                params.internshipTitle = $(param_id.internshipTitle).val();
            }

            if (dataRange !== null) {
                params.dataRange = dataRange;
            } else {
                params.dataRange = _.isUndefined($("input[name='dataRange']:checked").val()) ? 0 : 1;
            }

            if (pageNum !== null) {
                param.pageNum = pageNum;
            } else {
                param.pageNum = 0;
            }
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var internshipTitle = null;
            var dataRange = null;
            if (typeof (Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
            }
            if (internshipTitle !== null) {
                $(param_id.internshipTitle).val(internshipTitle);
            }

            if (dataRange !== null && Number(dataRange) === 1) {
                $(param_id.dataRange).prop('checked', true);
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
                currentPage: data.page.pageNum,
                hrefTextPrefix: '',
                prevText: '<',
                nextText: '>',
                cssStyle: '',
                listStyle: 'pagination',
                onPageClick: function (pageNumber, event) {
                    // Callback triggered when a page is clicked
                    // Page number is given as an optional parameter
                    if (typeof (Storage) !== "undefined") {
                        sessionStorage.setItem(webStorageKey.PAGE_NUM, pageNumber);
                    }
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