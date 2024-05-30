package org.example.tspsaservice.controller;

import org.example.tspsaservice.DTO.TourDTO;
import org.example.tspsaservice.services.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tsp")
public class DistanceController {

    @Autowired
    DistanceService distanceService;

    @PostMapping("/route")
    public ResponseEntity<TourDTO> findOptimalTourRoute(@RequestBody String start) {
        return new ResponseEntity<>(distanceService.findOptimalTour(start), HttpStatus.OK);
    }

}
