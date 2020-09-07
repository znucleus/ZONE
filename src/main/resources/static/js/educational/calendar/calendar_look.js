//# sourceURL=timetable_data.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "jquery.address", "js-year-calendar.zh-CN"],
    function ($, _, tools, Handlebars, navActive) {
        /*
         ajax url.
        */
        var ajax_url = {
            data: web_path + '/web/educational/timetable/search',
            list: '/web/educational/calendar/list',
            authorize: '/web/educational/calendar/authorize/add',
            page: '/web/menu/educational/calendar'
        };

        navActive(ajax_url.page);

        var currentYear = new Date().getFullYear();
        new Calendar('#calendar', {
            language: 'zh-CN',
            style: 'background',
            dataSource: [
                {
                    startDate: new Date(currentYear, 1, 4),
                    endDate: new Date(currentYear, 1, 15)
                },
                {
                    startDate: new Date(currentYear, 3, 5),
                    endDate: new Date(currentYear, 5, 15)
                }
            ]
        });

        $('#list').click(function () {
            $.address.value(ajax_url.list);
        });

        /*
       权限分配
       */
        $('#authorize').click(function () {
            $.address.value(ajax_url.authorize);
        });

    });