//# sourceURL=politics_data.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4", "check.all", "jquery.address", "messenger"],
    function ($, _, tools, Handlebars, navActive, Swal) {

        /*
         参数
        */
        var param = {
            politicalLandscapeName: ''
        };

        var add_param = {
            politicalLandscapeName: ''
        };

        var edit_param = {
            politicalLandscapeId: '',
            politicalLandscapeName: ''
        };

        var add_param_id = {
            politicalLandscapeName: '#addPoliticalLandscapeName'
        };

        var edit_param_id = {
            politicalLandscapeId: '#editPoliticalLandscapeId',
            politicalLandscapeName: '#editPoliticalLandscapeName'
        };

        var button_id = {
            add: {
                id: '#add',
                text: '保存',
                tip: '保存中...'
            },
            edit: {
                id: '#edit',
                text: '保存',
                tip: '保存中...'
            }
        };

        /*
        web storage key.
       */
        var webStorageKey = {
            POLITICAL_LANDSCAPE_NAME: 'DATA_POLITICS_LANDSCAPE_NAME_SEARCH'
        };

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/data/politics/data',
                save: web_path + '/web/data/politics/save',
                check_add_name: web_path + '/web/data/politics/check/add/name',
                update: web_path + '/web/data/politics/update',
                check_edit_name: web_path + '/web/data/politics/check/edit/name',
                page: '/web/menu/data/politics'
            };
        }

        navActive(getAjaxUrl().page);

        // 预编译模板
        var template = Handlebars.compile($("#operator_button").html());

        var tableElement = $('#dataTable');

        var myTable = tableElement.DataTable({
            autoWidth: false,
            searching: false,
            "processing": true, // 打开数据加载时的等待效果
            "serverSide": true,// 打开后台分页
            "aaSorting": [[0, 'desc']],// 排序
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
                {"data": "politicalLandscapeId"},
                {"data": "politicalLandscapeName"},
                {"data": null}
            ],
            columnDefs: [
                {
                    targets: 2,
                    orderable: false,
                    render: function (a, b, c, d) {
                        var context =
                            {
                                func: [
                                    {
                                        "name": "编辑",
                                        "css": "edit",
                                        "type": "primary",
                                        "id": c.politicalLandscapeId,
                                        "value": c.politicalLandscapeName
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
            "dom": "<'row'<'col-lg-2 col-md-12'l><'#global_button.col-lg-4 col-md-12'><'col-lg-6 col-md-12'<'#mytoolbox'>>r>" +
                "t" +
                "<'row'<'col-sm-5'i><'col-sm-7'p>>",
            initComplete: function () {
                tableElement.delegate('.edit', "click", function () {
                    edit($(this).attr('data-id'), $(this).attr('data-value'));
                });

                // 初始化搜索框中内容
                initSearchInput();
            }
        });

        var html = '<div class="row ">' +
            '<div class="col-md-9 pd-t-2"><input type="text" id="search_politics" class="form-control form-control-sm" placeholder="政治面貌" /></div>' +
            '<div class="col-md-3 pd-t-2 text-right "><div class="btn-group" role="group"><button type="button" id="search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-search"></i>搜索</button>' +
            '  <button type="button" id="reset_search" class="btn btn-outline-secondary btn-sm"><i class="fa fa-repeat"></i>重置</button></div></div>' +
            '</div>';
        $('#mytoolbox').append(html);

        var global_button = '<button type="button" id="politics_add" class="btn btn-outline-primary btn-sm"><i class="fa fa-plus"></i>添加</button>' +
            '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').append(global_button);

        /*
         参数id
         */
        function getParamId() {
            return {
                politicalLandscapeName: '#search_politics'
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
            param.politicalLandscapeName = $(getParamId().politicalLandscapeName).val();
            if (typeof (Storage) !== "undefined") {
                sessionStorage.setItem(webStorageKey.POLITICAL_LANDSCAPE_NAME, param.politicalLandscapeName);
            }
        }

        /*
        初始化搜索内容
         */
        function initSearchContent() {
            var politicalLandscapeName = null;
            if (typeof (Storage) !== "undefined") {
                politicalLandscapeName = sessionStorage.getItem(webStorageKey.POLITICAL_LANDSCAPE_NAME);
            }
            if (politicalLandscapeName !== null) {
                param.politicalLandscapeName = politicalLandscapeName;
            }
        }

        /*
       初始化搜索框
        */
        function initSearchInput() {
            var politicalLandscapeName = null;
            if (typeof (Storage) !== "undefined") {
                politicalLandscapeName = sessionStorage.getItem(webStorageKey.POLITICAL_LANDSCAPE_NAME);
            }
            if (politicalLandscapeName !== null) {
                $(getParamId().politicalLandscapeName).val(politicalLandscapeName);
            }
        }

        /*
         清空参数
         */
        function cleanParam() {
            $(getParamId().politicalLandscapeName).val('');
        }

        $(getParamId().politicalLandscapeName).keyup(function (event) {
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
        $('#politics_add').click(function () {
            $('#addModal').modal('show');
        });

        /*
         编辑页面
         */
        function edit(politicalLandscapeId, politicalLandscapeName) {
            $(edit_param_id.politicalLandscapeId).val(politicalLandscapeId);
            $(edit_param_id.politicalLandscapeName).val(politicalLandscapeName);
            $('#editModal').modal('show');
        }

        function initDataParam() {
            add_param.politicalLandscapeName = _.trim($(add_param_id.politicalLandscapeName).val());

            edit_param.politicalLandscapeId = $(edit_param_id.politicalLandscapeId).val();
            edit_param.politicalLandscapeName = _.trim($(edit_param_id.politicalLandscapeName).val());
        }

        /*
         即时检验
         */
        $(add_param_id.politicalLandscapeName).blur(function () {
            initDataParam();
            var politicalLandscapeName = $(add_param_id.politicalLandscapeName).val();
            if (politicalLandscapeName.length <= 0 || politicalLandscapeName.length > 30) {
                tools.validErrorDom(add_param_id.politicalLandscapeName, '政治面貌30个字符以内');
            } else {
                $.post(getAjaxUrl().check_add_name, add_param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(add_param_id.politicalLandscapeName);
                    } else {
                        tools.validErrorDom(add_param_id.politicalLandscapeName, data.msg);
                    }
                });
            }
        });

        $(edit_param_id.politicalLandscapeName).blur(function () {
            initDataParam();
            var politicalLandscapeName = $(edit_param_id.politicalLandscapeName).val();
            if (politicalLandscapeName.length <= 0 || politicalLandscapeName.length > 30) {
                tools.validErrorDom(edit_param_id.politicalLandscapeName, '政治面貌30个字符以内');
            } else {
                $.post(getAjaxUrl().check_edit_name, edit_param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(edit_param_id.politicalLandscapeName);
                    } else {
                        tools.validErrorDom(edit_param_id.politicalLandscapeName, data.msg);
                    }
                });
            }
        });

        $(button_id.add.id).click(function () {
            initDataParam();
            validAddName();
        });

        $(button_id.edit.id).click(function () {
            initDataParam();
            validEditName();
        });

        function validAddName() {
            var politicalLandscapeName = $(add_param_id.politicalLandscapeName).val();
            if (politicalLandscapeName.length <= 0 || politicalLandscapeName.length > 30) {
                tools.validErrorDom(add_param_id.politicalLandscapeName, '政治面貌30个字符以内');
            } else {
                $.post(getAjaxUrl().check_add_name, add_param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(add_param_id.politicalLandscapeName);
                        sendAddAjax();
                    } else {
                        tools.validErrorDom(add_param_id.politicalLandscapeName, data.msg);
                    }
                });
            }
        }

        function validEditName() {
            var politicalLandscapeName = $(edit_param_id.politicalLandscapeName).val();
            if (politicalLandscapeName.length <= 0 || politicalLandscapeName.length > 30) {
                tools.validErrorDom(edit_param_id.politicalLandscapeName, '政治面貌30个字符以内');
            } else {
                $.post(getAjaxUrl().check_edit_name, edit_param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(edit_param_id.politicalLandscapeName);
                        sendEditAjax();
                    } else {
                        tools.validErrorDom(edit_param_id.politicalLandscapeName, data.msg);
                    }
                });
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAddAjax() {
            tools.buttonLoading(button_id.add.id, button_id.add.tip);
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().save,
                data: add_param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.add.id, button_id.add.text);
                    var globalError = $('#globalAddError');
                    if (data.state) {
                        globalError.text('');
                        $(add_param_id.politicalLandscapeName).val('');
                        $('#addModal').modal('hide');
                        myTable.ajax.reload();
                        Swal.fire('保存成功', data.msg, 'success');
                    } else {
                        globalError.text(data.msg);
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.add.id, button_id.add.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        function sendEditAjax() {
            tools.buttonLoading(button_id.edit.id, button_id.edit.tip);
            $.ajax({
                type: 'POST',
                url: getAjaxUrl().update,
                data: edit_param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.edit.id, button_id.edit.text);
                    var globalError = $('#globalEditError');
                    if (data.state) {
                        globalError.text('');
                        $('#editModal').modal('hide');
                        $(edit_param_id.politicalLandscapeId).val('');
                        $(edit_param_id.politicalLandscapeName).val('');
                        myTable.ajax.reload();
                        Swal.fire('保存成功', data.msg, 'success');
                    } else {
                        globalError.text(data.msg);
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.edit.id, button_id.edit.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

    });