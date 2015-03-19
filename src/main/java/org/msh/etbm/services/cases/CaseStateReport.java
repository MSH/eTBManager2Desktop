package org.msh.etbm.services.cases;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.Tag.TagType;
import org.msh.etbm.entities.UserWorkspace;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.entities.enums.UserView;
import org.msh.etbm.entities.enums.ValidationState;
import org.msh.etbm.services.login.UserSession;
import org.msh.etbm.services.misc.GlobalLists;
import org.springframework.stereotype.Component;


/**
 * Generate report by case state to be displayed in the main page
 * @author Ricardo Memoria
 *
 */
@Component
public class CaseStateReport {


	protected List<CaseStateItem> items;
	private List<ValidationItem> validationItems;
	private Item total;
	private List<TagItem> tags;


	/**
	 * Return list of consolidated values by case state
	 * @return
	 */
	public List<CaseStateItem> getItems() {
		if (items == null)
			createItems();
		return items;
	}
	
	
	/**
	 * Remove all data from the report forcing it to refresh in the next time it's accessed
	 */
	public void refresh() {
		items = null;
		validationItems = null;
		total = null;
		tags = null;
	}


	/**
	 * Create items of the report
	 */
	public void createItems() {
		items = new ArrayList<CaseStateItem>();
		validationItems = new ArrayList<ValidationItem>();
		
		String aucond;
		UserWorkspace uw = UserSession.getUserWorkspace();
		
		if (uw.getView() == UserView.ADMINUNIT)
			 aucond = "inner join administrativeunit a on a.id = u.adminunit_id ";
		else aucond = "";

		String cond = generateSQLConditionByUserView();

		String condByCase = generateSQLConditionByCase();

		Integer hsID = null;
		if (uw.getHealthSystem() != null)
			hsID = uw.getHealthSystem().getId();
		
		Workspace defaultWorkspace = UserSession.getWorkspace();

		String sql = "select c.state, c.validationState, c.diagnosisType, count(*) " +
		"from tbcase c " +
		"inner join tbunit u on u.id = c.notification_unit_id " + aucond +
		"where c.state not in (" + CaseState.ONTREATMENT.ordinal() + ',' + CaseState.TRANSFERRING.ordinal() + ") " +
		(hsID != null? "and u.healthSystem_id = " + hsID.toString(): "") +
		" and u.workspace_id = " + defaultWorkspace.getId() + cond + condByCase +
		" group by c.state, c.validationState, c.diagnosisType " +
		"union " +
		"select c.state, c.validationState, c.diagnosisType, count(*) " +
		"from tbcase c " +
		"inner join tbunit u on u.id = c.owner_unit_id " + aucond + 
		"where c.state in (" + CaseState.ONTREATMENT.ordinal() + ',' + CaseState.TRANSFERRING.ordinal() + ")"+
		" and u.workspace_id = " + defaultWorkspace.getId() + cond + condByCase +
		(hsID != null? " and u.healthSystem_id = " + hsID.toString(): "") +
		" group by c.state, c.validationState, c.diagnosisType";
		
		List<Object[]> lst = App.getEntityManager().createNativeQuery(sql).getResultList();
		
		total = new Item( App.getMessage("global.total"), 0);
		
		for (Object[] val: lst) {
			int qty = ((BigInteger)val[3]).intValue();
			
			DiagnosisType diagType;
			if (val[2] != null)
				diagType = DiagnosisType.values()[(Integer)val[2]];
			else diagType = DiagnosisType.CONFIRMED;
			ValidationState vs = ValidationState.values()[(Integer)val[1]];

			Item item = findItem(CaseState.values()[(Integer)val[0]], diagType);
			item.add(qty);
			total.add(qty);
			
			if (!ValidationState.VALIDATED.equals(vs)) {
				ValidationItem valItem = findValidationItem(vs);
				valItem.add(qty);
			}
		}
		
		Collections.sort(items, new Comparator<CaseStateItem>() {

			public int compare(CaseStateItem o1, CaseStateItem o2) {
				Integer cs1 = o1.getStateIndex();
				Integer cs2 = o2.getStateIndex();
				if (cs1 == null)
					return 1;
				if (cs2 == null)
					return -1;

				return cs1.compareTo(cs2);
			}

		});
	}


	/**
	 * Generate SQL condition to filter cases by user view
	 * @return
	 */
	protected String generateSQLConditionByUserView() {
		UserWorkspace userWorkspace = UserSession.getUserWorkspace();
		UserView view = userWorkspace.getView();

		if (view == null)
			return "";

		switch (view) {
		case ADMINUNIT: 
			return " and (a.code like '" + userWorkspace.getAdminUnit().getCode() + "%')"; 
		case TBUNIT: 
			return " and u.id = " + userWorkspace.getTbunit().getId();
		default: return "";
		}
	}


	/**
	 * Generate SQL condition to filter cases
	 * @return SQL condition to be used in a where clause
	 */
	protected String generateSQLConditionByCase() {
		CaseClassification[] classifs = GlobalLists.instance().getCaseClassifications();
		
		String caseCondition = "";

		for (CaseClassification cla: classifs) {
			boolean hasClassif = UserSession.instance().isCanOpenCaseByClassification(cla);
			if (hasClassif) {
				if (!caseCondition.isEmpty())
					caseCondition += ",";
				 caseCondition += cla.ordinal();
			}
		}
		
		if (!caseCondition.isEmpty())
			 return " and c.classification in (" + caseCondition + ")";
		else return caseCondition;
		
/*		boolean tbcases = userSession.isCanOpenCaseByClassification(CaseClassification.TB);
		boolean mdrcases = userSession.isCanOpenMDRTBCases();
		
		if (tbcases && mdrcases) {
			return "";
		}
		
		String caseCondition = "";
		
		if (tbcases)
			 caseCondition = " and (c.classification = " + CaseClassification.TB.ordinal() + ")";
		else
		if (mdrcases)
			caseCondition = " and (c.classification = " + CaseClassification.DRTB.ordinal() + ")";
		
		return caseCondition;
*/	}

	
	/**
	 * Return report by tags
	 * @return
	 */
	public List<TagItem> getTags() {
		if (tags == null)
			createTagsReport();
		return tags;
	}
	
	
	/**
	 * Generate the consolidated tag report displayed at the left side of the home page in the case management module
	 */
	protected void createTagsReport() {
		Workspace workspace = UserSession.getWorkspace();

		String s;
		switch (UserSession.getUserWorkspace().getView()) {
		case TBUNIT: s = "inner join tbcase c on c.id=tc.case_id inner join tbunit u on u.id = c.owner_unit_id ";
			break;
		case ADMINUNIT: s = "inner join tbcase c on c.id=tc.case_id inner join tbunit u on u.id = c.owner_unit_id inner join administrativeunit a on a.id = u.adminunit_id";
			break;
		default: s = "";
		}
		
		String sql = "select t.id, t.tag_name, t.sqlCondition is null, t.consistencyCheck, count(*) " +
			"from tags_case tc " +
				"inner join tag t on t.id = tc.tag_id " +
				s +
			" where t.workspace_id = :id " +
			generateSQLConditionByUserView() + 
			" group by t.id, t.tag_name order by t.tag_name";
		
		List<Object[]> lst = App.getEntityManager().createNativeQuery(sql)
				.setParameter("id", workspace.getId())
				.getResultList();

		tags = new ArrayList<TagItem>();
		for (Object[] vals: lst) {
			TagType type = null;
			if ((Boolean)vals[2] == true) 
				type = TagType.MANUAL;
			else {
				if ((Boolean)vals[3] == Boolean.TRUE)
					 type = TagType.AUTOGEN_CONSISTENCY;
				else type = TagType.AUTOGEN;
			}

			tags.add(new TagItem(vals[1].toString(), ((BigInteger)vals[4]).longValue(), type, (Integer)vals[0]));
		}
	}
	

	/**
	 * Return an item from the validation list from its validation state
	 * @param state
	 * @return Instance of {@link ValidationState}
	 */
	protected ValidationItem findValidationItem(ValidationState state) {
		for (ValidationItem item: validationItems) {
			if (item.getValidationState().equals(state)) {
				return item;
			}
		}
		
		ValidationItem item = new ValidationItem( App.getMessage(state.getKey()), 0, state );
		validationItems.add(item);
		return item;
	}


	/**
	 * Search for a specific item based on the state
	 * @param state
	 * @return
	 */
	protected Item findItem(CaseState state, DiagnosisType diagType) {
		Integer sc = null;
		String desc = null;
		
		if (state.ordinal() >= CaseState.CURED.ordinal()) {
			sc = CaseFilters.CLOSED;
			desc = App.getMessage("cases.closed");
		}
		else
		if ((state == CaseState.WAITING_TREATMENT) && (diagType == DiagnosisType.SUSPECT)) {
			sc = CaseFilters.SUSPECT_NOT_ON_TREATMENT; //stateIndex = 200; 
			desc = App.getMessage("CaseState.NOT_ON_TREATMENT");
		}
		//VR: additional registered-cases categories
		else
			if((state == CaseState.ONTREATMENT) && (diagType == DiagnosisType.SUSPECT)){
				sc = CaseFilters.SUSPECT_ON_TREATMENT;  //stateIndex = 300;
				desc = App.getMessage("cases.suspectOnTreatment");
			}
		else
			if ((state == CaseState.ONTREATMENT) && (diagType == DiagnosisType.CONFIRMED)){
				sc = CaseFilters.CONFIRMED_ON_TREATMENT; //stateIndex = 400;	
				desc = App.getMessage("cases.confirmedOnTreatment");
				}
		else
			if ((state == CaseState.WAITING_TREATMENT) && (diagType == DiagnosisType.CONFIRMED)){
				sc = CaseFilters.CONFIRMED_NOT_ON_TREATMENT; //stateIndex = 500;	
				desc = App.getMessage("cases.confirmedNotOnTreatment");
				}
		else
			if (state == CaseState.TRANSFERRING) {
				sc = CaseFilters.TRANSFERRING;
				desc = App.getMessage(CaseState.TRANSFERRING.getKey());
			}
			else {
				sc = state.ordinal();
				desc = App.getMessage(state.getKey());
			}

		if (sc == null) 
			return null; 
		
		for (CaseStateItem item: items) {
			if (item.getStateIndex() == sc)
				return item;
		}
		
		CaseStateItem item = new CaseStateItem(desc, 0, sc);
		items.add(item);
		
		return item;
	}

	/**
	 * @return the total
	 */
	public Item getTotal() {
		if (total == null)
			createItems();
		return total;
	}

	
	/**
	 * Return list of items to be Validated
	 * @return List of {@link ValidationItem} instances
	 */
	public List<ValidationItem> getValidationItems() {
		if (validationItems == null)
			createItems();
		return validationItems;
	}
	
	
	/**
	 * Store total quantity of cases by an specific indicator
	 * @author Ricardo Memoria
	 *
	 */
	public class Item {
		private String description;
		private long total;

		public Item(String description, long total) {
			super();
			this.description = description;
			this.total = total;
		}

		public void add(int val) {
			total += val;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return the total
		 */
		public long getTotal() {
			return total;
		}
	}


	/**
	 * Store quantity of cases by one specific case state
	 * @author Ricardo Memoria
	 *
	 */
	public class CaseStateItem extends Item {
		private int stateIndex;

		public CaseStateItem(String description, long total, int stateIndex) {
			super(description, total);
			this.stateIndex = stateIndex;
		}

		/**
		 * @return the stateIndex
		 */
		public int getStateIndex() {
			return stateIndex;
		}
	}


	/**
	 * Store consolidated information about cases under validation
	 * @author Ricardo Memoria
	 *
	 */
	public class ValidationItem extends Item {
		private ValidationState validationState;

		public ValidationItem(String description, long total, ValidationState validationState) {
			super(description, total);
			this.validationState = validationState;
		}

		public ValidationState getValidationState() {
			return validationState;
		}
	}

	
	/**
	 * Information about the quantity of cases for a specific tag
	 * @author Ricardo Memoria
	 *
	 */
	public class TagItem extends Item {
		private TagType type;
		private Integer tagId;

		public TagItem(String description, long total, TagType type, Integer tagId) {
			super(description, total);
			this.type = type;
			this.tagId = tagId;
		}

		/**
		 * @return the type
		 */
		public TagType getType() {
			return type;
		}

		/**
		 * @return the tagId
		 */
		public Integer getTagId() {
			return tagId;
		}
	}


	public void setValidationItems(List<ValidationItem> validationItems) {
		this.validationItems = validationItems;
	}


	public void setTotal(Item total) {
		this.total = total;
	}
	
	
	public static CaseStateReport instance() {
		return App.getComponent(CaseStateReport.class);
	}
}
