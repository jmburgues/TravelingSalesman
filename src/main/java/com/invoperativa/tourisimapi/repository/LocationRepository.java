package com.invoperativa.tourisimapi.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.invoperativa.tourisimapi.model.Location;

@Repository
public class LocationRepository {
    private final List<Location> locations = List.of(
            new Location(0,
                    "Mar del Plata",
                    createDistanceMap(new int[]{0, 1188, 1401, 1372, 2902, 1966, 1996, 979, 1113, 751, 1436})),
            new Location(1,
                    "Carlos Paz",
                    createDistanceMap(new int[]{1188, 0, 1535, 665, 3147, 887, 917, 1113, 406, 434, 549})),
            new Location(2,
                    "Bariloche",
                    createDistanceMap(new int[]{1401, 1535, 0, 1221, 2228, 2390, 2420, 422, 1196, 1584, 1386})),
            new Location(3,
                    "Mendoza",
                    createDistanceMap(new int[]{1372, 665, 1221, 0, 3329, 1276, 1306, 799, 259, 851, 165})),
            new Location(4,
                    "Usuahia",
                    createDistanceMap(new int[]{2902, 3147, 2228, 3329, 0, 4000, 4030, 2462, 3070, 3161, 3393})),
            new Location(5,
                    "Salta",
                    createDistanceMap(new int[]{1966, 887, 2390, 1276, 4000, 0, 115, 1968, 1152, 1215, 1111})),
            new Location(6,
                    "Jujuy",
                    createDistanceMap(new int[]{1996, 917, 2420, 1306, 4030, 115, 0, 1998, 1182, 1245, 1141})),
            new Location(7,
                    "Neuquen",
                    createDistanceMap(new int[]{979, 1113, 422, 799, 2462, 1968, 1998, 0, 774, 1162, 964})),
            new Location(8,
                    "San Luis",
                    createDistanceMap(new int[]{1113, 406, 1196, 259, 3070, 1152, 1182, 774, 0, 592, 323})),
            new Location(9,
                    "Rosario",
                    createDistanceMap(new int[]{751, 434, 1584, 851, 3161, 1215, 1245, 1162, 592, 0, 915})),
            new Location(10,
                    "San Juan",
                    createDistanceMap(new int[]{1436, 549, 1386, 165, 3393, 1111, 1141, 964, 323, 915, 0})));

    public Location getLocation(int locationId) {
        return this.locations.get(locationId);
    }

    public List<Location> getAllLocations() {
        List<Location> clonedList = new ArrayList<>();
        for (Location location : this.locations) {
            clonedList.add(
                    new Location(location.getId(),
                            location.getName(),
                            cloneDistances(location.getDistances())
                    ));
        }
        return clonedList;
    }

    // Clones the repository by value instead of by reference,
    // so different calls to the API do not modify original distances.
    private Map<Integer, Integer> cloneDistances(Map<Integer, Integer> originalMap) {
        Map<Integer, Integer> newMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : originalMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            newMap.put(key, value);
        }
        return newMap;
    }

    private Map<Integer, Integer> createDistanceMap(int[] distances) {
        Map<Integer, Integer> distanceMap = new HashMap<>();
        int pos = 0;
        for (int distance : distances) {
            distanceMap.put(pos, distance);
            pos++;
        }
        return distanceMap;
    }
}
