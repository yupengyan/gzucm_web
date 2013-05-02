package cn.edu.gzucm.web.test;

import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import cn.edu.gzucm.web.data.model.User;

import com.mongodb.Mongo;

public class MongoTest {

    @Autowired
    private static MongoTemplate mongoTemplate;

    @BeforeClass
    public static void setUpClass() throws UnknownHostException {

        Mongo mongo = new Mongo("localhost");
        mongoTemplate = new MongoTemplate(mongo, "gzucm_core");
    }

    @Test
    public void testAddEntity() {

        User user = new User();
        user.setId("222222");
        mongoTemplate.save(user);
    }

}
