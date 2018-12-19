package hr.fer.tki.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorResult {

    private boolean isValid;
    private List<String> violatedRestrictions;

    public ValidatorResult() {
        isValid = true;
        violatedRestrictions = new ArrayList<>();
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getViolatedRestrictions() {
        return violatedRestrictions;
    }

    public void addViolatedRestriction(String description) {
        isValid = false;
        violatedRestrictions.add(description);
    }
}
