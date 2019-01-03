package hr.fer.tki.optimization.genetic;

import hr.fer.tki.models.Garage;
import hr.fer.tki.validator.GarageValidator;
import hr.fer.tki.validator.ValidatorResult;

import java.util.List;

public class Individual {
    private List<Integer> values;

    public Individual(List<Integer> values) {
        this.values = values;
    }

    public double calculateFitness(Garage garage) {
        double fitness = 0.0;
        ValidatorResult result = GarageValidator.validate(garage);
        int numberOfPossibleViolations = GarageValidator.getNumberOfPossibleViolations();

        int award = (numberOfPossibleViolations - result.getViolatedRestrictions().size()) * 10_000;
        int penalty = Integer.MIN_VALUE;

        fitness = award + penalty;
        return fitness;
    }


    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
