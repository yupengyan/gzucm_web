package cn.edu.gzucm.web.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id; //用户ID
    private String screen_name; //用户昵称
    private String gender;//性别，m：男、f：女、n：未知
    private int province;//用户所在省级ID
    private int city;//用户所在城市ID
    private int followers_count;//粉丝数
    private int friends_count;//关注数
    private int statuses_count;//微博数
    private int favourites_count;//收藏数
    private String created_at;//创建时间
    private boolean verified;//是否是微博认证用户

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public String getScreen_name() {

        return screen_name;
    }

    public void setScreen_name(String screen_name) {

        this.screen_name = screen_name;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public int getProvince() {

        return province;
    }

    public void setProvince(int province) {

        this.province = province;
    }

    public int getCity() {

        return city;
    }

    public void setCity(int city) {

        this.city = city;
    }

    public int getFollowers_count() {

        return followers_count;
    }

    public void setFollowers_count(int followers_count) {

        this.followers_count = followers_count;
    }

    public int getFriends_count() {

        return friends_count;
    }

    public void setFriends_count(int friends_count) {

        this.friends_count = friends_count;
    }

    public int getStatuses_count() {

        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {

        this.statuses_count = statuses_count;
    }

    public int getFavourites_count() {

        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {

        this.favourites_count = favourites_count;
    }

    public String getCreated_at() {

        return created_at;
    }

    public void setCreated_at(String created_at) {

        this.created_at = created_at;
    }

    public boolean isVerified() {

        return verified;
    }

    public void setVerified(boolean verified) {

        this.verified = verified;
    }

}
