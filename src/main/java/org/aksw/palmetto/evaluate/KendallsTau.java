package org.aksw.palmetto.evaluate;


public class KendallsTau extends AbstractRankCorrelationCalculator {

    protected double calculateRankCorrelation(ValuePair[] pairs) {
        double concordance = 0, disconcordance = 0, boundInX = 0, boundInY = 0;
        double currentX, currentY;
        // Go through all pairs
        for (int i = 0; i < pairs.length; i++) {
            currentX = pairs[i].first;
            currentY = pairs[i].second;
            // Go through all following pairs and check the order of y
            for (int j = i + 1; j < pairs.length; j++) {
                if (pairs[j].first > currentX) {
                    if (pairs[j].second > currentY) {
                        ++concordance;
                    } else if (pairs[j].second < currentY) {
                        ++disconcordance;
                    } else {
                        ++boundInY;
                    }
                } else {
                    if (pairs[j].second != currentY) {
                        ++boundInX;
                    }
                    // else bound in X and Y, but we don't need this
                }
            }
        }

        return (concordance - disconcordance)
                / Math.sqrt((concordance + disconcordance + boundInX) * (concordance + disconcordance + boundInY));
    }
}
