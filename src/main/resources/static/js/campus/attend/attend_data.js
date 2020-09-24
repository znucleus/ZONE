//# sourceURL=attend_data.js
require(["jquery", "handlebars", "nav.active", "responsive.bootstrap4", "flatpickr-zh"],
    function ($, Handlebars, navActive) {

        /*
         参数
        */
        var param = {
            title: '',
            organizeName: '',
            releaseTime: '',
            dataRange: 2
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'CAMPUS_ATTEND_TITLE_SEARCH',
            ORGANIZE_NAME: 'CAMPUS_ATTEND_ORGANIZE_NAME_SEARCH',
            RELEASE_TIME: 'CAMPUS_ATTEND_RELEASE_TIME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/campus/attend/data',
                details: '/web/campus/attend/details',
                page: '/web/menu/campus/attend'
            };
        }

        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
            stateSaveCallback: function (settings, data) {
                localStorage.setItem('CAMPUS_ATTEND_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('CAMPUS_ATTEND_' + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[9, 'desc']],// 排序
            "ajax": {
                "url": getAjaxUrl().data,
                "dataSrc": "data",
                "data": function (d) {
                    // 添加额外的参数传给服务器
                    initSearchContent();
                    var searchParam = getParam();
                    d.extra_search = JSON.stringify(searchParam);
                }
            },
            "columns": [
                {"data": "title"},
                {"data": "attendStartTimeStr"},
                {"data": "attendEndTimeStr"},
                {"data": "isAuto"},
                {"data": "validDateStr"},
                {"data": "expireDateStr"},
                {"data": "organizeName"},
                {"data": "totalUsers"},
                {"data": "totalAttend"},
                {"data": "releaseTimeStr"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 0,
                    orderable: false
                },
                {
                    targets: 1,
                    orderable: false
                },
                {
                    targets: 2,
                    orderable: false
                },
                {
                    targets: 3,
                    orderable: false,
                    render: function (a, b, c, d) {
                        var v = '否';
                        if (c.isAuto === 1) {
                            v = '是';
                        }
                        return v;
                    }
                },
                {
                    targets: 4,
                    orderable: false
                },
                {
                    targets: 5,
                    orderable: false
                },
                {
                    targets: 6,
                    orderable: false
                },
                {
                    targets: 7,
                    orderable: false
                },
                {
                    targets: 8,
                    orderable: false
                },
                {
                    targets: 10,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "详情",
                                    "css": "details",
                                    "type": "primary",
                                    "id": c.attendReleaseSubId
                                }
                            ]
                        };
                        return template(context);
                    }
                }

            ],
            "language": {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "<",
                    "sNext": ">",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-10 col-md-12'>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.details', "click", function () {
                    details($(this).attr('data-id'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                title: '#search_title',
                organizeName: '#search_organize',
                releaseTime: '#search_release_time'
            };
        }

        /*
         得到参数
         */
        function getParam() {
            return param;
        }

        /*
         初始化参数
         */
        function initParam() {
            param.title = $(getParamId().title).val();
            param.organizeName = $(getParamId().organizeName).val();
            param.releaseTime = $(getParamId().releaseTime).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.TITLE, param.title);
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, param.organizeName);
                sessionStorage.setItem(webStorageKey.RELEASE_TIME, param.releaseTime);
            }
        }

        $(getParamId().releaseTime).flatpickr({
            "locale": "zh",
            "mode": "range",
            onClose: function () {
                initParam();
                myTable.ajax.reload();
            }
        });

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var title = null;
            var organizeName = null;
            var releaseTime = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                releaseTime = sessionStorage.getItem(webStorageKey.RELEASE_TIME);
            }
            if (title !== null) {
                param.title = title;
            }

            if (organizeName !== null) {
                param.organizeName = organizeName;
            }

            if (releaseTime !== null) {
                param.releaseTime = releaseTime;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var title = null;
            var organizeName = null;
            var releaseTime = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                releaseTime = sessionStorage.getItem(webStorageKey.RELEASE_TIME);
            }
            if (title !== null) {
                $(getParamId().title).val(title);
            }

            if (organizeName !== null) {
                $(getParamId().organizeName).val(organizeName);
            }

            if (releaseTime !== null) {
                $(getParamId().releaseTime).val(releaseTime);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().title).val('');
            $(getParamId().organizeName).val('');
            $(getParamId().releaseTime).val('');
        }

        $(getParamId().title).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().organizeName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $('#search').click(function () {
            initParam();
            myTable.ajax.reload();
        });

        $('#reset_search').click(function () {
            cleanParam();
            initParam();
            myTable.ajax.reload();
        });

        $('#refresh').click(function () {
            myTable.ajax.reload();
        });

        /*
        编辑页面
        */
        function details(attendReleaseSubId) {
            $.address.value(getAjaxUrl().details + '/' + attendReleaseSubId);
        }
    });