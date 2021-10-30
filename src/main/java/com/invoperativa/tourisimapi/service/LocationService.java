package com.invoperativa.tourisimapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.invoperativa.tourisimapi.model.Location;
import com.invoperativa.tourisimapi.repository.LocationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LocationService {
    private LocationRepository locationRepository;

    /*
    * Retrieves locations from database and adjusts its distances according to the number of locations wanted.
    * Generates a Map for each location where KEY is locationId and VALUE is a map of distances to other locations.
     */

    public Set<Integer> getSupportedIds() {
        Set<Integer> supportedIds = new HashSet<>();
        for(Location location : this.locationRepository.getAllLocations()) {
            supportedIds.add(location.getId());
        }
        return supportedIds;
    }

    public Location getLocation(Integer locationId) {
        return locationRepository.getLocation(locationId);
    }

    public Map<Integer, Location> getLocations(Set<Integer> wantedIds) {
        Map<Integer, Location> wantedLocations = new HashMap<>();
        List<Location> originalLocations = locationRepository.getAllLocations();
        for(Integer locationId : wantedIds) {
            Location oneLocation;
            oneLocation = originalLocations.get(locationId);
            oneLocation.setDistances(adecuateDistances(oneLocation.getDistances(), wantedIds));
            wantedLocations.put(locationId, oneLocation);
        }
        return wantedLocations;
    }

    private Map<Integer,Integer> adecuateDistances(Map<Integer, Integer> distances, Set<Integer> wantedIds){
        Map<Integer,Integer> newDistances=new HashMap<>();
        for (Integer location : wantedIds){
            newDistances.put(location,distances.get(location));
        }
        return  newDistances;
    }
}
