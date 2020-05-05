//# sourceURL=internship_statistical_info.js
require(["jquery", "nav.active", "responsive.bootstrap4", "jquery.address", "messenger"],
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
            organizeName: '',
            mobile: '',
            headmaster: '',
            schoolGuidanceTeacher: '',
            internshipReleaseId: page_param.paramInternshipReleaseId
        };

        /*
       web storage key.
       */
        var webStorageKey = {
            REAL_NAME: 'INTERNSHIP_STATISTICS_INFO_REAL_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_STATISTICS_INFO_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            ORGANIZE_NAME: 'INTERNSHIP_STATISTICS_INFO_COLLEGE_CLASS_SEARCH_' + page_param.paramInternshipReleaseId,
            MOBILE: 'INTERNSHIP_STATISTICS_INFO_MOBILE_SEARCH_' + page_param.paramInternshipReleaseId,
            HEADMASTER: 'INTERNSHIP_STATISTICS_INFO_HEADMASTER_SEARCH_' + page_param.paramInternshipReleaseId,
            SCHOOL_GUIDANCE_TEACHER: 'INTERNSHIP_STATISTICS_INFO_SCHOOL_GUIDANCE_TEACHER_SEARCH_' + page_param.paramInternshipReleaseId
        };

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/statistical/info/data',
                export_url: web_path + '/web/internship/statistical/info/export',
                page: '/web/menu/internship/statistical'
            };
        }

        /**
         * byte to boolean
         * @param b
         * @returns {*}
         */
        function byteToBoolean(b) {
            if (b === 1) {
                return '已交';
            } else {
                return '未交';
            }
        }

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
                }
            },
            "columns": [
                {"data": "studentName"},
                {"data": "organizeName"},
                {"data": "studentSex"},
                {"data": "studentNumber"},
                {"data": "mobile"},
                {"data": "qqMailbox"},
                {"data": "parentContactPhone"},
                {"data": "headmaster"},
                {"data": "headmasterTel"},
                {"data": "companyName"},
                {"data": "companyAddress"},
                {"data": "companyContact"},
                {"data": "companyMobile"},
                {"data": "schoolGuidanceTeacher"},
                {"data": "schoolGuidanceTeacherTel"},
                {"data": "startTime"},
                {"data": "endTime"},
                {"data": "commitmentBook"},
                {"data": "safetyResponsibilityBook"},
                {"data": "practiceAgreement"},
                {"data": "internshipApplication"},
                {"data": "practiceReceiving"},
                {"data": "securityEducationAgreement"},
                {"data": "parentalConsent"}
            ],
            columnDefs: [
                {
                    targets: 17,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.commitmentBook);
                    }
                },
                {
                    targets: 18,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.safetyResponsibilityBook);
                    }
                },
                {
                    targets: 19,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.practiceAgreement);
                    }
                },
                {
                    targets: 20,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.internshipApplication);
                    }
                },
                {
                    targets: 21,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.practiceReceiving);
                    }
                },
                {
                    targets: 22,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.securityEducationAgreement);
                    }
                },
                {
                    targets: 23,
                    render: function (a, b, c, d) {
                        return byteToBoolean(c.parentalConsent);
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

        var global_button = '<button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                realName: '#search_real_name',
                studentNumber: '#search_student_number',
                organizeName: '#search_organize_name',
                mobile: '#search_mobile',
                headmaster: '#search_headmaster',
                schoolGuidanceTeacher: '#search_school_guidance_teacher'
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
            param.organizeName = $(getParamId().organizeName).val();
            param.mobile = $(getParamId().mobile).val();
            param.headmaster = $(getParamId().headmaster).val();
            param.schoolGuidanceTeacher = $(getParamId().schoolGuidanceTeacher).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, param.organizeName);
                sessionStorage.setItem(webStorageKey.MOBILE, param.mobile);
                sessionStorage.setItem(webStorageKey.HEADMASTER, param.headmaster);
                sessionStorage.setItem(webStorageKey.SCHOOL_GUIDANCE_TEACHER, param.schoolGuidanceTeacher);
            }
        }

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var organizeName = null;
            var mobile = null;
            var headmaster = null;
            var schoolGuidanceTeacher = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                mobile = sessionStorage.getItem(webStorageKey.MOBILE);
                headmaster = sessionStorage.getItem(webStorageKey.HEADMASTER);
                schoolGuidanceTeacher = sessionStorage.getItem(webStorageKey.SCHOOL_GUIDANCE_TEACHER);
            }
            if (realName !== null) {
                param.realName = realName;
            }

            if (studentNumber !== null) {
                param.studentNumber = studentNumber;
            }

            if (organizeName !== null) {
                param.organizeName = organizeName;
            }

            if (mobile !== null) {
                param.mobile = mobile;
            }

            if (headmaster !== null) {
                param.headmaster = headmaster;
            }

            if (schoolGuidanceTeacher !== null) {
                param.schoolGuidanceTeacher = schoolGuidanceTeacher;
            }
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            var organizeName = null;
            var mobile = null;
            var headmaster = null;
            var schoolGuidanceTeacher = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                mobile = sessionStorage.getItem(webStorageKey.MOBILE);
                headmaster = sessionStorage.getItem(webStorageKey.HEADMASTER);
                schoolGuidanceTeacher = sessionStorage.getItem(webStorageKey.SCHOOL_GUIDANCE_TEACHER);
            }
            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (studentNumber !== null) {
                $(getParamId().studentNumber).val(studentNumber);
            }

            if (organizeName !== null) {
                $(getParamId().organizeName).val(organizeName);
            }

            if (mobile !== null) {
                $(getParamId().mobile).val(mobile);
            }

            if (headmaster !== null) {
                $(getParamId().headmaster).val(headmaster);
            }

            if (schoolGuidanceTeacher !== null) {
                $(getParamId().schoolGuidanceTeacher).val(schoolGuidanceTeacher);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().organizeName).val('');
            $(getParamId().mobile).val('');
            $(getParamId().headmaster).val('');
            $(getParamId().schoolGuidanceTeacher).val('');
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

        $(getParamId().organizeName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().mobile).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().headmaster).keyup(function (event) {
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
    });