//# sourceURL=role_data.js
require(["jquery", "lodash_plugin", "sweetalert2", "handlebars", "nav.active", "responsive.bootstrap4", "jquery.address", "messenger"],
    function ($, DP, Swal, Handlebars, navActive) {

        /*
         参数
        */
        var param = {
            schoolName: '',
            collegeName: '',
            roleName: ''
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            SCHOOL_NAME: 'PLATFORM_ROLE_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: 'PLATFORM_ROLE_COLLEGE_NAME_SEARCH',
            ROLE_NAME: 'PLATFORM_ROLE_ROLE_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/platform/role/data',
                del: web_path + '/web/platform/role/delete',
                add: '/web/platform/role/add',
                edit: '/web/platform/role/edit',
                page: '/web/menu/platform/role'
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
                {"data": "roleName"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "roleEnName"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 4,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context =
                            {
                                func: [
                                    {
                                        "name": "编辑",
                                        "css": "edit",
                                        "type": "primary",
                                        "id": c.roleId,
                                        "role": c.roleName
                                    },
                                    {
                                        "name": "删除",
                                        "css": "del",
                                        "type": "danger",
                                        "id": c.roleId,
                                        "role": c.roleName
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
            "dom": "<'row'<'col-sm-2'l><'#global_button.col-sm-3'>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {

                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    role_del($(this).attr('data-id'), $(this).attr('data-role'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="school_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                schoolName: '#search_school',
                collegeName: '#search_college',
                roleName: '#search_role'
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
            param.schoolName = $(getParamId().schoolName).val();
            param.collegeName = $(getParamId().collegeName).val();
            param.roleName = $(getParamId().roleName).val();
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, DP.defaultUndefinedValue(param.schoolName, ''));
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, DP.defaultUndefinedValue(param.collegeName, ''));
                sessionStorage.setItem(webStorageKey.ROLE_NAME, param.roleName);
            }
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var schoolName = null;
            var collegeName = null;
            var roleName = null;
            if (typeof(Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                roleName = sessionStorage.getItem(webStorageKey.ROLE_NAME);
            }
            if (schoolName !== null) {
                param.schoolName = schoolName;
            }

            if (collegeName !== null) {
                param.collegeName = collegeName;
            }

            if (roleName !== null) {
                param.roleName = roleName;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            var collegeName = null;
            var roleName = null;
            if (typeof(Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                roleName = sessionStorage.getItem(webStorageKey.ROLE_NAME);
            }
            if (schoolName !== null) {
                $(getParamId().schoolName).val(schoolName);
            }

            if (collegeName !== null) {
                $(getParamId().collegeName).val(collegeName);
            }

            if (roleName !== null) {
                $(getParamId().roleName).val(roleName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
            $(getParamId().collegeName).val('');
            $(getParamId().roleName).val('');
        }

        $(getParamId().schoolName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().collegeName).keyup(function (event) {
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
        $('#school_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         编辑页面
         */
        function edit(roleId) {
            $.address.value(getAjaxUrl().edit + '/' + roleId);
        }

        /*
         删除
         */
        function role_del(roleId, roleName) {
            Swal.fire({
                title: "确定删除角色 '" + roleName + "' 吗？",
                text: roleName + "删除后不可恢复！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDelAjax(roleId);
                }
            });
        }

        /**
         * 删除ajax
         * @param roleId
         */
        function sendDelAjax(roleId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {roleId: roleId},
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