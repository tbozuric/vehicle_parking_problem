package hr.fer.tki.validator;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.Vehicle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GarageValidator {

    private ValidatorResult validatorResult;
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
    }

    private void validateAllVehiclesAtExactlyOneLocation() {
        Set<Vehicle> distinctVehicles = new HashSet<>();
        int vehiclesCount = 0;
        for (ParkingLane lane : garage.getParkingLanes()) {
            distinctVehicles.addAll(lane.getParkedVehicles());
            vehiclesCount += lane.getParkedVehicles().size();
        }

        int actualVehiclesNumber = garage.getNumberOfVehicles();

        if (vehiclesCount < actualVehiclesNumber) {
            validatorResult.addViolatedRestriction("Not all vehicles stored.");
        } else if (vehiclesCount > actualVehiclesNumber) {
            validatorResult.addViolatedRestriction("Some vehicles are duplicated.");
        }

        if (distinctVehicles.size() != actualVehiclesNumber) {
            validatorResult.addViolatedRestriction("Some vehicles are duplicated.");
        }
    }

    private void validateEveryLaneContainsSameSeriesVehicles() {
        garage.getParkingLanes().forEach(lane -> {
            Object[] distinctSeries = lane.getParkedVehicles().stream().map(Vehicle::getSeriesOfVehicle).distinct().toArray();
            if (distinctSeries.length > 1) {
                validatorResult.addViolatedRestriction("Lane contains vehicles of different series.");
            }
        });
    }

    private void validateVehiclesAreInLanesWithCorrectPermission() {
        int numberOfLanes = garage.getParkingLanes().size();
        for (int laneIndex = 0; laneIndex < numberOfLanes; laneIndex++) {
            ParkingLane lane = garage.getParkingLanes().get(laneIndex);
            for (Vehicle vehicle : lane.getParkedVehicles()) {
                int vehicleIndex = garage.getVehicles().indexOf(vehicle);

                if (garage.getParkingPermissions()[vehicleIndex][laneIndex].equals(false)) {
                    validatorResult.addViolatedRestriction(String.format("Wrong vehicles parked at lane %d.", vehicleIndex));
                }
            }
        }
    }

    private void validateLanesAreNotOverloaded() {
        for (int laneIndex = 0; laneIndex < garage.getNumberOfParkingLanes(); laneIndex++) {
            ParkingLane lane = garage.getParkingLanes().get(laneIndex);

            List<Vehicle> parkedVehicles = lane.getParkedVehicles();
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
        for (int laneIndex = 0; laneIndex < garage.getNumberOfParkingLanes(); laneIndex++) {
            ParkingLane lane = garage.getParkingLanes().get(laneIndex);

            int time = -1;
            for (Vehicle vehicle : lane.getParkedVehicles()) {
                int departureTime = vehicle.getDepartureTime();

                if (time >= departureTime) {
                    validatorResult.addViolatedRestriction(String.format("Departure time constraint broken in %d lane.", laneIndex);
                }
                time = departureTime;
            }
        }
    }

    private void validateBlockingTracksAreBeforeBlockedOnes() {
        for (int laneIndex = 0; laneIndex < garage.getNumberOfParkingLanes(); laneIndex++) {
            ParkingLane lane = garage.getParkingLanes().get(laneIndex);
            List<Vehicle> vehiclesInThisLane = lane.getParkedVehicles();
            if (vehiclesInThisLane.isEmpty())
                continue;

            Vehicle firstInThis = vehiclesInThisLane.get(0);

            for (ParkingLane blocking : lane.getBlockingParkingLanes()) {
                List<Vehicle> vehiclesInBlockingLanes = blocking.getParkedVehicles();
                if (vehiclesInBlockingLanes.isEmpty()) {
                    continue;
                }

                Vehicle lastInBlocking = vehiclesInBlockingLanes.get(vehiclesInBlockingLanes.size() - 1);
                if (lastInBlocking.getDepartureTime() >= firstInThis.getDepartureTime()) {
                    validatorResult.addViolatedRestriction(
                            String.format("Lane %d is blocked, but does not breaks the time rule.", laneIndex));
                }
            }
        }
    }
}