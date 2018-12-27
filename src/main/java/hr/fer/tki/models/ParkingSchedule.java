package hr.fer.tki.models;

import java.util.*;

import static hr.fer.tki.models.ParkingLane.DISTANCE_BETWEEN_VEHICLES;
import static hr.fer.tki.models.ParkingLane.VEHICLE_SERIES_NOT_DEFINED;

public class ParkingSchedule {

    private List<ParkingLane> parkingLanes;
    private List<Vehicle> vehicles;
    private Map<ParkingLane, List<Vehicle>> vehiclesAtLanes;

    public ParkingSchedule(List<ParkingLane> parkingLanes, List<Vehicle> vehicles) {
        this.parkingLanes = parkingLanes;
        this.vehicles = vehicles;
        this.vehiclesAtLanes = new HashMap<>();
    }

    private ParkingSchedule(List<ParkingLane> lanes, List<Vehicle> vehicles, Map<ParkingLane, List<Vehicle>> vehiclesAtLanes) {
        this.parkingLanes = lanes;
        this.vehicles = vehicles;
        this.vehiclesAtLanes = vehiclesAtLanes;
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

    public ParkingSchedule deepcopy() {
        Map<ParkingLane, List<Vehicle>> vehiclesAtLanesCopy = new HashMap<>();
        for (ParkingLane lane : vehiclesAtLanes.keySet()) {
            List<Vehicle> copiedVehicles = new ArrayList<>(vehiclesAtLanes.get(lane));
            vehiclesAtLanesCopy.put(lane, copiedVehicles);
        }
        return new ParkingSchedule(parkingLanes, vehicles, vehiclesAtLanesCopy);
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

            addVehicleToLane(vehicle, parkingLane);
            return true;

        } else if (availableSpace - lengthOfVehicle >= 0) {
            addVehicleToLane(vehicle, parkingLane);
            return true;
        }
        return false;
    }

    public void addVehicleToLane(Vehicle vehicle, ParkingLane lane) {
        List<Vehicle> vehicles = vehiclesAtLanes.get(lane);
        if (vehicles == null) {
            vehicles = new ArrayList<>();
            vehiclesAtLanes.put(lane, vehicles);
        }

        double availableSpace = lane.getAvailableSpace();
        if (vehicles.size() == 0) {
            lane.setAvailableSpace(availableSpace - vehicle.getLengthOfVehicle());
        }
        lane.setAvailableSpace(availableSpace - vehicle.getLengthOfVehicle() - DISTANCE_BETWEEN_VEHICLES);
        lane.setSeriesOfParkedVehicles(vehicle.getSeriesOfVehicle());

        int position = findPosition(vehicle, vehicles);
        vehicles.add(position, vehicle);
    }

    public void removeVehicleFromLane(Vehicle vehicle, ParkingLane lane) {
        List<Vehicle> vehicles = vehiclesAtLanes.get(lane);
        vehicles.remove(vehicle);

        double availableSpace = lane.getAvailableSpace();
        if (vehicles.size() > 0) {
            lane.setAvailableSpace(availableSpace + vehicle.getLengthOfVehicle() + DISTANCE_BETWEEN_VEHICLES);
        } else {
            lane.setAvailableSpace(lane.getLengthOfLane());
            lane.setSeriesOfParkedVehicles(VEHICLE_SERIES_NOT_DEFINED);
        }
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
