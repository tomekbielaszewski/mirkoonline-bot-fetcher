package org.grizz.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.grizz.service.TagExtractor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Entry {
    private Long id;

    private String author;

    @SerializedName("author_group")
    private int authorGroup;

    @SerializedName("author_sex")
    private String authorSex;

    private String app;

    @SerializedName("date")
    private Date dateAdded;

    private Embed embed;

    private String body;

    private String url;

    @SerializedName("vote_count")
    private int votes;

    private List<User> voters;

    private List<EntryComment> comments;

    private Set<Tag> tags;

    public Set<Tag> getTags() {
        return new TagExtractor().extract(this.getBody());
    }
}
