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

    public GreedyParkingAlgorithm(Garage garage) {
        super(garage);
    }

    @Override
    public void parkVehiclesInTheGarage() {
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        List<Vehicle> vehicles = parkingSchedule.getVehicles();
        List<ParkingLane> allParkingLanes = parkingSchedule.getParkingLanes();
        //sortiraj vozila po tipu
        Map<Integer, List<Vehicle>> vehicleListGroupedBySeries =
                vehicles.stream().collect(Collectors.groupingBy(Vehicle::getSeriesOfVehicle));

        Set<SeriesParkingLanesTuple> tuples = new TreeSet<>();
        for (Integer series : vehicleListGroupedBySeries.keySet()) {
            Vehicle someVehicleFromSeries = vehicleListGroupedBySeries.get(series).get(0);
            tuples.add(new SeriesParkingLanesTuple(series,
                    garage.getNumberOfParkingLinesWhereCanBeParked(someVehicleFromSeries.getId())));
        }

        for (SeriesParkingLanesTuple tuple : tuples) {
            int series = tuple.getSeriesOfVehicle();
            //vozila ce automatski biti sortirana po vremenu kretanja, zatim po tipu rasporeda pa po duzini da
            //se automatski redaju da ostvare cim vise profita u fji dobrote
            Set<Vehicle> sortedVehicles = new TreeSet<>(vehicleListGroupedBySeries.get(series));
            for (Vehicle vehicle : sortedVehicles) {
                int[] indicesOfParkingLanes = garage.getIdsOfParkingLanesWhereCanBeParked(vehicle.getId());
                Set<ParkingLane> parkingLanes = new TreeSet<>(new GreedyLaneComparator(parkingSchedule));
                for (int index : indicesOfParkingLanes) {
                    ParkingLane lane = allParkingLanes.get(index);
                    //uzmi u obzir parkirnu traku ako nije puna i ako na njoj nije parkirano nijedno vozilo ili
                    //ako trenutno vozilo ima istu seriju kao i vec pakrirano
                    int seriesOfParkedVeihclesAtLane = parkingSchedule.getSeriesOfParkedVeihclesAtLane(lane);
                    if (!parkingSchedule.isParkingLaneFull(lane) && (seriesOfParkedVeihclesAtLane == VEHICLE_SERIES_NOT_DEFINED
                            || seriesOfParkedVeihclesAtLane == vehicle.getSeriesOfVehicle())) {
                        parkingLanes.add(allParkingLanes.get(index));
                    }
                }
                //pakrirne linije su sortirane rastuce po broju blokirajucih linija i po slobodnom prostoru
                // tako da se pune prvo one s najmanje blokirajucih linija pa onda one na kojima je preostalo najmanje
                //slobodnog prosotra da se minimizira broj koristenih traka
                for (ParkingLane parkingLane : parkingLanes) {
                    if (parkingSchedule.parkVehicle(vehicle, parkingLane)) {
                        break;
                    }
                }
            }
        }
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
}
