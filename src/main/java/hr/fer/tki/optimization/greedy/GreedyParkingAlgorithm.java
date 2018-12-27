package hr.fer.tki.optimization.greedy;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;
import hr.fer.tki.models.Vehicle;
import hr.fer.tki.optimization.AbstractOptimizationAlgorithm;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GreedyParkingAlgorithm extends AbstractOptimizationAlgorithm {

    public GreedyParkingAlgorithm(Garage garage) {
        super(garage);
    }

    @Override
    public void parkVehiclesInTheGarage() {
        List<Vehicle> vehicles = garage.getParkingSchedule().getVehicles();
        List<ParkingLane> allParkingLanes = garage.getParkingSchedule().getParkingLanes();
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
                Set<ParkingLane> parkingLanes = new TreeSet<>();
                for (int index : indicesOfParkingLanes) {
                    ParkingLane lane = allParkingLanes.get(index);
                    //uzmi u obzir parkirnu traku ako nije puna i ako na njoj nije parkirano nijedno vozilo ili
                    //ako trenutno vozilo ima istu seriju kao i vec pakrirano
                    if (!lane.isParkingLaneFull() && (lane.getVehicleSeries() == -1 ||
                            lane.getVehicleSeries() == vehicle.getSeriesOfVehicle())) {
                        parkingLanes.add(allParkingLanes.get(index));
                    }
                }
                //pakrirne linije su sortirane rastuce po broju blokirajucih linija i po slobodnom prostoru
                // tako da se pune prvo one s najmanje blokirajucih linija pa onda one na kojima je preostalo najmanje
                //slobodnog prosotra da se minimizira broj koristenih traka
                for (ParkingLane parkingLane : parkingLanes) {
                    if (parkingLane.parkVehicle(vehicle)) {
                        break;
                    }
                }
            }
        }
    }
}
