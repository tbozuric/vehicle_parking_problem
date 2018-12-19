package hr.fer.tki.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ParkingLane implements Comparable<ParkingLane> {
    private static int counter = 0;
    private static final double DISTANCE_BETWEEN_VEHICLES = 0.5;

    private int id;
    private int lengthOfLane;
    private int vehicleSeries;
    private double availableSpace;
    /**
     * Parking lanes that block this one.
     */
    private List<ParkingLane> blockingParkingLanes;
    private List<Vehicle> parkedVehicles;

    public ParkingLane(int lengthOfLane) {
        this.id = counter++;
        this.vehicleSeries = -1;
        this.lengthOfLane = lengthOfLane;
        this.availableSpace = lengthOfLane;
        this.blockingParkingLanes = new ArrayList<>();
        this.parkedVehicles = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public int getLengthOfLane() {
        return lengthOfLane;
    }

    public int getVehicleSeries() {
        return vehicleSeries;
    }

    public List<ParkingLane> getBlockingParkingLanes() {
        return blockingParkingLanes;
    }

    public void setLengthOfLane(int lengthOfLane) {
        this.lengthOfLane = lengthOfLane;
    }

    public void setVehicleSeries(int vehicleSeries) {
        this.vehicleSeries = vehicleSeries;
    }

    public void setBlockingParkingLanes(List<ParkingLane> blockingParkingLanes) {
        this.blockingParkingLanes = blockingParkingLanes;
    }

    public void addBlockingParkingLane(ParkingLane blockingParkingLane) {
        blockingParkingLanes.add(blockingParkingLane);
    }

    public List<Vehicle> getParkedVehicles() {
        return parkedVehicles;
    }

    public void setParkedVehicles(List<Vehicle> parkedVehicles) {
        this.parkedVehicles = parkedVehicles;
    }


    public boolean isParkingLaneFull() {
        return availableSpace <= DISTANCE_BETWEEN_VEHICLES;
    }

    public void setSeriesOfParkedVehicles(Vehicle vehicle) {
        if (vehicleSeries == -1) {
            vehicleSeries = vehicle.getSeriesOfVehicle();
        }
    }

    public boolean parkVehicle(Vehicle vehicle) {
        int lengthOfVehicle = vehicle.getLengthOfVehicle();

        for (ParkingLane parkingLane : blockingParkingLanes) {
            int parkedVehiclesOnLane = parkingLane.parkedVehicles.size();
            if (parkedVehiclesOnLane != 0) {
                Vehicle lastParkedVehicle = parkingLane.parkedVehicles.get(parkedVehiclesOnLane - 1);
                if (lastParkedVehicle.getDepartureTime() >= vehicle.getDepartureTime()) {
                    return false;
                }
            }
        }

        if (availableSpace - lengthOfVehicle < 0) {
            return false;
        }

        int numberOfParkedVehicles = parkedVehicles.size();
        if (numberOfParkedVehicles >= 1) {

            if (availableSpace - lengthOfVehicle - DISTANCE_BETWEEN_VEHICLES < 0) {
                return false;
            }

            availableSpace = availableSpace - lengthOfVehicle - DISTANCE_BETWEEN_VEHICLES;
            setSeriesOfParkedVehicles(vehicle);
            parkedVehicles.add(vehicle);
            return true;

        } else if (availableSpace - lengthOfVehicle >= 0) {
            availableSpace -= lengthOfVehicle;
            setSeriesOfParkedVehicles(vehicle);
            parkedVehicles.add(vehicle);
            return true;
        }
        return false;
    }


    private int getNumberOfBlockingLanes() {
        return blockingParkingLanes.size();
    }

    private double getAvailableSpace() {
        return availableSpace;
    }

    @Override
    public int compareTo(ParkingLane other) {
        return Comparator.comparingInt(ParkingLane::getNumberOfBlockingLanes)
                .thenComparingDouble(ParkingLane::getAvailableSpace)
                .thenComparingInt(ParkingLane::getId)
                .compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLane that = (ParkingLane) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
