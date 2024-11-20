package io.tkasparaitis.website_backend.controller;

import io.tkasparaitis.website_backend.dto.JobListingDTO;
import io.tkasparaitis.website_backend.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-listings")
public class JobListingController {

    @Autowired
    private JobListingService jobListingService;

    /**
     * Retrieves a paginated list of job listings.
     *
     * @param page   Page number (1-based)
     * @param size   Number of records per page
     * @param sortBy Field to sort by
     * @return Paginated job listings
     */
    @GetMapping
    public ResponseEntity<Page<JobListingDTO>> getAllJobListings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "timeWhenScraped") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<JobListingDTO> jobListings = jobListingService.getJobListings(pageable);
        return new ResponseEntity<>(jobListings, HttpStatus.OK);
    }

    // Optional: Additional endpoints for filtering can be added here
}
