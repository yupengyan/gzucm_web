package cn.edu.gzucm.web.service.jobs;

import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import cn.edu.gzucm.web.service.SpringApplicationContext;
import cn.edu.gzucm.web.service.WeiboService;
import cn.edu.gzucm.web.sns.MyApiException;

public class FetchUserJob implements Job {

    private final Logger _logger = Logger.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("Execute FetchUser Job---" + new Date().toString());
        WeiboService weiboService = ((WeiboService) SpringApplicationContext.getBean("weiboService"));
        //从数据库中取出一个微博用户ID
        String userId = weiboService.getOneUnFetchUserID();
        if (userId == null) {
            userId = weiboService.getRandomUserId();//随机获取一个微博用户
        }
        if (userId != null) {
            try {
                if (weiboService.needFetchUser(userId)) {
                    weiboService.addUser(userId);
                }
                if (weiboService.needFetchFriends(userId)) {
                    Set<Long> friendsId = weiboService.addUserFriends(userId);
                    if (friendsId != null) {
                        weiboService.batchAddUserID(friendsId);
                    }
                }
                if (weiboService.needFetchFollowers(userId)) {
                    Set<Long> followersId = weiboService.addUserFollowers(userId);
                    if (followersId != null) {
                        weiboService.batchAddUserID(followersId);
                    }
                }
            } catch (MyApiException e) {
                _logger.error("", e);
                //TODO API调用超限，暂停Job
                if (e.getErrorCode() == MyApiException.USER_REQUESTS_OUT_OF_RATE_LIMIT) {
                    System.out.println("USER_REQUESTS_OUT_OF_RATE_LIMIT");
                    try {
                        context.getScheduler().pauseTrigger(context.getTrigger().getKey());
                    } catch (SchedulerException ex) {
                        _logger.error("", ex);
                    }
                }
            }
            weiboService.deleteOneFetchUser(userId);
        }
    }
}
