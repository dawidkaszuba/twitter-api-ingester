package pl.dawidkaszuba.twitterapiingester.model;

import lombok.Data;

import java.util.List;

@Data
public class TwitterResponse {

    private List<Tweet> data;
    private Includes includes;
    private Meta meta;
}
