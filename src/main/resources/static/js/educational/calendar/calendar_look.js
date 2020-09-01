//# sourceURL=timetable_data.js
require(["jquery", "lodash", "tools", "handlebars", "nav.active", "js-year-calendar.zh-CN"],
    function ($, _, tools, Handlebars, navActive) {
        /*
         ajax url.
        */
        var ajax_url = {
            data: web_path + '/web/educational/timetable/search',
            course_name: web_path + '/web/educational/timetable/course_name',
            attend_class: web_path + '/web/educational/timetable/attend_class',
            classroom: web_path + '/web/educational/timetable/classroom',
            teacher_name: web_path + '/web/educational/timetable/teacher_name',
            uniques: web_path + '/web/educational/timetable/uniques',
            sync_data: web_path + '/web/educational/timetable/sync',
            page: '/web/menu/educational/calendar'
        };

        navActive(ajax_url.page);

        var currentYear = new Date().getFullYear();
        new Calendar('#calendar', {
            language: 'zh-CN',
            style:'background',
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


    });