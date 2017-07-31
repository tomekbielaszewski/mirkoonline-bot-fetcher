package org.grizz.service;

import com.crozin.wykop.sdk.Command;
import com.crozin.wykop.sdk.Session;
import com.crozin.wykop.sdk.exception.ConnectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.grizwold.microblog.model.Entry;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class EntryFetcher {
    @Autowired
    private SessionProvider sessionProvider;

    public List<Entry> getPage(int page) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss") //"date":"2014-11-19 23:55:21",
                .create();
        Entry[] userActivities = null;

        try {
            Session session = sessionProvider.getSession();
            String entriesJSON = session.execute(getMikroblogLatestCommand(page));
            userActivities = gson.fromJson(entriesJSON, Entry[].class);
        } catch (ConnectionException e) {
            log.warn("Current app-key is exhausted - switching...");
            return getPage(page);
        }

        return Arrays.asList(userActivities);
    }

    private Command getMikroblogLatestCommand(Integer page) {
        if (page > 99) throw new IllegalArgumentException("Page limit is 99 but you tried to get page number " + page);
        return new Command("stream", "index", new String[]{"page", page.toString()});
    }
}
