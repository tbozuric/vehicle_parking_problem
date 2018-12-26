package hr.fer.tki.optimization;

import hr.fer.tki.models.Garage;

public abstract class AbstractOptimizationAlgorithm implements OptimizationAlgorithm {

    protected Garage garage;

    public AbstractOptimizationAlgorithm(Garage garage) {
        this.garage = garage;
    }
}
