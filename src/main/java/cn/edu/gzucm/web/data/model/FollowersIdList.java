package cn.edu.gzucm.web.data.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Transient;

import cn.edu.gzucm.web.data.model.base.BaseEntity;

/**
 * 微博用户粉丝ID列表
 * 注：用户ID继承父类id字段；使用用户ID当作实体ID
 * @author jtian
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowersIdList extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //    @Field(value = "foid")
    @JsonProperty("ids")
    private List<Long> followerIds = new ArrayList<Long>();//粉丝ID列表

    @Transient
    @JsonProperty("next_cursor")
    private int nextCursor = 0;//下一次结果游标，用于分页查询
    @Transient
    @JsonProperty("previous_cursor")
    private int previousCursor = 0;//上一次结果游标，用于分页查询

    //    @Field(value = "tn")
    @JsonProperty("total_number")
    private int totalNumber;//粉丝总数

    public List<Long> getFollowerIds() {

        return followerIds;
    }

    public void setFollowerIds(List<Long> followerIds) {

        this.followerIds = followerIds;
    }

    public int getNextCursor() {

        return nextCursor;
    }

    public void setNextCursor(int nextCursor) {

        this.nextCursor = nextCursor;
    }

    public int getPreviousCursor() {

        return previousCursor;
    }

    public void setPreviousCursor(int previousCursor) {

        this.previousCursor = previousCursor;
    }

    public int getTotalNumber() {

        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {

        this.totalNumber = totalNumber;
    }
}
