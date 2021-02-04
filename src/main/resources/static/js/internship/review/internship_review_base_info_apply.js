//# sourceURL=internship_review_base_info_apply.js
require(["jquery", "lodash", "tools", "sweetalert2", "handlebars", "nav.active", "messenger", "jquery.address", "jquery.simple-pagination", "select2-zh-CN", "flatpickr-zh"],
    function ($, _, tools, Swal, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/internship/review/paging',
            detail: web_path + '/web/internship/review/detail',
            agree: web_path + '/web/internship/review/agree',
            disagree: web_path + '/web/internship/review/disagree',
            obtain_organize_data: web_path + '/web/internship/review/organize',
            page: '/web/menu/internship/review',
            download: web_path + '/web/internship/review/download'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            realName: '#search_real_name',
            studentNumber: '#search_student_number',
            organize: '#search_organize'
        };

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val()
        };

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 2,
            displayedPages: 3,
            orderColumnName: 'studentNumber',
            orderDir: 'asc',
            extraSearch: JSON.stringify({
                realName: '',
                studentNumber: '',
                organizeId: '',
                internshipReleaseId: page_param.paramInternshipReleaseId,
                internshipApplyState: 4
            })
        };

        var init_configure = {
            init_organize: false
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            REAL_NAME: 'INTERNSHIP_REVIEW_BASE_APPLY_REAL_NAME_SEARCH_' + page_param.paramInternshipReleaseId,
            STUDENT_NUMBER: 'INTERNSHIP_REVIEW_BASE_APPLY_STUDENT_NUMBER_SEARCH_' + page_param.paramInternshipReleaseId,
            ORGANIZE_ID: 'INTERNSHIP_REVIEW_BASE_APPLY_ORGANIZE_ID_SEARCH_' + page_param.paramInternshipReleaseId,
            PAGE_NUM: 'INTERNSHIP_REVIEW_BASE_APPLY_PAGE_NUM_' + page_param.paramInternshipReleaseId,
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.realName).val('');
            $(param_id.studentNumber).val('');
            organizeSelect2.val('').trigger("change");
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.REAL_NAME, $(param_id.realName).val());
                sessionStorage.setItem(webStorageKey.STUDENT_NUMBER, $(param_id.studentNumber).val());
                sessionStorage.setItem(webStorageKey.ORGANIZE_ID, $(param_id.organize).val() != null ? $(param_id.organize).val() : '');
                sessionStorage.setItem(webStorageKey.PAGE_NUM, "0");
            }
        }

        /*
         搜索
         */
        $('#search').click(function () {
            refreshSearch();
            init();
        });

        /*
         重置
         */
        $('#reset_search').click(function () {
            cleanParam();
            refreshSearch();
            init();
        });

        $('#refresh').click(function () {
            init();
        });

        $(param_id.realName).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.studentNumber).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.organize).on('select2:select', function (e) {
            refreshSearch();
            init();
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#internship-audit-template").html());
            Handlebars.registerHelper('internship_apply_state', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(internshipApplyStateCode(this.internshipApplyState)));
            });
            $(tableData).html(template(data));
        }

        /**
         * 详情数据
         * @param data 数据
         */
        function detailData(data) {
            var template = Handlebars.compile($("#internship-detail-template").html());
            $(detail).html(template(data.internshipInfo));
        }

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

        $('#fillTime').flatpickr({
            "locale": "zh",
            "mode": "range"
        });

        /*
         下载电子资料
         */
        $(tableData).delegate('.downloadFile', "click", function () {
            var id = $(this).attr('data-id');
            window.location.href = ajax_url.download + '/' + id;
        });

        var detail = null;
        var detailSelect = {};
        /*
         查看详情
         */
        $(tableData).delegate('.detail_apply', "click", function () {
            detail = $(this).parent().prev().find('.detail');
            var studentId = $(this).attr('data-student');

            if (!detailSelect[studentId]) {
                initDetail(studentId);
                detailSelect[studentId] = true;
            } else {
                $(detail).prev().addClass('col-md-12').removeClass('col-md-8');
                $(detail).removeClass('col-md-4');
                $(detail).css({'overflow-y': '', 'overflow-x': '', 'height': ''});
                $(detail).empty();
                detailSelect[studentId] = false;
            }

        });

        function initDetail(studentId) {
            $.get(ajax_url.detail + '/' + page_param.paramInternshipReleaseId + '/' + studentId, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    $(detail).prev().removeClass('col-md-12').addClass('col-md-8');
                    $(detail).addClass('col-md-4');
                    $(detail).css({'overflow-y': 'auto', 'overflow-x': 'auto', 'height': '360px'});
                    detailData(data);
                }
            });
        }

        /*
       同意
       */
        $(tableData).delegate('.agree_apply', "click", function () {
            var id = $(this).attr('data-id');
            var studentId = $(this).attr('data-student');
            showFillTimeModal(5, id, studentId, '同意');
        });

        /*
         拒绝
         */
        $(tableData).delegate('.disagree_apply', "click", function () {
            var id = $(this).attr('data-id');
            var studentId = $(this).attr('data-student');
            showStateModal(2, id, studentId, '拒绝');
        });

        /*
         提交变更申请
         */
        $('#stateOk').click(function () {
            stateAdd();
        });

        /*
         提交同意申请
         */
        $('#fillTimeOk').click(function () {
            fillTimeAdd();
        });

        /*
         取消变更申请
         */
        $('#stateCancel').click(function () {
            hideStateModal();
        });

        /*
         取消同意申请
         */
        $('#fillTimeCancel').click(function () {
            hideFillTimeModal();
        });

        /**
         * 展示变更模态框
         * @param state
         * @param internshipReleaseId
         * @param studentId
         * @param title
         */
        function showStateModal(state, internshipReleaseId, studentId, title) {
            $('#applyState').val(state);
            $('#applyInternshipReleaseId').val(internshipReleaseId);
            $('#applyStudentId').val(studentId);
            $('#stateModalLabel').text(title);
            $('#stateModal').modal('show');
        }

        /**
         * 展示填写时间模态框
         * @param state
         * @param internshipReleaseId
         * @param studentId
         * @param title
         */
        function showFillTimeModal(state, internshipReleaseId, studentId, title) {
            $('#fillTimeState').val(state);
            $('#fillTimeInternshipReleaseId').val(internshipReleaseId);
            $('#fillTimeStudentId').val(studentId);
            $('#fillTimeModalLabel').text(title);
            $('#fillTimeModal').modal('show');
        }

        /**
         * 隐藏变更模态框
         */
        function hideStateModal() {
            $('#applyState').val('');
            $('#applyInternshipReleaseId').val('');
            $('#applyStudentId').val('');
            $('#reason').val('');
            $('#stateModalLabel').text('');
            $('#stateModal').modal('hide');
        }

        /**
         * 隐藏变更模态框
         */
        function hideFillTimeModal() {
            $('#fillTimeState').val('');
            $('#fillTimeInternshipReleaseId').val('');
            $('#fillTimeStudentId').val('');
            $('#fillTimeModalLabel').text('');
            $('#fillTimeModal').modal('hide');
        }

        /*
        检验原因字数
        */
        $('#reason').blur(function () {
            var reason_id = '#reason';
            var reason = $(reason_id).val();
            if (reason.length <= 0 || reason.length > 500) {
                tools.validErrorDom(reason_id, '原因500个字符以内');
            } else {
                tools.validSuccessDom(reason_id);
            }
        });

        /*
         状态申请提交询问
         */
        function stateAdd() {
            Swal.fire({
                title: "确定拒绝吗？",
                text: "申请拒绝！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    validReason();
                }
            });
        }

        /*
         同意提交询问
         */
        function fillTimeAdd() {
            Swal.fire({
                title: "确定同意吗？",
                text: "申请通过！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    validFillTime();
                }
            });
        }

        function validReason() {
            var reason_id = '#reason';
            var reason = $(reason_id).val();
            if (reason.length <= 0 || reason.length > 500) {
                tools.validErrorDom(reason_id, '原因500个字符以内');
            } else {
                tools.validSuccessDom(reason_id);
                sendStateAjax();
            }
        }

        /**
         * 发送状态申请
         */
        function sendStateAjax() {
            $.post(ajax_url.disagree, $('#state_form').serialize(), function (data) {
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            hideStateModal();
                            init();
                        }
                    });
                } else {
                    Swal.fire('保存失败', data.msg, 'error');
                }
            });
        }

        function validFillTime() {
            var fillTimeId = '#fillTime';
            var fillTime = $(fillTimeId).val();
            if (fillTime.length <= 0) {
                tools.validErrorDom(fillTimeId, '请选择填写时间范围');
            } else {
                tools.validSuccessDom(fillTimeId);
                sendFillTimeAjax();
            }
        }

        /**
         * 发送同意申请
         */
        function sendFillTimeAjax() {
            $.post(ajax_url.agree, $('#fill_time_form').serialize(), function (data) {
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            hideFillTimeModal();
                            init();
                        }
                    });
                } else {
                    Swal.fire('保存失败', data.msg, 'error');
                }
            });
        }

        init();
        initSearchOrganize();
        initSelect2();
        initSearchInput();

        /**
         * 初始化数据
         */
        function init() {
            initSearchContent();
            tools.dataLoading();
            $.get(ajax_url.data, param, function (data) {
                tools.dataEndLoading();
                createPage(data);
                listData(data);
            });
        }

        var organizeSelect2 = null;

        /**
         * 初始化班级数据
         */
        function initSearchOrganize() {
            $.get(ajax_url.obtain_organize_data + "/" + page_param.paramInternshipReleaseId, function (data) {
                $(param_id.organize).html('<option label="请选择班级"></option>');
                organizeSelect2 = $(param_id.organize).select2({data: data.results});

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
        初始化搜索内容
        */
        function initSearchContent() {
            var realName = null;
            var studentNumber = null;
            var organizeId = null;
            var pageNum = null;
            var params = {
                realName: '',
                studentNumber: '',
                organizeId: '',
                internshipReleaseId: page_param.paramInternshipReleaseId,
                internshipApplyState: 4
            };
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
                organizeId = sessionStorage.getItem(webStorageKey.ORGANIZE_ID);
                pageNum = sessionStorage.getItem(webStorageKey.PAGE_NUM);
            }
            if (realName !== null) {
                params.realName = realName;
            } else {
                params.realName = $(param_id.realName).val();
            }

            if (studentNumber !== null) {
                params.studentNumber = studentNumber;
            } else {
                params.studentNumber = $(param_id.studentNumber).val();
            }

            if (organizeId !== null) {
                params.organizeId = organizeId;
            } else {
                params.organizeId = $(param_id.organize).val();
            }

            if (pageNum !== null) {
                param.pageNum = pageNum;
            } else {
                param.pageNum = 0;
            }
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var realName = null;
            var studentNumber = null;
            if (typeof (Storage) !== "undefined") {
                realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
                studentNumber = sessionStorage.getItem(webStorageKey.STUDENT_NUMBER);
            }
            if (realName !== null) {
                $(param_id.realName).val(realName);
            }

            if (studentNumber !== null) {
                $(param_id.studentNumber).val(studentNumber);
            }
        }

        /**
         * 创建分页
         * @param data 数据
         */
        function createPage(data) {
            $('#pagination').pagination({
                pages: data.page.totalPages,
                displayedPages: data.page.displayedPages,
                currentPage: data.page.pageNum,
                hrefTextPrefix: '',
                prevText: '<',
                nextText: '>',
                cssStyle: '',
                listStyle: 'pagination',
                onPageClick: function (pageNumber, event) {
                    // Callback triggered when a page is clicked
                    // Page number is given as an optional parameter
                    if (typeof (Storage) !== "undefined") {
                        sessionStorage.setItem(webStorageKey.PAGE_NUM, pageNumber);
                    }
                    nextPage(pageNumber);
                }
            });
        }

        /**
         * 下一页
         * @param pageNumber 当前页
         */
        function nextPage(pageNumber) {
            param.pageNum = pageNumber;
            tools.dataLoading();
            $.get(ajax_url.data, param, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

    });