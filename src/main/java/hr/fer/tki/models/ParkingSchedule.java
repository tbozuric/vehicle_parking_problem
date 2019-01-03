package hr.fer.tki.models;

import java.util.*;
import java.util.stream.Collectors;

import static hr.fer.tki.models.ParkingLane.DISTANCE_BETWEEN_VEHICLES;

public class ParkingSchedule {
    public static final int VEHICLE_SERIES_NOT_DEFINED = -1;

    private List<ParkingLane> parkingLanes;
    private List<Vehicle> vehicles;
    private Map<ParkingLane, List<Vehicle>> vehiclesAtLanes;
    private Map<ParkingLane, Double> availableSpaceAtLanes;
    private Map<ParkingLane, Integer> vehicleSeriesAtLanes;

    public ParkingSchedule(List<ParkingLane> parkingLanes, List<Vehicle> vehicles) {
        this.parkingLanes = parkingLanes;
        this.vehicles = vehicles;
        this.vehiclesAtLanes = new HashMap<>();
        this.availableSpaceAtLanes = parkingLanes.stream().collect(Collectors.toMap(lane -> lane, ParkingLane::getLengthOfLane));
        this.vehicleSeriesAtLanes = parkingLanes.stream().collect(Collectors.toMap(lane -> lane, lane -> VEHICLE_SERIES_NOT_DEFINED));
    }

    private ParkingSchedule(List<ParkingLane> parkingLanes, List<Vehicle> vehicles, Map<ParkingLane, List<Vehicle>> vehiclesAtLanes, Map<ParkingLane, Double> availableSpaceAtLanes, Map<ParkingLane, Integer> vehicleSeriesAtLanes) {
        this.parkingLanes = parkingLanes;
        this.vehicles = vehicles;
        this.vehiclesAtLanes = vehiclesAtLanes;
        this.availableSpaceAtLanes = availableSpaceAtLanes;
        this.vehicleSeriesAtLanes = vehicleSeriesAtLanes;
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
        return new ParkingSchedule(
                parkingLanes,
                vehicles,
                vehiclesAtLanesCopy,
                new HashMap<>(availableSpaceAtLanes),
                new HashMap<>(vehicleSeriesAtLanes));
    }

    public boolean parkVehicle(Vehicle vehicle, ParkingLane parkingLane) {
        int lengthOfVehicle = vehicle.getLengthOfVehicle();
        List<Vehicle> parkedVehicles = getVehiclesAt(parkingLane);

        List<ParkingLane> blockingParkingLanes = parkingLane.getBlockingParkingLanes();
        for (ParkingLane blockingLane : blockingParkingLanes) {
            List<Vehicle> blockingLaneVehicles = getVehiclesAt(blockingLane);
            int parkedVehiclesOnLane = blockingLaneVehicles.size();
            if (parkedVehiclesOnLane != 0) {
                Vehicle lastParkedVehicle = blockingLaneVehicles.get(parkedVehiclesOnLane - 1);
                if (lastParkedVehicle.getDepartureTime() >= vehicle.getDepartureTime()) {
                   // int position = findPosition(vehicle, vehicles);
                   // if (position == parkedVehicles.size() - 1) {

                        return false;
                   // }
                }
            }
        }

        double availableSpace = availableSpaceAtLanes.get(parkingLane);
        if (availableSpace - lengthOfVehicle < 0) {
            return false;
        }

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
        List<Vehicle> vehicles = vehiclesAtLanes.computeIfAbsent(lane, k -> new ArrayList<>());

        double availableSpace = availableSpaceAtLanes.get(lane);
        if (vehicles.size() == 0) {
            availableSpaceAtLanes.put(lane, availableSpace - vehicle.getLengthOfVehicle());
        } else {
            availableSpaceAtLanes.put(lane, availableSpace - vehicle.getLengthOfVehicle() - DISTANCE_BETWEEN_VEHICLES);
        }
        setSeriesOfParkedVehiclesAtLane(lane, vehicle.getSeriesOfVehicle());

        int position = findPosition(vehicle, vehicles);
        vehicles.add(position, vehicle);
    }

    public void removeVehicleFromLane(Vehicle vehicle, ParkingLane lane) {
        List<Vehicle> vehicles = vehiclesAtLanes.get(lane);
        vehicles.remove(vehicle);

        double availableSpace = availableSpaceAtLanes.get(lane);
        if (vehicles.size() > 0) {
            availableSpaceAtLanes.put(lane, availableSpace + vehicle.getLengthOfVehicle() + DISTANCE_BETWEEN_VEHICLES);
        } else {
            availableSpaceAtLanes.put(lane, lane.getLengthOfLane());
            setSeriesOfParkedVehiclesAtLane(lane, VEHICLE_SERIES_NOT_DEFINED);
        }
    }

    public double getAvailableSpaceAt(ParkingLane lane) {
        return availableSpaceAtLanes.get(lane);
    }

    public boolean isParkingLaneFull(ParkingLane lane) {
        return availableSpaceAtLanes.get(lane) <= DISTANCE_BETWEEN_VEHICLES;
    }

    public int getSeriesOfParkedVehiclesAtLane(ParkingLane lane) {
        return vehicleSeriesAtLanes.get(lane);
    }

    public void setSeriesOfParkedVehiclesAtLane(ParkingLane lane, int series) {
        vehicleSeriesAtLanes.put(lane, series);
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
