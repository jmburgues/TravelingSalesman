package com.invoperativa.tourisimapi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.invoperativa.tourisimapi.model.EconomicPathResponse;
import com.invoperativa.tourisimapi.service.LocationService;
import com.invoperativa.tourisimapi.service.RoutingService;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@RestController
@RequestMapping("/bestRoute")
@CrossOrigin(origins = "*" , methods = {RequestMethod.POST})
@AllArgsConstructor
public class RouteController {
    private RoutingService routingService;
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<EconomicPathResponse> getEconomicPath(@RequestBody List<Integer> idsList){
        Set<Integer> locationsIds = new HashSet<>(idsList);
        if(locationsIds.stream().anyMatch(requestedLocation -> !locationService.getSupportedIds().contains(requestedLocation))){
            throw new IllegalArgumentException("There is a location id not supported.");
        }
        return ResponseEntity.ok(routingService.getBestPath(locationsIds));
    }
}
