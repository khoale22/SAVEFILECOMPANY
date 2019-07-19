package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a primary merchandising location.
 *
 * @author s769046
 * @since 2.8.0
 */
@Entity
@Table(name = "A102248_PRIM_MDSE_LOC")
@TypeDefs({
        @TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class),
})
public class PrimaryMerchandisingLocation implements CodeTable {

    private static final long serialVersionUID = 1L;

    public PrimaryMerchandisingLocation() {}

    public PrimaryMerchandisingLocation(CodeTable codeTable) {
        this.id = codeTable.getId();
        this.description = codeTable.getDescription();
    }

    @Id
    @Column(name = "A102248_PRIM_MDSE_LOC_CD")
    @Type(type="fixedLengthCharPK")
    private String id;

    @Column(name = "A102248_PRIM_MDSE_LOC_DES")
    private String description;

    /**
     * Returns the id.
     *
     * @return the id.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * @param id the id.
     *
     * @return the updated PrimaryMerchandisingLocation.
     */
    @Override
    public PrimaryMerchandisingLocation setId(String id) {
        this.id = id;
        return this;
    }


    /**
     * Returns the description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description.
     * @return the updated PrimaryMerchandisingLocation.
     */
    public PrimaryMerchandisingLocation setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Compares another object to this one. If that object is a Location, it uses they keys
     * to determine if they are equal and ignores non-key values for the comparison.
     *
     * @param o The object to compare to.
     * @return True if they are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimaryMerchandisingLocation that = (PrimaryMerchandisingLocation) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Returns a hash code for the object. Equal objects return the same falue. Unequal objects (probably) return
     * different values.
     *
     * @return A hash code for the object.
     */
    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "PrimaryMerchandisingLocation{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
