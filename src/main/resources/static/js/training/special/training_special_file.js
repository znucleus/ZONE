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
            page: '/web/menu/training/special'
        };

        navActive(ajax_url.page);

        var page_param = {
            paramTrainingSpecialId: $('#paramTrainingSpecialId').val()
        };

        var tableData = '#tableData';

        init();

        $('refreshType').click(function () {
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

        function initFile() {
            var files = $('.file');
            $.each(files, function (i, v) {
                var fileTypeId = $(v).attr('data-id');
                sendFileAjax(fileTypeId, v);
            });
        }

        function sendFileAjax(fileTypeId, obj) {
            tools.dataLocalLoading(obj);
            $.get(ajax_url.file_data + '/' + fileTypeId, function (data) {
                tools.dataLocalEndLoading(obj);
                fileListData(data, obj);
            });
        }

        /**
         * 文件列表数据
         * @param data 数据
         * @param obj 节点
         */
        function fileListData(data, obj) {
            var template = Handlebars.compile($("#file-template").html());
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
            var files = $('.file');
            $.each(files, function (i, v) {
                var fileTypeId = $(v).attr('data-id');
                if(curFileTypeId === fileTypeId){
                    sendFileAjax(fileTypeId, v);
                }
            });
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
    });