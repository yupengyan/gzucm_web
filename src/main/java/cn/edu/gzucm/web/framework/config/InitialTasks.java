package cn.edu.gzucm.web.framework.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.edu.gzucm.web.service.TimerService;
import cn.edu.gzucm.web.service.jobs.FetchUserJob;
import cn.edu.gzucm.web.service.jobs.APIRequestRateMonitorJob;

public class InitialTasks {

    private final Logger _logger = Logger.getLogger(getClass());
    @Autowired
    TimerService timerService;

    public void initMethod() {

        timerService.addMinuteJob(5, "MinuteJob", "System", new APIRequestRateMonitorJob());
        timerService.addSecondJob(30, "SecondJob", "System", new FetchUserJob());
        _logger.info("** initMethod done.");
    }
}
