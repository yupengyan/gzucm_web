package cn.edu.gzucm.web.sns;

import java.util.Map;

import org.scribe.model.Verb;

import cn.edu.gzucm.web.data.model.CommentListPerStatus;
import cn.edu.gzucm.web.data.model.FollowersIdList;
import cn.edu.gzucm.web.data.model.FriendsIdList;
import cn.edu.gzucm.web.data.model.RateLimitStatus;
import cn.edu.gzucm.web.data.model.RepostListPerStatus;
import cn.edu.gzucm.web.data.model.StatusIdList;
import cn.edu.gzucm.web.data.model.User;

public interface ProviderConnector {
    public <T> T makeCall(Verb verb, String api, Class<T> returnType, Map<String, Object> parameters) throws MyApiException;

    public <T> T makeCall(String api, String fileParam, String filePath, Class<T> returnType, Map<String, Object> parameters) throws MyApiException;

    public void destroyStatus(final String id) throws MyApiException;

    public String getBlogLink(final String userId, final String blogId) throws MyApiException;

    public String getIdByMid(final String mid, final int type, final int isBase62) throws MyApiException;

    public CommentListPerStatus getCommentList(final String blogId) throws MyApiException;

    public RepostListPerStatus getRepostList(final String blogId) throws MyApiException;

    public StatusIdList getStatusIdList(final String userId, final String sinceId, final String maxId, final Integer count, final Integer page) throws MyApiException;

    public User getUserById(final String userId) throws MyApiException;

    /**
     * 获取关注好友id列表
     * 由于新浪api限制，最多只能获取500条记录
     * @param userId 用户ID
     * @param count 返回记录条数（api限制，该参数暂时不起作用）
     * @param cursor 返回记录游标（api限制，该参数暂时不起作用）
     * @return
     * @throws MyApiException
     */
    public FriendsIdList getFriendsIdList(final String userId, int count, int cursor) throws MyApiException;

    /**
     * 获取粉丝id列表
     * 由于新浪api限制，最多只能获取500条记录
     * @param userId 用户ID
     * @param count 返回记录条数（api限制，该参数暂时不起作用）
     * @param cursor 返回记录游标（api限制，该参数暂时不起作用）
     * @return
     * @throws MyApiException
     */
    public FollowersIdList getFollowersIdList(final String userId, int count, int cursor) throws MyApiException;

    /**
     * 获取当前登录用户的API访问频率限制情况
     * @param accessToken
     * @return
     * @throws MyApiException
     */
    public RateLimitStatus getRateLimitStatus(String accessToken) throws MyApiException;
}
