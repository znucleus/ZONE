//# sourceURL=authorize_data.js
require(["jquery", "sweetalert2", "handlebars", "nav.active", "workbook", "responsive.bootstrap4", "jquery.address", "messenger"],
    function ($, Swal, Handlebars, navActive, workbook) {

        /*
         参数
        */
        var param = {
            username: '',
            realName: '',
            organizeName: '',
            roleName: '',
            dataRange: ''
        };

        var page_param = {
            authorities: $('#authorities').val(),
            username: $('#username').val()
        };

        var button_id = {
            all: {
                id: '#all',
                text: '全部',
                tip: '查询中...'
            },
            person: {
                id: '#person',
                text: '本人',
                tip: '查询中...'
            },
            audit: {
                id: '#audit',
                text: '待审核',
                tip: '查询中...'
            }
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            USERNAME: 'PLATFORM_AUTHORIZE_USERNAME_SEARCH',
            REAL_NAME: 'PLATFORM_AUTHORIZE_REAL_NAME_SEARCH',
            ORGANIZE_NAME: 'PLATFORM_AUTHORIZE_ORGANIZE_NAME_SEARCH',
            ROLE_NAME: 'PLATFORM_AUTHORIZE_ROLE_NAME_SEARCH',
            DATA_RANGE: 'PLATFORM_AUTHORIZE_DATA_RANGE_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/platform/authorize/data',
                del: web_path + '/web/platform/authorize/delete',
                add: '/web/platform/authorize/add',
                edit: '/web/platform/authorize/edit',
                page: '/web/menu/platform/authorize'
            };
        }

        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
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
                {"data": "realName"},
                {"data": "username"},
                {"data": "authorizeTypeName"},
                {"data": "organizeName"},
                {"data": "roleName"},
                {"data": "duration"},
                {"data": "validDateStr"},
                {"data": "expireDateStr"},
                {"data": "applyStatus"},
                {"data": "createDateStr"},
                {"data": "reason"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 8,
                    render: function (a, b, c, d) {
                        var v = '';
                        if (c.applyStatus === 0) {
                            v = '待审核';
                        } else if (c.applyStatus === 1) {
                            v = '通过';
                        } else if (c.applyStatus === 2) {
                            v = '失败';
                        }
                        return v;
                    }
                },
                {
                    targets: 11,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context =
                            {
                                func: []
                            };

                        if (page_param.authorities === workbook.authorities.ROLE_SYSTEM ||
                            page_param.authorities === workbook.authorities.ROLE_ADMIN) {
                            context.func.push({
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.roleUsersId
                                },
                                {
                                    "name": "删除",
                                    "css": "del",
                                    "type": "danger",
                                    "id": c.roleUsersId
                                });

                            if (c.applyStatus === 0) {
                                context.func.push({
                                    "name": "通过",
                                    "css": "pass",
                                    "type": "success",
                                    "id": c.roleUsersId
                                }, {
                                    "name": "拒绝",
                                    "css": "refuse",
                                    "type": "warning",
                                    "id": c.roleUsersId
                                });
                            } else if (c.applyStatus === 1) {
                                context.func.push({
                                    "name": "拒绝",
                                    "css": "refuse",
                                    "type": "warning",
                                    "id": c.roleUsersId
                                });
                            } else if (c.applyStatus === 2) {
                                context.func.push({
                                    "name": "通过",
                                    "css": "pass",
                                    "type": "success",
                                    "id": c.roleUsersId
                                });
                            }
                        } else {
                            if (c.username === page_param.username) {
                                context.func.push({
                                        "name": "编辑",
                                        "css": "edit",
                                        "type": "primary",
                                        "id": c.roleUsersId
                                    },
                                    {
                                        "name": "删除",
                                        "css": "del",
                                        "type": "danger",
                                        "id": c.roleUsersId
                                    });
                            }
                        }
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
            "dom": "<'row'<'col-sm-2'l><'#global_button.col-sm-3'>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {

                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    authorize_del($(this).attr('data-id'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="authorize_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>申请</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                username: '#search_username',
                realName: '#search_real_name',
                organizeName: '#search_organize_name',
                roleName: '#search_role_name',
                dataRange: ''
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
            param.realName = $(getParamId().realName).val();
            param.organizeName = $(getParamId().organizeName).val();
            param.roleName = $(getParamId().roleName).val();


            if ($(button_id.all.id).hasClass('active')) {
                param.dataRange = '';
            } else if ($(button_id.person.id).hasClass('active')) {
                param.dataRange = 1;
            } else if ($(button_id.audit.id).hasClass('active')) {
                param.dataRange = 2;
            }

            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.USERNAME, param.username);
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, param.organizeName);
                sessionStorage.setItem(webStorageKey.ROLE_NAME, param.roleName);
                sessionStorage.setItem(webStorageKey.DATA_RANGE, param.dataRange);
            }
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var username = null;
            var realName = null;
            var organizeName = null;
            var roleName = null;
            var dataRange = null;
            if (typeof(Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                roleName = sessionStorage.getItem(webStorageKey.ROLE_NAME);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
            }
            if (username !== null) {
                param.username = username;
            }

            if (realName !== null) {
                param.realName = realName;
            }

            if (organizeName !== null) {
                param.organizeName = organizeName;
            }

            if (roleName !== null) {
                param.roleName = roleName;
            }

            if (dataRange !== null) {
                param.dataRange = dataRange;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var username = null;
            var realName = null;
            var organizeName = null;
            var roleName = null;
            var dataRange = null;
            if (typeof(Storage) !== "undefined") {
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                roleName = sessionStorage.getItem(webStorageKey.ROLE_NAME);
                dataRange = sessionStorage.getItem(webStorageKey.DATA_RANGE);
            }
            if (username !== null) {
                $(getParamId().username).val(username);
            }

            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (organizeName !== null) {
                $(getParamId().organizeName).val(organizeName);
            }

            if (roleName !== null) {
                $(getParamId().roleName).val(roleName);
            }

            if (dataRange !== null) {
                $(button_id.all.id).removeClass('active');
                $(button_id.person.id).removeClass('active');
                $(button_id.audit.id).removeClass('active');
                if (Number(dataRange) === 1) {
                    $(button_id.person.id).addClass('active');
                } else if (Number(dataRange) === 2) {
                    $(button_id.audit.id).addClass('active');
                } else {
                    $(button_id.all.id).addClass('active');
                }
            } else {
                $(button_id.all.id).addClass('active');
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().username).val('');
            $(getParamId().realName).val('');
            $(getParamId().organizeName).val('');
            $(getParamId().roleName).val('');
        }

        $(getParamId().username).keyup(function (event) {
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

        $(getParamId().organizeName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().roleName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        // 查询全部
        $(button_id.all.id).click(function () {
            $(button_id.all.id).addClass('active');
            $(button_id.person.id).removeClass('active');
            $(button_id.audit.id).removeClass('active');
            initParam();
            myTable.ajax.reload();
        });

        // 查询本人
        $(button_id.person.id).click(function () {
            $(button_id.all.id).removeClass('active');
            $(button_id.person.id).addClass('active');
            $(button_id.audit.id).removeClass('active');
            initParam();
            myTable.ajax.reload();
        });

        // 查询待审核
        $(button_id.audit.id).click(function () {
            $(button_id.all.id).removeClass('active');
            $(button_id.person.id).removeClass('active');
            $(button_id.audit.id).addClass('active');
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
        $('#authorize_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         编辑页面
         */
        function edit(roleUsersId) {
            $.address.value(getAjaxUrl().edit + '/' + roleUsersId);
        }

        /*
         删除
         */
        function authorize_del(roleUsersId) {
            Swal.fire({
                title: "确定删除申请吗？",
                text: "删除后不可恢复！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(roleUsersId);
                }
            });
        }

        function del(roleUsersId) {
            sendDelAjax(roleUsersId);
        }

        /**
         * 删除ajax
         * @param roleUsersId
         */
        function sendDelAjax(roleUsersId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {roleUsersId: roleUsersId},
                success: function (data) {
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