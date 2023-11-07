package com.mysite.springtraining.RestAppProject.repository;

import com.mysite.springtraining.RestAppProject.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    long countByRainingIsTrue();
}
