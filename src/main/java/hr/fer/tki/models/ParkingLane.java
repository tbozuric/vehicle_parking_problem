package hr.fer.tki.models;

import java.util.ArrayList;
import java.util.List;

public class ParkingLane {
    private static int counter = 0;

    private int id;
    private int lengthOfLane;
    private int vehicleSeries;
    /**
     * Parking lanes that block this one.
     */
    private List<ParkingLane> blockingParkingLanes;
    private List<Vehicle> parkedVehicles;

    public ParkingLane(int lengthOfLane) {
        this.id = counter++;
        this.lengthOfLane = lengthOfLane;

        blockingParkingLanes = new ArrayList<>();
        parkedVehicles = new ArrayList<>();
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
        if (blockingParkingLanes == null) {
            blockingParkingLanes = new ArrayList<>();
        }
        blockingParkingLanes.add(blockingParkingLane);
    }

    public List<Vehicle> getParkedVehicles() {
        return parkedVehicles;
    }

    public void setParkedVehicles(List<Vehicle> parkedVehicles) {
        this.parkedVehicles = parkedVehicles;
    }
}
