package org.aksw.palmetto.calculations;

public abstract class AbstractUndefinedResultHandlingCoherenceCalculation implements CoherenceCalculation {

    private static final double DEFAULT_RESULT_FOR_UNDIFEND_CALCULATIONS = Double.NaN;

    protected double resultIfCalcUndefined;

    public AbstractUndefinedResultHandlingCoherenceCalculation() {
        resultIfCalcUndefined = DEFAULT_RESULT_FOR_UNDIFEND_CALCULATIONS;
    }

    public AbstractUndefinedResultHandlingCoherenceCalculation(double resultIfCalcUndefined) {
        this.resultIfCalcUndefined = resultIfCalcUndefined;
    }

    public double getResultIfCalcUndefined() {
        return resultIfCalcUndefined;
    }

    public void setResultIfCalcUndefined(double resultIfCalcUndefined) {
        this.resultIfCalcUndefined = resultIfCalcUndefined;
    }

    @Override
    public String getCalculationName() {
        if (Double.isNaN(resultIfCalcUndefined)) {
            return getName();
        } else {
            return getName() + "_(" + resultIfCalcUndefined + ")";
        }
    }

    protected abstract String getName();
}
