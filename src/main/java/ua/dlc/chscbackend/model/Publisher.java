package ua.dlc.chscbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Publisher {

    private String name;

    @JsonProperty("homepage_url")
    private String homepageUrl;

    @JsonProperty("logo_url")
    private String logoUrl;

    @JsonProperty("favicon_url")
    private String faviconUrl;

}
