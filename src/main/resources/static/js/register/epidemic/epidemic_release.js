//# sourceURL=epidemic_release.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/register/epidemic/data',
            add: '/web/register/epidemic/release/add',
            edit: '/web/register/epidemic/release/edit',
            del: web_path + '/web/register/epidemic/release/delete',
            register:'/web/register/epidemic/data/add',
            review:'/web/register/epidemic/review',
            page: '/web/menu/register/epidemic'
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
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'REGISTER_EPIDEMIC_TITLE_SEARCH'
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
            var template = Handlebars.compile($("#epidemic-release-template").html());
            $(tableData).html(template(data));
        }

        /*
         编辑
         */
        $(tableData).delegate('.edit', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        /*
         登记
        */
        $(tableData).delegate('.register', "click", function () {
            $.address.value(ajax_url.register + '/' + $(this).attr('data-id'));
        });

        /*
         统计
        */
        $(tableData).delegate('.review', "click", function () {
            $.address.value(ajax_url.review + '/' + $(this).attr('data-id'));
        });

        /*
         删除
         */
        $(tableData).delegate('.del', "click", function () {
            epidemicReleaseDel($(this).attr('data-id'), $(this).attr('data-name'));
        });

        /**
         * 删除确认
         * @param id 疫情发布id
         * @param name 标题
         */
        function epidemicReleaseDel(id, name) {
            Swal.fire({
                title: "确定删除疫情发布 '" + name + "' 吗？",
                text: "疫情发布删除！",
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
         * 注销
         * @param id
         */
        function del(id) {
            sendDelAjax(id);
        }

        /**
         * 删除ajax
         * @param epidemicRegisterReleaseId
         */
        function sendDelAjax(epidemicRegisterReleaseId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.del,
                data: {epidemicRegisterReleaseId: epidemicRegisterReleaseId},
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