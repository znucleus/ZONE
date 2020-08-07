//# sourceURL=training_document_file.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address",
        "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            file_data: web_path + '/web/training/document/file/data',
            delete_file_url: web_path + '/web/training/document/delete/file',
            download: '/web/training/document/download',
            page: '/web/menu/training/document'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramCourseId: $('#paramCourseId').val()
        };

        var document_file_param = {
            pageNum: 0,
            length: 10,
            displayedPages: 3,
            orderColumnName: 'downloads',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                originalFileName: '',
                courseId: page_param.paramCourseId,
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            DOCUMENT_FILE: 'TRAINING_DOCUMENT_RELATION_FILE_SEARCH_' + page_param.paramTrainingReleaseId,
            PAGE_NUM: 'TRAINING_DOCUMENT_FILE_PAGE_NUM_' + page_param.paramTrainingReleaseId
        };

        /*
         参数id
         */
        var param_id = {
            documentFile: '#search_document_file'
        };

        var documentFileTableData = '#documentFileTableData';

        /*
         清空参数
         */
        function cleanDocumentFileParam() {
            $(param_id.documentFile).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshDocumentFileSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.DOCUMENT_FILE, $(param_id.documentFile).val());
                sessionStorage.setItem(webStorageKey.PAGE_NUM, "0");
            }
        }

        /*
         搜索
         */
        $('#document_file_search').click(function () {
            refreshDocumentFileSearch();
            initDocumentFile();
        });

        /*
         重置
         */
        $('#document_file_reset_search').click(function () {
            cleanDocumentFileParam();
            refreshDocumentFileSearch();
            initDocumentFile();
        });

        $('#document_file_refresh').click(function () {
            initDocumentFile();
        });

        $(param_id.documentFile).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshDocumentFileSearch();
                initDocumentFile();
            }
        });

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

        initDocumentFile();
        initDocumentFileSearchInput();

        /**
         * 初始化数据
         */
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
        function initDocumentFileSearchContent() {
            var documentFile = null;
            var documentPageNum = null;
            var params = {
                originalFileName: '',
                trainingReleaseId: page_param.paramTrainingReleaseId,
            };
            if (typeof (Storage) !== "undefined") {
                documentFile = sessionStorage.getItem(webStorageKey.DOCUMENT_FILE);
                documentPageNum = sessionStorage.getItem(webStorageKey.PAGE_NUM);
            }
            if (documentFile !== null) {
                params.originalFileName = documentFile;
            } else {
                params.originalFileName = $(param_id.documentFile).val();
            }

            if (documentPageNum !== null) {
                document_file_param.pageNum = documentPageNum;
            } else {
                document_file_param.pageNum = 0;
            }
            document_file_param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
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
        function createDocumentFilePage(data) {
            $('#documentFilePagination').pagination({
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
                    nextDocumentFilePage(pageNumber);
                }
            });
        }

        /**
         * 下一页
         * @param pageNumber 当前页
         */
        function nextDocumentFilePage(pageNumber) {
            document_file_param.pageNum = pageNumber;
            tools.dataLoading();
            $.get(ajax_url.file_data, document_file_param, function (data) {
                tools.dataEndLoading();
                documentFileData(data);
            });
        }

        /*
        下载
        */
        $(documentFileTableData).delegate('.download', "click", function () {
            window.location.href = ajax_url.download + '/' + $(this).attr('data-id');
        });

        /*
       删除
        */
        $(documentFileTableData).delegate('.del', "click", function () {
            document_file_del($(this).attr('data-id'));
        });

        /*
        文档删除
        */
        function document_file_del(trainingDocumentFileId) {
            Swal.fire({
                title: "确定删除该文档吗？",
                text: "文档删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    $.ajax({
                        type: 'POST',
                        url: ajax_url.delete_file_url,
                        data: {trainingDocumentFileId: trainingDocumentFileId},
                        success: function (data) {
                            Messenger().post({
                                message: data.msg,
                                type: data.state ? 'success' : 'error',
                                showCloseButton: true
                            });

                            if (data.state) {
                                initDocumentFile();
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
        }

    });