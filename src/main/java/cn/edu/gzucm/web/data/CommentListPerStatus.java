package cn.edu.gzucm.web.data;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentListPerStatus {
    @JsonDeserialize(contentAs = Comment.class)
    @JsonSerialize(contentAs = Comment.class)
    private List<Comment> comments = new ArrayList<Comment>();
    private int total_number;

    public List<Comment> getComments() {

        return comments;
    }

    public void setComments(List<Comment> comments) {

        this.comments = comments;
    }

    public int getTotal_number() {

        return total_number;
    }

    public void setTotal_number(int total_number) {

        this.total_number = total_number;
    }
}
