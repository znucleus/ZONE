//# sourceURL=calendar_data.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        /*
        参数
        */
        var param = {
            title: ''
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            TITLE: 'EDUCATIONAL_CALENDAR_TITLE_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/educational/calendar/data',
                add: '/web/educational/calendar/add',
                edit: '/web/educational/calendar/edit',
                del: web_path + '/web/educational/calendar/delete',
                page: '/web/menu/educational/calendar'
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
                localStorage.setItem('EDUCATIONAL_CALENDAR_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('EDUCATIONAL_CALENDAR_' + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[11, 'desc']],// 排序
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
                {"data": "title"},
                {"data": "collegeName"},
                {"data": "academicYear"},
                {"data": "term"},
                {"data": "startDate"},
                {"data": "endDate"},
                {"data": "holidayStartDate"},
                {"data": "holidayEndDate"},
                {"data": "publisher"},
                {"data": "releaseTimeStr"},
                {"data": "remark"},
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
                        return '<input type="checkbox" value="' + c.calendarId + '" name="check"/>';
                    }
                },
                {
                    targets: 5,
                    render: function (a, b, c, d) {
                        var v = '';
                        if (Number(c.term) === 0) {
                            v = '上学期';
                        } else if (Number(c.term) === 1) {
                            v = '下学期';
                        }
                        return v;
                    }
                },
                {
                    targets: 13,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.calendarId,
                                    "calendar": c.title
                                },
                                {
                                    "name": "删除",
                                    "css": "del",
                                    "type": "danger",
                                    "id": c.calendarId,
                                    "calendar": c.title
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
                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'));
                });

                tableElement.delegate('.del', "click", function () {
                    calendar_del($(this).attr('data-id'), $(this).attr('data-calendar'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="calendar_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="calendar_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                title: '#search_title'
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
            param.title = $(getParamId().title).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.TITLE, param.title);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var title = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
            }
            if (title !== null) {
                param.title = title;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var title = null;
            if (typeof (Storage) !== "undefined") {
                title = sessionStorage.getItem(webStorageKey.TITLE);
            }
            if (title !== null) {
                $(getParamId().title).val(title);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().title).val('');
        }

        $(getParamId().title).keyup(function (event) {
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
        $('#calendar_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量删除
         */
        $('#calendar_dels').click(function () {
            var calendarIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                calendarIds.push($(ids[i]).val());
            }

            if (calendarIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的校历吗？",
                    text: "校历删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(calendarIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的校历!");
            }
        });

        /*
         编辑页面
         */
        function edit(calendarId) {
            $.address.value(getAjaxUrl().edit + '/' + calendarId);
        }

        /*
         删除
         */
        function calendar_del(calendarId, title) {
            Swal.fire({
                title: "确定删除校历 '" + title + "' 吗？",
                text: "校历删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(calendarId);
                }
            });
        }

        function del(calendarId) {
            sendDelAjax(calendarId);
        }

        function dels(calendarIds) {
            sendDelAjax(calendarIds.join(","));
        }

        /**
         * 删除ajax
         * @param calendarId
         */
        function sendDelAjax(calendarId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {calendarIds: calendarId},
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