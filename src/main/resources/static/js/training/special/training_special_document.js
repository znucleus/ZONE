//# sourceURL=training_special_document.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address",
        "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/special/document/paging',
            add: '/web/training/special/document/add',
            edit: '/web/training/special/document/edit',
            del: web_path + '/web/training/special/document/delete',
            look: '/web/training/special/document/look',
            page: '/web/menu/training/special'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTrainingSpecialId: $('#paramTrainingSpecialId').val()
        };

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 10,
            displayedPages: 3,
            orderColumnName: 'createDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                title: '',
                trainingSpecialId: page_param.paramTrainingSpecialId,
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'TRAINING_SPECIAL_DOCUMENT_TITLE_SEARCH_' + page_param.paramTrainingSpecialId,
            PAGE_NUM: 'TRAINING_SPECIAL_DOCUMENT_PAGE_NUM_' + page_param.paramTrainingSpecialId,
        };

        /*
         参数id
         */
        var param_id = {
            title: '#search_title'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.title).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.TITLE, $(param_id.title).val());
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

        $(param_id.title).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        /**
         * 文章列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#document-template").html());
            $(tableData).html(template(data));
        }

        $('#add').click(function () {
            $.address.value(ajax_url.add + '/' + page_param.paramTrainingSpecialId);
        });

        /*
         查看
         */
        $(tableData).delegate('.look', "click", function () {
            $.address.value(ajax_url.look + '/' + $(this).attr('data-id'));
        });

        /*
         编辑
         */
        $(tableData).delegate('.edit', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        /*
       删除
        */
        $(tableData).delegate('.del', "click", function () {
            document_del($(this).attr('data-id'));
        });

        /*
        文章删除
        */
        function document_del(trainingSpecialDocumentId) {
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
                        data: {trainingSpecialDocumentId: trainingSpecialDocumentId},
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
        }

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
            var title = null;
            var pageNum = null;
            var params = {
                title: '',
                trainingSpecialId: page_param.paramTrainingSpecialId,
            };
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                pageNum = sessionStorage.getItem(webStorageKey.PAGE_NUM);
            }
            if (title !== null) {
                params.title = title;
            } else {
                params.title = $(param_id.title).val();
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
            var title = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
            }
            if (title !== null) {
                $(param_id.title).val(title);
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