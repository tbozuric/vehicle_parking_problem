package hr.fer.tki.optimization.genetic.providers;

import hr.fer.tki.optimization.genetic.Individual;

import java.util.List;

public interface ISelection {
    List<Individual> selection(int numberOfIndividuals);
}
