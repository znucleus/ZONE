//# sourceURL=system_notify.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            notifyTitle: '',
            notifyContent: '',
            realName: '',
            time: ''
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            NOTIFY_TITLE: 'SYSTEM_NOTIFY_NOTIFY_TITLE_SEARCH',
            NOTIFY_CONTENT: 'SYSTEM_NOTIFY_NOTIFY_CONTENT_SEARCH',
            REAL_NAME: 'SYSTEM_NOTIFY_REAL_NAME_SEARCH',
            TIME: 'SYSTEM_NOTIFY_TIME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/system/notify/data',
                del: web_path + '/web/system/notify/delete',
                add: '/web/system/notify/add',
                edit: '/web/system/notify/edit',
                page: '/web/menu/system/notify'
            };
        }

        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            drawCallback: function (oSettings) {
                $('#checkall').prop('checked', false);
                // 调用全选插件
                $.fn.check({checkall_name: "checkall", checkbox_name: "check"});
            },
            searching: false,
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[8, 'asc']],// 排序
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
                {"data": null},
                {"data": null},
                {"data": "notifyTitle"},
                {"data": "notifyContent"},
                {"data": "notifyType"},
                {"data": "validDateStr"},
                {"data": "expireDateStr"},
                {"data": "realName"},
                {"data": "createDateStr"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 0,
                    orderable: false,
                    render: function (a, b, c, d) {
                        return '';
                    }
                },
                {
                    targets: 1,
                    orderable: false,
                    render: function (a, b, c, d) {
                        return '<input type="checkbox" value="' + c.systemNotifyId + '" name="check"/>';
                    }
                },
                {
                    targets: 9,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.systemNotifyId
                                },
                                {
                                    "name": "注销",
                                    "css": "del",
                                    "type": "danger",
                                    "id": c.systemNotifyId
                                }
                            ]
                        };

                        return template(context);
                    }
                },
                {
                    targets: 4,
                    render: function (a, b, c, d) {
                        var v = '';
                        if (c.notifyType === 'success') {
                            v = '成功';
                        } else if (c.notifyType === 'info') {
                            v = '消息';
                        } else if (c.notifyType === 'warning') {
                            v = '警告';
                        } else if (c.notifyType === 'danger') {
                            v = '危险';
                        }
                        return v;
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
                    notify_del($(this).attr('data-id'), $(this).attr('data-school'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="notify_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="notify_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                notifyTitle: '#search_notify_title',
                notifyContent: '#search_notify_content',
                realName: '#search_real_name',
                time: '#search_time'
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
            param.notifyTitle = $(getParamId().notifyTitle).val();
            param.notifyContent = $(getParamId().notifyContent).val();
            param.realName = $(getParamId().realName).val();
            param.time = $(getParamId().time).val();
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.NOTIFY_TITLE, param.notifyTitle);
                sessionStorage.setItem(webStorageKey.NOTIFY_CONTENT, param.notifyContent);
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.TIME, param.time);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var notifyTitle = null;
            var notifyContent = null;
            var realName = null;
            var time = null;
            if (typeof(Storage) !== "undefined") {
                notifyTitle = sessionStorage.getItem(webStorageKey.NOTIFY_TITLE);
                notifyContent = sessionStorage.getItem(webStorageKey.NOTIFY_CONTENT);
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                time = sessionStorage.getItem(webStorageKey.TIME);
            }
            if (notifyTitle !== null) {
                param.notifyTitle = notifyTitle;
            }

            if (notifyContent !== null) {
                param.notifyContent = notifyContent;
            }

            if (realName !== null) {
                param.realName = realName;
            }

            if (time !== null) {
                param.time = time;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var notifyTitle = null;
            var notifyContent = null;
            var realName = null;
            var time = null;
            if (typeof(Storage) !== "undefined") {
                notifyTitle = sessionStorage.getItem(webStorageKey.NOTIFY_TITLE);
                notifyContent = sessionStorage.getItem(webStorageKey.NOTIFY_CONTENT);
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                time = sessionStorage.getItem(webStorageKey.TIME);
            }
            if (notifyTitle !== null) {
                $(getParamId().notifyTitle).val(notifyTitle);
            }

            if (notifyContent !== null) {
                $(getParamId().notifyContent).val(notifyContent);
            }


            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (time !== null) {
                $(getParamId().time).val(time);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().notifyTitle).val('');
            $(getParamId().notifyContent).val('');
            $(getParamId().realName).val('');
            $(getParamId().time).val('');
        }

        $(getParamId().notifyTitle).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().notifyContent).keyup(function (event) {
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

        $(getParamId().time).change(function () {
            initParam();
            myTable.ajax.reload();
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
        $('#notify_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量删除
         */
        $('#notify_dels').click(function () {
            var systemNotifyIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                systemNotifyIds.push($(ids[i]).val());
            }

            if (systemNotifyIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的通知吗？",
                    text: "通知删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(systemNotifyIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的通知!");
            }
        });

        /*
         编辑页面
         */
        function edit(systemNotifyId) {
            $.address.value(getAjaxUrl().edit + '/' + systemNotifyId);
        }

        /*
         删除
         */
        function notify_del(systemNotifyId) {
            Swal.fire({
                title: "确定删除该通知吗？",
                text: "通知删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(systemNotifyId);
                }
            });
        }

        function del(systemNotifyId) {
            sendDelAjax(systemNotifyId);
        }

        function dels(systemNotifyId) {
            sendDelAjax(systemNotifyId.join(","));
        }

        /**
         * 删除ajax
         * @param systemNotifyId
         */
        function sendDelAjax(systemNotifyId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {systemNotifyIds: systemNotifyId},
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