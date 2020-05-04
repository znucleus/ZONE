//# sourceURL=training_document_documents.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address",
        "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/document/list/data',
            edit: '/web/training/document/edit',
            del: web_path + '/web/training/document/delete',
            look: '/web/training/document/look',
            page: '/web/menu/training/document'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramCourseId: $('#paramCourseId').val()
        };

        /*
         参数
         */
        var document_param = {
            pageNum: 0,
            length: 10,
            displayedPages: 3,
            orderColumnName: 'reading',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                documentTitle: '',
                courseId: page_param.paramCourseId,
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            DOCUMENT_TITLE: 'TRAINING_DOCUMENT_RELATION_TITLE_SEARCH' + page_param.paramTrainingReleaseId
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
            if (typeof (Storage) !== "undefined") {
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
                courseId: page_param.paramCourseId,
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