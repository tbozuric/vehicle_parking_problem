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
import java.util.stream.Collectors;

public class TabooSearch extends AbstractOptimizationAlgorithm {


    public TabooSearch(Garage garage) {
        super(garage);

    }

    @Override
    public void parkVehiclesInTheGarage() {
        ParkingSchedule incumbentSolution = garage.getParkingSchedule();
        GoalFunctionEvaluator evaluator = new GoalFunctionEvaluator(garage);
        double bestSolution = evaluator.evaluateTotalProblem();

        List<ParkingSchedule> neighbourhood = createNeighbourhood(garage.getParkingSchedule(), 1);
        removeInvalidNeighbours(neighbourhood);

        for (ParkingSchedule neighbour : neighbourhood) {
            garage.setParkingSchedule(neighbour);
            GoalFunctionEvaluator neighbourEvaluator = new GoalFunctionEvaluator(garage);
            double solution = neighbourEvaluator.evaluateTotalProblem();

            if (solution < bestSolution) {
                incumbentSolution = neighbour;
            }
        }

        garage.setParkingSchedule(incumbentSolution);
    }

    private List<ParkingSchedule> createNeighbourhood(ParkingSchedule currentSchedule, int series) {
        List<ParkingSchedule> neighbourhood = new ArrayList<>();

        List<SwappingVehiclesPair> swappingVehiclesList = findSwappingPairs(currentSchedule, series);

        for (SwappingVehiclesPair pair : swappingVehiclesList) {
            ParkingSchedule parkingSchedule = currentSchedule.deepcopy();

            parkingSchedule.removeVehicleFromLane(pair.getFirstVehicle(), pair.getFirstLane());
            parkingSchedule.removeVehicleFromLane(pair.getSecondVehicle(), pair.getSecondLane());

            parkingSchedule.addVehicleToLane(pair.getFirstVehicle(), pair.getSecondLane());
            parkingSchedule.addVehicleToLane(pair.getSecondVehicle(), pair.getFirstLane());

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

        for (ParkingLane firstLane : currentSchedule.getParkingLanes()) { //tODO
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

        return swappingVehiclesList;
    }
}
