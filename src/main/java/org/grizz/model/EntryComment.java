package org.grizz.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class EntryComment extends Entry {
    @SerializedName("entry_id")
    private int entryId;

    @Override
    public List<EntryComment> getComments() {
        return Collections.EMPTY_LIST;
    }
}
