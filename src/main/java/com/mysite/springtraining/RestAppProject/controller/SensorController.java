package com.mysite.springtraining.RestAppProject.controller;

import com.mysite.springtraining.RestAppProject.dto.SensorDTO;
import com.mysite.springtraining.RestAppProject.model.Sensor;
import com.mysite.springtraining.RestAppProject.service.SensorService;
import com.mysite.springtraining.RestAppProject.util.NotCreatedException;
import com.mysite.springtraining.RestAppProject.util.SensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid SensorDTO sensorDTO,
                                               BindingResult bindingResult) {
        Sensor newSensor = sensorService.convertToSensor(sensorDTO);

        sensorValidator.validate(newSensor, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append("; ");
            }

            throw new NotCreatedException(errorMsg.toString());
        }

        sensorService.save(newSensor);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<SensorDTO> getSensors() {
        return sensorService.findAll().stream().map(sensorService::convertToSensorDTO)
                .collect(Collectors.toList());
    }
}
