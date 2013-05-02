package cn.edu.gzucm.web.data.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import cn.edu.gzucm.web.data.model.base.BaseEntity;

/**
 * 微博用户实体类
 * 用户ID继承父类id字段；使用用户ID当作实体ID
 * @author jtian
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //    @Field(value = "scn")
    @JsonProperty("screen_name")
    private String screenName; //用户昵称

    //    @Field(value = "gd")
    private String gender;//性别，m：男、f：女、n：未知

    //    @Field(value = "pid")
    private int province;//用户所在省级ID

    //    @Field(value = "cid")
    private int city;//用户所在城市ID

    //    @Field(value = "foc")
    @JsonProperty("followers_count")
    private int followersCount;//粉丝数

    //    @Field(value = "frc")
    @JsonProperty("friends_count")
    private int friendsCount;//关注数

    //    @Field(value = "stc")
    @JsonProperty("statuses_count")
    private int statusesCount;//微博数

    //    @Field(value = "fac")
    @JsonProperty("favourites_count")
    private int favouritesCount;//收藏数

    //    @Field(value = "ca")
    @JsonProperty("created_at")
    private String createdAt;//创建时间

    //    @Field(value = "vf")
    private boolean verified;//是否是微博认证用户

    public String getScreenName() {

        return screenName;
    }

    public void setScreenName(String screenName) {

        this.screenName = screenName;
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

    public int getFollowersCount() {

        return followersCount;
    }

    public void setFollowersCount(int followersCount) {

        this.followersCount = followersCount;
    }

    public int getFriendsCount() {

        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {

        this.friendsCount = friendsCount;
    }

    public int getStatusesCount() {

        return statusesCount;
    }

    public void setStatusesCount(int statusesCount) {

        this.statusesCount = statusesCount;
    }

    public int getFavouritesCount() {

        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {

        this.favouritesCount = favouritesCount;
    }

    public String getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {

        this.createdAt = createdAt;
    }

    public boolean isVerified() {

        return verified;
    }

    public void setVerified(boolean verified) {

        this.verified = verified;
    }

}
