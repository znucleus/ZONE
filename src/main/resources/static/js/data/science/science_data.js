//# sourceURL=science_data.js
require(["jquery", "lodash_plugin", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, DP, Handlebars, navActive, Swal) {

        /*
         参数
         */
        var param = {
            schoolName: '',
            collegeName: '',
            departmentName: '',
            scienceName: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_SCIENCE_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: 'DATA_SCIENCE_COLLEGE_NAME_SEARCH',
            DEPARTMENT_NAME: 'DATA_SCIENCE_DEPARTMENT_NAME_SEARCH',
            SCIENCE_NAME: 'DATA_SCIENCE_SCIENCE_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/science/data',
                status: web_path + '/web/data/science/status',
                add: '/web/data/science/add',
                edit: '/web/data/science/edit',
                page: '/web/menu/data/science'
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
                localStorage.setItem('DATA_SCIENCE_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('DATA_SCIENCE_' + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[2, 'desc']],// 排序
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
                {"data": "scienceId"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "departmentName"},
                {"data": "scienceName"},
                {"data": "scienceCode"},
                {"data": "scienceIsDel"},
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
                        return '<input type="checkbox" value="' + c.scienceId + '" name="check"/>';
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
                                    "id": c.scienceId,
                                    "science": c.scienceName
                                }
                            ]
                        };

                        if (c.scienceIsDel === 0 || c.scienceIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.scienceId,
                                "science": c.scienceName
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.scienceId,
                                "science": c.scienceName
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 8,
                    render: function (a, b, c, d) {
                        if (c.scienceIsDel === 0 || c.scienceIsDel == null) {
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
                    science_del($(this).attr('data-id'), $(this).attr('data-science'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    science_recovery($(this).attr('data-id'), $(this).attr('data-science'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="science_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="science_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="science_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                schoolName: '#search_school',
                collegeName: '#search_college',
                departmentName: '#search_department',
                scienceName: '#search_science'
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
            param.scienceName = $(getParamId().scienceName).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, DP.defaultUndefinedValue(param.schoolName, ''));
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, DP.defaultUndefinedValue(param.collegeName, ''));
                sessionStorage.setItem(webStorageKey.DEPARTMENT_NAME, DP.defaultUndefinedValue(param.departmentName, ''));
                sessionStorage.setItem(webStorageKey.SCIENCE_NAME, param.scienceName);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var schoolName = null;
            var collegeName = null;
            var departmentName = null;
            var scienceName = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                departmentName = sessionStorage.getItem(webStorageKey.DEPARTMENT_NAME);
                scienceName = sessionStorage.getItem(webStorageKey.SCIENCE_NAME);
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

            if (scienceName !== null) {
                param.scienceName = scienceName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var schoolName = null;
            var collegeName = null;
            var departmentName = null;
            var scienceName = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                departmentName = sessionStorage.getItem(webStorageKey.DEPARTMENT_NAME);
                scienceName = sessionStorage.getItem(webStorageKey.SCIENCE_NAME);
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

            if (scienceName !== null) {
                $(getParamId().scienceName).val(scienceName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().schoolName).val('');
            $(getParamId().collegeName).val('');
            $(getParamId().departmentName).val('');
            $(getParamId().scienceName).val('');
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

        $(getParamId().scienceName).keyup(function (event) {
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
        $('#science_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#science_dels').click(function () {
            var scienceIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                scienceIds.push($(ids[i]).val());
            }

            if (scienceIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的专业吗？",
                    text: "专业注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(scienceIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的专业!");
            }
        });

        /*
         批量恢复
         */
        $('#science_recoveries').click(function () {
            var scienceIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                scienceIds.push($(ids[i]).val());
            }

            if (scienceIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的专业吗？",
                    text: "专业恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(scienceIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的专业!");
            }
        });

        /*
         编辑页面
         */
        function edit(scienceId) {
            $.address.value(getAjaxUrl().edit + '/' + scienceId);
        }

        /*
         注销
         */
        function science_del(scienceId, scienceName) {
            Swal.fire({
                title: "确定注销专业 '" + scienceName + "' 吗？",
                text: "专业注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(scienceId);
                }
            });
        }

        /*
         恢复
         */
        function science_recovery(scienceId, scienceName) {
            Swal.fire({
                title: "确定恢复专业 '" + scienceName + "' 吗？",
                text: "专业恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(scienceId);
                }
            });
        }

        function del(scienceId) {
            sendStatusAjax(scienceId, 1);
        }

        function recovery(scienceId) {
            sendStatusAjax(scienceId, 0);
        }

        function dels(scienceIds) {
            sendStatusAjax(scienceIds.join(","), 1);
        }

        function recoveries(scienceIds) {
            sendStatusAjax(scienceIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param scienceId
         * @param isDel
         */
        function sendStatusAjax(scienceId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {scienceIds: scienceId, isDel: isDel},
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