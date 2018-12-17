package hr.fer.tki.models;

import java.util.List;

public class VehicleStorage {

    private int numberOfParkingLanes;
    private int numberOfVehicles;
    private List<ParkingLane>  parkingLanes;

    public VehicleStorage(int numberOfParkingLanes, int numberOfVehicles, List<ParkingLane> parkingLanes) {
        this.numberOfParkingLanes = numberOfParkingLanes;
        this.numberOfVehicles = numberOfVehicles;
        this.parkingLanes = parkingLanes;
    }

    public int getNumberOfParkingLanes() {
        return numberOfParkingLanes;
    }

    public void setNumberOfParkingLanes(int numberOfParkingLanes) {
        this.numberOfParkingLanes = numberOfParkingLanes;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public List<ParkingLane> getParkingLanes() {
        return parkingLanes;
    }

    public void setParkingLanes(List<ParkingLane> parkingLanes) {
        this.parkingLanes = parkingLanes;
    }
}
