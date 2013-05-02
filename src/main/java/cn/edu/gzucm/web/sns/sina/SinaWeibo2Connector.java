package cn.edu.gzucm.web.sns.sina;

import java.util.HashMap;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import cn.edu.gzucm.web.data.model.CommentListPerStatus;
import cn.edu.gzucm.web.data.model.FollowersIdList;
import cn.edu.gzucm.web.data.model.FriendsIdList;
import cn.edu.gzucm.web.data.model.RateLimitStatus;
import cn.edu.gzucm.web.data.model.RepostListPerStatus;
import cn.edu.gzucm.web.data.model.StatusIdList;
import cn.edu.gzucm.web.data.model.User;
import cn.edu.gzucm.web.sns.MyApiException;
import cn.edu.gzucm.web.sns.ProviderConnectorImpl;

public class SinaWeibo2Connector extends ProviderConnectorImpl {

    private static final String PROTECTED_RESOURCE_URL = "https://api.weibo.com/2/";
    private String apiKey;
    private String apiSecret;

    public SinaWeibo2Connector(String userToken, String userSecret) {

        super(PROTECTED_RESOURCE_URL);
        //        apiKey = StringEncrypter.decode("if9teGOiqlegHrictfEe%2BA%3D%3D%0D%0A", hash);
        //        apiSecret = StringEncrypter.decode("%2FJfjnY03ndJYRIiNxknQfDlTrDVhxcToeGhO0zVhSdJPwPGOL5qakg%3D%3D%0D%0A", hash);

        apiKey = "3971669613";
        apiSecret = "c16509133f90c5f59ec1297ca5326643";
        //        apiKey = "2158001219";
        //        apiSecret = "f8294c8ff0ddff3df4031397e19b6f62";

        service = new ServiceBuilder().provider(SinaWeibo2Api.class).apiKey(apiKey).apiSecret(apiSecret).build();
        accessToken = new Token(userToken, userSecret);
    }

    public void destroyStatus(final String id) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("id", id);
        makeCall(Verb.POST, "statuses/destroy.json", null, statusParams);
    }

    public String getBlogLink(final String userId, final String blogId) {

        return "http://api.t.sina.com.cn/" + userId + "/statuses/" + blogId;
    }

    public String getIdByMid(final String mid, final int type, final int isBase62) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("mid", mid);
        statusParams.put("type", type);
        statusParams.put("isBase62", isBase62);
        return makeCall(Verb.GET, "statuses/queryid.json", null, statusParams);
    }

    public CommentListPerStatus getCommentList(final String blogId) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("id", blogId);
        return makeCall(Verb.GET, "comments/show.json", CommentListPerStatus.class, statusParams);
    }

    public RepostListPerStatus getRepostList(final String blogId) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("id", blogId);
        return makeCall(Verb.GET, "statuses/repost_timeline.json", RepostListPerStatus.class, statusParams);
    }

    public StatusIdList getStatusIdList(final String userId, final String sinceId, final String maxId, final Integer count, final Integer page) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("uid", userId);
        if (sinceId != null && !sinceId.equals("")) {
            statusParams.put("since_id ", sinceId);
        }
        if (maxId != null && !maxId.equals("")) {
            statusParams.put("max_id ", maxId);
        }
        if (count != null) {
            statusParams.put("count ", count);
        }
        if (page != null) {
            statusParams.put("page ", page);
        }
        return makeCall(Verb.GET, "statuses/user_timeline/ids.json", StatusIdList.class, statusParams);
    }

    public User getUserById(final String userId) throws MyApiException {

        User result = new User();
        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("uid", userId);
        result = makeCall(Verb.GET, "users/show.json", User.class, statusParams);
        result.setId(userId);
        return result;
    }

    public FriendsIdList getFriendsIdList(final String userId, int count, int cursor) throws MyApiException {

        FriendsIdList result = new FriendsIdList();
        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("uid", userId);
        if (count <= 0 || count > 5000) {
            count = 500;
        }
        if (cursor < 0) {
            cursor = 0;
        }
        statusParams.put("count ", count);
        statusParams.put("cursor ", cursor);
        result = makeCall(Verb.GET, "friendships/friends/ids.json", FriendsIdList.class, statusParams);
        if (result != null) {
            result.setId(userId);
        }
        return result;
        //        FriendsIdList subResult = null;
        //        subResult = makeCall(Verb.GET, "friendships/friends/ids.json", FriendsIdList.class, statusParams);
        //        if (subResult != null) {
        //            result.setId(userId);
        //            result.getFriendsId().addAll(subResult.getFriendsId());
        //            result.setTotalNumber(subResult.getTotalNumber());
        //            if (subResult.getNextCursor() != 0) {
        //                subResult = getFriendsIdList(userId, count, subResult.getNextCursor());
        //            }
        //        }
    }

    @Override
    public FollowersIdList getFollowersIdList(String userId, int count, int cursor) throws MyApiException {

        FollowersIdList result = new FollowersIdList();
        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("uid", userId);
        if (count <= 0 || count > 5000) {
            count = 500;
        }
        if (cursor < 0) {
            cursor = 0;
        }
        statusParams.put("count ", count);
        statusParams.put("cursor ", cursor);
        result = makeCall(Verb.GET, "friendships/followers/ids.json", FollowersIdList.class, statusParams);
        if (result != null) {
            result.setId(userId);
        }
        return result;
        //        FollowersIdList subResult = null;
        //        subResult = makeCall(Verb.GET, "friendships/followers/ids.json", FollowersIdList.class, statusParams);
        //        if (subResult != null) {
        //            result.setId(userId);
        //            result.getFollowersId().addAll(subResult.getFollowersId());
        //            result.setTotalNumber(subResult.getTotalNumber());
        //            if (subResult.getNextCursor() != 0) {
        //                subResult = getFollowersIdList(userId, count, subResult.getNextCursor());
        //            }
        //        }
    }

    public RateLimitStatus getRateLimitStatus(String accessToken) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("access_token", accessToken);
        return makeCall(Verb.GET, "account/rate_limit_status.json", RateLimitStatus.class, statusParams);
    }

}