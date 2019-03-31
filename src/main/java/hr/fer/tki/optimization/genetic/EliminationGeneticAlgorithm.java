package hr.fer.tki.optimization.genetic;

import hr.fer.tki.models.Garage;
import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.ParkingIndividual;
import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;
import hr.fer.tki.optimization.genetic.providers.ISelection;
import hr.fer.tki.optimization.taboo.TabooSearch;
import hr.fer.tki.output.GarageOutputWriter;
import hr.fer.tki.validator.GarageValidator;

import java.io.FileNotFoundException;
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

            if (iteration % 100000 == 0) {
                System.out.println("Iteration : " + iteration + ", Best individual: " + fitness.get(bestIndex));
            }

            Garage garage = ((ParkingIndividual) population.get(bestIndex)).getGarage();

            if (iteration % 10_000_000 == 0) {
                localSearch(population, bestIndex);
                IIndividual newIndividual = new ParkingIndividual(garage.getParkingSchedule().getParkedVehiclesOnLanesAsArray(),
                        garage);
                population.set(bestIndex, newIndividual);
                fitness.set(bestIndex, manager.calculateFitness(newIndividual));
            }

            if (GarageValidator.validate(garage).isValid()) {
                localSearch(population, bestIndex);
                return population.get(bestIndex);

                //if (iteration >= numberOfEvaluations) {
                //    localSearch(population, bestIndex);
                //    return population.get(bestIndex);
                //}
            }

            int numberOfSelectedIndividuals = manager.getSelectionSize();

            List<IIndividual> selection = this.selection.select(numberOfSelectedIndividuals, fitness, population);
            if (numberOfSelectedIndividuals > 2) {
                IIndividual firstParent = selection.get(0);
                IIndividual secondParent = selection.get(1);
                IIndividual worstIndividual = selection.get(numberOfSelectedIndividuals - 1);

                IIndividual crossover = manager.crossover(this.crossovers, firstParent, secondParent);
                IIndividual mutated = manager.mutate(this.mutations, crossover, probabilityOfMutation);

                int index = population.indexOf(worstIndividual);

                double currentBestFitness = fitness.get(bestIndex);

                if (index < bestIndex) {
                    bestIndex -= 1;
                }


                double fitnessOfMutatedIndividual = manager.calculateFitness(mutated);

                population.remove(index);
                fitness.remove(index);

                population.add(mutated);
                fitness.add(fitnessOfMutatedIndividual);


                if (fitnessOfMutatedIndividual > currentBestFitness && index != bestIndex) {
                    bestIndex = fitness.size() - 1;
                }
            }

        }
        return population.get(bestIndex);
    }

    private void localSearch(List<IIndividual> population, int bestIndex) {
        Garage garage = ((ParkingIndividual) population.get(bestIndex)).getGarage();
        TabooSearch tabooSearch = new TabooSearch(garage);
        tabooSearch.parkVehiclesInTheGarage();
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
