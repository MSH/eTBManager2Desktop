package org.msh.springframework.seam;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.NonUniqueResultException;


/**
 * This is the EntityQuery class available in the SEAM framework and being adapted to
 * a Swing application (or any other platform). It contains both org.jboss.seam.framework.EntityQuery
 * and org.msh.utils.EntityQuery and the objective is to make sharing of code between web and desktop
 * as easy as possible, promoting code reusing
 *  
 * @author Ricardo Memoria
 *
 * @param <E>
 */
public class EntityQuery<E> extends Query<E> {
	private static final long serialVersionUID = 7741549786723871723L;

	private static final Pattern WHERE_PATTERN = Pattern.compile("\\s(where)\\s", Pattern.CASE_INSENSITIVE);

	private List<E> resultList;
	private E singleResult;
	private Long resultCount;
	private Map<String, String> hints;


	public EntityQuery() {
		super();
		List<String> restrictions = getStringRestrictions();
		if (restrictions != null)
			setRestrictionExpressionStrings(restrictions);
	}

	protected List<String> getStringRestrictions() {
		return null;
	}


	/**
	 * Validate the query
	 * 
	 * @throws IllegalStateException if the query is not valid
	 */
	@Override
	public void validate()
	{
		super.validate();
		if ( getEntityManager()==null )
		{
			throw new IllegalStateException("entityManager is null");
		}
	}

	@Override
//	@Transactional
	public boolean isNextExists()
	{
		return resultList!=null && getMaxResults()!=null &&
				resultList.size() > getMaxResults();
	}


	/**
	 * Get the list of results this query returns
	 * 
	 * Any changed restriction values will be applied
	 */
//	@Transactional
	@Override
	public List<E> getResultList()
	{
		if ( isAnyParameterDirty() )
		{
			refresh();
		}
		initResultList();
		return truncResultList(resultList);
	}

	private void initResultList()
	{
		if (resultList==null)
		{
			javax.persistence.Query query = createQuery();
			resultList = query==null ? null : query.getResultList();
		}
	}

	/**
	 * Get a single result from the query
	 * 
	 * Any changed restriction values will be applied
	 * 
	 * @throws NonUniqueResultException if there is more than one result
	 */
//	@Transactional
	@Override
	public E getSingleResult()
	{
		if (isAnyParameterDirty())
		{
			refresh();
		}
		initSingleResult();
		return singleResult;
	}

	private void initSingleResult()
	{
		if ( singleResult==null)
		{
			javax.persistence.Query query = createQuery();
			singleResult = (E) (query==null ? 
					null : query.getSingleResult());
		}
	}

	/**
	 * Get the number of results this query returns
	 * 
	 * Any changed restriction values will be applied
	 */
//	@Transactional
	@Override
	public Long getResultCount()
	{
		if (isAnyParameterDirty())
		{
			refresh();
		}
		initResultCount();
		return resultCount;
	}

	private void initResultCount()
	{
		if ( resultCount==null )
		{
			javax.persistence.Query query = createCountQuery();
			resultCount = query==null ? 
					null : (Long) query.getSingleResult();
		}
	}

	/**
	 * The refresh method will cause the result to be cleared.  The next access
	 * to the result set will cause the query to be executed.
	 * 
	 * This method <b>does not</b> cause the ejbql or restrictions to reread.
	 * If you want to update the ejbql or restrictions you must call 
	 * {@link #setEjbql(String)} or {@link #setRestrictions(List)}
	 */
	@Override
	public void refresh()
	{
		super.refresh();
		resultCount = null;
		resultList = null;
		singleResult = null;
	}


	protected javax.persistence.Query createQuery()
	{
		parseEjbql();

		evaluateAllParameters();

		//	      joinTransaction();

		javax.persistence.Query query = getEntityManager().createQuery( getRenderedEjbql() );
		setParameters( query, getQueryParameterValues(), 0 );
		setParameters( query, getRestrictionParameterValues(), getQueryParameterValues().size() );
		if ( getFirstResult()!=null) query.setFirstResult( getFirstResult() );
		if ( getMaxResults()!=null) query.setMaxResults( getMaxResults()+1 ); //add one, so we can tell if there is another page
		if ( getHints()!=null )
		{
			for ( Map.Entry<String, String> me: getHints().entrySet() )
			{
				query.setHint(me.getKey(), me.getValue());
			}
		}
		return query;
	}

	protected javax.persistence.Query createCountQuery()
	{
		parseEjbql();

		evaluateAllParameters();

		//	      joinTransaction();

		String hql = getCountEjbqlWithRestrictions();
		javax.persistence.Query query = getEntityManager().createQuery( hql );
		return query;
		/*	      javax.persistence.Query query = getEntityManager().createQuery( getCountEjbql() );
	      setParameters( query, getQueryParameterValues(), 0 );
	      setParameters( query, getRestrictionParameterValues(), getQueryParameterValues().size() );
	      return query;
		 */	   }


	protected String getCountEjbqlWithRestrictions() {
		String hql = getCountEjbql();
		StringBuilder builder = new StringBuilder().append(hql);

		boolean bWhere = WHERE_PATTERN.matcher(builder).find();

		for (int i = 0; i < getRestrictions().size(); i++) {
			Object parameterValue = getRestrictionParameters().get(i)
					.getValue();
			if (isRestrictionParameterSet(parameterValue)) {
				if (bWhere) {
					builder.append(" and ");
				} else {
					builder.append(" where ");
					bWhere = true;
				}
				builder.append(getRestrictions().get(i).getExpressionString());
			}
		}
		return builder.toString();
	}


	private void setParameters(javax.persistence.Query query, List<Object> parameters, int start)
	{
		for (int i=0; i<parameters.size(); i++)
		{
			Object parameterValue = parameters.get(i);
			if ( isRestrictionParameterSet(parameterValue) )
			{
				query.setParameter( QueryParser.getParameterName(start + i), parameterValue );
			}
		}
	}

	public Map<String, String> getHints()
	{
		return hints;
	}

	public void setHints(Map<String, String> hints)
	{
		this.hints = hints;
	}
	   
/*	   protected void joinTransaction()
	   {
	      try
	      {
	         Transaction.instance().enlist( getEntityManager() );
	      }
	      catch (SystemException se)
	      {
	         throw new RuntimeException("could not join transaction", se);
	      }
	   }
*/
}
