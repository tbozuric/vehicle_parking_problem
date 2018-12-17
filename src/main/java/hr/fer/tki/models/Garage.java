package hr.fer.tki.models;

import java.util.List;

public class Garage {

    private int numberOfParkingLanes;
    private int numberOfVehicles;
    private List<ParkingLane> parkingLanes;
    private List<Vehicle> vehicles;

    public Garage(int numberOfParkingLanes, int numberOfVehicles, List<ParkingLane> parkingLanes, List<Vehicle> vehicles) {
        this.numberOfParkingLanes = numberOfParkingLanes;
        this.numberOfVehicles = numberOfVehicles;
        this.parkingLanes = parkingLanes;
        this.vehicles = vehicles;
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

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
