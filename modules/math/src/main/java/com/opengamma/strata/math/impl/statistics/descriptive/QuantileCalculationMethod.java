/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.math.impl.statistics.descriptive;

import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.array.DoubleArray;

/**
 * Abstract method to estimate quantiles and expected shortfalls from sample observations.
 */
public abstract class QuantileCalculationMethod {

  /**
   * Compute the quantile estimation.
   * <p>
   * The quantile level is in decimal, i.e. 99% = 0.99 and 0 < level < 1 should be satisfied.
   * This is measured from the bottom, that is, the quantile estimation with the level 99% corresponds to 
   * the smallest 99% observations and 1% of the observation are above that level.
   * <p>
   * If index value computed from the level is outside of the sample data range, 
   * {@code IllegalArgumentException} is thrown. 
   * <p> 
   * The sample observations are supposed to be unsorted. The indices are based on the original, unsorted array.
   * Additionally, the indices start from 0 and so do not need to be shifted to account for java indexing, when using
   * them to reference the data points in the quantile calculation.
   * 
   * @param level  the quantile level
   * @param sample  the sample observations
   * @return The quantile estimation
   */
  public QuantileResult quantileFromUnsorted(double level, DoubleArray sample) {
    return quantile(level, sample, false);
  }

  /**
   * Compute the quantile estimation.
   * <p>
   * The quantile level is in decimal, i.e. 99% = 0.99 and 0 < level < 1 should be satisfied.
   * This is measured from the bottom, that is, the quantile estimation with the level 99% corresponds to 
   * the smallest 99% observations and 1% of the observation are above that level.
   * <p>
   * If index value computed from the level is outside of the sample data range, the nearest data point is used, i.e., 
   * quantile is computed with flat extrapolation.
   * <p> 
   * The sample observations are supposed to be unsorted. The indices are based on the original, unsorted array.
   * Additionally, the indices start from 0 and so do not have to be shifted to account for java indexing, when using
   * them to reference the data points in the quantile calculation..
   * 
   * @param level  the quantile level
   * @param sample  the sample observations
   * @return the quantile estimation
   */
  public QuantileResult quantileWithExtrapolationFromUnsorted(double level, DoubleArray sample) {
    return quantile(level, sample, true);
  }

  //-------------------------------------------------------------------------
  /**
   * Compute the expected shortfall.
   * <p>
   * The shortfall level is in decimal, i.e. 99% = 0.99 and 0 < level < 1 should be satisfied.
   * This is measured from the bottom, that is, the expected shortfall with the level 99% corresponds to 
   * the average of the smallest 99% of the observations.
   * <p>
   * If index value computed from the level is outside of the sample data range, the nearest data point is used, i.e., 
   * expected short fall is computed with flat extrapolation.
   * Thus this is coherent to {@link #quantileWithExtrapolationFromUnsorted(double, DoubleArray)}.
   * <p> 
   * The sample observations are supposed to be unsorted. The indices are based on the original, unsorted array.
   * Additionally, the indices start from 0 and so do not have to be shifted to account for java indexing, when using
   * them to reference the data points in the expected shortfall calculation.
   * 
   * @param level  the quantile level
   * @param sample  the sample observations
   * @return the quantile estimation
   */
  public QuantileResult expectedShortfallFromUnsorted(double level, DoubleArray sample) {
    return expectedShortfall(level, sample);
  }

  //-------------------------------------------------------------------------
  /**
   * Computed the quantile. 
   * <p>
   * This protected method should be implemented in subclasses. 
   * 
   * @param level  the quantile level
   * @param sorted  the sample observations
   * @param isExtrapolated  extrapolated if true, not extrapolated otherwise.
   * @return the quantile
   */
  protected abstract QuantileResult quantile(double level, DoubleArray sample, boolean isExtrapolated);

  /**
   * Computed the expected shortfall.
   * <p>
   * This protected method should be implemented in subclasses 
   * and coherent to {@link #quantile(double, DoubleArray, boolean)}.
   * 
   * @param level  the quantile level
   * @param sample  the sample observations
   * @return the expected shortfall
   */
  protected abstract QuantileResult expectedShortfall(double level, DoubleArray sample);

  /**
   * Check the index is within the sample data range. 
   * 
   * If the index is outside the data range, the nearest data point is used in case of {@code isExtrapolated == true} or 
   * an exception is thrown in case of {@code isExtrapolated == false}.  
   * 
   * @param index  the index
   * @param size  the sample size
   * @param isExtrapolated  extrapolated if true, not extrapolated otherwise
   * @return the index
   */
  protected double checkIndex(double index, int size, boolean isExtrapolated) {
    if (isExtrapolated) {
      return Math.min(Math.max(index, 1), size);
    }
    ArgChecker.isTrue(index >= 1, "Quantile can not be computed below the lowest probability level.");
    ArgChecker.isTrue(index <= size, "Quantile can not be computed above the highest probability level.");
    return index;
  }
}
