package com.heb.scaleMaintenanceSetUp.entity;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.domain.Sort;

import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.TopLevelEntity;

/**
 * Entity for scale maintenance tracking.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "GBL_PARM")
public class GblParm implements TopLevelEntity<GblParm>, Serializable {

	private static final long serialVersionUID = 8805862154194412474L;
	private static final String GBL_PARM_VAL_TXT_SORT_FIELD = "gblParmValTxt";

	@Id
	@Column(name = "GBL_PARM_ID")
	private Long gblParmId;

	@Column(name = "GBL_PARM_NM")
	private String gblParmName;

	@Column(name = "GBL_PARM_VAL_TXT")
	private String gblParmValTxt;

	@Column(name = "DSCRM_01_TXT")
	private String deptIds;

	
	public Long getGblParmId() {
		return gblParmId;
	}

	public void setGblParmId(Long gblParmId) {
		this.gblParmId = gblParmId;
	}

	public String getGblParmName() {
		return gblParmName;
	}

	public void setGblParmName(String gblParmName) {
		this.gblParmName = gblParmName;
	}

	public String getGblParmValTxt() {
		return gblParmValTxt;
	}

	public void setGblParmValTxt(String gblParmValTxt) {
		this.gblParmValTxt = gblParmValTxt;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	

	@Override
	public <R> R map(Function<? super GblParm, ? extends R> mapper) {
		return mapper.apply(this);
	}

	@Override
	public GblParm save(Function<? super GblParm, ? extends GblParm> saver) {
		return saver.apply(this);
	}
	
	/**
	 * Returns the default sort order for the prod_del table.
	 *
	 * @return The default sort order for the prod_del table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.DESC, GblParm.GBL_PARM_VAL_TXT_SORT_FIELD)
		);
	}

	@Override
	public String toString() {
		return "GblParm [gblParmId=" + gblParmId + ", gblParmName=" + gblParmName + ", gblParmValTxt=" + gblParmValTxt
				+ ", deptIds=" + deptIds ;
	}

}
