//# sourceURL=internship_apply_my.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address", "jquery.simple-pagination", "jquery-labelauty"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/internship/apply/data',
            edit: '/web/internship/apply/edit',
            look: '/web/internship/apply/look',
            recall_apply: web_path + '/web/internship/apply/recall',
            page: '/web/menu/internship/apply'
        };

        navActive(ajax_url.page);

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 2,
            displayedPages: 3,
            orderColumnName: 'applyTime',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                internshipTitle: ''
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            INTERNSHIP_TITLE: 'INTERNSHIP_APPLY_MY_INTERNSHIP_TITLE_SEARCH'
        };

        /*
         参数id
         */
        var param_id = {
            internshipTitle: '#search_internship_title'
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

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#internship-apply-template").html());
            Handlebars.registerHelper('internship_apply_state', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(internshipApplyStateCode(this.internshipApplyState)));
            });
            $(tableData).html(template(data));
        }

        /**
         * 状态码表
         * @param state 状态码
         * @returns {string}
         */
        function internshipApplyStateCode(state) {
            var msg = '';
            switch (state) {
                case 0:
                    msg = '已保存';
                    break;
                case 1:
                    msg = '审核中';
                    break;
                case 2:
                    msg = '已通过';
                    break;
                case 3:
                    msg = '未通过';
                    break;
                case 4:
                    msg = '基本信息变更审核中';
                    break;
                case 5:
                    msg = '基本信息变更填写中';
                    break;
                case 6:
                    msg = '单位信息变更申请中';
                    break;
                case 7:
                    msg = '单位信息变更填写中...';
                    break;
            }
            return msg;
        }

        /*
         进入申请
         */
        $(tableData).delegate('.myApply', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        /*
        查看申请
        */
        $(tableData).delegate('.myApplyLook', "click", function () {
            $.address.value(ajax_url.look + '/' + $(this).attr('data-id'));
        });

        /*
       撤消申请
       */
        $(tableData).delegate('.recallApply', "click", function () {
            var id = $(this).attr('data-id');
            recall(id);
        });

        /**
         * 撤消询问
         * @param id
         */
        function recall(id) {
            Swal.fire({
                title: "确定撤消申请吗？",
                text: "申请撤消！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#ddc144',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendRecallAjax(id);
                }
            });
        }

        /**
         * 撤消ajax
         * @param id
         */
        function sendRecallAjax(id) {
            $.post(ajax_url.recall_apply, {id: id}, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    init();
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
            var internshipTitle = null;
            var params = {
                internshipTitle: ''
            };
            if (typeof(Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
            }
            if (internshipTitle !== null) {
                params.internshipTitle = internshipTitle;
            } else {
                params.internshipTitle = $(param_id.internshipTitle).val();
            }
            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var internshipTitle = null;
            if (typeof(Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
            }
            if (internshipTitle !== null) {
                $(param_id.internshipTitle).val(internshipTitle);
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