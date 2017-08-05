package org.grizz.service;

import lombok.extern.slf4j.Slf4j;
import org.grizz.keeper.client.KeeperClientFactory;
import org.grizz.keeper.client.model.KeeperEntry;
import org.grizz.keeper.client.resources.EntriesResourceProvider;
import org.grizz.model.Statistics;
import org.grizz.model.Value;
import org.grizz.model.properties.KeeperProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResultPoster {
    private final KeeperProperties keeper;

    @Autowired
    public ResultPoster(KeeperProperties keeper) {
        this.keeper = keeper;
    }

    public void post(Statistics statistics) {
        EntriesResourceProvider entriesResource = KeeperClientFactory.create(keeper.getUrl())
                .login(keeper.getUsername(), keeper.getPassword())
                .entries();

        statistics.getStats().entrySet().stream()
                .map(stat -> KeeperEntry.builder()
                        .key(stat.getKey())
                        .value(Value.of(stat.getKey(), stat.getValue()))
                        .build())
                .forEach(entry -> {
                    log.info("Posting entry under key: {}", entry.getKey());
                    entriesResource.add(entry);
                });

        log.info("All stats posted to keeper");
    }
}
