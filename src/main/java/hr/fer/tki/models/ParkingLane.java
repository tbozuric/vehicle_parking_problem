package hr.fer.tki.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingLane {
    private static int counter = 0;
    static final double DISTANCE_BETWEEN_VEHICLES = 0.5;

    private int id;
    private int lengthOfLane;

    /**
     * Parking lanes that block this one.
     */
    private List<ParkingLane> blockingParkingLanes;

    public ParkingLane(int lengthOfLane) {
        this.id = counter++;
        this.lengthOfLane = lengthOfLane;
        this.blockingParkingLanes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public double getLengthOfLane() {
        return (double) lengthOfLane;
    }

    public List<ParkingLane> getBlockingParkingLanes() {
        return blockingParkingLanes;
    }

    public void addBlockingParkingLane(ParkingLane blockingParkingLane) {
        blockingParkingLanes.add(blockingParkingLane);
    }

    public int getNumberOfBlockingLanes() {
        return blockingParkingLanes.size();
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