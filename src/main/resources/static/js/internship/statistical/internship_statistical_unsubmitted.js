//# sourceURL=internship_statistical_unsubmitted.js
require(["jquery", "nav.active", "responsive.bootstrap4", "jquery.address", "messenger", "select2-zh-CN"],
    function ($, navActive) {

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val()
        };

        /*
         参数
        */
        var param = {
            realName: '',
            studentNumber: '',
            organizeId: '',
            internshipReleaseId: page_param.paramInternshipReleaseId
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'INTERNSHIP_STATISTICAL_UNSUBMITTED_STUDENT_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_STATISTICAL_UNSUBMITTED_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            ORGANIZE_ID: 'INTERNSHIP_STATISTICAL_UNSUBMITTED_ORGANIZE_NAME_SEARCH_' + page_param.paramInternshipReleaseId
        };

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/statistical/data',
                obtain_organize_data: web_path + '/web/internship/statistical/organize',
                page: '/web/menu/internship/statistical'
            };
        }

        var init_configure = {
            init_organize: false
        };

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
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
                {"data": "scienceName"},
                {"data": "organizeName"}
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
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        var organizeSelect2 = null;

        initSearchOrganize();
        initSelect2();

        /**
         * 初始化班级数据
         */
        function initSearchOrganize() {
            $.get(getAjaxUrl().obtain_organize_data + "/" + page_param.paramInternshipReleaseId, function (data) {
                $(getParamId().organizeId).html('<option label="请选择班级"></option>');
                organizeSelect2 = $(getParamId().organizeId).select2({data: data.results});

                if (!init_configure.init_organize) {
                    if (typeof(Storage) !== "undefined") {
                        var organizeId = sessionStorage.getItem(webStorageKey.ORGANIZE_ID);
                        organizeSelect2.val(Number(organizeId)).trigger("change");
                    }
                    init_configure.init_organize = true;
                }
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /*
         参数id
         */
        function getParamId() {
            return {
                realName: '#search_real_name',
                studentNumber: '#search_student_number',
                organizeId: '#search_organize'
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
            param.organizeId = $(getParamId().organizeId).val();
            if (typeof(Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.ORGANIZE_ID, param.organizeId);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var organizeId = null;
            if (typeof(Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeId = sessionStorage.getItem(webStorageKey.ORGANIZE_ID);
            }
            if (realName !== null) {
                param.realName = realName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (organizeId !== null) {
                param.organizeId = organizeId;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            if (typeof(Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
            }
            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().studentNumber).val('');
            organizeSelect2.val('').trigger("change");
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


        $(getParamId().organizeId).on('select2:select', function (e) {
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
    });