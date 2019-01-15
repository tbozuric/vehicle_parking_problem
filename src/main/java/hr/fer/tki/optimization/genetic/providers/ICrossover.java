package hr.fer.tki.optimization.genetic.providers;

import hr.fer.tki.optimization.genetic.individual.IIndividual;

public interface ICrossover {
    IIndividual crossover(IIndividual firstParent, IIndividual secondParent);
}
