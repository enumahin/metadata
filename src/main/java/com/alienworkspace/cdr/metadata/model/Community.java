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
 * Community entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Entity
@Table(name = "community",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"community_code", "city_id"}),
        @UniqueConstraint(columnNames = {"community_name", "city_id"})
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "This is a model class")
public class Community extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Integer communityId;

    @Column(name = "community_name", nullable = false)
    private String communityName;

    @Column(name = "community_code", nullable = false, unique = true)
    private String communityCode;

    @Column(name = "community_geo_code")
    private String communityGeoCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "locale")
    private String locale;

    @Column(name = "locale_preferred")
    private boolean localePreferred;

    @Column(name = "community_phone_code")
    private Integer communityPhoneCode;

    @OneToMany(mappedBy = "community")
    @Builder.Default
    private Set<Location> locations = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Community community = (Community) o;
        return isLocalePreferred() == community.isLocalePreferred()
                && Objects.equals(getCommunityId(), community.getCommunityId())
                && Objects.equals(getCommunityName(), community.getCommunityName())
                && Objects.equals(getCommunityCode(), community.getCommunityCode())
                && Objects.equals(getCommunityGeoCode(), community.getCommunityGeoCode())
                && Objects.equals(getCity(), community.getCity())
                && Objects.equals(getLocale(), community.getLocale())
                && Objects.equals(getCommunityPhoneCode(), community.getCommunityPhoneCode())
                && Objects.equals(getLocations(), community.getLocations())
                && Objects.equals(getCreatedBy(), community.getCreatedBy())
                && Objects.equals(getCreatedAt(), community.getCreatedAt())
                && Objects.equals(getLastModifiedBy(), community.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), community.getLastModifiedAt())
                && Objects.equals(getVoidedBy(), community.getVoidedBy())
                && Objects.equals(getVoidedAt(), community.getVoidedAt())
                && Objects.equals(getVoidReason(), community.getVoidReason())
                && Objects.equals(isVoided(), community.isVoided());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommunityId(), getCommunityName(), getCommunityCode(), getCommunityGeoCode(),
                getCity().getCityId(), getLocale(), isLocalePreferred(), getCommunityPhoneCode(), getCreatedBy(),
                getCreatedAt(), getLastModifiedBy(), getLastModifiedAt(), getVoidedBy(), getVoidedAt(),
                getVoidReason(), isVoided());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("communityId", communityId)
                .add("communityName", communityName)
                .add("communityCode", communityCode)
                .add("communityGeoCode", communityGeoCode)
                .add("city", city)
                .add("locale", locale)
                .add("localePreferred", localePreferred)
                .add("communityPhoneCode", communityPhoneCode)
                .add("locations", locations)
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
