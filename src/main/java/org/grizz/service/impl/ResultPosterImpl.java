package org.grizz.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.grizz.service.ResultPoster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Grizz on 2015-08-26.
 */
@Slf4j
@Service
public class ResultPosterImpl implements ResultPoster {
    @Value("${keeper.url}")
    private String keeperUrl;
    private String keeperAddEntryResourceUrl = "/entries";
    private String keeperLoginResourceUrl = "/login";

    @Value("${keeper.key}")
    private String keeperKey;

    @Value("${keeper.username}")
    private String keeperUsername;

    @Value("${keeper.password}")
    private String keeperPassword;

    @Override
    public void post(int result) {
        login(keeperUsername, keeperPassword);
        addEntry(result);
    }

    private void login(String keeperUsername, String keeperPassword) {
        log.info("Logging in to Keeper as {}", keeperUsername);

        try {
            int statusCode = Request.Post(keeperUrl + keeperLoginResourceUrl)
                    .bodyForm(Form.form()
                            .add("username", keeperUsername)
                            .add("password", keeperPassword)
                            .build())
                    .execute().returnResponse()
                    .getStatusLine().getStatusCode();
            logStatusCode(statusCode);
        } catch (IOException e) {
            log.error("Keeper unreachable! Error message: {}", e.getMessage());
        }
    }

    private void addEntry(int result) {
        String entityJSON = "{\n" +
                "    \"key\": \"%s\",\n" +
                "    \"value\": \"%d\"\n" +
                "}";

        try {
            log.info("Posting result={} on Keeper", result);
            int statusCode = Request.Post(keeperUrl + keeperAddEntryResourceUrl)
                    .bodyString(String.format(entityJSON, keeperKey, result), ContentType.APPLICATION_JSON)
                    .execute().returnResponse().getStatusLine().getStatusCode();
            logStatusCode(statusCode);
        } catch (IOException e) {
            log.error("Keeper unreachable! Error message: {}", e.getMessage());
        }
    }

    private void logStatusCode(int statusCode) {
        if (statusCode == 200) {
            log.info("Request successful");
        } else if (statusCode == 403) {
            log.error("Unauthorized access! Bad username or password!");
        } else {
            log.warn("Keeper returned status code: {}", statusCode);
        }
    }


}
