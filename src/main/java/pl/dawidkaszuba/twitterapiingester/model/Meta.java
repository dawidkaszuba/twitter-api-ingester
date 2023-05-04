package pl.dawidkaszuba.twitterapiingester.model;

import lombok.Data;

@Data
public class Meta {

    private Long id;
    private String newest_id;
    private String oldest_id;
    private int result_count;
    private String next_token;

}
