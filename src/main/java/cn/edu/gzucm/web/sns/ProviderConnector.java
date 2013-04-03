package cn.edu.gzucm.web.sns;

import java.util.Map;

import org.scribe.model.Verb;

import cn.edu.gzucm.web.data.CommentListPerStatus;
import cn.edu.gzucm.web.data.RepostListPerStatus;
import cn.edu.gzucm.web.data.StatusIdList;

public interface ProviderConnector {
    public <T> T makeCall(Verb verb, String api, Class<T> returnType, Map<String, Object> parameters) throws MyApiException;

    public <T> T makeCall(String api, String fileParam, String filePath, Class<T> returnType, Map<String, Object> parameters) throws MyApiException;

    public void destroyStatus(final String id) throws MyApiException;

    public String getBlogLink(final String userId, final String blogId) throws MyApiException;

    public String getIdByMid(final String mid, final int type, final int isBase62) throws MyApiException;

    public CommentListPerStatus getCommentList(final String blogId) throws MyApiException;

    public RepostListPerStatus getRepostList(final String blogId) throws MyApiException;

    public StatusIdList getStatusIdList(final String userId, final String sinceId, final String maxId, final Integer count, final Integer page) throws MyApiException;
}
