package com.mysite.springtraining.RestAppProject.service;

import com.mysite.springtraining.RestAppProject.dto.MeasurementDTO;
import com.mysite.springtraining.RestAppProject.model.Measurement;
import com.mysite.springtraining.RestAppProject.model.Sensor;
import com.mysite.springtraining.RestAppProject.repository.MeasurementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, ModelMapper modelMapper) {
        this.measurementRepository = measurementRepository;
        this.modelMapper = modelMapper;
    }

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @Transactional
    public void save(Measurement newMeasurement, Sensor sensor) {
        newMeasurement.setSensor(sensor);
        newMeasurement.setCreatedAt(LocalDateTime.now());
        measurementRepository.save(newMeasurement);
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public long getRainyDaysCount() {
        return measurementRepository.countByRainingIsTrue();
    }
}
