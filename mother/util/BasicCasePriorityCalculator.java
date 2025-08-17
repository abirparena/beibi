package mother.util;

import mother.model.Case;


public class BasicCasePriorityCalculator implements ICasePriorityCalculator {
    @Override
    public double calculatePriority(Case caseObj) {
        try {
            int age = Integer.parseInt(caseObj.getVictimAge());
            if (age < 18) return 1.0; // High priority for minors
            if (age > 60) return 0.8; // Medium priority for seniors
            return 0.5; // Normal priority
        } catch (NumberFormatException e) {
            return 0.5; // Default if age parsing fails
        }
    }
}