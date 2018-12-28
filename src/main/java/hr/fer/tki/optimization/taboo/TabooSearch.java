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

    private static final int ITERATION_STOP_CONDITION = 100;
    private static final int TABOO_DURATION = 10;

    private Map<Vehicle, Integer> taboo;

    public TabooSearch(Garage garage) {
        super(garage);
        taboo = garage.getParkingSchedule().getVehicles().stream().collect(Collectors.toMap(vehicle -> vehicle, vehicle -> 0));
    }

    @Override
    public void parkVehiclesInTheGarage() {
        ParkingSchedule incumbentSchedule = garage.getParkingSchedule();
        GoalFunctionEvaluator evaluator = new GoalFunctionEvaluator(garage);
        double incumbentResult = evaluator.evaluateTotalProblem();

        int[] distinctSeries = garage.getParkingSchedule().getVehicles().stream().mapToInt(Vehicle::getSeriesOfVehicle).distinct().toArray();
        for (int iteration = 0; iteration < ITERATION_STOP_CONDITION; iteration++) {
            int series = distinctSeries[iteration % distinctSeries.length];
            List<ParkingSchedule> neighbourhood = createNeighbourhood(garage.getParkingSchedule(), series);
            removeInvalidNeighbours(neighbourhood);

            ParkingSchedule iterationBestSchedule = null;
            double iterationBestResult = Double.POSITIVE_INFINITY;
            for (ParkingSchedule neighbour : neighbourhood) {
                garage.setParkingSchedule(neighbour);
                GoalFunctionEvaluator neighbourEvaluator = new GoalFunctionEvaluator(garage);
                double result = neighbourEvaluator.evaluateTotalProblem();

                if (iterationBestSchedule == null || result < iterationBestResult) {
                    iterationBestSchedule = neighbour;
                    iterationBestResult = result;
                }
            }
            if (iterationBestSchedule != null && iterationBestResult < incumbentResult) {
                incumbentSchedule = iterationBestSchedule;
            }
        }

        garage.setParkingSchedule(incumbentSchedule);
    }

    private List<ParkingSchedule> createNeighbourhood(ParkingSchedule currentSchedule, int series) {
        List<ParkingSchedule> neighbourhood = new ArrayList<>();

        List<SwappingVehiclesPair> swappingVehiclesList = findSwappingPairs(currentSchedule, series);

        for (SwappingVehiclesPair pair : swappingVehiclesList) {
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

            neighbourhood.add(parkingSchedule);
        }

        return neighbourhood;
    }

    private void removeInvalidNeighbours(List<ParkingSchedule> neighbourhood) {
        Iterator<ParkingSchedule> parkingScheduleIterator = neighbourhood.iterator();
        while (parkingScheduleIterator.hasNext()) {
            garage.setParkingSchedule(parkingScheduleIterator.next());
            ValidatorResult validatorResult = GarageValidator.validate(garage);
            if (!validatorResult.isValid()) {
                parkingScheduleIterator.remove();
            }
        }
    }

    private List<SwappingVehiclesPair> findSwappingPairs(ParkingSchedule currentSchedule, int series) {
        List<SwappingVehiclesPair> swappingVehiclesList = new ArrayList<>();

        List<ParkingLane> relevantLanes = currentSchedule.getParkingLanes()
                                                .stream()
                                                .filter(lane -> currentSchedule.getSeriesOfParkedVeihclesAtLane(lane) == series)
                                                .collect(Collectors.toList());

        for (ParkingLane firstLane : relevantLanes) {
            for (ParkingLane secondLane : relevantLanes) {
                if (firstLane.equals(secondLane))
                    continue;

                List<Vehicle> vehiclesAtFirstLane = currentSchedule.getVehiclesAt(firstLane);
                List<Vehicle> vehiclesAtSecondLane = currentSchedule.getVehiclesAt(secondLane);

                for (Vehicle firstLaneVehicle : vehiclesAtFirstLane) {
                    for (Vehicle secondLaneVehicle : vehiclesAtSecondLane) {
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
