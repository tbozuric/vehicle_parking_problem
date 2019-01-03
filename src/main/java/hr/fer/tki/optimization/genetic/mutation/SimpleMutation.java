package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.Individual;
import hr.fer.tki.optimization.genetic.IndividualFactory;
import hr.fer.tki.optimization.genetic.providers.IMutation;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleMutation implements IMutation {

    private int numberOfParkingLanes;
    private double probabilityOfMutation;

    public SimpleMutation(int numberOfParkingLanesm, double probabilityOfMutation) {
        this.numberOfParkingLanes = numberOfParkingLanes;
        this.probabilityOfMutation = probabilityOfMutation;
    }

    @Override
    public Individual mutate(Individual individual) {

        if (Math.random() < probabilityOfMutation) {
            int index = ThreadLocalRandom.current().nextInt(0, individual.getValues().size());
            int newValue = ThreadLocalRandom.current().nextInt(0, numberOfParkingLanes);

            List<Integer> values = individual.getValues();
            values.set(index, newValue);
            return IndividualFactory.createIndividual(values);
        }
        return individual;
    }
}
