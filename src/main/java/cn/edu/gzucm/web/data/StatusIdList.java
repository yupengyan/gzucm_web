package cn.edu.gzucm.web.data;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusIdList {
    private List<String> statuses = new ArrayList<String>();
    private int total_number;

    public List<String> getStatuses() {

        return statuses;
    }

    public void setStatuses(List<String> statuses) {

        this.statuses = statuses;
    }

    public int getTotal_number() {

        return total_number;
    }

    public void setTotal_number(int total_number) {

        this.total_number = total_number;
    }
}
