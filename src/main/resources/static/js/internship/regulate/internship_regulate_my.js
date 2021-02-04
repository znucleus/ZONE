//# sourceURL=internship_regulate_my.js
require(["jquery", "sweetalert2", "handlebars", "workbook", "nav.active", "responsive.bootstrap4", "check.all", "jquery.address", "messenger", "flatpickr-zh"],
    function ($, Swal, Handlebars, workbook, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/regulate/paging',
                export_url: web_path + '/web/internship/regulate/export',
                del: web_path + '/web/internship/regulate/delete',
                edit: '/web/internship/regulate/edit',
                add: '/web/internship/regulate/add',
                look: '/web/internship/regulate/look',
                page: '/web/menu/internship/regulate'
            };
        }

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        /*
         参数id
         */
        function getParamId() {
            return {
                studentName: '#search_real_name',
                studentNumber: '#search_student_number',
                createDate: '#search_create_date'
            };
        }

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            staffId: Number($('#staffId').val())
        };

        /*
         参数
         */
        var param = {
            studentName: '',
            studentNumber: '',
            createDate: '',
            internshipReleaseId: page_param.paramInternshipReleaseId,
            dataRange: 1
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            STUDENT_NAME: 'INTERNSHIP_REGULATE_MY_STUDENT_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_REGULATE_MY_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            CREATE_DATE: 'INTERNSHIP_REGULATE_MY_CREATE_DATE_SEARCH_' + page_param.paramInternshipReleaseId
        };

        /*
         得到参数
         */
        function getParam() {
            return param;
        }

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
                localStorage.setItem('INTERNSHIP_REGULATE_MY_' + page_param.paramInternshipReleaseId + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('INTERNSHIP_REGULATE_MY_' + page_param.paramInternshipReleaseId + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[6, 'desc']],// 排序
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
                {"data": "studentName"},
                {"data": "studentNumber"},
                {"data": "studentTel"},
                {"data": "schoolGuidanceTeacher"},
                {"data": "createDateStr"},
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
                        return '<input type="checkbox" value="' + c.internshipRegulateId + '" name="check"/>';
                    }
                },
                {
                    targets: 7,
                    orderable: false,
                    render: function (a, b, c, d) {
                        var context =
                            {
                                func: [
                                    {
                                        "name": "查看",
                                        "css": "look",
                                        "type": "info",
                                        "id": c.internshipRegulateId
                                    },
                                    {
                                        "name": "编辑",
                                        "css": "edit",
                                        "type": "primary",
                                        "id": c.internshipRegulateId
                                    },
                                    {
                                        "name": "删除",
                                        "css": "del",
                                        "type": "danger",
                                        "id": c.internshipRegulateId
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
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-10 col-md-12'>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.look', "click", function () {
                    look($(this).attr('data-id'));
                });

                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    regulate_del($(this).attr('data-id'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="regulate_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="regulate_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         初始化参数
         */
        function initParam() {
            param.studentName = $(getParamId().studentName).val();
            param.studentNumber = $(getParamId().studentNumber).val();
            param.createDate = $(getParamId().createDate).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.STUDENT_NAME, param.studentName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.CREATE_DATE, param.createDate);
            }
        }

        $(getParamId().createDate).flatpickr({
            "locale": "zh",
            "mode": "range",
            onClose: function () {
                initParam();
                myTable.ajax.reload();
            }
        });

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var studentName = null;
            var studentNumber = null;
            var createDate = null;
            if (typeof (Storage) !== "undefined") {
                studentName = sessionStorage.getItem(webStorageKey.STUDENT_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                createDate = sessionStorage.getItem(webStorageKey.CREATE_DATE);
            }
            if (studentName !== null) {
                param.studentName = studentName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (createDate !== null) {
                param.createDate = createDate;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var studentName = null;
            var studentNumber = null;
            var createDate = null;
            if (typeof (Storage) !== "undefined") {
                studentName = sessionStorage.getItem(webStorageKey.STUDENT_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                createDate = sessionStorage.getItem(webStorageKey.CREATE_DATE);
            }
            if (studentName !== null) {
                $(getParamId().studentName).val(studentName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (createDate !== null) {
                $(getParamId().createDate).val(createDate);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().studentName).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().createDate).val('');
        }

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

        $(getParamId().studentName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().studentNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().schoolGuidanceTeacher).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $('#export_xls').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xls'
            };
            window.location.href = encodeURI(getAjaxUrl().export_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        $('#export_xlsx').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xlsx'
            };
            window.location.href = encodeURI(getAjaxUrl().export_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        /*
         添加
         */
        $('#regulate_add').click(function () {
            $.address.value(getAjaxUrl().add + '/' + page_param.paramInternshipReleaseId);
        });

        /*
         批量删除
         */
        $('#regulate_dels').click(function () {
            var regulateIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                regulateIds.push($(ids[i]).val());
            }
            if (regulateIds.length > 0) {
                regulate_dels(regulateIds);
            } else {
                Messenger().post("未发现有选中的记录!");
            }
        });

        /*
         查看页面
         */
        function look(regulateId) {
            $.address.value(getAjaxUrl().look + '/' + regulateId);
        }

        /*
         编辑页面
         */
        function edit(regulateId) {
            $.address.value(getAjaxUrl().edit + '/' + regulateId);
        }

        /*
         删除
         */
        function regulate_del(regulateId) {
            Swal.fire({
                title: "确定删除监管记录吗？",
                text: "记录删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(regulateId);
                }
            });
        }

        /*
         批量删除
         */
        function regulate_dels(regulateIds) {
            Swal.fire({
                title: "确定删除选中的监管记录吗？",
                text: "记录批量删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    dels(regulateIds);
                }
            });
        }

        function del(regulateId) {
            sendDelAjax(regulateId);
        }

        function dels(regulateIds) {
            sendDelAjax(regulateIds.join(','));
        }

        /**
         * 删除ajax
         * @param regulateId
         */
        function sendDelAjax(regulateId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {regulateIds: regulateId},
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