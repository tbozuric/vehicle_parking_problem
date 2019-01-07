package hr.fer.tki.sketch;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.ParkingSchedule;
import hr.fer.tki.models.Vehicle;

import java.util.Collections;

public class GarageDrawer {

    public static void drawGarage(Garage garage) {
        StringBuilder garageBuilder = new StringBuilder();
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        parkingSchedule.getParkingLanes().forEach(lane -> garageBuilder.append(drawLane(parkingSchedule, lane)));
        System.out.println(garageBuilder.toString());
    }

    private static String drawLane(ParkingSchedule schedule, ParkingLane lane) {
        StringBuilder laneBuilder = new StringBuilder();

        laneBuilder.append("Lane ").append(String.format("%4d | ", lane.getId()));
        for (Vehicle v : schedule.getVehiclesAt(lane)) {
            laneBuilder.append(String.format("id:%d,l:%d,time:%d", v.getId(), v.getLengthOfVehicle(), v.getDepartureTime()));
            laneBuilder.append("| ");
        }
        for (ParkingLane l: lane.getBlockingParkingLanes()) {
            laneBuilder.append(String.format(" blocked by:%d", l.getId()));
        }
        laneBuilder.append(System.lineSeparator());

        laneBuilder.append(String.join("",
                Collections.nCopies((int) (12 + lane.getLengthOfLane()), "-")));
        laneBuilder.append(System.lineSeparator());

        return laneBuilder.toString();
    }
}
