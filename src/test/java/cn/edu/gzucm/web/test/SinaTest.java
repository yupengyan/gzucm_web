package cn.edu.gzucm.web.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cn.edu.gzucm.web.data.model.CommentListPerStatus;
import cn.edu.gzucm.web.data.model.RepostListPerStatus;
import cn.edu.gzucm.web.sns.ProviderConnector;
import cn.edu.gzucm.web.sns.MyApiException;
import cn.edu.gzucm.web.sns.sina.SinaWeibo2Connector;

public class SinaTest extends TestCase {

    private ProviderConnector connector;

    @Before
    public void setUp() {

        connector = new SinaWeibo2Connector("2.00nUAgzBRhkC3C895bcb2254rT7Q6E", null);
    }

    @Test
    public void testConnect() {

        assertNotNull(connector);
    }

    @Test
    public void testGetBlogLink() throws MyApiException {

        String blogLink = connector.getBlogLink("1218146700", "3524383697638522");
        assertNotNull(blogLink);
        System.out.println(blogLink);
    }

    @Test
    public void testGetIdByMid() throws MyApiException {

        String blogId = connector.getIdByMid("z9wGUt9MG", 1, 1);
        assertNotNull(blogId);
        System.out.println(blogId);
    }

    @Test
    public void testGetCommentList() throws MyApiException {

        CommentListPerStatus list = connector.getCommentList("3524383697638522");
        assertNotNull(list);
        System.out.println(list);
    }

    @Test
    public void testGetRepostList() throws MyApiException {

        RepostListPerStatus list = connector.getRepostList("3524383697638522");
        assertNotNull(list);
        System.out.println(list);
    }

}
