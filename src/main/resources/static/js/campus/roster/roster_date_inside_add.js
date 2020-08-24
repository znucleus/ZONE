//# sourceURL=roster_date_inside_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "nav.active", "bootstrap", "messenger",
        "csrf", "select2-zh-CN", "flatpickr-zh", "bootstrap-inputmask", "jquery.address"],
    function ($, _, tools, Swal, moment, navActive) {

        moment.locale('zh-cn');

        var ajax_url = {
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            save: web_path + '/web/campus/roster/data/save',
            page: '/web/menu/campus/roster'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var param_id = {
            studentNumber:'#studentNumber',
            realName: '#realName',
            namePinyin: '#namePinyin',
            sex: '#sex',
            birthday: '#birthday',
            idCard: '#idCard',
            politicalLandscapeId: '#politicalLandscape',
            nationId: '#nation',
            organizeId:'#organizeId',
            province: '#province',
            region: '#region',
            busSection: '#busSection',
            parentName: '#parentName',
            parentContactPhone: '#parentContactPhone',
            parentContactAddress: '#parentContactAddress',
            zipCode: '#zipCode',
            phoneNumber:'#phoneNumber',
            dormitoryNumber: '#dormitoryNumber'
        };

        var button_id = {
            save: {
                tip: '保存中...',
                text: '保存',
                id: '#save'
            }
        };

        var page_param = {
            rosterReleaseId: $('#rosterReleaseId').val(),
            sex: $('#sexParam').val(),
            politicalLandscapeId: $('#politicalLandscapeParam').val(),
            nationId: $('#nationParam').val()
        };

        var param = {
            rosterReleaseId: '',
            studentNumber:'',
            realName: '',
            namePinyin: '',
            sex: '',
            birthday: '',
            idCard: '',
            politicalLandscapeId: '',
            nationId: '',
            organizeId:'',
            province: '',
            region: '',
            busSection: '',
            parentName: '',
            parentContactPhone: '',
            parentContactAddress: '',
            zipCode: '',
            phoneNumber:'',
            dormitoryNumber: ''
        };

        function initParam() {
            param.rosterReleaseId = page_param.rosterReleaseId;
            param.studentNumber = _.trim($(param_id.studentNumber).val());
            param.realName = _.trim($(param_id.realName).val());
            param.namePinyin = _.trim($(param_id.namePinyin).val());
            param.sex = $(param_id.sex).val();
            param.birthday = $(param_id.birthday).val();
            param.idCard = _.trim($(param_id.idCard).val());
            param.politicalLandscapeId = $(param_id.politicalLandscapeId).val();
            param.nationId = $(param_id.nationId).val();
            param.organizeId = $(param_id.organizeId).val();
            param.province = _.trim($(param_id.province).val());
            param.region = _.trim($(param_id.region).val());
            param.busSection = _.trim($(param_id.busSection).val());
            param.parentName = _.trim($(param_id.parentName).val());
            param.parentContactPhone = _.trim($(param_id.parentContactPhone).val());
            param.parentContactAddress = _.trim($(param_id.parentContactAddress).val());
            param.zipCode = _.trim($(param_id.zipCode).val());
            param.phoneNumber = _.trim($(param_id.phoneNumber).val());
            param.dormitoryNumber = _.trim($(param_id.dormitoryNumber).val());
        }

        init();

        function init() {
            initSex();
            initNation();
            initPoliticalLandscapeId();
            initSelect2();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
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

        $(param_id.birthday).flatpickr({
            "locale": "zh",
            defaultDate: _.trim($(param_id.birthday).val()) !== '' ? moment(_.trim($(param_id.birthday).val()), "YYYY-MM-DD").toDate() : moment((new Date().getFullYear() - 26) + "-01-07", "YYYY-MM-DD").toDate()
        });

        $(button_id.save.id).click(function () {
            initParam();
            validRealName();
        });

        function validRealName() {
            var realName = param.realName;
            if (realName.length <= 0 || realName.length > 15) {
                tools.validErrorDom(param_id.realName, '姓名15个字符以内');
                errorTip('姓名15个字符以内');
            } else {
                tools.validSuccessDom(param_id.realName);
                validNamePinyin();
            }
        }

        function validNamePinyin() {
            var namePinyin = param.namePinyin;
            if (namePinyin.length <= 0 || namePinyin.length > 20) {
                tools.validErrorDom(param_id.namePinyin, '姓名拼音20个字符以内');
                errorTip('姓名拼音20个字符以内');
            } else {
                tools.validSuccessDom(param_id.namePinyin);
                validSex();
            }
        }

        function validSex() {
            var sex = param.sex;
            if (sex.length <= 0) {
                tools.validErrorDom(param_id.sex, '请选择性别');
                errorTip('请选择性别');
            } else {
                tools.validSuccessDom(param_id.sex);
                validBirthday();
            }
        }

        function validBirthday() {
            var birthday = param.birthday;
            if (birthday.length <= 0) {
                tools.validErrorDom(param_id.birthday, '请选择生日');
                errorTip('请选择生日');
            } else {
                tools.validSuccessDom(param_id.birthday);
                validIdCard();
            }
        }

        function validIdCard() {
            var idCard = param.idCard;
            if (idCard.length <= 0) {
                tools.validErrorDom(param_id.idCard, '请填写身份证号');
                errorTip('请填写身份证号');
            } else {
                if (!tools.regex.idCard.test(idCard)) {
                    tools.validErrorDom(param_id.idCard, '身份证号格式不正确');
                    errorTip('身份证号格式不正确');
                } else {
                    tools.validSuccessDom(param_id.idCard);
                    validPoliticalLandscape();
                }
            }

        }

        function validPoliticalLandscape() {
            var politicalLandscapeId = param.politicalLandscapeId;
            if (Number(politicalLandscapeId) <= 0) {
                tools.validErrorDom(param_id.politicalLandscapeId, '请选择政治面貌');
                errorTip('请选择政治面貌');
            } else {
                tools.validSuccessDom(param_id.politicalLandscapeId);
                validNation();
            }
        }

        function validNation() {
            var nationId = param.nationId;
            if (Number(nationId) <= 0) {
                tools.validSelect2ErrorDom(param_id.nationId, '请选择民族');
                errorTip('请选择民族');
            } else {
                tools.validSelect2SuccessDom(param_id.nationId);
                validProvince();
            }
        }

        function validProvince() {
            var province = param.province;
            if (province.length <= 0 || province.length > 20) {
                tools.validErrorDom(param_id.province, '省份20个字符以内');
                errorTip('省份20个字符以内');
            } else {
                tools.validSuccessDom(param_id.province);
                validRegion();
            }
        }

        function validRegion() {
            var region = param.region;
            if (region.length <= 0 || region.length > 50) {
                tools.validErrorDom(param_id.region, '所属地区50个字符以内');
                errorTip('所属地区50个字符以内');
            } else {
                tools.validSuccessDom(param_id.region);
                validBusSection();
            }
        }

        function validBusSection() {
            var busSection = param.busSection;
            if (busSection.length <= 0 || busSection.length > 150) {
                tools.validCustomerSingleErrorDom(param_id.busSection, '乘车区间150个字符以内');
                errorTip('乘车区间150个字符以内');
            } else {
                tools.validCustomerSingleSuccessDom(param_id.busSection);
                validParentName();
            }
        }

        function validParentName() {
            var parentName = param.parentName;
            if (parentName.length <= 0 || parentName.length > 10) {
                tools.validErrorDom(param_id.parentName, '家长姓名10个字符以内');
                errorTip('家长姓名10个字符以内');
            } else {
                tools.validSuccessDom(param_id.parentName);
                validParentContactPhone();
            }
        }

        function validParentContactPhone() {
            var parentContactPhone = param.parentContactPhone;
            if (parentContactPhone.length <= 0) {
                tools.validErrorDom(param_id.parentContactPhone, '请填写家长联系电话');
                errorTip('请填写家长联系电话');
            } else {
                if (!tools.regex.mobile.test(parentContactPhone)) {
                    tools.validErrorDom(param_id.parentContactPhone, '家长联系电话格式不正确');
                    errorTip('家长联系电话格式不正确');
                } else {
                    tools.validSuccessDom(param_id.parentContactPhone);
                    validParentContactAddress();
                }
            }
        }

        function validParentContactAddress() {
            var parentContactAddress = param.parentContactAddress;
            if (parentContactAddress.length <= 0 || parentContactAddress.length > 200) {
                tools.validErrorDom(param_id.parentContactAddress, '家长联系地址200个字符以内');
                errorTip('家长联系地址200个字符以内');
            } else {
                tools.validSuccessDom(param_id.parentContactAddress);
                validZipCode();
            }
        }

        function validZipCode() {
            var zipCode = param.zipCode;
            if (zipCode.length <= 0) {
                tools.validErrorDom(param_id.zipCode, '请填写邮政编码');
                errorTip('请填写邮政编码');
            } else {
                if (!tools.regex.zipCode.test(zipCode)) {
                    tools.validErrorDom(param_id.zipCode, '邮政编码格式不正确');
                    errorTip('邮政编码格式不正确');
                } else {
                    tools.validSuccessDom(param_id.zipCode);
                    validDormitoryNumber();
                }
            }

        }

        function validDormitoryNumber() {
            var dormitoryNumber = param.dormitoryNumber;
            if (dormitoryNumber.length <= 0) {
                tools.validErrorDom(param_id.dormitoryNumber, '请填写宿舍号');
                errorTip('请填写宿舍号');
            } else {
                if (!tools.regex.dormitoryNumber.test(dormitoryNumber)) {
                    tools.validErrorDom(param_id.dormitoryNumber, '宿舍号格式不正确');
                    errorTip('宿舍号格式不正确');
                } else {
                    tools.validSuccessDom(param_id.dormitoryNumber);
                    lastInitParam();
                }
            }
        }

        function errorTip(msg) {
            Messenger().post({
                message: msg,
                type: 'error',
                showCloseButton: true
            });
        }

        function lastInitParam() {
            param.busSection = "昆明-" + param.busSection;
            sendAjax();
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.save,
                data: param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.page);
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