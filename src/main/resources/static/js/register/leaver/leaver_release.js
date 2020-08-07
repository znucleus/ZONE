//# sourceURL=leaver_release.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination", "jquery-labelauty"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/register/leaver/data',
            add: '/web/register/leaver/release/add',
            edit: '/web/register/leaver/release/edit',
            del: web_path + '/web/register/leaver/release/delete',
            register: '/web/register/leaver/data/add',
            cancel_register: '/web/register/leaver/data/delete',
            review: '/web/register/leaver/review',
            page: '/web/menu/register/leaver'
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
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'REGISTER_LEAVER_TITLE_SEARCH',
            DATA_RANGE: 'REGISTER_LEAVER_DATA_RANGE_SEARCH',
            PAGE_NUM: 'REGISTER_LEAVER_PAGE_NUM'
        };

        /*
         参数id
         */
        var param_id = {
            title: '#search_title',
            dataRange: '#dataRange'
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
         登记
        */
        $(tableData).delegate('.register', "click", function () {
            $.address.value(ajax_url.register + '/' + $(this).attr('data-id'));
        });

        /*
         取消登记
        */
        $(tableData).delegate('.cancelRegister', "click", function () {
            leaverDataDel($(this).attr('data-id'));
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
            leaverReleaseDel($(this).attr('data-id'), $(this).attr('data-name'));
        });

        /**
         * 删除确认
         * @param id 发布id
         * @param name 标题
         */
        function leaverReleaseDel(id, name) {
            Swal.fire({
                title: "确定删除离校登记 '" + name + "' 吗？",
                text: "离校登记删除！",
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
         * @param leaverRegisterReleaseId
         */
        function sendDelAjax(leaverRegisterReleaseId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.del,
                data: {leaverRegisterReleaseId: leaverRegisterReleaseId},
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

        /**
         * 取消登记确认
         * @param id 发布id
         */
        function leaverDataDel(id) {
            Swal.fire({
                title: "确定取消登记吗？",
                text: "离校登记取消！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendLeaverDataDelAjax(id);
                }
            });
        }

        /**
         * 取消登记ajax
         * @param leaverRegisterReleaseId
         */
        function sendLeaverDataDelAjax(leaverRegisterReleaseId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.cancel_register,
                data: {leaverRegisterReleaseId: leaverRegisterReleaseId},
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
            var pageNum = null;
            var params = {
                title: '',
                dataRange: 0
            };
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
                pageNum = sessionStorage.getItem(webStorageKey.PAGE_NUM);
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
            var dataRange = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
            }
            if (title !== null) {
                $(param_id.title).val(title);
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