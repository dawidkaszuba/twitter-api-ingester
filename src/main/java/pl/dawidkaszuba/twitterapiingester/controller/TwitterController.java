package pl.dawidkaszuba.twitterapiingester.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dawidkaszuba.twitterapiingester.service.TwitterService;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class TwitterController {

    private final TwitterService twitterService;

    public TwitterController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @GetMapping("/get-twitts")
    public void getTwitts() throws URISyntaxException {
        twitterService.getTweets();
    }
}
