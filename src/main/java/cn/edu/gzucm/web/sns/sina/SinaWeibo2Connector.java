package cn.edu.gzucm.web.sns.sina;

import java.util.HashMap;
import java.util.Map;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import cn.edu.gzucm.web.data.CommentListPerStatus;
import cn.edu.gzucm.web.data.RepostListPerStatus;
import cn.edu.gzucm.web.data.StatusIdList;
import cn.edu.gzucm.web.sns.MyApiException;
import cn.edu.gzucm.web.sns.ProviderConnectorImpl;
import cn.edu.gzucm.web.utils.StringEncrypter;

public class SinaWeibo2Connector extends ProviderConnectorImpl {

    private static final String PROTECTED_RESOURCE_URL = "https://api.weibo.com/2/";
    private String apiKey;
    private String apiSecret;

    public SinaWeibo2Connector(String userToken, String userSecret) {

        super(PROTECTED_RESOURCE_URL);
        apiKey = StringEncrypter.decode("if9teGOiqlegHrictfEe%2BA%3D%3D%0D%0A", hash);
        apiSecret = StringEncrypter.decode("%2FJfjnY03ndJYRIiNxknQfDlTrDVhxcToeGhO0zVhSdJPwPGOL5qakg%3D%3D%0D%0A", hash);
        service = new ServiceBuilder().provider(SinaWeibo2Api.class).apiKey(apiKey).apiSecret(apiSecret).build();
        accessToken = new Token(userToken, null);
    }

    public void destroyStatus(final String id) throws MyApiException {

        Map<String, Object> statusParams = new HashMap<String, Object>();
        statusParams.put("id", id);
        makeCall(Verb.POST, "statuses/destroy.json", null, statusParams);
    }

    //获取微博地址
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
}