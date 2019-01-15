package hr.fer.tki.optimization.genetic.individual;

import java.util.List;

public interface IIndividual {
    double calculateFitness();

    List<Integer> getValues();
}
