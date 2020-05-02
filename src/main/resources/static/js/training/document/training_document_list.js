//# sourceURL=training_document_list.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address",
        "jquery.simple-pagination", "jquery.fileupload-validate"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/document/list/data',
            add: '/web/training/document/add',
            edit: '/web/training/document/edit',
            del: web_path + '/web/training/document/delete',
            file_data: web_path + '/web/training/document/file/data',
            look: '/web/training/document/look',
            file_upload_url: web_path + '/web/training/document/upload/file',
            delete_file_url: web_path + '/web/training/document/delete/file',
            download: '/web/training/document/download',
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

        /*
       删除
        */
        $(documentTableData).delegate('.del', "click", function () {
            document_del($(this).attr('data-id'));
        });

        /*
        文章删除
        */
        function document_del(trainingDocumentId) {
            Swal.fire({
                title: "确定删除该文章吗？",
                text: "文章删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    $.ajax({
                        type: 'POST',
                        url: ajax_url.del,
                        data: {trainingDocumentId: trainingDocumentId},
                        success: function (data) {
                            Messenger().post({
                                message: data.msg,
                                type: data.state ? 'success' : 'error',
                                showCloseButton: true
                            });

                            if (data.state) {
                                initDocument();
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

        // 上传组件
        $('#fileupload').fileupload({
            url: ajax_url.file_upload_url,
            dataType: 'json',
            maxFileSize: 500000000,// 500MB
            formAcceptCharset: 'utf-8',
            messages: {
                maxFileSize: '单文件上传仅允许500MB大小'
            },
            submit: function (e, data) {
                data.formData = {
                    'trainingReleaseId': page_param.paramTrainingReleaseId
                };
            },
            done: function (e, data) {
                Messenger().post({
                    message: data.result.msg,
                    type: data.result.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.result.state) {
                    initDocumentFile();
                }
            }
        }).on('fileuploadadd', function (evt, data) {
            var isOk = true;
            var $this = $(this);
            var validation = data.process(function () {
                return $this.fileupload('process', data);
            });
            validation.fail(function (data) {
                isOk = false;
                Messenger().post({
                    message: '上传失败: ' + data.files[0].error,
                    type: 'error',
                    showCloseButton: true
                });
            });
            return isOk;
        });

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