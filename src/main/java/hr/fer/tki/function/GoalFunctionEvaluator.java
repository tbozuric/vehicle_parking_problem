package hr.fer.tki.function;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.ParkingSchedule;
import hr.fer.tki.models.Vehicle;

import java.util.ArrayList;
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

    private double f1Total;
    private double f2Total;
    private double f3Total;

    private double g1Total;
    private double g2Total;
    private double g3Total;


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
            List<Integer> values = new ArrayList<>(size);
            for (ParkingLane parkingLane : parkingLanes) {
                int vehicleSeriesOfParkingLane = parkingSchedule.getSeriesOfParkedVehiclesAtLane(parkingLane);
                if (vehicleSeriesOfParkingLane != -1) {
                    values.add(vehicleSeriesOfParkingLane);
                }
            }
            int sizeOfValid = values.size();
            for (int i = 0; i < sizeOfValid - 1; i++) {
                int vehicleSeriesOfParkingLane = values.get(i);
                int vehicleSeriesOfOtherParkingLane = values.get(i + 1);
                if (vehicleSeriesOfParkingLane != vehicleSeriesOfOtherParkingLane) {
                    output += 1;
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
            List<Integer> validValues = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                List<Vehicle> currentLaneParkedVehicles = parkingSchedule.getVehiclesAt(parkingLanes.get(i));
                if (currentLaneParkedVehicles.size() != 0) {
                    validValues.add(i);
                }
            }

            int validSize = validValues.size();

            for (int i = 0; i < validSize - 1; i++) {
                int indexCurrentLaneParkedVehicles = validValues.get(i);
                int indexNextLaneParkedVehicles = validValues.get(i + 1);

                List<Vehicle> currentLaneParkedVehicles = parkingSchedule.getVehiclesAt(parkingLanes.get(indexCurrentLaneParkedVehicles));
                List<Vehicle> nextLaneParkedVehicles = parkingSchedule.getVehiclesAt(parkingLanes.get(indexNextLaneParkedVehicles));
                if (currentLaneParkedVehicles.get(currentLaneParkedVehicles.size() - 1).getTypeOfSchedule() ==
                        nextLaneParkedVehicles.get(0).getTypeOfSchedule()) {
                    sum += 1;
                }
            }
            return sum;
        }

        ;

        g3 = () ->

        {
            int reward = 0;
            numberOfEvaluations = 0;
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
        }

        ;
    }


    public double evaluateMinimizationProblem() {

        int numberOfParkingLanes = parkingLanes.size();

        double p1 = pow((int) parkingLanes.stream()
                .filter(parkingLane -> parkingSchedule.getVehiclesAt(parkingLane).size() != 0)
                .count() - 1, -1);
        double p2 = pow(numberOfParkingLanes, -1);
        double p3 = pow(parkingLanes.stream().mapToDouble(ParkingLane::getLengthOfLane).sum() -
                vehicles.stream().mapToInt(Vehicle::getLengthOfVehicle).sum(), -1);


        f1Total = f1.get() * p1;
        f2Total = f2.get() * p2;
        f3Total = f3.get() * p3;

        return f1Total + f2Total + f3Total;
    }


    public double evaluateMaximizationProblem() {
        int numberOfUsedLanes = (int) parkingLanes.stream()
                .filter(parkingLane -> parkingSchedule.getVehiclesAt(parkingLane).size() != 0)
                .count();
        double r1 = pow(vehicles.size() - numberOfUsedLanes, -1);
        double r2 = pow(numberOfUsedLanes - 1, -1);
        double g3Value = g3.get();
        double r3 = pow(15 * numberOfEvaluations, -1);

        g1Total = g1.get() * r1;
        g2Total = g2.get() * r2;
        g3Total = g3Value * r3;

        return g1Total + g2Total + g3Total;

    }

    public double evaluateTotalProblem() {
        return evaluateMinimizationProblem() - evaluateMaximizationProblem();
    }


    public double getF1Total() {
        return f1Total;
    }

    public double getF2Total() {
        return f2Total;
    }

    public double getF3Total() {
        return f3Total;
    }

    public double getG1Total() {
        return g1Total;
    }

    public double getG2Total() {
        return g2Total;
    }

    public double getG3Total() {
        return g3Total;
    }
}
