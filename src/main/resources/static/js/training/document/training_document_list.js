//# sourceURL=training_document_list.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "messenger", "jquery.address", "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/document/list/data',
            look: '/web/training/document/look',
            page: '/web/menu/training/document'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTrainingReleaseId:$('#paramTrainingReleaseId').val()
        };

        /*
         参数
         */
        var document_param = {
            pageNum: 0,
            length: 10,
            displayedPages: 3,
            orderColumnName: 'createDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                documentTitle: '',
                trainingReleaseId: page_param.paramTrainingReleaseId,
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            DOCUMENT_TITLE: 'TRAINING_DOCUMENT_LIST_TITLE_SEARCH' + page_param.paramTrainingReleaseId
        };

        /*
         参数id
         */
        var param_id = {
            documentTitle: '#search_document_title'
        };

        var documentTableData = '#documentTableData';

        /*
         清空参数
         */
        function cleanDocumentParam() {
            $(param_id.documentTitle).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshDocumentSearch() {
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.DOCUMENT_TITLE, $(param_id.documentTitle).val());
            }
        }

        /*
         搜索
         */
        $('#document_search').click(function () {
            refreshDocumentSearch();
            initDocument();
        });

        /*
         重置
         */
        $('#document_reset_search').click(function () {
            cleanDocumentParam();
            refreshDocumentSearch();
            initDocument();
        });

        $('#document_refresh').click(function () {
            initDocument();
        });

        $(param_id.documentTitle).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshDocumentSearch();
                initDocument();
            }
        });

        /**
         * 文章列表数据
         * @param data 数据
         */
        function documentListData(data) {
            var template = Handlebars.compile($("#document-template").html());
            $(documentTableData).html(template(data));
        }

        /*
         查看
         */
        $(documentTableData).delegate('.look', "click", function () {
            $.address.value(ajax_url.look + '/' + $(this).attr('data-id'));
        });

        initDocument();
        initDocumentSearchInput();

        /**
         * 初始化数据
         */
        function initDocument() {
            initDocumentSearchContent();
            tools.dataLoading();
            $.get(ajax_url.data, document_param, function (data) {
                tools.dataEndLoading();
                createDocumentPage(data);
                documentListData(data);
            });
        }

        /*
        初始化搜索内容
       */
        function initDocumentSearchContent() {
            var documentTitle = null;
            var params = {
                documentTitle: '',
                trainingReleaseId: page_param.paramTrainingReleaseId,
            };
            if (typeof(Storage) !== "undefined") {
                documentTitle = sessionStorage.getItem(webStorageKey.DOCUMENT_TITLE);
            }
            if (documentTitle !== null) {
                params.documentTitle = documentTitle;
            } else {
                params.documentTitle = $(param_id.documentTitle).val();
            }

            document_param.pageNum = 0;
            document_param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initDocumentSearchInput() {
            var documentTitle = null;
            if (typeof(Storage) !== "undefined") {
                documentTitle = sessionStorage.getItem(webStorageKey.DOCUMENT_TITLE);
            }
            if (documentTitle !== null) {
                $(param_id.documentTitle).val(documentTitle);
            }
        }

        /**
         * 创建分页
         * @param data 数据
         */
        function createDocumentPage(data) {
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
                    nextDocumentPage(pageNumber);
                }
            });
        }

        /**
         * 下一页
         * @param pageNumber 当前页
         */
        function nextDocumentPage(pageNumber) {
            document_param.pageNum = pageNumber;
            tools.dataLoading();
            $.get(ajax_url.data, document_param, function (data) {
                tools.dataEndLoading();
                documentListData(data);
            });
        }

    });