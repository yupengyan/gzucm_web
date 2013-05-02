package cn.edu.gzucm.web.service.jobs;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import cn.edu.gzucm.web.service.SpringApplicationContext;
import cn.edu.gzucm.web.service.WeiboService;

public class APIRequestRateMonitorJob implements Job {

    private final Logger _logger = Logger.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("Execute Manager Job---" + new Date().toString());
        WeiboService weiboService = ((WeiboService) SpringApplicationContext.getBean("weiboService"));
        Scheduler scheduler = context.getScheduler();
        try {
            if (weiboService.isRequestOutRate()) {
                TriggerKey triggerKey = getTriggerKey(scheduler, "SecondJob");
                if (triggerKey != null)
                    scheduler.pauseTrigger(triggerKey);
            } else {
                scheduler.resumeAll();
            }
        } catch (SchedulerException ex) {
            _logger.error("", ex);
        }
    }

    private TriggerKey getTriggerKey(Scheduler scheduler, String triggerName) {

        try {
            List<String> groupNames = scheduler.getTriggerGroupNames();
            for (String groupName : groupNames) {
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
                for (TriggerKey triggerKey : triggerKeys) {
                    if (triggerKey.getName().equals(triggerName)) {
                        return triggerKey;
                    }
                }
            }
        } catch (SchedulerException e) {
            _logger.error("", e);
            return null;
        }
        return null;
    }
}
