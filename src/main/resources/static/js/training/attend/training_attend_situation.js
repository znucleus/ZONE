//# sourceURL=training_attend_situation.js
require(["jquery", "nav.active", "responsive.bootstrap4", "jquery.address", "messenger", "flatpickr-zh"],
    function ($, navActive) {

        var page_param = {
            paramTrainingReleaseId: $('#paramTrainingReleaseId').val()
        };

        /*
         参数
        */
        var param = {
            realName: '',
            studentNumber: '',
            attendDate: '',
            operate: '',
            trainingReleaseId: page_param.paramTrainingReleaseId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'TRAINING_ATTEND_SITUATION_REAL_NAME_SEARCH' + page_param.paramTrainingReleaseId,
            STUDENT_NUMBER: 'TRAINING_ATTEND_SITUATION_STUDENT_NUMBER_SEARCH' + page_param.paramTrainingReleaseId,
            ATTEND_DATE: 'TRAINING_ATTEND_SITUATION_ATTEND_DATE_SEARCH' + page_param.paramTrainingReleaseId,
            OPERATE: 'TRAINING_ATTEND_SITUATION_OPERATE_SEARCH' + page_param.paramTrainingReleaseId
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/training/attend/situation/paging',
                export_data_url: web_path + '/web/training/attend/situation/export',
                page: '/web/menu/training/attend'
            };
        }

        navActive(getAjaxUrl().page);

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
            stateSaveCallback: function (settings, data) {
                localStorage.setItem('TRAINING_ATTEND_SITUATION_' + page_param.paramTrainingReleaseId + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('TRAINING_ATTEND_SITUATION_' + page_param.paramTrainingReleaseId + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[1, 'asc']],// 排序
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
                {"data": "realName"},
                {"data": "studentNumber"},
                {"data": "attendDate"},
                {"data": "organizeName"},
                {"data": "sex"},
                {"data": "operate"},
                {"data": "remark"}
            ],
            columnDefs: [
                {
                    targets: 5,
                    render: function (a, b, c, d) {
                        var v = '不存在';
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

        var global_button = '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                realName: '#search_real_name',
                studentNumber: '#search_student_number',
                attendDate: '#search_attend_date',
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
            param.attendDate = $(getParamId().attendDate).val();
            param.operate = $(getParamId().operate).val();

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.ATTEND_DATE, param.attendDate);
                sessionStorage.setItem(webStorageKey.OPERATE, param.operate);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var attendDate = null;
            var operate = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
                operate = sessionStorage.getItem(webStorageKey.OPERATE);
            }
            if (realName !== null) {
                param.realName = realName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (attendDate !== null) {
                param.attendDate = attendDate;
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
            var attendDate = null;
            var operate = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                attendDate = sessionStorage.getItem(webStorageKey.ATTEND_DATE);
                operate = sessionStorage.getItem(webStorageKey.OPERATE);
            }
            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (attendDate !== null) {
                $(getParamId().attendDate).val(attendDate);
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
            $(getParamId().attendDate).val('');
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

        $(getParamId().attendDate).flatpickr({
            "locale": "zh",
            "mode": "range",
            onClose: function () {
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