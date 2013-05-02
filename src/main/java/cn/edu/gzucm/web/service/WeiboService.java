package cn.edu.gzucm.web.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.gzucm.web.data.dao.FollowersDAO;
import cn.edu.gzucm.web.data.dao.FriendsDAO;
import cn.edu.gzucm.web.data.dao.UserDAO;
import cn.edu.gzucm.web.data.dao.UserIDDAO;
import cn.edu.gzucm.web.data.model.FollowersIdList;
import cn.edu.gzucm.web.data.model.FriendsIdList;
import cn.edu.gzucm.web.data.model.RateLimitStatus;
import cn.edu.gzucm.web.data.model.User;
import cn.edu.gzucm.web.data.model.UserID;
import cn.edu.gzucm.web.sns.MyApiException;
import cn.edu.gzucm.web.sns.sina.SinaWeibo2Connector;
import cn.edu.gzucm.web.utils.MyProperties;

@Service("weiboService")
public class WeiboService {
    private final Logger _logger = Logger.getLogger(getClass());

    @Autowired
    UserDAO userDAO;

    @Autowired
    FriendsDAO friendsDAO;

    @Autowired
    FollowersDAO followersDAO;

    @Autowired
    UserIDDAO userIDDAO;

    @Autowired
    SinaWeibo2Connector sinaConnector;

    public User addUser(String userId) throws MyApiException {

        User user = sinaConnector.getUserById(userId);
        if (user != null) {
            _logger.info("add user:" + userId);
            userDAO.add(user);
        }
        return user;
    }

    public Set<Long> addUserFriends(String userId) throws MyApiException {

        FriendsIdList idList = sinaConnector.getFriendsIdList(userId, 500, 0);
        if (idList != null) {
            _logger.info("add user friendsId");
            friendsDAO.add(idList);
            return new HashSet<Long>(idList.getFriendIds());
        }
        return null;
    }

    public Set<Long> addUserFollowers(String userId) throws MyApiException {

        FollowersIdList idList = sinaConnector.getFollowersIdList(userId, 500, 0);
        if (idList != null) {
            _logger.info("add user followersId");
            followersDAO.add(idList);
            return new HashSet<Long>(idList.getFollowerIds());
        }
        return null;
    }

    public void batchAddUserID(Set<Long> userIds) {

        if (userIds != null)
            userIDDAO.batchAddUserId(userIds);
    }

    public String getOneUnFetchUserID() {

        UserID userID = userIDDAO.findLatest();
        if (userID != null) {
            _logger.info("get one unfetched user id:" + userID.getId());
            return userID.getId();
        }
        return null;
    }

    public void deleteOneFetchUser(String userId) {

        _logger.info("remove user id:" + userId);
        userIDDAO.removeById(userId);
    }

    public boolean needFetchUser(String userId) {

        boolean result = true;
        User user = userDAO.findById(userId);
        if (null != user) {
            _logger.info("not need fetch user:" + userId);
            result = false;
        }
        return result;
    }

    public boolean needFetchFriends(String userId) {

        boolean result = true;
        FriendsIdList friendsIdList = friendsDAO.findById(userId);
        if (null != friendsIdList) {
            _logger.info("not need fetch user friends:" + userId);
            result = false;
        }
        return result;
    }

    public boolean needFetchFollowers(String userId) {

        boolean result = true;
        FollowersIdList followersIdList = followersDAO.findById(userId);
        if (null != followersIdList) {
            _logger.info("not need fetch user followers:" + userId);
            result = false;
        }
        return result;
    }

    public boolean isRequestOutRate() {

        boolean result = false;
        RateLimitStatus limitStatus = null;
        try {
            limitStatus = sinaConnector.getRateLimitStatus(MyProperties.getProperty("weibo.accessToken"));
        } catch (MyApiException e) {
            limitStatus = null;
            _logger.error("", e);
        }
        if (limitStatus == null || limitStatus.getRemainingUserHits() < 5) {
            result = true;
        }
        return result;
    }

    public String getRandomUserId() {

        return "1827501013";
    }
}
