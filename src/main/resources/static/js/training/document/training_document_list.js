//# sourceURL=training_document_list.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "messenger", "jquery.address", "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/document/list/data',
            add: '/web/training/document/add',
            edit: '/web/training/document/edit',
            file_data: web_path + '/web/training/document/file/data',
            look: '/web/training/document/look',
            page: '/web/menu/training/document'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val()
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

        var document_file_param = {
            pageNum: 0,
            length: 10,
            displayedPages: 3,
            orderColumnName: 'createDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                originalFileName: '',
                trainingReleaseId: page_param.paramTrainingReleaseId,
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            DOCUMENT_TITLE: 'TRAINING_DOCUMENT_LIST_TITLE_SEARCH' + page_param.paramTrainingReleaseId,
            DOCUMENT_FILE: 'TRAINING_DOCUMENT_LIST_FILE_SEARCH' + page_param.paramTrainingReleaseId
        };

        /*
         参数id
         */
        var param_id = {
            documentTitle: '#search_document_title',
            documentFile: '#search_document_file'
        };

        var documentTableData = '#documentTableData';
        var documentFileTableData = '#documentFileTableData';

        /*
         清空参数
         */
        function cleanDocumentParam() {
            $(param_id.documentTitle).val('');
        }

        function cleanDocumentFileParam() {
            $(param_id.documentFile).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshDocumentSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.DOCUMENT_TITLE, $(param_id.documentTitle).val());
            }
        }

        function refreshDocumentFileSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.DOCUMENT_FILE, $(param_id.documentFile).val());
            }
        }

        /*
         搜索
         */
        $('#document_search').click(function () {
            refreshDocumentSearch();
            initDocument();
        });

        $('#document_file_search').click(function () {
            refreshDocumentFileSearch();
            initDocumentFile();
        });

        /*
         重置
         */
        $('#document_reset_search').click(function () {
            cleanDocumentParam();
            refreshDocumentSearch();
            initDocument();
        });

        $('#document_file_reset_search').click(function () {
            cleanDocumentFileParam();
            refreshDocumentFileSearch();
            initDocumentFile();
        });

        $('#document_refresh').click(function () {
            initDocument();
        });

        $('#document_file_refresh').click(function () {
            initDocumentFile();
        });

        $(param_id.documentTitle).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshDocumentSearch();
                initDocument();
            }
        });

        $(param_id.documentFile).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshDocumentFileSearch();
                initDocumentFile();
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

        /**
         * 文档列表数据
         * @param data 数据
         */
        function documentFileData(data) {
            var template = Handlebars.compile($("#document-file-template").html());
            Handlebars.registerHelper('file_size', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.toSize(this.fileSize)));
            });
            $(documentFileTableData).html(template(data));
        }

        $('#document_add').click(function () {
            $.address.value(ajax_url.add + '/' + page_param.paramTrainingReleaseId);
        });

        /*
         查看
         */
        $(documentTableData).delegate('.look', "click", function () {
            $.address.value(ajax_url.look + '/' + $(this).attr('data-id'));
        });

        /*
         编辑
         */
        $(documentTableData).delegate('.edit', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        initDocument();
        initDocumentFile();
        initDocumentSearchInput();
        initDocumentFileSearchInput();

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

        function initDocumentFile() {
            initDocumentFileSearchContent();
            tools.dataLoading();
            $.get(ajax_url.file_data, document_file_param, function (data) {
                tools.dataEndLoading();
                createDocumentFilePage(data);
                documentFileData(data);
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
            if (typeof (Storage) !== "undefined") {
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

        function initDocumentFileSearchContent() {
            var documentFile = null;
            var params = {
                originalFileName: '',
                trainingReleaseId: page_param.paramTrainingReleaseId,
            };
            if (typeof (Storage) !== "undefined") {
                documentFile = sessionStorage.getItem(webStorageKey.DOCUMENT_FILE);
            }
            if (documentFile !== null) {
                params.originalFileName = documentFile;
            } else {
                params.originalFileName = $(param_id.documentFile).val();
            }

            document_file_param.pageNum = 0;
            document_file_param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initDocumentSearchInput() {
            var documentTitle = null;
            if (typeof (Storage) !== "undefined") {
                documentTitle = sessionStorage.getItem(webStorageKey.DOCUMENT_TITLE);
            }
            if (documentTitle !== null) {
                $(param_id.documentTitle).val(documentTitle);
            }
        }

        function initDocumentFileSearchInput() {
            var documentFile = null;
            if (typeof (Storage) !== "undefined") {
                documentFile = sessionStorage.getItem(webStorageKey.DOCUMENT_FILE);
            }
            if (documentFile !== null) {
                $(param_id.documentFile).val(documentFile);
            }
        }

        /**
         * 创建分页
         * @param data 数据
         */
        function createDocumentPage(data) {
            $('#documentPagination').pagination({
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

        function createDocumentFilePage(data) {
            $('#documentFilePagination').pagination({
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
                    nextDocumentFilePage(pageNumber);
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

        function nextDocumentFilePage(pageNumber) {
            document_file_param.pageNum = pageNumber;
            tools.dataLoading();
            $.get(ajax_url.file_data, document_file_param, function (data) {
                tools.dataEndLoading();
                documentFileData(data);
            });
        }

    });