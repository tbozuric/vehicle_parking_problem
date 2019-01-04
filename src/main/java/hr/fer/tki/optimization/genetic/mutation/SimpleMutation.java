package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleMutation extends AbstractMutationAlgorithm {

    private int numberOfParkingLanes;

    public SimpleMutation(IIndividualFactory factory, int numberOfParkingLanes) {
        super(factory);
        this.numberOfParkingLanes = numberOfParkingLanes;
    }


    @Override
    public IIndividual mutate(IIndividual individual, double probabilityOfMutation) {
        List<Integer> values = new ArrayList<>(individual.getValues());
        if (Math.random() < probabilityOfMutation) {
            int index = ThreadLocalRandom.current().nextInt(0, individual.getValues().size());
            int newValue = ThreadLocalRandom.current().nextInt(0, numberOfParkingLanes);
            values.set(index, newValue);
        }
        return factory.createIndividual(values);
    }
}
