package org.example.tspsaservice.services;

import org.example.tspsaservice.DTO.TourDTO;
import org.example.tspsaservice.models.City;
import org.example.tspsaservice.repository.CityRepository;
import org.example.tspsaservice.utils.TSPAnnealingSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DistanceService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TSPAnnealingSimulator annealingSimulator;

    private String[] getCityNames(){
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(City::getName).toArray(String[]::new);
    }

    private double[][] getDistances(){
        List<City> cities = cityRepository.findAll();
        String[] places = getCityNames();
        double[][] distances = new double[places.length][places.length];
        for (int i = 0; i < places.length; i++) {
            Map<String, Double> cityDistances = cities.get(i).getDistances();
            for (int j = 0; j < places.length; j++) {
                Double dist = cityDistances.get(places[j]);
                distances[i][j] = dist != null ? dist : 0;
            }
        }
        return distances;
    }


    public TourDTO findOptimalTour(String startCity){
        String[] places = getCityNames();
        int startIndex = -1;
        for (int i = 0; i < places.length; i++) {
            if (startCity.equals(places[i])) {
                startIndex = i;
                break;
            }
        }
        return annealingSimulator.simulate(getCityNames(), getDistances(), startIndex);
    }

}
