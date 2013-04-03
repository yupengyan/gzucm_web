package cn.edu.gzucm.web.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id; //微博ID
    private long mid; //微博MID
    private String text; //微博内容
    private User user; // 作者信息
    private String created_at; // 创建时间
    private long reposts_count; // 转发数
    private long comments_count; // 评论数

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getMid() {

        return mid;
    }

    public void setMid(long mid) {

        this.mid = mid;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public String getCreated_at() {

        return created_at;
    }

    public void setCreated_at(String created_at) {

        this.created_at = created_at;
    }

    public long getReposts_count() {

        return reposts_count;
    }

    public void setReposts_count(long reposts_count) {

        this.reposts_count = reposts_count;
    }

    public long getComments_count() {

        return comments_count;
    }

    public void setComments_count(long comments_count) {

        this.comments_count = comments_count;
    }
}
