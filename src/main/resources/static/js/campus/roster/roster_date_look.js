//# sourceURL=roster_date_look.js
require(["jquery", "nav.active", "jquery.address", "select2-zh-CN"],
    function ($, navActive) {

        var ajax_url = {
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            page: '/web/menu/campus/roster'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var param_id = {
            sex: '#sex',
            politicalLandscapeId: '#politicalLandscape',
            nationId: '#nation',
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
        };

        var page_param = {
            rosterDataId: $('#rosterDataId').val(),
            sex: $('#sexParam').val(),
            politicalLandscapeId: $('#politicalLandscapeParam').val(),
            nationId: $('#nationParam').val(),
            candidatesType:$('#candidatesTypeParam').val(),
            isDeformedMan:$('#isDeformedManParam').val(),
            isMilitaryServiceRegistration:$('#isMilitaryServiceRegistrationParam').val(),
            isProvideLoan:$('#isProvideLoanParam').val(),
            isPoorStudents:$('#isPoorStudentsParam').val(),
            poorStudentsType:$('#poorStudentsTypeParam').val(),
            isStayOutside:$('#isStayOutsideParam').val(),
            stayOutsideType:$('#stayOutsideTypeParam').val(),
            isRegisteredVolunteers:$('#isRegisteredVolunteersParam').val(),
            isOkLeagueMembership:$('#isOkLeagueMembershipParam').val()
        };

        init();

        function init() {
            initBaseData();
            initSex();
            initNation();
            initPoliticalLandscapeId();
            initSelect2();
        }

        function initBaseData() {
            var isDeformedMan = page_param.isDeformedMan;
            if(Number(isDeformedMan) === 1){
                $(param_id.deformedManCode).parent().css('display','');
            }

            var isPoorStudents = page_param.isPoorStudents;
            if(Number(isPoorStudents) === 1){
                $(param_id.poorStudentsType).parent().css('display','');
            }

            var isStayOutside = page_param.isStayOutside;
            if(Number(isStayOutside) === 1){
                $(param_id.stayOutsideType).parent().css('display','');
                $(param_id.stayOutsideAddress).parent().css('display','');
            } else {
                $(param_id.dormitoryNumber).parent().css('display','');
            }

            $(param_id.candidatesType).val(page_param.candidatesType);
            $(param_id.isDeformedMan).val(page_param.isDeformedMan);
            $(param_id.isMilitaryServiceRegistration).val(page_param.isMilitaryServiceRegistration);
            $(param_id.isProvideLoan).val(page_param.isProvideLoan);
            $(param_id.isPoorStudents).val(page_param.isPoorStudents);
            $(param_id.poorStudentsType).val(page_param.poorStudentsType);
            $(param_id.isStayOutside).val(page_param.isStayOutside);
            $(param_id.stayOutsideType).val(page_param.stayOutsideType);
            $(param_id.isRegisteredVolunteers).val(page_param.isRegisteredVolunteers);
            $(param_id.isOkLeagueMembership).val(page_param.isOkLeagueMembership);
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
                    disabled: true,
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
    });