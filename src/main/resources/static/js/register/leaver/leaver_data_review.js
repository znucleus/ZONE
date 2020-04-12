//# sourceURL=leaver_data_review.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/register/leaver/data/list',
            del: web_path + '/web/register/leaver/data/list/delete',
            export_data_url: web_path + '/web/register/leaver/data/export',
            page: '/web/menu/register/leaver'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramLeaverRegisterReleaseId: $('#paramLeaverRegisterReleaseId').val()
        };

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 5,
            displayedPages: 3,
            orderColumnName: 'studentNumber',
            orderDir: 'asc',
            extraSearch: JSON.stringify({
                realName: '',
                studentNumber: '',
                leaverRegisterReleaseId: page_param.paramLeaverRegisterReleaseId
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'REGISTER_LEAVER_DATA_REVIEW_REAL_NAME_SEARCH' + page_param.paramLeaverRegisterReleaseId,
            STUDENT_NUMBER: 'REGISTER_LEAVER_DATA_REVIEW_STUDENT_NUMBER_SEARCH' + page_param.paramLeaverRegisterReleaseId
        };

        /*
         参数id
         */
        var param_id = {
            realName: '#search_real_name',
            studentNumber: '#search_student_number'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.realName).val('');
            $(param_id.studentNumber).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, $(param_id.realName).val());
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, $(param_id.studentNumber).val());
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

        $(param_id.realName).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.studentNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#review-template").html());
            $(tableData).html(template(data));
        }

        /*
         删除
         */
        $(tableData).delegate('.del', "click", function () {
            leaverDataDel($(this).attr('data-id'));
        });

        /**
         * 删除确认
         * @param id 发布id
         */
        function leaverDataDel(id) {
            Swal.fire({
                title: "确定删除登记吗？",
                text: "登记删除！",
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
         * @param leaverRegisterDataId
         */
        function sendDelAjax(leaverRegisterDataId) {
            $.ajax({
                type: 'POST',
                url: ajax_url.del,
                data: {
                    leaverRegisterReleaseId: page_param.paramLeaverRegisterReleaseId,
                    leaverRegisterDataId: leaverRegisterDataId
                },
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
            var realName = null;
            var studentNumber = null;
            var params = {
                realName: '',
                studentNumber: '',
                leaverRegisterReleaseId: page_param.paramLeaverRegisterReleaseId
            };
            if (typeof(Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
            }
            if (realName !== null) {
                params.realName = realName;
            } else {
                params.realName = $(param_id.realName).val();
            }

            if (studentNumber !== null) {
                params.studentNumber = studentNumber;
            } else {
                params.studentNumber = $(param_id.studentNumber).val();
            }
            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            if (typeof(Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
            }
            if (realName !== null) {
                $(param_id.realName).val(realName);
            }

            if (studentNumber !== null) {
                $(param_id.studentNumber).val(studentNumber);
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

        $('#export_xls').click(function () {
            initSearchContent();
            var searchParam = param.extraSearch;
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xls'
            };
            window.location.href = encodeURI(ajax_url.export_data_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        $('#export_xlsx').click(function () {
            initSearchContent();
            var searchParam = param.extraSearch;
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xlsx'
            };
            window.location.href = encodeURI(ajax_url.export_data_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

    });