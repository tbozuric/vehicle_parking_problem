package hr.fer.tki.output;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingSchedule;
import hr.fer.tki.models.Vehicle;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringJoiner;

public class GarageOutputWriter {

    public static void printGarageToFile(String outputFilepath, Garage garage) throws FileNotFoundException {
        StringJoiner laneJoiner = new StringJoiner(System.lineSeparator());
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        parkingSchedule.getParkingLanes()
                .forEach(lane -> laneJoiner.add(vehiclesAtLaneToString(parkingSchedule.getVehiclesAt(lane))));

        try (PrintWriter printWriter = new PrintWriter(outputFilepath)) {
            printWriter.print(laneJoiner.toString());
        }
    }

    private static String vehiclesAtLaneToString(List<Vehicle> vehicles) {
        StringJoiner vehicleJoiner = new StringJoiner(" ");
        vehicles.forEach(vehicle -> vehicleJoiner.add(Integer.toString(vehicle.getId() + 1)));
        return vehicleJoiner.toString();
    }
}
