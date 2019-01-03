package hr.fer.tki.optimization.genetic.mutation;

import hr.fer.tki.optimization.genetic.providers.IMutation;

public abstract class AbstractMutationAlgorithm implements IMutation {
    protected double probabilityOfMutation;

    public AbstractMutationAlgorithm(double probabilityOfMutation) {
        this.probabilityOfMutation = probabilityOfMutation;
    }
}
