package org.grizz.service;

import lombok.extern.slf4j.Slf4j;
import org.grizz.keeper.client.KeeperClientFactory;
import org.grizz.keeper.client.model.KeeperEntry;
import org.grizz.model.Statistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResultPoster {
    @Value("${keeper.url}")
    private String keeperUrl;

    @Value("${keeper.key}")
    private String keeperKey;

    @Value("${keeper.username}")
    private String keeperUsername;

    @Value("${keeper.password}")
    private String keeperPassword;

    public void post(Statistics statistics) {
        KeeperEntry<Statistics> entry = KeeperEntry.<Statistics>builder()
                .key(keeperKey)
                .value(statistics)
                .build();

        KeeperClientFactory.create(keeperUrl)
                .login(keeperUsername, keeperPassword)
                .entries()
                .add(entry);
    }
}
