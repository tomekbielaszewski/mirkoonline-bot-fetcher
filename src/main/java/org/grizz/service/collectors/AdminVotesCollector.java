package org.grizz.service.collectors;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import lombok.Value;
import org.grizz.config.Configuration;
import org.springframework.stereotype.Component;
import pl.grizwold.microblog.model.Entry;
import pl.grizwold.microblog.model.User;
import pl.grizwold.microblog.model.UserGroup;

import java.util.List;
import java.util.Map;

@Component
public class AdminVotesCollector implements StatisticsCollector {
    private Map<String, Object> stats = Maps.newHashMap();
    private List<AdminVote> adminVotes = Lists.newArrayList();

    @Override
    public void collect(Entry entry, Configuration configuration) {
        entry.getVoters().stream()
                .filter(this::isAdmin)
                .forEach(v -> adminVotes.add(new AdminVote(entry.getId(), null, v.getAuthor())));

        entry.getComments().forEach(c -> c.getVoters().stream()
                .filter(this::isAdmin)
                .forEach(v -> adminVotes.add(new AdminVote(entry.getId(), c.getId(), v.getAuthor()))));
    }

    @Override
    public Map<String, Object> getStats() {
        stats.put("admin_votes", adminVotes);
        return stats;
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
