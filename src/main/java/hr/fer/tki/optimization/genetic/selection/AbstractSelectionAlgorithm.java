package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.List;

public abstract class AbstractSelectionAlgorithm implements ISelection {
    protected List<Double> fitnessOfIndividuals;
    protected List<IIndividual> individuals;


    public AbstractSelectionAlgorithm(List<Double> fitnessOfIndividuals, List<IIndividual> individuals) {
        this.fitnessOfIndividuals = fitnessOfIndividuals;
        this.individuals = individuals;
    }
}
