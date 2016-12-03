package org.grizz.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
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
