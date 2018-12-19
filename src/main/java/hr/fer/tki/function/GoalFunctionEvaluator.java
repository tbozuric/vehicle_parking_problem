package hr.fer.tki.function;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.Vehicle;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.lang.Math.pow;

public class GoalFunctionEvaluator {


    private static final int UPPER_TIME_INTERVAL_LIMIT = 20;
    private static final int LOWER_TIME_INTERVAL_LIMIT = 10;
    private Supplier<Integer> f1;
    private Supplier<Integer> f2;
    private Supplier<Double> f3;
    private Supplier<Integer> g1;
    private Supplier<Integer> g2;
    private Supplier<Integer> g3;
    private int numberOfEvaluations;

    private List<ParkingLane> parkingLanes;
    private List<Vehicle> vehicles;


    private GoalFunctionEvaluator(Garage garage) {
        Objects.requireNonNull(garage, "Garage must not be null!");
        this.parkingLanes = garage.getParkingLanes();
        this.vehicles = garage.getVehicles();
        this.numberOfEvaluations = 0;
        initFunctions();
    }


    private void initFunctions() {
        f1 = () -> {
            int output = 0;
            int size = parkingLanes.size();
            for (int i = 0; i < size - 1; i++) {
                int vehicleSeriesOfParkingLane = parkingLanes.get(i).getVehicleSeries();
                int vehicleSeriesOfOtherParkingLane = parkingLanes.get(i + 1).getVehicleSeries();
                if (vehicleSeriesOfParkingLane != -1 && vehicleSeriesOfOtherParkingLane != -1) {
                    if (vehicleSeriesOfParkingLane != vehicleSeriesOfOtherParkingLane) {
                        output += 1;
                    }
                }
            }
            return output;
        };

        f2 = () -> (int) parkingLanes.stream()
                .filter(parkingLane -> parkingLane.getParkedVehicles().size() != 0)
                .count();

        f3 = () -> parkingLanes.stream()
                .filter(parkingLane -> parkingLane.getParkedVehicles().size() != 0)
                .mapToDouble(ParkingLane::getAvailableSpace)
                .sum();


        g1 = () -> {
            int sum = 0;
            for (ParkingLane lane : parkingLanes) {
                List<Vehicle> vehicles = lane.getParkedVehicles();
                int size = vehicles.size();
                if (size != 0) {
                    for (int i = 0; i < size - 1; i++) {
                        if (vehicles.get(i).getTypeOfSchedule() == vehicles.get(i + 1).getTypeOfSchedule()) {
                            sum += 1;
                        }
                    }
                }
            }
            return sum;
        };

        g2 = () -> {
            int size = parkingLanes.size();
            int sum = 0;
            for (int i = 0; i < size - 1; i++) {
                List<Vehicle> currentLaneParkedVehicles = parkingLanes.get(i).getParkedVehicles();
                List<Vehicle> nextLaneParkedVehicles = parkingLanes.get(i + 1).getParkedVehicles();

                if (currentLaneParkedVehicles.size() != 0
                        && nextLaneParkedVehicles.size() != 0) {
                    if (currentLaneParkedVehicles.get(currentLaneParkedVehicles.size() - 1).getTypeOfSchedule() ==
                            nextLaneParkedVehicles.get(0).getTypeOfSchedule()) {
                        sum += 1;
                    }
                }
            }
            return sum;
        };

        g3 = () -> {
            int reward = 0;
            for (ParkingLane parkingLane : parkingLanes) {
                List<Vehicle> parkedVehicles = parkingLane.getParkedVehicles();
                int sizeOfParkedVehicles = parkedVehicles.size();
                for (int i = 0; i < sizeOfParkedVehicles - 1; i++) {
                    int timeInterval = parkedVehicles.get(i + 1).getDepartureTime()
                            - parkedVehicles.get(i).getDepartureTime();
                    numberOfEvaluations++;
                    if (timeInterval >= LOWER_TIME_INTERVAL_LIMIT && timeInterval <= UPPER_TIME_INTERVAL_LIMIT) {
                        reward += 15;
                    } else if (timeInterval > UPPER_TIME_INTERVAL_LIMIT) {
                        reward += 10;
                    } else {
                        reward += (-4 * (10 - timeInterval));
                    }
                }
            }
            return reward;
        };
    }


    public double evaluteMinimizationProblem() {

        int numberOfParkingLanes = parkingLanes.size();

        double p1 = pow(numberOfParkingLanes - 1, -1);
        double p2 = pow(numberOfParkingLanes, -1);
        double p3 = pow(parkingLanes.stream().mapToInt(ParkingLane::getLengthOfLane).sum() -
                vehicles.stream().mapToInt(Vehicle::getLengthOfVehicle).sum(), -1);

        return f1.get() * p1 + f2.get() * p2 + f3.get() * p3;
    }


    public double evaluateMaximizationProblem() {

        double r1 = pow(vehicles.size() - parkingLanes.stream().filter(x -> x.getNumberOfParkedVehicles() > 0).count(),
                -1);
        double r2 = pow(parkingLanes.size() - 1, -1);
        double r3 = pow(15 * numberOfEvaluations, -1);

        double result = g1.get() * r1 + g2.get() * r2 + g3.get() * r3;
        numberOfEvaluations = 0;
        return result;
    }
}