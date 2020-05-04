//# sourceURL=training_special_file.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         ajax url.
         */
        var ajax_url = {
            file_type_data: web_path + '/web/training/special/file/type/data',
            file_type_save: web_path + '/web/training/special/file/type/save',
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
                $.get(ajax_url.file_data + '/' + fileTypeId, function (data) {
                    fileListData(data, v);
                });
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

        $('#addFileType').click(function () {
            var addFileTypeName = '#addFileTypeName';
            var param = {
                fileTypeName: $(addFileTypeName).val(),
                trainingSpecialId: page_param.paramTrainingSpecialId
            };
            validFileTypeName(addFileTypeName, ajax_url.file_type_save, param);
        });

        function validFileTypeName(obj, url, param) {
            var fileTypeName = _.trim($(obj).val());
            if (fileTypeName.length <= 0 || fileTypeName.length > 100) {
                tools.validErrorDom(obj, '文件类型100个字符以内');
            } else {
                tools.validSuccessDom(obj);
                sendFileTypeAjax(url, param);
            }
        }

        function sendFileTypeAjax(url, param) {
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
                                $('#fileTypeAddModal').modal('hide');
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