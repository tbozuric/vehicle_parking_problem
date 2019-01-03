package hr.fer.tki.optimization.genetic.individual;

import hr.fer.tki.models.Garage;
import hr.fer.tki.validator.GarageValidator;
import hr.fer.tki.validator.ValidatorResult;

import java.util.List;

public class ParkingIndividual implements IIndividual {


    private List<Integer> values;
    private Garage garage;

    public ParkingIndividual(List<Integer> values, Garage garage) {
        this.values = values;
        this.garage = garage;
    }


    public void setValues(List<Integer> values) {
        this.values = values;
    }


    @Override
    public double calculateFitness() {
        double fitness;
        ValidatorResult result = GarageValidator.validate(garage);
        int numberOfPossibleViolations = GarageValidator.getNumberOfPossibleViolations();

        int award = (numberOfPossibleViolations - result.getViolatedRestrictions().size()) * 10_000;
        int penalty = Integer.MIN_VALUE;

        fitness = award + penalty;
        return fitness;
    }


    @Override
    public List<Integer> getValues() {
        return values;
    }
}
