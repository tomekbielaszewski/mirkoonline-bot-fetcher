package org.grizz.springconfig;

import lombok.extern.slf4j.Slf4j;
import org.grizz.model.Entry;
import org.grizz.model.UserActivity;
import org.grizz.service.MirkoonlineBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Configuration
@EnableScheduling
@ComponentScan("org.grizz")
public class MainConfig {
    private static final long MINUTE = 60 * 1000;
    private static final long TIME_OFFSET = 30 * MINUTE;

    @Autowired
    private MirkoonlineBot mirkoonlineBot;

    @Scheduled(fixedDelay = 5 * MINUTE)
    public void periodicallyRun() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(new MirkoonlineCounterTask());

        try {
            future.get(9, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            future.cancel(true);
            log.warn("Mirkoonline task terminated at {}! Repeating...", new Date().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
    }

    private class MirkoonlineCounterTask implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            long since = new Date().getTime() - TIME_OFFSET;
            List<Entry> entries = mirkoonlineBot.getEntries();
            List<UserActivity> filteredActivities = mirkoonlineBot.collectStatistics(entries, since); //get activities from last x minutes
            List<UserActivity> uniqueActivities = mirkoonlineBot.filterDuplicatedUsernames(filteredActivities);
            mirkoonlineBot.postResults(uniqueActivities);

            return null;
        }
    }
}
