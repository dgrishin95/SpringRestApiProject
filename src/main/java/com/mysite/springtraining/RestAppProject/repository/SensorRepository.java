package com.mysite.springtraining.RestAppProject.repository;

import com.mysite.springtraining.RestAppProject.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    List<Sensor> findByNameIgnoreCase(String name);
}
