package io.tkasparaitis.website_backend.repository;

import io.tkasparaitis.website_backend.entity.JobListingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobListingRepository extends JpaRepository<JobListingEntity, String> {
    // Additional query methods can be defined here if needed
}
