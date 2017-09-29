package org.grizz.service.collectors;

import com.google.common.collect.Lists;
import lombok.Value;
import org.grizz.config.Configuration;
import org.grizz.model.Statistics;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;
import pl.grizwold.microblog.model.User;
import pl.grizwold.microblog.model.UserGroup;

import java.util.List;

@Component
public class AdminVotesCollector implements StatisticsCollector {
    @Override
    public void collect(List<Entry> entries, Statistics statistics, Configuration configuration) {
        List<AdminVote> adminVotes = Lists.newArrayList();

        entries.forEach(e -> e.getVoters().stream()
                .filter(this::isAdmin)
                .forEach(v -> adminVotes.add(new AdminVote(e.getId(), null, v.getAuthor()))));

        entries.stream()
                .flatMap(e -> e.getComments().stream())
                .forEach(c -> c.getVoters().stream()
                        .filter(this::isAdmin)
                        .forEach(v -> adminVotes.add(new AdminVote(c.getEntryId(), c.getId(), v.getAuthor()))));

        statistics.put("admin_votes", adminVotes);
    }

    private boolean isAdmin(User v) {
        return UserGroup.BLACK.equals(v.getAuthorGroup());
    }

    @Value
    static class AdminVote {
        private Long entryId;
        private Long commentId;
        private String author;
    }
}
