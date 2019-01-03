package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.Individual;
import hr.fer.tki.optimization.genetic.IndividualFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UniformMutation extends AbstractMutationAlgorithm {
    private int numberOfParkingLanes;

    public UniformMutation(double probabilityOfMutation, int numberOfParkingLanes) {
        super(probabilityOfMutation);
        this.numberOfParkingLanes = numberOfParkingLanes;
    }

    @Override
    public Individual mutate(Individual individual) {
        List<Integer> values = individual.getValues();
        List<Integer> newValues = new ArrayList<>(values.size());
        for (Integer value : values) {
            if (Math.random() < probabilityOfMutation) {
                newValues.add(ThreadLocalRandom.current().nextInt(0, numberOfParkingLanes));
            } else {
                newValues.add(value);
            }
        }
        return IndividualFactory.createIndividual(newValues);
    }
}
