package cn.edu.gzucm.web.data.dao;

import org.springframework.stereotype.Repository;

import cn.edu.gzucm.web.data.dao.base.NosqlBaseDAO;
import cn.edu.gzucm.web.data.model.FriendsIdList;

@Repository("friendsDAO")
public class FriendsDAO extends NosqlBaseDAO<FriendsIdList> {

    @Override
    public Class<FriendsIdList> getEntityClass() {

        return FriendsIdList.class;
    }

}
