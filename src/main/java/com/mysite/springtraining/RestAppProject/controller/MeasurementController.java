package com.mysite.springtraining.RestAppProject.controller;

import com.mysite.springtraining.RestAppProject.dto.MeasurementDTO;
import com.mysite.springtraining.RestAppProject.model.Measurement;
import com.mysite.springtraining.RestAppProject.model.Sensor;
import com.mysite.springtraining.RestAppProject.service.MeasurementService;
import com.mysite.springtraining.RestAppProject.service.SensorService;
import com.mysite.springtraining.RestAppProject.util.NotCreatedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;

    @Autowired
    public MeasurementController(MeasurementService measurementService, SensorService sensorService) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        Measurement newMeasurement = measurementService.convertToMeasurement(measurementDTO);
        Optional<Sensor> sensor = sensorService.show(newMeasurement.getSensor().getName());

        if (bindingResult.hasErrors() || sensor.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();

            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    errorMsg.append(error.getField())
                            .append(" - ").append(error.getDefaultMessage())
                            .append("; ");
                }
            }

            if (sensor.isEmpty()) {
                errorMsg.append("The name of sensor is not found");
            }

            throw new NotCreatedException(errorMsg.toString());
        }

        measurementService.save(newMeasurement, sensor.get());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurements() {
        return measurementService.findAll().stream().map(measurementService::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public long getRainyDaysCount() {
        return measurementService.getRainyDaysCount();
    }
}
