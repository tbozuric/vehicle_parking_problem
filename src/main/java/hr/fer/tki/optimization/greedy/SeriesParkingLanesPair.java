package hr.fer.tki.optimization.greedy;

import java.util.Comparator;
import java.util.Objects;

public class SeriesParkingLanesPair implements Comparable<SeriesParkingLanesPair> {
    private int seriesOfVehicle;
    private double averageNumberOfParkingLines;

    public SeriesParkingLanesPair(int seriesOfVehicle, double averageNumberOfParkingLines) {
        this.seriesOfVehicle = seriesOfVehicle;
        this.averageNumberOfParkingLines = averageNumberOfParkingLines;
    }

    @Override
    public int compareTo(SeriesParkingLanesPair o) {
        return Comparator.comparingDouble(SeriesParkingLanesPair::getAverageNumberOfParkingLines)
                .thenComparingInt(SeriesParkingLanesPair::getSeriesOfVehicle).compare(this, o);
    }

    public int getSeriesOfVehicle() {
        return seriesOfVehicle;
    }

    public void setSeriesOfVehicle(int seriesOfVehicle) {
        this.seriesOfVehicle = seriesOfVehicle;
    }

    public double getAverageNumberOfParkingLines() {
        return averageNumberOfParkingLines;
    }

    public void setAverageNumberOfParkingLines(int averageNumberOfParkingLines) {
        this.averageNumberOfParkingLines = averageNumberOfParkingLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeriesParkingLanesPair that = (SeriesParkingLanesPair) o;
        return seriesOfVehicle == that.seriesOfVehicle;
    }

    @Override
    public int hashCode() {

        return Objects.hash(seriesOfVehicle);
    }
}
