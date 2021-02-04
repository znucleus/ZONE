//# sourceURL=system_operator_log.js
require(["jquery", "nav.active", "responsive.bootstrap4"],
    function ($, navActive) {

        /*
        参数
        */
        var param = {
            username: '',
            behavior: '',
            ipAddress: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            USERNAME: 'SYSTEM_OPERATOR_LOG_USERNAME_SEARCH',
            BEHAVIOR: 'SYSTEM_OPERATOR_LOG_BEHAVIOR_SEARCH',
            IP_ADDRESS: 'SYSTEM_OPERATOR_LOG_IP_ADDRESS_SEARCH'
        };

        function getAjaxUrl() {
            return {
                logs: web_path + '/web/system/log/operator/paging',
                page: '/web/menu/system/log'
            }
        }

        navActive(getAjaxUrl().page);

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
            stateSaveCallback: function (settings, data) {
                localStorage.setItem('SYSTEM_OPERATOR_LOG_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('SYSTEM_OPERATOR_LOG_' + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[2, 'desc']],// 排序
            "ajax": {
                "url": getAjaxUrl().logs,
                "dataSrc": "data",
                "data": function (d) {
                    // 添加额外的参数传给服务器
                    initSearchContent();
                    var searchParam = getParam();
                    d.extra_search = JSON.stringify(searchParam);
                }
            },
            "columns": [
                {"data": "username"},
                {"data": "behavior"},
                {"data": "operatingTimeNew"},
                {"data": "ipAddress"}
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
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        function getParamId() {
            return {
                username: '#search_user',
                behavior: '#search_behavior',
                ipAddress: '#search_ip'
            };
        }

        /*
         得到参数
         */
        function getParam() {
            return param;
        }

        function initParam() {
            param.username = $(getParamId().username).val();
            param.behavior = $(getParamId().behavior).val();
            param.ipAddress = $(getParamId().ipAddress).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.USERNAME, param.username);
                sessionStorage.setItem(webStorageKey.BEHAVIOR, param.behavior);
                sessionStorage.setItem(webStorageKey.IP_ADDRESS, param.ipAddress);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var username = null;
            var behavior = null;
            var ipAddress = null;
            if (typeof (Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                behavior = sessionStorage.getItem(webStorageKey.BEHAVIOR);
                ipAddress = sessionStorage.getItem(webStorageKey.IP_ADDRESS);
            }
            if (username !== null) {
                param.username = username;
            }

            if (behavior !== null) {
                param.behavior = behavior;
            }

            if (ipAddress !== null) {
                param.ipAddress = ipAddress;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var username = null;
            var behavior = null;
            var ipAddress = null;
            if (typeof (Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                behavior = sessionStorage.getItem(webStorageKey.BEHAVIOR);
                ipAddress = sessionStorage.getItem(webStorageKey.IP_ADDRESS);
            }
            if (username !== null) {
                $(getParamId().username).val(username);
            }

            if (behavior !== null) {
                $(getParamId().behavior).val(behavior);
            }

            if (ipAddress !== null) {
                $(getParamId().ipAddress).val(ipAddress);
            }
        }

        function cleanParam() {
            $(getParamId().username).val('');
            $(getParamId().behavior).val('');
            $(getParamId().ipAddress).val('');
        }

        $(getParamId().username).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().behavior).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().ipAddress).keyup(function (event) {
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

    });