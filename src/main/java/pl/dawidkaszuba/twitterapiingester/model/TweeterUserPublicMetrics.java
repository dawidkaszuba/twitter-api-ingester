package pl.dawidkaszuba.twitterapiingester.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TweeterUserPublicMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long followers_count;
    private Long following_count;
    private Long tweet_count;
    private Long listed_count;
}
