package hr.fer.tki.optimization.genetic;

import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.List;

public abstract class GeneticAlgorithm implements IGeneticAlgorithm {
    private ISelection selection;
    private List<IMutation> mutations;
    private List<ICrossover> crossovers;
    private int numberOfEvaluations;
    private double errorMinimum;
    private double probabilityOfMutation;

    public GeneticAlgorithm(ISelection selection, List<IMutation> mutations,
                            List<ICrossover> crossovers, int numberOfEvaluations, double errorMinimum,
                            double probabilityOfMutation) {
        this.selection = selection;
        this.mutations = mutations;
        this.crossovers = crossovers;
        this.numberOfEvaluations = numberOfEvaluations;
        this.errorMinimum = errorMinimum;
        this.probabilityOfMutation = probabilityOfMutation;
    }


}
