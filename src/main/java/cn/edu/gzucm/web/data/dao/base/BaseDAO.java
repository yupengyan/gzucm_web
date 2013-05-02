package cn.edu.gzucm.web.data.dao.base;

import org.apache.log4j.Logger;

import cn.edu.gzucm.web.data.model.base.Entity;

public abstract class BaseDAO {

    protected final Logger _logger = Logger.getLogger(getClass());

    protected void backDataToDisk(Entity a_Entity, boolean a_Remove) {

        //        if (a_Entity == null) {
        //            return;
        //        }
        //        String filename = XKProperties.ENTITY_BACKUP_FOLDER + "/txt/" + a_Entity.getClass().getName() + "_" + a_Entity.getId() + ".txt";
        //        String content = null;
        //        if (a_Remove) {
        //            content = "REMOVE:" + a_Entity.getClass().getName() + ":" + a_Entity.getId();
        //        } else {
        //            content = XKUtils.objectToJson(a_Entity);
        //        }
        //        try {
        //            XKUtils.writeTextFile(filename, content);
        //        } catch (IOException e) {
        //            //            LoggerUtil.error(_logger, e);
        //        }
    }

    protected void backDataToDisk(String a_Hql) {

        //        String filename = XKProperties.ENTITY_BACKUP_FOLDER + "/txt/" + "_HQL_" + UUID.randomUUID().toString() + ".txt";
        //
        //        try {
        //            XKUtils.writeTextFile(filename, a_Hql);
        //        } catch (IOException e) {
        //            //            LoggerUtil.error(_logger, e);
        //        }
    }
}
