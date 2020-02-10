//# sourceURL=internship_release.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination", "jquery-labelauty"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/internship/release/data',
            add: '/web/internship/release/add',
            edit: '/web/internship/release/edit',
            status: web_path + '/web/internship/release/status',
            page: '/web/menu/internship/release'
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
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            INTERNSHIP_TITLE: 'INTERNSHIP_RELEASE_INTERNSHIP_TITLE_SEARCH',
            DATA_RANGE: 'INTERNSHIP_RELEASE_DATA_RANGE_SEARCH'
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
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.INTERNSHIP_TITLE, $(param_id.internshipTitle).val());
                sessionStorage.setItem(webStorageKey.DATA_RANGE, _.isUndefined($("input[name='dataRange']:checked").val()) ? '0' : '1');
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
            var template = Handlebars.compile($("#internship-release-template").html());
            $(tableData).html(template(data));
        }

        /*
         编辑
         */
        $(tableData).delegate('.edit', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        /*
         注销
         */
        $(tableData).delegate('.del', "click", function () {
            internshipReleaseDel($(this).attr('data-id'), $(this).attr('data-name'));
        });

        /*
         恢复
         */
        $(tableData).delegate('.recovery', "click", function () {
            internshipReleaseRecovery($(this).attr('data-id'), $(this).attr('data-name'));
        });

        /**
         * 注销确认
         * @param id 实习发布id
         * @param name 标题
         */
        function internshipReleaseDel(id, name) {
            Swal.fire({
                title: "确定注销实习发布 '" + name + "' 吗？",
                text: "实习发布注销！",
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
         * 恢复确认
         * @param id 实习发布id
         * @param name 标题
         */
        function internshipReleaseRecovery(id, name) {
            Swal.fire({
                title: "确定恢复实习发布 '" + name + "' 吗？",
                text: "实习发布恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(id);
                }
            });
        }

        /**
         * 注销
         * @param id
         */
        function del(id) {
            sendStatusAjax(id, 1);
        }

        /**
         * 恢复
         * @param id
         */
        function recovery(id) {
            sendStatusAjax(id, 0);
        }

        /**
         * 注销或恢复ajax
         * @param internshipReleaseId
         * @param isDel
         */
        function sendStatusAjax(internshipReleaseId, isDel) {
            $.ajax({
                type: 'POST',
                url: ajax_url.status,
                data: {internshipReleaseId: internshipReleaseId, isDel: isDel},
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
            var internshipTitle = null;
            var dataRange = null;
            var params = {
                internshipTitle: '',
                dataRange: 0
            };
            if (typeof(Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
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
            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var internshipTitle = null;
            var dataRange = null;
            if (typeof(Storage) !== "undefined") {
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