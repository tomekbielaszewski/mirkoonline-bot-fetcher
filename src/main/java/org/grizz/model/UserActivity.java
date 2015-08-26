package org.grizz.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by Grizz on 2014-07-23.
 */
@Data
public class UserActivity {
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String nick;
    @SerializedName("date")
    private Date activity;

    private List<UserActivity> comments;
    private List<UserActivity> voters;
}
