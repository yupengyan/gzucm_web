package cn.edu.gzucm.web.data.dao;

import org.springframework.stereotype.Repository;

import cn.edu.gzucm.web.data.dao.base.NosqlBaseDAO;
import cn.edu.gzucm.web.data.model.User;

@Repository("userDAO")
public class UserDAO extends NosqlBaseDAO<User> {

    @Override
    public Class<User> getEntityClass() {

        return User.class;
    }

}
