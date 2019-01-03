package hr.fer.tki.optimization.genetic.providers;

import hr.fer.tki.optimization.genetic.Individual;

public interface IMutation {
    Individual mutate(Individual individual);
}
