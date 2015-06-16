/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 * <p>
 * Please see distribution for license.
 */
package com.opengamma.strata.finance.credit;

import com.opengamma.strata.basics.BuySell;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.date.HolidayCalendar;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.opengamma.strata.finance.credit.fee.FeeLeg;
import com.opengamma.strata.finance.credit.general.GeneralTerms;
import org.joda.beans.Bean;
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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A credit default swap (single name and index).
 * <p>
 * In a credit default swap one party (the protection seller) agrees to compensate another party
 * (the protection buyer) if a specified company or Sovereign (the reference entity)
 * experiences a credit event, indicating it is or may be unable to service its debts.
 * The protection seller is typically paid a fee and/or premium, expressed as an annualized
 * percent of the notional in basis points, regularly over the life of the transaction or
 * otherwise as agreed by the parties.
 */
@BeanDefinition
public final class Cds
    implements CdsProduct, ImmutableBean, Serializable {

  /**
   * This element contains all the data that appears in the section entitled "1. General Terms"
   * in the 2003 ISDA Credit Derivatives Confirmation
   */
  @PropertyDefinition(validate = "notNull")
  private final GeneralTerms generalTerms;

  /**
   * This element contains all the terms relevant to defining the fixed amounts/payments per
   * the applicable ISDA definitions.
   */
  @PropertyDefinition(validate = "notNull")
  private final FeeLeg feeLeg;

  /**
   * Value that the ISDA Standard model needs but that does not occur in the FpML
   */
  @PropertyDefinition(validate = "notNull")
  private final boolean payAccOnDefault;

  @Override
  public ExpandedCds expand() {
    BusinessDayConvention businessdayAdjustmentConvention = getGeneralTerms().getBusinessDayAdjustment().getConvention();
    HolidayCalendar calendar = getGeneralTerms().getBusinessDayAdjustment().getCalendar();
    BusinessDayAdjustment businessDayAdjustment = getGeneralTerms().getBusinessDayAdjustment();
    LocalDate accStartDate = businessDayAdjustment.adjust(
        getGeneralTerms().getStartDate()
    );
    LocalDate endDate = getGeneralTerms().getEndDate();
    boolean payAccOnDefault = isPayAccOnDefault();
    Period paymentInterval = getFeeLeg().getPeriodicPayments().getPaymentFrequency().getPeriod();
    StubConvention stubConvention = getFeeLeg().getPeriodicPayments().getStubConvention();
    DayCount accrualDayCount = getFeeLeg().getPeriodicPayments().getDayCount();
    BuySell buySellProtection = getGeneralTerms().getBuySellProtection();
    double upfrontFeeAmount = getFeeLeg().getUpfrontFee().getFixedAmount().getAmount();
    LocalDate upfrontFeePaymentDate = getFeeLeg().getUpfrontFee().getPaymentDate();
    double coupon = getFeeLeg().getPeriodicPayments().getCoupon();
    double notional = getFeeLeg().getPeriodicPayments().getNotional().getAmount();
    Currency currency = getFeeLeg().getPeriodicPayments().getNotional().getCurrency();

    return ExpandedCds
        .builder()
        .accStartDate(accStartDate)
        .endDate(endDate)
        .payAccOnDefault(payAccOnDefault)
        .paymentInterval(paymentInterval)
        .stubConvention(stubConvention)
        .businessdayAdjustmentConvention(businessdayAdjustmentConvention)
        .calendar(calendar)
        .accrualDayCount(accrualDayCount)
        .buySellProtection(buySellProtection)
        .accrualDayCount(accrualDayCount)
        .upfrontFeeAmount(upfrontFeeAmount)
        .upfrontFeePaymentDate(upfrontFeePaymentDate)
        .coupon(coupon)
        .notional(notional)
        .currency(currency)
        .build();

  }


  // TODO add validation that notional currency match the fee currency

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code Cds}.
   * @return the meta-bean, not null
   */
  public static Cds.Meta meta() {
    return Cds.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(Cds.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static Cds.Builder builder() {
    return new Cds.Builder();
  }

  private Cds(
      GeneralTerms generalTerms,
      FeeLeg feeLeg,
      boolean payAccOnDefault) {
    JodaBeanUtils.notNull(generalTerms, "generalTerms");
    JodaBeanUtils.notNull(feeLeg, "feeLeg");
    JodaBeanUtils.notNull(payAccOnDefault, "payAccOnDefault");
    this.generalTerms = generalTerms;
    this.feeLeg = feeLeg;
    this.payAccOnDefault = payAccOnDefault;
  }

  @Override
  public Cds.Meta metaBean() {
    return Cds.Meta.INSTANCE;
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
   * Gets this element contains all the data that appears in the section entitled "1. General Terms"
   * in the 2003 ISDA Credit Derivatives Confirmation
   * @return the value of the property, not null
   */
  public GeneralTerms getGeneralTerms() {
    return generalTerms;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets this element contains all the terms relevant to defining the fixed amounts/payments per
   * the applicable ISDA definitions.
   * @return the value of the property, not null
   */
  public FeeLeg getFeeLeg() {
    return feeLeg;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets value that the ISDA Standard model needs but that does not occur in the FpML
   * @return the value of the property, not null
   */
  public boolean isPayAccOnDefault() {
    return payAccOnDefault;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      Cds other = (Cds) obj;
      return JodaBeanUtils.equal(getGeneralTerms(), other.getGeneralTerms()) &&
          JodaBeanUtils.equal(getFeeLeg(), other.getFeeLeg()) &&
          (isPayAccOnDefault() == other.isPayAccOnDefault());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getGeneralTerms());
    hash = hash * 31 + JodaBeanUtils.hashCode(getFeeLeg());
    hash = hash * 31 + JodaBeanUtils.hashCode(isPayAccOnDefault());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("Cds{");
    buf.append("generalTerms").append('=').append(getGeneralTerms()).append(',').append(' ');
    buf.append("feeLeg").append('=').append(getFeeLeg()).append(',').append(' ');
    buf.append("payAccOnDefault").append('=').append(JodaBeanUtils.toString(isPayAccOnDefault()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code Cds}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code generalTerms} property.
     */
    private final MetaProperty<GeneralTerms> generalTerms = DirectMetaProperty.ofImmutable(
        this, "generalTerms", Cds.class, GeneralTerms.class);
    /**
     * The meta-property for the {@code feeLeg} property.
     */
    private final MetaProperty<FeeLeg> feeLeg = DirectMetaProperty.ofImmutable(
        this, "feeLeg", Cds.class, FeeLeg.class);
    /**
     * The meta-property for the {@code payAccOnDefault} property.
     */
    private final MetaProperty<Boolean> payAccOnDefault = DirectMetaProperty.ofImmutable(
        this, "payAccOnDefault", Cds.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "generalTerms",
        "feeLeg",
        "payAccOnDefault");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1474273663:  // generalTerms
          return generalTerms;
        case -1278433112:  // feeLeg
          return feeLeg;
        case -988493655:  // payAccOnDefault
          return payAccOnDefault;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public Cds.Builder builder() {
      return new Cds.Builder();
    }

    @Override
    public Class<? extends Cds> beanType() {
      return Cds.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code generalTerms} property.
     * @return the meta-property, not null
     */
    public MetaProperty<GeneralTerms> generalTerms() {
      return generalTerms;
    }

    /**
     * The meta-property for the {@code feeLeg} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FeeLeg> feeLeg() {
      return feeLeg;
    }

    /**
     * The meta-property for the {@code payAccOnDefault} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Boolean> payAccOnDefault() {
      return payAccOnDefault;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1474273663:  // generalTerms
          return ((Cds) bean).getGeneralTerms();
        case -1278433112:  // feeLeg
          return ((Cds) bean).getFeeLeg();
        case -988493655:  // payAccOnDefault
          return ((Cds) bean).isPayAccOnDefault();
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
   * The bean-builder for {@code Cds}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<Cds> {

    private GeneralTerms generalTerms;
    private FeeLeg feeLeg;
    private boolean payAccOnDefault;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(Cds beanToCopy) {
      this.generalTerms = beanToCopy.getGeneralTerms();
      this.feeLeg = beanToCopy.getFeeLeg();
      this.payAccOnDefault = beanToCopy.isPayAccOnDefault();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1474273663:  // generalTerms
          return generalTerms;
        case -1278433112:  // feeLeg
          return feeLeg;
        case -988493655:  // payAccOnDefault
          return payAccOnDefault;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1474273663:  // generalTerms
          this.generalTerms = (GeneralTerms) newValue;
          break;
        case -1278433112:  // feeLeg
          this.feeLeg = (FeeLeg) newValue;
          break;
        case -988493655:  // payAccOnDefault
          this.payAccOnDefault = (Boolean) newValue;
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
    public Cds build() {
      return new Cds(
          generalTerms,
          feeLeg,
          payAccOnDefault);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code generalTerms} property in the builder.
     * @param generalTerms  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder generalTerms(GeneralTerms generalTerms) {
      JodaBeanUtils.notNull(generalTerms, "generalTerms");
      this.generalTerms = generalTerms;
      return this;
    }

    /**
     * Sets the {@code feeLeg} property in the builder.
     * @param feeLeg  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder feeLeg(FeeLeg feeLeg) {
      JodaBeanUtils.notNull(feeLeg, "feeLeg");
      this.feeLeg = feeLeg;
      return this;
    }

    /**
     * Sets the {@code payAccOnDefault} property in the builder.
     * @param payAccOnDefault  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder payAccOnDefault(boolean payAccOnDefault) {
      JodaBeanUtils.notNull(payAccOnDefault, "payAccOnDefault");
      this.payAccOnDefault = payAccOnDefault;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("Cds.Builder{");
      buf.append("generalTerms").append('=').append(JodaBeanUtils.toString(generalTerms)).append(',').append(' ');
      buf.append("feeLeg").append('=').append(JodaBeanUtils.toString(feeLeg)).append(',').append(' ');
      buf.append("payAccOnDefault").append('=').append(JodaBeanUtils.toString(payAccOnDefault));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
