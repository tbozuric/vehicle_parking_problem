package hr.fer.tki.optimization.genetic.providers;

import hr.fer.tki.optimization.genetic.Individual;

public interface ICrossover {
    Individual crossover(Individual firstParent, Individual secondParent);
}
