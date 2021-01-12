//# sourceURL=theory_attend_list.js
require(["jquery", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination", "flatpickr-zh"],
    function ($, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/theory/attend/data',
            configure_data: web_path + '/web/theory/attend/configure/data',
            configure_release: web_path + '/web/theory/attend/configure/release',
            release: '/web/theory/attend/release',
            edit: '/web/theory/attend/edit',
            del: web_path + '/web/theory/attend/delete',
            users: '/web/theory/attend/users/list',
            my: '/web/theory/attend/my',
            situation: '/web/theory/attend/situation',
            page: '/web/menu/theory/attend'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTheoryReleaseId: $('#paramTheoryReleaseId').val()
        };

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 2,
            displayedPages: 3,
            orderColumnName: 'attendDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                attendDate: '',
                theoryReleaseId: page_param.paramTheoryReleaseId
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            ATTEND_DATE: 'THEORY_ATTEND_DATE_SEARCH_' + page_param.paramTheoryReleaseId,
            PAGE_NUM: 'THEORY_ATTEND_PAGE_NUM_' + page_param.paramTheoryReleaseId,
        };

        /*
         参数id
         */
        var param_id = {
            attendDate: '#search_attend_date'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.attendDate).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.ATTEND_DATE, $(param_id.attendDate).val());
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

        $(param_id.attendDate).flatpickr({
            "locale": "zh",
            "mode": "range",
            onClose: function () {
                refreshSearch();
                init();
            }
        });

        $('#customRelease').click(function () {
            $.address.value(ajax_url.release + '/' + page_param.paramTheoryReleaseId);
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#attend-template").html());
            $(tableData).html(template(data));
        }

        /*
         列表
         */
        $(tableData).delegate('.list', "click", function () {
            $.address.value(ajax_url.users + '/' + $(this).attr('data-id'));
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
            theoryAttendDel($(this).attr('data-id'));
        });

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
            var attendDate = null;
            var pageNum = null;
            var params = {
                attendDate: '',
                theoryReleaseId: page_param.paramTheoryReleaseId
            };
            if (typeof (Storage) !== "undefined") {
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
                pageNum = sessionStorage.getItem(webStorageKey.PAGE_NUM);
            }
            if (attendDate !== null) {
                params.attendDate = attendDate;
            } else {
                params.attendDate = $(param_id.attendDate).val();
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
            var attendDate = null;
            if (typeof (Storage) !== "undefined") {
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
            }
            if (attendDate !== null) {
                $(param_id.attendDate).val(attendDate);
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

        $('#configureRelease').click(function () {
            $.get(ajax_url.configure_data + '/' + page_param.paramTheoryReleaseId, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    configureData(data);
                    $('#configureReleaseModal').modal('show');
                }
            });
        });


        /**
         * 列表数据
         * @param data 数据
         */
        function configureData(data) {
            var template = Handlebars.compile($("#configure-data-template").html());

            Handlebars.registerHelper('week_day', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.weekDay(this.weekDay)));
            });
            $('#dataTable > tbody').html(template(data));
        }

        $('#dataTable').delegate('.release', "click", function () {
            sendConfigureReleaseAjax($(this).attr('data-id'));
        });

        /**
         * 配置发布ajax
         * @param theoryConfigureId
         */
        function sendConfigureReleaseAjax(theoryConfigureId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.configure_release,
                data: {theoryConfigureId: theoryConfigureId},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        init();
                        $('#configureReleaseModal').modal('hide');
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
         * 删除确认
         * @param id 考勤id
         */
        function theoryAttendDel(id) {
            Swal.fire({
                title: "确定删除理论考勤吗？",
                text: "理论考勤删除！",
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
         * @param theoryAttendId
         */
        function sendDelAjax(theoryAttendId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.del,
                data: {theoryAttendId: theoryAttendId},
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

        $('#myAttend').click(function () {
            $.address.value(ajax_url.my + '/' + page_param.paramTheoryReleaseId);
        });

        $('#attendSituation').click(function () {
            $.address.value(ajax_url.situation + '/' + page_param.paramTheoryReleaseId);
        });

    });