package hr.fer.tki.optimization.genetic;

import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.List;

public abstract class GeneticAlgorithm implements IGeneticAlgorithm {

     GeneticManager manager;
     ISelection selection;
     List<IMutation> mutations;
     List<ICrossover> crossovers;
     int numberOfEvaluations;
     private double errorMinimum;
     double probabilityOfMutation;


    public GeneticAlgorithm(GeneticManager manager , ISelection selection, List<IMutation> mutations,
                            List<ICrossover> crossovers, int numberOfEvaluations, double errorMinimum,
                            double probabilityOfMutation) {
        this.manager = manager;
        this.selection = selection;
        this.mutations = mutations;
        this.crossovers = crossovers;
        this.numberOfEvaluations = numberOfEvaluations;
        this.errorMinimum = errorMinimum;
        this.probabilityOfMutation = probabilityOfMutation;
    }


}
