//# sourceURL=internship_distribution_list.js
require(["jquery", "nav.active", "sweetalert2", "responsive.bootstrap4", "jquery.address", "messenger"],
    function ($, navActive, Swal) {

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val()
        };

        /*
        参数
        */
        var param = {
            studentUsername: '',
            staffUsername: '',
            studentNumber: '',
            staffNumber: '',
            username: '',
            assigner: '',
            internshipReleaseId: page_param.paramInternshipReleaseId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            STUDENT_USERNAME: 'INTERNSHIP_DISTRIBUTION_LOOK_STUDENT_USERNAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STAFF_USERNAME: 'INTERNSHIP_DISTRIBUTION_LOOK_STAFF_USERNAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_DISTRIBUTION_LOOK_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            STAFF_NUMBER: 'INTERNSHIP_DISTRIBUTION_LOOK_STAFF_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            USERNAME: 'INTERNSHIP_DISTRIBUTION_LOOK_USERNAME_SEARCH_' + page_param.paramInternshipReleaseId,
            ASSIGNER: 'INTERNSHIP_DISTRIBUTION_LOOK_ASSIGNER_SEARCH_' + page_param.paramInternshipReleaseId
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/teacher_distribution/data',
                delete_not_apply: web_path + '/web/internship/teacher_distribution/distribution/delete_not_apply',
                export_data_url: web_path + '/web/internship/teacher_distribution/export',
                page: '/web/menu/internship/teacher_distribution'
            };
        }

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        var serialNumber = 0;// 序号
        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
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
                    serialNumber = 0;
                }
            },
            "columns": [
                {"data": null},
                {"data": "studentRealName"},
                {"data": "studentUsername"},
                {"data": "studentNumber"},
                {"data": "staffRealName"},
                {"data": "staffUsername"},
                {"data": "staffNumber"},
                {"data": "assigner"},
                {"data": "username"}
            ],
            columnDefs: [
                {
                    targets: 0,
                    orderable: false,
                    render: function (a, b, c, d) {
                        serialNumber++;
                        return serialNumber;
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
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="delete_not_apply" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>删除未申请学生分配</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                studentUsername: '#search_student_username',
                staffUsername: '#search_staff_username',
                studentNumber: '#search_student_number',
                staffNumber: '#search_staff_number',
                username: '#search_username',
                assigner: '#search_assigner'
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
            param.studentUsername = $(getParamId().studentUsername).val();
            param.staffUsername = $(getParamId().staffUsername).val();
            param.studentNumber = $(getParamId().studentNumber).val();
            param.staffNumber = $(getParamId().staffNumber).val();
            param.username = $(getParamId().username).val();
            param.assigner = $(getParamId().assigner).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.STUDENT_USERNAME, param.studentUsername);
                sessionStorage.setItem(webStorageKey.STAFF_USERNAME, param.staffUsername);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.STAFF_NUMBER, param.staffNumber);
                sessionStorage.setItem(webStorageKey.USERNAME, param.username);
                sessionStorage.setItem(webStorageKey.ASSIGNER, param.assigner);
            }
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var studentUsername = null;
            var staffUsername = null;
            var studentNumber = null;
            var staffNumber = null;
            var username = null;
            var assigner = null;
            if (typeof (Storage) !== "undefined") {
                studentUsername = sessionStorage.getItem(webStorageKey.STUDENT_USERNAME);
                staffUsername = sessionStorage.getItem(webStorageKey.STAFF_USERNAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                staffNumber = sessionStorage.getItem(webStorageKey.STAFF_NUMBER);
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                assigner = sessionStorage.getItem(webStorageKey.ASSIGNER);
            }
            if (studentUsername !== null) {
                param.studentUsername = studentUsername;
            }

            if (staffUsername !== null) {
                param.staffUsername = staffUsername;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (staffNumber !== null) {
                param.staffNumber = staffNumber;
            }

            if (username !== null) {
                param.username = username;
            }

            if (assigner !== null) {
                param.assigner = assigner;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var studentUsername = null;
            var staffUsername = null;
            var studentNumber = null;
            var staffNumber = null;
            var username = null;
            var assigner = null;
            if (typeof (Storage) !== "undefined") {
                studentUsername = sessionStorage.getItem(webStorageKey.STUDENT_USERNAME);
                staffUsername = sessionStorage.getItem(webStorageKey.STAFF_USERNAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                staffNumber = sessionStorage.getItem(webStorageKey.STAFF_NUMBER);
                username = sessionStorage.getItem(webStorageKey.USERNAME);
                assigner = sessionStorage.getItem(webStorageKey.ASSIGNER);
            }
            if (studentUsername !== null) {
                $(getParamId().studentUsername).val(studentUsername);
            }

            if (staffUsername !== null) {
                $(getParamId().staffUsername).val(staffUsername);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (staffNumber !== null) {
                $(getParamId().staffNumber).val(staffNumber);
            }

            if (username !== null) {
                $(getParamId().username).val(username);
            }

            if (assigner !== null) {
                $(getParamId().assigner).val(assigner);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().studentUsername).val('');
            $(getParamId().staffUsername).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().staffNumber).val('');
            $(getParamId().username).val('');
            $(getParamId().assigner).val('');
        }

        $(getParamId().studentUsername).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().staffUsername).keyup(function (event) {
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

        $(getParamId().staffNumber).keyup(function (event) {
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

        $(getParamId().assigner).keyup(function (event) {
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
       删除未申请学生分配
       */
        $('#delete_not_apply').click(function () {
            Swal.fire({
                title: "确定删除未申请学生的分配吗？",
                text: "未申请学生删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendDeleteNotApplyAjax();
                }
            });
        });

        /**
         * 发送删除未申请学生分配
         */
        function sendDeleteNotApplyAjax() {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().delete_not_apply,
                data: {id: page_param.paramInternshipReleaseId},
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