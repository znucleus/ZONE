//# sourceURL=roster_data_inside_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "moment-with-locales", "nav.active", "bootstrap", "messenger",
        "csrf", "select2-zh-CN", "flatpickr-zh", "bootstrap-inputmask", "jquery.address"],
    function ($, _, tools, Swal, moment, navActive) {

        moment.locale('zh-cn');

        var ajax_url = {
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            convert_name: web_path + '/anyone/campus/roster/convert-name',
            save: web_path + '/web/campus/roster/data/save',
            page: '/web/menu/campus/roster'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var param_id = {
            studentNumber: '#studentNumber',
            realName: '#realName',
            namePinyin: '#namePinyin',
            sex: '#sex',
            birthday: '#birthday',
            idCard: '#idCard',
            politicalLandscapeId: '#politicalLandscape',
            nationId: '#nation',
            organizeId: '#organizeId',
            province: '#province',
            nativePlace: '#nativePlace',
            region: '#region',
            busSection: '#busSection',
            parentName: '#parentName',
            parentContactPhone: '#parentContactPhone',
            parentContactAddress: '#parentContactAddress',
            zipCode: '#zipCode',
            phoneNumber: '#phoneNumber',
            email: '#email',
            candidatesType: '#candidatesType',
            isDeformedMan: '#isDeformedMan',
            deformedManCode: '#deformedManCode',
            isMilitaryServiceRegistration: '#isMilitaryServiceRegistration',
            isProvideLoan: '#isProvideLoan',
            universityPosition: '#universityPosition',
            isPoorStudents: '#isPoorStudents',
            poorStudentsType: '#poorStudentsType',
            isStayOutside: '#isStayOutside',
            dormitoryNumber: '#dormitoryNumber',
            stayOutsideType: '#stayOutsideType',
            stayOutsideAddress: '#stayOutsideAddress',
            leagueMemberJoinDate: '#leagueMemberJoinDate',
            isRegisteredVolunteers: '#isRegisteredVolunteers',
            isOkLeagueMembership: '#isOkLeagueMembership',
            applyPartyMembershipDate: '#applyPartyMembershipDate',
            becomeActivistsDate: '#becomeActivistsDate',
            becomeProbationaryPartyMemberDate: '#becomeProbationaryPartyMemberDate',
            joiningPartyDate: '#joiningPartyDate'
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
            studentNumber: '',
            realName: '',
            namePinyin: '',
            sex: '',
            birthday: '',
            idCard: '',
            politicalLandscapeId: '',
            nationId: '',
            organizeId: '',
            province: '',
            nativePlace: '',
            region: '',
            busSection: '',
            parentName: '',
            parentContactPhone: '',
            parentContactAddress: '',
            zipCode: '',
            phoneNumber: '',
            email: '',
            candidatesType: '',
            isDeformedMan: '',
            deformedManCode: '',
            isMilitaryServiceRegistration: '',
            isProvideLoan: '',
            universityPosition: '',
            isPoorStudents: '',
            poorStudentsType: '',
            isStayOutside: '',
            dormitoryNumber: '',
            stayOutsideType: '',
            stayOutsideAddress: '',
            leagueMemberJoinDate: '',
            isRegisteredVolunteers: '',
            isOkLeagueMembership: '',
            applyPartyMembershipDate: '',
            becomeActivistsDate: '',
            becomeProbationaryPartyMemberDate: '',
            joiningPartyDate: ''
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
            param.nativePlace = _.trim($(param_id.nativePlace).val());
            param.region = _.trim($(param_id.region).val());
            param.busSection = _.trim($(param_id.busSection).val());
            param.parentName = _.trim($(param_id.parentName).val());
            param.parentContactPhone = _.trim($(param_id.parentContactPhone).val());
            param.parentContactAddress = _.trim($(param_id.parentContactAddress).val());
            param.zipCode = _.trim($(param_id.zipCode).val());
            param.phoneNumber = _.trim($(param_id.phoneNumber).val());
            param.email = _.trim($(param_id.email).val());
            param.candidatesType = $(param_id.candidatesType).val();
            param.isDeformedMan = $(param_id.isDeformedMan).val();
            param.deformedManCode = _.trim($(param_id.deformedManCode).val());
            param.isMilitaryServiceRegistration = $(param_id.isMilitaryServiceRegistration).val();
            param.isProvideLoan = $(param_id.isProvideLoan).val();
            param.universityPosition = _.trim($(param_id.universityPosition).val());
            param.isPoorStudents = $(param_id.isPoorStudents).val();
            param.poorStudentsType = $(param_id.poorStudentsType).val();
            param.isStayOutside = $(param_id.isStayOutside).val();
            param.dormitoryNumber = _.trim($(param_id.dormitoryNumber).val());
            param.stayOutsideType = $(param_id.stayOutsideType).val();
            param.stayOutsideAddress = _.trim($(param_id.stayOutsideAddress).val());
            param.leagueMemberJoinDate = $(param_id.leagueMemberJoinDate).val();
            param.isRegisteredVolunteers = $(param_id.isRegisteredVolunteers).val();
            param.isOkLeagueMembership = $(param_id.isOkLeagueMembership).val();
            param.applyPartyMembershipDate = $(param_id.applyPartyMembershipDate).val();
            param.becomeActivistsDate = $(param_id.becomeActivistsDate).val();
            param.becomeProbationaryPartyMemberDate = $(param_id.becomeProbationaryPartyMemberDate).val();
            param.joiningPartyDate = $(param_id.joiningPartyDate).val();
        }

        init();

        function init() {
            initIdCard();
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

        function initIdCard() {
            var v = $(param_id.idCard).val();
            if (tools.regex.idCard.test(v)) {
                $(param_id.birthday).val(v.substring(6, 10) + "-" + v.substring(10, 12) + "-" + v.substring(12, 14));
                if (Number(v.substring(16, 17)) % 2 === 0) {
                    $(param_id.sex).val('女');
                } else {
                    $(param_id.sex).val('男');
                }
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

        $(param_id.birthday).flatpickr({
            "locale": "zh",
            defaultDate: _.trim($(param_id.birthday).val()) !== '' ? moment(_.trim($(param_id.birthday).val()), "YYYY-MM-DD").toDate() : moment((new Date().getFullYear() - 26) + "-01-07", "YYYY-MM-DD").toDate()
        });

        $('.flatpickr').flatpickr({
            "locale": "zh",
            wrap: true
        });

        $(param_id.idCard).blur(function () {
            initIdCard();
        });

        $(param_id.realName).blur(function () {
            initParam();
            var realName = param.realName;
            if (realName !== '') {
                tools.dataLoading();
                $.get(ajax_url.convert_name, {realName: realName}, function (data) {
                    tools.dataEndLoading();
                    if (data.state) {
                        $(param_id.namePinyin).val(data.pinyin);
                    }
                });
            }
        });

        $(param_id.isDeformedMan).change(function () {
            var v = $(this).val();
            if (Number(v) === 1) {
                $(param_id.deformedManCode).parent().css('display', '');
            } else {
                $(param_id.deformedManCode).parent().css('display', 'none');
                $(param_id.deformedManCode).val('');
            }
        });

        $(param_id.isPoorStudents).change(function () {
            var v = $(this).val();
            if (Number(v) === 1) {
                $(param_id.poorStudentsType).parent().css('display', '');
            } else {
                $(param_id.poorStudentsType).parent().css('display', 'none');
                $(param_id.poorStudentsType).val('');
            }
        });

        $(param_id.isStayOutside).change(function () {
            var v = $(this).val();
            if (Number(v) === 1) {
                $(param_id.stayOutsideType).parent().css('display', '');
                $(param_id.stayOutsideAddress).parent().css('display', '');
                $(param_id.dormitoryNumber).parent().css('display', 'none');
                $(param_id.dormitoryNumber).val('');
            } else {
                $(param_id.stayOutsideType).parent().css('display', 'none');
                $(param_id.stayOutsideAddress).parent().css('display', 'none');
                $(param_id.dormitoryNumber).parent().css('display', '');
                $(param_id.stayOutsideType).val('');
                $(param_id.stayOutsideAddress).val('');
            }
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
                    validSex();
                }
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
                validPoliticalLandscape();
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
                validNativePlace();
            }
        }

        function validNativePlace() {
            var nativePlace = param.nativePlace;
            if (nativePlace.length <= 0 || nativePlace.length > 120) {
                tools.validErrorDom(param_id.nativePlace, '籍贯120个字符以内');
                errorTip('籍贯120个字符以内');
            } else {
                tools.validSuccessDom(param_id.nativePlace);
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
                    validDeformedManCode();
                }
            }
        }

        function validDeformedManCode() {
            var isDeformedMan = param.isDeformedMan;
            var deformedManCode = param.deformedManCode;
            if (Number(isDeformedMan) === 1) {
                if (deformedManCode.length <= 0 || deformedManCode.length > 100) {
                    tools.validErrorDom(param_id.deformedManCode, '残疾人编号100个字符以内');
                    errorTip('残疾人编号100个字符以内');
                } else {
                    tools.validSuccessDom(param_id.deformedManCode);
                    validPoorStudentsType();
                }
            } else {
                tools.validSuccessDom(param_id.deformedManCode);
                validPoorStudentsType();
            }
        }

        function validPoorStudentsType() {
            var isPoorStudents = param.isPoorStudents;
            var poorStudentsType = param.poorStudentsType;
            if (Number(isPoorStudents) === 1) {
                if (poorStudentsType === '') {
                    tools.validErrorDom(param_id.poorStudentsType, '请选择贫困生类型');
                    errorTip('请选择贫困生类型');
                } else {
                    tools.validSuccessDom(param_id.poorStudentsType);
                    validDormitoryNumber();
                }
            } else {
                tools.validSuccessDom(param_id.poorStudentsType);
                validDormitoryNumber();
            }
        }

        function validDormitoryNumber() {
            var isStayOutside = param.isStayOutside;
            var dormitoryNumber = param.dormitoryNumber;
            if (Number(isStayOutside) === 0) {
                if (dormitoryNumber.length <= 0) {
                    tools.validErrorDom(param_id.dormitoryNumber, '请填写宿舍号');
                    errorTip('请填写宿舍号');
                } else {
                    if (!tools.regex.dormitoryNumber.test(dormitoryNumber)) {
                        tools.validErrorDom(param_id.dormitoryNumber, '宿舍号格式不正确');
                        errorTip('宿舍号格式不正确');
                    } else {
                        tools.validSuccessDom(param_id.dormitoryNumber);
                        validStayOutsideType();
                    }
                }
            } else {
                tools.validSuccessDom(param_id.dormitoryNumber);
                validStayOutsideType();
            }
        }

        function validStayOutsideType() {
            var isStayOutside = param.isStayOutside;
            var stayOutsideType = param.stayOutsideType;
            if (Number(isStayOutside) === 1) {
                if (stayOutsideType === '') {
                    tools.validErrorDom(param_id.stayOutsideType, '请选择外宿类型');
                    errorTip('请选择外宿类型');
                } else {
                    tools.validSuccessDom(param_id.stayOutsideType);
                    validStayOutsideAddress();
                }
            } else {
                tools.validSuccessDom(param_id.stayOutsideType);
                validStayOutsideAddress();
            }
        }

        function validStayOutsideAddress() {
            var isStayOutside = param.isStayOutside;
            var stayOutsideAddress = param.stayOutsideAddress;
            if (Number(isStayOutside) === 1) {
                if (stayOutsideAddress.length <= 0 || stayOutsideAddress.length > 200) {
                    tools.validErrorDom(param_id.stayOutsideAddress, '外宿详细地址200个字符以内');
                    errorTip('外宿详细地址200个字符以内');
                } else {
                    tools.validSuccessDom(param_id.stayOutsideAddress);
                    lastInitParam();
                }
            } else {
                tools.validSuccessDom(param_id.stayOutsideAddress);
                lastInitParam();
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