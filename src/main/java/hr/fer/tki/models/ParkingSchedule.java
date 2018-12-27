package hr.fer.tki.models;

import java.util.*;

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

    public List<Vehicle> getVehiclesAt(ParkingLane lane) {
        return vehiclesAtLanes.getOrDefault(lane, Collections.emptyList());
    }

    public boolean parkVehicle(Vehicle vehicle, ParkingLane parkingLane) {
        int lengthOfVehicle = vehicle.getLengthOfVehicle();

        List<ParkingLane> blockingParkingLanes = parkingLane.getBlockingParkingLanes();
        for (ParkingLane blockingLane : blockingParkingLanes) {
            List<Vehicle> blockingLaneVehicles = getVehiclesAt(blockingLane);
            int parkedVehiclesOnLane = blockingLaneVehicles.size();
            if (parkedVehiclesOnLane != 0) {
                Vehicle lastParkedVehicle = blockingLaneVehicles.get(parkedVehiclesOnLane - 1);
                if (lastParkedVehicle.getDepartureTime() >= vehicle.getDepartureTime()) {
                    return false;
                }
            }
        }

        double availableSpace = parkingLane.getAvailableSpace();
        if (availableSpace - lengthOfVehicle < 0) {
            return false;
        }

        List<Vehicle> parkedVehicles = getVehiclesAt(parkingLane);
        int numberOfParkedVehicles = parkedVehicles.size();
        if (numberOfParkedVehicles >= 1) {

            if (availableSpace - lengthOfVehicle - DISTANCE_BETWEEN_VEHICLES < 0) {
                return false;
            }

            parkingLane.setAvailableSpace(availableSpace - lengthOfVehicle - DISTANCE_BETWEEN_VEHICLES);
            parkingLane.setSeriesOfParkedVehicles(vehicle);
            addVehicleToLane(vehicle, parkingLane);
            return true;

        } else if (availableSpace - lengthOfVehicle >= 0) {
            parkingLane.setAvailableSpace(availableSpace - lengthOfVehicle);
            parkingLane.setSeriesOfParkedVehicles(vehicle);
            addVehicleToLane(vehicle, parkingLane);
            return true;
        }
        return false;
    }

    private void addVehicleToLane(Vehicle vehicle, ParkingLane lane) {
        List<Vehicle> vehicles = vehiclesAtLanes.get(lane);
        if (vehicles == null) {
            vehicles = new ArrayList<>();
            vehiclesAtLanes.put(lane, vehicles);
        }
        int position = findPosition(vehicle, vehicles);
        vehicles.add(position, vehicle);
    }

    private int findPosition(Vehicle vehicle, List<Vehicle> vehicles) {
        int position = 0;
        for (Vehicle v : vehicles) {
            if (vehicle.getDepartureTime() > v.getDepartureTime()) {
                position++;
            } else {
                break;
            }
        }
        return position;
    }
}
