//# sourceURL=college_data.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        /*
        参数
        */
        var param = {
            schoolName: '',
            collegeName: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_COLLEGE_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: "DATA_COLLEGE_COLLEGE_NAME_SEARCH"
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/college/data',
                status: web_path + '/web/data/college/status',
                add: '/web/data/college/add',
                edit: '/web/data/college/edit',
                mount: '/web/data/college/mount',
                page: '/web/menu/data/college'
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
                {"data": "collegeId"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "collegeCode"},
                {"data": "collegeAddress"},
                {"data": "collegeIsDel"},
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
                        return '<input type="checkbox" value="' + c.collegeId + '" name="check"/>';
                    }
                },
                {
                    targets: 8,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.collegeId,
                                    "college": c.collegeName
                                },
                                {
                                    "name": "挂载",
                                    "css": "mount",
                                    "type": "indigo",
                                    "id": c.collegeId,
                                    "college": c.collegeName
                                }
                            ]
                        };

                        if (c.collegeIsDel === 0 || c.collegeIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.collegeId,
                                "college": c.collegeName
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.collegeId,
                                "college": c.collegeName
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 7,
                    render: function (a, b, c, d) {
                        if (c.collegeIsDel === 0 || c.collegeIsDel == null) {
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
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-10 col-md-12'>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    college_del($(this).attr('data-id'), $(this).attr('data-college'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    college_recovery($(this).attr('data-id'), $(this).attr('data-college'));
                });

                tableElement.delegate('.mount', "click", function () {
                    college_mount($(this).attr('data-id'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="college_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="college_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="college_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                schoolName: '#search_school',
                collegeName: '#search_college'
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

            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, param.schoolName);
            }

            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, param.collegeName);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var schoolName = null;
            var collegeName = null;
            if (typeof(Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
            }
            if (schoolName !== null) {
                param.schoolName = schoolName;
            }

            if (collegeName !== null) {
                param.collegeName = collegeName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            var collegeName = null;
            if (typeof(Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
            }
            if (schoolName !== null) {
                $(getParamId().schoolName).val(schoolName);
            }

            if (collegeName !== null) {
                $(getParamId().collegeName).val(collegeName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
            $(getParamId().collegeName).val('');
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
        $('#college_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#college_dels').click(function () {
            var collegeIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                collegeIds.push($(ids[i]).val());
            }

            if (collegeIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的院吗？",
                    text: "院注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(collegeIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的院!");
            }
        });

        /*
         批量恢复
         */
        $('#college_recoveries').click(function () {
            var collegeIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                collegeIds.push($(ids[i]).val());
            }

            if (collegeIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的院吗？",
                    text: "院恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(collegeIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的院!");
            }
        });

        /*
         编辑页面
         */
        function edit(collegeId) {
            $.address.value(getAjaxUrl().edit + '/' + collegeId);
        }

        /*
         注销
         */
        function college_del(collegeId, collegeName) {
            Swal.fire({
                title: "确定注销院 '" + collegeName + "' 吗？",
                text: "院注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(collegeId);
                }
            });
        }

        /*
         恢复
         */
        function college_recovery(collegeId, collegeName) {
            Swal.fire({
                title: "确定恢复院 '" + collegeName + "' 吗？",
                text: "院恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(collegeId);
                }
            });
        }

        /**
         * 院挂载应用
         * @param collegeId 院id
         */
        function college_mount(collegeId) {
            $.address.value(getAjaxUrl().mount + '/' + collegeId);
        }

        function del(collegeId) {
            sendStatusAjax(collegeId, 1);
        }

        function recovery(collegeId) {
            sendStatusAjax(collegeId, 0);
        }

        function dels(collegeIds) {
            sendStatusAjax(collegeIds.join(","), 1);
        }

        function recoveries(collegeIds) {
            sendStatusAjax(collegeIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param collegeId
         * @param isDel
         */
        function sendStatusAjax(collegeId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {collegeIds: collegeId, isDel: isDel},
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