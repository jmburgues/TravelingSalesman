package com.invoperativa.tourisimapi.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@CrossOrigin(origins = "*" , methods = {RequestMethod.GET})
@AllArgsConstructor
public class RouteController {
    private RoutingService routingService;
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<EconomicPathResponse> getEconomicPath(@RequestBody Set<Integer> locationsIds){
        if(locationsIds.stream().anyMatch(requestedLocation -> !locationService.getSupportedIds().contains(requestedLocation))){
            throw new IllegalArgumentException("There is a location id not supported.");
        }
        return ResponseEntity.ok(routingService.getBestPath(locationsIds));
    }
}
