package hr.fer.tki.optimization.genetic.individual;

import hr.fer.tki.models.Garage;
import hr.fer.tki.optimization.greedy.GreedyParkingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndividualFactory implements IIndividualFactory {

    private static IndividualFactory factory;
    private Garage emptyGarage;


    private IndividualFactory(Garage emptyGarage) {
        this.emptyGarage = emptyGarage;
    }


    public static IndividualFactory getFactory(Garage emptyGarage) {
        if (factory == null) {
            factory = new IndividualFactory(emptyGarage);
        }
        return factory;
    }

    @Override
    public List<IIndividual> createPopulation(int sizeOfPopulation) {

        List<IIndividual> population = new ArrayList<>(sizeOfPopulation);
        Garage clone = new Garage(emptyGarage);

        GreedyParkingAlgorithm parkingAlgorithm = new GreedyParkingAlgorithm(clone);
        parkingAlgorithm.parkVehiclesInTheGarage();
        List<Integer> parkedVehicles = clone.getParkingSchedule().getParkedVehiclesOnLanesAsArray();

        population.add(new ParkingIndividual(parkedVehicles, clone));

        for (int i = 1; i < sizeOfPopulation; i++) {
            List<Integer> values = new ArrayList<>(parkedVehicles);
            Collections.shuffle(values);
            population.add(new ParkingIndividual(values, new Garage(emptyGarage)));
        }

//        for (int i = 0; i < sizeOfPopulation; i++) {
//            PrimitiveIterator.OfInt iterator = ThreadLocalRandom.current()
//                    .ints(numberOfParkingLanes, 0, numberOfParkingLanes)
//                    .iterator();
//            List<Integer> values = new ArrayList<>(numberOfParkingLanes);
//            for (int j = 0; j < numberOfParkingLanes; j++) {
//                values.add(iterator.nextInt());
//            }
//            population.add(new ParkingIndividual(values, new Garage(emptyGarage)));
//        }
        return population;
    }

    @Override
    public IIndividual createIndividual(List<Integer> values) {
        return new ParkingIndividual(values, new Garage(emptyGarage));
    }
}
