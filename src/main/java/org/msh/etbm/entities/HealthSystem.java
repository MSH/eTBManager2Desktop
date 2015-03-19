package org.msh.etbm.entities;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.msh.utils.FieldLog;

@Entity
@Table(name = "healthsystem")
public class HealthSystem extends WSObject {

    private static final long serialVersionUID = 8006343659248774062L;
    @Id
    private Integer id;
    @Embedded
    @FieldLog(key = "form.name")
    private LocalizedNameComp name = new LocalizedNameComp();
    @Column(length = 50)
    @FieldLog(key = "global.legacyId")
    private String legacyId;

    /** {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthSystem)) {
            return false;
        }

        return ((HealthSystem) other).getId().equals(id);
    }

    /** {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return (name.toString() != null ? name.toString() : super.toString());
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public LocalizedNameComp getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(LocalizedNameComp name) {
        this.name = name;
    }

    /**
     * @return the legacyId
     */
    public String getLegacyId() {
        return legacyId;
    }

    /**
     * @param legacyId the legacyId to set
     */
    public void setLegacyId(String legacyId) {
        this.legacyId = legacyId;
    }
}
