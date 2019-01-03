package hr.fer.tki.optimization.genetic;

import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.List;

public class EliminationGeneticAlgorithm extends GeneticAlgorithm {


    public EliminationGeneticAlgorithm(ISelection selection, List<IMutation> mutations,
                                       List<ICrossover> crossovers, int numberOfEvaluations, double errorMinimum) {
        super(selection, mutations, crossovers, numberOfEvaluations, errorMinimum);
    }

    @Override
    public Individual search() {
        return null;
    }
}
