package org.msh.etbm.services.adminunit;

import java.util.Arrays;
import java.util.List;

import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.springframework.seam.EntityQuery;
import org.springframework.stereotype.Component;



/**
 * Return the root elements of the administrative unit of the country
 * @author Ricardo Memoria
 *
 */
@Component("adminUnits")
public class AdminUnitsQuery extends EntityQuery<AdministrativeUnit> {
	private static final long serialVersionUID = 6428637361635215953L;

	private static final String[] restrictions = {"a.workspace.id = #{defaultWorkspace.id}",
			"a.parent.id = #{adminUnits.parentId}"};

	private Integer parentId;

	/** {@inheritDoc}
	 * @see org.jboss.seam.framework.Query#getEjbql()
	 */
	@Override
	public String getEjbql() {
		return "from AdministrativeUnit a left join fetch a.countryStructure " + getStaticCondition();
	}
		

	/** {@inheritDoc}
	 * @see org.jboss.seam.framework.Query#getCountEjbql()
	 */
	@Override
	protected String getCountEjbql() {
		return "select count(*) from AdministrativeUnit a " + getStaticCondition();
	}


	/** {@inheritDoc}
	 * @see com.rmemoria.utils.EntityQuery#getStringRestrictions()
	 */
	@Override
	protected List<String> getStringRestrictions() {
		return Arrays.asList(restrictions);
	}


	/** {@inheritDoc}
	 * @see org.jboss.seam.framework.Query#getOrder()
	 */
	@Override
	public String getOrder() {
		String s = super.getOrder();
		if (s == null)
			s = "a.name.name1";
		return s;
	}


	/**
	 * Generate HQL static condition to be included in the query
	 * @return HQL condition
	 */
	protected String getStaticCondition() {
		String s = "";
		if (parentId == null)
			s = " where a.parent.id is null";
		return s;
	}


	/**
	 * @return the parentId
	 */
	public Integer getParentId() {
		return parentId;
	}


	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}



}
