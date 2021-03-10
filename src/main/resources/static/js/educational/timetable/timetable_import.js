//# sourceURL=timetable_import.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "select2-zh-CN", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            query:web_path + '/web/educational/timetable/import/semesters',
            save: web_path + '/web/educational/timetable/import/save',
            page: '/web/menu/educational/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            username: '#username',
            password: '#password',
            schoolYear:'#schoolYear'
        };

        var button_id = {
            query: {
                id: '#query',
                text: '查询',
                tip: '查询中...'
            },
            save: {
                id: '#save',
                text: '确定',
                tip: '导入中...'
            },
            cancel: {
                id: '#cancel'
            }
        };

        /*
         参数
         */
        var param = {
            username: '',
            password: '',
            schoolYear:''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.username = _.trim($(param_id.username).val());
            param.password = _.trim($(param_id.password).val());
            param.schoolYear = $(param_id.schoolYear).val();
        }

        $(param_id.username).blur(function (){
            initParam();
           var username =  param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        });

        $(param_id.password).blur(function (){
            initParam();
            var password =  param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        });

        init();

        function init(){
            initSelect2();
        }

        $(button_id.query.id).click(function (){
            initParam();
            validUsername('query');
        });

        function initSchoolYear() {
            $('#queryError').val('');
            tools.buttonLoading(button_id.query.id, button_id.query.tip);
            $.get(ajax_url.query, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.query.id, button_id.query.text);
                if(data.results.length > 0){
                    var schoolYearSelect2 = $(param_id.schoolYear).select2({data: data.results});
                    $.each(data.results, function (i, v) {
                        if(v.selected){
                            schoolYearSelect2.val(v.id).trigger("change");
                        }
                    });
                    $('#saveForm').css('display', '');
                    $('#queryForm').css('display', 'none');
                } else {
                    $('#queryError').val('未查询到数据，请检查账号密码或稍后重试');
                }
            });

        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(button_id.save.id).click(function (){
            initParam();
            validUsername('save');
        });

        $(button_id.cancel.id).click(function (){
            $('#saveForm').css('display', 'none');
            $('#queryForm').css('display', '');
        });

        function validUsername(type) {
            var username = param.username;
            if (username !== '') {
                tools.validSuccessDom(param_id.username);
                validPassword(type);
            } else {
                tools.validErrorDom(param_id.username, '请填写新教务系统账号');
            }
        }

        function validPassword(type) {
            var password = param.password;
            if (password !== '') {
                tools.validSuccessDom(param_id.password);
                if(type === 'save'){
                    validSchoolYear();
                } else {
                    initSchoolYear();
                }

            } else {
                tools.validErrorDom(param_id.password, '请填写新教务系统密码');
            }
        }

        function validSchoolYear() {
            var schoolYear = param.schoolYear;
            if (schoolYear !== '') {
                tools.validSuccessDom(param_id.password);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.password, '请选择学年学期');
            }
        }

        function sendAjax() {
            $('#saveError').val('');
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.get(ajax_url.save, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                if(data.state){
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $.address.value(ajax_url.page);
                        }
                    });
                } else {
                    $('#saveError').text(data.msg);
                }
            });
        }

    });