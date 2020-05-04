//# sourceURL=training_special.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/training/special/data',
            add: '/web/training/special/add',
            edit: '/web/training/special/edit',
            del: web_path + '/web/training/special/delete',
            document: '/web/training/special/document',
            file: '/web/training/special/file',
            page: '/web/menu/training/special'
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
                title: ''
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'TRAINING_SPECIAL_TITLE_SEARCH'
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
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.TITLE, $(param_id.title).val());
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

        /*
         发布
         */
        $('#release').click(function () {
            $.address.value(ajax_url.add);
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#release-template").html());
            $(tableData).html(template(data));
        }

        /*
         编辑
         */
        $(tableData).delegate('.edit', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        /*
        文档
        */
        $(tableData).delegate('.document', "click", function () {
            $.address.value(ajax_url.document + '/' + $(this).attr('data-id'));
        });

        /*
        文件
        */
        $(tableData).delegate('.file', "click", function () {
            $.address.value(ajax_url.file + '/' + $(this).attr('data-id'));
        });

        /*
         删除
         */
        $(tableData).delegate('.del', "click", function () {
            trainingSpecialDel($(this).attr('data-id'));
        });

        /**
         * 删除确认
         * @param id 发布id
         */
        function trainingSpecialDel(id) {
            Swal.fire({
                title: "确定删除实训专题吗？",
                text: "实训专题删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(id);
                }
            });
        }

        /**
         * 删除
         * @param id
         */
        function del(id) {
            sendDelAjax(id);
        }


        /**
         * 删除ajax
         * @param trainingSpecialId
         */
        function sendDelAjax(trainingSpecialId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.del,
                data: {trainingSpecialId: trainingSpecialId},
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
            var params = {
                title: ''
            };
            if (typeof(Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
            }
            if (title !== null) {
                params.title = title;
            } else {
                params.title = $(param_id.title).val();
            }

            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var title = null;
            if (typeof(Storage) !== "undefined") {
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