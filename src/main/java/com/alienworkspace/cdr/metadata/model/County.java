package com.alienworkspace.cdr.metadata.model;

import com.alienworkspace.cdr.metadata.model.audit.AuditTrail;
import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * County entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Entity
@Table(name = "county",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"state_id", "county_name"}),
        @UniqueConstraint(columnNames = {"state_id", "county_code"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "This is a model class")
public class County extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "county_id")
    private Integer countyId;

    @Column(name = "county_name", nullable = false)
    private String countyName;

    @Column(name = "county_code", nullable = false, unique = true)
    private String countyCode;

    @Column(name = "county_geo_code")
    private String countyGeoCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Column(name = "locale")
    private String locale;

    @Column(name = "locale_preferred")
    private boolean localePreferred;

    @Column(name = "county_phone_code")
    private Integer countyPhoneCode;

    @OneToMany(mappedBy = "county")
    @Builder.Default
    private Set<City> cities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        County county = (County) o;
        return isLocalePreferred() == county.isLocalePreferred()
                && Objects.equals(getCountyId(), county.getCountyId())
                && Objects.equals(getCountyName(), county.getCountyName())
                && Objects.equals(getCountyCode(), county.getCountyCode())
                && Objects.equals(getCountyGeoCode(), county.getCountyGeoCode())
                && Objects.equals(getState(), county.getState())
                && Objects.equals(getLocale(), county.getLocale())
                && Objects.equals(getCountyPhoneCode(), county.getCountyPhoneCode())
                && Objects.equals(getCities(), county.getCities())
                && Objects.equals(getCreatedBy(), county.getCreatedBy())
                && Objects.equals(getCreatedAt(), county.getCreatedAt())
                && Objects.equals(getLastModifiedBy(), county.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), county.getLastModifiedAt())
                && Objects.equals(getVoidedBy(), county.getVoidedBy())
                && Objects.equals(getVoidedAt(), county.getVoidedAt())
                && Objects.equals(getVoidReason(), county.getVoidReason())
                && Objects.equals(isVoided(), county.isVoided());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountyId(), getCountyName(), getCountyCode(), getCountyGeoCode(),
                getState().getStateId(), getLocale(), isLocalePreferred(), getCountyPhoneCode(), getCreatedBy(),
                getCreatedAt(), getLastModifiedBy(), getLastModifiedAt(), getVoidedBy(), getVoidedAt(), getVoidReason(),
                isVoided());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("countyId", countyId)
                .add("countyName", countyName)
                .add("countyCode", countyCode)
                .add("countyGeoCode", countyGeoCode)
                .add("state", state)
                .add("locale", locale)
                .add("localePreferred", localePreferred)
                .add("countyPhoneCode", countyPhoneCode)
                .add("cities", cities)
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
