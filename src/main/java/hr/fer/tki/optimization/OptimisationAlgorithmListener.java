package hr.fer.tki.optimization;

import hr.fer.tki.models.Garage;

public interface OptimisationAlgorithmListener {

    void algorithmStarted(String algorithmName);

    void newSolutionFound(String header, Garage currentSolution);

    void algorithmOver(String algorithmName);
}
