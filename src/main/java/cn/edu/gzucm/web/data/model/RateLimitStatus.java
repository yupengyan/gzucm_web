package cn.edu.gzucm.web.data.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * API访问频率状态
 * @author jtian
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RateLimitStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("limit_time_unit")
    private String limitTimeUnit;

    @JsonProperty("ip_limit")
    private int ipLimit;

    @JsonProperty("remaining_ip_hits")
    private int remainingIpHits;

    @JsonProperty("user_limit")
    private int userLimit;

    @JsonProperty("remaining_user_hits")
    private int remainingUserHits;

    @JsonProperty("reset_time")
    private String resetTime;

    @JsonProperty("reset_time_in_seconds")
    private int resetTimeInSeconds;

    public String getLimitTimeUnit() {

        return limitTimeUnit;
    }

    public void setLimitTimeUnit(String limitTimeUnit) {

        this.limitTimeUnit = limitTimeUnit;
    }

    public int getIpLimit() {

        return ipLimit;
    }

    public void setIpLimit(int ipLimit) {

        this.ipLimit = ipLimit;
    }

    public int getRemainingIpHits() {

        return remainingIpHits;
    }

    public void setRemainingIpHits(int remainingIpHits) {

        this.remainingIpHits = remainingIpHits;
    }

    public int getUserLimit() {

        return userLimit;
    }

    public void setUserLimit(int userLimit) {

        this.userLimit = userLimit;
    }

    public int getRemainingUserHits() {

        return remainingUserHits;
    }

    public void setRemainingUserHits(int remainingUserHits) {

        this.remainingUserHits = remainingUserHits;
    }

    public String getResetTime() {

        return resetTime;
    }

    public void setResetTime(String resetTime) {

        this.resetTime = resetTime;
    }

    public int getResetTimeInSeconds() {

        return resetTimeInSeconds;
    }

    public void setResetTimeInSeconds(int resetTimeInSeconds) {

        this.resetTimeInSeconds = resetTimeInSeconds;
    }
}
