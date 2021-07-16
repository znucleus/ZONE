//# sourceURL=examination_detail.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger", "jquery.fileupload-validate"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/educational/examination/detail/paging',
                subscribe_sms: web_path + '/web/educational/examination/sms-subscribe/save',
                file_upload_url: web_path + '/web/educational/examination/detail/upload/file',
                add: '/web/educational/examination/detail/add',
                edit: '/web/educational/examination/detail/edit',
                del: '/web/educational/examination/detail/delete',
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
                examWeek: '',
                examDate: '',
                weekday: '',
                examTime: '',
                examClassroom: '',
                invigilator: '',
                chiefExaminer: '',
                examinationNoticeReleaseId: page_param.examinationNoticeReleaseId
            })
        };

        /*
        web storage key.
        */
        var webStorageKey = {
            COURSE_NAME: 'EDUCATIONAL_EXAMINATION_DETAIL_COURSE_NAME_SEARCH_' + page_param.examinationNoticeReleaseId,
            ORGANIZE_NAME: 'EDUCATIONAL_EXAMINATION_DETAIL_ORGANIZE_NAME_SEARCH_' + page_param.examinationNoticeReleaseId,
            EXAM_WEEK: 'EDUCATIONAL_EXAMINATION_DETAIL_EXAM_WEEK_SEARCH_' + page_param.examinationNoticeReleaseId,
            EXAM_DATE: 'EDUCATIONAL_EXAMINATION_DETAIL_EXAM_DATE_SEARCH_' + page_param.examinationNoticeReleaseId,
            WEEKDAY: 'EDUCATIONAL_EXAMINATION_DETAIL_WEEKDAY_SEARCH_' + page_param.examinationNoticeReleaseId,
            EXAM_TIME: 'EDUCATIONAL_EXAMINATION_DETAIL_EXAM_TIME_SEARCH_' + page_param.examinationNoticeReleaseId,
            INVIGILATOR: 'EDUCATIONAL_EXAMINATION_DETAIL_INVIGILATOR_SEARCH_' + page_param.examinationNoticeReleaseId,
            EXAM_CLASSROOM: 'EDUCATIONAL_EXAMINATION_DETAIL_EXAM_CLASSROOM_SEARCH_' + page_param.examinationNoticeReleaseId,
            CHIEF_EXAMINER: 'EDUCATIONAL_EXAMINATION_DETAIL_CHIEF_EXAMINER_SEARCH_' + page_param.examinationNoticeReleaseId
        };

        /*
        参数id
        */
        var param_id = {
            courseName: '#search_course_name',
            organizeName: '#search_organize_name',
            examWeek: '#search_exam_week',
            examDate: '#search_exam_date',
            weekday: '#search_weekday',
            examTime: '#search_exam_time',
            invigilator: '#search_invigilator',
            examClassroom: '#search_exam_classroom',
            chiefExaminer: '#search_chief_examiner',
        };

        /*
        清空参数
        */
        function cleanParam() {
            $(param_id.courseName).val('');
            $(param_id.organizeName).val('');
            $(param_id.examWeek).val('');
            $(param_id.examDate).val('');
            $(param_id.weekday).val('');
            $(param_id.examTime).val('');
            $(param_id.invigilator).val('');
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
                sessionStorage.setItem(webStorageKey.EXAM_WEEK, $(param_id.examWeek).val());
                sessionStorage.setItem(webStorageKey.EXAM_DATE, $(param_id.examDate).val());
                sessionStorage.setItem(webStorageKey.WEEKDAY, $(param_id.weekday).val());
                sessionStorage.setItem(webStorageKey.EXAM_TIME, $(param_id.examTime).val());
                sessionStorage.setItem(webStorageKey.INVIGILATOR, $(param_id.invigilator).val());
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

        $('#add').click(function () {
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

        $(param_id.examWeek).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.examDate).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.weekday).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.examTime).keyup(function (event) {
            if (event.keyCode === 13) {
                refreshSearch();
                init();
            }
        });

        $(param_id.invigilator).keyup(function (event) {
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
            var examWeek = null;
            var examDate = null;
            var weekday = null;
            var examTime = null;
            var invigilator = null;
            var examClassroom = null;
            var chiefExaminer = null;
            var params = {
                courseName: '',
                organizeName: '',
                examWeek: '',
                examDate: '',
                weekday: '',
                examTime: '',
                invigilator: '',
                examClassroom: '',
                chiefExaminer: '',
                examinationNoticeReleaseId: page_param.examinationNoticeReleaseId
            };
            if (typeof (Storage) !== "undefined") {
                courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                examWeek = sessionStorage.getItem(webStorageKey.EXAM_WEEK);
                examDate = sessionStorage.getItem(webStorageKey.EXAM_DATE);
                weekday = sessionStorage.getItem(webStorageKey.WEEKDAY);
                examTime = sessionStorage.getItem(webStorageKey.EXAM_TIME);
                invigilator = sessionStorage.getItem(webStorageKey.INVIGILATOR);
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

            if (examWeek !== null) {
                params.examWeek = examWeek;
            } else {
                params.examWeek = $(param_id.examWeek).val();
            }

            if (examDate !== null) {
                params.examDate = examDate;
            } else {
                params.examDate = $(param_id.examDate).val();
            }

            if (weekday !== null) {
                params.weekday = weekday;
            } else {
                params.weekday = $(param_id.weekday).val();
            }

            if (examTime !== null) {
                params.examTime = examTime;
            } else {
                params.examTime = $(param_id.examTime).val();
            }

            if (invigilator !== null) {
                params.invigilator = invigilator;
            } else {
                params.invigilator = $(param_id.invigilator).val();
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
            var examWeek = null;
            var examDate = null;
            var weekday = null;
            var examTime = null;
            var invigilator = null;
            var examClassroom = null;
            var chiefExaminer = null;
            if (typeof (Storage) !== "undefined") {
                courseName = sessionStorage.getItem(webStorageKey.COURSE_NAME);
                organizeName = sessionStorage.getItem(webStorageKey.ORGANIZE_NAME);
                examWeek = sessionStorage.getItem(webStorageKey.EXAM_WEEK);
                examDate = sessionStorage.getItem(webStorageKey.EXAM_DATE);
                weekday = sessionStorage.getItem(webStorageKey.WEEKDAY);
                examTime = sessionStorage.getItem(webStorageKey.EXAM_TIME);
                invigilator = sessionStorage.getItem(webStorageKey.INVIGILATOR);
                examClassroom = sessionStorage.getItem(webStorageKey.EXAM_CLASSROOM);
                chiefExaminer = sessionStorage.getItem(webStorageKey.CHIEF_EXAMINER);
            }
            if (courseName !== null) {
                $(param_id.courseName).val(courseName);
            }

            if (organizeName !== null) {
                $(param_id.organizeName).val(organizeName);
            }

            if (examWeek !== null) {
                $(param_id.examWeek).val(examWeek);
            }

            if (examDate !== null) {
                $(param_id.examDate).val(examDate);
            }

            if (weekday !== null) {
                $(param_id.weekday).val(weekday);
            }

            if (examTime !== null) {
                $(param_id.examTime).val(examTime);
            }

            if (invigilator !== null) {
                $(param_id.invigilator).val(invigilator);
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

        tableElement.delegate('.edit', "click", function () {
            $.address.value(getAjaxUrl().edit + "/" + $(this).attr('data-id'));
        });

        tableElement.delegate('.del', "click", function () {
            detailDel($(this).attr('data-id'), $(this).attr('data-name'));
        });

        /**
         * 删除确认
         * @param id 发布id
         * @param name 标题
         */
        function detailDel(id, name) {
            Swal.fire({
                title: "确定删除 '" + name + "' 吗？",
                text: "教务考试详情删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    del(id);
                }
            });
        }

        /**
         * 删除
         * @param id
         */
        function del(id) {
            sendDelAjax(id);
        }

        /**
         * 删除ajax
         * @param id
         */
        function sendDelAjax(id) {
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().del,
                data: {id: id},
                success: function (data) {
                    Messenger().post({
                        message: data.msg,
                        type: data.state ? 'success' : 'error',
                        showCloseButton: true
                    });

                    if (data.state) {
                        init();
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

        tableElement.delegate('.subscribe_sms', "click", function () {
            sendSubscribeAjax($(this).attr('data-id'));
        });

        /**
         * 订阅ajax
         * @param examinationNoticeDetailId
         */
        function sendSubscribeAjax(examinationNoticeDetailId) {
            $.post(getAjaxUrl().subscribe_sms, {
                id: examinationNoticeDetailId
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