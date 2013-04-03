package cn.edu.gzucm.web.test;

import java.util.HashMap;
import java.util.Map;

import cn.edu.gzucm.web.data.Comment;
import cn.edu.gzucm.web.data.CommentListPerStatus;
import cn.edu.gzucm.web.data.RepostListPerStatus;
import cn.edu.gzucm.web.data.Status;
import cn.edu.gzucm.web.data.StatusIdList;
import cn.edu.gzucm.web.sns.ProviderConnector;
import cn.edu.gzucm.web.sns.MyApiException;
import cn.edu.gzucm.web.sns.sina.SinaWeibo2Connector;

public class RelationTest {
    private static ProviderConnector connector;
    private static Map<String, Integer> commentMap = new HashMap<String, Integer>();
    private static Map<String, Integer> repostMap = new HashMap<String, Integer>();
    private static Map<String, Integer> sumMap = new HashMap<String, Integer>();

    public static void main(String[] args) throws MyApiException {

        connector = new SinaWeibo2Connector("2.00nUAgzBRhkC3C895bcb2254rT7Q6E", null);
        String userId = "2137428673";
        StatusIdList ids = connector.getStatusIdList(userId, null, null, 100, 1);
        //        String userId = "1218146700";
        //        String sinceId = "3519671934759646";
        //        StatusIdList ids = connector.getStatusIdList(userId, sinceId, null, 100, 1);
        for (String blogId : ids.getStatuses()) {
            getCommentMap(blogId);
            getRepostMap(blogId);
        }
        System.out.println(commentMap);
        System.out.println(repostMap);
    }

    public static void getCommentMap(String blogId) throws MyApiException {

        CommentListPerStatus commentList = connector.getCommentList(blogId);
        if (null != commentList) {
            for (Comment com : commentList.getComments()) {
                if (commentMap.containsKey(com.getUser().getScreen_name())) {
                    commentMap.put(com.getUser().getScreen_name(), commentMap.get(com.getUser().getScreen_name()) + 1);
                } else {
                    commentMap.put(com.getUser().getScreen_name(), 1);
                }
            }
        }
    }

    public static void getRepostMap(String blogId) throws MyApiException {

        RepostListPerStatus repostList = connector.getRepostList(blogId);
        if (null != repostList) {
            for (Status sta : repostList.getStatuses()) {
                if (repostMap.containsKey(sta.getUser().getScreen_name())) {
                    repostMap.put(sta.getUser().getScreen_name(), repostMap.get(sta.getUser().getScreen_name()) + 1);
                } else {
                    repostMap.put(sta.getUser().getScreen_name(), 1);
                }
            }
        }
    }
}
