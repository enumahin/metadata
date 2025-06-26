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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Location entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Entity
@Table(name = "location",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"community_id", "location_name"}),
        @UniqueConstraint(columnNames = {"community_id", "location_code"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "This is a model class")
public class Location extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "location_code", nullable = false)
    private String locationCode;

    @Column(name = "location_geo_code")
    private String locationGeoCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Column(name = "locale")
    private String locale;

    @Column(name = "locale_preferred")
    private boolean localePreferred;

    @Column(name = "location_phone_code")
    private Integer locationPhoneCode;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return isLocalePreferred() == location.isLocalePreferred()
                && Objects.equals(getLocationId(), location.getLocationId())
                && Objects.equals(getLocationName(), location.getLocationName())
                && Objects.equals(getLocationCode(), location.getLocationCode())
                && Objects.equals(getLocationGeoCode(), location.getLocationGeoCode())
                && Objects.equals(getCommunity(), location.getCommunity())
                && Objects.equals(getLocale(), location.getLocale())
                && Objects.equals(getLocationPhoneCode(), location.getLocationPhoneCode())
                && Objects.equals(getLastModifiedDate(), location.getLastModifiedDate())
                && Objects.equals(getCreatedBy(), location.getCreatedBy())
                && Objects.equals(getCreatedAt(), location.getCreatedAt())
                && Objects.equals(getLastModifiedBy(), location.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), location.getLastModifiedAt())
                && Objects.equals(getVoidedBy(), location.getVoidedBy())
                && Objects.equals(getVoidedAt(), location.getVoidedAt())
                && Objects.equals(getVoidReason(), location.getVoidReason())
                && Objects.equals(isVoided(), location.isVoided());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocationId(), getLocationName(), getLocationCode(), getLocationGeoCode(),
                getCommunity().getCommunityId(), getLocale(), isLocalePreferred(), getLocationPhoneCode(),
                getLastModifiedDate(), getCreatedBy(), getCreatedAt(), getLastModifiedBy(), getLastModifiedAt(),
                getVoidedBy(), getVoidedAt(), getVoidReason(), isVoided());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("locationId", locationId)
                .add("locationName", locationName)
                .add("locationCode", locationCode)
                .add("locationGeoCode", locationGeoCode)
                .add("community", community)
                .add("locale", locale)
                .add("localePreferred", localePreferred)
                .add("locationPhoneCode", locationPhoneCode)
                .add("lastModifiedDate", lastModifiedDate)
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
