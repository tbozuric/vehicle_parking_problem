package hr.fer.tki.validator;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.ParkingSchedule;
import hr.fer.tki.models.Vehicle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GarageValidator {

    private ValidatorResult validatorResult;
    private static int NUMBER_OF_POSSIBLE_VIOLATIONS;
    private Garage garage;

    private GarageValidator(Garage garage) {
        this.validatorResult = new ValidatorResult();
        this.garage = garage;

        validateConditions();
    }

    public static ValidatorResult validate(Garage garage) {
        GarageValidator garageValidator = new GarageValidator(garage);
        return garageValidator.validatorResult;
    }

    private void validateConditions() {
        validateAllVehiclesAtExactlyOneLocation();
        validateEveryLaneContainsSameSeriesVehicles();
        validateVehiclesAreInLanesWithCorrectPermission();
        validateLanesAreNotOverloaded();
        validateVehiclesTimesAreSortedCorrectly();
        validateBlockingTracksAreBeforeBlockedOnes();
        NUMBER_OF_POSSIBLE_VIOLATIONS += 6;
    }

    private void validateAllVehiclesAtExactlyOneLocation() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();

        Set<Vehicle> distinctVehicles = new HashSet<>();
        int vehiclesCount = 0;
        for (ParkingLane lane : parkingSchedule.getParkingLanes()) {
            List<Vehicle> parkedVehicles = parkingSchedule.getVehiclesAt(lane);
            if (parkedVehicles == null)
                continue;

            distinctVehicles.addAll(parkedVehicles);
            vehiclesCount += parkedVehicles.size();
        }

        int actualVehiclesNumber = garage.getNumberOfVehicles();

        if (vehiclesCount < actualVehiclesNumber) {
            validatorResult.addViolatedRestriction("Not all vehicles stored.");
        } else if (vehiclesCount > actualVehiclesNumber) {
            validatorResult.addViolatedRestriction("Some vehicles are duplicated.");
        }

        if (distinctVehicles.size() != vehiclesCount) {
            validatorResult.addViolatedRestriction("Some vehicles are duplicated.");
        }
    }

    private void validateEveryLaneContainsSameSeriesVehicles() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        parkingSchedule.getParkingLanes().forEach(lane -> {
            Object[] distinctSeries = parkingSchedule.getVehiclesAt(lane).stream().map(Vehicle::getSeriesOfVehicle).distinct().toArray();
            if (distinctSeries.length > 1) {
                validatorResult.addViolatedRestriction("Lane contains vehicles of different series.");
            }
        });
    }

    private void validateVehiclesAreInLanesWithCorrectPermission() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        int numberOfLanes = parkingSchedule.getParkingLanes().size();
        for (int laneIndex = 0; laneIndex < numberOfLanes; laneIndex++) {
            ParkingLane lane = parkingSchedule.getParkingLanes().get(laneIndex);
            for (Vehicle vehicle : parkingSchedule.getVehiclesAt(lane)) {
                int vehicleIndex = parkingSchedule.getVehicles().indexOf(vehicle);

                if (garage.getParkingPermissions()[vehicleIndex][laneIndex].equals(false)) {
                    validatorResult.addViolatedRestriction(String.format("Wrong vehicles parked at lane %d.", vehicleIndex));
                }
            }
        }
    }

    private void validateLanesAreNotOverloaded() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        for (int laneIndex = 0; laneIndex < garage.getNumberOfParkingLanes(); laneIndex++) {
            ParkingLane lane = parkingSchedule.getParkingLanes().get(laneIndex);

            List<Vehicle> parkedVehicles = parkingSchedule.getVehiclesAt(lane);
            int numberOfParked = parkedVehicles.size();

            int parkedLength = parkedVehicles.stream().mapToInt(Vehicle::getLengthOfVehicle).sum();
            if (numberOfParked > 0) {
                parkedLength += (numberOfParked - 1) * 0.5;
            }

            if (parkedLength > lane.getLengthOfLane()) {
                validatorResult.addViolatedRestriction(String.format("Parking lane %d is overloaded.", laneIndex));
            }
        }
    }

    private void validateVehiclesTimesAreSortedCorrectly() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();

        for (int laneIndex = 0; laneIndex < garage.getNumberOfParkingLanes(); laneIndex++) {
            ParkingLane lane = parkingSchedule.getParkingLanes().get(laneIndex);

            int time = -1;
            for (Vehicle vehicle : parkingSchedule.getVehiclesAt(lane)) {
                int departureTime = vehicle.getDepartureTime();

                if (time >= departureTime) {
                    validatorResult.addViolatedRestriction(String.format("Departure time constraint broken in %d lane.", laneIndex));
                }
                time = departureTime;
            }
        }
    }

    private void validateBlockingTracksAreBeforeBlockedOnes() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        for (int laneIndex = 0; laneIndex < garage.getNumberOfParkingLanes(); laneIndex++) {
            ParkingLane lane = parkingSchedule.getParkingLanes().get(laneIndex);
            List<Vehicle> vehiclesInThisLane = parkingSchedule.getVehiclesAt(lane);
            if (vehiclesInThisLane.isEmpty() || lane.getBlockingParkingLanes() == null)
                continue;

            Vehicle firstInThis = vehiclesInThisLane.get(0);

            for (ParkingLane blocking : lane.getBlockingParkingLanes()) {
                List<Vehicle> vehiclesInBlockingLanes = parkingSchedule.getVehiclesAt(blocking);
                if (vehiclesInBlockingLanes.isEmpty()) {
                    continue;
                }

                Vehicle lastInBlocking = vehiclesInBlockingLanes.get(vehiclesInBlockingLanes.size() - 1);
                if (lastInBlocking.getDepartureTime() >= firstInThis.getDepartureTime()) {
                    validatorResult.addViolatedRestriction(
                            String.format("Lane %d is blocked, but does not break the time rule.", laneIndex));
                }
            }
        }
    }

    public static int getNumberOfPossibleViolations() {
        return NUMBER_OF_POSSIBLE_VIOLATIONS;
    }
}