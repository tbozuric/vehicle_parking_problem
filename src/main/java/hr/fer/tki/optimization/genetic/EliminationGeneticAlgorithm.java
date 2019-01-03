package hr.fer.tki.optimization.genetic;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.providers.ISelection;

import java.util.List;

public class EliminationGeneticAlgorithm extends GeneticAlgorithm {


    public EliminationGeneticAlgorithm(GeneticManager manager, ISelection selection,
                                       List<IMutation> mutations, List<ICrossover> crossovers,
                                       int numberOfEvaluations, double errorMinimum, double probabilityOfMutation) {
        super(manager, selection, mutations, crossovers, numberOfEvaluations, errorMinimum, probabilityOfMutation);
    }

    @Override
    public IIndividual search() {

        int iteration = 0;

        List<IIndividual> population = manager.getInitialPopulation();
        List<Double> fitness = manager.getFitnessOfPopulation(population);
        int bestIndex = indexOfMaxElement(fitness);

        while (iteration < numberOfEvaluations) {
            iteration++;

            if (iteration % 10 == 0) {
                System.out.println("Iteration : " + iteration + ", Best individual: " + fitness.get(bestIndex));
            }

            if (iteration >= numberOfEvaluations || fitness.get(bestIndex) >= 60000) {
                System.out.println("Iteration : " + iteration + ", Best individual: " + fitness.get(bestIndex));
                return population.get(bestIndex);
            }


            int numberOfSelectedIndividuals = manager.getSelectionSize();

            List<IIndividual> selection = this.selection.select(numberOfSelectedIndividuals);
            if (numberOfSelectedIndividuals > 2) {
                IIndividual firstParent = selection.get(0);
                IIndividual secondParent = selection.get(1);
                IIndividual worstIndividual = selection.get(numberOfSelectedIndividuals - 1);

                IIndividual crossover = manager.crossover(this.crossovers, firstParent, secondParent);
                IIndividual mutated = manager.mutate(this.mutations, crossover);

                int index = population.indexOf(worstIndividual);
                population.remove(index);
                fitness.remove(index);

                population.add(mutated);
                double fitnessOfMutatedIndividual = manager.calculateFitness(mutated);
                fitness.add(fitnessOfMutatedIndividual);

                if (fitnessOfMutatedIndividual > fitness.get(bestIndex)) {
                    bestIndex = fitness.size() - 1;
                }
            }

        }
        return population.get(bestIndex);
    }

    private int indexOfMaxElement(List<Double> fitness) {
        int maxAt = 0;
        int size = fitness.size();
        for (int i = 0; i < size; i++) {
            maxAt = fitness.get(i) > fitness.get(maxAt) ? i : maxAt;
        }
        return maxAt;
    }
}
