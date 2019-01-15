package hr.fer.tki.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorResult {

    private boolean isValid;
    private List<String> violatedRestrictions;
    private double violationPunishment;

    public ValidatorResult() {
        isValid = true;
        violationPunishment = 0;
        violatedRestrictions = new ArrayList<>();
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getViolatedRestrictions() {
        return violatedRestrictions;
    }

    public double getViolationPunishment() {
        return violationPunishment;
    }

    public void addViolatedRestriction(String description, double violationPunishment) {
        isValid = false;
        this.violationPunishment += violationPunishment;
        violatedRestrictions.add(description);
    }
}
