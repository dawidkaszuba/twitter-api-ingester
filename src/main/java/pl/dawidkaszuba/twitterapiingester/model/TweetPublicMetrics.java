package pl.dawidkaszuba.twitterapiingester.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TweetPublicMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long retweet_count;
    private Long reply_count;
    private Long like_count;
    private Long quote_count;
    private Long impression_count;

}
