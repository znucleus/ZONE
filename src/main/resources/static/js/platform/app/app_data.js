//# sourceURL=app_data.js
require(["jquery", "lodash_plugin", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "jquery.address", "messenger"],
    function ($, DP, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            username: '',
            appName: '',
            realName: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            USERNAME: 'PLATFORM_APP_USERNAME_SEARCH',
            APP_NAME: 'PLATFORM_APP_APP_NAME_SEARCH',
            REAL_NAME: 'PLATFORM_APP_REAL_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/platform/app/paging',
                del: web_path + '/web/platform/app/delete',
                remark: web_path + '/web/platform/app/remark',
                add: '/web/platform/app/add',
                edit: '/web/platform/app/edit',
                page: '/web/menu/platform/app'
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
                localStorage.setItem('PLATFORM_APP_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('PLATFORM_APP_' + settings.sInstance))
            },
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
                {"data": "appName"},
                {"data": "username"},
                {"data": "realName"},
                {"data": "clientId"},
                {"data": "secret"},
                {"data": "oauthType"},
                {"data": "webServerRedirectUri"},
                {"data": "remark"},
                {"data": "createDateStr"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 5,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(c.oauthType === 0){
                            v = '授权码模式';
                        } else {
                            v = '密码模式';
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
                                    "id": c.clientId,
                                    "app": c.appName,
                                    "remark": ''
                                },
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.clientId,
                                    "app": c.appName,
                                    "remark": ''
                                },
                                {
                                    "name": "备注",
                                    "css": "remark",
                                    "type": "info",
                                    "id": c.clientId,
                                    "app": c.appName,
                                    "remark": c.remark
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
                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    app_del($(this).attr('data-id'), $(this).attr('data-app'));
                });

                tableElement.delegate('.remark', "click", function () {
                    remark($(this).attr('data-id'), $(this).attr('data-remark'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="app_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                username: '#search_username',
                appName: '#search_app_name',
                realName: '#search_real_name'
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
            param.username = $(getParamId().username).val();
            param.appName = $(getParamId().appName).val();
            param.realName = $(getParamId().realName).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.USERNAME, DP.defaultUndefinedValue(param.username, ''));
                sessionStorage.setItem(webStorageKey.REAL_NAME, DP.defaultUndefinedValue(param.realName, ''));
                sessionStorage.setItem(webStorageKey.APP_NAME, param.appName);

            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var username = null;
            var appName = null;
            var realName = null;
            if (typeof (Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                appName = sessionStorage.getItem(webStorageKey.APP_NAME);
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
            }
            if (username !== null) {
                param.username = username;
            }

            if (appName !== null) {
                param.appName = appName;
            }

            if (realName !== null) {
                param.realName = realName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var username = null;
            var appName = null;
            var realName = null;
            if (typeof (Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                appName = sessionStorage.getItem(webStorageKey.APP_NAME);
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
            }
            if (username !== null) {
                $(getParamId().username).val(username);
            }

            if (appName !== null) {
                $(getParamId().appName).val(appName);
            }

            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().username).val('');
            $(getParamId().appName).val('');
            $(getParamId().realName).val('');
        }

        $(getParamId().username).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().appName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().realName).keyup(function (event) {
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
         添加页面
         */
        $('#app_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         编辑页面
         */
        function edit(clientId) {
            $.address.value(getAjaxUrl().edit + '/' + clientId);
        }

        /*
         删除
         */
        function app_del(clientId, appName) {
            Swal.fire({
                title: "确定删除应用 '" + appName + "' 吗？",
                text: "应用删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(clientId);
                }
            });
        }

        function del(clientId) {
            sendDelAjax(clientId, 1);
        }

        /**
         * 删除ajax
         * @param clientId
         */
        function sendDelAjax(clientId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {clientId: clientId},
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

        function remark(clientId, remark) {
            $('#editClientId').val(clientId);
            $('#editRemark').val(remark);
            $('#editModal').modal('show');
        }

        $('#edit').click(function () {
            sendRemarkAjax();
        });

        function sendRemarkAjax() {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().remark,
                data: $('#edit_form').serialize(),
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        $('#editModal').modal('hide');
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