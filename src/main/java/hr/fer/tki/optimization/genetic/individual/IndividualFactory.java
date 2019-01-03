package hr.fer.tki.optimization.genetic.individual;

import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingLane;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.ThreadLocalRandom;

public class IndividualFactory implements IIndividualFactory {

    private static IndividualFactory factory;
    private Garage emptyGarage;
    private List<ParkingLane> parkingLanes;

    private IndividualFactory(Garage emptyGarage) {
        this.emptyGarage = emptyGarage;
        this.parkingLanes = emptyGarage.getParkingSchedule().getParkingLanes();
    }


    public static IndividualFactory getFactory(Garage emptyGarage) {
        if (factory == null) {
            factory = new IndividualFactory(emptyGarage);
        }
        return factory;
    }

    @Override
    public List<IIndividual> createPopulation(int sizeOfPopulation) {
        int numberOfParkingLanes = parkingLanes.size();
        List<IIndividual> population = new ArrayList<>(sizeOfPopulation);

        for (int i = 0; i < sizeOfPopulation; i++) {
            PrimitiveIterator.OfInt iterator = ThreadLocalRandom.current()
                    .ints(numberOfParkingLanes - 1, 0, numberOfParkingLanes)
                    .iterator();
            List<Integer> values = new ArrayList<>(numberOfParkingLanes);
            for (int j = 0; j < numberOfParkingLanes; j++) {
                values.add(iterator.nextInt());
            }
            population.add(new ParkingIndividual(values, new Garage(emptyGarage)));
        }
        return population;
    }

    @Override
    public IIndividual createIndividual(List<Integer> values) {
        return new ParkingIndividual(values, new Garage(emptyGarage));
    }
}
