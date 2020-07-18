//# sourceURL=building_data.js
require(["jquery", "lodash_plugin", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, DP, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            schoolName: '',
            collegeName: '',
            buildingName: '',
            coordinate:''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_BUILDING_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: 'DATA_BUILDING_COLLEGE_NAME_SEARCH',
            BUILDING_NAME: 'DATA_BUILDING_BUILDING_NAME_SEARCH',
            COORDINATE: 'DATA_BUILDING_COORDINATE_SEARCH',
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/building/data',
                status: web_path + '/web/data/building/status',
                add: '/web/data/building/add',
                edit: '/web/data/building/edit',
                page: '/web/menu/data/building'
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
                {"data": "buildingId"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "buildingClassifyName"},
                {"data": "buildingName"},
                {"data": "coordinate"},
                {"data": "buildingIsDel"},
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
                        return '<input type="checkbox" value="' + c.buildingId + '" name="check"/>';
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
                                    "id": c.buildingId,
                                    "building": c.buildingName
                                }
                            ]
                        };

                        if (c.buildingIsDel === 0 || c.buildingIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.buildingId,
                                "building": c.buildingName
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.buildingId,
                                "building": c.buildingName
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 8,
                    render: function (a, b, c, d) {
                        if (c.buildingIsDel === 0 || c.buildingIsDel == null) {
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
                    building_del($(this).attr('data-id'), $(this).attr('data-building'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    building_recovery($(this).attr('data-id'), $(this).attr('data-building'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="building_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="building_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="building_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
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
                coordinate:'#search_coordinate'
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
            param.coordinate = $(getParamId().coordinate).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, DP.defaultUndefinedValue(param.schoolName, ''));
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, DP.defaultUndefinedValue(param.collegeName, ''));
                sessionStorage.setItem(webStorageKey.BUILDING_NAME, param.buildingName);
                sessionStorage.setItem(webStorageKey.COORDINATE, param.coordinate);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var schoolName = null;
            var collegeName = null;
            var buildingName = null;
            var coordinate = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                buildingName = sessionStorage.getItem(webStorageKey.BUILDING_NAME);
                coordinate = sessionStorage.getItem(webStorageKey.COORDINATE);
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

            if (coordinate !== null) {
                param.coordinate = coordinate;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            var collegeName = null;
            var buildingName = null;
            var coordinate = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                buildingName = sessionStorage.getItem(webStorageKey.BUILDING_NAME);
                coordinate = sessionStorage.getItem(webStorageKey.COORDINATE);
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

            if (coordinate !== null) {
                $(getParamId().coordinate).val(coordinate);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
            $(getParamId().collegeName).val('');
            $(getParamId().buildingName).val('');
            $(getParamId().coordinate).val('');
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

        $(getParamId().coordinate).keyup(function (event) {
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
        $('#building_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#building_dels').click(function () {
            var buildingIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                buildingIds.push($(ids[i]).val());
            }

            if (buildingIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的楼吗？",
                    text: "楼注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(buildingIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的楼!");
            }
        });

        /*
         批量恢复
         */
        $('#building_recoveries').click(function () {
            var buildingIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                buildingIds.push($(ids[i]).val());
            }

            if (buildingIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的楼吗？",
                    text: "楼恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(buildingIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的楼!");
            }
        });

        /*
         编辑页面
         */
        function edit(buildingId) {
            $.address.value(getAjaxUrl().edit + '/' + buildingId);
        }

        /*
         注销
         */
        function building_del(buildingId, buildingName) {
            Swal.fire({
                title: "确定注销楼 '" + buildingName + "' 吗？",
                text: "楼注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(buildingId);
                }
            });
        }

        /*
         恢复
         */
        function building_recovery(buildingId, buildingName) {
            Swal.fire({
                title: "确定恢复楼 '" + buildingName + "' 吗？",
                text: "楼恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(buildingId);
                }
            });
        }

        function del(buildingId) {
            sendStatusAjax(buildingId, 1);
        }

        function recovery(buildingId) {
            sendStatusAjax(buildingId, 0);
        }

        function dels(buildingIds) {
            sendStatusAjax(buildingIds.join(","), 1);
        }

        function recoveries(buildingIds) {
            sendStatusAjax(buildingIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param buildingId
         * @param isDel
         */
        function sendStatusAjax(buildingId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {buildingIds: buildingId, isDel: isDel},
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