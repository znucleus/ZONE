//# sourceURL=system_api_log.js
require(["jquery", "nav.active", "responsive.bootstrap4", "select2-zh-CN"],
    function ($, navActive) {

        /*
        参数
        */
        var param = {
            username: '',
            channelId: '',
            url: '',
            remark: '',
            ipAddress: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            USERNAME: 'SYSTEM_API_LOG_USERNAME_SEARCH',
            CHANNEL_ID: 'SYSTEM_API_LOG_CHANNEL_ID_SEARCH',
            URL: 'SYSTEM_API_LOG_URL_SEARCH',
            REMARK: 'SYSTEM_API_LOG_REMARK_SEARCH',
            IP_ADDRESS: 'SYSTEM_API_LOG_IP_ADDRESS_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                logs: web_path + '/web/system/log/api/data',
                obtain_channel_data: web_path + "/users/data/channel",
                page: '/web/menu/system/log'
            };
        }

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[4, 'desc']],// 排序
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
                {"data": "channelName"},
                {"data": "url"},
                {"data": "remark"},
                {"data": "createTimeStr"},
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

        /*
         参数id
         */
        function getParamId() {
            return {
                username: '#search_username',
                channelId: '#search_channel_id',
                url: '#search_url',
                remark: '#search_remark',
                ipAddress: '#search_ip_address'
            };
        }

        /*
         得到参数
         */
        function getParam() {
            return param;
        }

        var init_configure = {
            init_channel: false
        };

        /*
         初始化参数
         */
        function initParam() {
            param.username = $(getParamId().username).val();
            param.channelId = $(getParamId().channelId).val();
            param.url = $(getParamId().url).val();
            param.remark = $(getParamId().remark).val();
            param.ipAddress = $(getParamId().ipAddress).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.USERNAME, param.username);
                sessionStorage.setItem(webStorageKey.CHANNEL_ID, param.channelId != null ? param.channelId : '');
                sessionStorage.setItem(webStorageKey.URL, param.url);
                sessionStorage.setItem(webStorageKey.REMARK, param.remark);
                sessionStorage.setItem(webStorageKey.IP_ADDRESS, param.ipAddress);
            }
        }

        init();

        function init() {
            initSearchChannel();
            initSelect2();
        }

        var channelSelect2 = null;

        /**
         * 初始化渠道数据
         */
        function initSearchChannel() {
            $.get(getAjaxUrl().obtain_channel_data, function (data) {
                $(getParamId().channelId).html('<option label="请选择渠道"></option>');
                channelSelect2 = $(getParamId().channelId).select2({data: data.results});

                if (!init_configure.init_channel) {
                    if (typeof (Storage) !== "undefined") {
                        var channelId = sessionStorage.getItem(webStorageKey.CHANNEL_ID);
                        channelSelect2.val(Number(channelId)).trigger("change");
                    }
                    init_configure.init_channel = true;
                }
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var username = null;
            var channelId = null;
            var url = null;
            var remark = null;
            var ipAddress = null;

            if (typeof (Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                channelId = sessionStorage.getItem(webStorageKey.CHANNEL_ID);
                url = sessionStorage.getItem(webStorageKey.URL);
                remark = sessionStorage.getItem(webStorageKey.REMARK);
                ipAddress = sessionStorage.getItem(webStorageKey.IP_ADDRESS);
            }
            if (username !== null) {
                param.username = username;
            }

            if (channelId !== null) {
                param.channelId = channelId;
            }

            if (url !== null) {
                param.url = url;
            }

            if (remark !== null) {
                param.remark = remark;
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
            var channelId = null;
            var url = null;
            var remark = null;
            var ipAddress = null;
            if (typeof (Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                channelId = sessionStorage.getItem(webStorageKey.CHANNEL_ID);
                url = sessionStorage.getItem(webStorageKey.URL);
                remark = sessionStorage.getItem(webStorageKey.REMARK);
                ipAddress = sessionStorage.getItem(webStorageKey.IP_ADDRESS);
            }
            if (username !== null) {
                $(getParamId().username).val(username);
            }

            if (channelId !== null) {
                $(getParamId().channelId).val(channelId);
            }

            if (url !== null) {
                $(getParamId().url).val(url);
            }

            if (remark !== null) {
                $(getParamId().remark).val(remark);
            }

            if (ipAddress !== null) {
                $(getParamId().ipAddress).val(ipAddress);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().username).val('');
            $(getParamId().url).val('');
            $(getParamId().remark).val('');
            $(getParamId().ipAddress).val('');

            channelSelect2.val('').trigger("change");
        }

        $(getParamId().username).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().url).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().remark).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().channelId).on('select2:select', function (e) {
            initParam();
            myTable.ajax.reload();
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