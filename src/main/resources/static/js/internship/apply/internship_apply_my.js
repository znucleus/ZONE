//# sourceURL=internship_apply_my.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address",
        "jquery.simple-pagination", "jquery-labelauty", "jquery.fileupload-validate"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/internship/apply/data',
            edit: '/web/internship/apply/edit',
            look: '/web/internship/apply/look',
            change_state: '/web/internship/apply/state',
            recall_apply: web_path + '/web/internship/apply/recall',
            file_upload_url: web_path + '/web/internship/apply/upload/file',
            download: web_path + '/web/internship/apply/download',
            delete_file_url: '/web/internship/apply/delete/file',
            page: '/web/menu/internship/apply'
        };

        navActive(ajax_url.page);

        /*
         参数
         */
        var param = {
            pageNum: 0,
            length: 2,
            displayedPages: 3,
            orderColumnName: 'applyTime',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                internshipTitle: ''
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            INTERNSHIP_TITLE: 'INTERNSHIP_APPLY_MY_INTERNSHIP_TITLE_SEARCH'
        };

        /*
         参数id
         */
        var param_id = {
            internshipTitle: '#search_internship_title'
        };

        var tableData = '#tableData';

        /*
         清空参数
         */
        function cleanParam() {
            $(param_id.internshipTitle).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.INTERNSHIP_TITLE, $(param_id.internshipTitle).val());
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

        $(param_id.internshipTitle).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#internship-apply-template").html());
            Handlebars.registerHelper('internship_apply_state', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(internshipApplyStateCode(this.internshipApplyState)));
            });
            $(tableData).html(template(data));
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
                    msg = '单位信息变更填写中...';
                    break;
            }
            return msg;
        }

        /*
         进入申请
         */
        $(tableData).delegate('.myApply', "click", function () {
            $.address.value(ajax_url.edit + '/' + $(this).attr('data-id'));
        });

        /*
        查看申请
        */
        $(tableData).delegate('.myApplyLook', "click", function () {
            $.address.value(ajax_url.look + '/' + $(this).attr('data-id'));
        });

        /*
       撤消申请
       */
        $(tableData).delegate('.recallApply', "click", function () {
            var id = $(this).attr('data-id');
            recall(id);
        });

        /*
       基础信息变更申请
       */
        $(tableData).delegate('.basisApply', "click", function () {
            var id = $(this).attr('data-id');
            showStateModal(4, id, '基础信息变更申请');
        });

        /*
        单位信息变更申请
        */
        $(tableData).delegate('.firmApply', "click", function () {
            var id = $(this).attr('data-id');
            showStateModal(6, id, '单位信息变更申请');
        });

        /*
       上传电子资料
       */
        $(tableData).delegate('.uploadFile', "click", function () {
            var id = $(this).attr('data-id');
            showUploadModal(id);
        });

        /*
         下载电子资料
         */
        $(tableData).delegate('.downloadFile', "click", function () {
            var id = $(this).attr('data-id');
            window.location.href = ajax_url.download + '/' + id;
        });

        /*
         删除电子资料
         */
        $(tableData).delegate('.deleteFile', "click", function () {
            var id = $(this).attr('data-id');
            Swal.fire({
                title: "确定删除文件吗？",
                text: "文件删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    deleteFile(id);
                }
            });
        });

        /**
         * 撤消询问
         * @param id
         */
        function recall(id) {
            Swal.fire({
                title: "确定撤消申请吗？",
                text: "申请撤消！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#ddc144',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendRecallAjax(id);
                }
            });
        }

        /**
         * 撤消ajax
         * @param id
         */
        function sendRecallAjax(id) {
            $.post(ajax_url.recall_apply, {id: id}, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    init();
                }
            });
        }

        /**
         * 展示变更模态框
         * @param state
         * @param internshipReleaseId
         * @param title
         */
        function showStateModal(state, internshipReleaseId, title) {
            $('#applyState').val(state);
            $('#applyInternshipReleaseId').val(internshipReleaseId);
            $('#stateModalLabel').text(title);
            $('#stateModal').modal('show');
        }

        /**
         * 隐藏变更模态框
         */
        function hideStateModal() {
            $('#applyState').val('');
            $('#applyInternshipReleaseId').val('');
            $('#reason').val('');
            $('#stateModalLabel').text('');
            $('#stateModal').modal('hide');
        }

        /*
        提交变更申请
        */
        $('#stateOk').click(function () {
            validReason();
        });

        /*
        取消变更申请
        */
        $('#stateCancel').click(function () {
            hideStateModal();
        });

        function validReason() {
            var reason = $('#reason').val();
            if (reason.length <= 0 || reason.length > 500) {
                tools.validErrorDom('#reason', '原因500个字符以内');
            } else {
                tools.validSuccessDom('#reason');
                stateAdd();
            }
        }

        /*
      状态申请提交询问
      */
        function stateAdd() {
            Swal.fire({
                title: "确定进行申请吗？",
                text: "实习申请！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#ddd974',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    sendStateAjax();
                }
            });
        }

        /**
         * 发送状态申请
         */
        function sendStateAjax() {
            $.post(ajax_url.change_state, $('#state_form').serialize(), function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    hideStateModal();
                    init();
                }
            });
        }

        /**
         * 发送删除ajax
         * @param id
         */
        function deleteFile(id) {
            $.post(ajax_url.delete_file_url, {id: id}, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    init();
                }
            });
        }

        /**
         * 展开上传文件modal
         * @param id
         */
        function showUploadModal(id) {
            $('#uploadInternshipReleaseId').val(id);
            $('#uploadModal').modal('show');
        }

        /**
         * 关闭文件上传modal
         */
        function closeUploadModal() {
            $('#uploadInternshipReleaseId').val('');
            $('#fileName').text('');
            $('#fileSize').text('');
            $('#uploadModal').modal('hide');
        }

        var startUpload = null; // 开始上传

        // 上传组件
        $('#fileupload').fileupload({
            url: ajax_url.file_upload_url,
            dataType: 'json',
            maxFileSize: 500000000,// 500MB
            formAcceptCharset: 'utf-8',
            autoUpload: false,// 关闭自动上传
            maxNumberOfFiles: 1,
            messages: {
                maxNumberOfFiles: '最大支持上传1个文件',
                maxFileSize: '单文件上传仅允许100MB大小'
            },
            add: function (e, data) {
                $('#fileName').text(data.files[0].name);
                $('#fileSize').text(data.files[0].size);
                startUpload = data;
            },
            submit: function (e, data) {
                if (validUpload()) {
                    var internshipReleaseId = $('#uploadInternshipReleaseId').val();
                    data.formData = {
                        'internshipReleaseId': internshipReleaseId
                    };
                }
            },
            done: function (e, data) {
                Messenger().post({
                    message: data.result.msg,
                    type: data.result.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.result.state) {
                    init();// 刷新我的申请
                    closeUploadModal();// 清空信息
                }

            }
        }).on('fileuploadsubmit', function (evt, data) {
            var isOk = true;
            var $this = $(this);
            var validation = data.process(function () {
                return $this.fileupload('process', data);
            });
            validation.fail(function (data) {
                isOk = false;
                Messenger().post({
                    message: '上传失败: ' + data.files[0].error,
                    type: 'error',
                    showCloseButton: true
                });
            });
            return isOk;
        });

        function validUpload() {
            var internshipReleaseId = $('#uploadInternshipReleaseId').val();
            var fileName = $('#fileName').text();
            if (internshipReleaseId !== '') {
                if (fileName !== '') {
                    return true;
                } else {
                    Messenger().post({
                        message: '请选择文件',
                        type: 'error',
                        showCloseButton: true
                    });
                    return false;
                }
            } else {
                Messenger().post({
                    message: '缺失重要参数不能上传',
                    type: 'error',
                    showCloseButton: true
                });
                return false;
            }
        }

        // 确认上传
        $('#confirmUpload').click(function () {
            if (validUpload()) {
                startUpload.submit();
            }
        });

        // 取消上传
        $('#cancelUpload').click(function () {
            closeUploadModal();// 清空信息
        });

        init();
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

        /*
        初始化搜索内容
       */
        function initSearchContent() {
            var internshipTitle = null;
            var params = {
                internshipTitle: ''
            };
            if (typeof (Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
            }
            if (internshipTitle !== null) {
                params.internshipTitle = internshipTitle;
            } else {
                params.internshipTitle = $(param_id.internshipTitle).val();
            }
            param.pageNum = 0;
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var internshipTitle = null;
            if (typeof (Storage) !== "undefined") {
                internshipTitle = sessionStorage.getItem(webStorageKey.INTERNSHIP_TITLE);
            }
            if (internshipTitle !== null) {
                $(param_id.internshipTitle).val(internshipTitle);
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
                hrefTextPrefix: '',
                prevText: '<',
                nextText: '>',
                cssStyle: '',
                listStyle: 'pagination',
                onPageClick: function (pageNumber, event) {
                    // Callback triggered when a page is clicked
                    // Page number is given as an optional parameter
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