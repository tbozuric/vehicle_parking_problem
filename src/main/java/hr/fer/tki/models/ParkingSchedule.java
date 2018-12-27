package hr.fer.tki.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hr.fer.tki.models.ParkingLane.DISTANCE_BETWEEN_VEHICLES;

public class ParkingSchedule {

    private List<ParkingLane> parkingLanes;
    private List<Vehicle> vehicles;
    private Map<ParkingLane, List<Vehicle>> vehiclesAtLanes;

    public ParkingSchedule(List<ParkingLane> parkingLanes, List<Vehicle> vehicles) {
        this.parkingLanes = parkingLanes;
        this.vehicles = vehicles;
        this.vehiclesAtLanes = new HashMap<>();
    }

    public List<ParkingLane> getParkingLanes() {
        return parkingLanes;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public boolean parkVehicle(Vehicle vehicle, ParkingLane parkingLane) {
        int lengthOfVehicle = vehicle.getLengthOfVehicle();

        List<ParkingLane> blockingParkingLanes = parkingLane.getBlockingParkingLanes();
        for (ParkingLane parkingLane : blockingParkingLanes) {
            List<Vehicle> vehicles = vehiclesAtLanes.get(parkingLane);
            int parkedVehiclesOnLane = vehicles.size();
            if (parkedVehiclesOnLane != 0) {
                Vehicle lastParkedVehicle = vehicles.get(parkedVehiclesOnLane - 1);
                if (lastParkedVehicle.getDepartureTime() >= vehicle.getDepartureTime()) {
                    return false;
                }
            }
        }

        double availableSpace = parkingLane.getAvailableSpace();
        if (availableSpace - lengthOfVehicle < 0) {
            return false;
        }

        List<Vehicle> parkedVehicles = vehiclesAtLanes.get(parkingLane);
        int numberOfParkedVehicles = parkedVehicles.size();
        if (numberOfParkedVehicles >= 1) {

            if (availableSpace - lengthOfVehicle - DISTANCE_BETWEEN_VEHICLES < 0) {
                return false;
            }

            parkingLane.setAvailableSpace(availableSpace - lengthOfVehicle - DISTANCE_BETWEEN_VEHICLES);
            parkingLane.setSeriesOfParkedVehicles(vehicle);
            parkedVehicles.add(vehicle);
            return true;

        } else if (availableSpace - lengthOfVehicle >= 0) {
            parkingLane.setAvailableSpace(availableSpace - lengthOfVehicle);
            parkingLane.setSeriesOfParkedVehicles(vehicle);
            parkedVehicles.add(vehicle);
            return true;
        }
        return false;
    }
}
