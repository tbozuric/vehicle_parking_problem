package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleMutation extends AbstractMutationAlgorithm {

    private int numberOfParkingLanes;
    private double probabilityOfMutation;

    public SimpleMutation(IIndividualFactory factory, double probabilityOfMutation, int numberOfParkingLanes) {
        super(factory, probabilityOfMutation);
        this.probabilityOfMutation = probabilityOfMutation;
        this.numberOfParkingLanes = numberOfParkingLanes;
    }


    @Override
    public IIndividual mutate(IIndividual individual) {

        if (Math.random() < probabilityOfMutation) {
            int index = ThreadLocalRandom.current().nextInt(0, individual.getValues().size());
            int newValue = ThreadLocalRandom.current().nextInt(0, numberOfParkingLanes);

            List<Integer> values = individual.getValues();
            values.set(index, newValue);
            return factory.createIndividual(values);
        }
        return individual;
    }
}
