package org.grizz;

import org.grizz.config.Configuration;
import org.grizz.config.ConfigurationParser;
import org.grizz.model.Statistics;
import org.grizz.model.properties.KeeperProperties;
import org.grizz.service.MirkoonlineBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import pl.grizwold.microblog.model.Entry;

import java.util.stream.Stream;

@SpringBootApplication
@EnableConfigurationProperties(KeeperProperties.class)
public class Starter {

    public static void main(String... args) {
        Configuration configuration = new ConfigurationParser().parse(args);

        ConfigurableApplicationContext context = SpringApplication.run(Starter.class, args);
        MirkoonlineBot mirkoonlineBot = context.getBean(MirkoonlineBot.class);

        run(mirkoonlineBot, configuration);
    }

    private static void run(MirkoonlineBot mirkoonlineBot, Configuration configuration) {
        Stream<Entry> entries = mirkoonlineBot.getEntries(configuration.getHoursOfHistory());
        Statistics statistics = mirkoonlineBot.collectStatistics(entries, configuration);
        mirkoonlineBot.postResults(statistics);
    }
}
