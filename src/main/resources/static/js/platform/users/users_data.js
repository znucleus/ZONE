//# sourceURL=users_data.js
require(["jquery", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4",
    "check.all", "jquery.address", "messenger"], function ($, Handlebars, navActive, Swal) {

    /*
     ajax url
     */
    function getAjaxUrl() {
        return {
            data: web_path + '/web/platform/users/data',
            role_data: web_path + '/web/platform/users/role/data',
            setting_role: web_path + '/web/platform/users/role',
            update: web_path + '/web/platform/users/update',
            del: web_path + '/web/platform/users/delete'
        };
    }

    /*
    参数
    */
    var param = {
        realName: '',
        username: '',
        email: '',
        mobile: '',
        audited: 1
    };

    var button_id = {
        audited: {
            id: '#audited',
            text: '已审核',
            tip: '查询中...'
        },
        unaudited: {
            id: '#unaudited',
            text: '未审核',
            tip: '查询中...'
        }
    };

    /*
     web storage key.
    */
    var webStorageKey = {
        REAL_NAME: 'PLATFORM_USERS_REAL_NAME_SEARCH',
        USERNAME: 'PLATFORM_USERS_USERNAME_SEARCH',
        EMAIL: 'PLATFORM_USERS_EMAIL_SEARCH',
        MOBILE: 'PLATFORM_USERS_MOBILE_SEARCH',
        AUDITED: 'PLATFORM_USERS_AUDITED_SEARCH'
    };

    // 预编译模板
    var template = Handlebars.compile($("#operator_button").html());

    // datatables 初始化
    var tableElement = $('#dataTable');

    var myTable = tableElement.DataTable({
        autoWidth: false,
        drawCallback: function (oSettings) {
            $('#checkall').prop('checked', false);
            // 调用全选插件
            $.fn.check({checkall_name: "checkall", checkbox_name: "check"});
        },
        searching: false,
        "processing": true, // 打开数据加载时的等待效果
        "serverSide": true,// 打开后台分页
        "aaSorting": [[12, 'desc']],// 排序
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
            {"data": "username"},
            {"data": "email"},
            {"data": "mobile"},
            {"data": "idCard"},
            {"data": "roleName"},
            {"data": "usersTypeName"},
            {"data": "enabled"},
            {"data": "accountNonLocked"},
            {"data": "langKey"},
            {"data": "joinDate"},
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
                    return '<input type="checkbox" value="' + c.applicationId + '" name="check"/>';
                }
            },
            {
                targets: 7,
                orderable: false
            },
            {
                targets: 9,
                render: function (a, b, c, d) {
                    var v = '';
                    if (c.enabled === 0 || c.enabled == null) {
                        v = "<span class='text-danger'>已注销</span>";
                    } else {
                        v = "<span class='text-info'>正常</span>";
                    }
                    return v;
                }
            },
            {
                targets: 10,
                render: function (a, b, c, d) {
                    var v = '';
                    if (c.accountNonLocked === 0 || c.accountNonLocked == null) {
                        v = "<span class='text-warning'>已锁定</span>";
                    } else {
                        v = "<span class='text-info'>正常</span>";
                    }
                    return v;
                }
            },
            {
                targets: 13,
                orderable: false,
                render: function (a, b, c, d) {

                    var context = {
                        func: [
                            {
                                "name": "设置角色",
                                "css": "role",
                                "type": "info",
                                "id": c.username,
                                "role": c.roleName
                            }
                        ]
                    };

                    if (c.enabled === 1) {
                        context.func.push({
                            "name": "注销",
                            "css": "del",
                            "type": "danger",
                            "id": c.username,
                            "role": c.roleName
                        });
                    } else {
                        context.func.push({
                            "name": "恢复",
                            "css": "recovery",
                            "type": "warning",
                            "id": c.username,
                            "role": c.roleName
                        });
                    }

                    if (c.accountNonLocked === 1) {
                        context.func.push({
                            "name": "锁定",
                            "css": "locked",
                            "type": "secondary",
                            "id": c.username,
                            "role": c.roleName
                        });
                    } else {
                        context.func.push({
                            "name": "解锁",
                            "css": "unlocked",
                            "type": "purple",
                            "id": c.username,
                            "role": c.roleName
                        });
                    }

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
            tableElement.delegate('.del', "click", function () {
                cancel($(this).attr('data-id'));
            });

            tableElement.delegate('.recovery', "click", function () {
                recovery($(this).attr('data-id'));
            });

            tableElement.delegate('.role', "click", function () {
                role($(this).attr('data-id'), $(this).attr('data-role'));
            });
            // 初始化搜索框中内容
            initSearchInput();
        }
    });

    var global_button = '<button type="button" id="dels" class="btn btn-outline btn-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
        '  <button type="button" id="recoveries" class="btn btn-outline btn-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
        '  <button type="button" id="locked" class="btn btn-outline btn-secondary btn-sm"><i class="fa fa-lock"></i>批量锁定</button>' +
        '  <button type="button" id="unlocked" class="btn btn-outline btn-purple btn-sm"><i class="fa fa-unlock"></i>批量解锁</button>' +
        '  <button type="button" id="refresh" class="btn btn-outline btn-default btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
    $('#global_button').append(global_button);

    $('#dels').click(function () {
        cancels();
    });

    $('#recoveries').click(function () {
        recoveries();
    });

    function getParamId() {
        return {
            realName: '#search_real_name',
            username: '#search_username',
            email: '#search_email',
            mobile: '#search_mobile',
            audited: ''
        };
    }

    /*
     得到参数
     */
    function getParam() {
        return param;
    }

    /**
     * init pass tab param.
     * @returns {{username: (*|jQuery), mobile: (*|jQuery), usersType: (*|jQuery)}}
     */
    function initParam() {
        param.realName = $(getParamId().realName).val();
        param.username = $(getParamId().username).val();
        param.email = $(getParamId().email).val();
        param.mobile = $(getParamId().mobile).val();

        if ($(button_id.audited.id).hasClass('active')) {
            param.audited = 1;
        } else if ($(button_id.unaudited.id).hasClass('active')) {
            param.audited = 2;
        }

        if (typeof(Storage) !== "undefined") {
            sessionStorage.setItem(webStorageKey.REAL_NAME, param.realName);
            sessionStorage.setItem(webStorageKey.USERNAME, param.username);
            sessionStorage.setItem(webStorageKey.EMAIL, param.email);
            sessionStorage.setItem(webStorageKey.MOBILE, param.mobile);
            sessionStorage.setItem(webStorageKey.AUDITED, param.audited);
        }
    }

    /*
     初始化搜索内容
    */
    function initSearchContent() {
        var realName = null;
        var username = null;
        var email = null;
        var mobile = null;
        var audited = null;
        if (typeof(Storage) !== "undefined") {
            realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
            username = sessionStorage.getItem(webStorageKey.USERNAME);
            email = sessionStorage.getItem(webStorageKey.EMAIL);
            mobile = sessionStorage.getItem(webStorageKey.MOBILE);
            audited = sessionStorage.getItem(webStorageKey.AUDITED);
        }
        if (realName !== null) {
            param.realName = realName;
        }

        if (username !== null) {
            param.username = username;
        }

        if (email !== null) {
            param.email = email;
        }

        if (mobile !== null) {
            param.mobile = mobile;
        }

        if (audited !== null) {
            param.audited = audited;
        }
    }

    /*
    初始化搜索框
    */
    function initSearchInput() {
        var realName = null;
        var username = null;
        var email = null;
        var mobile = null;
        var audited = null;
        if (typeof(Storage) !== "undefined") {
            realName = sessionStorage.getItem(webStorageKey.REAL_NAME);
            username = sessionStorage.getItem(webStorageKey.USERNAME);
            email = sessionStorage.getItem(webStorageKey.EMAIL);
            mobile = sessionStorage.getItem(webStorageKey.MOBILE);
            audited = sessionStorage.getItem(webStorageKey.AUDITED);
        }
        if (realName !== null) {
            $(getParamId().realName).val(realName);
        }

        if (username !== null) {
            $(getParamId().username).val(username);
        }

        if (email !== null) {
            $(getParamId().email).val(email);
        }

        if (mobile !== null) {
            $(getParamId().mobile).val(mobile);
        }

        if (audited !== null) {
            $(button_id.audited.id).removeClass('active');
            $(button_id.unaudited.id).removeClass('active');
            if (Number(audited) === 1) {
                $(button_id.audited.id).addClass('active');
            } else if (Number(audited) === 2) {
                $(button_id.unaudited.id).addClass('active');
            } else {
                $(button_id.audited.id).addClass('active');
            }
        } else {
            $(button_id.audited.id).addClass('active');
        }
    }

    function cleanParam() {
        $(getParamId().realName).val('');
        $(getParamId().username).val('');
        $(getParamId().email).val('');
        $(getParamId().mobile).val('');
    }

    $(getParamId().realName).keyup(function (event) {
        if (event.keyCode === 13) {
            initParam();
            myTable.ajax.reload();
        }
    });

    $(getParamId().username).keyup(function (event) {
        if (event.keyCode === 13) {
            initParam();
            myTable.ajax.reload();
        }
    });

    $(getParamId().email).keyup(function (event) {
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

    $(button_id.audited.id).click(function () {
        $(button_id.audited.id).addClass('active');
        $(button_id.unaudited.id).removeClass('active');
        initParam();
        myTable.ajax.reload();
    });

    $(button_id.unaudited.id).click(function () {
        $(button_id.unaudited.id).addClass('active');
        $(button_id.audited.id).removeClass('active');
        initParam();
        myTable.ajax.reload();
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

    /**
     * 处理角色
     * @param username
     * @param role
     */
    function role(username, role) {
        $.post(getAjaxUrl().role_data, {username: username}, function (data) {
            var html = roleData(data);
            $('#roles').html(html);
            var roleNames = role.split(' ');
            var roles = $('.role_set');
            for (var i = 0; i < roles.length; i++) {
                for (var j = 0; j < roleNames.length; j++) {
                    if ($(roles[i]).text() === roleNames[j]) {
                        $(roles[i]).prev().prop('checked', true);
                    }
                }
            }
            $('#roleUsername').val(username);
            $('#roleModal').modal('toggle');
        });
    }

    /**
     * 角色数据展现
     * @param data json数据
     */
    function roleData(data) {
        var template = Handlebars.compile($("#role-template").html());
        return template(data);
    }

    /*
     关闭角色设置modal
     */
    $('#roleModalMiss').click(function () {
        $('#role_error_msg').addClass('hidden').removeClass('text-danger').text('');
        $('#roleModal').modal('hide');
    });

    // 保存角色
    $("#saveRole").click(function () {
        var roles = $('input[name="role"]:checked');
        if (roles.length <= 0) {
            $('#role_error_msg').removeClass('hidden').addClass('text-danger').text('请至少选择一个角色');
        } else {
            $('#role_error_msg').addClass('hidden').removeClass('text-danger').text('');
            var r = [];
            for (var i = 0; i < roles.length; i++) {
                r.push($(roles[i]).val());
            }
            $.post(web_path + getAjaxUrl().saveRole, {
                username: $('#roleUsername').val(),
                roles: r.join(",")
            }, function (data) {
                if (data.state) {
                    $('#roleModal').modal('toggle');
                    if (passTable != null) {
                        passTable.ajax.reload();
                    }
                    if (waitTable != null) {
                        waitTable.ajax.reload();
                    }
                } else {
                    $('#role_error_msg').removeClass('hidden').addClass('text-danger').text(data.msg);
                }
            });
        }
    });

    /**
     * 注销
     * @param username
     */
    function cancel(username) {
        Swal.fire({
            title: "确定注销 '" + username + "' 吗？",
            text: "账号注销！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: '#d33',
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            preConfirm: function () {
                toCancel(username);
            }
        });
    }

    /**
     * 恢复
     * @param username
     */
    function recovery(username) {
        Swal.fire({
            title: "确定恢复 '" + username + "' 吗？",
            text: "账号恢复！",
            type: "success",
            showCancelButton: true,
            confirmButtonColor: '#d33',
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            preConfirm: function () {
                toRecovery(username);
            }
        });
    }

    /*
     批量注销
     */
    function cancels() {
        var userIds = [];
        var ids = $('input[name="check"]:checked');
        for (var i = 0; i < ids.length; i++) {
            userIds.push($(ids[i]).val());
        }

        if (userIds.length > 0) {
            Swal.fire({
                title: "确定注销选中的用户吗？",
                text: "账号注销！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    toCancels(userIds);
                }
            });
        } else {
            Messenger().post("未发现有选中的用户!");
        }
    }

    /*
     批量恢复
     */
    function recoveries() {
        var userIds = [];
        var ids = $('input[name="check"]:checked');
        for (var i = 0; i < ids.length; i++) {
            userIds.push($(ids[i]).val());
        }

        if (userIds.length > 0) {
            Swal.fire({
                title: "确定恢复选中的用户吗？",
                text: "账号恢复！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    toRecoveries(userIds);
                }
            });
        } else {
            Messenger().post("未发现有选中的用户!");
        }
    }

    function toCancel(username) {
        sendUpdateEnabledAjax(username, '注销', 0);
    }

    function toRecovery(username) {
        sendUpdateEnabledAjax(username, '恢复', 1);
    }

    function toCancels(ids) {
        sendUpdateEnabledAjax(ids.join(","), '批量注销', 0);
    }

    function toRecoveries(ids) {
        sendUpdateEnabledAjax(ids.join(","), '批量恢复', 1);
    }

    /**
     * 发送更新用户状态 ajax.
     * @param username
     * @param message
     * @param enabled
     */
    function sendUpdateEnabledAjax(username, message, enabled) {
        $.ajax({
            type: 'POST',
            url: getAjaxUrl().update,
            data: {userIds: username, enabled: enabled},
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