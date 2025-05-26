package com.alienworkspace.cdr.metadata.model;

import com.alienworkspace.cdr.metadata.model.audit.AuditTrail;
import com.google.common.base.MoreObjects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Program entity class.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Program extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_id")
    private Integer programId;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private boolean active;

    /**
     * Equals.
     *
     * @param o Object
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Program program)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return isActive() == program.isActive() && Objects.equals(getProgramId(), program.getProgramId())
                && Objects.equals(getName(), program.getName())
                && Objects.equals(getDescription(), program.getDescription())
                && Objects.equals(getLastModifiedBy(), program.getLastModifiedBy())
                && Objects.equals(getLastModifiedAt(), program.getLastModifiedAt())
                && Objects.equals(isVoided(), program.isVoided())
                && Objects.equals(getVoidedBy(), program.getVoidedBy())
                && Objects.equals(getVoidedAt(), program.getVoidedAt())
                && Objects.equals(getVoidReason(), program.getVoidReason())
                && Objects.equals(getUuid(), program.getUuid())
                && Objects.equals(getCreatedBy(), program.getCreatedBy())
                && Objects.equals(getCreatedAt(), program.getCreatedAt());
    }

    /**
     * Hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getProgramId(), getName(), getDescription(), isActive(),
                getLastModifiedBy(), getLastModifiedAt(), isVoided(), getVoidedBy(), getVoidedAt(),
                getVoidReason(), getUuid(), getCreatedBy(), getCreatedAt());
    }

    /**
     * To string.
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("programId", programId)
                .add("name", name)
                .add("description", description)
                .add("active", active)
                .add("createdBy", getCreatedBy())
                .add("createdAt", getCreatedAt())
                .add("lastModifiedBy", getLastModifiedBy())
                .add("lastModifiedAt", getLastModifiedAt())
                .add("voided", isVoided())
                .add("voidedBy", getVoidedBy())
                .add("voidedAt", getVoidedAt())
                .add("voidReason", getVoidReason())
                .add("uuid", getUuid())
                .toString();
    }
}
