/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 * <p>
 * Please see distribution for license.
 */
package com.opengamma.strata.finance.credit.general.reference;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.collect.id.StandardId;
import com.opengamma.strata.finance.credit.common.RestructuringClause;
import com.opengamma.strata.finance.credit.common.SeniorityLevel;
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Contains all the terms relevant to defining the reference entity and reference obligation(s)
 */
@BeanDefinition
public final class SingleNameReferenceInformation
    implements ReferenceInformation, ImmutableBean, Serializable {

  /**
   * A legal entity identifier (e.g. RED entity code)
   */
  @PropertyDefinition(validate = "notNull")
  final StandardId referenceEntityId;

  /**
   * property of the reference obligation. Senior obligations will have a higher recovery rate
   * applied during pricing than subordinate securities.
   */
  @PropertyDefinition(validate = "notNull")
  final SeniorityLevel seniority;

  /**
   * currency property of the reference obligation
   */
  @PropertyDefinition(validate = "notNull")
  final Currency currency;

  /**
   * Specifies the type of restructuring that is applicable.
   * This is used as part of the key in determining which curves will be used to price
   * the swap.
   */
  @PropertyDefinition(validate = "notNull")
  final RestructuringClause restructuringClause;

  /**
   * @return enum describe to distinguish that this is a single name CDS
   */
  @Override
  public ReferenceInformationType getType() {
    return ReferenceInformationType.SINGLE_NAME;
  }

  /**
   * The restructuring doc clause will be combined with this info to
   * form a key to find the correct associated curves for pricing
   * <p>
   * ShortName RedCode Tier Ccy
   * e.g. Agilent Tech Inc 008CA0 SNRFOR USD
   *
   * @return
   */
  @Override
  public String getMarketDataKeyName() {
    return String.format(
        "%s %s %s %s",
        getReferenceEntityId(),
        getSeniority().getRedTierCode(),
        getCurrency(),
        getRestructuringClause()
    );
  }

  public static ReferenceInformation of(
      StandardId referenceEntityId,
      SeniorityLevel seniority,
      Currency currency,
      RestructuringClause restructuringClause
  ) {
    return new SingleNameReferenceInformation(
        referenceEntityId,
        seniority,
        currency,
        restructuringClause
    );
  }
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SingleNameReferenceInformation}.
   * @return the meta-bean, not null
   */
  public static SingleNameReferenceInformation.Meta meta() {
    return SingleNameReferenceInformation.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SingleNameReferenceInformation.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static SingleNameReferenceInformation.Builder builder() {
    return new SingleNameReferenceInformation.Builder();
  }

  private SingleNameReferenceInformation(
      StandardId referenceEntityId,
      SeniorityLevel seniority,
      Currency currency,
      RestructuringClause restructuringClause) {
    JodaBeanUtils.notNull(referenceEntityId, "referenceEntityId");
    JodaBeanUtils.notNull(seniority, "seniority");
    JodaBeanUtils.notNull(currency, "currency");
    JodaBeanUtils.notNull(restructuringClause, "restructuringClause");
    this.referenceEntityId = referenceEntityId;
    this.seniority = seniority;
    this.currency = currency;
    this.restructuringClause = restructuringClause;
  }

  @Override
  public SingleNameReferenceInformation.Meta metaBean() {
    return SingleNameReferenceInformation.Meta.INSTANCE;
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
   * Gets a legal entity identifier (e.g. RED entity code)
   * @return the value of the property, not null
   */
  public StandardId getReferenceEntityId() {
    return referenceEntityId;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets property of the reference obligation. Senior obligations will have a higher recovery rate
   * applied during pricing than subordinate securities.
   * @return the value of the property, not null
   */
  public SeniorityLevel getSeniority() {
    return seniority;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets currency property of the reference obligation
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets specifies the type of restructuring that is applicable.
   * This is used as part of the key in determining which curves will be used to price
   * the swap.
   * @return the value of the property, not null
   */
  public RestructuringClause getRestructuringClause() {
    return restructuringClause;
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
      SingleNameReferenceInformation other = (SingleNameReferenceInformation) obj;
      return JodaBeanUtils.equal(getReferenceEntityId(), other.getReferenceEntityId()) &&
          JodaBeanUtils.equal(getSeniority(), other.getSeniority()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getRestructuringClause(), other.getRestructuringClause());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getReferenceEntityId());
    hash = hash * 31 + JodaBeanUtils.hashCode(getSeniority());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash = hash * 31 + JodaBeanUtils.hashCode(getRestructuringClause());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("SingleNameReferenceInformation{");
    buf.append("referenceEntityId").append('=').append(getReferenceEntityId()).append(',').append(' ');
    buf.append("seniority").append('=').append(getSeniority()).append(',').append(' ');
    buf.append("currency").append('=').append(getCurrency()).append(',').append(' ');
    buf.append("restructuringClause").append('=').append(JodaBeanUtils.toString(getRestructuringClause()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SingleNameReferenceInformation}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code referenceEntityId} property.
     */
    private final MetaProperty<StandardId> referenceEntityId = DirectMetaProperty.ofImmutable(
        this, "referenceEntityId", SingleNameReferenceInformation.class, StandardId.class);
    /**
     * The meta-property for the {@code seniority} property.
     */
    private final MetaProperty<SeniorityLevel> seniority = DirectMetaProperty.ofImmutable(
        this, "seniority", SingleNameReferenceInformation.class, SeniorityLevel.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofImmutable(
        this, "currency", SingleNameReferenceInformation.class, Currency.class);
    /**
     * The meta-property for the {@code restructuringClause} property.
     */
    private final MetaProperty<RestructuringClause> restructuringClause = DirectMetaProperty.ofImmutable(
        this, "restructuringClause", SingleNameReferenceInformation.class, RestructuringClause.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "referenceEntityId",
        "seniority",
        "currency",
        "restructuringClause");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1949849399:  // referenceEntityId
          return referenceEntityId;
        case 184581246:  // seniority
          return seniority;
        case 575402001:  // currency
          return currency;
        case -1774904020:  // restructuringClause
          return restructuringClause;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public SingleNameReferenceInformation.Builder builder() {
      return new SingleNameReferenceInformation.Builder();
    }

    @Override
    public Class<? extends SingleNameReferenceInformation> beanType() {
      return SingleNameReferenceInformation.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code referenceEntityId} property.
     * @return the meta-property, not null
     */
    public MetaProperty<StandardId> referenceEntityId() {
      return referenceEntityId;
    }

    /**
     * The meta-property for the {@code seniority} property.
     * @return the meta-property, not null
     */
    public MetaProperty<SeniorityLevel> seniority() {
      return seniority;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    /**
     * The meta-property for the {@code restructuringClause} property.
     * @return the meta-property, not null
     */
    public MetaProperty<RestructuringClause> restructuringClause() {
      return restructuringClause;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1949849399:  // referenceEntityId
          return ((SingleNameReferenceInformation) bean).getReferenceEntityId();
        case 184581246:  // seniority
          return ((SingleNameReferenceInformation) bean).getSeniority();
        case 575402001:  // currency
          return ((SingleNameReferenceInformation) bean).getCurrency();
        case -1774904020:  // restructuringClause
          return ((SingleNameReferenceInformation) bean).getRestructuringClause();
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
   * The bean-builder for {@code SingleNameReferenceInformation}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<SingleNameReferenceInformation> {

    private StandardId referenceEntityId;
    private SeniorityLevel seniority;
    private Currency currency;
    private RestructuringClause restructuringClause;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(SingleNameReferenceInformation beanToCopy) {
      this.referenceEntityId = beanToCopy.getReferenceEntityId();
      this.seniority = beanToCopy.getSeniority();
      this.currency = beanToCopy.getCurrency();
      this.restructuringClause = beanToCopy.getRestructuringClause();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1949849399:  // referenceEntityId
          return referenceEntityId;
        case 184581246:  // seniority
          return seniority;
        case 575402001:  // currency
          return currency;
        case -1774904020:  // restructuringClause
          return restructuringClause;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -1949849399:  // referenceEntityId
          this.referenceEntityId = (StandardId) newValue;
          break;
        case 184581246:  // seniority
          this.seniority = (SeniorityLevel) newValue;
          break;
        case 575402001:  // currency
          this.currency = (Currency) newValue;
          break;
        case -1774904020:  // restructuringClause
          this.restructuringClause = (RestructuringClause) newValue;
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
    public SingleNameReferenceInformation build() {
      return new SingleNameReferenceInformation(
          referenceEntityId,
          seniority,
          currency,
          restructuringClause);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code referenceEntityId} property in the builder.
     * @param referenceEntityId  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder referenceEntityId(StandardId referenceEntityId) {
      JodaBeanUtils.notNull(referenceEntityId, "referenceEntityId");
      this.referenceEntityId = referenceEntityId;
      return this;
    }

    /**
     * Sets the {@code seniority} property in the builder.
     * @param seniority  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder seniority(SeniorityLevel seniority) {
      JodaBeanUtils.notNull(seniority, "seniority");
      this.seniority = seniority;
      return this;
    }

    /**
     * Sets the {@code currency} property in the builder.
     * @param currency  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currency(Currency currency) {
      JodaBeanUtils.notNull(currency, "currency");
      this.currency = currency;
      return this;
    }

    /**
     * Sets the {@code restructuringClause} property in the builder.
     * @param restructuringClause  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder restructuringClause(RestructuringClause restructuringClause) {
      JodaBeanUtils.notNull(restructuringClause, "restructuringClause");
      this.restructuringClause = restructuringClause;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("SingleNameReferenceInformation.Builder{");
      buf.append("referenceEntityId").append('=').append(JodaBeanUtils.toString(referenceEntityId)).append(',').append(' ');
      buf.append("seniority").append('=').append(JodaBeanUtils.toString(seniority)).append(',').append(' ');
      buf.append("currency").append('=').append(JodaBeanUtils.toString(currency)).append(',').append(' ');
      buf.append("restructuringClause").append('=').append(JodaBeanUtils.toString(restructuringClause));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
