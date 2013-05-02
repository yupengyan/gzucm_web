package cn.edu.gzucm.web.data.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RepostListPerStatus {

    @JsonDeserialize(contentAs = Status.class)
    @JsonSerialize(contentAs = Status.class)
    private List<Status> reposts = new ArrayList<Status>();
    private int total_number;

    public List<Status> getStatuses() {

        return reposts;
    }

    public void setStatuses(List<Status> statuses) {

        this.reposts = statuses;
    }

    public int getTotal_number() {

        return total_number;
    }

    public void setTotal_number(int total_number) {

        this.total_number = total_number;
    }
}
