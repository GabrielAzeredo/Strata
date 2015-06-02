/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.market.value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.market.curve.Curve;
import com.opengamma.strata.market.curve.CurveName;
import com.opengamma.strata.market.curve.InterpolatedNodalCurve;
import com.opengamma.strata.market.sensitivity.PointSensitivityBuilder;
import com.opengamma.strata.market.sensitivity.ZeroRateSensitivity;

/**
 * Provides access to discount factors for a currency based on a zero rate curve.
 * <p>
 * This provides discount factors for a single currency.
 * <p>
 * This implementation is based on an underlying curve that is stored with maturities
 * and zero-coupon continuously-compounded rates.
 */
@BeanDefinition(builderScope = "private")
public final class ZeroRateDiscountFactors
    implements DiscountFactors, ImmutableBean, Serializable {

  /**
   * The currency that the discount factors are for.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final Currency currency;
  /**
   * The valuation date.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final LocalDate valuationDate;
  /**
   * The day count convention of the curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final DayCount dayCount;
  /**
   * The underlying curve.
   */
  @PropertyDefinition(validate = "notNull")
  private final Curve curve;

  //-------------------------------------------------------------------------
  /**
   * Creates a new discount factors instance.
   * <p>
   * The curve is specified by an instance of {@link Curve}, such as {@link InterpolatedNodalCurve}.
   * The curve is stored with maturities and zero-coupon continuously-compounded rates.
   * 
   * @param currency  the currency
   * @param valuationDate  the valuation date for which the curve is valid
   * @param dayCount  the day count
   * @param underlyingCurve  the underlying curve
   * @return the curve
   */
  public static ZeroRateDiscountFactors of(
      Currency currency,
      LocalDate valuationDate,
      DayCount dayCount,
      Curve underlyingCurve) {

    return new ZeroRateDiscountFactors(currency, valuationDate, dayCount, underlyingCurve);
  }

  //-------------------------------------------------------------------------
  @Override
  public CurveName getCurveName() {
    return curve.getName();
  }

  @Override
  public int getParameterCount() {
    return curve.getParameterCount();
  }

  //-------------------------------------------------------------------------
  @Override
  public double discountFactor(LocalDate date) {
    return discountFactor(relativeTime(date));
  }

  // calculates the discount factor at a given time
  private double discountFactor(double relativeTime) {
    // short cut rate lookup
    if (relativeTime == 0) {
      return 1.0;
    }
    return Math.exp(-relativeTime * curve.yValue(relativeTime));
  }

  // calculate the relative time between the valuation date and the specified date
  private double relativeTime(LocalDate date) {
    return dayCount.relativeYearFraction(valuationDate, date);
  }

  //-------------------------------------------------------------------------
  @Override
  public PointSensitivityBuilder pointSensitivity(LocalDate date, Currency sensitivityCurrency) {
    double relativeTime = relativeTime(date);
    double discountFactor = discountFactor(relativeTime);
    return ZeroRateSensitivity.of(currency, sensitivityCurrency, date, -discountFactor * relativeTime);
  }

  @Override
  public double[] unitParameterSensitivity(LocalDate date) {
    double relativeTime = relativeTime(date);
    return curve.yValueParameterSensitivity(relativeTime);
  }

  //-------------------------------------------------------------------------
  /**
   * Returns a new instance with a different curve.
   * 
   * @param curve  the new curve
   * @return the new instance
   */
  public ZeroRateDiscountFactors withCurve(Curve curve) {
    return new ZeroRateDiscountFactors(currency, valuationDate, dayCount, curve);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code ZeroRateDiscountFactors}.
   * @return the meta-bean, not null
   */
  public static ZeroRateDiscountFactors.Meta meta() {
    return ZeroRateDiscountFactors.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(ZeroRateDiscountFactors.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private ZeroRateDiscountFactors(
      Currency currency,
      LocalDate valuationDate,
      DayCount dayCount,
      Curve curve) {
    JodaBeanUtils.notNull(currency, "currency");
    JodaBeanUtils.notNull(valuationDate, "valuationDate");
    JodaBeanUtils.notNull(dayCount, "dayCount");
    JodaBeanUtils.notNull(curve, "curve");
    this.currency = currency;
    this.valuationDate = valuationDate;
    this.dayCount = dayCount;
    this.curve = curve;
  }

  @Override
  public ZeroRateDiscountFactors.Meta metaBean() {
    return ZeroRateDiscountFactors.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency that the discount factors are for.
   * @return the value of the property, not null
   */
  @Override
  public Currency getCurrency() {
    return currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the valuation date.
   * @return the value of the property, not null
   */
  @Override
  public LocalDate getValuationDate() {
    return valuationDate;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the day count convention of the curve.
   * @return the value of the property, not null
   */
  public DayCount getDayCount() {
    return dayCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying curve.
   * @return the value of the property, not null
   */
  public Curve getCurve() {
    return curve;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ZeroRateDiscountFactors other = (ZeroRateDiscountFactors) obj;
      return JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getValuationDate(), other.getValuationDate()) &&
          JodaBeanUtils.equal(getDayCount(), other.getDayCount()) &&
          JodaBeanUtils.equal(getCurve(), other.getCurve());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getValuationDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(getDayCount());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurve());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("ZeroRateDiscountFactors{");
    buf.append("currency").append('=').append(getCurrency()).append(',').append(' ');
    buf.append("valuationDate").append('=').append(getValuationDate()).append(',').append(' ');
    buf.append("dayCount").append('=').append(getDayCount()).append(',').append(' ');
    buf.append("curve").append('=').append(JodaBeanUtils.toString(getCurve()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ZeroRateDiscountFactors}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofImmutable(
        this, "currency", ZeroRateDiscountFactors.class, Currency.class);
    /**
     * The meta-property for the {@code valuationDate} property.
     */
    private final MetaProperty<LocalDate> valuationDate = DirectMetaProperty.ofImmutable(
        this, "valuationDate", ZeroRateDiscountFactors.class, LocalDate.class);
    /**
     * The meta-property for the {@code dayCount} property.
     */
    private final MetaProperty<DayCount> dayCount = DirectMetaProperty.ofImmutable(
        this, "dayCount", ZeroRateDiscountFactors.class, DayCount.class);
    /**
     * The meta-property for the {@code curve} property.
     */
    private final MetaProperty<Curve> curve = DirectMetaProperty.ofImmutable(
        this, "curve", ZeroRateDiscountFactors.class, Curve.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "currency",
        "valuationDate",
        "dayCount",
        "curve");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return currency;
        case 113107279:  // valuationDate
          return valuationDate;
        case 1905311443:  // dayCount
          return dayCount;
        case 95027439:  // curve
          return curve;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends ZeroRateDiscountFactors> builder() {
      return new ZeroRateDiscountFactors.Builder();
    }

    @Override
    public Class<? extends ZeroRateDiscountFactors> beanType() {
      return ZeroRateDiscountFactors.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    /**
     * The meta-property for the {@code valuationDate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<LocalDate> valuationDate() {
      return valuationDate;
    }

    /**
     * The meta-property for the {@code dayCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<DayCount> dayCount() {
      return dayCount;
    }

    /**
     * The meta-property for the {@code curve} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Curve> curve() {
      return curve;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((ZeroRateDiscountFactors) bean).getCurrency();
        case 113107279:  // valuationDate
          return ((ZeroRateDiscountFactors) bean).getValuationDate();
        case 1905311443:  // dayCount
          return ((ZeroRateDiscountFactors) bean).getDayCount();
        case 95027439:  // curve
          return ((ZeroRateDiscountFactors) bean).getCurve();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code ZeroRateDiscountFactors}.
   */
  private static final class Builder extends DirectFieldsBeanBuilder<ZeroRateDiscountFactors> {

    private Currency currency;
    private LocalDate valuationDate;
    private DayCount dayCount;
    private Curve curve;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return currency;
        case 113107279:  // valuationDate
          return valuationDate;
        case 1905311443:  // dayCount
          return dayCount;
        case 95027439:  // curve
          return curve;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          this.currency = (Currency) newValue;
          break;
        case 113107279:  // valuationDate
          this.valuationDate = (LocalDate) newValue;
          break;
        case 1905311443:  // dayCount
          this.dayCount = (DayCount) newValue;
          break;
        case 95027439:  // curve
          this.curve = (Curve) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public ZeroRateDiscountFactors build() {
      return new ZeroRateDiscountFactors(
          currency,
          valuationDate,
          dayCount,
          curve);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("ZeroRateDiscountFactors.Builder{");
      buf.append("currency").append('=').append(JodaBeanUtils.toString(currency)).append(',').append(' ');
      buf.append("valuationDate").append('=').append(JodaBeanUtils.toString(valuationDate)).append(',').append(' ');
      buf.append("dayCount").append('=').append(JodaBeanUtils.toString(dayCount)).append(',').append(' ');
      buf.append("curve").append('=').append(JodaBeanUtils.toString(curve));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}