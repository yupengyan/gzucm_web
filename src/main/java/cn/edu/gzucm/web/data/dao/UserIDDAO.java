package cn.edu.gzucm.web.data.dao;

import java.util.Set;

import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.edu.gzucm.web.data.dao.base.NosqlBaseDAO;
import cn.edu.gzucm.web.data.model.UserID;

@Repository("userIDDAO")
public class UserIDDAO extends NosqlBaseDAO<UserID> {

    @Override
    public Class<UserID> getEntityClass() {

        return UserID.class;
    }

    public void batchAddUserId(Set<Long> userIds) {

        //        List<UserID> batchToSave = new ArrayList<UserID>();
        //        for (Long userId : userIds) {
        //            batchToSave.add(new UserID(userId.toString()));
        //        }
        //        try {
        //            super.mongoTemplate.insert(batchToSave, getEntityClass());
        //        } catch (Exception e) {
        //            System.err.println("MongoException$DuplicateKey:" + e.getMessage());
        //        }
        for (Long userId : userIds) {
            super.mongoTemplate.save(new UserID(userId.toString()));
        }
    }

    public UserID findLatest() {

        Query query = new Query();
        query.sort().on("cd", Order.DESCENDING);
        return super.findFirst(query);
    }
}
