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
            nationId: '#nation'
        };

        var page_param = {
            rosterDataId: $('#rosterDataId').val(),
            sex: $('#sexParam').val(),
            politicalLandscapeId: $('#politicalLandscapeParam').val(),
            nationId: $('#nationParam').val()
        };

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