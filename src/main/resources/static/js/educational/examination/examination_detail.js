//# sourceURL=examination_detail.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger", "jquery.fileupload-validate"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/educational/examination/detail/paging',
                subscribe_sms: web_path + '/web/educational/examination/detail/subscribe_sms',
                file_upload_url: web_path + '/web/educational/examination/detail/upload/file',
                add:'/web/educational/examination/detail/add',
                page: '/web/menu/educational/examination'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            examinationNoticeReleaseId: $('#examinationNoticeReleaseId').val()
        };

        /*
        参数
        */
        var param = {
            orderColumnName: 'serialNumber',
            orderDir: 'asc',
            extraSearch: JSON.stringify({
                courseName: '',
                organizeName: '',
                examClassroom: '',
                chiefExaminer: '',
                examinationNoticeReleaseId: page_param.examinationNoticeReleaseId
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            COURSE_NAME: 'EDUCATIONAL_EXAMINATION_DETAIL_COURSE_NAME_SEARCH',
            ORGANIZE_NAME: 'EDUCATIONAL_EXAMINATION_DETAIL_ORGANIZE_NAME_SEARCH',
            EXAM_CLASSROOM: 'EDUCATIONAL_EXAMINATION_DETAIL_EXAM_CLASSROOM_SEARCH',
            CHIEF_EXAMINER: 'EDUCATIONAL_EXAMINATION_DETAIL_CHIEF_EXAMINER_SEARCH'
        };

        /*
        参数id
        */
        var param_id = {
            courseName: '#search_course_name',
            organizeName: '#search_organize_name',
            examClassroom: '#search_exam_classroom',
            chiefExaminer: '#search_chief_examiner',
        };

        /*
        清空参数
        */
        function cleanParam() {
            $(param_id.courseName).val('');
            $(param_id.organizeName).val('');
            $(param_id.examClassroom).val('');
            $(param_id.chiefExaminer).val('');
        }

        /**
         * 刷新查询参数
         */
        function refreshSearch() {
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.COURSE_NAME, $(param_id.courseName).val());
                sessionStorage.setItem(webStorageKey.ORGANIZE_NAME, $(param_id.organizeName).val());
                sessionStorage.setItem(webStorageKey.EXAM_CLASSROOM, $(param_id.examClassroom).val());
                sessionStorage.setItem(webStorageKey.CHIEF_EXAMINER, $(param_id.chiefExaminer).val());
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

        // 上传组件
        $('#batchImport').fileupload({
            url: getAjaxUrl().file_upload_url,
            dataType: 'json',
            maxFileSize: 500000000,// 500MB
            maxNumberOfFiles: 1,
            acceptFileTypes: /(xls|XLS|xlsx|XLSX)$/i,
            formAcceptCharset: 'utf-8',
            messages: {
                acceptFileTypes: '仅支持xls或xlsx类型',
                maxFileSize: '单文件上传仅允许500MB大小'
            },
            submit: function (e, data) {
                data.formData = {
                    'examinationNoticeReleaseId': page_param.examinationNoticeReleaseId
                };
            },
            done: function (e, data) {
                Messenger().post({
                    message: data.result.msg,
                    type: data.result.state ? 'success' : 'error',
                    showCloseButton: true
                });
                $('#importBtn').text('批量导入');
                init();
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#importBtn').text('导入中...' + progress + '%');
            }
        }).on('fileuploadadd', function (evt, data) {
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

        $('#refresh').click(function () {
            init();
        });

        $('#add').click(function (){
            $.address.value(getAjaxUrl().add + "/" + page_param.examinationNoticeReleaseId);
        });

        $(param_id.courseName).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.organizeName).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.examClassroom).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.chiefExaminer).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        init();
        initSearchInput();

        function init() {
            initSearchContent();
            initData();
        }

        function initData() {
            $.get(getAjaxUrl().data, param, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    listData(data);
                }
            });
        }

        /*
         初始化搜索内容
        */
        function initSearchContent() {
            var courseName = null;
            var organizeName = null;
            var examClassroom = null;
            var chiefExaminer = null;
            var params = {
                courseName: '',
                organizeName: '',
                examClassroom: '',
                chiefExaminer: '',
                examinationNoticeReleaseId: page_param.examinationNoticeReleaseId
            };
            if (typeof (Storage) !== "undefined") {
                courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                examClassroom = sessionStorage.getItem(webStorageKey.EXAM_CLASSROOM);
                chiefExaminer = sessionStorage.getItem(webStorageKey.CHIEF_EXAMINER);
            }
            if (courseName !== null) {
                params.courseName = courseName;
            } else {
                params.courseName = $(param_id.courseName).val();
            }

            if (organizeName !== null) {
                params.organizeName = organizeName;
            } else {
                params.organizeName = $(param_id.organizeName).val();
            }

            if (examClassroom !== null) {
                params.examClassroom = examClassroom;
            } else {
                params.examClassroom = $(param_id.examClassroom).val();
            }

            if (chiefExaminer !== null) {
                params.chiefExaminer = chiefExaminer;
            } else {
                params.chiefExaminer = $(param_id.chiefExaminer).val();
            }
            param.extraSearch = JSON.stringify(params);
        }

        /*
        初始化搜索框
        */
        function initSearchInput() {
            var courseName = null;
            var organizeName = null;
            var examClassroom = null;
            var chiefExaminer = null;
            if (typeof (Storage) !== "undefined") {
                courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                examClassroom = sessionStorage.getItem(webStorageKey.EXAM_CLASSROOM);
                chiefExaminer = sessionStorage.getItem(webStorageKey.CHIEF_EXAMINER);
            }
            if (courseName !== null) {
                $(param_id.courseName).val(courseName);
            }

            if (organizeName !== null) {
                $(param_id.organizeName).val(organizeName);
            }

            if (examClassroom !== null) {
                $(param_id.examClassroom).val(examClassroom);
            }

            if (chiefExaminer !== null) {
                $(param_id.chiefExaminer).val(chiefExaminer);
            }
        }

        var tableElement = $('#tableData');

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#data_template").html());
            $('#totalSize').text(data.page.totalSize);
            $(tableElement).html(template(data));
        }

        tableElement.delegate('.subscribe_sms', "click", function () {
            sendSubscribeAjax($(this).attr('data-id'));
        });

        /**
         * 订阅ajax
         * @param examinationNoticeDetailId
         */
        function sendSubscribeAjax(examinationNoticeDetailId) {
            $.get(getAjaxUrl().subscribe_sms, {
                examinationNoticeDetailId: examinationNoticeDetailId,
                subscribeType: 1
            }, function (data) {
                if (data.state) {
                    Swal.fire('订阅成功', data.msg, 'success');
                    init();
                } else {
                    Swal.fire('订阅失败', data.msg, 'error');
                }
            });
        }
    });