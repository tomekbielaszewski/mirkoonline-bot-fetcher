package org.grizz.service.impl;

import com.crozin.wykop.sdk.Application;
import com.crozin.wykop.sdk.Command;
import com.crozin.wykop.sdk.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.grizz.model.UserActivity;
import org.grizz.service.EntryFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Grizz on 2015-08-26.
 */
@Service
public class EntryFetcherImpl implements EntryFetcher {
    @Value("${mirko.app.key}")
    private String APP_KEY;
    @Value("${mirko.secret.key}")
    private String SECRET_KEY;
//    @Value("${mirko.account.key}")
//    private String ACCOUNT_KEY;

    @Override
    public List<UserActivity> page(int page) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss") //"date":"2014-11-19 23:55:21",
                .create();
        Session session = getSession();
        String entriesJSON = session.execute(getMikroblogLatestCommand(page));
        UserActivity[] userActivities = gson.fromJson(entriesJSON, UserActivity[].class);

        return Arrays.asList(userActivities);
    }

    private Session getSession() {
        Application app = new Application(APP_KEY, SECRET_KEY);
        return app.openSession();
    }

    private Command getMikroblogLatestCommand(Integer page) {
        if (page > 99) throw new IllegalArgumentException("Page limit is 99 but you tried to get page number " + page);
        return new Command("stream", "index", new String[]{"page", page.toString()});
    }
}
