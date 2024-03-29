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

    public Garage(Garage another) {
        this.numberOfParkingLanes = another.getNumberOfParkingLanes();
        this.numberOfVehicles = another.getNumberOfVehicles();
        ParkingSchedule parkingSchedule = another.getParkingSchedule();
        this.parkingSchedule = parkingSchedule.deepcopy();
        this.parkingPermissions = another.getParkingPermissions();
    }

    public int getNumberOfParkingLanes() {
        return numberOfParkingLanes;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public ParkingSchedule getParkingSchedule() {
        return parkingSchedule;
    }

    public void setParkingSchedule(ParkingSchedule parkingSchedule) {
        this.parkingSchedule = parkingSchedule;
    }

    public Boolean[][] getParkingPermissions() {
        return parkingPermissions;
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