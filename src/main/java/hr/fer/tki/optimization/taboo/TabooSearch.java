package hr.fer.tki.optimization.taboo;

import hr.fer.tki.function.GoalFunctionEvaluator;
import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.ParkingSchedule;
import hr.fer.tki.models.Vehicle;
import hr.fer.tki.optimization.AbstractOptimizationAlgorithm;
import hr.fer.tki.validator.GarageValidator;
import hr.fer.tki.validator.ValidatorResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TabooSearch extends AbstractOptimizationAlgorithm {

    private static final int ITERATION_STOP_CONDITION = 500;
    private static final int TABOO_DURATION = 10;
    private static final boolean FIND_ONLY_NEIGHBOURS_WITH_SAME_SERIES = false;

    private Map<Vehicle, Integer> taboo;

    public TabooSearch(Garage garage) {
        super(garage, "Taboo search");
        taboo = garage.getParkingSchedule().getVehicles().stream().collect(Collectors.toMap(vehicle -> vehicle, vehicle -> 0));
    }

    @Override
    public void parkVehiclesInTheGarage() {
        notifyAlgorithmStarted();
        ParkingSchedule incumbentSchedule = garage.getParkingSchedule();
        GoalFunctionEvaluator evaluator = new GoalFunctionEvaluator(garage);
        double incumbentResult = evaluator.evaluateTotalProblem();

        ParkingSchedule currentSchedule = incumbentSchedule;
        int[] distinctSeries = garage.getParkingSchedule().getVehicles().stream().mapToInt(Vehicle::getSeriesOfVehicle).distinct().toArray();
        for (int iteration = 0; iteration < ITERATION_STOP_CONDITION; iteration++) {
            decreaseTabooValues();

            int series = distinctSeries[iteration % distinctSeries.length];
            List<SwappingVehiclesPair> swappingVehiclesPairs = findSwappingPairs(incumbentSchedule, series);

            ParkingSchedule iterationBestSchedule = null;
            double iterationBestResult = Double.POSITIVE_INFINITY;
            SwappingVehiclesPair pairUsedForSwapping = null;
            for (SwappingVehiclesPair pair : swappingVehiclesPairs) {
                ParkingSchedule neighbour = createNeighbour(pair, currentSchedule);
                if (neighbourIsValid(neighbour)) {
                    continue;
                }
                garage.setParkingSchedule(neighbour);
                GoalFunctionEvaluator neighbourEvaluator = new GoalFunctionEvaluator(garage);
                double result = neighbourEvaluator.evaluateTotalProblem();

                if (iterationBestSchedule == null || result < iterationBestResult) {
                    iterationBestSchedule = neighbour;
                    iterationBestResult = result;
                    pairUsedForSwapping = pair;
                }
            }

            if (iterationBestSchedule != null) {
                currentSchedule = iterationBestSchedule;
                updateTabooList(pairUsedForSwapping);
            }

            if (iterationBestSchedule != null && iterationBestResult < incumbentResult) {
                incumbentSchedule = iterationBestSchedule;
                incumbentResult = iterationBestResult;
                garage.setParkingSchedule(incumbentSchedule);
                notifyListenersAboutNewSolution(String.format("ITERATION %d / %d", iteration, ITERATION_STOP_CONDITION));
            }
        }

        garage.setParkingSchedule(incumbentSchedule);
        notifyAlgorithmOver();
    }

    private void updateTabooList(SwappingVehiclesPair pairUsedForSwapping) {
        taboo.put(pairUsedForSwapping.getFirstVehicle(), TABOO_DURATION);
        taboo.put(pairUsedForSwapping.getSecondVehicle(), TABOO_DURATION);
    }

    private void decreaseTabooValues() {
        for (Vehicle vehicle : taboo.keySet()) {
            Integer tabooValue = taboo.get(vehicle);
            if (tabooValue > 0) {
                taboo.put(vehicle, tabooValue - 1);
            }
        }
    }

    private ParkingSchedule createNeighbour(SwappingVehiclesPair pair, ParkingSchedule currentSchedule) {
        ParkingSchedule parkingSchedule = currentSchedule.deepcopy();

        if (pair.getSecondVehicle() != null) {
            parkingSchedule.removeVehicleFromLane(pair.getFirstVehicle(), pair.getFirstLane());
            parkingSchedule.removeVehicleFromLane(pair.getSecondVehicle(), pair.getSecondLane());

            parkingSchedule.addVehicleToLane(pair.getFirstVehicle(), pair.getSecondLane());
            parkingSchedule.addVehicleToLane(pair.getSecondVehicle(), pair.getFirstLane());
        } else {
            parkingSchedule.removeVehicleFromLane(pair.getFirstVehicle(), pair.getFirstLane());
            parkingSchedule.addVehicleToLane(pair.getFirstVehicle(), pair.getSecondLane());
        }

        return parkingSchedule;
    }

    private boolean neighbourIsValid(ParkingSchedule neighbour) {
        garage.setParkingSchedule(neighbour);
        ValidatorResult validatorResult = GarageValidator.validate(garage);
        return validatorResult.isValid();
    }

    private List<SwappingVehiclesPair> findSwappingPairs(ParkingSchedule currentSchedule, int series) {
        List<SwappingVehiclesPair> swappingVehiclesList = new ArrayList<>();

        List<ParkingLane> relevantLanes;
        if (FIND_ONLY_NEIGHBOURS_WITH_SAME_SERIES) {
            relevantLanes = currentSchedule.getParkingLanes()
                    .stream()
                    .filter(lane -> currentSchedule.getSeriesOfParkedVeihclesAtLane(lane) == series)
                    .collect(Collectors.toList());
        } else {
            relevantLanes = currentSchedule.getParkingLanes();
        }

        for (ParkingLane firstLane : relevantLanes) {
            for (ParkingLane secondLane : relevantLanes) {
                if (firstLane.equals(secondLane))
                    continue;

                List<Vehicle> vehiclesAtFirstLane = currentSchedule.getVehiclesAt(firstLane);
                List<Vehicle> vehiclesAtSecondLane = currentSchedule.getVehiclesAt(secondLane);

                for (Vehicle firstLaneVehicle : vehiclesAtFirstLane) {
                    for (Vehicle secondLaneVehicle : vehiclesAtSecondLane) {
                        if (taboo.get(firstLaneVehicle) > 0 || taboo.get(secondLaneVehicle) > 0) {
                            continue;
                        }

                        swappingVehiclesList.add(new SwappingVehiclesPair(firstLane, firstLaneVehicle, secondLane, secondLaneVehicle));
                    }
                }
            }
        }

        List<ParkingLane> emptyLanes = currentSchedule.getParkingLanes()
                                                .stream()
                                                .filter(lane -> currentSchedule.getVehiclesAt(lane).size() == 0)
                                                .collect(Collectors.toList());

        for (ParkingLane firstLane : relevantLanes) {
            for (ParkingLane emptyLane : emptyLanes) {
                List<Vehicle> vehiclesAtFirstLane = currentSchedule.getVehiclesAt(firstLane);

                for (Vehicle firstLaneVehicle : vehiclesAtFirstLane) {
                    swappingVehiclesList.add(new SwappingVehiclesPair(firstLane, firstLaneVehicle, emptyLane, null));
                }
            }
        }

        return swappingVehiclesList;
    }
}
