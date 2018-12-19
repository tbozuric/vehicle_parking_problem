package hr.fer.tki.models;

import java.util.Comparator;
import java.util.Objects;

public class Vehicle implements Comparable<Vehicle> {
    private static int counter = 0;

    private int id;
    private int lengthOfVehicle;
    private int seriesOfVehicle;
    private int typeOfSchedule;
    private int departureTime;

    public Vehicle(int lengthOfVehicle, int seriesOfVehicle, int typeOfSchedule, int departureTime) {
        this.id = counter++;
        this.lengthOfVehicle = lengthOfVehicle;
        this.seriesOfVehicle = seriesOfVehicle;
        this.typeOfSchedule = typeOfSchedule;
        this.departureTime = departureTime;
    }

    public int getId() {
        return id;
    }

    public int getLengthOfVehicle() {
        return lengthOfVehicle;
    }

    public void setLengthOfVehicle(int lengthOfVehicle) {
        this.lengthOfVehicle = lengthOfVehicle;
    }

    public int getSeriesOfVehicle() {
        return seriesOfVehicle;
    }

    public void setSeriesOfVehicle(int seriesOfVehicle) {
        this.seriesOfVehicle = seriesOfVehicle;
    }

    public int getTypeOfSchedule() {
        return typeOfSchedule;
    }

    public void setTypeOfSchedule(int typeOfSchedule) {
        this.typeOfSchedule = typeOfSchedule;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public int compareTo(Vehicle other) {
        return Comparator
                .comparingInt(Vehicle::getSeriesOfVehicle)
                .thenComparingInt(Vehicle::getDepartureTime)
                .thenComparingInt(Vehicle::getTypeOfSchedule)
                .thenComparingInt(Vehicle::getLengthOfVehicle)
                .thenComparingInt(Vehicle::getId)
                .compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
