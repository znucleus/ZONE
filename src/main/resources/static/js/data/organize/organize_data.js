//# sourceURL=organize_data.js
require(["jquery", "lodash_plugin", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, DP, Handlebars, navActive, Swal) {

        /*
        参数
        */
        var param = {
            schoolName: '',
            collegeName: '',
            departmentName: '',
            scienceName: '',
            organizeName: '',
            grade: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            SCHOOL_NAME: 'DATA_ORGANIZE_SCHOOL_NAME_SEARCH',
            COLLEGE_NAME: 'DATA_ORGANIZE_COLLEGE_NAME_SEARCH',
            DEPARTMENT_NAME: 'DATA_ORGANIZE_DEPARTMENT_NAME_SEARCH',
            SCIENCE_NAME: 'DATA_ORGANIZE_SCIENCE_NAME_SEARCH',
            ORGANIZE_NAME: 'DATA_ORGANIZE_ORGANIZE_NAME_SEARCH',
            GRADE: 'DATA_ORGANIZE_GRADE_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/organize/paging',
                status: web_path + '/web/data/organize/status',
                add: '/web/data/organize/add',
                edit: '/web/data/organize/edit',
                page: '/web/menu/data/organize'
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
                localStorage.setItem('DATA_ORGANIZE_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('DATA_ORGANIZE_' + settings.sInstance))
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
                {"data": "organizeId"},
                {"data": "schoolName"},
                {"data": "collegeName"},
                {"data": "departmentName"},
                {"data": "scienceName"},
                {"data": "grade"},
                {"data": "organizeName"},
                {"data": "realName"},
                {"data": "organizeIsDel"},
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
                        return '<input type="checkbox" value="' + c.organizeId + '" name="check"/>';
                    }
                },
                {
                    targets: 11,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.organizeId,
                                    "organize": c.organizeName
                                }
                            ]
                        };

                        if (c.organizeIsDel === 0 || c.organizeIsDel == null) {
                            context.func.push({
                                "name": "注销",
                                "css": "del",
                                "type": "danger",
                                "id": c.organizeId,
                                "organize": c.organizeName
                            });
                        } else {
                            context.func.push({
                                "name": "恢复",
                                "css": "recovery",
                                "type": "warning",
                                "id": c.organizeId,
                                "organize": c.organizeName
                            });
                        }

                        return template(context);
                    }
                },
                {
                    targets: 10,
                    render: function (a, b, c, d) {
                        if (c.organizeIsDel === 0 || c.organizeIsDel == null) {
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
                    organize_del($(this).attr('data-id'), $(this).attr('data-organize'));
                });

                tableElement.delegate('.recovery', "click", function () {
                    organize_recovery($(this).attr('data-id'), $(this).attr('data-organize'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="organize_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="organize_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="organize_recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
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
                scienceName: '#search_science',
                organizeName: '#search_organize',
                grade: '#search_grade'
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
            param.organizeName = $(getParamId().organizeName).val();
            param.grade = $(getParamId().grade).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, DP.defaultUndefinedValue(param.schoolName, ''));
                sessionStorage.setItem(webStorageKey.COLLEGE_NAME, DP.defaultUndefinedValue(param.collegeName, ''));
                sessionStorage.setItem(webStorageKey.DEPARTMENT_NAME, DP.defaultUndefinedValue(param.departmentName, ''));
                sessionStorage.setItem(webStorageKey.SCIENCE_NAME, param.scienceName);
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, param.organizeName);
                sessionStorage.setItem(webStorageKey.GRADE, param.grade);
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
            var organizeName = null;
            var grade = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                departmentName = sessionStorage.getItem(webStorageKey.DEPARTMENT_NAME);
                scienceName = sessionStorage.getItem(webStorageKey.SCIENCE_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                grade = sessionStorage.getItem(webStorageKey.GRADE);
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

            if (organizeName !== null) {
                param.organizeName = organizeName;
            }

            if (grade !== null) {
                param.grade = grade;
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
            var organizeName = null;
            var grade = null;
            if (typeof (Storage) !== "undefined") {
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                collegeName = sessionStorage.getItem(webStorageKey.COLLEGE_NAME);
                departmentName = sessionStorage.getItem(webStorageKey.DEPARTMENT_NAME);
                scienceName = sessionStorage.getItem(webStorageKey.SCIENCE_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                grade = sessionStorage.getItem(webStorageKey.GRADE);
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

            if (organizeName !== null) {
                $(getParamId().organizeName).val(organizeName);
            }

            if (grade !== null) {
                $(getParamId().grade).val(grade);
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
            $(getParamId().organizeName).val('');
            $(getParamId().grade).val('');
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

        $(getParamId().organizeName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().grade).keyup(function (event) {
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
        $('#organize_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量注销
         */
        $('#organize_dels').click(function () {
            var organizeIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                organizeIds.push($(ids[i]).val());
            }

            if (organizeIds.length > 0) {
                Swal.fire({
                    title: "确定注销选中的班级吗？",
                    text: "班级注销！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(organizeIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的班级!");
            }
        });

        /*
         批量恢复
         */
        $('#organize_recoveries').click(function () {
            var organizeIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                organizeIds.push($(ids[i]).val());
            }

            if (organizeIds.length > 0) {
                Swal.fire({
                    title: "确定恢复选中的班级吗？",
                    text: "班级恢复！",
                    type: "success",
                    showCancelButton: true,
                    confirmButtonColor: '#27dd4b',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        recoveries(organizeIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的班级!");
            }
        });

        /*
         编辑页面
         */
        function edit(organizeId) {
            $.address.value(getAjaxUrl().edit + '/' + organizeId);
        }

        /*
         注销
         */
        function organize_del(organizeId, organizeName) {
            Swal.fire({
                title: "确定注销班级 '" + organizeName + "' 吗？",
                text: "班级注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(organizeId);
                }
            });
        }

        /*
         恢复
         */
        function organize_recovery(organizeId, organizeName) {
            Swal.fire({
                title: "确定恢复班级 '" + organizeName + "' 吗？",
                text: "班级恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    recovery(organizeId);
                }
            });
        }

        function del(organizeId) {
            sendStatusAjax(organizeId, 1);
        }

        function recovery(organizeId) {
            sendStatusAjax(organizeId, 0);
        }

        function dels(organizeIds) {
            sendStatusAjax(organizeIds.join(","), 1);
        }

        function recoveries(organizeIds) {
            sendStatusAjax(organizeIds.join(","), 0);
        }

        /**
         * 注销或恢复ajax
         * @param organizeId
         * @param isDel
         */
        function sendStatusAjax(organizeId, isDel) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().status,
                data: {organizeIds: organizeId, isDel: isDel},
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