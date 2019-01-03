package hr.fer.tki.optimization.genetic.providers;

import hr.fer.tki.optimization.genetic.individual.IIndividual;

import java.util.List;

public interface ISelection {
    List<IIndividual> select(int numberOfIndividuals);
}
