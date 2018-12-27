package hr.fer.tki.models;

import java.util.Arrays;
import java.util.List;

public class Garage {

    private int numberOfParkingLanes;
    private int numberOfVehicles;
    /**
     * rows => vehicles
     * columns => parkingLanes
     */
    private Boolean[][] parkingPermissions;
    private ParkingSchedule parkingSchedule;

    public Garage(int numberOfParkingLanes, int numberOfVehicles, List<ParkingLane> parkingLanes,
                  List<Vehicle> vehicles, Boolean[][] parkingPermissions) {
        this.numberOfParkingLanes = numberOfParkingLanes;
        this.numberOfVehicles = numberOfVehicles;
        this.parkingSchedule = new ParkingSchedule(parkingLanes, vehicles);
        this.parkingPermissions = parkingPermissions;
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

//    public List<ParkingLane> getParkingLanes() {
//        return parkingLanes;
//    }
//
//    public void setParkingLanes(List<ParkingLane> parkingLanes) {
//        this.parkingLanes = parkingLanes;
//    }
//
//    public List<Vehicle> getVehicles() {
//        return vehicles;
//    }
//
//    public void setVehicles(List<Vehicle> vehicles) {
//        this.vehicles = vehicles;
//    }


    public ParkingSchedule getParkingSchedule() {
        return parkingSchedule;
    }

    public void setParkingSchedule(ParkingSchedule parkingSchedule) {
        this.parkingSchedule = parkingSchedule;
    }

    public Boolean[][] getParkingPermissions() {
        return parkingPermissions;
    }

    public void setParkingPermissions(Boolean[][] parkingPermissions) {
        this.parkingPermissions = parkingPermissions;
    }

    public int getNumberOfParkingLinesWhereCanBeParked(int vehicleId) {
        if (vehicleId < 0 || vehicleId >= parkingPermissions.length) {
            throw new IllegalArgumentException("Vehicle id is not in the required range!");
        }
        return (int) Arrays.stream(parkingPermissions[vehicleId]).filter(parkingLane -> parkingLane).count();
    }

    public int[] getIdsOfParkingLanesWhereCanBeParked(int vehicleId) {
        if (vehicleId < 0 || vehicleId >= parkingPermissions.length) {
            throw new IllegalArgumentException("Vehicle id is not in the required range!");
        }

        long numberOfParkingLanes = getNumberOfParkingLinesWhereCanBeParked(vehicleId);
        int[] indicesOfParkingLanes = new int[(int) numberOfParkingLanes];
        int size = parkingPermissions[vehicleId].length;

        int k = 0;
        for (int i = 0; i < size; i++) {
            if (parkingPermissions[vehicleId][i]) {
                indicesOfParkingLanes[k++] = i;
            }
        }

        return indicesOfParkingLanes;
    }
}