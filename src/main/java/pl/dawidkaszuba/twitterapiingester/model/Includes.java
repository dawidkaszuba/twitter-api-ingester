package pl.dawidkaszuba.twitterapiingester.model;

import lombok.Data;

import java.util.List;

@Data
public class Includes {
    private List<TweeterUser> users;
}
