//# sourceURL=school_data.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            schoolName: ''
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_SCHOOL_SCHOOL_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/schools/paging',
                status: web_path + '/web/data/schools/update-status',
                add: '/web/data/school/add',
                edit: '/web/data/school/edit',
                page: '/web/menu/data/school'
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
            stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
            stateSaveCallback: function (settings, data) {
                localStorage.setItem('DATA_SCHOOL_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('DATA_SCHOOL_' + settings.sInstance))
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
                {"data": "schoolId"},
                {"data": "schoolName"},
                {"data": "schoolIsDel"},
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
                        return '<input type="checkbox" value="' + c.schoolId + '" name="check"/>';
                    }
                },
                {
                    targets: 5,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.schoolId,
                                    "school": c.schoolName
                                }
                            ]
                        };

                        if (c.schoolIsDel === 0 || c.schoolIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.schoolId,
                                "school": c.schoolName
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.schoolId,
                                "school": c.schoolName
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 4,
                    render: function (a, b, c, d) {
                        if (c.schoolIsDel === 0 || c.schoolIsDel == null) {
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
                    "sPrevious": "<",
                    "sNext": ">",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-4 col-md-12'><'col-lg-6 col-md-12'<'#mytoolbox'>>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    school_del($(this).attr('data-id'), $(this).attr('data-school'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    school_recovery($(this).attr('data-id'), $(this).attr('data-school'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var html = '<div class="row ">' +
            '<div class="col-md-9 pd-t-2"><input type="text" id="search_school" class="form-control form-control-sm" placeholder="学校" /></div>' +
            '<div class="col-md-3 pd-t-2 text-right "><div class="btn-group" role="group"><button type="button" id="search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-search"></i>搜索</button>' +
            '  <button type="button" id="reset_search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-repeat"></i>重置</button></div></div>' +
            '</div>';
        $('#mytoolbox').append(html);

        var global_button = '<button type="button" id="school_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="school_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="school_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                schoolName: '#search_school'
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
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, param.schoolName);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var schoolName = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
            }
            if (schoolName !== null) {
                param.schoolName = schoolName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
            }
            if (schoolName !== null) {
                $(getParamId().schoolName).val(schoolName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
        }

        $(getParamId().schoolName).keyup(function (event) {
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
         批量注销
         */
        $('#school_dels').click(function () {
            var schoolIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                schoolIds.push($(ids[i]).val());
            }

            if (schoolIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的学校吗？",
                    text: "学校注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(schoolIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的学校!");
            }
        });

        /*
         批量恢复
         */
        $('#school_recoveries').click(function () {
            var schoolIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                schoolIds.push($(ids[i]).val());
            }

            if (schoolIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的学校吗？",
                    text: "学校恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(schoolIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的学校!");
            }
        });

        /*
         编辑页面
         */
        function edit(schoolId) {
            $.address.value(getAjaxUrl().edit + '/' + schoolId);
        }

        /*
         注销
         */
        function school_del(schoolId, schoolName) {
            Swal.fire({
                title: "确定注销学校 '" + schoolName + "' 吗？",
                text: "学校注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(schoolId);
                }
            });
        }

        /*
         恢复
         */
        function school_recovery(schoolId, schoolName) {
            Swal.fire({
                title: "确定恢复学校 '" + schoolName + "' 吗？",
                text: "学校恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(schoolId);
                }
            });
        }

        function del(schoolId) {
            sendStatusAjax(schoolId, 1);
        }

        function recovery(schoolId) {
            sendStatusAjax(schoolId, 0);
        }

        function dels(schoolIds) {
            sendStatusAjax(schoolIds.join(","), 1);
        }

        function recoveries(schoolIds) {
            sendStatusAjax(schoolIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param schoolId
         * @param isDel
         */
        function sendStatusAjax(schoolId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {schoolIds: schoolId, isDel: isDel},
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