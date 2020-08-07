//# sourceURL=internship_journal_my.js
require(["jquery", "sweetalert2", "handlebars", "nav.active", "responsive.bootstrap4", "check.all", "jquery.address", "messenger", "flatpickr-zh"],
    function ($, Swal, Handlebars, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/journal/data',
                del: web_path + '/web/internship/journal/del',
                edit: '/web/internship/journal/edit',
                add: '/web/internship/journal/add',
                look: '/web/internship/journal/look',
                download: web_path + '/web/internship/journal/download',
                downloads: web_path + '/web/internship/journal/my/downloads',
                page: '/web/menu/internship/journal'
            };
        }

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        /*
         参数id
         */
        function getParamId() {
            return {
                createDate: '#search_create_date'
            };
        }

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            studentId: Number($('#studentId').val())
        };

        /*
         参数
         */
        var param = {
            createDate: '',
            internshipReleaseId: page_param.paramInternshipReleaseId,
            dataRange: 1,
            studentId: page_param.studentId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            CREATE_DATE: 'INTERNSHIP_JOURNAL_MY_CREATE_DATE_SEARCH_' + page_param.paramInternshipReleaseId + page_param.studentId
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
                localStorage.setItem('INTERNSHIP_JOURNAL_MY_' + page_param.paramInternshipReleaseId + page_param.studentId + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('INTERNSHIP_JOURNAL_MY_' + page_param.paramInternshipReleaseId + page_param.studentId + settings.sInstance))
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

                        var context =
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
                                    },
                                    {
                                        "name": "下载",
                                        "css": "download",
                                        "type": "teal",
                                        "id": c.internshipJournalId
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
            "dom": "<'row'<'col-sm-2'l><'#global_button.col-sm-5'>r>" +
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

        var global_button = '<button type="button" id="journal_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="journal_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
            '  <button type="button" id="journal_download_all" class="btn btn-outline-default btn-sm"><i class="fa fa-download"></i>下载全部</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         初始化参数
         */
        function initParam() {
            param.createDate = $(getParamId().createDate).val();
            if (typeof (Storage) !== "undefined") {
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
            var createDate = null;
            if (typeof (Storage) !== "undefined") {
                createDate = sessionStorage.getItem(webStorageKey.CREATE_DATE);
            }
            if (createDate !== null) {
                param.createDate = createDate;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var createDate = null;
            if (typeof (Storage) !== "undefined") {
                createDate = sessionStorage.getItem(webStorageKey.CREATE_DATE);
            }
            if (createDate !== null) {
                $(getParamId().createDate).val(createDate);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
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

        /*
         添加
         */
        $('#journal_add').click(function () {
            $.address.value(getAjaxUrl().add + '/' + page_param.paramInternshipReleaseId);
        });

        /*
         下载 全部
         */
        $('#journal_download_all').click(function () {
            window.location.href = getAjaxUrl().downloads + '/' + page_param.paramInternshipReleaseId;
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
            journal_dels(journalIds);
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
                text: "日志删除！",
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
    });