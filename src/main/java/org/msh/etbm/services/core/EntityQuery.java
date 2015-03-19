/**
 * 
 */
package org.msh.etbm.services.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.msh.etbm.desktop.app.App;

/**
 * @author Ricardo Memoria
 *
 */
public class EntityQuery<E> {

	private static final Pattern WHERE_PATTERN = Pattern.compile("\\s(where)\\s", Pattern.CASE_INSENSITIVE);

	private Integer maxResults;
	private Integer firstResult;
	private Map<String, Object> params = new HashMap<String, Object>();
	private String orderBy;
	private List<String> conditions;
	private String hql;
	private String hqlCount;
	
	/**
	 * Clear all settings like query, maximum results, order by, etc.
	 */
	public void clear() {
		conditions = null;
		maxResults = null;
		firstResult = null;
		orderBy = null;
		params.clear();
	}

	
	/**
	 * Add a condition to the query
	 * @param condition
	 */
	public void addCondition(String condition) {
		if (conditions == null)
			conditions = new ArrayList<String>();
		conditions.add(condition);
	}

	/**
	 * Add a parameter value to the query. The values will be added to the query
	 * when the query is about to be executed
	 * @param param the parameter name
	 * @param value the value of the parameter
	 */
	public void addParameter(String param, Object value) {
		params.put(param, value);
	}

	/**
	 * Query the database and return its entities
	 * @return {@link List} of entities
	 */
	public List<E> getResults() {
		return prepareQuery().getResultList();
	}
	
	
	/**
	 * Return number of items in the query
	 * @return {@link Long} instance
	 */
	public Long getResultCount() {
		return (Long)prepareCountQuery().getSingleResult();
	}

	protected StringBuilder generateHqlConditions(String hqlquery) {
		StringBuilder s = new StringBuilder(hqlquery);
		
		// where clause was declared ?
		boolean bWhere = WHERE_PATTERN.matcher(hqlquery).find();

		boolean bFirst = true;
		if (conditions != null) {
			for (String cond: conditions) {
				// is the first condition to be included ?
				if ((bFirst) && (!bWhere)) 
					 s.append("\n where ");
				else s.append("\n and ");
				s.append(cond);
				bFirst = false;
			}
		}
		return s;
	}

	/**
	 * Add the parameter values to the query
	 * @param qry instance of {@link Query}
	 */
	protected void addQueryParameters(Query qry) {
		// include parameter values
		for (String paramname: params.keySet()) {
			qry.setParameter(paramname, params.get(paramname));
		}
	}
	
	/**
	 * Prepare the query to be run
	 * @return instance of {@link Query} ready to be executed
	 */
	protected Query prepareQuery() {
		EntityManager em = App.getEntityManager();

		StringBuilder s = generateHqlConditions(hql);

		// include clause "order by"
		if (orderBy != null)
			s.append("\norder by " + orderBy);
		
		Query qry = em.createQuery(s.toString());
		if (firstResult != null)
			qry.setFirstResult(firstResult);
		if (maxResults != null)
			qry.setMaxResults(maxResults);

		addQueryParameters(qry);
		
		return qry;
	}
	
	/**
	 * Prepare the query that will count the number of records to run
	 * @return instance of {@link Query} ready to be executed
	 */
	protected Query prepareCountQuery() {
		EntityManager em = App.getEntityManager();

		StringBuilder s = generateHqlConditions(hqlCount);
		
		Query qry = em.createQuery(s.toString());

		addQueryParameters(qry);
		
		return qry;
	}
	
	/**
	 * @return the maxResults
	 */
	public Integer getMaxResults() {
		return maxResults;
	}
	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	/**
	 * @return the firstResult
	 */
	public Integer getFirstResult() {
		return firstResult;
	}
	/**
	 * @param firstResult the firstResult to set
	 */
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}


	/**
	 * @return the hql
	 */
	public String getHql() {
		return hql;
	}


	/**
	 * @param hql the hql to set
	 */
	public void setHql(String hql) {
		this.hql = hql;
	}


	/**
	 * @return the hqlCount
	 */
	public String getHqlCount() {
		return hqlCount;
	}


	/**
	 * @param hqlCount the hqlCount to set
	 */
	public void setHqlCount(String hqlCount) {
		this.hqlCount = hqlCount;
	}
}
