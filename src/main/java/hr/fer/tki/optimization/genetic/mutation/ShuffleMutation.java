package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.Individual;
import hr.fer.tki.optimization.genetic.IndividualFactory;

import java.util.Collections;
import java.util.List;

public class ShuffleMutation extends AbstractMutationAlgorithm {

    public ShuffleMutation(double probabilityOfMutation) {
        super(probabilityOfMutation);
    }

    @Override
    public Individual mutate(Individual individual) {
        if (Math.random() < probabilityOfMutation) {
            List<Integer> values = individual.getValues();
            Collections.shuffle(values);
            return IndividualFactory.createIndividual(values);
        } else {
            return individual;
        }
    }
}
