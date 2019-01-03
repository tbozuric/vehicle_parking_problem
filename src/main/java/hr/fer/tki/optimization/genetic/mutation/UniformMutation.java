package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UniformMutation extends AbstractMutationAlgorithm {
    private int numberOfParkingLanes;

    public UniformMutation(IIndividualFactory factory, double probabilityOfMutation, int numberOfParkingLanes) {
        super(factory, probabilityOfMutation);
        this.numberOfParkingLanes = numberOfParkingLanes;
    }


    @Override
    public IIndividual mutate(IIndividual individual) {
        List<Integer> values = individual.getValues();
        List<Integer> newValues = new ArrayList<>(values.size());
        for (Integer value : values) {
            if (Math.random() < probabilityOfMutation) {
                newValues.add(ThreadLocalRandom.current().nextInt(0, numberOfParkingLanes));
            } else {
                newValues.add(value);
            }
        }
        return factory.createIndividual(newValues);
    }
}
