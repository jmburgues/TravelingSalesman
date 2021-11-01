package com.invoperativa.tourisimapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.invoperativa.tourisimapi.model.EconomicPathResponse;
import com.invoperativa.tourisimapi.model.Location;
import com.invoperativa.tourisimapi.model.Slot;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoutingService {
    public static final int DEPARTURE_LOCATION_ID = 0;
    public static final int INFINITE_VALUE = 999999;

    private LocationService locationService;
    private Map<Integer, List<Slot>> matrix;
    private List<Slot> chosenPivots;

    public EconomicPathResponse getBestPath(Set<Integer> locationsIds) {
        locationsIds.add(DEPARTURE_LOCATION_ID);
        generateMatrix(locationService.getLocations(locationsIds));
        List<Slot> bestRoute = reorderRoute(algorithmCicles());
        return generateResponse(bestRoute);
    }

    private void generateMatrix(Map<Integer, Location> locations) {
        /*
         * 1. Gets the distances map of every location selected,
         * 2. Generates  List of slots. Each slot contains the following data:
         *        COLUMN is the location id.
         *        ROW is the destiny location id.
         *        NAME is the origin locations name.
         *        DISTANCE & ORIGINAL_DISTANCE are the distances to destiny locations, if it happens to be the same location it
         *        sets a big distance number in order to block the slot (for further calculus).
         *        PIVOT it initializes the pivot as -1 (disabled)
         * 3. Generates matrix (Map) where
         *        KEY is the locationId.
         *        VALUE is the list of slots.
         */
        List<Slot> columnMatrix = new ArrayList<>();
        this.chosenPivots = new ArrayList<>();
        for (Map.Entry<Integer, Location> location : locations.entrySet()) {
            List<Slot> finalColumnMatriz = columnMatrix;
            location.getValue().getDistances().forEach((destinyLocationId, distanceToLocation) -> {
                Slot oneSlot = new Slot();
                oneSlot.setColumn(location.getKey());
                oneSlot.setRow(destinyLocationId);
                oneSlot.setOrigin(locationService.getLocation(destinyLocationId).getName());
                oneSlot.setDestination(location.getValue().getName());
                if (distanceToLocation > 0) {
                    oneSlot.setDistance(distanceToLocation);
                    oneSlot.setOriginalDistance(distanceToLocation);
                } else {
                    oneSlot.setDistance(INFINITE_VALUE);
                    oneSlot.setOriginalDistance(0);
                }
                oneSlot.setPivot(-1);
                finalColumnMatriz.add(oneSlot);
            });
            this.matrix.put(location.getKey(), columnMatrix);
            columnMatrix = new ArrayList<>();
        }
    }

    private Integer getMinimumDistance(List<Slot> slotList) {
        int minDistance = INFINITE_VALUE;
        for (Slot slot : slotList) {
            if (slot.getDistance() != 0 && slot.getDistance() != INFINITE_VALUE && slot.getDistance() < minDistance) {
                minDistance = slot.getDistance();
            }
        }
        return minDistance;
    }

    private Integer getMinimumDistance(Slot zeroSlot, List<Slot> slotList) {
        int minDistance = INFINITE_VALUE;
        for (Slot slot : slotList) {
            if (slot != zeroSlot && slot.getDistance() < minDistance) {
                minDistance = slot.getDistance();
            }
        }
        return minDistance;
    }

    // Gets the minimum distance SlotList and subtracts that distance to every slot.
    private void reduceSlotList(List<Slot> slotList) {
        Integer minDistance = getMinimumDistance(slotList);
        if (minDistance != 0 && minDistance != INFINITE_VALUE) {
            slotList.forEach(slot -> {
                if (slot.getDistance() != INFINITE_VALUE) {
                    slot.setDistance(slot.getDistance() - minDistance);
                }
            });
        }
    }

    private boolean areZerosInList(List<Slot> slotList) {
        for (Slot slot : slotList) {
            if (slot.getDistance() == 0) {
                return true;
            }
        }
        return false;
    }

    private void reduceDistances() {
        // Reduces to zero by row
        List<Slot> firstSlotList = matrix.entrySet().iterator().next().getValue();
        for (Slot slot : firstSlotList) {
            List<Slot> rowSlotList = createSlotListByRow(slot.getRow());
            if (!areZerosInList(rowSlotList)) {
                reduceSlotList(rowSlotList);
            }
        }

        // Reduces to zero by column
        matrix.forEach((column, slotList) -> {
            if (!areZerosInList(slotList)) {
                reduceSlotList(slotList);
            }
        });
    }

    private List<Slot> createSlotListByRow(int row) {
        List<Slot> rowSlotList = new ArrayList<>();
        matrix.forEach((integer, slotList) -> slotList.forEach(slot -> {
            if (slot.getRow() == row) {
                rowSlotList.add(slot);
            }
        }));
        return rowSlotList;
    }

    // For each matrix slot, verifies if its a zero and sets respective pivot number.
    // Pivot number it's the sum of columns minimum distance and rows minimum distance.
    // TODO: VERIFY HOW IS THE PIVOT NUMBER CALCULATED
    private void setPivotsValue() {
        this.matrix.forEach((column, slotList) -> slotList.forEach(
                slot -> {
                    if (slot.getDistance() == 0) {
                        List<Slot> columnSlots = matrix.get(slot.getColumn());
                        List<Slot> rowSlots = createSlotListByRow(slot.getRow());
                        int columnMinimumDistance = getMinimumDistance(slot, columnSlots);
                        int rowMinimumDistance = getMinimumDistance(slot, rowSlots);
                        slot.setPivot(columnMinimumDistance + rowMinimumDistance);
                    }
                }));
    }

    private Slot getBestPivot() {
        // Searches for the highest value pivot and verifies if it is the best pivot to choose.
        int maxPivot = -1;
        Slot bestPivot = new Slot();
        for (Map.Entry<Integer, List<Slot>> slotList : matrix.entrySet()) {
            for (Slot slot : slotList.getValue()) {
                if (slot.getPivot() > maxPivot && locationNotVisited(slot)) {
                    maxPivot = slot.getPivot();
                    bestPivot = slot;
                }
            }
        }
        chosenPivots.add(bestPivot);
        return bestPivot;
    }

    private boolean locationNotVisited(Slot possiblePivot) {
        //If it's not te last pivot, verifies that the possible pivot does not return to an already visited location.
        boolean isLastPivot = chosenPivots.size() == (matrix.size() - 1);
        if (!isLastPivot) {
            for (Slot pivot : chosenPivots) {
                if (possiblePivot.getColumn().equals(pivot.getPivot())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void clearPivotsValue() {
        matrix.forEach((locationId, slotList) -> slotList.forEach(slot -> slot.setPivot(-1)));
    }

    // Generates the necessary calculus for each round of the Traveling Salesman algorithm
    private Slot oneMatrixRound() {
        clearPivotsValue();
        reduceDistances();
        setPivotsValue();
        Slot maxPivotSlot = getBestPivot();
        removeRowAndColumn(maxPivotSlot);
        blockReversePath(maxPivotSlot);
        return maxPivotSlot;
    }

    private void blockReversePath(Slot maxPivotSlot) {
        List<Slot> reversePath;
        if (this.matrix.containsKey(maxPivotSlot.getRow())) {
            reversePath = this.matrix.get(maxPivotSlot.getRow());
            reversePath.forEach(slot -> {
                if (Objects.equals(slot.getRow(), maxPivotSlot.getColumn())) {
                    slot.setDistance(INFINITE_VALUE);
                    slot.setPivot(-1);
                }
            });
        }
    }

    private void removeRowAndColumn(Slot maxPivotSlot) {
        this.matrix.remove(maxPivotSlot.getColumn());
        for (Map.Entry<Integer, List<Slot>> entry : matrix.entrySet()) {
            entry.getValue().removeIf(slot -> (Objects.equals(slot.getRow(), maxPivotSlot.getRow())));
        }
    }

    private List<Slot> reorderRoute(List<Slot> bestRoute) {
        List<Slot> orderedRoute = new ArrayList<>();
        int locationID = DEPARTURE_LOCATION_ID;

        while(!bestRoute.isEmpty()) {
            Slot originSlot = getSlotByRow(bestRoute, locationID);
            locationID = originSlot.getColumn();
            orderedRoute.add(originSlot);
        }
        return orderedRoute;
    }

    private Slot getSlotByRow(List<Slot> slotList, Integer row) {
        for(Slot slot : slotList) {
            if(slot.getRow().equals(row)) {
                slotList.remove(slot);
                return slot;
            }
        }
        throw new NullPointerException("Required row does not exist in selected slotList");
    }

    private List<Slot> algorithmCicles() {
        List<Slot> route = new ArrayList<>();
        while (this.matrix.size() > 1) {
            route.add(oneMatrixRound());
        }
        matrix.forEach((column, slotList) -> route.addAll(slotList));
        return route;
    }

    private EconomicPathResponse generateResponse(List<Slot> route) {
        List<String> orderedLocations = new ArrayList<>();
        int totalDistance = 0;

        for (Slot slot : route) {
            orderedLocations.add(slot.getOrigin());
            totalDistance += slot.getOriginalDistance();
        }
        orderedLocations.add(locationService.getLocation(DEPARTURE_LOCATION_ID).getName());
        return new EconomicPathResponse(orderedLocations, totalDistance);
    }
}