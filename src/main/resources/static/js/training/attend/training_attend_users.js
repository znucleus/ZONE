//# sourceURL=training_attend_users.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4",
        "check.all", "jquery.address", "messenger"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val(),
            paramTrainingAttendId: $('#paramTrainingAttendId').val(),
            canOperator: $('#canOperator').val()
        };

        /*
         参数
        */
        var param = {
            realName: '',
            studentNumber: '',
            operate: '',
            trainingAttendId: page_param.paramTrainingAttendId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'TRAINING_ATTEND_USERS_REAL_NAME_SEARCH_' + page_param.paramTrainingAttendId,
            STUDENT_NUMBER: 'TRAINING_ATTEND_USERS_STUDENT_NUMBER_SEARCH_' + page_param.paramTrainingAttendId,
            OPERATE: 'TRAINING_ATTEND_USERS_OPERATE_SEARCH_' + page_param.paramTrainingAttendId
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/training/attend/users/paging',
                export_data_url: web_path + '/web/training/attend/users/export',
                operate: web_path + '/web/training/attend/users/operate',
                del: web_path + '/web/training/attend/users/delete',
                remark: web_path + '/web/training/attend/users/remark',
                reset: web_path + '/web/training/attend/users/reset',
                all_ok: web_path + '/web/training/attend/users/full-attendance',
                page: '/web/menu/training/attend'
            };
        }

        navActive(getAjaxUrl().page);

        var button_id = {
            toRemark: {
                tip: '更新中...',
                text: '确定',
                id: '#toRemark'
            }
        };

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
                localStorage.setItem('TRAINING_ATTEND_USERS_' + page_param.paramTrainingReleaseId + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('TRAINING_ATTEND_USERS_' + page_param.paramTrainingReleaseId + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[3, 'asc']],// 排序
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
                {"data": "realName"},
                {"data": "studentNumber"},
                {"data": "organizeName"},
                {"data": "mobile"},
                {"data": "email"},
                {"data": "sex"},
                {"data": "operate"},
                {"data": "remark"},
                {"data": "operateUser"},
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
                        return '<input type="checkbox" value="' + c.attendUsersId + '" name="check"/>';
                    }
                },
                {
                    targets: 8,
                    render: function (a, b, c, d) {
                        var v = '';
                        if (c.operate === 0) {
                            v = '<span class="text-danger">缺席</span>';
                        } else if (c.operate === 1) {
                            v = '<span class="text-info">请假</span>';
                        } else if (c.operate === 2) {
                            v = '<span class="text-warning">迟到</span>';
                        } else if (c.operate === 3) {
                            v = '<span class="text-success">正常</span>';
                        }
                        return v;
                    }
                },
                {
                    targets: 11,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = null;
                        var html = '<i class="fa fa-lock"></i>';

                        if (Number(page_param.canOperator) === 1) {
                            context =
                                {
                                    func: [
                                        {
                                            "name": "状态",
                                            "css": "operate",
                                            "type": "primary",
                                            "id": c.attendUsersId,
                                            "student": c.realName,
                                            "remark": c.remark,
                                            "operate": c.operate
                                        },
                                        {
                                            "name": "删除",
                                            "css": "del",
                                            "type": "danger",
                                            "id": c.attendUsersId,
                                            "student": c.realName,
                                            "remark": c.remark,
                                            "operate": c.operate
                                        }, {
                                            "name": "备注",
                                            "css": "remark",
                                            "type": "light",
                                            "id": c.attendUsersId,
                                            "student": c.realName,
                                            "remark": c.remark,
                                            "operate": c.operate
                                        }
                                    ]
                                };
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
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-10 col-md-12'>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.operate', "click", function () {
                    operate($(this).attr('data-id'), $(this).attr('data-student'), $(this).attr('data-operate'));
                });

                tableElement.delegate('.del', "click", function () {
                    training_del($(this).attr('data-id'), $(this).attr('data-student'));
                });

                tableElement.delegate('.remark', "click", function () {
                    remark($(this).attr('data-id'), $(this).attr('data-remark'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '';
        if (Number(page_param.canOperator) === 1) {
            global_button += '<button type="button" id="all_ok" class="btn btn-outline-primary btn-sm"><i class="fa fa-star-o"></i>全勤</button>' +
                '  <button type="button" id="training_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
                '  <button type="button" id="training_reset" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>重置</button>';
        }

        global_button += '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                realName: '#search_real_name',
                studentNumber: '#search_student_number',
                operate: '#search_operate'
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
            param.realName = $(getParamId().realName).val();
            param.studentNumber = $(getParamId().studentNumber).val();
            param.operate = $(getParamId().operate).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.OPERATE, param.operate);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var operate = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                operate = sessionStorage.getItem(webStorageKey.OPERATE);
            }
            if (realName !== null) {
                param.realName = realName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (operate !== null) {
                param.operate = operate;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            var operate = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                operate = sessionStorage.getItem(webStorageKey.OPERATE);
            }
            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (operate !== null) {
                $(getParamId().operate).val(operate);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().operate).val('');
        }

        $(getParamId().realName).keyup(function (event) {
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

        $(getParamId().operate).change(function () {
            initParam();
            myTable.ajax.reload();
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

        $('#all_ok').click(function () {
            Swal.fire({
                title: "确定全勤吗？",
                text: "全勤！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#2998dd',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendAllOkAjax();
                }
            });
        });

        /*
         批量删除
         */
        $('#training_dels').click(function () {
            var attendUsersIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                attendUsersIds.push($(ids[i]).val());
            }

            if (attendUsersIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的学生吗？",
                    text: "学生删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(attendUsersIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的学生!");
            }
        });

        /*
        重置
        */
        $('#training_reset').click(function () {
            Swal.fire({
                title: "确定重置名单吗？",
                text: "名单重置！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendResetAjax();
                }
            });
        });

        /*
         删除
         */
        function training_del(attendUsersId, realName) {
            Swal.fire({
                title: "确定删除学生 '" + realName + "' 吗？",
                text: "学生删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(attendUsersId);
                }
            });
        }

        function del(attendUsersId) {
            sendDelAjax(attendUsersId);
        }

        function dels(attendUsersIds) {
            sendDelAjax(attendUsersIds.join(","));
        }

        /**
         * 删除ajax
         * @param attendUsersId
         */
        function sendDelAjax(attendUsersId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {attendUsersIds: attendUsersId, trainingReleaseId: page_param.paramTrainingReleaseId},
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

        /**
         * 重置ajax
         */
        function sendResetAjax() {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().reset,
                data: {
                    trainingAttendId: page_param.paramTrainingAttendId,
                    trainingReleaseId: page_param.paramTrainingReleaseId
                },
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

        /**
         * 全勤ajax
         */
        function sendAllOkAjax() {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().all_ok,
                data: {
                    trainingAttendId: page_param.paramTrainingAttendId,
                    trainingReleaseId: page_param.paramTrainingReleaseId
                },
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

        /*
       备注
       */
        function remark(attendUsersId, remark) {
            $('#editRemark').text(remark);
            $('#editAttendUsersId').val(attendUsersId);
            $('#remarkModal').modal('show');
        }

        $(button_id.toRemark.id).click(function () {
            tools.buttonLoading(button_id.toRemark.id, button_id.toRemark.tip);
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().remark,
                data: {
                    attendUsersId: $('#editAttendUsersId').val(),
                    remark: $('#editRemark').val(),
                    trainingReleaseId: page_param.paramTrainingReleaseId
                },
                success: function (data) {
                    tools.buttonEndLoading(button_id.toRemark.id, button_id.toRemark.text);
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        $('#remarkModal').modal('hide');
                        myTable.ajax.reload();
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.toRemark.id, button_id.toRemark.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        });

        $('#export_xls').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xls'
            };
            window.location.href = encodeURI(getAjaxUrl().export_data_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        $('#export_xlsx').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xlsx'
            };
            window.location.href = encodeURI(getAjaxUrl().export_data_url + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        /*
       状态
       */
        function operate(attendUsersId, student, operate) {
            $('#student').val(student);
            var v = Number(operate);
            if (v === 0) {
                $('#operate').val('缺席');
            } else if (v === 1) {
                $('#operate').val('请假');
            } else if (v === 2) {
                $('#operate').val('迟到');
            } else if (v === 3) {
                $('#operate').val('正常');
            }
            $('#operateAttendUsersId').val(attendUsersId);
            $('#operateModal').modal('show');
        }

        $('#absent').click(function () {
            toOperate(0);
        });

        $('#leave').click(function () {
            toOperate(1);
        });

        $('#late').click(function () {
            toOperate(2);
        });

        $('#sign').click(function () {
            toOperate(3);
        });

        function toOperate(operate) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().operate,
                data: {
                    attendUsersId: $('#operateAttendUsersId').val(),
                    operate: operate,
                    trainingReleaseId: page_param.paramTrainingReleaseId
                },
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        $('#operateModal').modal('hide');
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