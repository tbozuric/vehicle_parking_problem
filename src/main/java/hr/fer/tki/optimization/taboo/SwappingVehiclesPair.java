package hr.fer.tki.optimization.taboo;

import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.Vehicle;

public class SwappingVehiclesPair {

    private ParkingLane firstLane;
    private Vehicle firstVehicle;
    private ParkingLane secondLane;
    private Vehicle secondVehicle;

    public SwappingVehiclesPair(ParkingLane firstLane, Vehicle firstVehicle, ParkingLane secondLane, Vehicle secondVehicle) {
        this.firstLane = firstLane;
        this.firstVehicle = firstVehicle;
        this.secondLane = secondLane;
        this.secondVehicle = secondVehicle;
    }

    public ParkingLane getFirstLane() {
        return firstLane;
    }

    public Vehicle getFirstVehicle() {
        return firstVehicle;
    }

    public ParkingLane getSecondLane() {
        return secondLane;
    }

    public Vehicle getSecondVehicle() {
        return secondVehicle;
    }
}
