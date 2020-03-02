//# sourceURL=epidemic_data_review.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "jquery.address", "messenger", "select2-zh-CN", "flatpickr-zh"],
    function ($, Handlebars, navActive, Swal) {

        var page_param = {
            paramEpidemicRegisterReleaseId: $('#paramEpidemicRegisterReleaseId').val()
        };

        /*
        参数
        */
        var param = {
            registerRealName: '',
            registerUsername: '',
            registerType: '',
            channelId: '',
            epidemicStatus: '',
            address: '',
            institute: '',
            registerDate: '',
            epidemicRegisterReleaseId: page_param.paramEpidemicRegisterReleaseId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REGISTER_REAL_NAME: 'REGISTER_EPIDEMIC_DATA_REGISTER_REAL_NAME_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            REGISTER_USERNAME: 'REGISTER_EPIDEMIC_DATA_REGISTER_USERNAME_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            REGISTER_TYPE: 'REGISTER_EPIDEMIC_DATA_REGISTER_TYPE_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            CHANNEL_ID: 'REGISTER_EPIDEMIC_DATA_CHANNEL_ID_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            EPIDEMIC_STATUS: 'REGISTER_EPIDEMIC_DATA_EPIDEMIC_STATUS_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            ADDRESS: 'REGISTER_EPIDEMIC_DATA_ADDRESS_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            INSTITUTE: 'REGISTER_EPIDEMIC_DATA_INSTITUTE_SEARCH_' + page_param.paramEpidemicRegisterReleaseId,
            REGISTER_DATE: 'REGISTER_EPIDEMIC_DATA_REGISTER_DATE_SEARCH_' + page_param.paramEpidemicRegisterReleaseId
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/register/epidemic/data/list',
                obtain_channel_data: web_path + "/users/data/channel",
                export_data_url: web_path + '/web/register/epidemic/data/export',
                del: web_path + '/web/register/epidemic/data/delete',
                page: '/web/menu/register/epidemic'
            };
        }

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[8, 'desc']],// 排序
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
                {"data": "registerRealName"},
                {"data": "registerUsername"},
                {"data": "registerType"},
                {"data": "epidemicStatus"},
                {"data": "address"},
                {"data": "institute"},
                {"data": "channelName"},
                {"data": "remark"},
                {"data": "registerDateStr"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 3,
                    render: function (a, b, c, d) {
                        var v = c.epidemicStatus;
                        if (v === '无下列情况，身体健康') {
                            v = "<span class='text-info'>" + v + "</span>";
                        } else if (v === '近15日内到过湖北' || v === '近15日内接触过新型肺炎感染者') {
                            v = "<span class='text-warning'>" + v + "</span>";
                        } else {
                            v = "<span class='text-danger'>" + v + "</span>";
                        }
                        return v;
                    }
                },
                {
                    targets: 9,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "删除",
                                    "css": "del",
                                    "type": "danger",
                                    "id": c.epidemicRegisterDataId
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
                tableElement.delegate('.del', "click", function () {
                    data_del($(this).attr('data-id'));
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
                registerRealName: '#search_register_real_name',
                registerUsername: '#search_register_username',
                registerType: '#search_register_type',
                channelId: '#search_channel_id',
                epidemicStatus: '#search_epidemic_status',
                address: '#search_address',
                institute: '#search_institute',
                registerDate: '#search_register_date'
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
            param.registerRealName = $(getParamId().registerRealName).val();
            param.registerUsername = $(getParamId().registerUsername).val();
            param.registerType = $(getParamId().registerType).val();
            param.channelId = $(getParamId().channelId).val();
            param.epidemicStatus = $(getParamId().epidemicStatus).val();
            param.address = $(getParamId().address).val();
            param.institute = $(getParamId().institute).val();
            param.registerDate = $(getParamId().registerDate).val();
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REGISTER_REAL_NAME, param.registerRealName);
                sessionStorage.setItem(webStorageKey.REGISTER_USERNAME, param.registerUsername);
                sessionStorage.setItem(webStorageKey.REGISTER_TYPE, param.registerType);
                sessionStorage.setItem(webStorageKey.CHANNEL_ID, param.channelId != null ? param.channelId : '');
                sessionStorage.setItem(webStorageKey.EPIDEMIC_STATUS, param.epidemicStatus);
                sessionStorage.setItem(webStorageKey.ADDRESS, param.address);
                sessionStorage.setItem(webStorageKey.INSTITUTE, param.institute);
                sessionStorage.setItem(webStorageKey.REGISTER_DATE, param.registerDate);
            }
        }

        $(getParamId().registerDate).flatpickr({
            "locale": "zh",
            "mode": "range",
            onClose: function () {
                initParam();
                myTable.ajax.reload();
            }
        });

        init();

        function init() {
            initSearchChannel();
            initSelect2();
        }

        var channelSelect2 = null;

        /**
         * 初始化班级数据
         */
        function initSearchChannel() {
            $.get(getAjaxUrl().obtain_channel_data, function (data) {
                $(getParamId().channelId).html('<option label="请选择渠道"></option>');
                channelSelect2 = $(getParamId().channelId).select2({data: data.results});

                if (!init_configure.init_channel) {
                    if (typeof(Storage) !== "undefined") {
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
            var registerRealName = null;
            var registerUsername = null;
            var registerType = null;
            var channelId = null;
            var epidemicStatus = null;
            var address = null;
            var institute = null;
            var registerDate = null;
            if (typeof(Storage) !== "undefined") {
                registerRealName = sessionStorage.getItem(webStorageKey.REGISTER_REAL_NAME);
                registerUsername = sessionStorage.getItem(webStorageKey.REGISTER_USERNAME);
                registerType = sessionStorage.getItem(webStorageKey.REGISTER_TYPE);
                channelId = sessionStorage.getItem(webStorageKey.CHANNEL_ID);
                epidemicStatus = sessionStorage.getItem(webStorageKey.EPIDEMIC_STATUS);
                address = sessionStorage.getItem(webStorageKey.ADDRESS);
                institute = sessionStorage.getItem(webStorageKey.INSTITUTE);
                registerDate = sessionStorage.getItem(webStorageKey.REGISTER_DATE);
            }
            if (registerRealName !== null) {
                param.registerRealName = registerRealName;
            }

            if (registerUsername !== null) {
                param.registerUsername = registerUsername;
            }

            if (registerType !== null) {
                param.registerType = registerType;
            }

            if (channelId !== null) {
                param.channelId = channelId;
            }

            if (epidemicStatus !== null) {
                param.epidemicStatus = epidemicStatus;
            }

            if (address !== null) {
                param.address = address;
            }

            if (institute !== null) {
                param.institute = institute;
            }

            if (registerDate !== null) {
                param.registerDate = registerDate;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var registerRealName = null;
            var registerUsername = null;
            var registerType = null;
            var channelId = null;
            var epidemicStatus = null;
            var address = null;
            var institute = null;
            var registerDate = null;
            if (typeof(Storage) !== "undefined") {
                registerRealName = sessionStorage.getItem(webStorageKey.REGISTER_REAL_NAME);
                registerUsername = sessionStorage.getItem(webStorageKey.REGISTER_USERNAME);
                registerType = sessionStorage.getItem(webStorageKey.REGISTER_TYPE);
                channelId = sessionStorage.getItem(webStorageKey.CHANNEL_ID);
                epidemicStatus = sessionStorage.getItem(webStorageKey.EPIDEMIC_STATUS);
                address = sessionStorage.getItem(webStorageKey.ADDRESS);
                institute = sessionStorage.getItem(webStorageKey.INSTITUTE);
                registerDate = sessionStorage.getItem(webStorageKey.REGISTER_DATE);
            }
            if (registerRealName !== null) {
                $(getParamId().registerRealName).val(registerRealName);
            }

            if (registerUsername !== null) {
                $(getParamId().registerUsername).val(registerUsername);
            }

            if (registerType !== null) {
                $(getParamId().registerType).val(registerType);
            }

            if (channelId !== null) {
                $(getParamId().channelId).val(channelId);
            }

            if (epidemicStatus !== null) {
                $(getParamId().epidemicStatus).val(epidemicStatus);
            }

            if (address !== null) {
                $(getParamId().address).val(address);
            }

            if (institute !== null) {
                $(getParamId().institute).val(institute);
            }

            if (registerDate !== null) {
                $(getParamId().registerDate).val(registerDate);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().registerRealName).val('');
            $(getParamId().registerUsername).val('');
            $(getParamId().registerType).val('');
            $(getParamId().epidemicStatus).val('');
            $(getParamId().address).val('');
            $(getParamId().institute).val('');
            $(getParamId().registerDate).val('');

            channelSelect2.val('').trigger("change");
        }

        $(getParamId().registerRealName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().registerUsername).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().registerType).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().channelId).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().channelId).on('select2:select', function (e) {
            initParam();
            myTable.ajax.reload();
        });

        $(getParamId().epidemicStatus).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().address).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().institute).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().registerDate).keyup(function (event) {
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

        $('#export_xls').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xls'
            };
            window.location.href = encodeURI(getAjaxUrl().export_data_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        $('#export_xlsx').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xlsx'
            };
            window.location.href = encodeURI(getAjaxUrl().export_data_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        /*
       删除
       */
        function data_del(epidemicRegisterDataId) {
            Swal.fire({
                title: "确定删除登记吗？",
                text: "登记删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelAjax(epidemicRegisterDataId);
                }
            });
        }

        /**
         * 删除ajax
         * @param epidemicRegisterDataId
         */
        function sendDelAjax(epidemicRegisterDataId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {epidemicRegisterDataId: epidemicRegisterDataId},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        myTable.ajax.reload();
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
    });