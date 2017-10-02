package org.grizz.service.collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Value;
import org.grizz.config.Configuration;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;
import pl.grizwold.microblog.model.UserGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdminCommentsCollector implements StatisticsCollector {
    private List<AdminComment> adminComments = Lists.newArrayList();
    private Map<String, Object> stats = Maps.newHashMap();

    @Override
    public void collect(Entry entry, Configuration configuration) {
        adminComments.addAll(entry.getComments().stream()
                .filter(e -> UserGroup.BLACK.equals(e.getAuthorGroup()))
                .map(e -> new AdminComment(e.getEntryId(), e.getId(), e.getAuthor()))
                .collect(Collectors.toList()));
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("admin_comments", adminComments);
        return stats;
    }

    @Value
    static class AdminComment {
        private Long entryId;
        private Long commentId;
        private String author;
    }
}
