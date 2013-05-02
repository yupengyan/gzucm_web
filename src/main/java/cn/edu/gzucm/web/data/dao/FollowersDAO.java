package cn.edu.gzucm.web.data.dao;

import org.springframework.stereotype.Repository;

import cn.edu.gzucm.web.data.dao.base.NosqlBaseDAO;
import cn.edu.gzucm.web.data.model.FollowersIdList;

@Repository("followersDAO")
public class FollowersDAO extends NosqlBaseDAO<FollowersIdList> {

    @Override
    public Class<FollowersIdList> getEntityClass() {

        return FollowersIdList.class;
    }

}
