//# sourceURL=schoolroom_data.js
require(["jquery", "lodash_plugin", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, DP, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            schoolName: '',
            collegeName: '',
            buildingName: '',
            buildingCode: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_SCHOOLROOM_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: 'DATA_SCHOOLROOM_COLLEGE_NAME_SEARCH',
            BUILDING_NAME: 'DATA_SCHOOLROOM_BUILDING_NAME_SEARCH',
            BUILDING_CODE: 'DATA_SCHOOLROOM_BUILDING_CODE_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/schoolroom/data',
                status: web_path + '/web/data/schoolroom/status',
                add: '/web/data/schoolroom/add',
                edit: '/web/data/schoolroom/edit',
                page: '/web/menu/data/schoolroom'
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
                {"data": "schoolroomId"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "buildingName"},
                {"data": "buildingCode"},
                {"data": "schoolroomIsDel"},
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
                        return '<input type="checkbox" value="' + c.schoolroomId + '" name="check"/>';
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
                                    "id": c.schoolroomId,
                                    "schoolroom": c.buildingName + c.buildingCode
                                }
                            ]
                        };

                        if (c.schoolroomIsDel === 0 || c.schoolroomIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.schoolroomId,
                                "schoolroom": c.buildingName + c.buildingCode
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.schoolroomId,
                                "schoolroom": c.buildingName + c.buildingCode
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 7,
                    render: function (a, b, c, d) {
                        if (c.schoolroomIsDel === 0 || c.schoolroomIsDel == null) {
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
                    schoolroom_del($(this).attr('data-id'), $(this).attr('data-schoolroom'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    schoolroom_recovery($(this).attr('data-id'), $(this).attr('data-schoolroom'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="schoolroom_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="schoolroom_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="schoolroom_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                schoolName: '#search_school',
                collegeName: '#search_college',
                buildingName: '#search_building',
                buildingCode: '#search_schoolroom'
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
            param.buildingName = $(getParamId().buildingName).val();
            param.buildingCode = $(getParamId().buildingCode).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, DP.defaultUndefinedValue(param.schoolName, ''));
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, DP.defaultUndefinedValue(param.collegeName, ''));
                sessionStorage.setItem(webStorageKey.BUILDING_NAME, param.buildingName);
                sessionStorage.setItem(webStorageKey.BUILDING_CODE, param.buildingCode);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var schoolName = null;
            var collegeName = null;
            var buildingName = null;
            var buildingCode = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                buildingName = sessionStorage.getItem(webStorageKey.BUILDING_NAME);
                buildingCode = sessionStorage.getItem(webStorageKey.BUILDING_CODE);
            }
            if (schoolName !== null) {
                param.schoolName = schoolName;
            }

            if (collegeName !== null) {
                param.collegeName = collegeName;
            }

            if (buildingName !== null) {
                param.buildingName = buildingName;
            }

            if (buildingCode !== null) {
                param.buildingCode = buildingCode;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            var collegeName = null;
            var buildingName = null;
            var buildingCode = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                buildingName = sessionStorage.getItem(webStorageKey.BUILDING_NAME);
                buildingCode = sessionStorage.getItem(webStorageKey.BUILDING_CODE);
            }
            if (schoolName !== null) {
                $(getParamId().schoolName).val(schoolName);
            }

            if (collegeName !== null) {
                $(getParamId().collegeName).val(collegeName);
            }

            if (buildingName !== null) {
                $(getParamId().buildingName).val(buildingName);
            }

            if (buildingCode !== null) {
                $(getParamId().buildingCode).val(buildingCode);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
            $(getParamId().collegeName).val('');
            $(getParamId().buildingName).val('');
            $(getParamId().buildingCode).val('');
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

        $(getParamId().buildingName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().buildingCode).keyup(function (event) {
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
        $('#schoolroom_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#schoolroom_dels').click(function () {
            var schoolroomIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                schoolroomIds.push($(ids[i]).val());
            }

            if (schoolroomIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的教室吗？",
                    text: "教室注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(schoolroomIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的教室!");
            }
        });

        /*
         批量恢复
         */
        $('#schoolroom_recoveries').click(function () {
            var schoolroomIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                schoolroomIds.push($(ids[i]).val());
            }

            if (schoolroomIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的教室吗？",
                    text: "教室恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(schoolroomIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的教室!");
            }
        });

        /*
         编辑页面
         */
        function edit(schoolroomId) {
            $.address.value(getAjaxUrl().edit + '/' + schoolroomId);
        }

        /*
         注销
         */
        function schoolroom_del(schoolroomId, schoolroomName) {
            Swal.fire({
                title: "确定注销教室 '" + schoolroomName + "' 吗？",
                text: "教室注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(schoolroomId);
                }
            });
        }

        /*
         恢复
         */
        function schoolroom_recovery(schoolroomId, schoolroomName) {
            Swal.fire({
                title: "确定恢复教室 '" + schoolroomName + "' 吗？",
                text: "教室恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(schoolroomId);
                }
            });
        }

        function del(schoolroomId) {
            sendStatusAjax(schoolroomId, 1);
        }

        function recovery(schoolroomId) {
            sendStatusAjax(schoolroomId, 0);
        }

        function dels(schoolroomIds) {
            sendStatusAjax(schoolroomIds.join(","), 1);
        }

        function recoveries(schoolroomIds) {
            sendStatusAjax(schoolroomIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param schoolroomId
         * @param isDel
         */
        function sendStatusAjax(schoolroomId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {schoolroomIds: schoolroomId, isDel: isDel},
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