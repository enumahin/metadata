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
 * State entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "state",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"state_name", "country_id"}),
        @UniqueConstraint(columnNames = {"state_code", "country_id"})
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "This is a model class")
public class State extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private Integer stateId;

    @Column(name = "state_name", nullable = false)
    private String stateName;

    @Column(name = "locale")
    private String locale;

    @Column(name = "locale_preferred")
    private boolean localePreferred;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "state_geo_code")
    private String stateGeoCode;

    @Column(name = "state_phone_code")
    private Integer statePhoneCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "state")
    @Builder.Default
    private Set<County> counties = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        State state = (State) o;
        return isLocalePreferred() == state.isLocalePreferred()
                && Objects.equals(getStateId(), state.getStateId())
                && Objects.equals(getStateName(), state.getStateName())
                && Objects.equals(getLocale(), state.getLocale())
                && Objects.equals(getStateCode(), state.getStateCode())
                && Objects.equals(getStateGeoCode(), state.getStateGeoCode())
                && Objects.equals(getStatePhoneCode(), state.getStatePhoneCode())
                && Objects.equals(getCountry(), state.getCountry())
                && Objects.equals(getCounties(), state.getCounties())
                && Objects.equals(getCreatedBy(), state.getCreatedBy())
                && Objects.equals(getCreatedAt(), state.getCreatedAt())
                && Objects.equals(getLastModifiedBy(), state.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), state.getLastModifiedAt())
                && Objects.equals(getVoidedBy(), state.getVoidedBy())
                && Objects.equals(getVoidedAt(), state.getVoidedAt())
                && Objects.equals(getVoidReason(), state.getVoidReason())
                && isVoided() == state.isVoided();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStateId(), getStateName(), getLocale(), isLocalePreferred(), getStateCode(),
                getStateGeoCode(), getStatePhoneCode(), getCountry().getCountryId(), getCreatedBy(),
                getCreatedAt(), getLastModifiedBy(), getLastModifiedAt(), getVoidedBy(), getVoidedAt(),
                getVoidReason(), isVoided());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("stateId", stateId)
                .add("stateName", stateName)
                .add("locale", locale)
                .add("localePreferred", localePreferred)
                .add("stateCode", stateCode)
                .add("stateGeoCode", stateGeoCode)
                .add("statePhoneCode", statePhoneCode)
                .add("country", country)
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
