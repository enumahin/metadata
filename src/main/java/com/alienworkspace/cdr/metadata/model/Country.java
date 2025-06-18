package com.alienworkspace.cdr.metadata.model;

import com.alienworkspace.cdr.metadata.model.audit.AuditTrail;
import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Country entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Entity
@Table(name = "country",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = {"country_name"}),
        @jakarta.persistence.UniqueConstraint(columnNames = {"country_code"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "This is a model class")
public class Country extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "country_code", nullable = false, unique = true)
    private String countryCode;

    @Column(name = "country_phone_code")
    private Integer countryPhoneCode;

    @Column(name = "currency_name")
    private  String currencyName;

    @Column(name = "currency_symbol", nullable = false)
    private String currencySymbol;

    @Column(name = "currency_code")
    private String currencyCode;

    private String locale;

    @Column(name = "country_geo_code")
    private String countryGeoCode;

    @Column(name = "locale_preferred")
    private boolean localePreferred;

    @OneToMany(mappedBy = "country")
    @Builder.Default
    private Set<State> states = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country country = (Country) o;
        return isLocalePreferred() == country.isLocalePreferred()
                && Objects.equals(getCountryId(), country.getCountryId())
                && Objects.equals(getCountryName(), country.getCountryName())
                && Objects.equals(getCountryCode(), country.getCountryCode())
                && Objects.equals(getCountryPhoneCode(), country.getCountryPhoneCode())
                && Objects.equals(getCurrencyName(), country.getCurrencyName())
                && Objects.equals(getCurrencySymbol(), country.getCurrencySymbol())
                && Objects.equals(getCurrencyCode(), country.getCurrencyCode())
                && Objects.equals(getLocale(), country.getLocale())
                && Objects.equals(getCountryGeoCode(), country.getCountryGeoCode())
                && Objects.equals(getStates(), country.getStates())
                && Objects.equals(getCreatedBy(), country.getCreatedBy())
                && Objects.equals(getCreatedAt(), country.getCreatedAt())
                && Objects.equals(getLastModifiedBy(), country.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), country.getLastModifiedAt())
                && Objects.equals(getVoidedBy(), country.getVoidedBy())
                && Objects.equals(getVoidedAt(), country.getVoidedAt())
                && Objects.equals(getVoidReason(), country.getVoidReason())
                && Objects.equals(isVoided(), country.isVoided());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountryId(), getCountryName(), getCountryCode(), getCountryPhoneCode(),
                getCurrencyName(), getCurrencySymbol(), getCurrencyCode(), getLocale(), getCountryGeoCode(),
                isLocalePreferred(), getCreatedBy(), getCreatedAt(), getLastModifiedBy(),
                getLastModifiedAt(), getVoidedBy(), getVoidedAt(), getVoidReason(), isVoided());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("countryId", countryId)
                .add("countryName", countryName)
                .add("countryCode", countryCode)
                .add("countryPhoneCode", countryPhoneCode)
                .add("currencyName", currencyName)
                .add("currencySymbol", currencySymbol)
                .add("currencyCode", currencyCode)
                .add("locale", locale)
                .add("countryGeoCode", countryGeoCode)
                .add("localePreferred", localePreferred)
                .add("states", states)
                .add("createdBy", getCreatedBy())
                .add("createdAt", getCreatedAt())
                .add("lastModifiedBy", getLastModifiedBy())
                .add("lastModifiedAt", getLastModifiedAt())
                .add("voidedBy", getVoidedBy())
                .add("voidedAt", getVoidedAt())
                .add("voidReason", getVoidReason())
                .add("voided", isVoided())
                .toString();
    }
}
