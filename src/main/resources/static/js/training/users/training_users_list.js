//# sourceURL=training_users_list.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4",
        "check.all", "jquery.address", "messenger"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val(),
            canOperator: $('#canOperator').val()
        };

        /*
         参数
        */
        var param = {
            realName: '',
            studentNumber: '',
            username: '',
            trainingReleaseId: page_param.paramTrainingReleaseId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'TRAINING_USERS_LIST_REAL_NAME_SEARCH',
            STUDENT_NUMBER: 'TRAINING_USERS_LIST_STUDENT_NUMBER_SEARCH',
            USERNAME: 'TRAINING_USERS_LIST_USERNAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/training/users/data',
                export_data_url: web_path + '/web/training/users/export',
                del: web_path + '/web/training/users/delete',
                save: web_path + '/web/training/users/save',
                remark: web_path + '/web/training/users/remark',
                reset: web_path + '/web/training/users/reset',
                page: '/web/menu/training/users'
            };
        }

        navActive(getAjaxUrl().page);

        var button_id = {
            addStudent: {
                tip: '添加中...',
                text: '确定',
                id: '#addStudent'
            },
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
                {"data": "username"},
                {"data": "mobile"},
                {"data": "email"},
                {"data": "sex"},
                {"data": "remark"},
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
                        return '<input type="checkbox" value="' + c.trainingUsersId + '" name="check"/>';
                    }
                },
                {
                    targets: 10,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = null;
                        var html = '<i class="fa fa-lock"></i>';

                        if (Number(page_param.canOperator) === 1) {
                            context =
                                {
                                    func: [
                                        {
                                            "name": "删除",
                                            "css": "del",
                                            "type": "danger",
                                            "id": c.trainingUsersId,
                                            "student": c.realName,
                                            "remark": c.remark
                                        }, {
                                            "name": "备注",
                                            "css": "remark",
                                            "type": "light",
                                            "id": c.trainingUsersId,
                                            "student": c.realName,
                                            "remark": c.remark
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
            global_button += '<button type="button" id="training_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
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
                username: '#search_username'
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
            param.username = $(getParamId().username).val();

            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.USERNAME, param.username);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var username = null;
            if (typeof(Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                username = sessionStorage.getItem(webStorageKey.USERNAME);
            }
            if (realName !== null) {
                param.realName = realName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (username !== null) {
                param.username = username;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            var username = null;
            if (typeof(Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                username = sessionStorage.getItem(webStorageKey.USERNAME);
            }
            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (username !== null) {
                $(getParamId().username).val(username);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().username).val('');
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

        $(getParamId().username).keyup(function (event) {
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
        $('#training_add').click(function () {
            $('#addModal').modal('show');
        });

        /*
         批量删除
         */
        $('#training_dels').click(function () {
            var trainingUsersIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                trainingUsersIds.push($(ids[i]).val());
            }

            if (trainingUsersIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的学生吗？",
                    text: "学生删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(trainingUsersIds);
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
        function training_del(trainingUsersId, realName) {
            Swal.fire({
                title: "确定删除学生 '" + realName + "' 吗？",
                text: "学生删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(trainingUsersId);
                }
            });
        }

        function del(trainingUsersId) {
            sendDelAjax(trainingUsersId);
        }

        function dels(trainingUsersIds) {
            sendDelAjax(trainingUsersIds.join(","));
        }

        /**
         * 删除ajax
         * @param trainingUsersId
         */
        function sendDelAjax(trainingUsersId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {trainingUsersIds: trainingUsersId, trainingReleaseId: page_param.paramTrainingReleaseId},
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
                data: {trainingReleaseId: page_param.paramTrainingReleaseId},
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

        var add_param_id = {
            studentNumber: '#addStudentNumber',
            remark: '#addRemark'
        };

        $(button_id.addStudent.id).click(function () {
            validStudentNumber();
        });

        function validStudentNumber() {
            var studentNumber = _.trim($(add_param_id.studentNumber).val());

            if (studentNumber.length <= 0 || studentNumber.length > 20) {
                tools.validErrorDom(add_param_id.studentNumber, '学号20个字符以内');
            } else {
                tools.validSuccessDom(add_param_id.studentNumber);
                sendAddAjax();
            }
        }

        function sendAddAjax() {
            tools.buttonLoading(button_id.addStudent.id, button_id.addStudent.tip);
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().save,
                data: {
                    trainingReleaseId: page_param.paramTrainingReleaseId,
                    studentNumber: $(add_param_id.studentNumber).val(),
                    remark: $(add_param_id.remark).val()
                },
                success: function (data) {
                    tools.buttonEndLoading(button_id.addStudent.id, button_id.addStudent.text);
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        $('#addModal').modal('hide');
                        myTable.ajax.reload();
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.addStudent.id, button_id.addStudent.text);
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
        function remark(trainingUsersId, remark) {
            $('#editRemark').text(remark);
            $('#editTrainingUsersId').val(trainingUsersId);
            $('#remarkModal').modal('show');
        }

        $(button_id.toRemark.id).click(function () {
            tools.buttonLoading(button_id.toRemark.id, button_id.toRemark.tip);
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().remark,
                data: $('#remark_form').serialize(),
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
    });