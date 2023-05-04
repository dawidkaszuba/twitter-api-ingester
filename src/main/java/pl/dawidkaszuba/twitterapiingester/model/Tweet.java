package pl.dawidkaszuba.twitterapiingester.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;
@Data
@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tweetId;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @Column(columnDefinition = "TEXT")
    private String text;
    private String id;
    private String author_id;
    @OneToOne(cascade=CascadeType.ALL)
    private TweetPublicMetrics public_metrics;
    @OneToOne
    private TweeterUser tweeterUser;
    private String lang;
}
