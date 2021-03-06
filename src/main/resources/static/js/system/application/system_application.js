//# sourceURL=system_application.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            applicationName: '',
            applicationEnName: '',
            applicationCode: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            APPLICATION_NAME: 'SYSTEM_APPLICATION_APPLICATION_NAME_SEARCH',
            APPLICATION_EN_NAME: 'SYSTEM_APPLICATION_APPLICATION_EN_NAME_SEARCH',
            APPLICATION_CODE: 'SYSTEM_APPLICATION_APPLICATION_CODE_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/system/application/paging',
                status: web_path + '/web/system/application/delete',
                add: '/web/system/application/add',
                edit: '/web/system/application/edit',
                page: '/web/menu/system/application'
            };
        }

        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        // datatables 初始化
        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            drawCallback: function (oSettings) {
                $('#checkall').prop('checked', false);
                // 调用全选插件
                $.fn.check({checkall_name: "checkall", checkbox_name: "check"});
            },
            searching: false,
            stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
            stateSaveCallback: function (settings, data) {
                localStorage.setItem('SYSTEM_APPLICATION_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('SYSTEM_APPLICATION_' + settings.sInstance))
            },
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
                {"data": "applicationName"},
                {"data": "applicationEnName"},
                {"data": "applicationPidName"},
                {"data": "applicationUrl"},
                {"data": "icon"},
                {"data": "applicationSort"},
                {"data": "applicationCode"},
                {"data": "applicationDataUrlStartWith"},
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
                        return '<input type="checkbox" value="' + c.applicationId + '" name="check"/>';
                    }
                },
                {
                    targets: 10,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context =
                            {
                                func: [
                                    {
                                        "name": "编辑",
                                        "css": "edit",
                                        "type": "primary",
                                        "id": c.applicationId,
                                        "application": c.applicationName
                                    },
                                    {
                                        "name": "删除",
                                        "css": "del",
                                        "type": "danger",
                                        "id": c.applicationId,
                                        "application": c.applicationName
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

                tableElement.delegate('.del', "click", function () {
                    application_del($(this).attr('data-id'), $(this).attr('data-application'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var html = '<div class="row ">' +
            '<div class="col-md-3 pd-t-2"><input type="text" id="search_application" class="form-control form-control-sm" placeholder="应用" /></div>' +
            '<div class="col-md-3 pd-t-2"><input type="text" id="search_en_application" class="form-control form-control-sm" placeholder="英文" /></div>' +
            '<div class="col-md-3 pd-t-2"><input type="text" id="search_application_code" class="form-control form-control-sm" placeholder="识别码" /></div>' +
            '<div class="col-md-3 pd-t-2 text-right "><div class="btn-group" role="group"><button type="button" id="search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-search"></i>搜索</button>' +
            ' <button type="button" id="reset_search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-repeat"></i>重置</button></div></div>' +
            '</div>';

        $('#mytoolbox').append(html);

        var global_button = '<div class="btn-group pd-t-2" role="group"><button type="button" id="application_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="application_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button></div>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                applicationName: '#search_application',
                applicationEnName: '#search_en_application',
                applicationCode: '#search_application_code'
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
            param.applicationName = $(getParamId().applicationName).val();
            param.applicationEnName = $(getParamId().applicationEnName).val();
            param.applicationCode = $(getParamId().applicationCode).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.APPLICATION_NAME, param.applicationName);
                sessionStorage.setItem(webStorageKey.APPLICATION_EN_NAME, param.applicationEnName);
                sessionStorage.setItem(webStorageKey.APPLICATION_CODE, param.applicationCode);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var applicationName = null;
            var applicationEnName = null;
            var applicationCode = null;
            if (typeof (Storage) !== "undefined") {
                applicationName = sessionStorage.getItem(webStorageKey.APPLICATION_NAME);
                applicationEnName = sessionStorage.getItem(webStorageKey.APPLICATION_EN_NAME);
                applicationCode = sessionStorage.getItem(webStorageKey.APPLICATION_CODE);
            }
            if (applicationName !== null) {
                param.applicationName = applicationName;
            }

            if (applicationEnName !== null) {
                param.applicationEnName = applicationEnName;
            }

            if (applicationCode !== null) {
                param.applicationCode = applicationCode;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var applicationName = null;
            var applicationEnName = null;
            var applicationCode = null;
            if (typeof (Storage) !== "undefined") {
                applicationName = sessionStorage.getItem(webStorageKey.APPLICATION_NAME);
                applicationEnName = sessionStorage.getItem(webStorageKey.APPLICATION_EN_NAME);
                applicationCode = sessionStorage.getItem(webStorageKey.APPLICATION_CODE);
            }
            if (applicationName !== null) {
                $(getParamId().applicationName).val(applicationName);
            }

            if (applicationEnName !== null) {
                $(getParamId().applicationEnName).val(applicationEnName);
            }

            if (applicationCode !== null) {
                $(getParamId().applicationCode).val(applicationCode);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().applicationName).val('');
            $(getParamId().applicationEnName).val('');
            $(getParamId().applicationCode).val('');
        }

        $(getParamId().applicationName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().applicationEnName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().applicationCode).keyup(function (event) {
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

        /*
         添加页面
         */
        $('#application_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#application_dels').click(function () {
            var applicationIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                applicationIds.push($(ids[i]).val());
            }

            if (applicationIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的应用吗？",
                    text: "删除后不可恢复！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(applicationIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的应用!");
            }
        });

        /*
         编辑页面
         */
        function edit(applicationId) {
            $.address.value(getAjaxUrl().edit + '/' + applicationId);
        }

        /*
         删除
         */
        function application_del(applicationId, applicationName) {
            Swal.fire({
                title: "确定删除应用 '" + applicationName + "' 吗？",
                text: applicationName + "删除后不可恢复！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(applicationId);
                }
            });
        }

        function del(applicationId) {
            sendStatusAjax(applicationId);
        }

        function dels(applicationIds) {
            sendStatusAjax(applicationIds.join(","));
        }

        /**
         * 注销或恢复ajax
         * @param applicationId
         */
        function sendStatusAjax(applicationId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {applicationIds: applicationId},
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