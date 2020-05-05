//# sourceURL=system_role.js
require(["jquery", "handlebars", "nav.active", "responsive.bootstrap4", "jquery.address", "messenger"],
    function ($, Handlebars, navActive) {
        /*
         参数
         */
        var param = {
            roleName: ''
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            ROLE_NAME: 'SYSTEM_ROLE_ROLE_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                roles: web_path + '/web/system/role/data',
                edit: '/web/system/role/edit',
                page: '/web/menu/system/role'
            };
        }

        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        // datatables 初始化
        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "ajax": {
                "url": getAjaxUrl().roles,
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
                {"data": "roleEnName"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 2,
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
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-2 col-md-12'><'col-lg-8 col-md-12'<'#mytoolbox'>>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var html = '<div class="row ">' +
            '<div class="col-md-9 pd-t-2"><input type="text" id="search_role" class="form-control form-control-sm" placeholder="角色" /></div>' +
            '<div class="col-md-3 pd-t-2 text-right "><div class="btn-group" role="group"><button type="button" id="search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-search"></i>搜索</button>' +
            ' <button type="button" id="reset_search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-repeat"></i>重置</button></div></div>' +
            '</div>';

        $('#mytoolbox').append(html);

        var global_button = '<button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
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
            param.roleName = $(getParamId().roleName).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.ROLE_NAME, param.roleName);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var roleName = null;
            if (typeof (Storage) !== "undefined") {
                roleName = sessionStorage.getItem(webStorageKey.ROLE_NAME);
            }
            if (roleName !== null) {
                param.roleName = roleName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var roleName = null;
            if (typeof (Storage) !== "undefined") {
                roleName = sessionStorage.getItem(webStorageKey.ROLE_NAME);
            }
            if (roleName !== null) {
                $(getParamId().roleName).val(roleName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().roleName).val('');
        }

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
         编辑页面
         */
        function edit(roleId) {
            $.address.value(getAjaxUrl().edit + '/' + roleId);
        }
    });