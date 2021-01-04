//# sourceURL=theory_configure_edit.js
require(["jquery", "tools", "moment-with-locales", "handlebars", "sweetalert2",
        "nav.active", "messenger", "jquery.address", "select2-zh-CN", "flatpickr-zh"],
    function ($, tools, moment, Handlebars, Swal, navActive) {

        moment.locale('zh-cn');

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_building_data: web_path + '/users/data/building',
            obtain_schoolroom_data: web_path + '/users/data/schoolroom',
            update: web_path + '/web/theory/release/configure/update',
            back: '/web/theory/release/configure',
            page: '/web/menu/theory/release'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            theoryReleaseId: '#theoryReleaseId',
            weekDay: '#weekDay',
            startTime: '#startTime',
            endTime: '#endTime',
            building: '#building',
            schoolroom: '#schoolroom'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        /*
         参数
         */
        var param = {
            theoryReleaseId: '',
            weekDay: '',
            startTime: '',
            endTime: '',
            buildingId: '',
            schoolroomId: ''
        };

        var page_param = {
            paramCollegeId: $('#paramCollegeId').val(),
            paramWeekDay: $('#paramWeekDay').val(),
            paramBuildingId: $('#paramBuildingId').val(),
            paramSchoolroomId: $('#paramSchoolroomId').val(),
        };

        var init_configure = {
            init_buildingId: false,
            init_schoolroomId: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.theoryReleaseId = $(param_id.theoryReleaseId).val();
            param.weekDay = $(param_id.weekDay).val();
            param.startTime = $(param_id.startTime).val();
            param.endTime = $(param_id.endTime).val();
            param.buildingId = $(param_id.building).val();
            param.schoolroomId = $(param_id.schoolroom).val();
        }

        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initWeekDay();
            initBuilding();
            initSelect2();
        }

        function initWeekDay() {
            $(param_id.weekDay).val(page_param.paramWeekDay);
        }

        function initBuilding() {
            $.get(ajax_url.obtain_building_data, {
                collegeId: page_param.paramCollegeId,
                buildingClassifyId: 1
            }, function (data) {
                var sl = $(param_id.building).select2({
                    data: data.results
                });

                if (!init_configure.init_buildingId) {
                    sl.val(page_param.paramBuildingId).trigger("change");
                    init_configure.init_buildingId = true;
                }
            });
        }

        function initSchoolroom(buildingId) {
            if (Number(buildingId) > 0) {
                $.get(ajax_url.obtain_schoolroom_data, {buildingId: buildingId}, function (data) {
                    $(param_id.schoolroom).html('<option label="请选择教室"></option>');
                    var sl = $(param_id.schoolroom).select2({data: data.results});

                    if (!init_configure.init_schoolroomId) {
                        sl.val(page_param.paramSchoolroomId).trigger("change");
                        init_configure.init_schoolroomId = true;
                    }
                });
            } else {
                $(param_id.schoolroom).html('<option label="请选择教室"></option>');
            }
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(param_id.startTime).flatpickr({
            "locale": "zh",
            "enableTime": true,
            "noCalendar": true,
            "dateFormat": "H:i:S",
            "time_24hr": true
        });

        $(param_id.endTime).flatpickr({
            "locale": "zh",
            "enableTime": true,
            "noCalendar": true,
            "dateFormat": "H:i:S",
            "time_24hr": true
        });

        $(param_id.building).change(function () {
            var v = $(this).val();
            initSchoolroom(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.building);
            }
        });

        $(param_id.schoolroom).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.schoolroom);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validStartTime();
        });

        function validStartTime() {
            var startTime = param.startTime;
            if (startTime.length <= 0) {
                tools.validErrorDom(param_id.startTime, '请选择上课开始时间');
            } else {
                tools.validSuccessDom(param_id.startTime);
                validEndTime();
            }
        }

        function validEndTime() {
            var endTime = param.endTime;
            if (endTime.length <= 0) {
                tools.validErrorDom(param_id.endTime, '上课结束时间');
            } else {
                var startTime = param.startTime;
                if (moment(endTime, 'HH:mm:ss').isSameOrAfter(moment(startTime, 'HH:mm:ss'))) {
                    tools.validSuccessDom(param_id.endTime);
                    validBuildingId();
                } else {
                    tools.validErrorDom(param_id.endTime, '上课结束时间应大于或等于上课开始时间');
                }
            }
        }

        function validBuildingId() {
            var buildingId = param.buildingId;
            if (Number(buildingId) <= 0) {
                tools.validSelect2ErrorDom(param_id.building, '请选择楼');
            } else {
                tools.validSelect2SuccessDom(param_id.building);
                validSchoolroomId();
            }
        }

        function validSchoolroomId() {
            var schoolroomId = param.schoolroomId;
            if (Number(schoolroomId) <= 0) {
                tools.validSelect2ErrorDom(param_id.schoolroom, '请选择教室');
            } else {
                tools.validSelect2SuccessDom(param_id.schoolroom);
                sendAjax();
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.update,
                data: $('#app_form').serialize(),
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.back + '/' + param.theoryReleaseId);
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

    });