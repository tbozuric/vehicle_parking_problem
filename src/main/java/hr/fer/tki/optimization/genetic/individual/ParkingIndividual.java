package hr.fer.tki.optimization.genetic.individual;

import hr.fer.tki.function.GoalFunctionEvaluator;
import hr.fer.tki.models.Garage;
import hr.fer.tki.models.ParkingSchedule;
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
        ParkingSchedule parkingSchedule = garage.getParkingSchedule();
        int indexOfVehicle = 0;

        if (parkingSchedule.getNumberOfParkedVehicles() == 0) {


            for (Integer integer : values) {
                parkingSchedule.parkVehicle(parkingSchedule.getVehicles().get(indexOfVehicle++),
                        parkingSchedule.getParkingLanes().get(integer), true);
            }
        }

        ValidatorResult result = GarageValidator.validate(garage);

        if(result.isValid()){
            GoalFunctionEvaluator evaluator = new GoalFunctionEvaluator(garage);
            return 1.0 / (1 + result.getViolationPunishment()) *100000 + (evaluator.evaluateMaximizationProblem() -
            evaluator.evaluateMinimizationProblem());
        }

        return 1.0 / (1 + result.getViolationPunishment());
    }


    @Override
    public List<Integer> getValues() {
        return values;
    }

    public Garage getGarage() {
        return garage;
    }
}
