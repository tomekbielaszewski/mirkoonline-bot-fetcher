package org.grizz.service.collectors;

import lombok.Value;
import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;
import pl.grizwold.microblog.model.UserGroup;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminCommentsCollector implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        List<AdminComment> adminComments = entries.stream()
                .flatMap(e -> e.getComments().stream())
                .filter(e -> UserGroup.BLACK.equals(e.getAuthorGroup()))
                .map(e -> new AdminComment(e.getEntryId(), e.getId(), e.getAuthor()))
                .collect(Collectors.toList());

        statistics.put("admin_comments", adminComments);
    }

    @Value
    static class AdminComment {
        private Long entryId;
        private Long commentId;
        private String author;
    }
}
