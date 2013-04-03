package cn.edu.gzucm.web.data;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowersIdList {
    private long sourceUid;
    private List<Long> followersId = new ArrayList<Long>();
    private int total_number;

    public long getSourceUid() {

        return sourceUid;
    }

    public void setSourceUid(long sourceUid) {

        this.sourceUid = sourceUid;
    }

    public List<Long> getFollowersId() {

        return followersId;
    }

    public void setFollowersId(List<Long> followersId) {

        this.followersId = followersId;
    }

    public int getTotal_number() {
    
        return total_number;
    }

    public void setTotal_number(int total_number) {
    
        this.total_number = total_number;
    }
}
