package cn.edu.gzucm.web.data.base;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * NoSQL的实体父类
 *
 */
public class BaseEntity implements Entity {

    private static final long serialVersionUID = 9007068617826085952L;

    @Id
    private String id;
    @Field(value = "cdt")
    private Date created;

    @Field(value = "upd")
    private Date updated;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Date getCreated() {

        return created;
    }

    public void setCreated(Date created) {

        this.created = created;
    }

    public Date getUpdated() {

        return updated;
    }

    public void setUpdated(Date updated) {

        this.updated = updated;
    }
}
