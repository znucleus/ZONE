//# sourceURL=internship_statistical_submitted.js
require(["jquery", "handlebars", "nav.active", "responsive.bootstrap4", "jquery.address", "messenger", "select2-zh-CN"],
    function ($, Handlebars, navActive) {

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
            internshipApplyState: '',
            internshipReleaseId: page_param.paramInternshipReleaseId,
            dataRange: 1
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'INTERNSHIP_STATISTICAL_SUBMITTED_STUDENT_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_STATISTICAL_SUBMITTED_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            ORGANIZE_ID: 'INTERNSHIP_STATISTICAL_SUBMITTED_ORGANIZE_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            INTERNSHIP_APPLY_STATE: 'INTERNSHIP_STATISTICAL_SUBMITTED_INTERNSHIP_APPLY_STATE_SEARCH_' + page_param.paramInternshipReleaseId
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
                change_history: '/web/internship/statistical/record/apply',
                change_company: '/web/internship/statistical/record/company',
                page: '/web/menu/internship/statistical'
            };
        }

        var init_configure = {
            init_organize: false
        };

        /**
         * 状态码表
         * @param state 状态码
         * @returns {string}
         */
        function internshipApplyStateCode(state) {
            var msg = '';
            switch (state) {
                case 0:
                    msg = '已保存';
                    break;
                case 1:
                    msg = '审核中';
                    break;
                case 2:
                    msg = '已通过';
                    break;
                case 3:
                    msg = '未通过';
                    break;
                case 4:
                    msg = '基本信息变更审核中';
                    break;
                case 5:
                    msg = '基本信息变更填写中';
                    break;
                case 6:
                    msg = '单位信息变更申请中';
                    break;
                case 7:
                    msg = '单位信息变更填写中';
                    break;
            }
            return msg;
        }

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
            stateSaveCallback: function (settings, data) {
                localStorage.setItem('INTERNSHIP_STATISTICAL_SUBMITTED_' + page_param.paramInternshipReleaseId + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('INTERNSHIP_STATISTICAL_SUBMITTED_' + page_param.paramInternshipReleaseId + settings.sInstance))
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
                {"data": "scienceName"},
                {"data": "organizeName"},
                {"data": "internshipApplyState"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 5,
                    orderable: false,
                    render: function (a, b, c, d) {
                        var context =
                            {
                                func: [
                                    {
                                        "name": "申请记录",
                                        "css": "apply_record",
                                        "type": "primary",
                                        "studentId": c.studentId,
                                        "internshipReleaseId": c.internshipReleaseId
                                    },
                                    {
                                        "name": "单位变更记录",
                                        "css": "change_record",
                                        "type": "primary",
                                        "studentId": c.studentId,
                                        "internshipReleaseId": c.internshipReleaseId
                                    }
                                ]
                            };
                        return template(context);
                    }
                },
                {
                    targets: 4,
                    render: function (a, b, c, d) {
                        return internshipApplyStateCode(c.internshipApplyState);
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
                tableElement.delegate('.apply_record', "click", function () {
                    changeHistory($(this).attr('data-internshipReleaseId'), $(this).attr('data-studentId'));
                });

                tableElement.delegate('.change_record', "click", function () {
                    changeCompanyHistory($(this).attr('data-internshipReleaseId'), $(this).attr('data-studentId'));
                });

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
                    if (typeof (Storage) !== "undefined") {
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
                organizeId: '#search_organize',
                internshipApplyState: '#select_internship_apply_state'
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
            param.internshipApplyState = $(getParamId().internshipApplyState).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.ORGANIZE_ID, param.organizeId != null ? param.organizeId : '');
                sessionStorage.setItem(webStorageKey.INTERNSHIP_APPLY_STATE, param.internshipApplyState);
            }
        }

        /*
        初始化搜索内容
        */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var organizeId = null;
            var internshipApplyState = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeId = sessionStorage.getItem(webStorageKey.ORGANIZE_ID);
                internshipApplyState = sessionStorage.getItem(webStorageKey.INTERNSHIP_APPLY_STATE);
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

            if (internshipApplyState !== null) {
                param.internshipApplyState = internshipApplyState;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            var internshipApplyState = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                internshipApplyState = sessionStorage.getItem(webStorageKey.INTERNSHIP_APPLY_STATE);
            }
            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (internshipApplyState !== null) {
                $(getParamId().internshipApplyState).val(internshipApplyState);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().studentNumber).val('');
            organizeSelect2.val('').trigger("change");
            $(getParamId().internshipApplyState).val('');
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

        $(getParamId().internshipApplyState).change(function () {
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


        function changeHistory(internshipReleaseId, studentId) {
            $.address.value(getAjaxUrl().change_history + '/' + internshipReleaseId + '/' + studentId);
        }

        function changeCompanyHistory(internshipReleaseId, studentId) {
            $.address.value(getAjaxUrl().change_company + '/' + internshipReleaseId + '/' + studentId);
        }
    });