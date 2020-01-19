//# sourceURL=department_data.js
require(["jquery", "lodash_plugin", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, DP, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            schoolName: '',
            collegeName: '',
            departmentName: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_DEPARTMENT_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: 'DATA_DEPARTMENT_COLLEGE_NAME_SEARCH',
            DEPARTMENT_NAME: 'DATA_DEPARTMENT_DEPARTMENT_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/department/data',
                status: web_path + '/web/data/department/status',
                add: '/web/data/department/add',
                edit: '/web/data/department/edit',
                page: '/web/menu/data/department'
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
            "aaSorting": [[2, 'asc']],// 排序
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
                {"data": "departmentId"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "departmentName"},
                {"data": "departmentIsDel"},
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
                        return '<input type="checkbox" value="' + c.departmentId + '" name="check"/>';
                    }
                },
                {
                    targets: 7,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.departmentId,
                                    "department": c.departmentName
                                }
                            ]
                        };

                        if (c.departmentIsDel === 0 || c.departmentIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.departmentId,
                                "department": c.departmentName
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.departmentId,
                                "department": c.departmentName
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 6,
                    render: function (a, b, c, d) {
                        if (c.departmentIsDel === 0 || c.departmentIsDel == null) {
                            return "<span class='text-info'>正常</span>";
                        } else {
                            return "<span class='text-danger'>已注销</span>";
                        }
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
                    "sPrevious": "上页",
                    "sNext": "下页",
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
                    department_del($(this).attr('data-id'), $(this).attr('data-department'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    department_recovery($(this).attr('data-id'), $(this).attr('data-department'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="department_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="department_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="department_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                schoolName: '#search_school',
                collegeName: '#search_college',
                departmentName: '#search_department'
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
            param.departmentName = $(getParamId().departmentName).val();

            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, DP.defaultUndefinedValue(param.schoolName, ''));
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, DP.defaultUndefinedValue(param.collegeName, ''));
                sessionStorage.setItem(webStorageKey.DEPARTMENT_NAME, param.departmentName);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var schoolName = null;
            var collegeName = null;
            var departmentName = null;
            if (typeof(Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                departmentName = sessionStorage.getItem(webStorageKey.DEPARTMENT_NAME);
            }
            if (schoolName !== null) {
                param.schoolName = schoolName;
            }

            if (collegeName !== null) {
                param.collegeName = collegeName;
            }

            if (departmentName !== null) {
                param.departmentName = departmentName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            var collegeName = null;
            var departmentName = null;
            if (typeof(Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                departmentName = sessionStorage.getItem(webStorageKey.DEPARTMENT_NAME);
            }
            if (schoolName !== null) {
                $(getParamId().schoolName).val(schoolName);
            }

            if (collegeName !== null) {
                $(getParamId().collegeName).val(collegeName);
            }

            if (departmentName !== null) {
                $(getParamId().departmentName).val(departmentName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
            $(getParamId().collegeName).val('');
            $(getParamId().departmentName).val('');
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

        $(getParamId().departmentName).keyup(function (event) {
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
        $('#department_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#department_dels').click(function () {
            var departmentIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                departmentIds.push($(ids[i]).val());
            }

            if (departmentIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的系吗？",
                    text: "系注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(departmentIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的系!");
            }
        });

        /*
         批量恢复
         */
        $('#department_recoveries').click(function () {
            var departmentIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                departmentIds.push($(ids[i]).val());
            }

            if (departmentIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的系吗？",
                    text: "系恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(departmentIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的系!");
            }
        });

        /*
         编辑页面
         */
        function edit(departmentId) {
            $.address.value(getAjaxUrl().edit + '/' + departmentId);
        }

        /*
         注销
         */
        function department_del(departmentId, departmentName) {
            Swal.fire({
                title: "确定注销系 '" + departmentName + "' 吗？",
                text: "系注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(departmentId);
                }
            });
        }

        /*
         恢复
         */
        function department_recovery(departmentId, departmentName) {
            Swal.fire({
                title: "确定恢复系 '" + departmentName + "' 吗？",
                text: "系恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(departmentId);
                }
            });
        }

        function del(departmentId) {
            sendStatusAjax(departmentId, 1);
        }

        function recovery(departmentId) {
            sendStatusAjax(departmentId, 0);
        }

        function dels(departmentIds) {
            sendStatusAjax(departmentIds.join(","), 1);
        }

        function recoveries(departmentIds) {
            sendStatusAjax(departmentIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param departmentId
         * @param isDel
         */
        function sendStatusAjax(departmentId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {departmentIds: departmentId, isDel: isDel},
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