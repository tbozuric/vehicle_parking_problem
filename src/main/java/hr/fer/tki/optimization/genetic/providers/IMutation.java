package hr.fer.tki.optimization.genetic.providers;


import hr.fer.tki.optimization.genetic.individual.IIndividual;

public interface IMutation {
    IIndividual mutate(IIndividual individual);
}
