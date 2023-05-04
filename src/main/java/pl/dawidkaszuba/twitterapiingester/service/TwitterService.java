package pl.dawidkaszuba.twitterapiingester.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.dawidkaszuba.twitterapiingester.model.Tweet;
import pl.dawidkaszuba.twitterapiingester.model.TweeterUser;
import pl.dawidkaszuba.twitterapiingester.model.TwitterResponse;
import pl.dawidkaszuba.twitterapiingester.repository.TweetRepository;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TwitterService {

    private final RestTemplate restTemplate;
    private final TweetRepository tweetRepository;

    @Value("${token}")
    private String token;
    private String nextToken = "";
    private boolean isNullNextToken;


    private static final String TWITTER_BASIC_URL = "https://api.twitter.com/2/tweets/search/recent";


    public TwitterService(RestTemplate restTemplate, TweetRepository tweetRepository) {
        this.restTemplate = restTemplate;
        this.tweetRepository = tweetRepository;
    }

    public void getTweets() throws URISyntaxException {

        String searchString = "(#BTC OR #btc OR #bitcoin OR BITCOIN OR btc OR bitcoin) lang:en";

        URIBuilder uriBuilder = new URIBuilder(TWITTER_BASIC_URL);
        ArrayList<NameValuePair> queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));
        queryParameters.add(new BasicNameValuePair("max_results", "10"));
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at,author_id,public_metrics,lang"));
        queryParameters.add(new BasicNameValuePair("user.fields", "public_metrics,id,name,username"));
        queryParameters.add(new BasicNameValuePair("expansions", "author_id,referenced_tweets.id,attachments.media_keys"));


        uriBuilder.addParameters(queryParameters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("content-type", "application/json; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        int i = 0;
        int numberOfRequests = 3;

        while (i < numberOfRequests) {

            URI uri;

            if (isNullNextToken) {
                try {
                    log.info("I'm waiting one minute...");
                    Thread.sleep(60000);
                    isNullNextToken = false;
                } catch (InterruptedException e) {
                    log.error("Thread exception1");
                }
            }
            long second = System.currentTimeMillis() / 1000;

                log.info("Current second: {}", second);
                if (i != 0 && !isNullNextToken) {
                    queryParameters.add(new BasicNameValuePair("next_token", nextToken));
                } else {
                    queryParameters.removeIf(param -> param.getName().equals("next_token"));
                }
                uri = uriBuilder.build();
                i++;
                log.info("Loop number: {}", i);
                try {
                    ResponseEntity<TwitterResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, TwitterResponse.class);
                    processResponse(response);
                } catch (HttpClientErrorException errorException) {
                    log.info("HttpClientErrorException. I'm waiting 2 minutes");
                    errorException.printStackTrace();
                    try {
                        Thread.sleep(1000*60*2);
                    } catch (InterruptedException e) {
                        log.error("Thread exception2");
                    }
                }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("Thread exception3");
            }
        }
        if (i == numberOfRequests) {
            log.info("Finished...");
        }
    }

    private void processResponse(ResponseEntity<TwitterResponse> response) {
        if (response.getBody() != null && response.getBody().getMeta() != null) {
            if (response.getBody().getMeta().getNext_token() == null) {
                isNullNextToken = true;
            } else {
                nextToken = response.getBody().getMeta().getNext_token();
            }
        }
        if (response.getBody().getData() != null) {

            List<Tweet> twitterResponse = response.getBody().getData();

            if (response.getBody().getIncludes() != null && response.getBody().getIncludes().getUsers() != null
                    && !response.getBody().getIncludes().getUsers().isEmpty()) {
                List<TweeterUser> tweeterUsers = response.getBody().getIncludes().getUsers();
                twitterResponse
                        .forEach(tweet -> {
                            Optional<TweeterUser> tweeterUserOptional = tweeterUsers.stream()
                                    .filter(user -> String.valueOf(user.getTweeterUserId()).equals(tweet.getAuthor_id()))
                                    .findFirst();
                            tweeterUserOptional.ifPresent(tweet::setTweeterUser);
                            tweetRepository.save(tweet);
                        });
            } else {
                twitterResponse
                        .forEach(tweetRepository::save);
            }
        }
    }
}