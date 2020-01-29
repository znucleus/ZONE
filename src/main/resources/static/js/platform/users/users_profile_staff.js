//# sourceURL=users_profile_staff.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "bootstrap",
        "csrf", "select2-zh-CN", "flatpickr-zh"],
    function ($, _, tools, Swal, moment) {

        moment.locale('zh-cn');

        var ajax_url = {
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_academic_title_data: web_path + '/anyone/data/academic',
            staff_update_school: web_path + '/users/staff/update/school',
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            check_staff_number: web_path + '/users/check/staff/number',
            staff_update_info: web_path + '/users/staff/update/info'
        };

        var param_id = {
            department: '#department',
            staffNumber: '#staffNumber',
            sex: '#sex',
            birthday: '#birthday',
            nationId: '#nation',
            politicalLandscapeId: '#politicalLandscape',
            post: '#post',
            academicTitleId: '#academicTitle',
            familyResidence: '#familyResidence'
        };

        var button_id = {
            saveSchool: {
                tip: '保存中...',
                text: '保存',
                id: '#saveSchool'
            },
            info: {
                tip: '保存中...',
                text: '保存',
                id: '#updateInfo'
            }
        };

        var param = {
            departmentId: '',
            staffNumber: '',
            sex: '',
            birthday: '',
            nationId: '',
            politicalLandscapeId: '',
            post: '',
            academicTitleId: '',
            familyResidence: ''
        };

        var page_param = {
            collegeId: $('#collegeId').val(),
            departmentId: $('#departmentId').val(),
            sex: $('#sexParam').val(),
            nationId: $('#nationParam').val(),
            politicalLandscapeId: $('#politicalLandscapeParam').val(),
            academicTitleId: $('#academicTitleParam').val()
        };

        var init_configure = {
            init_department: false
        };

        function initParam() {
            param.departmentId = $(param_id.department).val();
            param.staffNumber = _.trim($(param_id.staffNumber).val());
            param.sex = $(param_id.sex).val();
            param.birthday = $(param_id.birthday).val();
            param.nationId = $(param_id.nationId).val();
            param.politicalLandscapeId = $(param_id.politicalLandscapeId).val();
            param.academicTitleId = $(param_id.academicTitleId).val();
            param.post = _.trim($(param_id.post).val());
            param.familyResidence = _.trim($(param_id.familyResidence).val());
        }

        $(param_id.department).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
            }
        });

        init();

        function init() {
            initDepartment(page_param.collegeId);
            initSelect2();

            initSex();
            initNation();
            initPoliticalLandscapeId();
            initAcademicTitle();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initDepartment(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                    $(param_id.department).html('<option label="请选择系"></option>');
                    $.each(data.results, function (i, n) {
                        $(param_id.department).append('<option value="' + n.id + '">' + n.text + '</option>');
                    });

                    if (!init_configure.init_department) {
                        $(param_id.department).val(page_param.departmentId);
                        init_configure.init_department = true;
                    }
                });
            } else {
                $(param_id.department).html('<option label="请选择系"></option>');
            }
        }

        function initSex() {
            $(param_id.sex).val(page_param.sex);
        }

        function initNation() {
            $.get(ajax_url.obtain_nation_data, function (data) {
                var sl = $(param_id.nationId).select2({
                    data: data.results
                });

                sl.val(page_param.nationId).trigger("change");
            });
        }

        function initPoliticalLandscapeId() {
            $.get(ajax_url.obtain_political_landscape_data, function (data) {
                $(param_id.politicalLandscapeId).html('<option label="请选择政治面貌"></option>');
                $.each(data.results, function (i, n) {
                    $(param_id.politicalLandscapeId).append('<option value="' + n.id + '">' + n.text + '</option>');
                });
                $(param_id.politicalLandscapeId).val(page_param.politicalLandscapeId);
            });
        }

        function initAcademicTitle() {
            $.get(ajax_url.obtain_academic_title_data, function (data) {
                $(param_id.academicTitleId).html('<option label="请选择职称"></option>');
                $.each(data.results, function (i, n) {
                    $(param_id.academicTitleId).append('<option value="' + n.id + '">' + n.text + '</option>');
                });
                $(param_id.academicTitleId).val(page_param.academicTitleId);
            });
        }

        $(button_id.saveSchool.id).click(function () {
            initParam();
            validDepartment();
        });

        function validDepartment() {
            var departmentId = param.departmentId;
            if (Number(departmentId) > 0) {
                tools.validSelect2SuccessDom(param_id.department);
                updateSchool();
            } else {
                tools.validSelect2ErrorDom(param_id.department, '请选择您所在的系');
            }
        }

        function updateSchool() {
            // 显示遮罩
            tools.buttonLoading(button_id.saveSchool.id, button_id.saveSchool.tip);
            $.post(ajax_url.staff_update_school, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.saveSchool.id, button_id.saveSchool.text);
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    Swal.fire('更新失败', data.msg, 'error');
                }
            });
        }

        $(param_id.birthday).flatpickr({
            "locale": "zh",
            defaultDate: _.trim($(param_id.birthday).val()) !== '' ? moment(_.trim($(param_id.birthday).val()), "YYYY-MM-DD").toDate() : moment((new Date().getFullYear() - 26) + "-01-07", "YYYY-MM-DD").toDate()
        });

        $(button_id.info.id).click(function () {
            initParam();
            validStaffNumber();
        });

        function validStaffNumber() {
            var staffNumber = param.staffNumber;
            if (staffNumber !== '') {
                if (tools.regex.staffNumber.test(staffNumber)) {
                    $.post(ajax_url.check_staff_number, {staffNumber: staffNumber}, function (data) {
                        if (data.state) {
                            tools.validSuccessDom(param_id.staffNumber);
                            validPost();
                        } else {
                            tools.validErrorDom(param_id.staffNumber, data.msg);
                        }
                    });
                } else {
                    tools.validErrorDom(param_id.staffNumber, '工号至少1位数字');
                }
            } else {
                tools.validErrorDom(param_id.staffNumber, '工号不能为空');
            }
        }

        function validPost() {
            var post = param.post;
            if (post !== '') {
                if (post.length <= 50) {
                    tools.validSuccessDom(param_id.post);
                    validFamilyResidence();
                } else {
                    tools.validErrorDom(param_id.post, '职务为50个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.post);
                validFamilyResidence();
            }
        }

        function validFamilyResidence() {
            var familyResidence = param.familyResidence;
            if (familyResidence !== '') {
                if (familyResidence.length <= 200) {
                    tools.validSuccessDom(param_id.familyResidence);
                    updateInfo();
                } else {
                    tools.validErrorDom(param_id.familyResidence, '家庭居住地为200个字符以内');
                }
            } else {
                tools.validSuccessDom(param_id.familyResidence);
                updateInfo();
            }
        }

        function updateInfo() {
            // 显示遮罩
            tools.buttonLoading(button_id.info.id, button_id.info.tip);
            $.post(ajax_url.staff_update_info, param, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.info.id, button_id.info.text);
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $('#logout').submit();
                        }
                    });
                } else {
                    Swal.fire('更新失败', data.msg, 'error');
                }
            });
        }
    });