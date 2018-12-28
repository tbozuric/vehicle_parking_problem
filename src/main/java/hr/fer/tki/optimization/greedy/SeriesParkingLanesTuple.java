package hr.fer.tki.optimization.greedy;

import java.util.Comparator;

public class SeriesParkingLanesTuple implements Comparable<SeriesParkingLanesTuple> {
    private int seriesOfVehicle;
    private int numberOfParkingLines;

    public SeriesParkingLanesTuple(int seriesOfVehicle, int numberOfParkingLines) {
        this.seriesOfVehicle = seriesOfVehicle;
        this.numberOfParkingLines = numberOfParkingLines;
    }

    @Override
    public int compareTo(SeriesParkingLanesTuple o) {
        return Comparator.comparingInt(SeriesParkingLanesTuple::getNumberOfParkingLines).compare(this, o);
    }

    public int getSeriesOfVehicle() {
        return seriesOfVehicle;
    }

    public void setSeriesOfVehicle(int seriesOfVehicle) {
        this.seriesOfVehicle = seriesOfVehicle;
    }

    public int getNumberOfParkingLines() {
        return numberOfParkingLines;
    }

    public void setNumberOfParkingLines(int numberOfParkingLines) {
        this.numberOfParkingLines = numberOfParkingLines;
    }
}
