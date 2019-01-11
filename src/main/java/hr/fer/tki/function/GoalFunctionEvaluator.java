package hr.fer.tki.function;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.ParkingSchedule;
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

    private ParkingSchedule parkingSchedule;
    private List<ParkingLane> parkingLanes;
    private List<Vehicle> vehicles;

    public GoalFunctionEvaluator(Garage garage) {
        Objects.requireNonNull(garage, "Garage must not be null!");
        this.parkingSchedule = garage.getParkingSchedule();
        this.parkingLanes = parkingSchedule.getParkingLanes();
        this.vehicles = parkingSchedule.getVehicles();
        this.numberOfEvaluations = 0;
        initFunctions();
    }

    private void initFunctions() {
        f1 = () -> {
            int output = 0;
            int size = parkingLanes.size();
            for (int i = 0; i < size - 1; i++) {
                int vehicleSeriesOfParkingLane = parkingSchedule.getSeriesOfParkedVehiclesAtLane(parkingLanes.get(i));
                int vehicleSeriesOfOtherParkingLane = parkingSchedule.getSeriesOfParkedVehiclesAtLane(parkingLanes.get(i + 1));
                if (vehicleSeriesOfParkingLane != -1 && vehicleSeriesOfOtherParkingLane != -1) {
                    if (vehicleSeriesOfParkingLane != vehicleSeriesOfOtherParkingLane) {
                        output += 1;
                    }
                }
            }
            return output;
        };

        f2 = () -> (int) parkingLanes.stream()
                .filter(parkingLane -> parkingSchedule.getVehiclesAt(parkingLane).size() != 0)
                .count();

        f3 = () -> parkingLanes.stream()
                .filter(parkingLane -> parkingSchedule.getVehiclesAt(parkingLane).size() != 0)
                .mapToDouble(parkingSchedule::getAvailableSpaceAt)
                .sum();


        g1 = () -> {
            int sum = 0;
            for (ParkingLane lane : parkingLanes) {
                List<Vehicle> vehicles = parkingSchedule.getVehiclesAt(lane);
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
                List<Vehicle> currentLaneParkedVehicles = parkingSchedule.getVehiclesAt(parkingLanes.get(i));
                List<Vehicle> nextLaneParkedVehicles = parkingSchedule.getVehiclesAt(parkingLanes.get(i + 1));

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
                List<Vehicle> parkedVehicles = parkingSchedule.getVehiclesAt(parkingLane);
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


    public double evaluateMinimizationProblem() {

        int numberOfParkingLanes = parkingLanes.size();

        double p1 = pow(numberOfParkingLanes - 1, -1);
        double p2 = pow(numberOfParkingLanes, -1);
        double p3 = pow(parkingLanes.stream().mapToDouble(ParkingLane::getLengthOfLane).sum() -
                vehicles.stream().mapToInt(Vehicle::getLengthOfVehicle).sum(), -1);

        return f1.get() * p1 + f2.get() * p2 + f3.get() * p3;
    }


    public double evaluateMaximizationProblem() {

        double r1 = pow(vehicles.size() - parkingLanes.stream().filter(lane -> parkingSchedule.getVehiclesAt(lane).size() > 0).count(), -1);
        double r2 = pow(parkingLanes.size() - 1, -1);
        double g3Value = g3.get();
        double r3 = pow(15 * numberOfEvaluations, -1);

        double result = g1.get() * r1 + g2.get() * r2 + g3Value * r3;
        numberOfEvaluations = 0;
        return result;
    }

    public double evaluateTotalProblem() {
        return evaluateMaximizationProblem() / evaluateMinimizationProblem();
    }
}
