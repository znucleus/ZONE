//# sourceURL=roster_review.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        var page_param = {
            rosterReleaseId: $('#rosterReleaseId').val()
        };

        /*
         参数
        */
        var param = {
            realName: '',
            studentNumber: '',
            organizeName: '',
            phoneNumber: '',
            sex: '',
            dormitoryNumber: '',
            rosterReleaseId: page_param.rosterReleaseId
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            REAL_NAME: 'CAMPUS_ROSTER_REVIEW_REAL_NAME_SEARCH_' + page_param.rosterReleaseId,
            STUDENT_NUMBER: 'CAMPUS_ROSTER_REVIEW_STUDENT_NUMBER_SEARCH_' + page_param.rosterReleaseId,
            ORGANIZE_NAME: 'CAMPUS_ROSTER_REVIEW_ORGANIZE_NAME_SEARCH_' + page_param.rosterReleaseId,
            PHONE_NUMBER: 'CAMPUS_ROSTER_REVIEW_PHONE_NUMBER_SEARCH_' + page_param.rosterReleaseId,
            SEX: 'CAMPUS_ROSTER_REVIEW_SEX_SEARCH_' + page_param.rosterReleaseId,
            DORMITORY_NUMBER: 'CAMPUS_ROSTER_REVIEW_DORMITORY_NUMBER_SEARCH_' + page_param.rosterReleaseId
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/campus/roster/review/data',
                del: web_path + '/web/campus/roster/review/delete',
                export_data: web_path + '/web/campus/roster/review/data/export',
                page: '/web/menu/campus/roster'
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
                localStorage.setItem('CAMPUS_ROSTER_REVIEW_' + page_param.rosterReleaseId + "_" + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('CAMPUS_ROSTER_REVIEW_' + page_param.rosterReleaseId + "_" + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[40, 'desc']],// 排序
            "ajax": {
                "url": getAjaxUrl().data,
                "type": "POST",
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
                {"data": "studentNumber"},
                {"data": "realName"},
                {"data": "namePinyin"},
                {"data": "sex"},
                {"data": "birthday"},
                {"data": "idCard"},
                {"data": "politicalLandscapeName"},
                {"data": "nationName"},
                {"data": "organizeName"},
                {"data": "province"},
                {"data": "nativePlace"},
                {"data": "region"},
                {"data": "busSection"},
                {"data": "parentName"},
                {"data": "parentContactPhone"},
                {"data": "parentContactAddress"},
                {"data": "zipCode"},
                {"data": "phoneNumber"},
                {"data": "email"},
                {"data": "candidatesType"},
                {"data": "isDeformedMan"},
                {"data": "deformedManCode"},
                {"data": "isMilitaryServiceRegistration"},
                {"data": "isProvideLoan"},
                {"data": "universityPosition"},
                {"data": "isPoorStudents"},
                {"data": "poorStudentsType"},
                {"data": "isStayOutside"},
                {"data": "dormitoryNumber"},
                {"data": "stayOutsideType"},
                {"data": "stayOutsideAddress"},
                {"data": "leagueMemberJoinDate"},
                {"data": "isRegisteredVolunteers"},
                {"data": "isOkLeagueMembership"},
                {"data": "applyPartyMembershipDate"},
                {"data": "becomeActivistsDate"},
                {"data": "becomeProbationaryPartyMemberDate"},
                {"data": "joiningPartyDate"},
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
                        return '<input type="checkbox" value="' + c.rosterDataId + '" name="check"/>';
                    }
                },
                {
                    targets: 21,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.candidatesType){
                            var n = Number(c.candidatesType);
                            if (n === 0) {
                                v = '城镇应届';
                            } else if (n === 1) {
                                v = '城市往届';
                            } else if (n === 2) {
                                v = '农村应届';
                            } else if (n === 3) {
                                v = '农村往届';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 22,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isDeformedMan){
                            var n = Number(c.isDeformedMan);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 24,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isMilitaryServiceRegistration){
                            var n = Number(c.isMilitaryServiceRegistration);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 25,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isProvideLoan){
                            var n = Number(c.isProvideLoan);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 27,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isPoorStudents){
                            var n = Number(c.isPoorStudents);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 28,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.poorStudentsType){
                            var n = Number(c.poorStudentsType);
                            if (n === 0) {
                                v = '一般困难';
                            } else if (n === 1) {
                                v = '贫困';
                            } else if (n === 3) {
                                v = '特困';
                            } else if (n === 4) {
                                v = '建档立卡户';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 29,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isStayOutside){
                            var n = Number(c.isStayOutside);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            } 
                        }
                        return v;
                    }
                },
                {
                    targets: 31,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.stayOutsideType){
                            var n = Number(c.stayOutsideType);
                            if (n === 0) {
                                v = '住自家';
                            } else if (n === 1) {
                                v = '亲戚家';
                            } else if (n === 2) {
                                v = '同学家';
                            } else if (n === 3) {
                                v = '租房';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 34,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isRegisteredVolunteers){
                            var n = Number(c.isRegisteredVolunteers);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 35,
                    render: function (a, b, c, d) {
                        var v = '';
                        if(null != c.isOkLeagueMembership){
                            var n = Number(c.isOkLeagueMembership);
                            if (n === 0) {
                                v = '否';
                            } else if (n === 1) {
                                v = '是';
                            }
                        }
                        return v;
                    }
                },
                {
                    targets: 41,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "删除",
                                    "css": "del",
                                    "type": "danger",
                                    "id": c.rosterDataId,
                                    "student": c.realName
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
                tableElement.delegate('.del', "click", function () {
                    student_del($(this).attr('data-id'), $(this).attr('data-student'));
                });
                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="student_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                realName: '#search_real_name',
                studentNumber: '#search_student_number',
                organizeName: '#search_organize_name',
                phoneNumber: '#search_phone_number',
                sex: '#search_sex',
                dormitoryNumber: '#search_dormitory_number'
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
            param.phoneNumber = $(getParamId().phoneNumber).val();
            param.sex = $(getParamId().sex).val();
            param.dormitoryNumber = $(getParamId().dormitoryNumber).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, param.studentNumber);
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, param.organizeName);
                sessionStorage.setItem(webStorageKey.PHONE_NUMBER, param.phoneNumber);
                sessionStorage.setItem(webStorageKey.SEX, param.sex);
                sessionStorage.setItem(webStorageKey.DORMITORY_NUMBER, param.dormitoryNumber);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var organizeName = null;
            var phoneNumber = null;
            var sex = null;
            var dormitoryNumber = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                phoneNumber = sessionStorage.getItem(webStorageKey.PHONE_NUMBER);
                sex = sessionStorage.getItem(webStorageKey.SEX);
                dormitoryNumber = sessionStorage.getItem(webStorageKey.DORMITORY_NUMBER);
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

            if (phoneNumber !== null) {
                param.phoneNumber = phoneNumber;
            }

            if (sex !== null) {
                param.sex = sex;
            }

            if (dormitoryNumber !== null) {
                param.dormitoryNumber = dormitoryNumber;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            var organizeName = null;
            var phoneNumber = null;
            var sex = null;
            var dormitoryNumber = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                phoneNumber = sessionStorage.getItem(webStorageKey.PHONE_NUMBER);
                sex = sessionStorage.getItem(webStorageKey.SEX);
                dormitoryNumber = sessionStorage.getItem(webStorageKey.DORMITORY_NUMBER);
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

            if (phoneNumber !== null) {
                $(getParamId().phoneNumber).val(phoneNumber);
            }

            if (sex !== null) {
                $(getParamId().sex).val(sex);
            }

            if (dormitoryNumber !== null) {
                $(getParamId().dormitoryNumber).val(dormitoryNumber);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().studentNumber).val('');
            $(getParamId().organizeName).val('');
            $(getParamId().phoneNumber).val('');
            $(getParamId().sex).val('');
            $(getParamId().dormitoryNumber).val('');
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

        $(getParamId().phoneNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().sex).change(function () {
            initParam();
            myTable.ajax.reload();
        });

        $(getParamId().dormitoryNumber).keyup(function (event) {
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
         批量删除
         */
        $('#student_dels').click(function () {
            var rosterDataIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                rosterDataIds.push($(ids[i]).val());
            }

            if (rosterDataIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的学生吗？",
                    text: "学生删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(rosterDataIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的学生!");
            }
        });

        /*
         删除
         */
        function student_del(rosterDataId, name) {
            Swal.fire({
                title: "确定删除学生 '" + name + "' 吗？",
                text: "学生删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(rosterDataId);
                }
            });
        }

        function del(rosterDataId) {
            sendDelAjax(rosterDataId);
        }

        function dels(rosterDataIds) {
            sendDelAjax(rosterDataIds.join(","));
        }

        /**
         * 删除ajax
         * @param rosterDataId
         */
        function sendDelAjax(rosterDataId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {rosterDataIds: rosterDataId, rosterReleaseId: page_param.rosterReleaseId},
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

        $('#export_xls').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xls'
            };
            window.location.href = encodeURI(getAjaxUrl().export_data + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });

        $('#export_xlsx').click(function () {
            initParam();
            var searchParam = JSON.stringify(getParam());
            var exportFile = {
                fileName: $('#export_file_name').val(),
                ext: 'xlsx'
            };
            window.location.href = encodeURI(getAjaxUrl().export_data + "?extra_search=" + searchParam + "&export_info=" + JSON.stringify(exportFile));
        });
    });