//# sourceURL=student_achievement_history.js
require(["jquery", "tools", "handlebars", "nav.active", "messenger"],
    function ($, tools, Handlebars, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                semester: web_path + '/web/achievement/student/query/history/new/semester',
                data: web_path + '/web/achievement/student/query/history/new/data',
                page: '/web/menu/achievement/student/query'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            studentNumber: $('#paramStudentNumber').val()
        }

        init();

        function init() {
            initSemester();
        }

        var semesterList = [];
        var semesterId = "";

        function initSemester() {
            $.get(getAjaxUrl().semester, page_param, function (data) {
                if (data.state) {
                    semesterList = data.listResult;
                    semesterData(data);
                    if (semesterList.length > 0) {
                        $('#departmentName').text(semesterList[0].departmentName);
                        $('#organizeName').text(semesterList[0].organizeName);
                        $('#studentNumber').text(semesterList[0].studentNumber);
                        $('#realName').text(semesterList[0].realName);
                        semesterId = semesterList[0].semesterId;
                        initAchievement();
                    }
                } else {
                    Messenger().post({
                        message: data.msg,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        var semesterTableElement = $('#semesterData');

        function semesterData(data) {
            var template = Handlebars.compile($("#semester_template").html());
            semesterTableElement.html(template(data));
            $(semesterTableElement).children().first().children().first().addClass('active');
        }

        semesterTableElement.delegate('.semester', "click", function () {
            $('.semester').removeClass('active');
            $(this).addClass('active');
            semesterId = $(this).attr('data-id');
            $.each(semesterList, function (i, v) {
                if (v.semesterId === semesterId) {
                    $('#departmentName').text(v.departmentName);
                    $('#organizeName').text(v.organizeName);
                    $('#studentNumber').text(v.studentNumber);
                    $('#realName').text(v.realName);
                }
            });
            initAchievement();
        });

        var achievementList = [];

        function initAchievement() {
            if (semesterId !== '') {
                tools.dataLocalLoading('#dataTable');
                $.get(getAjaxUrl().data + '/' + semesterId, function (data) {
                    tools.dataLocalEndLoading('#dataTable');
                    if (data.state) {
                        achievementList = data.listResult;
                        search();
                    } else {
                        Messenger().post({
                            message: data.msg,
                            type: 'error',
                            showCloseButton: true
                        });
                    }
                });
            }
        }

        var tableElement = $('#dataTable');

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#data_template").html());
            tableElement.html(template(data));
        }

        $('#refresh').click(function () {
            initAchievement();
        });

        $("#search").keyup(function () {
            search();
        });

        function search() {
            var v = $('#search').val();
            var filterData = {listResult: []};
            if (v !== '') {
                $.each(achievementList, function (i, n) {
                    if (n.courseName.indexOf(v) > -1) {
                        filterData.listResult.push(n);
                    }
                });
            } else {
                filterData.listResult = achievementList;
            }
            listData(filterData);
        }
    });