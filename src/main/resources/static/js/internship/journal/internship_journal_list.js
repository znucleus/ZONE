//# sourceURL=internship_journal_list.js
require(["jquery", "sweetalert2", "handlebars", "workbook", "nav.active", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Swal, Handlebars, workbook, navActive) {

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            authorities: $('#authorities').val(),
            usersTypeName: $('#usersTypeName').val(),
            studentId: Number($('#studentId').val()),
            staffId: Number($('#staffId').val()),
            canAdd: Number($('#canAdd').val())
        };

        /*
        参数
        */
        var param = {
            studentName: '',
            studentNumber: '',
            organize: '',
            guidanceTeacher: '',
            internshipReleaseId: page_param.paramInternshipReleaseId,
            dataRange: 0,
            staffId: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            STUDENT_NAME: 'INTERNSHIP_JOURNAL_LIST_STUDENT_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_JOURNAL_LIST_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            ORGANIZE: 'INTERNSHIP_JOURNAL_LIST_ORGANIZE_SEARCH_' + page_param.paramInternshipReleaseId
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/journal/data',
                obtain_staff_data: web_path + '/web/internship/journal/team/staff',
                del: web_path + '/web/internship/journal/del',
                edit: '/web/internship/journal/edit',
                look: '/web/internship/journal/look',
                download: web_path + '/web/internship/journal/download',
                downloads: web_path + '/web/internship/journal/downloads',
                statistical: web_path + '/web/internship/journal/statistical',
                exports: web_path + '/web/internship/journal/exports',
                add: '/web/internship/journal/add',
                page: '/web/menu/internship/journal'
            };
        }

        // 刷新时选中菜单
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
                {"data": "organize"},
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
                        return '<input type="checkbox" value="' + c.internshipJournalId + '" name="check"/>';
                    }
                },
                {
                    targets: 7,
                    orderable: false,
                    render: function (a, b, c, d) {
                        var context = null;
                        var html = '<i class="fa fa-lock"></i>';
                        // 当前用户查看自己的实习日志，指导教师查看 或系统，管理员 或发布人
                        if (c.studentId === page_param.studentId ||
                            page_param.authorities === workbook.authorities.ROLE_SYSTEM ||
                            page_param.authorities === workbook.authorities.ROLE_ADMIN ||
                            c.staffId === page_param.staffId) {
                            context =
                                {
                                    func: [
                                        {
                                            "name": "查看",
                                            "css": "look",
                                            "type": "info",
                                            "id": c.internshipJournalId
                                        },
                                        {
                                            "name": "编辑",
                                            "css": "edit",
                                            "type": "primary",
                                            "id": c.internshipJournalId
                                        },
                                        {
                                            "name": "删除",
                                            "css": "del",
                                            "type": "danger",
                                            "id": c.internshipJournalId
                                        }
                                    ]
                                };

                            if (c.internshipJournalWord && c.internshipJournalWord !== '') {
                                context.func.push({
                                    "name": "下载",
                                    "css": "download",
                                    "type": "primary",
                                    "id": c.internshipJournalId
                                });
                            }
                        } else {
                            if (c.isSeeStaff === 1) {
                                if (page_param.usersTypeName === workbook.users_type.STAFF_USERS_TYPE) {
                                    context =
                                        {
                                            func: [
                                                {
                                                    "name": "查看",
                                                    "css": "look",
                                                    "type": "info",
                                                    "id": c.internshipJournalId
                                                }
                                            ]
                                        };

                                    if (c.internshipJournalWord && c.internshipJournalWord !== '') {
                                        context.func.push({
                                            "name": "下载",
                                            "css": "download",
                                            "type": "primary",
                                            "id": c.internshipJournalId
                                        });
                                    }
                                }
                            } else {
                                context =
                                    {
                                        func: [
                                            {
                                                "name": "查看",
                                                "css": "look",
                                                "type": "info",
                                                "id": c.internshipJournalId
                                            }
                                        ]
                                    };

                                if (c.internshipJournalWord && c.internshipJournalWord !== '') {
                                    context.func.push({
                                        "name": "下载",
                                        "css": "download",
                                        "type": "primary",
                                        "id": c.internshipJournalId
                                    });
                                }
                            }

                        }

                        if (context != null) {
                            html = template(context);
                        }
                        return html;
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
            "dom": "<'row'<'col-lg-4 col-md-12'l><'#global_button.col-lg-8 col-md-12'>r>" +
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
                    journal_del($(this).attr('data-id'));
                });

                tableElement.delegate('.download', "click", function () {
                    download($(this).attr('data-id'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        if (page_param.authorities === workbook.authorities.ROLE_SYSTEM ||
            page_param.authorities === workbook.authorities.ROLE_ADMIN) {
            var temp = '  <button type="button" id="journal_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>';
            global_button = temp + global_button;
        }
        if (page_param.canAdd > 0) {
            global_button = '<button type="button" id="journal_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
                global_button;
        }
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                studentName: '#search_real_name',
                studentNumber: '#search_student_number',
                organize: '#search_organize'
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
            param.studentName = $(getParamId().studentName).val();
            param.studentNumber = $(getParamId().studentNumber).val();
            param.organize = $(getParamId().organize).val();
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.STUDENT_NAME, param.studentName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.ORGANIZE, param.organize);
            }
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var studentName = null;
            var studentNumber = null;
            var organize = null;
            if (typeof(Storage) !== "undefined") {
                studentName = sessionStorage.getItem(webStorageKey.STUDENT_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organize = sessionStorage.getItem(webStorageKey.ORGANIZE);
            }
            if (studentName !== null) {
                param.studentName = studentName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (organize !== null) {
                param.organize = organize;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var studentName = null;
            var studentNumber = null;
            var organize = null;
            if (typeof(Storage) !== "undefined") {
                studentName = sessionStorage.getItem(webStorageKey.STUDENT_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organize = sessionStorage.getItem(webStorageKey.ORGANIZE);
            }
            if (studentName !== null) {
                $(getParamId().studentName).val(studentName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (organize !== null) {
                $(getParamId().organize).val(organize);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().studentName).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().organize).val('');
        }

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

        $(getParamId().organize).keyup(function (event) {
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
         添加
        */
        $('#journal_add').click(function () {
            $.address.value(getAjaxUrl().add + '/' + page_param.paramInternshipReleaseId);
        });

        /*
         批量删除
         */
        $('#journal_dels').click(function () {
            var journalIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                journalIds.push($(ids[i]).val());
            }
            if (journalIds.length > 0) {
                journal_dels(journalIds);
            } else {
                Messenger().post("未发现有选中的日志!");
            }
        });

        /*
         查看页面
         */
        function look(journalId) {
            $.address.value(getAjaxUrl().look + '/' + journalId);
        }

        /*
         编辑页面
         */
        function edit(journalId) {
            $.address.value(getAjaxUrl().edit + '/' + journalId);
        }

        /*
         下载
         */
        function download(journalId) {
            window.location.href = getAjaxUrl().download + '/' + journalId;
        }

        /*
         删除
         */
        function journal_del(journalId) {
            Swal.fire({
                title: "确定删除日志吗？",
                text: "日志删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(journalId);
                }
            });
        }

        /*
         批量删除
         */
        function journal_dels(journalIds) {
            Swal.fire({
                title: "确定删除选中日志吗？",
                text: "日志批量删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    dels(journalIds);
                }
            });
        }

        function del(journalId) {
            sendDelAjax(journalId);
        }

        function dels(journalIds) {
            sendDelAjax(journalIds.join(','));
        }

        /**
         * 删除ajax
         * @param journalId
         */
        function sendDelAjax(journalId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {journalIds: journalId},
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

        init();

        function init() {
            initTeamStaff();
        }

        function initTeamStaff() {
            $.get(getAjaxUrl().obtain_staff_data + "/" + page_param.paramInternshipReleaseId, function (data) {
                teamData(data);
            });
        }

        var teamElement = $('#team');

        function teamData(data) {
            data.listResult.forEach(function (e, v) {
                if (page_param.usersTypeName === workbook.users_type.STAFF_USERS_TYPE ||
                    page_param.authorities === workbook.authorities.ROLE_SYSTEM ||
                    page_param.authorities === workbook.authorities.ROLE_ADMIN) {
                    e.canOperator = 1;
                } else {
                    e.canOperator = 0;
                }

            });
            var template = Handlebars.compile($("#team_template").html());
            teamElement.html(template(data));
        }

        teamElement.delegate('.staff', "click", function () {
            var staffIds = [];
            var ids = $('input[name="staff"]:checked');
            for (var i = 0; i < ids.length; i++) {
                staffIds.push($(ids[i]).val());
            }
            if (staffIds.length > 0) {
                param.staffId = staffIds.join(',');
                param.dataRange = 2;
            } else {
                param.staffId = '';
                param.dataRange = 0;
            }
            initParam();
            myTable.ajax.reload();

        });

        teamElement.delegate('.download', "click", function () {
            var staffId = $(this).attr("data-id");
            window.location.href = getAjaxUrl().downloads + "/" + page_param.paramInternshipReleaseId + "/" + staffId;
        });

        teamElement.delegate('.count', "click", function () {
            var staffId = $(this).attr("data-id");
            $.address.value(getAjaxUrl().statistical + "/" + page_param.paramInternshipReleaseId + "/" + staffId);
        });

        teamElement.delegate('.export', "click", function () {
            var staffId = $(this).attr("data-id");
            window.location.href = getAjaxUrl().exports + "/" + page_param.paramInternshipReleaseId + "/" + staffId;
        });
    });