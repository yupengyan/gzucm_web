package cn.edu.gzucm.web.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.gzucm.web.service.jobs.FetchUserJob;
import cn.edu.gzucm.web.utils.XKUtils;

@Service("timerService")
public class TimerService {
    private final Logger _logger = Logger.getLogger(getClass());

    @Autowired
    Scheduler myScheduler;

    private SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();

    public void addFetchUserJob(String a_Id, String a_Group) {

        addFetchUserJob(null, a_Id, a_Group);
    }

    public void addFetchUserJob(Date a_Time, String a_Id, String a_Group) {

        Trigger trigger = null;
        if (a_Time == null) {
            trigger = TriggerBuilder.newTrigger().withIdentity(a_Id.toString(), a_Group).startNow().withSchedule(scheduleBuilder).build();
        } else {
            //加几秒避免突发性并发导致瞬间访问很大
            a_Time = DateUtils.addSeconds(a_Time, XKUtils.getRandom(0, 20));
            trigger = TriggerBuilder.newTrigger().withIdentity(a_Id.toString(), a_Group).startAt(a_Time).withSchedule(scheduleBuilder).build();
        }

        // Tell quartz to schedule the job using our trigger
        if (trigger != null) {
            try {
                JobKey jobKey = new JobKey(a_Id.toString(), a_Group);
                if (!myScheduler.checkExists(jobKey)) {
                    JobDetail job = JobBuilder.newJob(FetchUserJob.class).withIdentity(a_Id.toString(), a_Group).build();
                    myScheduler.scheduleJob(job, trigger);
                }
            } catch (SchedulerException e) {
                _logger.error("", e);
            }
        }
    }

    public void updateJob(Date a_NewTime, String a_Id, String a_Group) throws SchedulerException {

        TriggerKey triggerKey = new TriggerKey(a_Id, a_Group);
        Trigger trigger = myScheduler.getTrigger(triggerKey);
        if (trigger != null) {
            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(a_NewTime).withSchedule(scheduleBuilder).build();
            myScheduler.rescheduleJob(triggerKey, newTrigger);
        }
    }

    public void runJobNow(String a_Id, String a_Group) throws SchedulerException {

        TriggerKey triggerKey = new TriggerKey(a_Id, a_Group);
        Trigger trigger = myScheduler.getTrigger(triggerKey);
        if (trigger != null) {
            Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startNow().withSchedule(scheduleBuilder).build();
            myScheduler.rescheduleJob(triggerKey, newTrigger);
        }
    }

    public boolean removeJob(String a_Id, String a_Group) throws SchedulerException {

        JobKey jobKey = new JobKey(a_Id, a_Group);
        return myScheduler.deleteJob(jobKey);

    }

    public void addWeeklyJob(String a_Id, String a_Group, Job a_JobClass) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();

        //每周一的凌晨一点运行
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 ? * MON").withMisfireHandlingInstructionFireAndProceed()).forJob(job).build();

        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

    /**
     * 添加一个每天运行一次的job
     * @param a_Time HH:mm
     * @param a_Id
     * @param a_Group
     * @param a_JobClass
     */
    public void addDailyJob(String a_Time, String a_Id, String a_Group, Job a_JobClass) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();
        Date runTime = XKUtils.getTime(a_Time);
        if (XKUtils.compareDates(runTime, new Date()) == -1) {
            runTime = DateUtils.addDays(runTime, 1);
        }

        //Runs every 24 hours(daily)
        SimpleScheduleBuilder sBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(24).withMisfireHandlingInstructionFireNow();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(runTime).withSchedule(sBuilder).build();

        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

    /**
     * 添加一个每小时运行一次的job
     * @param a_Time HH:mm
     * @param a_Id
     * @param a_Group
     * @param a_JobClass
     */
    public void addHourlyJob(String a_Id, String a_Group, Job a_JobClass) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();

        Date runTime = DateUtils.truncate(new Date(), Calendar.HOUR);
        //每小时的43分运行一次，错过整点
        //runTime = DateUtils.addMinutes(runTime, 22);
        //Runs every 1 hours
        SimpleScheduleBuilder sBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInHours(1).withMisfireHandlingInstructionIgnoreMisfires();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(runTime).withSchedule(sBuilder).build();

        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

    /**
     * 添加一个每分钟运行一次的job
     * @param a_Id
     * @param a_Group
     * @param a_JobClass
     */
    public void addMinuteJob(String a_Id, String a_Group, Job a_JobClass) {

        addMinuteJob(1, a_Id, a_Group, a_JobClass);
    }

    public void addMinuteJob(int minutes, String a_Id, String a_Group, Job a_JobClass) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();

        Date runTime = DateUtils.truncate(new Date(), Calendar.MINUTE);
        SimpleScheduleBuilder sBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMinutes(minutes).withMisfireHandlingInstructionIgnoreMisfires();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(runTime).withSchedule(sBuilder).build();

        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

    public void addSecondJob(int seconds, String a_Id, String a_Group, Job a_JobClass) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();

        Date runTime = DateUtils.truncate(new Date(), Calendar.SECOND);
        SimpleScheduleBuilder sBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(seconds).withMisfireHandlingInstructionIgnoreMisfires();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(runTime).withSchedule(sBuilder).build();

        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

    public void addJob(Date planTime, String a_Id, String a_Group, Job a_JobClass) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();

        SimpleScheduleBuilder sBuilder = SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();

        Trigger trigger = null;

        trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(planTime).withSchedule(sBuilder).build();

        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

    public void runJob(String a_Id, String a_Group, Job a_JobClass, int delaySeconds) {

        JobDetail job = JobBuilder.newJob(a_JobClass.getClass()).withIdentity(a_Id, a_Group).build();

        SimpleScheduleBuilder sBuilder = SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();

        Trigger trigger = null;

        if (delaySeconds == 0) {
            trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startNow().withSchedule(sBuilder).build();
        } else {
            Date triggerStartTime = new Date();
            triggerStartTime = DateUtils.addSeconds(triggerStartTime, delaySeconds);
            trigger = TriggerBuilder.newTrigger().withIdentity(a_Id, a_Group).startAt(triggerStartTime).withSchedule(sBuilder).build();
        }
        // Tell quartz to schedule the job using our trigger
        try {
            myScheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            _logger.error("", e);
        }
    }

}
