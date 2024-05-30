package org.example.tspsaservice.controller;

import org.example.tspsaservice.DTO.CityDTO;
import org.example.tspsaservice.models.City;
import org.example.tspsaservice.services.CityService;
import org.example.tspsaservice.services.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;


    @PostMapping("/create")
    public ResponseEntity<City> createCity(@RequestBody CityDTO dto) {
        return new ResponseEntity<>(cityService.createCity(dto), HttpStatus.CREATED);
    }

    @PostMapping("/insert")
    public ResponseEntity<List<City>> insertCities() {
        return new ResponseEntity<>(cityService.insertCities(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<City>> getAll() {
        return new ResponseEntity<>(cityService.getAllCities(), HttpStatus.OK);
    }

}
