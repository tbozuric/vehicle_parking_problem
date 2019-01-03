package hr.fer.tki.optimization.genetic.selection;

import hr.fer.tki.optimization.genetic.Individual;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.List;

public abstract class AbstractSelectionAlgorithm implements ISelection {
    protected List<Double> fitnessOfIndividuals;
    protected List<Individual> individuals;


    public AbstractSelectionAlgorithm(List<Double> fitnessOfIndividuals, List<Individual> individuals) {
        this.fitnessOfIndividuals = fitnessOfIndividuals;
        this.individuals = individuals;
    }
}
