//# sourceURL=training_attend_my.js
require(["jquery", "tools", "handlebars", "nav.active", "tablesaw", "jquery.address", "flatpickr-zh"],
    function ($, tools, Handlebars, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/training/attend/my/paging',
                page: '/web/menu/training/attend'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val()
        };

        /*
        参数
        */
        var param = {
            orderColumnName: 'attendDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                attendDate: '',
                operate: '',
                trainingReleaseId: page_param.paramTrainingReleaseId
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            ATTEND_DATE: 'TRAINING_ATTEND_MY_ATTEND_DATE_SEARCH' + page_param.paramTrainingReleaseId,
            OPERATE: 'TRAINING_ATTEND_MY_OPERATE_SEARCH' + page_param.paramTrainingReleaseId
        };

        /*
        参数id
        */
        var param_id = {
            attendDate: '#search_attend_date',
            operate: '#search_operate'
        };

        var tableElement = $('#dataTable');

        /*
        清空参数
        */
        function cleanParam() {
            $(param_id.attendDate).val('');
            $(param_id.operate).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.ATTEND_DATE, $(param_id.attendDate).val());
                sessionStorage.setItem(webStorageKey.OPERATE, $(param_id.operate).val());
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

        $(param_id.operate).change(function () {
            refreshSearch();
            init();
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#data_template").html());

            Handlebars.registerHelper('operate', function () {
                return new Handlebars.SafeString(operate(this.operate));
            });
            $('#dataTable > tbody').html(template(data));
            $('#totalSize').text(data.listResult.length);
            tableElement.tablesaw().data("tablesaw").refresh();
        }

        function operate(operate) {
            var v = '不存在';
            if (operate === 0) {
                v = '<span class="text-danger">缺席';
            } else if (operate === 1) {
                v = '<span class="text-info">请假</span>';
            } else if (operate === 2) {
                v = '<span class="text-warning">迟到</span>';
            } else if (operate === 3) {
                v = '<span class="text-success">正常</span>';
            }
            return v;
        }

        init();
        initSearchInput();

        function init() {
            initSearchContent();
            tools.dataLoading();
            $.get(getAjaxUrl().data, param, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

        /*
       初始化搜索内容
      */
        function initSearchContent() {
            var attendDate = null;
            var operate = null;
            var params = {
                attendDate: '',
                operate: '',
                trainingReleaseId: page_param.paramTrainingReleaseId
            };
            if (typeof (Storage) !== "undefined") {
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
                operate = sessionStorage.getItem(webStorageKey.OPERATE);
            }
            if (attendDate !== null) {
                params.attendDate = attendDate;
            } else {
                params.attendDate = $(param_id.attendDate).val();
            }

            if (operate !== null) {
                params.operate = operate;
            } else {
                params.operate = $(param_id.operate).val();
            }
            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var attendDate = null;
            var operate = null;
            if (typeof (Storage) !== "undefined") {
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
                operate = sessionStorage.getItem(webStorageKey.OPERATE);
            }
            if (attendDate !== null) {
                $(param_id.attendDate).val(attendDate);
            }

            if (operate !== null) {
                $(param_id.operate).val(operate);
            }

        }
    });