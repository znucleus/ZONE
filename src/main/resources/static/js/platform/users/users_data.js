//# sourceURL=users_data.js
require(["jquery", "tools", "handlebars", "nav.active", "sweetalert2", "responsive.bootstrap4",
    "check.all", "jquery.address", "messenger"], function ($, tools, Handlebars, navActive, Swal) {

    /*
     ajax url
     */
    function getAjaxUrl() {
        return {
            data: web_path + '/web/platform/users/paging',
            role_data: web_path + '/web/platform/users/role/data',
            setting_role: web_path + '/web/platform/users/role/save',
            update_enabled: web_path + '/web/platform/users/update/enabled',
            update_locked: web_path + '/web/platform/users/update/locked',
            del: web_path + '/web/platform/users/delete',
            reset_password: web_path + '/web/platform/users/update/password',
            page: '/web/menu/platform/users'
        };
    }

    navActive(getAjaxUrl().page);

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
        },
        saveRole: {
            id: '#saveRole',
            text: '确定',
            tip: '保存中...'
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
        stateSave: true,// 打开客户端状态记录功能。这个数据是记录在cookies中的，打开了这个记录后，即使刷新一次页面，或重新打开浏览器，之前的状态都是保存下来的
        stateSaveCallback: function (settings, data) {
            localStorage.setItem('PLATFORM_USERS_' + settings.sInstance, JSON.stringify(data))
        },
        stateLoadCallback: function (settings) {
            return JSON.parse(localStorage.getItem('PLATFORM_USERS_' + settings.sInstance))
        },
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
                    return '<input type="checkbox" value="' + c.username + '" name="check"/>';
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

                    var context = {func: []};

                    var param = getParam();
                    var audited = Number(param.audited);

                    if (c.verifyMailbox !== null && c.verifyMailbox === 1) {
                        context.func.push(
                            {
                                "name": "设置角色",
                                "css": "role",
                                "type": "info",
                                "id": c.username,
                                "role": c.roleName
                            });
                    }

                    if (audited === 1) {
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

                        context.func.push({
                            "name": "重置密码",
                            "css": "resetPassword",
                            "type": "dark",
                            "id": c.username,
                            "role": c.roleName
                        });
                    } else if (audited === 2) {
                        context.func.push({
                            "name": "删除",
                            "css": "delete",
                            "type": "danger",
                            "id": c.username,
                            "role": ''
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

            tableElement.delegate('.locked', "click", function () {
                locked($(this).attr('data-id'));
            });

            tableElement.delegate('.unlocked', "click", function () {
                unlocked($(this).attr('data-id'));
            });

            tableElement.delegate('.role', "click", function () {
                role($(this).attr('data-id'), $(this).attr('data-role'));
            });

            tableElement.delegate('.delete', "click", function () {
                usersDelete($(this).attr('data-id'));
            });

            tableElement.delegate('.resetPassword', "click", function () {
                resetPassword($(this).attr('data-id'));
            });
            // 初始化搜索框中内容
            initSearchInput();
        }
    });

    var global_button = $('#global_button');

    function initGlobalButton() {
        var global_button = '<button type="button" id="dels" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量注销</button>' +
            '  <button type="button" id="recoveries" class="btn btn-outline-warning btn-sm"><i class="fa fa-reply-all"></i>批量恢复</button>' +
            '  <button type="button" id="locked" class="btn btn-outline-secondary btn-sm"><i class="fa fa-lock"></i>批量锁定</button>' +
            '  <button type="button" id="unlocked" class="btn btn-outline-purple btn-sm"><i class="fa fa-unlock"></i>批量解锁</button>';
        if (Number(param.audited) === 2) {
            global_button += '  <button type="button" id="deletes" class="btn btn-outline-danger btn-sm"><i class="fa fa-trash-o"></i>批量删除</button>';
        }

        global_button += '  <button type="button" id="refresh" class="btn btn-light btn-sm"><i class="fa fa-refresh"></i>刷新</button>';
        $('#global_button').html(global_button);
    }

    global_button.delegate('#dels', "click", function () {
        cancels();
    });

    global_button.delegate('#recoveries', "click", function () {
        recoveries();
    });

    global_button.delegate('#locked', "click", function () {
        lockeds();
    });

    global_button.delegate('#unlocked', "click", function () {
        unlockeds();
    });

    global_button.delegate('#deletes', "click", function () {
        usersDeletes();
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

        if (typeof (Storage) !== "undefined") {
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
        if (typeof (Storage) !== "undefined") {
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
        if (typeof (Storage) !== "undefined") {
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

        initGlobalButton();
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
        initGlobalButton();
        myTable.ajax.reload();
    });

    $(button_id.unaudited.id).click(function () {
        $(button_id.unaudited.id).addClass('active');
        $(button_id.audited.id).removeClass('active');
        initParam();
        initGlobalButton();
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

    global_button.delegate('#refresh', "click", function () {
        myTable.ajax.reload();
    });

    /**
     * 处理角色
     * @param username
     * @param role
     */
    function role(username, role) {
        $.post(getAjaxUrl().role_data, {username: username}, function (data) {
            roleData(data);
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
        $('#roles').html(template(data));
    }

    /*
     关闭角色设置modal
     */
    $('#roleModalMiss').click(function () {
        $('#globalRoleError').text('');
        $('#roleModal').modal('hide');
    });

    // 保存角色
    $(button_id.saveRole.id).click(function () {
        var roles = $('input[name="role"]:checked');
        if (roles.length <= 0) {
            $('#globalRoleError').text('请至少选择一个角色');
        } else {
            $('#globalRoleError').text('');
            var r = [];
            for (var i = 0; i < roles.length; i++) {
                r.push($(roles[i]).val());
            }
            sendRoleAjax(r.join(','));
        }
    });

    function sendRoleAjax(roles) {
        tools.buttonLoading(button_id.saveRole.id, button_id.saveRole.tip);
        $.post(getAjaxUrl().setting_role, {
            username: $('#roleUsername').val(),
            roles: roles
        }, function (data) {
            tools.buttonEndLoading(button_id.saveRole.id, button_id.saveRole.text);
            if (data.state) {
                $('#roleModal').modal('toggle');
                myTable.ajax.reload(null, false);
            } else {
                $('#globalRoleError').text(data.msg);
            }
        });
    }

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
            confirmButtonColor: '#27dd4b',
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
                confirmButtonColor: '#27dd4b',
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
        sendUpdateEnabledAjax(username, 0);
    }

    function toRecovery(username) {
        sendUpdateEnabledAjax(username, 1);
    }

    function toCancels(ids) {
        sendUpdateEnabledAjax(ids.join(","), 0);
    }

    function toRecoveries(ids) {
        sendUpdateEnabledAjax(ids.join(","), 1);
    }

    /**
     * 发送更新用户状态 ajax.
     * @param username
     * @param enabled
     */
    function sendUpdateEnabledAjax(username, enabled) {
        $.ajax({
            type: 'POST',
            url: getAjaxUrl().update_enabled,
            data: {userIds: username, enabled: enabled},
            success: function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });

                if (data.state) {
                    myTable.ajax.reload(null, false);
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

    /**
     * 锁定
     * @param username
     */
    function locked(username) {
        Swal.fire({
            title: "确定锁定 '" + username + "' 吗？",
            text: "账号锁定！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: '#534429',
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            preConfirm: function () {
                toLocked(username);
            }
        });
    }

    /**
     * 解锁
     * @param username
     */
    function unlocked(username) {
        Swal.fire({
            title: "确定解锁 '" + username + "' 吗？",
            text: "账号解锁！",
            type: "success",
            showCancelButton: true,
            confirmButtonColor: '#082e0f',
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            preConfirm: function () {
                toUnlocked(username);
            }
        });
    }

    /*
     批量锁定
     */
    function lockeds() {
        var userIds = [];
        var ids = $('input[name="check"]:checked');
        for (var i = 0; i < ids.length; i++) {
            userIds.push($(ids[i]).val());
        }

        if (userIds.length > 0) {
            Swal.fire({
                title: "确定锁定选中的用户吗？",
                text: "账号锁定！",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: '#534429',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    toLockeds(userIds);
                }
            });
        } else {
            Messenger().post("未发现有选中的用户!");
        }
    }

    /*
     批量解锁
     */
    function unlockeds() {
        var userIds = [];
        var ids = $('input[name="check"]:checked');
        for (var i = 0; i < ids.length; i++) {
            userIds.push($(ids[i]).val());
        }

        if (userIds.length > 0) {
            Swal.fire({
                title: "确定解锁选中的用户吗？",
                text: "账号解锁！",
                type: "success",
                showCancelButton: true,
                confirmButtonColor: '#27dd4b',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    toUnlockeds(userIds);
                }
            });
        } else {
            Messenger().post("未发现有选中的用户!");
        }
    }

    function toLocked(username) {
        sendUpdateLockedAjax(username, 0);
    }

    function toUnlocked(username) {
        sendUpdateLockedAjax(username, 1);
    }

    function toLockeds(ids) {
        sendUpdateLockedAjax(ids.join(","), 0);
    }

    function toUnlockeds(ids) {
        sendUpdateLockedAjax(ids.join(","), 1);
    }

    /**
     * 发送更新用户锁定 ajax.
     * @param username
     * @param locked
     */
    function sendUpdateLockedAjax(username, locked) {
        $.ajax({
            type: 'POST',
            url: getAjaxUrl().update_locked,
            data: {userIds: username, locked: locked},
            success: function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });

                if (data.state) {
                    myTable.ajax.reload(null, false);
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

    function usersDelete(username) {
        Swal.fire({
            title: "确定删除 '" + username + "' 吗？",
            text: "账号删除！",
            type: "error",
            showCancelButton: true,
            confirmButtonColor: '#d33',
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            preConfirm: function () {
                toDelete(username);
            }
        });
    }

    /*
     批量删除
     */
    function usersDeletes() {
        var userIds = [];
        var ids = $('input[name="check"]:checked');
        for (var i = 0; i < ids.length; i++) {
            userIds.push($(ids[i]).val());
        }

        if (userIds.length > 0) {
            Swal.fire({
                title: "确定删除选中的用户吗？",
                text: "账号删除！",
                type: "error",
                showCancelButton: true,
                confirmButtonColor: '#d33',
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                preConfirm: function () {
                    toDeletes(userIds);
                }
            });
        } else {
            Messenger().post("未发现有选中的用户!");
        }
    }

    function toDelete(username) {
        sendDeleteAjax(username);
    }

    function toDeletes(ids) {
        sendDeleteAjax(ids.join(","));
    }

    function sendDeleteAjax(username) {
        $.ajax({
            type: 'POST',
            url: getAjaxUrl().del,
            data: {userIds: username},
            success: function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });

                if (data.state) {
                    myTable.ajax.reload(null, false);
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

    function resetPassword(username) {
        Swal.fire({
            title: "确定重置 '" + username + "' 的密码吗？",
            text: "账号密码重置！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: '#dcdd46',
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            preConfirm: function () {
                sendResetPasswordAjax(username);
            }
        });
    }

    function sendResetPasswordAjax(username) {
        $.ajax({
            type: 'POST',
            url: getAjaxUrl().reset_password,
            data: {username: username},
            success: function (data) {
                Swal.fire({
                    title: data.msg,
                    type: data.state ? 'success' : 'error',
                    confirmButtonText: "确定",
                    preConfirm: function () {
                        if (data.state) {
                            myTable.ajax.reload(null, false);
                        }
                    }
                });
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