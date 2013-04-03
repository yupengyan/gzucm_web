package cn.edu.gzucm.web.data;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendsIdList {
    private long sourceUid;
    private List<Long> friendsId = new ArrayList<Long>();
    private int total_number;

    public long getSourceUid() {

        return sourceUid;
    }

    public void setSourceUid(long sourceUid) {

        this.sourceUid = sourceUid;
    }

    public List<Long> getFriendsId() {

        return friendsId;
    }

    public void setFriendsId(List<Long> friendsId) {

        this.friendsId = friendsId;
    }

    public int getTotal_number() {
    
        return total_number;
    }

    public void setTotal_number(int total_number) {
    
        this.total_number = total_number;
    }
}
