//# sourceURL=attend_details.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/campus/attend/details/data',
                page: '/web/menu/campus/attend'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            attendReleaseId: $('#attendReleaseId').val(),
            attendReleaseSubId: $('#attendReleaseSubId').val(),
            initData: []
        };

        var param_id = {
            realName: '#search_real_name',
            studentNumber: '#search_student_number'
        };

        var button_id = {
            noAttend: {
                id: '#noAttend',
                text: '未签到',
                tip: '查询中...'
            },
            hasAttend: {
                id: '#hasAttend',
                text: '已签到',
                tip: '查询中...'
            },
            allAttend: {
                id: '#allAttend',
                text: '全部',
                tip: '查询中...'
            }
        };

        /*
       web storage key.
       */
        var webStorageKey = {
            REAL_NAME: 'CAMPUS_ATTEND_DETAILS_REAL_NAME_SEARCH_' + page_param.attendReleaseSubId,
            STUDENT_NUMBER: 'CAMPUS_ATTEND_DETAILS_STUDENT_NUMBER_SEARCH_' + page_param.attendReleaseSubId,
            TYPE: 'CAMPUS_ATTEND_DETAILS_TYPE_SEARCH_' + page_param.attendReleaseSubId
        };

        /*
        参数
        */
        var param = {
            attendReleaseId: page_param.attendReleaseId,
            attendReleaseSubId: page_param.attendReleaseSubId,
            type: 2
        };

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
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, $(param_id.realName).val());
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, $(param_id.studentNumber).val());
                sessionStorage.setItem(webStorageKey.TYPE, param.type);
            }
        }

        /*
         搜索
         */
        $('#search').click(function () {
            refreshSearch();
            initSearch();
        });

        /*
         重置
         */
        $('#reset_search').click(function () {
            cleanParam();
            refreshSearch();
            initSearch();
        });

        $(param_id.realName).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                initSearch();
            }
        });

        $(param_id.studentNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                initSearch();
            }
        });

        $(button_id.noAttend.id).click(function () {
            $(button_id.noAttend.id).addClass('active');
            $(button_id.hasAttend.id).removeClass('active');
            $(button_id.allAttend.id).removeClass('active');
            param.type = 2;
            refreshSearch();
            init();
        });

        $(button_id.hasAttend.id).click(function () {
            $(button_id.noAttend.id).removeClass('active');
            $(button_id.hasAttend.id).addClass('active');
            $(button_id.allAttend.id).removeClass('active');
            param.type = 1;
            refreshSearch();
            init();
        });

        $(button_id.allAttend.id).click(function () {
            $(button_id.noAttend.id).removeClass('active');
            $(button_id.hasAttend.id).removeClass('active');
            $(button_id.allAttend.id).addClass('active');
            param.type = 3;
            refreshSearch();
            init();
        });

        init();
        initSearchInput();

        function init() {
            initData();
        }

        function initData() {
            initSearchContent();
            $.get(getAjaxUrl().data, param, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                page_param.initData = data;
                if (data.state) {
                    initSearch();
                }
            });
        }

        function initSearch() {
            var data = page_param.initData;
            if (data.state) {
                // 过滤
                var realName = $(param_id.realName).val();
                var studentNumber = $(param_id.studentNumber).val();
                if (realName !== '' || studentNumber !== '') {
                    var listResult = data.listResult;
                    var newListResult = [];
                    var newObj = [];
                    for (var i = 0; i < listResult.length; i++) {
                        if ((listResult[i].realName.indexOf(realName) >= 0 && realName !== '') ||
                            (listResult[i].studentNumber.indexOf(studentNumber) >= 0 && studentNumber !== '')) {
                            newListResult.push(listResult[i]);
                        }
                    }
                    newObj.listResult = newListResult;
                    listData(newObj);
                } else {
                    listData(data);
                }
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            var type = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                type = sessionStorage.getItem(webStorageKey.TYPE);
            }
            if (realName !== null) {
                $(param_id.realName).val(realName);
            }

            if (studentNumber !== null) {
                $(param_id.studentNumber).val(studentNumber);
            }

            if (type !== null) {
                $(button_id.noAttend.id).removeClass('active');
                $(button_id.hasAttend.id).removeClass('active');
                $(button_id.allAttend.id).removeClass('active');
                if (Number(type) === 1) {
                    $(button_id.hasAttend.id).addClass('active');
                } else if (Number(type) === 2) {
                    $(button_id.noAttend.id).addClass('active');
                } else if (Number(type) === 3) {
                    $(button_id.allAttend.id).addClass('active');
                } else {
                    $(button_id.noAttend.id).addClass('active');
                }
            } else {
                $(button_id.noAttend.id).addClass('active');
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var type = null;
            if (typeof (Storage) !== "undefined") {
                type = sessionStorage.getItem(webStorageKey.TYPE);
            }
            if (type !== null) {
                param.type = type;
            }
        }

        var tableElement = $('#dataTable');

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#details-template").html());
            Handlebars.registerHelper("addOne", function (index) {
                //返回+1之后的结果
                return index + 1;
            });
            $('#dataTable > tbody').html(template(data));
            $('#totalSize').text(data.listResult ? data.listResult.length : 0);
            tableElement.tablesaw().data("tablesaw").refresh();
        }

        $('#refresh').click(function () {
            init();
        });
    });