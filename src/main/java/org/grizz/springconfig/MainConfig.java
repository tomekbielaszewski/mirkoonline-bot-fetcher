package org.grizz.springconfig;

import lombok.extern.slf4j.Slf4j;
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

/**
 * Created by Grizz on 2014-07-23.
 */
@Slf4j
@Configuration
@EnableScheduling
@ComponentScan("org.grizz")
public class MainConfig {
    private static final long MINUTE = 60 * 1000;
    private static final long TIME_OFFSET = 30 * MINUTE;

    @Autowired
    private MirkoonlineBot mirkoonlineBot;

    @Scheduled(fixedDelay = 10 * MINUTE)
    public void periodicallyRun() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(new MirkoonlineCounterTask());

        try {
            future.get(9, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            future.cancel(true);
            log.warn("Mirkoonline task terminated at {}!", new Date().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
    }

    private class MirkoonlineCounterTask implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            long since = new Date().getTime() - TIME_OFFSET;
            List<UserActivity> activities = mirkoonlineBot.getActivities();
            List<UserActivity> filteredActivities = mirkoonlineBot.getFilteredActivities(activities, since); //get activities from last x minutes
            List<UserActivity> uniqueActivities = mirkoonlineBot.filterDuplicatedUsernames(filteredActivities);
            mirkoonlineBot.postResults(uniqueActivities);

            return null;
        }
    }
}
