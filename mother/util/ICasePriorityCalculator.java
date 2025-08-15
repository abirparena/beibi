package mother.util;

import mother.model.Case;

/**
 * Interface for calculating case priority
 * Demonstrates ABSTRACTION
 */
public interface ICasePriorityCalculator {
    double calculatePriority(Case caseObj);
}
