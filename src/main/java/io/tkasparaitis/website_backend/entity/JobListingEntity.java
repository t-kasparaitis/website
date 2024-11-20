package io.tkasparaitis.website_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "job_listings")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobListingEntity {

    @Id
    @Column(name = "listing_id")
    private String listingId;
    private String link;
    private String title;
    private String company;
    @Column(name = "time_when_scraped")
    private Timestamp timeWhenScraped;
    private String source;
    private String location;
    private String compensation;
    
}
