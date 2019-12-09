//# sourceURL=system_health.js
require(["jquery", "tools", "nav.active"],
    function ($, tools, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/health',
            page: '/web/menu/system/health'
        };

        var param_id = {
            allStatus: '#allStatus',
            pingStatus: '#pingStatus',
            mailStatus: '#mailStatus',
            mailLocation: '#mailLocation',
            diskStatus: '#diskStatus',
            diskTotal: '#diskTotal',
            diskFree: '#diskFree',
            diskThreshold: '#diskThreshold',
            redisStatus: '#redisStatus',
            redisVersion: '#redisVersion',
            dbStatus: '#dbStatus',
            dbDatabase: '#dbDatabase'
        };

        navActive(ajax_url.page);

        init();

        function init() {
            tools.dataLoading();
            $.get(ajax_url.data, function (data) {
                tools.dataEndLoading();
                outputData(data);
            });
        }

        /**
         * 输出数据
         * @param data
         */
        function outputData(data) {
            dealStatus($(param_id.allStatus), data.status);
            dealStatus($(param_id.pingStatus), data.components.ping.status);
            dealStatus($(param_id.mailStatus), data.components.mail.status);
            $(param_id.mailLocation).text(data.components.mail.details.location);
            dealStatus($(param_id.diskStatus), data.components.diskSpace.status);
            $(param_id.diskTotal).text(tools.toSize(data.components.diskSpace.details.total));
            $(param_id.diskFree).text(tools.toSize(data.components.diskSpace.details.free));
            $(param_id.diskThreshold).text(tools.toSize(data.components.diskSpace.details.threshold));
            dealStatus($(param_id.redisStatus), data.components.redis.status);
            $(param_id.redisVersion).text(data.components.redis.details.version);
            dealStatus($(param_id.dbStatus), data.components.db.status);
            $(param_id.dbDatabase).text(data.components.db.details.database);
        }

        /**
         * 处理状态值
         * @param obj
         * @param status
         */
        function dealStatus(obj, status) {
            if (status === 'UP') {
                obj.addClass('text-success');
            } else {
                obj.addClass('text-danger');
            }
            obj.text(status);
        }

    });