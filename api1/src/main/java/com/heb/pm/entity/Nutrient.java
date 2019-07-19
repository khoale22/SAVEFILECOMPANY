package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a dynamic attribute of a Nutrients.
 *
 * @author m594201
 * @since 2.1.0
 */
@Entity
@Table(name = "pd_nutrient")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class Nutrient implements Serializable {

	private static final long serialVersionUID = 1L;

    private static final String FEDERAL_NUTRIENT_REQUIRED_FIELD = "isFederalRequired";
	private static final String FEDERAL_NUTRIENT_SORT_FIELD = "fedLblSequence";

	public static final char DELETE_MAINTENANCE_SW = 'D';

    @Id
    @Column(name = "pd_lbl_ntrnt_cd")
    private long nutrientCode;

    @Column(name = "pd_lbl_ntrnt_des")
    //db2o changes  vn00907
    @Type(type="fixedLengthChar")  
    private String nutrientDescription;

    @OneToOne
    @JoinColumn(name = "pd_lbl_uom_cd", referencedColumnName = "pd_lbl_uom_cd", insertable = false, updatable = false)
    private NutrientUom nutrientUom;

    @Column(name = "pd_lbl_uom_cd")
    private long uomCode;

    @Column(name = "pd_fed_req_seq_no")
    private double fedLblSequence;

    @Column(name = "pd_ntrnt_rda_qty")
    private long recommendedDailyAmount;

    @Column(name = "pd_ntrnt_pdv_sw")
    private boolean usePercentDailyValue;

    @Column(name = "pd_lbl_maint_dt")
    private LocalDate lstModifiedDate;

    @Column(name = "pd_ntrnt_maint_sw")  
	private char maintenanceSwitch;

	@Column(name = "pd_lbl_fed_req_sw") 
	private boolean isFederalRequired;

	@Column(name = "pd_pdd_req_sw")  
	private boolean isDefaultBehaviorOverrideRequired;

	@Column(name = "pd_pdd_seq_no")
	private double defaultBehaviorOverrideSequence;

	@Column(name = "PD_FED_REQ_PDV_SW")  
	private boolean isFederalRequiredPdv;

	@JsonIgnoreProperties("nutrient")
	@OneToMany(mappedBy = "nutrient", fetch = FetchType.LAZY)
	private List<NutrientStatementDetail> nutrientStatementDetails;

	/**
	 * Represents the values of the known scale nutrients.
	 */
	public enum Codes {
		VITAMIN_A(1L),
		VITAMIN_C(2L),
		THIAMINE(3L),
		RIBOFLAVIN(4L),
		NIACIN(5L),
		CALCIUM(6L),
		IRON(7L),
		VITAMIN_D(8L),
		VITAMIN_E(9L),
		VITAMIN_B6(10L),
		FOLIC_ACID(11L),
		VITAMIN_B12(12L),
		PHOSPHORUS(13L),
		IODINE(14L),
		MAGNESIUM(15L),
		ZINC(16L),
		COPPER(17L),
		BIOTIN(18L),
		PANTOTHENIC_ACID(19L),
		ASCORBIC_ACID(20L),
		FOLACIN(21L),
		VITAMIN_B2(22L),
		VITAMIN_B1(23L),
		CALORIES(100L),
		CALORIES_FROM_FAT(101L),
		TOTAL_FAT(102L),
		SATURATED_FAT(103L),
		CHOLESTEROL(104L),
		TOTAL_CARBOHYDRATE(105L),
		DIETARY_FIBER(106L),
		SODIUM(107L),
		SUGARS(108L),
		PROTEIN(109L),
		POTASSIUM(110L),
		TRANS_FAT(113L),
		SUGAR_ALCOHOL(120L),
		ADDED_SUGARS(127L);

		Long code;

		/**
		 * Constructor for a nutrient code.
		 * @param code The value of the code in the table.
		 */
		Codes(Long code) {
			this.code = code;
		}

		private static Map<Long, Codes> map = new HashMap<>();
		static {
			for (Codes nutrient : Codes.values()) {
				map.put(nutrient.code, nutrient);
			}
		}

		/**
		 * Returns the code that represents each value (the value that will go into the database).
		 *
		 * @return The code that represents each value.
		 */
		public Long getCode() {
			return this.code;
		}

		/**
		 * Returns the Codes matching the given code.
		 *
		 * @param code The value of the code in the table.
		 */
		public static Codes valueOf(Long code) {
			return map.get(code);
		}
	}

	/**
	 * Get a list of nutrient statement details.
	 *
	 * @return a list of nutrient statement details.
	 */
	public List<NutrientStatementDetail> getNutrientStatementDetails() {
		return nutrientStatementDetails;
	}

	/**
	 * Sets a list of nutrient statement details.
	 *
	 * @param nutrientStatementDetails list of nutrient statement details to set.
	 */
	public void setNutrientStatementDetails(List<NutrientStatementDetail> nutrientStatementDetails) {
		this.nutrientStatementDetails = nutrientStatementDetails;
	}

	/**
	 * Gets uom code.
	 *
	 * @return the uom code
	 */
	public long getUomCode() {
        return uomCode;
    }

	/**
	 * Sets uom code.
	 *
	 * @param uomCode the uom code
	 */
	public void setUomCode(long uomCode) {
        this.uomCode = uomCode;
    }

	/**
	 * Gets nutrient code.
	 *
	 * @return the nutrient code
	 */
	public long getNutrientCode() {
        return nutrientCode;
    }

	/**
	 * Sets nutrient code.
	 *
	 * @param nutrientCode the nutrient code
	 */
	public void setNutrientCode(long nutrientCode) {
        this.nutrientCode = nutrientCode;
    }

	/**
	 * Gets nutrient description.
	 *
	 * @return the nutrient description
	 */
	public String getNutrientDescription() {
        return nutrientDescription;
    }

	/**
	 * Sets nutrient description.
	 *
	 * @param nutrientDescription the nutrient description
	 */
	public void setNutrientDescription(String nutrientDescription) {
        this.nutrientDescription = nutrientDescription;
    }

	/**
	 * Gets nutrient uom.
	 *
	 * @return the nutrient uom
	 */
	public NutrientUom getNutrientUom() {
        return nutrientUom;
    }

	/**
	 * Sets nutrient uom.
	 *
	 * @param nutrientUom the nutrient uom
	 */
	public void setNutrientUom(NutrientUom nutrientUom) {
        this.nutrientUom = nutrientUom;
    }

	/**
	 * Gets fed lbl sequence.
	 *
	 * @return the fed lbl sequence
	 */
	public double getFedLblSequence() {
		return fedLblSequence;
	}

	/**
	 * Sets fed lbl sequence.
	 *
	 * @param fedLblSequence the fed lbl sequence
	 */
	public void setFedLblSequence(double fedLblSequence) {
		this.fedLblSequence = fedLblSequence;
	}

	/**
	 * Returns the FDA recommended daily amount of this nutrient in grams.
	 *
	 * @return The FDA recommended daily amount of this nutrient in grams.
	 */
	public long getRecommendedDailyAmount() {
        return this.recommendedDailyAmount;
    }

	/**
	 * Set the FDA recommended daily amount of this nutrient in grams.
	 *
	 * @param recommendedDailyAmount The FDA recommended daily amount of this nutrient in grams.
	 */
	public void setRecommendedDailyAmount(long recommendedDailyAmount) {
        this.recommendedDailyAmount = recommendedDailyAmount;
    }

	/**
	 * Returns whether or not this nutrient should be set by set by percent of daily value instead of by measure.
	 *
	 * @return True if this nutrient should be set by set by percent of daily value instead of by measure and false
	 * otherwise.
	 */
	public boolean getUsePercentDailyValue() {
		return usePercentDailyValue;
	}

	/**
	 * Sets whether or not this nutrient should be set by set by percent of daily value instead of by measure.
	 *
	 * @param usePercentDailyValue True if this nutrient should be set by set by percent of daily value instead of by
	 * measure and false otherwise.
	 */
	public void setUsePercentDailyValue(boolean usePercentDailyValue) {
		this.usePercentDailyValue = usePercentDailyValue;
	}

	/**
	 * Gets lst modified date.
	 *
	 * @return the lst modified date
	 */
	public LocalDate getLstModifiedDate() {
        return lstModifiedDate;
    }

	/**
	 * Sets lst modified date.
	 *
	 * @param lstModifiedDate the lst modified date
	 */
	public void setLstModifiedDate(LocalDate lstModifiedDate) {
        this.lstModifiedDate = lstModifiedDate;
    }

	/**
	 * Gets maintenance switch.
	 *
	 * @return the maintenance switch
	 */
	public char getMaintenanceSwitch() {
		return maintenanceSwitch;
	}

	/**
	 * Sets maintenance switch.
	 *
	 * @param maintenanceSwitch the maintenance switch
	 */
	public void setMaintenanceSwitch(char maintenanceSwitch) {
		this.maintenanceSwitch = maintenanceSwitch;
	}

	/**
	 * Is federal required boolean.
	 *
	 * @return the boolean
	 */
	public boolean isFederalRequired() {
		return isFederalRequired;
	}

	/**
	 * Sets federal required.
	 *
	 * @param federalRequired the federal required
	 */
	public void setFederalRequired(boolean federalRequired) {
		isFederalRequired = federalRequired;
	}

	/**
	 * Is default behavior override required boolean.
	 *
	 * @return the boolean
	 */
	public boolean isDefaultBehaviorOverrideRequired() {
		return isDefaultBehaviorOverrideRequired;
	}

	/**
	 * Sets default behavior override required.
	 *
	 * @param defaultBehaviorOverrideRequired the default behavior override required
	 */
	public void setDefaultBehaviorOverrideRequired(boolean defaultBehaviorOverrideRequired) {
		this.isDefaultBehaviorOverrideRequired = defaultBehaviorOverrideRequired;
	}

	/**
	 * Gets default behavior override sequence.
	 *
	 * @return the default behavior override sequence
	 */
	public double getDefaultBehaviorOverrideSequence() {
		return defaultBehaviorOverrideSequence;
	}

	/**
	 * Sets default behavior override sequence.
	 *
	 * @param defaultBehaviorOverrideSequence the default behavior override sequence
	 */
	public void setDefaultBehaviorOverrideSequence(double defaultBehaviorOverrideSequence) {
		this.defaultBehaviorOverrideSequence = defaultBehaviorOverrideSequence;
	}

	/**
	 * Is federal required pdv boolean.
	 *
	 * @return the boolean
	 */
	public boolean isFederalRequiredPdv() {
		return isFederalRequiredPdv;
	}

	/**
	 * Sets federal required pdv.
	 *
	 * @param federalRequiredPdv the federal required pdv
	 */
	public void setFederalRequiredPdv(boolean federalRequiredPdv) {
		isFederalRequiredPdv = federalRequiredPdv;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
    @Override
    public String toString() {
        return "Nutrient{" +
                "nutrientCode=" + nutrientCode +
                ", nutrientDescription='" + nutrientDescription + '\'' +
                ", nutrientUom=" + nutrientUom +
                ", fedLblSequence=" + fedLblSequence +
                ", recommendedDailyAmount=" + recommendedDailyAmount +
                ", usePercentDailyValue='" + usePercentDailyValue + '\'' +
                ", lstModifiedDate=" + lstModifiedDate +
				", isFederalRequired=" + isFederalRequired +
				", isDefaultBehaviorOverrideRequired=" + isDefaultBehaviorOverrideRequired +
				", defaultBehaviorOverrideSequence=" + defaultBehaviorOverrideSequence +
				", isFederalRequiredPdv=" + isFederalRequiredPdv +
                '}';
    }

	/**
	 * Returns the default sort order for the scale action code table.
	 *
	 * @return The default sort order for the scale action code table.
	 */
	public static Sort getDefaultSort() {
        return  new Sort(
                new Sort.Order(Sort.Direction.DESC, Nutrient.FEDERAL_NUTRIENT_REQUIRED_FIELD),
				new Sort.Order(Sort.Direction.ASC, Nutrient.FEDERAL_NUTRIENT_SORT_FIELD)
        );
    }

}
