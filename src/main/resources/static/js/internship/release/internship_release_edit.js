//# sourceURL=internship_release_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "flatpickr-zh", "jquery.fileupload-validate"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_internship_file_data: web_path + '/users/data/internship_file',
            file_upload_url: web_path + '/web/internship/release/upload/file',
            delete_file_url: web_path + '/web/internship/release/delete/file',
            update: web_path + '/web/internship/release/update',
            page: '/web/menu/internship/release'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            internshipTitle: '#internshipTitle',
            isTimeLimit: '#isTimeLimit',
            teacherDistributionTime: '#teacherDistributionTime',
            time: '#time'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        /*
         参数
         */
        var param = {
            internshipReleaseId: '',
            internshipTitle: '',
            isTimeLimit: '',
            teacherDistributionTime: '',
            time: '',
            internshipReleaseIsDel: '',
            files: ''
        };

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            paramTeacherDistributionStartTime: $('#paramTeacherDistributionStartTime').val(),
            paramTeacherDistributionEndTime: $('#paramTeacherDistributionEndTime').val(),
            paramStartTime: $('#paramStartTime').val(),
            paramEndTime: $('#paramEndTime').val(),
            collegeId: $('#collegeId').val()
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.internshipReleaseId = page_param.paramInternshipReleaseId;
            param.internshipTitle = $(param_id.internshipTitle).val();
            var isTimeLimit = $('input[name="isTimeLimit"]:checked').val();
            param.isTimeLimit = _.isUndefined(isTimeLimit) ? 0 : isTimeLimit;
            param.teacherDistributionTime = $(param_id.teacherDistributionTime).val();
            param.time = $(param_id.time).val();
            var internshipReleaseIsDel = $('input[name="internshipReleaseIsDel"]:checked').val();
            param.internshipReleaseIsDel = _.isUndefined(internshipReleaseIsDel) ? 0 : internshipReleaseIsDel;

            var f = $('.file');
            var p = [];
            for (var i = 0; i < f.length; i++) {
                p.push({
                    originalFileName: $(f[i]).attr('data-original-file-name'),
                    newName: $(f[i]).attr('data-new-name'),
                    relativePath: $(f[i]).attr('data-file-path'),
                    ext: $(f[i]).attr('data-ext'),
                    fileSize: $(f[i]).attr('data-size'),
                    contentType: $(f[i]).attr('data-content-type')
                });
            }
            if (p.length > 0) {
                param.files = JSON.stringify(p);
            } else {
                param.files = '';
            }
        }

        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initTeacherDistributionTime();
            initTime();
            initInternshipFile();
            initMaxLength();
        }

        function initInternshipFile() {
            $.get(ajax_url.obtain_internship_file_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                fileShow(data);
            });
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.internshipTitle).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        // 检验实习标题
        $(param_id.internshipTitle).blur(function () {
            initParam();
            var internshipTitle = param.internshipTitle;
            if (internshipTitle.length <= 0 || internshipTitle.length > 100) {
                tools.validErrorDom(param_id.internshipTitle, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipTitle);
            }
        });

        $(param_id.isTimeLimit).click(function () {
            initParam();
            var isTimeLimit = param.isTimeLimit;
            if (Number(isTimeLimit) === 0) {
                // 不需要填写时间
                $(param_id.teacherDistributionTime).parent().css('display', 'none');
                $(param_id.time).parent().css('display', 'none');
            } else {
                $(param_id.teacherDistributionTime).parent().css('display', '');
                $(param_id.time).parent().css('display', '');
            }
        });

        function initTeacherDistributionTime() {
            initParam();
            var isTimeLimit = param.isTimeLimit;
            if (Number(isTimeLimit) === 0) {
                $(param_id.teacherDistributionTime).parent().css('display', 'none');
                $(param_id.teacherDistributionTime).flatpickr({
                    "locale": "zh",
                    "mode": "range"
                });
            } else {
                $(param_id.teacherDistributionTime).flatpickr({
                    "locale": "zh",
                    "mode": "range",
                    dateFormat: "Y-m-d",
                    defaultDate: [page_param.paramTeacherDistributionStartTime, page_param.paramTeacherDistributionEndTime]
                });
            }

        }

        function initTime() {
            initParam();
            var isTimeLimit = param.isTimeLimit;
            if (Number(isTimeLimit) === 0) {
                $(param_id.time).parent().css('display', 'none');
                $(param_id.time).flatpickr({
                    "locale": "zh",
                    "mode": "range"
                });
            } else {
                $(param_id.time).flatpickr({
                    "locale": "zh",
                    "mode": "range",
                    dateFormat: "Y-m-d",
                    defaultDate: [page_param.paramStartTime, page_param.paramEndTime]
                });
            }
        }

        // 上传组件
        $('#fileupload').fileupload({
            url: ajax_url.file_upload_url,
            dataType: 'json',
            maxFileSize: 500000000,// 500MB
            formAcceptCharset: 'utf-8',
            messages: {
                maxFileSize: '单文件上传仅允许500MB大小'
            },
            done: function (e, data) {
                Messenger().post({
                    message: data.result.msg,
                    type: data.result.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.result.state) {
                    fileShow(data.result);
                }
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#progress').find('.progress-bar').css(
                    'width',
                    progress + '%'
                );
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

        /**
         * 文件显示
         * @param data 数据
         */
        function fileShow(data) {
            var template = Handlebars.compile($("#file-template").html());

            Handlebars.registerHelper('translationSize', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.toSize(this.fileSize)));
            });

            Handlebars.registerHelper('lastPath', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(this.relativePath));
            });

            $('#fileShow').append(template(data));
        }

        /*
        删除附件
        */
        $('#fileShow').delegate('.clearFile', "click", function () {
            initParam();
            var path = $(this).attr('data-file-path');
            var originalName = $(this).attr('data-original-file-name');
            var fileId = $(this).attr('data-file-id');
            var internshipReleaseId = param.internshipReleaseId;
            var obj = $(this);
            Swal.fire({
                title: "确定删除附件 '" + originalName + "'  吗？不可恢复！",
                text: "附件删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    $.post(web_path + ajax_url.delete_file_url, {
                        filePath: path,
                        fileId: fileId,
                        internshipReleaseId: internshipReleaseId
                    }, function (data) {
                        Messenger().post({
                            message: data.msg,
                            type: data.state ? 'success' : 'error',
                            showCloseButton: true
                        });
                        if (data.state) {
                            obj.parent().parent().remove();
                        }
                    });
                }
            });
        });


        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validInternshipTitle();
        });

        function validInternshipTitle() {
            var internshipTitle = param.internshipTitle;
            if (internshipTitle.length <= 0 || internshipTitle.length > 100) {
                tools.validErrorDom(param_id.internshipTitle, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipTitle);
                validIsTimeLimit();
            }
        }

        function validIsTimeLimit() {
            var isTimeLimit = param.isTimeLimit;
            if (Number(isTimeLimit) === 0) {
                sendAjax();
            } else {
                validTeacherDistributionTime();
            }
        }

        function validTeacherDistributionTime() {
            var teacherDistributionTime = param.teacherDistributionTime;
            if (teacherDistributionTime.length <= 0) {
                tools.validErrorDom(param_id.teacherDistributionTime, '请选择指导教师分配时间');
            } else {
                tools.validSuccessDom(param_id.teacherDistributionTime);
                validTime();
            }
        }

        function validTime() {
            var time = param.time;
            if (time.length <= 0) {
                tools.validErrorDom(param_id.time, '请选择实习申请时间');
            } else {
                tools.validSuccessDom(param_id.time);
                sendAjax();
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.update,
                data: param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.page);
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

    });