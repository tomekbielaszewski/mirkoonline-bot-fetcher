package org.grizz;

import org.grizz.model.Configuration;
import org.grizz.model.Statistics;
import org.grizz.model.properties.KeeperProperties;
import org.grizz.service.MirkoonlineBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import pl.grizwold.microblog.model.Entry;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(KeeperProperties.class)
public class Starter {
    private static final int HOUR = 1000 * 60 * 60;

    public static void main(String... args) {
        ConfigurableApplicationContext context = SpringApplication.run(Starter.class, args);
        MirkoonlineBot mirkoonlineBot = context.getBean(MirkoonlineBot.class);

        //TODO parse CLI Arguments here and pass those into `run` method as a configuration
        Configuration configuration = new Configuration(30, 24);

        run(mirkoonlineBot, configuration);
    }

    private static void run(MirkoonlineBot mirkoonlineBot, Configuration configuration) {
        List<Entry> entries = mirkoonlineBot.getEntries(configuration.getScanEntriesSinceGivenAmountOfHours());
        Statistics statistics = mirkoonlineBot.collectStatistics(entries, configuration);
        mirkoonlineBot.postResults(statistics);
    }
}
