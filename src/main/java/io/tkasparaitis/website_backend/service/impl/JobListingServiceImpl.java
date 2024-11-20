package io.tkasparaitis.website_backend.service.impl;

import io.tkasparaitis.website_backend.dto.JobListingDTO;
import io.tkasparaitis.website_backend.entity.JobListingEntity;
import io.tkasparaitis.website_backend.repository.JobListingRepository;
import io.tkasparaitis.website_backend.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobListingServiceImpl implements JobListingService {

    @Autowired
    private JobListingRepository jobListingRepository;

    /**
     * Retrieves a paginated list of job listings and converts them to DTOs.
     *
     * @param pageable Pagination information
     * @return Paginated job listings as DTOs
     */
    @Override
    public Page<JobListingDTO> getJobListings(Pageable pageable) {
        return jobListingRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Converts a JobListing entity to a JobListingDTO.
     *
     * @param jobListing The JobListing entity
     * @return The corresponding JobListingDTO
     */
    private JobListingDTO convertToDTO(JobListingEntity jobListing) {
        return new JobListingDTO(
            jobListing.getListingId(),
            jobListing.getLink(),
            jobListing.getTitle(),
            jobListing.getCompany(),
            jobListing.getTimeWhenScraped(),
            jobListing.getSource(),
            jobListing.getLocation(),
            jobListing.getCompensation()
        );
    }
}
