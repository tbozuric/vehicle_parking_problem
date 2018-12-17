package hr.fer.tki.util;

import java.util.Arrays;

public class VehicleUtil {

    private Boolean[][] canBeParkedOnParkingLane;

    public VehicleUtil(Boolean[][] canBeParkedOnParkingLane) {
        this.canBeParkedOnParkingLane = canBeParkedOnParkingLane;
    }


    public Boolean[][] getCanBeParkedOnParkingLane() {
        return canBeParkedOnParkingLane;
    }

    public void setCanBeParkedOnParkingLane(Boolean[][] canBeParkedOnParkingLane) {
        this.canBeParkedOnParkingLane = canBeParkedOnParkingLane;
    }


    private long getNumberOfParkingLinesWhereVehicleCanBeParked(int vehicleId) {
        if (vehicleId < 0 || vehicleId >= canBeParkedOnParkingLane.length) {
            throw new IllegalArgumentException("Vehicle id is not in the required range!");
        }
        return Arrays.stream(canBeParkedOnParkingLane[vehicleId]).filter(parkingLane -> parkingLane).count();
    }

    private int[] getIdsOfParkingLanesWhereCanBeParked(int vehicleId) {
        if (vehicleId < 0 || vehicleId >= canBeParkedOnParkingLane.length) {
            throw new IllegalArgumentException("Vehicle id is not in the required range!");
        }

        long numberOfParkingLanes = getNumberOfParkingLinesWhereVehicleCanBeParked(vehicleId);
        int[] indicesOfParkingLanes = new int[(int) numberOfParkingLanes];
        int size = canBeParkedOnParkingLane[vehicleId].length;

        int k = 0;
        for (int i = 0; i < size; i++) {
            if (canBeParkedOnParkingLane[vehicleId][i]) {
                indicesOfParkingLanes[k++] = i;
            }
        }

        return indicesOfParkingLanes;
    }

}
