package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;

import java.util.Collections;
import java.util.List;

public class ShuffleMutation extends AbstractMutationAlgorithm {


    public ShuffleMutation(IIndividualFactory factory, double probabilityOfMutation) {
        super(factory, probabilityOfMutation);
    }

    @Override
    public IIndividual mutate(IIndividual individual) {
        if (Math.random() < probabilityOfMutation) {
            List<Integer> values = individual.getValues();
            Collections.shuffle(values);
            return factory.createIndividual(values);
        } else {
            return individual;
        }
    }
}
