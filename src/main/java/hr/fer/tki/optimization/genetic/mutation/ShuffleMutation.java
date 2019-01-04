package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleMutation extends AbstractMutationAlgorithm {


    public ShuffleMutation(IIndividualFactory factory) {
        super(factory);
    }

    @Override
    public IIndividual mutate(IIndividual individual, double probabilityOfMutation) {
        List<Integer> values = new ArrayList<>(individual.getValues());
        if (Math.random() < probabilityOfMutation) {
            Collections.shuffle(values);
        }
        return factory.createIndividual(values);
    }
}
