/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2024 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicator;
import org.ta4j.core.num.Num;

/**
 * Change indicator aka. Momentum indicator.
 *
 * Compared to the rate of change (ROC) indicator, this indicator calculates the
 * absolute change value, whereas the ROC indicator calculates the relative
 * change (percentage).
 */
public class ChangeIndicator extends AbstractIndicator<Num> {
    private final Indicator<Num> changeIndicator;
    private final Indicator<Num> indicator;
    private final int barCount;

    public ChangeIndicator(BarSeries series) {
        this(series, 1);
    }

    public ChangeIndicator(Indicator<Num> indicator) {
        this(indicator, 1);
    }

    public ChangeIndicator(BarSeries series, int barCount) {
        this(new ClosePriceIndicator(series), barCount);
    }

    public ChangeIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator.getBarSeries());

        this.changeIndicator = CombineIndicator.minus(indicator, new PreviousValueIndicator(indicator, barCount));
        this.indicator = indicator;
        this.barCount = barCount;
    }

    @Override
    public Num getValue(int index) {
        if (index < barCount) {
            return getBarSeries().numFactory().zero();
        }

        return changeIndicator.getValue(index);
    }

    @Override
    public int getUnstableBars() {
        return Math.max(barCount, indicator.getUnstableBars());
    }
}