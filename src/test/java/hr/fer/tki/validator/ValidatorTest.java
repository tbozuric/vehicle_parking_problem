package hr.fer.tki.validator;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.Vehicle;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {

    private Garage garage;

    private ParkingLane laneA;
    private ParkingLane laneB;
    private ParkingLane laneC;
    private Vehicle vehicleA;
    private Vehicle vehicleB;
    private Vehicle vehicleC;

    @Before
    public void initialize_garage() {
        laneA = new ParkingLane(3);
        laneB = new ParkingLane(4);
        laneC = new ParkingLane(5);

        laneB.setBlockingParkingLanes(singletonList(laneA));

        vehicleA = new Vehicle(1, 1, 1, 1);
        vehicleB = new Vehicle(2, 2, 1, 2);
        vehicleC = new Vehicle(3, 1, 1, 3);

        Boolean[][] parkingPermissions = new Boolean[][] {
                {TRUE, TRUE, TRUE},
                {FALSE, FALSE, TRUE},
                {TRUE, TRUE, TRUE}
        };

        garage = new Garage(
                3,
                3,
                new ArrayList<>(Arrays.asList(laneA, laneB, laneC)),
                new ArrayList<>(Arrays.asList(vehicleA, vehicleB, vehicleC)),
                parkingPermissions);
    }

    @Test
    public void formation_is_valid() {
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(new ArrayList<>(Arrays.asList(vehicleA, vehicleC)));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleB));

        assertTrue(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void same_vehicle_at_multiple_lanes() {
        garage.getParkingSchedule().getParkingLanes().get(0).setParkedVehicles(singletonList(vehicleA));
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(singletonList(vehicleA));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleB));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void not_all_vehicles_parked() {
        garage.getParkingSchedule().getParkingLanes().get(0).setParkedVehicles(singletonList(vehicleA));
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(singletonList(vehicleC));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void redundant_vehicles_at_same_lane() {
        garage.getParkingSchedule().getParkingLanes().get(0).setParkedVehicles(new ArrayList<>(Arrays.asList(vehicleA, vehicleA)));
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(singletonList(vehicleA));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleB));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void different_series_vehicles_in_same_lane() {
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(singletonList(vehicleC));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(new ArrayList<>(Arrays.asList(vehicleA, vehicleB)));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void vehicle_in_forbidden_lane() {
        garage.getParkingSchedule().getParkingLanes().get(0).setParkedVehicles(singletonList(vehicleA));
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(singletonList(vehicleB));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleC));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void too_many_vehicles_in_lane() {
        garage.getParkingSchedule().getParkingLanes().get(0).setParkedVehicles(new ArrayList<>(Arrays.asList(vehicleA, vehicleC)));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleB));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void wrong_order_in_same_lane() {
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(new ArrayList<>(Arrays.asList(vehicleC, vehicleA)));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleB));

        assertFalse(GarageValidator.validate(garage).isValid());
    }

    @Test
    public void blocking_lane_should_finish_before_blocked() {
        garage.getParkingSchedule().getParkingLanes().get(0).setParkedVehicles(singletonList(vehicleC));
        garage.getParkingSchedule().getParkingLanes().get(1).setParkedVehicles(singletonList(vehicleA));
        garage.getParkingSchedule().getParkingLanes().get(2).setParkedVehicles(singletonList(vehicleB));

        assertFalse(GarageValidator.validate(garage).isValid());
    }
}
