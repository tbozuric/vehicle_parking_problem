package hr.fer.tki.optimization.genetic;

import hr.fer.tki.optimization.genetic.individual.IIndividual;
import hr.fer.tki.optimization.genetic.individual.IIndividualFactory;
import hr.fer.tki.optimization.genetic.providers.ICrossover;
import hr.fer.tki.optimization.genetic.providers.IMutation;

import java.util.ArrayList;
import java.util.List;

public class GeneticManager {
    private static GeneticManager manager;
    private IIndividualFactory factory;
    private int sizeOfPopulation;
    private int selectionSize;

    private GeneticManager(IIndividualFactory factory, int sizeOfPopulation, int selectionSize) {
        this.factory = factory;
        this.sizeOfPopulation = sizeOfPopulation;
        this.selectionSize = selectionSize;
    }


    public static GeneticManager getManager(IIndividualFactory factory, int sizeOfPopulation,
                                            int selectionSize) {
        if (manager == null) {
            manager = new GeneticManager(factory, sizeOfPopulation, selectionSize);
        }
        return manager;
    }

    public List<IIndividual> getInitialPopulation() {
        return factory.createPopulation(sizeOfPopulation);
    }

    public List<Double> getFitnessOfPopulation(List<IIndividual> population) {
        List<Double> fitness = new ArrayList<>(population.size());

        for (IIndividual individual : population) {
            fitness.add(individual.calculateFitness());
        }

        return fitness;
    }

    public int getSelectionSize() {
        return selectionSize;
    }

    public void setSelectionSize(int selectionSize) {
        this.selectionSize = selectionSize;
    }

    public IIndividual crossover(List<ICrossover> crossovers, IIndividual parentFirst, IIndividual parentSecond) {

        double p = Math.random();
        int size = crossovers.size();

        if (size < 1) {
            throw new IllegalArgumentException("You must provide some crossover system!");
        }

        if (size > 1) {
            double[] probabilities = new double[size];
            for (int i = 0; i < size; i++) {
                probabilities[i] = 1.0 / (double) size;
            }
            double cumulativeProbability = 0.0;
            for (int i = 0; i < size; i++) {
                cumulativeProbability += probabilities[i];
                if (p <= cumulativeProbability) {
                    return crossovers.get(i).crossover(parentFirst, parentSecond);
                }
            }
        }
        return crossovers.get(0).crossover(parentFirst, parentSecond);
    }

    public IIndividual mutate(List<IMutation> mutations, IIndividual individual, double probabilityOfMutation) {
        double p = Math.random();
        int size = mutations.size();

        if (size < 1) {
            throw new IllegalArgumentException("You must provide some mutation system!");
        }

        if (size > 1) {
            double[] probabilities = new double[size];
            probabilities[0] = 0.5 / size;
            //najmanja vjerojanost je suffle mutacije jer je to nasumicno pretrazivanje
            double difference = (1 - 0.5) / (double) (size - 1);

            for (int i = 1; i < size; i++) {
                probabilities[i] = (1.0 + difference) / (double) size;
            }
//            double[] probabilities = new double[size];
//            for (int i = 0; i < size; i++) {
//                probabilities[i] = 1.0 / (double) size;
//            }

            double cumulativeProbability = 0.0;
            for (int i = 0; i < size; i++) {
                cumulativeProbability += probabilities[i];
                if (p <= cumulativeProbability) {
                    return mutations.get(i).mutate(individual, probabilityOfMutation);
                }
            }
        }
        return mutations.get(0).mutate(individual, probabilityOfMutation);
    }

    public double calculateFitness(IIndividual individual) {
        return individual.calculateFitness();
    }
}
