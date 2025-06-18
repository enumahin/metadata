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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * City entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Entity
@Table(name = "city",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"city_code", "county_id"}),
        @UniqueConstraint(columnNames = {"city_name", "county_id"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "This is a model class")
public class City extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Integer cityId;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "city_code", nullable = false, unique = true)
    private String cityCode;

    @Column(name = "city_geo_code")
    private String cityGeoCode;

    @Column(name = "locale")
    private String locale;

    @Column(name = "locale_preferred")
    private boolean localePreferred;

    @Column(name = "city_phone_code")
    private Integer cityPhoneCode;

    @ManyToOne
    @JoinColumn(name = "county_id", nullable = false)
    private County county;

    @OneToMany(mappedBy = "city")
    @Builder.Default
    private Set<Community> communities = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        City city = (City) o;
        return isLocalePreferred() == city.isLocalePreferred()
                && Objects.equals(getCityId(), city.getCityId())
                && Objects.equals(getCityName(), city.getCityName())
                && Objects.equals(getCityCode(), city.getCityCode())
                && Objects.equals(getCityGeoCode(), city.getCityGeoCode())
                && Objects.equals(getLocale(), city.getLocale())
                && Objects.equals(getCityPhoneCode(), city.getCityPhoneCode())
                && Objects.equals(getCounty(), city.getCounty())
                && Objects.equals(getCommunities(), city.getCommunities())
                && Objects.equals(getCreatedBy(), city.getCreatedBy())
                && Objects.equals(getCreatedAt(), city.getCreatedAt())
                && Objects.equals(getLastModifiedBy(), city.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), city.getLastModifiedAt())
                && Objects.equals(getVoidedBy(), city.getVoidedBy())
                && Objects.equals(getVoidedAt(), city.getVoidedAt())
                && Objects.equals(getVoidReason(), city.getVoidReason())
                && Objects.equals(isVoided(), city.isVoided());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityId(), getCityName(), getCityCode(), getCityGeoCode(), getLocale(),
                isLocalePreferred(), getCityPhoneCode(), getCounty().getCountyId(), getCreatedBy(),
                getCreatedAt(), getLastModifiedBy(), getLastModifiedAt(), getVoidedBy(), getVoidedAt(),
                getVoidReason(), isVoided());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cityId", cityId)
                .add("cityName", cityName)
                .add("cityCode", cityCode)
                .add("cityGeoCode", cityGeoCode)
                .add("locale", locale)
                .add("county", county)
                .add("localePreferred", localePreferred)
                .add("cityPhoneCode", cityPhoneCode)
                .add("communities", communities)
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
