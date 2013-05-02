package cn.edu.gzucm.web.test;

import java.util.HashMap;
import java.util.Map;

import cn.edu.gzucm.web.data.model.Comment;
import cn.edu.gzucm.web.data.model.CommentListPerStatus;
import cn.edu.gzucm.web.data.model.RepostListPerStatus;
import cn.edu.gzucm.web.data.model.Status;
import cn.edu.gzucm.web.data.model.User;
import cn.edu.gzucm.web.sns.MyApiException;
import cn.edu.gzucm.web.sns.ProviderConnector;
import cn.edu.gzucm.web.sns.sina.SinaWeibo2Connector;

public class RelationTest {
    private static ProviderConnector connector;
    private static Map<String, Integer> commentMap = new HashMap<String, Integer>();
    private static Map<String, Integer> repostMap = new HashMap<String, Integer>();
    private static Map<String, Integer> sumMap = new HashMap<String, Integer>();

    public static void main(String[] args) throws MyApiException {

        //        connector = new SinaWeibo2Connector("2.00nUAgzBRhkC3C895bcb2254rT7Q6E", "");
        //        connector = new SinaWeibo2Connector("2.00nUAgzB_him1E80041d1e2059KAaB", "");
        connector = new SinaWeibo2Connector("", "");
        String userId = "1266321801";
        //        String userId = "1827501013";
        //FriendsIdList friendsIdList = connector.getFriendsIdList(userId, 600, 0);
        //        FollowersIdList followersIdList = connector.getFollowersIdList(userId, 600, 0);
        User user = connector.getUserById(userId);
        //        StatusIdList ids = connector.getStatusIdList(userId, null, null, 100, 1);
        //        String userId = "1218146700";
        //        String sinceId = "3519671934759646";
        //        StatusIdList ids = connector.getStatusIdList(userId, sinceId, null, 100, 1);
        //        for (String blogId : ids.getStatuses()) {
        //            getCommentMap(blogId);
        //            getRepostMap(blogId);
        //        }
        //        System.out.println(commentMap);
        //        System.out.println(repostMap);
        System.out.println("done");
    }

    public static void getCommentMap(String blogId) throws MyApiException {

        CommentListPerStatus commentList = connector.getCommentList(blogId);
        if (null != commentList) {
            for (Comment com : commentList.getComments()) {
                if (commentMap.containsKey(com.getUser().getScreenName())) {
                    commentMap.put(com.getUser().getScreenName(), commentMap.get(com.getUser().getScreenName()) + 1);
                } else {
                    commentMap.put(com.getUser().getScreenName(), 1);
                }
            }
        }
    }

    public static void getRepostMap(String blogId) throws MyApiException {

        RepostListPerStatus repostList = connector.getRepostList(blogId);
        if (null != repostList) {
            for (Status sta : repostList.getStatuses()) {
                if (repostMap.containsKey(sta.getUser().getScreenName())) {
                    repostMap.put(sta.getUser().getScreenName(), repostMap.get(sta.getUser().getScreenName()) + 1);
                } else {
                    repostMap.put(sta.getUser().getScreenName(), 1);
                }
            }
        }
    }
}
