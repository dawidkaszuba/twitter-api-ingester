package pl.dawidkaszuba.twitterapiingester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawidkaszuba.twitterapiingester.model.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
