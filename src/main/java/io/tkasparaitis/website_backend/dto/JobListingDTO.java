package io.tkasparaitis.website_backend.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobListingDTO {
    private String listingId;
    private String link;
    private String title;
    private String company;
    private Timestamp timeWhenScraped;
    private String source;
    private String location;
    private String compensation;
}
