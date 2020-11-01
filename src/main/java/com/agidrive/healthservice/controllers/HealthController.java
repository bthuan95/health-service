package com.agidrive.healthservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.agidrive.healthservice.services.HealthService;

/**
 * Controller for the health service
 */
@RestController
@RequestMapping(value = HealthController.HEALTH_MAPPING)
public class HealthController {
    public static final String HEALTH_MAPPING = "/health/api/v1";

    @Autowired
    private HealthService healthService;

    /**
     * Check status of Mongo DB
     */
    @GetMapping(value = "/mongodb")
    public void checkMongoDbStatus() {
        try {
            healthService.checkMongoDbStatus();
        } catch (Exception e) {
            throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "Mongo service is not available");
        }
    }

    /**
     * Check status of Redis
     */
    @GetMapping(value = "/redis")
    public void checkRedisStatus() {
        try {
            healthService.checkRedisStatus();
        } catch (Exception e) {
            throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "Redis service is not available");
        }
    }

    /**
     * Check status of Rabbit MQ
     */
    @GetMapping(value = "/rabbitmq")
    public void checkRabbitMqStatus() {
        try {
            healthService.checkRabbitMqStatus();
        } catch (Exception e) {
            throw new ResponseStatusException( HttpStatus.INTERNAL_SERVER_ERROR, "RabbitMQ service is not available");
        }
    }
}
