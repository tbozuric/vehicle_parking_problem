package hr.fer.tki.optimization.greedy;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.ParkingSchedule;
import hr.fer.tki.models.Vehicle;
import hr.fer.tki.optimization.AbstractOptimizationAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

import static hr.fer.tki.models.ParkingSchedule.VEHICLE_SERIES_NOT_DEFINED;

public class GreedyParkingAlgorithm extends AbstractOptimizationAlgorithm {

    private Map<Vehicle, Integer> canBeParkedFrequency;

    public GreedyParkingAlgorithm(Garage garage) {
        super(garage, "Greedy algorithm");
        canBeParkedFrequency = new HashMap<>();
    }

    @Override
    public void parkVehiclesInTheGarage() {
        notifyAlgorithmStarted();

        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        List<Vehicle> vehicles = parkingSchedule.getVehicles();
        List<ParkingLane> allParkingLanes = parkingSchedule.getParkingLanes();
        // sortiraj vozila po tipu
        Map<Integer, List<Vehicle>> vehicleListGroupedBySeries =
                vehicles.stream().collect(Collectors.groupingBy(Vehicle::getSeriesOfVehicle));

        Set<SeriesParkingLanesTuple> tuples = new TreeSet<>();

        for (Integer series : vehicleListGroupedBySeries.keySet()) {

            double average = 0.0;
            List<Vehicle> vehiclesInSeries = vehicleListGroupedBySeries.get(series);
            for (Vehicle vehicle : vehiclesInSeries) {
                int numberOfParkingLanesWhereCanBeParked = garage.getNumberOfParkingLinesWhereCanBeParked(vehicle.getId());
                average += numberOfParkingLanesWhereCanBeParked;
                canBeParkedFrequency.put(vehicle, numberOfParkingLanesWhereCanBeParked);

            }
            average /= (double) vehiclesInSeries.size();

            tuples.add(new SeriesParkingLanesTuple(series, average));
        }

        for (SeriesParkingLanesTuple tuple : tuples) {
            int series = tuple.getSeriesOfVehicle();
            // vozila ce automatski biti sortirana po vremenu kretanja, zatim po tipu rasporeda pa po duzini da
            // se automatski redaju da ostvare cim vise profita u fji dobrote

            Set<Vehicle> sortedVehicles = new TreeSet<>(new VehiclesComparator(canBeParkedFrequency));
            sortedVehicles.addAll(vehicleListGroupedBySeries.get(series));


            for (Vehicle vehicle : sortedVehicles) {
                int[] indicesOfParkingLanes = garage.getIdsOfParkingLanesWhereCanBeParked(vehicle.getId());
                Set<ParkingLane> parkingLanes = new TreeSet<>(new GreedyLaneComparator(parkingSchedule));
                for (int index : indicesOfParkingLanes) {
                    ParkingLane lane = allParkingLanes.get(index);
                    // uzmi u obzir parkirnu traku ako nije puna i ako na njoj nije parkirano nijedno vozilo ili
                    // ako trenutno vozilo ima istu seriju kao i vec parkirano
                    int seriesOfParkedVeihclesAtLane = parkingSchedule.getSeriesOfParkedVehiclesAtLane(lane);
                    if (!parkingSchedule.isParkingLaneFull(lane) && (seriesOfParkedVeihclesAtLane
                            == VEHICLE_SERIES_NOT_DEFINED
                            || seriesOfParkedVeihclesAtLane == vehicle.getSeriesOfVehicle())) {
                        parkingLanes.add(allParkingLanes.get(index));
                    }
                }
                // parkirne linije su sortirane rastuce po broju blokirajucih linija i po slobodnom prostoru
                // tako da se pune prvo one s najmanje blokirajucih linija pa onda one na kojima je preostalo najmanje
                // slobodnog prostora da se minimizira broj koristenih traka
                for (ParkingLane parkingLane : parkingLanes) {
                    if (parkingSchedule.parkVehicle(vehicle, parkingLane)) {
                        break;
                    }
                }
            }
        }

        notifyListenersAboutNewSolution("GREEDY");
        notifyAlgorithmOver();
    }

    class GreedyLaneComparator implements Comparator<ParkingLane> {

        private ParkingSchedule parkingSchedule;

        public GreedyLaneComparator(ParkingSchedule parkingSchedule) {
            this.parkingSchedule = parkingSchedule;
        }

        @Override
        public int compare(ParkingLane o1, ParkingLane o2) {
            return Comparator.comparingInt(ParkingLane::getNumberOfBlockingLanes)
                    .thenComparingDouble(parkingSchedule::getAvailableSpaceAt)
                    .thenComparingInt(ParkingLane::getId)
                    .compare(o1, o2);
        }
    }


    class VehiclesComparator implements Comparator<Vehicle> {

        private Map<Vehicle, Integer> parkingFrequency;

        public VehiclesComparator(Map<Vehicle, Integer> parkingFrequency) {
            this.parkingFrequency = parkingFrequency;
        }

        @Override
        public int compare(Vehicle o1, Vehicle o2) {

            Comparator<Vehicle> byParkingLines = Comparator
                    .comparingInt(x -> parkingFrequency.get(x));

            Comparator<Vehicle> byLengthOfVehicle = Collections.reverseOrder(
                    Comparator.comparingInt(Vehicle::getLengthOfVehicle));

            return Comparator
                    .comparingInt(Vehicle::getSeriesOfVehicle)
                    .thenComparing(byLengthOfVehicle)
                    .thenComparing(byParkingLines)
                    .thenComparingInt(Vehicle::getDepartureTime)
                    .thenComparingInt(Vehicle::getTypeOfSchedule)
                    .thenComparingInt(Vehicle::getId)
                    .compare(o1, o2);
        }
    }
}
