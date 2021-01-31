//# sourceURL=users_profile_potential.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "bootstrap",
        "csrf", "select2-zh-CN", "flatpickr-zh"],
    function ($, _, tools, Swal, moment) {

        moment.locale('zh-cn');

        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            potential_update_school: web_path + '/users/potential/update/school',
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            potential_update_info: web_path + '/users/potential/update/info'
        };

        var param_id = {
            school: '#school',
            college: '#college',
            sex: '#sex',
            birthday: '#birthday',
            nationId: '#nation',
            politicalLandscapeId: '#politicalLandscape',
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
            schoolId: '',
            collegeId: '',
            sex: '',
            birthday: '',
            nationId: '',
            politicalLandscapeId: '',
            familyResidence: ''
        };

        var page_param = {
            schoolId: $('#schoolIdParam').val(),
            collegeId: $('#collegeIdParam').val(),
            sex: $('#sexParam').val(),
            nationId: $('#nationParam').val(),
            politicalLandscapeId: $('#politicalLandscapeParam').val()
        };

        var init_configure = {
            init_school: false,
            init_college: false
        };

        function initParam() {
            param.schoolId = $(param_id.school).val();
            param.collegeId = $(param_id.college).val();
            param.sex = $(param_id.sex).val();
            param.birthday = $(param_id.birthday).val();
            param.nationId = $(param_id.nationId).val();
            param.politicalLandscapeId = $(param_id.politicalLandscapeId).val();
            param.familyResidence = _.trim($(param_id.familyResidence).val());
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        init();

        function init() {
            initSelect2();
            initSchool();

            initSex();
            initNation();
            initPoliticalLandscapeId();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                var schoolSelect2 = $(param_id.school).select2({
                    data: data.results
                });

                if (!init_configure.init_school) {
                    schoolSelect2.val(Number(page_param.schoolId)).trigger("change");
                    init_configure.init_school = true;
                }
            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    var collegeSelect2 = $(param_id.college).select2({data: data.results});
                    if (!init_configure.init_college) {
                        collegeSelect2.val(Number(page_param.collegeId)).trigger("change");
                        init_configure.init_college = true;
                    }
                });
            } else {
                $(param_id.college).html('<option label="请选择院"></option>');
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

        $(button_id.saveSchool.id).click(function () {
            initParam();
            validSchool();
        });

        function validSchool() {
            var schoolId = param.schoolId;
            if (Number(schoolId) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
                validCollege();
            } else {
                tools.validSelect2ErrorDom(param_id.school, '请选择您所在的学校');
            }
        }

        function validCollege() {
            var collegeId = param.collegeId;
            if (Number(collegeId) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
                updateSchool();
            } else {
                tools.validSelect2ErrorDom(param_id.college, '请选择您所在的院');
            }
        }

        function updateSchool() {
            // 显示遮罩
            tools.buttonLoading(button_id.saveSchool.id, button_id.saveSchool.tip);
            $.post(ajax_url.potential_update_school, param, function (data) {
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
            validFamilyResidence();
        });

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
            $.post(ajax_url.potential_update_info, param, function (data) {
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