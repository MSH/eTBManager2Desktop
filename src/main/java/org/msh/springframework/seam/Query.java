package org.msh.springframework.seam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * This is the Query class available in the SEAM framework (org.jboss.seam.framework) and being adapted to
 * a Swing application (or any other platform). The objective is to make sharing of code between web and desktop
 * as easy as possible, promoting code reusing.
 *  
 * @author Ricardo Memoria
 *
 * @param <E>
 */
public abstract class Query<E> extends Controller {
	private static final long serialVersionUID = 1165340874412353545L;

	private static final Pattern FROM_PATTERN = Pattern.compile("(^|\\s)(from)\\s",       Pattern.CASE_INSENSITIVE);
	private static final Pattern WHERE_PATTERN = Pattern.compile("\\s(where)\\s",         Pattern.CASE_INSENSITIVE);
	private static final Pattern ORDER_PATTERN = Pattern.compile("\\s(order)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);

	private static final Pattern ORDER_COLUMN_PATTERN = Pattern.compile("^\\w+(\\.\\w+)*$");

	private static final String DIR_ASC = "asc";
	private static final String DIR_DESC = "desc";

	private String ejbql;
	private Integer firstResult;
	private Integer maxResults;
	private List<ValueExpression> restrictions = new ArrayList<ValueExpression>(0);
	private String order;
	private String orderColumn;
	private String orderDirection;

	private String groupBy;

	private String parsedEjbql;
	private List<ValueExpression> queryParameters;
	private List<String> parsedRestrictions;
	private List<ValueExpression> restrictionParameters;

	private List<Object> queryParameterValues;
	private List<Object> restrictionParameterValues;

	public abstract List<E> getResultList();
	public abstract E getSingleResult();
	public abstract Long getResultCount();

	public void validate()
	{
		if ( getEjbql()==null )
		{
			throw new IllegalStateException("ejbql is null");
		}
	}


	public void last()
	{
		setFirstResult( getLastFirstResult().intValue() );
	}

	/**
	 * Move the result set cursor to the beginning of the next page
	 * 
	 */
	public void next()
	{
		setFirstResult( getNextFirstResult() );
	}

	/**
	 * Move the result set cursor to the beginning of the previous page
	 * 
	 */
	public void previous()
	{
		setFirstResult( getPreviousFirstResult() );
	}

	/**
	 * Move the result set cursor to the beginning of the first page
	 * 
	 */
	public void first()
	{
		setFirstResult(0);
	}

	/**
	 * Get the index of the first result of the last page
	 * 
	 */
	public Long getLastFirstResult()
	{
		Integer pc = getPageCount();
		return pc==null ? null : ( pc.longValue()-1 ) * getMaxResults();
	}

	/**
	 * Get the index of the first result of the next page
	 * 
	 */
	public int getNextFirstResult()
	{
		Integer fr = getFirstResult();
		return ( fr==null ? 0 : fr ) + getMaxResults();
	}

	/**
	 * Get the index of the first result of the previous page
	 * 
	 */
	public int getPreviousFirstResult()
	{
		Integer fr = getFirstResult();
		Integer mr = getMaxResults();
		return mr >= ( fr==null ? 0 : fr ) ? 
				0 : fr - mr;
	}

	/**
	 * Get the total number of pages
	 * 
	 */
//	@Transactional
	public Integer getPageCount()
	{
		if ( getMaxResults()==null )
		{
			return null;
		}
		else
		{
			int rc = getResultCount().intValue();
			int mr = getMaxResults().intValue();
			int pages = rc / mr;
			return rc % mr == 0 ? pages : pages+1;
		}
	}


	protected void parseEjbql()
	{
		if (parsedEjbql==null || parsedRestrictions==null)
		{
			QueryParser qp = new QueryParser( getEjbql() );
			queryParameters = qp.getParameterValueBindings();
			parsedEjbql = qp.getEjbql();

			List<ValueExpression> restrictionFragments = getRestrictions();
			parsedRestrictions = new ArrayList<String>( restrictionFragments.size() );
			restrictionParameters = new ArrayList<ValueExpression>( restrictionFragments.size() );         
			for ( ValueExpression restriction: restrictionFragments )
			{
				QueryParser rqp = new QueryParser( restriction.getExpressionString(), queryParameters.size() + restrictionParameters.size() );            
				if ( rqp.getParameterValueBindings().size()!=1 ) 
				{
					throw new IllegalArgumentException("there should be exactly one value binding in a restriction: " + restriction);
				}            
				parsedRestrictions.add( rqp.getEjbql() );
				restrictionParameters.addAll( rqp.getParameterValueBindings() );
			}

		}
	}


	public void refresh()
	{
		parsedEjbql = null;
	}


	protected String getRenderedEjbql()
	{
		StringBuilder builder = new StringBuilder().append(parsedEjbql);

		for (int i=0; i<getRestrictions().size(); i++)
		{
			Object parameterValue = restrictionParameters.get(i).getValue();
			if ( isRestrictionParameterSet(parameterValue) )
			{
				if ( WHERE_PATTERN.matcher(builder).find() )
				{
					builder.append(" and ");
				}
				else
				{
					builder.append(" where ");
				}
				builder.append( parsedRestrictions.get(i) );
			}
		}

		if (getGroupBy()!=null) {
			builder.append(" group by ").append(getGroupBy());
		}

		if (getOrder()!=null) {
			builder.append(" order by ").append( getOrder() );
		}

		return builder.toString();
	}

	protected boolean isRestrictionParameterSet(Object parameterValue)
	{
		return parameterValue != null && !"".equals(parameterValue) && (parameterValue instanceof Collection ? !((Collection) parameterValue).isEmpty() : true);
	}

	/**
	 * Return the ejbql to used in a count query (for calculating number of
	 * results)
	 * @return String The ejbql query
	 */
	protected String getCountEjbql()
	{
		String ejbql = getRenderedEjbql();

		Matcher fromMatcher = FROM_PATTERN.matcher(ejbql);
		if ( !fromMatcher.find() )
		{
			throw new IllegalArgumentException("no from clause found in query");
		}
		int fromLoc = fromMatcher.start(2);

		Matcher orderMatcher = ORDER_PATTERN.matcher(ejbql);
		int orderLoc = orderMatcher.find() ? orderMatcher.start(1) : ejbql.length();

		Matcher whereMatcher = WHERE_PATTERN.matcher(ejbql);
		int whereLoc = whereMatcher.find() ? whereMatcher.start(1) : orderLoc;

		String subject = "*";
		// TODO to be JPA-compliant, we need to make this query like "select count(u) from User u"
		// however, Hibernate produces queries some databases cannot run when the primary key is composite
		//	      Matcher subjectMatcher = SUBJECT_PATTERN.matcher(ejbql);
		//	      if ( subjectMatcher.find() )
		//	      {
		//	         subject = subjectMatcher.group(1);
		//	      }

		return new StringBuilder(ejbql.length() + 15).append("select count(").append(subject).append(") ").
				append(ejbql.substring(fromLoc, whereLoc).replace("join fetch", "join")).
				append(ejbql.substring(whereLoc, orderLoc)).toString().trim();
	}

	public String getEjbql()
	{
		return ejbql;
	}

	/**
	 * Set the ejbql to use.  Calling this causes the ejbql to be reparsed and
	 * the query to be refreshed
	 */
	public void setEjbql(String ejbql)
	{
		this.ejbql = ejbql;
		parsedEjbql = null;
		refresh();
	}

	/**
	 * Returns the index of the first result of the current page
	 */
	public Integer getFirstResult()
	{
		return firstResult;
	}

	/**
	 * Returns true if the previous page exists
	 */
	public boolean isPreviousExists()
	{
		return getFirstResult()!=null && getFirstResult()!=0;
	}

	/**
	 * Returns true if next page exists
	 */
	public abstract boolean isNextExists();

	/**
	 * Set the index at which the page to display should start
	 */
	public void setFirstResult(Integer firstResult)
	{
		this.firstResult = firstResult;
		refresh();
	}

	/**
	 * The page size
	 */
	public Integer getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(Integer maxResults)
	{
		this.maxResults = maxResults;
		refresh();
	}

	/**
	 * List of restrictions to apply to the query.
	 * 
	 * For a query such as 'from Foo f' a restriction could be 
	 * 'f.bar = #{foo.bar}'
	 */
	public List<ValueExpression> getRestrictions()
	{
		return restrictions;
	}

	/**
	 * Calling setRestrictions causes the restrictions to be reparsed and the 
	 * query refreshed
	 */
	public void setRestrictions(List<ValueExpression> restrictions)
	{
		this.restrictions = restrictions;
		parsedRestrictions = null;
		refresh();
	}

	/**
	 * A convenience method for registering the restrictions from Strings. This
	 * method is primarily intended to be used from Java, not to expose a bean
	 * property for component configuration. Use setRestrictions() for the later.
	 */
	public void setRestrictionExpressionStrings(List<String> expressionStrings)
	{
		Expressions expressions = new Expressions();
		List<ValueExpression> restrictionVEs = new ArrayList<ValueExpression>(expressionStrings.size());
		for (String expressionString : expressionStrings)
		{
			restrictionVEs.add(expressions.createValueExpression(expressionString));
		}
		setRestrictions(restrictionVEs);
	}

	public List<String> getRestrictionExpressionStrings()
	{
		List<String> expressionStrings = new ArrayList<String>();
		for (ValueExpression restriction : getRestrictions())
		{
			expressionStrings.add(restriction.getExpressionString());
		}
		return expressionStrings;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * The order clause of the query
	 */

	 public String getOrder() {
		 String column = getOrderColumn();

		 if (column == null) {
			 return order;
		 }

		 String direction = getOrderDirection();

		 if (direction == null) {
			 return column;
		 } else {
			 return column + ' ' + direction;
		 }   
	 }

	 public void setOrder(String order)
	 {      
		 this.order = order;
		 refresh();
	 }

	 public String getOrderDirection() {
		 return orderDirection;
	 }

	 public void setOrderDirection(String orderDirection) {
		 this.orderDirection = sanitizeOrderDirection(orderDirection);
	 }

	 private String sanitizeOrderDirection(String direction) {
		 if (direction == null || direction.length()==0) {
			 return null;
		 } else if (direction.equalsIgnoreCase(DIR_ASC)) {
			 return DIR_ASC;
		 } else if (direction.equalsIgnoreCase(DIR_DESC)) {
			 return DIR_DESC;
		 } else {
			 throw new IllegalArgumentException("invalid order direction");
		 }
	 }

	 public String getOrderColumn() {
		 return orderColumn;
	 }

	 public void setOrderColumn(String orderColumn) {
		 this.orderColumn = sanitizeOrderColumn(orderColumn);
	 }

	 private String sanitizeOrderColumn(String columnName) {
		 if (columnName == null || columnName.trim().length() == 0) {
			 return null;
		 } else if (ORDER_COLUMN_PATTERN.matcher(columnName).find()) {
			 return columnName;
		 } else {
			 throw new IllegalArgumentException("invalid order column (\"" + columnName + "\" must match the regular expression \"" + ORDER_COLUMN_PATTERN + "\")");
		 }
	 }

	 protected List<ValueExpression> getQueryParameters()
	 {
		 return queryParameters;
	 }

	 protected List<ValueExpression> getRestrictionParameters()
	 {
		 return restrictionParameters;
	 }

	 private static boolean isAnyParameterDirty(List<ValueExpression> valueBindings, List<Object> lastParameterValues)
	 {
		 if (lastParameterValues==null) return true;
		 for (int i=0; i<valueBindings.size(); i++)
		 {
			 Object parameterValue = valueBindings.get(i).getValue();
			 Object lastParameterValue = lastParameterValues.get(i);
			 //treat empty strings as null, for consistency with isRestrictionParameterSet()
			 if ( "".equals(parameterValue) ) parameterValue = null;
			 if ( "".equals(lastParameterValue) ) lastParameterValue = null;
			 if ( parameterValue!=lastParameterValue && ( parameterValue==null || !parameterValue.equals(lastParameterValue) ) )
			 {
				 return true;
			 }
		 }
		 return false;
	 }

	 private static List<Object> getParameterValues(List<ValueExpression> valueBindings)
	 {
		 List<Object> values = new ArrayList<Object>( valueBindings.size() );
		 for (int i=0; i<valueBindings.size(); i++)
		 {
			 values.add( valueBindings.get(i).getValue() );
		 }
		 return values;
	 }

	 protected void evaluateAllParameters()
	 {
		 setQueryParameterValues( getParameterValues( getQueryParameters() ) );
		 setRestrictionParameterValues( getParameterValues( getRestrictionParameters() ) );
	 }

	 protected boolean isAnyParameterDirty()
	 {
		 return isAnyParameterDirty( getQueryParameters(), getQueryParameterValues() )
				 || isAnyParameterDirty( getRestrictionParameters(), getRestrictionParameterValues() );
	 }

	 protected List<Object> getQueryParameterValues()
	 {
		 return queryParameterValues;
	 }

	 protected void setQueryParameterValues(List<Object> queryParameterValues)
	 {
		 this.queryParameterValues = queryParameterValues;
	 }

	 protected List<Object> getRestrictionParameterValues()
	 {
		 return restrictionParameterValues;
	 }

	 protected void setRestrictionParameterValues(List<Object> restrictionParameterValues)
	 {
		 this.restrictionParameterValues = restrictionParameterValues;
	 }
	 protected List<E> truncResultList(List<E> results)
	 {
		 Integer mr = getMaxResults();
		 if ( mr!=null && results.size() > mr )
		 {
			 return results.subList(0, mr);
		 }
		 else
		 {
			 return results;
		 }
	 }
}
