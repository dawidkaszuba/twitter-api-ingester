package pl.dawidkaszuba.twitterapiingester.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TweeterUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tweeterUserId;
    private String name;
    private String id;
    @OneToOne(cascade=CascadeType.ALL)
    private TweeterUserPublicMetrics public_metrics;
    private String username;
}
