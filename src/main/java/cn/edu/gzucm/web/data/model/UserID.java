package cn.edu.gzucm.web.data.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.edu.gzucm.web.data.model.base.BaseEntity;

/**
 * 微博用户ID
 * 用户ID继承父类id字段；使用用户ID当作实体ID
 * @author jtian
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserID extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public UserID() {

    }

    public UserID(String userId) {

        Date now = new Date();
        super.setId(userId);
        super.setCreated(now);
        super.setUpdated(now);
    }

}
