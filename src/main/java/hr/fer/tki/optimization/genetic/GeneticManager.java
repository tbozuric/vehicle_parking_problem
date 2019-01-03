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


    public void evaluate(int indexOfVehicle, int indexOfParkingLane) {
        //parkingSchedule.parkVehicle(vehicles.get(indexOfVehicle), parkingLanes.get(indexOfParkingLane));
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
                probabilities[i] = 1.0 / size;
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

    public IIndividual mutate(List<IMutation> mutation, IIndividual individual) {
        double p = Math.random();
        int size = mutation.size();

        if (size < 1) {
            throw new IllegalArgumentException("You must provide some mutation system!");
        }

        if (size > 1) {
            double[] probabilities = new double[size];
            for (int i = 0; i < size; i++) {
                probabilities[i] = 1.0 / size;
            }
        }


        return mutation.get(0).mutate(individual);
    }

    public double calculateFitness(IIndividual individual) {
        return individual.calculateFitness();
    }
}
