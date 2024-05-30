package org.example.tspsaservice.services;

import lombok.AllArgsConstructor;
import org.example.tspsaservice.DTO.CityDTO;
import org.example.tspsaservice.models.City;
import org.example.tspsaservice.repository.CityRepository;
import org.example.tspsaservice.utils.CityCodeGenerator;
import org.example.tspsaservice.utils.CityDistanceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityCodeGenerator codeGenerator;

    @Autowired
    private CityDistanceGenerator distanceGenerator;



    public List<City> insertCities() {
        String[] places = distanceGenerator.getPlaces();
        double[][] distances = distanceGenerator.getDistance();

        List<City> cities = new ArrayList<>();
        for(int i = 0; i < places.length; i++){
            Map<String, Double> distanceMap = new HashMap<>();

            for(int j = 0; j < places.length; j++){
                if(i != j){
                    String destinationCity = places[j];
                    Double distance = distances[i][j];
                    distanceMap.put(destinationCity, distance);
                }
            }
            System.out.println(distanceMap);
            City city = City.builder()
                    .id(codeGenerator.generate())
                    .name(places[i])
                    .distances(distanceMap)
                    .build();
            cities.add(city);
        }
        return cityRepository.saveAll(cities);
    }

    public City createCity(CityDTO dto) {

        City city = City.builder()
                .id(codeGenerator.generate())
                .name(dto.getName())
                .distances(dto.getDistances())
                .build();
        return cityRepository.save(city);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }


}
