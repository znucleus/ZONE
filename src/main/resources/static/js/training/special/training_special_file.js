//# sourceURL=training_special_file.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            file_type_data: web_path + '/web/training/special/file/type/data',
            file_type_save: web_path + '/web/training/special/file/type/save',
            file_type_update: web_path + '/web/training/special/file/type/update',
            file_type_del: web_path + '/web/training/special/file/type/delete',
            file_data: web_path + '/web/training/special/file/data',
            file_save: web_path + '/web/training/special/file/save',
            file_del: web_path + '/web/training/special/file/delete',
            file_download: web_path + '/web/training/special/file/download',
            file_info: web_path + '/web/training/special/file/info',
            file_mapping: web_path + '/web/training/special/file/mapping',
            file_cancel_mapping: web_path + '/web/training/special/file/relieve',
            page: '/web/menu/training/special'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTrainingSpecialId: $('#paramTrainingSpecialId').val()
        };

        var tableData = '#tableData';

        init();

        $('#refreshType').click(function () {
            init();
        });

        function init() {
            initType();
        }

        function initType() {
            tools.dataLoading();
            $.get(ajax_url.file_type_data + '/' + page_param.paramTrainingSpecialId, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#type-template").html());
            $(tableData).html(template(data));
            initFile();
        }

        var file_param = {
            fileTypeId: '',
            originalFileName: ''
        }

        function initFile() {
            var files = $('.file');
            $.each(files, function (i, v) {
                file_param.fileTypeId = $(v).attr('data-id');
                sendFileAjax(v);
            });
        }

        function sendFileAjax(obj) {
            tools.dataLocalLoading(obj);
            $.get(ajax_url.file_data, file_param, function (data) {
                tools.dataLocalEndLoading(obj);
                fileListData(data, obj);
            });
        }

        /**
         * 文件列表数据
         * @param data 数据
         */
        function fileListData(data, obj) {
            var template = Handlebars.compile($("#file-template").html());
            Handlebars.registerHelper('file_size', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(tools.toSize(this.fileSize)));
            });
            $(obj).html(template(data));
        }

        /*
        编辑类型
        */
        $(tableData).delegate('.edit', "click", function () {
            var id = $(this).attr('data-id');
            var type = $(this).attr('data-type');
            $('#editFileTypeId').val(id);
            $('#editFileTypeName').val(type);
            $('#fileTypeEditModal').modal('show');
        });

        /*
        删除类型
        */
        $(tableData).delegate('.del', "click", function () {
            type_del($(this).attr('data-id'));
        });

        /*
        刷新文件
        */
        $(tableData).delegate('.refresh', "click", function () {
            var curFileTypeId = $(this).attr('data-id');
            localRefresh(curFileTypeId);
        });

        function localRefresh(curFileTypeId) {
            var files = $('.file');
            $.each(files, function (i, v) {
                var fileTypeId = $(v).attr('data-id');
                if (curFileTypeId === fileTypeId) {
                    file_param.fileTypeId = fileTypeId;
                    sendFileAjax(v);
                }
            });
        }

        /*
        添加文件
        */
        $(tableData).delegate('.add', "click", function () {
            var fileTypeId = $(this).attr('data-id');
            $('#addFileFileTypeId').val(fileTypeId);
            $('#fileAddModal').modal('show');
        });

        /*
        删除文件
        */
        $(tableData).delegate('.delFile', "click", function () {
            file_del($(this).attr('data-id'), $(this).attr('data-type'))
        });

        /*
        下载文件
        */
        $(tableData).delegate('.download', "click", function () {
            var id = $(this).attr('data-id');
            window.location.href = ajax_url.file_download + '/' + id;
        });

        /*
        搜索文件
        */
        $(tableData).delegate('.search', "click", function () {
            file_param.originalFileName = $(this).parent().prev().val();
            localRefresh($(this).attr('data-id'));
        });

        /*
        重置
        */
        $(tableData).delegate('.reset_search', "click", function () {
            $(this).parent().prev().val('');
            file_param.originalFileName = '';
            localRefresh($(this).attr('data-id'));
        });

        /*
        映射
        */
        $(tableData).delegate('.mapping', "click", function () {
            var id = $(this).attr('data-id');
            $.post(ajax_url.file_info, {trainingSpecialFileId: id}, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    $('#mappingFileTypeId').val(data.trainingSpecialFile.fileTypeId);
                    $('#mappingFileId').val(data.trainingSpecialFile.fileId);
                    $('#mappingOriginalFileName').val(data.trainingSpecialFile.originalFileName);
                    $('#mappingNewName').val(data.trainingSpecialFile.newName);
                    $('#mappingExt').val(data.trainingSpecialFile.ext);
                    $('#mappingFileSize').val(data.trainingSpecialFile.fileSize);
                    $('#mappingRelativePath').val(data.trainingSpecialFile.relativePath);

                    $('#fileMappingModal').modal('show');
                }
            });
        });

        /*
        取消映射
        */
        $(tableData).delegate('.cancelMapping', "click", function () {
            mapping_del($(this).attr('data-id'), $(this).attr('data-type'))
        });

        $('#addFileType').click(function () {
            var addFileTypeName = '#addFileTypeName';
            var param = {
                fileTypeName: $(addFileTypeName).val(),
                trainingSpecialId: page_param.paramTrainingSpecialId
            };
            validFileTypeName(addFileTypeName, ajax_url.file_type_save, param, '#fileTypeAddModal');
        });

        $('#editFileType').click(function () {
            var editFileTypeName = '#editFileTypeName';
            var param = {
                fileTypeName: $(editFileTypeName).val(),
                fileTypeId: $('#editFileTypeId').val()
            };
            validFileTypeName(editFileTypeName, ajax_url.file_type_update, param, '#fileTypeEditModal');
        });

        function validFileTypeName(obj, url, param, modal) {
            var fileTypeName = _.trim($(obj).val());
            if (fileTypeName.length <= 0 || fileTypeName.length > 100) {
                tools.validErrorDom(obj, '文件类型100个字符以内');
            } else {
                tools.validSuccessDom(obj);
                sendFileTypeAjax(url, param, modal);
            }
        }

        function sendFileTypeAjax(url, param, modal) {
            $.ajax({
                type: 'POST',
                url: url,
                data: param,
                success: function (data) {
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                init();
                                $(modal).modal('hide');
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
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

        /*
        文章删除
        */
        function type_del(fileTypeId) {
            Swal.fire({
                title: "确定删除该文件类型吗？",
                text: "文件类型删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    $.ajax({
                        type: 'POST',
                        url: ajax_url.file_type_del,
                        data: {fileTypeId: fileTypeId},
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
            });
        }

        /*
       文件删除
       */
        function file_del(trainingSpecialFileId, fileTypeId) {
            Swal.fire({
                title: "确定删除该文件吗？",
                text: "文件删除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    $.ajax({
                        type: 'POST',
                        url: ajax_url.file_del,
                        data: {trainingSpecialFileId: trainingSpecialFileId},
                        success: function (data) {
                            Messenger().post({
                                message: data.msg,
                                type: data.state ? 'success' : 'error',
                                showCloseButton: true
                            });

                            if (data.state) {
                                localRefresh(fileTypeId);
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
        }

        /*
        映射解除
        */
        function mapping_del(trainingSpecialFileId, fileTypeId) {
            Swal.fire({
                title: "确定该文件映射吗？",
                text: "文件映射解除！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    $.ajax({
                        type: 'POST',
                        url: ajax_url.file_cancel_mapping,
                        data: {trainingSpecialFileId: trainingSpecialFileId},
                        success: function (data) {
                            Messenger().post({
                                message: data.msg,
                                type: data.state ? 'success' : 'error',
                                showCloseButton: true
                            });

                            if (data.state) {
                                localRefresh(fileTypeId);
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
        }

        $('#addFile').click(function () {
            validOriginalFileName();
        });

        function validOriginalFileName() {
            var originalFileNameId = '#originalFileName';
            var originalFileName = _.trim($(originalFileNameId).val());
            if (originalFileName.length <= 0 || originalFileName.length > 300) {
                tools.validErrorDom(originalFileNameId, '文件原名300个字符以内');
            } else {
                tools.validSuccessDom(originalFileNameId);
                validNewName();
            }
        }

        function validNewName() {
            var newNameId = '#newName';
            var newName = _.trim($(newNameId).val());
            if (newName.length <= 0 || newName.length > 300) {
                tools.validErrorDom(newNameId, '文件新名300个字符以内');
            } else {
                tools.validSuccessDom(newNameId);
                validExt();
            }
        }

        function validExt() {
            var extId = '#ext';
            var ext = _.trim($(extId).val());
            if (ext.length <= 0 || ext.length > 20) {
                tools.validErrorDom(extId, '后缀20个字符以内');
            } else {
                tools.validSuccessDom(extId);
                validFileSize();
            }
        }

        function validFileSize() {
            var fileSizeId = '#fileSize';
            var fileSize = _.trim($(fileSizeId).val());
            if (fileSize.length <= 0) {
                tools.validErrorDom(fileSizeId, '请填写文件大小');
            } else {
                tools.validSuccessDom(fileSizeId);
                saveFileAjax();
            }
        }

        function saveFileAjax() {
            $.ajax({
                type: 'POST',
                url: ajax_url.file_save,
                data: $('#file_form').serialize(),
                success: function (data) {
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                localRefresh($('#addFileFileTypeId').val());
                                $('#fileAddModal').modal('hide');
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
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

        $('#mappingFile').click(function () {
            validMappingOriginalFileName();
        });

        function validMappingOriginalFileName() {
            var originalFileNameId = '#mappingOriginalFileName';
            var originalFileName = _.trim($(originalFileNameId).val());
            if (originalFileName.length <= 0 || originalFileName.length > 300) {
                tools.validErrorDom(originalFileNameId, '文件原名300个字符以内');
            } else {
                tools.validSuccessDom(originalFileNameId);
                validMappingNewName();
            }
        }

        function validMappingNewName() {
            var newNameId = '#mappingNewName';
            var newName = _.trim($(newNameId).val());
            if (newName.length <= 0 || newName.length > 300) {
                tools.validErrorDom(newNameId, '文件新名300个字符以内');
            } else {
                tools.validSuccessDom(newNameId);
                validMappingExt();
            }
        }

        function validMappingExt() {
            var extId = '#mappingExt';
            var ext = _.trim($(extId).val());
            if (ext.length <= 0 || ext.length > 20) {
                tools.validErrorDom(extId, '后缀20个字符以内');
            } else {
                tools.validSuccessDom(extId);
                validMappingFileSize();
            }
        }

        function validMappingFileSize() {
            var fileSizeId = '#mappingFileSize';
            var fileSize = _.trim($(fileSizeId).val());
            if (fileSize.length <= 0) {
                tools.validErrorDom(fileSizeId, '请填写文件大小');
            } else {
                tools.validSuccessDom(fileSizeId);
                validMappingRelativePath();
            }
        }

        function validMappingRelativePath() {
            var relativePathId = '#mappingRelativePath';
            var relativePath = _.trim($(relativePathId).val());
            if (relativePath.length <= 0 || relativePath.length > 800) {
                tools.validErrorDom(relativePathId, '相对路径800个字符以内');
            } else {
                tools.validSuccessDom(relativePathId);
                saveMappingAjax();
            }
        }

        function saveMappingAjax() {
            $.ajax({
                type: 'POST',
                url: ajax_url.file_mapping,
                data: $('#file_mapping_form').serialize(),
                success: function (data) {
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                localRefresh($('#mappingFileTypeId').val());
                                $('#fileMappingModal').modal('hide');
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
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