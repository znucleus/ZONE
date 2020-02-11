//# sourceURL=internship_apply_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "jquery.address", "select2-zh-CN"],
    function ($, _, tools, Handlebars, Swal, navActive) {
        /*
         ajax url.
         */
        var ajax_url = {
            obtain_staff_data: web_path + '/web/internship/apply/staff',
            obtain_internship_file_data: web_path + '/users/data/internship_file',
            download: web_path + '/web/internship/apply/download',
            page: '/web/menu/internship/apply',
            my_page: '/web/internship/apply/my'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            studentId: '#studentId',
            username: '#username',
            realName: '#realName',
            sex: '#sex',
            qqMailbox: '#qqMailbox',
            parentContactPhone: '#parentContactPhone',
            staff: '#staff',
            companyName: '#companyName',
            companyAddress: '#companyAddress',
            companyContact: '#companyContact',
            companyMobile: '#companyMobile',
            startTime: '#startTime',
            endTime: '#endTime'
        };

        /*
         参数
         */
        var param = {
            studentId: '',
            username: '',
            realName: '',
            qqMailbox: '',
            parentContactPhone: '',
            staffId: '',
            companyName: '',
            companyAddress: '',
            companyContact: '',
            companyMobile: '',
            startTime: '',
            endTime: ''
        };

        var page_param = {
            paramSex: $('#paramSex').val(),
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            paramHeadmaster: $('#paramHeadmaster').val(),
            paramHeadmasterTel: $('#paramHeadmasterTel').val(),
            paramInternshipApplyState: $('#paramInternshipApplyState').val()
        };

        var init_configure = {
            init_staff: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.studentId = $(param_id.studentId).val();
            param.username = $(param_id.username).val();
            param.realName = $(param_id.realName).val();
            param.qqMailbox = $(param_id.qqMailbox).val();
            param.parentContactPhone = $(param_id.parentContactPhone).val();
            param.staffId = $(param_id.staff).val();
            param.companyName = $(param_id.companyName).val();
            param.companyAddress = $(param_id.companyAddress).val();
            param.companyContact = $(param_id.companyContact).val();
            param.companyMobile = $(param_id.companyMobile).val();
            param.startTime = $(param_id.startTime).val();
            param.endTime = $(param_id.endTime).val();
        }

        init();

        function init() {
            initParam();
            initInternshipFile();
            initStaff();
            initSelect2();

            $(param_id.sex).val(page_param.paramSex);
        }

        function initStaff() {
            $.get(ajax_url.obtain_staff_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                var sl = $(param_id.staff).select2({
                    data: data.results
                });

                if (!init_configure.init_staff) {
                    var staffId = '';
                    var realHeadmaster = page_param.paramHeadmaster + ' ' + page_param.paramHeadmasterTel;
                    for (var i = 0; i < data.results.length; i++) {
                        if (data.results[i].text === realHeadmaster) {
                            staffId = data.results[i].id;
                            break;
                        }
                    }
                    sl.val(staffId).trigger("change");
                    init_configure.init_staff = true;
                }
            });
        }

        function initInternshipFile() {
            $.get(ajax_url.obtain_internship_file_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                fileShow(data);
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

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
         下载附件
         */
        $('#fileShow').delegate('.downloadFile', "click", function () {
            var id = $(this).attr('data-file-id');
            window.location.href = ajax_url.download + '/' + id;
        });
    });