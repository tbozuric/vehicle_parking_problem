package hr.fer.tki.models;

public class Vehicle {
    private static int counter = 1;

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
}
