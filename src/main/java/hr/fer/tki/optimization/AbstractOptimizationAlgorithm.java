package hr.fer.tki.optimization;

import hr.fer.tki.models.Garage;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOptimizationAlgorithm implements OptimizationAlgorithm {

    protected Garage garage;
    protected List<OptimisationAlgorithmListener> listeners;
    protected String algorithmName;

    public AbstractOptimizationAlgorithm(Garage garage, String algorithmName) {
        this.garage = garage;
        this.algorithmName = algorithmName;
        listeners = new ArrayList<>();
        listeners.add(new ResultPrintListener(System.out));
    }

    public void notifyAlgorithmStarted() {
        for (OptimisationAlgorithmListener l : listeners) {
            l.algorithmStarted(algorithmName);
        }
    }

    public void notifyListenersAboutNewSolution(String header) {
        for (OptimisationAlgorithmListener l : listeners) {
            l.newSolutionFound(header, garage);
        }
    }

    public void notifyAlgorithmOver() {
        for (OptimisationAlgorithmListener l : listeners) {
            l.algorithmOver(algorithmName);
        }
    }
}
