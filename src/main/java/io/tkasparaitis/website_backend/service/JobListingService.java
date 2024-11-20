package io.tkasparaitis.website_backend.service;

import io.tkasparaitis.website_backend.dto.JobListingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobListingService {
    /**
     * Retrieves a paginated list of job listings.
     *
     * @param pageable Pagination information
     * @return Paginated job listings
     */
    Page<JobListingDTO> getJobListings(Pageable pageable);

    // Optional: Additional methods for filtering can be added here
}
