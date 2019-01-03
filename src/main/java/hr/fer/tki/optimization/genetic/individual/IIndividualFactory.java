package hr.fer.tki.optimization.genetic.individual;

import java.util.List;

public interface IIndividualFactory {
    List<IIndividual> createPopulation(int sizeOfPopulation);

    IIndividual createIndividual(List<Integer> values);

}
