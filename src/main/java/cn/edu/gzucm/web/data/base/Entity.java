package cn.edu.gzucm.web.data.base;

import java.io.Serializable;
import java.util.Date;

public interface Entity extends Serializable {

    public String getId();

    public void setId(String id);

    public void setCreated(Date date);

    public void setUpdated(Date date);

    public Date getCreated();

    public Date getUpdated();
}
