package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;
import hr.fer.tki.optimization.genetic.providers.IMutation;

public abstract class AbstractMutationAlgorithm implements IMutation {
    protected double probabilityOfMutation;
    protected IIndividualFactory factory;

    public AbstractMutationAlgorithm(IIndividualFactory factory, double probabilityOfMutation) {
        this.probabilityOfMutation = probabilityOfMutation;
        this.factory = factory;

    }
}
