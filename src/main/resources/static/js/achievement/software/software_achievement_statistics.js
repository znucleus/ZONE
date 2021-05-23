//# sourceURL=software_achievement_statistics.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            realName: '',
            sex: '',
            idCard: '',
            mobile: '',
            scienceName: '',
            schoolName: '',
            organizeName: '',
            level: '',
            examType: '',
            morningResults: '',
            afternoonResults: '',
            thesisResults: '',
            identificationResults: '',
            payment: '',
            examDate: '',
            dataScope:''
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            REAL_NAME: 'SOFTWARE_ACHIEVEMENT_STATISTICS_REAL_NAME_SEARCH',
            SEX: 'SOFTWARE_ACHIEVEMENT_STATISTICS_SEX_SEARCH',
            ID_CARD: 'SOFTWARE_ACHIEVEMENT_STATISTICS_ID_CARD_SEARCH',
            MOBILE: 'SOFTWARE_ACHIEVEMENT_STATISTICS_MOBILE_SEARCH',
            SCIENCE_NAME: 'SOFTWARE_ACHIEVEMENT_STATISTICS_SCIENCE_NAME_SEARCH',
            SCHOOL_NAME: 'SOFTWARE_ACHIEVEMENT_STATISTICS_SCHOOL_NAME_SEARCH',
            ORGANIZE_NAME: 'SOFTWARE_ACHIEVEMENT_STATISTICS_ORGANIZE_NAME_SEARCH',
            LEVEL: 'SOFTWARE_ACHIEVEMENT_STATISTICS_LEVEL_SEARCH',
            EXAM_TYPE: 'SOFTWARE_ACHIEVEMENT_STATISTICS_EXAM_TYPE_SEARCH',
            MORNING_RESULTS: 'SOFTWARE_ACHIEVEMENT_STATISTICS_MORNING_RESULTS_SEARCH',
            AFTERNOON_RESULTS: 'SOFTWARE_ACHIEVEMENT_STATISTICS_AFTERNOON_RESULTS_SEARCH',
            THESIS_RESULTS: 'SOFTWARE_ACHIEVEMENT_STATISTICS_THESIS_RESULTS_SEARCH',
            IDENTIFICATION_RESULTS: 'SOFTWARE_ACHIEVEMENT_STATISTICS_IDENTIFICATION_RESULTS_SEARCH',
            PAYMENT: 'SOFTWARE_ACHIEVEMENT_STATISTICS_PAYMENT_SEARCH',
            EXAM_DATE: 'SOFTWARE_ACHIEVEMENT_EXAM_DATE_SEARCH',
            DATA_SCOPE:'SOFTWARE_ACHIEVEMENT_DATA_SCOPE_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/achievement/software/statistics/paging',
                del: web_path + '/web/achievement/software/statistics/delete',
                add: '/web/achievement/software/statistics/add',
                edit: '/web/achievement/software/statistics/edit',
                page: '/web/menu/achievement/software/statistics'
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
                localStorage.setItem('SOFTWARE_ACHIEVEMENT_STATISTICS_' + settings.sInstance, JSON.stringify(data))
            },
            stateLoadCallback: function (settings) {
                return JSON.parse(localStorage.getItem('SOFTWARE_ACHIEVEMENT_STATISTICS_' + settings.sInstance))
            },
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[17, 'desc']],// 排序
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
                {"data": "sex"},
                {"data": "idCard"},
                {"data": "mobile"},
                {"data": "scienceName"},
                {"data": "schoolName"},
                {"data": "organizeName"},
                {"data": "level"},
                {"data": "examType"},
                {"data": "morningResults"},
                {"data": "afternoonResults"},
                {"data": "thesisResults"},
                {"data": "identificationResults"},
                {"data": "payment"},
                {"data": "examDate"},
                {"data": "createDateStr"},
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
                        return '<input type="checkbox" value="' + c.achievementId + '" name="check"/>';
                    }
                },
                {
                    targets: 19,
                    orderable: false,
                    render: function (a, b, c, d) {

                        var context = {
                            func: [
                                {
                                    "name": "编辑",
                                    "css": "edit",
                                    "type": "primary",
                                    "id": c.achievementId
                                },
                                {
                                    "name": "删除",
                                    "css": "del",
                                    "type": "danger",
                                    "id": c.achievementId
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
                    achievement_del($(this).attr('data-id'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var global_button = '<button type="button" id="achievement_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="achievement_dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                realName: '#search_real_name',
                sex: '#search_sex',
                idCard: '#search_id_card',
                mobile: '#search_mobile',
                scienceName: '#search_science',
                schoolName: '#search_school',
                organizeName: '#search_organize',
                level: '#search_level',
                examType: '#search_exam_type',
                morningResults: '#search_morning_results',
                afternoonResults: '#search_afternoon_results',
                thesisResults: '#search_thesis_results',
                identificationResults: '#search_identification_results',
                payment: '#search_payment',
                examDate: '#search_exam_date'
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
            param.sex = $(getParamId().sex).val();
            param.idCard = $(getParamId().idCard).val();
            param.mobile = $(getParamId().mobile).val();
            param.scienceName = $(getParamId().scienceName).val();
            param.schoolName = $(getParamId().schoolName).val();
            param.organizeName = $(getParamId().organizeName).val();
            param.level = $(getParamId().level).val();
            param.examType = $(getParamId().examType).val();
            param.morningResults = $(getParamId().morningResults).val();
            param.afternoonResults = $(getParamId().afternoonResults).val();
            param.thesisResults = $(getParamId().thesisResults).val();
            param.identificationResults = $(getParamId().identificationResults).val();
            param.payment = $(getParamId().payment).val();
            param.examDate = $(getParamId().examDate).val();
            if($('#pass').hasClass('active')){
                param.dataScope = 'PASS';
            } else {
                param.dataScope = 'FAIL';
            }

            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
                sessionStorage.setItem(webStorageKey.SEX, param.sex);
                sessionStorage.setItem(webStorageKey.ID_CARD, param.idCard);
                sessionStorage.setItem(webStorageKey.MOBILE, param.mobile);
                sessionStorage.setItem(webStorageKey.SCIENCE_NAME, param.scienceName);
                sessionStorage.setItem(webStorageKey.SCHOOL_NAME, param.schoolName);
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, param.organizeName);
                sessionStorage.setItem(webStorageKey.LEVEL, param.level);
                sessionStorage.setItem(webStorageKey.EXAM_TYPE, param.examType);
                sessionStorage.setItem(webStorageKey.MORNING_RESULTS, param.morningResults);
                sessionStorage.setItem(webStorageKey.AFTERNOON_RESULTS, param.afternoonResults);
                sessionStorage.setItem(webStorageKey.THESIS_RESULTS, param.thesisResults);
                sessionStorage.setItem(webStorageKey.IDENTIFICATION_RESULTS, param.identificationResults);
                sessionStorage.setItem(webStorageKey.PAYMENT, param.payment);
                sessionStorage.setItem(webStorageKey.EXAM_DATE, param.examDate);
                sessionStorage.setItem(webStorageKey.DATA_SCOPE, param.dataScope);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var realName = null;
            var sex = null;
            var idCard = null;
            var mobile = null;
            var scienceName = null;
            var schoolName = null;
            var organizeName = null;
            var level = null;
            var examType = null;
            var morningResults = null;
            var afternoonResults = null;
            var thesisResults = null;
            var identificationResults = null;
            var payment = null;
            var examDate = null;
            var dataScope = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                sex = sessionStorage.getItem(webStorageKey.SEX);
                idCard = sessionStorage.getItem(webStorageKey.ID_CARD);
                mobile = sessionStorage.getItem(webStorageKey.MOBILE);
                scienceName = sessionStorage.getItem(webStorageKey.SCIENCE_NAME);
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                level = sessionStorage.getItem(webStorageKey.LEVEL);
                examType = sessionStorage.getItem(webStorageKey.EXAM_TYPE);
                morningResults = sessionStorage.getItem(webStorageKey.MORNING_RESULTS);
                afternoonResults = sessionStorage.getItem(webStorageKey.AFTERNOON_RESULTS);
                thesisResults = sessionStorage.getItem(webStorageKey.THESIS_RESULTS);
                identificationResults = sessionStorage.getItem(webStorageKey.IDENTIFICATION_RESULTS);
                payment = sessionStorage.getItem(webStorageKey.PAYMENT);
                examDate = sessionStorage.getItem(webStorageKey.EXAM_DATE);
                dataScope = sessionStorage.getItem(webStorageKey.DATA_SCOPE);
            }
            if (realName !== null) {
                param.realName = realName;
            }

            if (sex !== null) {
                param.sex = sex;
            }

            if (idCard !== null) {
                param.idCard = idCard;
            }

            if (mobile !== null) {
                param.mobile = mobile;
            }

            if (scienceName !== null) {
                param.scienceName = scienceName;
            }

            if (schoolName !== null) {
                param.schoolName = schoolName;
            }

            if (organizeName !== null) {
                param.organizeName = organizeName;
            }

            if (level !== null) {
                param.level = level;
            }

            if (examType !== null) {
                param.examType = examType;
            }

            if (morningResults !== null) {
                param.morningResults = morningResults;
            }

            if (afternoonResults !== null) {
                param.afternoonResults = afternoonResults;
            }

            if (thesisResults !== null) {
                param.thesisResults = thesisResults;
            }

            if (identificationResults !== null) {
                param.identificationResults = identificationResults;
            }

            if (payment !== null) {
                param.payment = payment;
            }

            if (examDate !== null) {
                param.examDate = examDate;
            }

            if (dataScope !== null) {
                if(dataScope === 'PASS'){
                    $('#pass').addClass('active');
                    $('#failed').removeClass('active');
                    param.dataScope = 'PASS';
                } else {
                    $('#pass').removeClass('active');
                    $('#failed').addClass('active');
                    param.dataScope = 'FAIL';
                }
            } else {
                $('#pass').addClass('active');
                $('#failed').removeClass('active');
                param.dataScope = 'PASS';
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var sex = null;
            var idCard = null;
            var mobile = null;
            var scienceName = null;
            var schoolName = null;
            var organizeName = null;
            var level = null;
            var examType = null;
            var morningResults = null;
            var afternoonResults = null;
            var thesisResults = null;
            var identificationResults = null;
            var payment = null;
            var examDate = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                sex = sessionStorage.getItem(webStorageKey.SEX);
                idCard = sessionStorage.getItem(webStorageKey.ID_CARD);
                mobile = sessionStorage.getItem(webStorageKey.MOBILE);
                scienceName = sessionStorage.getItem(webStorageKey.SCIENCE_NAME);
                schoolName = sessionStorage.getItem(webStorageKey.SCHOOL_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                level = sessionStorage.getItem(webStorageKey.LEVEL);
                examType = sessionStorage.getItem(webStorageKey.EXAM_TYPE);
                morningResults = sessionStorage.getItem(webStorageKey.MORNING_RESULTS);
                afternoonResults = sessionStorage.getItem(webStorageKey.AFTERNOON_RESULTS);
                thesisResults = sessionStorage.getItem(webStorageKey.THESIS_RESULTS);
                identificationResults = sessionStorage.getItem(webStorageKey.IDENTIFICATION_RESULTS);
                payment = sessionStorage.getItem(webStorageKey.PAYMENT);
                examDate = sessionStorage.getItem(webStorageKey.EXAM_DATE);
            }

            if (realName !== null) {
                $(getParamId().realName).val(realName);
            }

            if (sex !== null) {
                $(getParamId().sex).val(sex);
            }

            if (idCard !== null) {
                $(getParamId().idCard).val(idCard);
            }

            if (mobile !== null) {
                $(getParamId().mobile).val(mobile);
            }

            if (scienceName !== null) {
                $(getParamId().scienceName).val(scienceName);
            }

            if (schoolName !== null) {
                $(getParamId().schoolName).val(schoolName);
            }

            if (organizeName !== null) {
                $(getParamId().organizeName).val(organizeName);
            }

            if (level !== null) {
                $(getParamId().level).val(level);
            }

            if (examType !== null) {
                $(getParamId().examType).val(examType);
            }

            if (morningResults !== null) {
                $(getParamId().morningResults).val(morningResults);
            }

            if (afternoonResults !== null) {
                $(getParamId().afternoonResults).val(afternoonResults);
            }

            if (thesisResults !== null) {
                $(getParamId().thesisResults).val(thesisResults);
            }

            if (identificationResults !== null) {
                $(getParamId().identificationResults).val(identificationResults);
            }

            if (payment !== null) {
                $(getParamId().payment).val(payment);
            }

            if (examDate !== null) {
                $(getParamId().examDate).val(examDate);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().realName).val('');
            $(getParamId().sex).val('');
            $(getParamId().idCard).val('');
            $(getParamId().mobile).val('');
            $(getParamId().scienceName).val('');
            $(getParamId().schoolName).val('');
            $(getParamId().organizeName).val('');
            $(getParamId().level).val('');
            $(getParamId().examType).val('');
            $(getParamId().morningResults).val('');
            $(getParamId().afternoonResults).val('');
            $(getParamId().thesisResults).val('');
            $(getParamId().identificationResults).val('');
            $(getParamId().payment).val('');
            $(getParamId().examDate).val('');
        }

        $('#pass').click(function (){
            $(this).addClass('active');
            $('#failed').removeClass('active');
            initParam();
            myTable.ajax.reload();
        });

        $('#failed').click(function (){
            $(this).addClass('active');
            $('#pass').removeClass('active');
            initParam();
            myTable.ajax.reload();
        });

        $(getParamId().realName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().sex).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().idCard).keyup(function (event) {
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

        $(getParamId().scienceName).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().schoolName).keyup(function (event) {
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

        $(getParamId().level).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().examType).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().morningResults).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().afternoonResults).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().thesisResults).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().identificationResults).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().payment).keyup(function (event) {
            if (event.keyCode === 13) {
                initParam();
                myTable.ajax.reload();
            }
        });

        $(getParamId().examDate).keyup(function (event) {
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
        $('#achievement_add').click(function () {
            $.address.value(getAjaxUrl().add);
        });

        /*
         批量删除
         */
        $('#achievement_dels').click(function () {
            var achievementIds = [];
            var ids = $('input[name="check"]:checked');
            for (var i = 0; i < ids.length; i++) {
                achievementIds.push($(ids[i]).val());
            }

            if (achievementIds.length > 0) {
                Swal.fire({
                    title: "确定删除选中的成绩吗？",
                    text: "成绩删除！",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    preConfirm: function () {
                        dels(achievementIds);
                    }
                });
            } else {
                Messenger().post("未发现有选中的成绩!");
            }
        });

        /*
         编辑页面
         */
        function edit(achievementId) {
            $.address.value(getAjaxUrl().edit + '/' + achievementId);
        }

        /*
         删除
         */
        function achievement_del(achievementId) {
            Swal.fire({
                title: "确定删除该成绩吗？",
                text: "成绩删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(achievementId);
                }
            });
        }

        function del(achievementId) {
            sendDelAjax(achievementId, 1);
        }

        function dels(achievementIds) {
            sendDelAjax(achievementIds.join(","), 1);
        }

        /**
         * 删除ajax
         * @param achievementId
         */
        function sendDelAjax(achievementId) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {achievementIds: achievementId},
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