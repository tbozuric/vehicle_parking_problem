package hr.fer.tki.optimization.genetic.crossover;

import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;
import hr.fer.tki.optimization.genetic.providers.ICrossover;

public abstract class AbstractCrossoverAlgorithm implements ICrossover{
    protected IIndividualFactory factory;

    public AbstractCrossoverAlgorithm(IIndividualFactory factory) {
        this.factory = factory;
    }
}
