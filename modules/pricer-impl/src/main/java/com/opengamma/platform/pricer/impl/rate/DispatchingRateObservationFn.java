/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.platform.pricer.impl.rate;

import java.time.LocalDate;

import com.opengamma.platform.finance.rate.FixedRateObservation;
import com.opengamma.platform.finance.rate.IborAveragedRateObservation;
import com.opengamma.platform.finance.rate.IborInterpolatedRateObservation;
import com.opengamma.platform.finance.rate.IborRateObservation;
import com.opengamma.platform.finance.rate.OvernightAveragedRateObservation;
import com.opengamma.platform.finance.rate.OvernightCompoundedRateObservation;
import com.opengamma.platform.finance.rate.RateObservation;
import com.opengamma.platform.pricer.PricingEnvironment;
import com.opengamma.platform.pricer.rate.RateObservationFn;
import com.opengamma.platform.pricer.sensitivity.PointSensitivityBuilder;
import com.opengamma.strata.collect.ArgChecker;

/**
 * Rate observation implementation using multiple dispatch.
 * <p>
 * Dispatches the request to the correct implementation.
 */
public class DispatchingRateObservationFn
    implements RateObservationFn<RateObservation> {

  /**
   * Default implementation.
   */
  public static final DispatchingRateObservationFn DEFAULT = new DispatchingRateObservationFn(
      ForwardIborRateObservationFn.DEFAULT,
      ForwardIborInterpolatedRateObservationFn.DEFAULT,
      ForwardIborAveragedRateObservationFn.DEFAULT,
      ForwardOvernightCompoundedRateObservationFn.DEFAULT,
      ApproxForwardOvernightAveragedRateObservationFn.DEFAULT);

  /**
   * Rate provider for {@link IborRateObservation}.
   */
  private final RateObservationFn<IborRateObservation> iborRateObservationFn;
  /**
   * Rate provider for {@link IborInterpolatedRateObservation}.
   */
  private final RateObservationFn<IborInterpolatedRateObservation> iborInterpolatedRateObservationFn;
  /**
   * Rate provider for {@link IborAveragedRateObservation}.
   */
  private final RateObservationFn<IborAveragedRateObservation> iborAveragedRateObservationFn;
  /**
   * Rate provider for {@link OvernightCompoundedRateObservation}.
   */
  private final RateObservationFn<OvernightCompoundedRateObservation> overnightCompoundedRateObservationFn;
  /**
   * Rate provider for {@link OvernightAveragedRateObservation}.
   */
  private final RateObservationFn<OvernightAveragedRateObservation> overnightAveragedRateObservationFn;

  /**
   * Creates an instance.
   *
   * @param iborRateObservationFn  the rate provider for {@link IborRateObservation}
   * @param iborInterpolatedRateObservationFn  the rate observation for {@link IborInterpolatedRateObservation}
   * @param iborAveragedRateObservationFn  the rate observation for {@link IborAveragedRateObservation}
   * @param overnightCompoundedRateObservationFn  the rate observation for {@link OvernightCompoundedRateObservation}
   * @param overnightAveragedRateObservationFn  the rate observation for {@link OvernightAveragedRateObservation}
   */
  public DispatchingRateObservationFn(
      RateObservationFn<IborRateObservation> iborRateObservationFn,
      RateObservationFn<IborInterpolatedRateObservation> iborInterpolatedRateObservationFn,
      RateObservationFn<IborAveragedRateObservation> iborAveragedRateObservationFn,
      RateObservationFn<OvernightCompoundedRateObservation> overnightCompoundedRateObservationFn,
      RateObservationFn<OvernightAveragedRateObservation> overnightAveragedRateObservationFn) {

    this.iborRateObservationFn =
        ArgChecker.notNull(iborRateObservationFn, "iborRateObservationFn");
    this.iborInterpolatedRateObservationFn =
        ArgChecker.notNull(iborInterpolatedRateObservationFn, "iborInterpolatedRateObservationFn");
    this.iborAveragedRateObservationFn =
        ArgChecker.notNull(iborAveragedRateObservationFn, "iborAverageRateObservationFn");
    this.overnightCompoundedRateObservationFn =
        ArgChecker.notNull(overnightCompoundedRateObservationFn, "overnightCompoundedRateObservationFn");
    this.overnightAveragedRateObservationFn =
        ArgChecker.notNull(overnightAveragedRateObservationFn, "overnightAveragedRateObservationFn");
  }

  //-------------------------------------------------------------------------
  @Override
  public double rate(
      PricingEnvironment env,
      RateObservation observation,
      LocalDate startDate,
      LocalDate endDate) {

    // dispatch by runtime type
    if (observation instanceof FixedRateObservation) {
      // inline code (performance) avoiding need for FixedRateObservationFn implementation
      return ((FixedRateObservation) observation).getRate();
    } else if (observation instanceof IborRateObservation) {
      return iborRateObservationFn.rate(
          env, (IborRateObservation) observation, startDate, endDate);
    } else if (observation instanceof IborInterpolatedRateObservation) {
      return iborInterpolatedRateObservationFn.rate(
          env, (IborInterpolatedRateObservation) observation, startDate, endDate);
    } else if (observation instanceof IborAveragedRateObservation) {
      return iborAveragedRateObservationFn.rate(
          env, (IborAveragedRateObservation) observation, startDate, endDate);
    } else if (observation instanceof OvernightAveragedRateObservation) {
      return overnightAveragedRateObservationFn.rate(
          env, (OvernightAveragedRateObservation) observation, startDate, endDate);
    } else if (observation instanceof OvernightCompoundedRateObservation) {
      return overnightCompoundedRateObservationFn.rate(
          env, (OvernightCompoundedRateObservation) observation, startDate, endDate);
    } else {
      throw new IllegalArgumentException("Unknown Rate type: " + observation.getClass().getSimpleName());
    }
  }

  @Override
  public PointSensitivityBuilder rateSensitivity(
      PricingEnvironment env,
      RateObservation observation,
      LocalDate startDate,
      LocalDate endDate) {

    // dispatch by runtime type
    if (observation instanceof FixedRateObservation) {
      // inline code (performance) avoiding need for FixedRateObservationFn implementation
      return PointSensitivityBuilder.none();
    } else if (observation instanceof IborRateObservation) {
      return iborRateObservationFn.rateSensitivity(
          env, (IborRateObservation) observation, startDate, endDate);
    } else if (observation instanceof IborInterpolatedRateObservation) {
      return iborInterpolatedRateObservationFn.rateSensitivity(
          env, (IborInterpolatedRateObservation) observation, startDate, endDate);
    } else if (observation instanceof IborAveragedRateObservation) {
      return iborAveragedRateObservationFn.rateSensitivity(
          env, (IborAveragedRateObservation) observation, startDate, endDate);
    } else if (observation instanceof OvernightAveragedRateObservation) {
      return overnightAveragedRateObservationFn.rateSensitivity(
          env, (OvernightAveragedRateObservation) observation, startDate, endDate);
    } else if (observation instanceof OvernightCompoundedRateObservation) {
      return overnightCompoundedRateObservationFn.rateSensitivity(
          env, (OvernightCompoundedRateObservation) observation, startDate, endDate);
    } else {
      throw new IllegalArgumentException("Unknown Rate type: " + observation.getClass().getSimpleName());
    }
  }

}