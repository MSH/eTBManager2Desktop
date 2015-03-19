package org.msh.etbm.services.cases;


import java.util.Calendar;
import java.util.Date;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.entities.enums.CaseState;
import org.msh.etbm.entities.enums.DiagnosisType;
import org.msh.etbm.entities.enums.DisplayCaseNumber;
import org.msh.etbm.entities.enums.InfectionSite;
import org.msh.etbm.entities.enums.PatientType;
import org.msh.etbm.entities.enums.ValidationState;
import org.msh.etbm.services.login.UserSession;
import org.msh.etbm.services.misc.GlobalLists;
import org.msh.utils.ItemSelectList;
import org.msh.utils.date.DateUtils;
import org.springframework.stereotype.Component;


/**
 * Define the filters for case searching
 * @author Ricardo Memoria
 *
 */
@Component
public class CaseFilters {

	// filters that may be applied to search for cases (int format to make it easier to extend)
	public static final int SUSPECT_NOT_ON_TREATMENT = 100;
	public static final int CONFIRMED_NOT_ON_TREATMENT = 101;
	public static final int WAIT_FOR_TREATMENT = 102;
	public static final int ON_TREATMENT = 103;
	public static final int SUSPECT_ON_TREATMENT = 104;
	public static final int CONFIRMED_ON_TREATMENT = 105;
	public static final int TRANSFERRING = 106;
	public static final int CLOSED = 107;
	public static final int CLOSED_WITHOUT_TREATMENT = 108;
	public static final int TRANSFER_IN = 109;
	public static final int TRANSFER_OUT = 110;

	
	private Workspace defaultWorkspace;
	
	private String name;
	private String middleName;
	private String lastName;
	private String recordNumber;
//	private String registrationCode;
	private String unitRegCode;
	private CaseState caseState;
	private ValidationState validationState;
	private CaseClassification classification;
	private int firstRecord;
	private int order;
	private boolean inverseOrder;
	private PatientType patientType;
	private InfectionSite infectionSite;
	private DiagnosisType diagnosisType;
	private Integer iniMonth;
	private Integer iniYear;
	private Integer endMonth;
	private Integer endYear;
	private Integer unitId;
	private Medicine prescribedMedicine;
	private Integer birthYear;
	private FilterHealthUnit filterHealthUnit = FilterHealthUnit.BOTH;
	// date filters
	private boolean registrationDate = true;
	private boolean diagnosisDate;
	private boolean outcomeDate;
	private boolean iniTreatmentDate;
	
	private AdministrativeUnit adminUnit;
	private Tbunit tbunit;
/*	private AdminUnitSelection auselection;
	private TBUnitSelection tbunitselection;
*/	
	private String patient;
	
	/**
	 * Used by the left-side status of the case. Used it because closed cases required a special index (100)
	 */
	private Integer stateIndex;
	
	private Integer tagid;
	
	private ItemSelectList<CaseClassification> classifications;


	/**
	 * Selected view in the detail page of the case
	 */
	private int caseTab;

	/**
	 * Used by {@link CasesQuery} to check which search mode to use 
	 */
	private SearchCriteria searchCriteria;

	/**
	 * Reinitialize the advanced filters
	 */
	public void clearFilters() {
		name = null;
		middleName = null;
		lastName = null;
		recordNumber = null;
//		registrationCode = null;
		unitRegCode = null;
		caseState = null;
		validationState = null;
		classification = null;
/*		auselection = null;
		tbunitselection = null;
*/		firstRecord = 0;
		patientType = null;
		infectionSite = null;
		diagnosisType = null;
		iniMonth = null;
		iniYear = null;
		endMonth = null;
		endYear = null;
		birthYear = null;
		filterHealthUnit = FilterHealthUnit.BOTH;
		registrationDate = true;
		diagnosisDate = false;
		outcomeDate = false;
		iniTreatmentDate = false;
		patient = null;
		stateIndex = null;
		unitId = null;
		prescribedMedicine = null;
		
		tagid = null;
		
		order = 0;
		inverseOrder = false;
	}


	
	/**
	 * Called when the search criteria changes
	 */
	protected void searchCriteriaChanged() {
		if (searchCriteria == null)
			searchCriteria = SearchCriteria.CUSTOM_FILTER;
		
		switch (searchCriteria) {
			case CASE_STATE:
				int sc = stateIndex;
				clearFilters();
				stateIndex = sc;
				break;

			case PATIENT:
				String s = patient;
				clearFilters();
				patient = s;
				break;
			
			case VALIDATION_STATE:
				ValidationState vs = validationState;
				clearFilters();
				validationState = vs;
				break;
			
			case CASE_TAG:
				Integer id = tagid;
				clearFilters();
				tagid = id;
				break;
			
			case CUSTOM_FILTER:
				patient = null;
				stateIndex = null;
				tagid = null;
				break;

			case CASE_UNIT_STATE:
				int sii = stateIndex;
				Integer uId = unitId;
				clearFilters();
				setFilterHealthUnit(FilterHealthUnit.TREATMENT_UNIT);
				stateIndex = sii;
				unitId = uId;
				break;
		}
	}


	/**
	 * Set the execution of case searching using the patient name/number 
	 */
/*	public String startSingleSearch() {
		clearAdvancedSearhFilters();
		advancedSearch = false;
		return "search";
	}
*/
	
	public String getNameLike() {
		return ((name == null) || (name.isEmpty())? null: '%' + name.toUpperCase() + '%');
	}
	
	public String getMiddleNameLike() {
		return ((middleName == null) || (middleName.isEmpty())? null: '%' + middleName.toUpperCase() + '%');
	}
	
	public String getLastNameLike() {
		return ((lastName == null) || (lastName.isEmpty())? null: '%' + lastName.toUpperCase() + '%');
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public CaseState getCaseState() {
		return caseState;
	}

	public void setCaseState(CaseState caseState) {
		this.caseState = caseState;
	}

	public String getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}

	public CaseClassification getClassification() {
		return classification;
/*		boolean mdrCases = UserSession.instance().hasRole(CaseClassification.DRTB.toString() + "_CASE_VIEW");
		boolean tbcases = UserSession.instance().hasRole(CaseClassification.TB.toString() + "_CASE_VIEW");
		
		if (mdrCases && tbcases)
			return classification;
		else {
			if (mdrCases)
				 return CaseClassification.DRTB;
			else return CaseClassification.TB;
		}
*/	}

	public void setClassification(CaseClassification classification) {
		this.classification = classification;
	}

	public int getFirstRecord() {
		return firstRecord;
	}

	public void setFirstRecord(int firstRecord) {
		this.firstRecord = firstRecord;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isInverseOrder() {
		return inverseOrder;
	}

	public void setInverseOrder(boolean inverseOrder) {
		this.inverseOrder = inverseOrder;
	}

	/**
	 * @return the patientType
	 */
	public PatientType getPatientType() {
		return patientType;
	}

	/**
	 * @param patientType the patientType to set
	 */
	public void setPatientType(PatientType patientType) {
		this.patientType = patientType;
	}

	/**
	 * @return the infectionSite
	 */
	public InfectionSite getInfectionSite() {
		return infectionSite;
	}

	/**
	 * @param infectionSite the infectionSite to set
	 */
	public void setInfectionSite(InfectionSite infectionSite) {
		this.infectionSite = infectionSite;
	}
	// VR: To include 'Type of Diagnostic' in Advance Search Page
	/**
	 * @return the diagnosisType
	 */
	public DiagnosisType getDiagnosisType(){
		return diagnosisType;
	}
	
	/**
	 * @param diagnosisType the diagnosisType to set
	 */
	public void setDiagnosisType(DiagnosisType diagnosisType){
		this.diagnosisType = diagnosisType;
	}

	/**
	 * @return the iniMonth
	 */
	public Integer getIniMonth() {
		return iniMonth;
	}

	/**
	 * @param iniMonth the iniMonth to set
	 */
	public void setIniMonth(Integer iniMonth) {
		this.iniMonth = iniMonth;
	}

	/**
	 * @return the iniYear
	 */
	public Integer getIniYear() {
		return iniYear;
	}

	/**
	 * @param iniYear the iniYear to set
	 */
	public void setIniYear(Integer iniYear) {
		this.iniYear = iniYear;
	}

	/**
	 * @return the endMonth
	 */
	public Integer getEndMonth() {
		return endMonth;
	}

	/**
	 * @param endMonth the endMonth to set
	 */
	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	/**
	 * @return the endYear
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	/**
	 * @return the filterHealthUnit
	 */
	public FilterHealthUnit getFilterHealthUnit() {
		return filterHealthUnit;
	}

	/**
	 * @param filterHealthUnit the filterHealthUnit to set
	 */
	public void setFilterHealthUnit(FilterHealthUnit filterHealthUnit) {
		this.filterHealthUnit = filterHealthUnit;
	}
	
	/**
	 * Returns the initial date of a filter by dates
	 * @return initial date if initial month and year are set in caseFilters, otherwise null
	 */
	public Date getIniDate() {
		if ((iniMonth != null) && (iniYear != null)) {
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(iniYear, iniMonth, 1);
			return c.getTime();
		}
		return null;
	}
	
	/**
	 * Returns the initial date of a filter by dates
	 * @return initial date if initial month and year are set in caseFilters, otherwise null
	 */
	public Date getEndDate() {
		if ((endMonth != null) && (endYear != null)) {
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(endYear, endMonth, DateUtils.daysInAMonth(endYear, endMonth));
			return c.getTime();
		}
		return null;		
	}
	/**
	 * @return the diagnosisDate
	 */
	public boolean isDiagnosisDate() {
		return diagnosisDate;
	}

	/**
	 * @param diagnosisDate the diagnosisDate to set
	 */
	public void setDiagnosisDate(boolean diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	/**
	 * @return the outcomeDate
	 */
	public boolean isOutcomeDate() {
		return outcomeDate;
	}

	/**
	 * @param outcomeDate the outcomeDate to set
	 */
	public void setOutcomeDate(boolean outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

	/**
	 * @return the iniTreatmentDate
	 */
	public boolean isIniTreatmentDate() {
		return iniTreatmentDate;
	}

	/**
	 * @param iniTreatmentDate the iniTreatmentDate to set
	 */
	public void setIniTreatmentDate(boolean iniTreatmentDate) {
		this.iniTreatmentDate = iniTreatmentDate;
	}


	/**
	 * Returns the new order property
	 * @return
	 */
	public int getNewOrder() {
		return order;
	}

	
	/**
	 * Change the order. If the order is the same as before, switch the inverseOrder property
	 * @param order
	 */
	public void setNewOrder(int order) {
		if (order == this.order)
			inverseOrder = !inverseOrder;
		else inverseOrder = false;
/*		CasesQuery.instance().set
		((CasesQuery)Component.getInstance("cases")).setCurrentPage(1);
*/		this.order = order;
	}
/*	
	public AdminUnitSelection getAuselection() {
		if (auselection == null) {
			auselection = new AdminUnitSelection();
			auselection.setApplyUserRestrictions(false);
		}
		return auselection;
	}
	
	public TBUnitSelection getTbunitselection() {
		if (tbunitselection == null) {
			tbunitselection = new TBUnitSelection();
			tbunitselection.setApplyUserRestrictions(true);
			tbunitselection.setFilter(TBUnitFilter.HEALTH_UNITS);
		}
		return tbunitselection;
	}
*/
	public String getAdminUnitLike() {
		AdministrativeUnit adm = getAdminUnit();
		
		if (adm == null)
			return null;
		
		return adm.getCode() + "%";
	}

	public String getTbAdminUnitLike() {
		AdministrativeUnit adm = getAdminUnit();
		
		if (adm == null)
			return null;
		
		return adm.getCode() + "%";
	}

	public String getTbAdminUnitAnyLevelLike() {
		AdministrativeUnit adm = getAdminUnit();
/*		AdministrativeUnit adm=null;
		if (getTbunitselection().getAuselection()!=null)
			adm = getTbunitselection().getAuselection().getSelectedUnit();
*/		
		if (adm == null)
			return null;
		
		return adm.getCode() + "%";
	}

	
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the registrationDate
	 */
	public boolean isRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(boolean registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the patient
	 */
	public String getPatient() {
		return patient;
	}

	/**
	 * @param patient the patient to set
	 */
	public void setPatient(String patient) {
		if ((patient != null) && (patient.trim().length() == 0))
			 this.patient = null;
		else this.patient = patient;
		
		stateIndex = null;
	}
	
	public String getPatientLike() {
		return patient != null? "%" + patient + "%": null;
	}
	
/*	public Integer getPatientNumber() {
		if (patient == null)
			return null;
		
		try {
			return Integer.valueOf(patient); 
		} catch (Exception e) {
			return null;
		}
	}
*/

	/**
	 * @return the validationState
	 */
	public ValidationState getValidationState() {
		return validationState;
	}

	/**
	 * @param validationState the validationState to set
	 */
	public void setValidationState(ValidationState validationState) {
		this.validationState = validationState;
	}

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		if ((this.searchCriteria == searchCriteria) && searchCriteria != SearchCriteria.CASE_UNIT_STATE)
			return;
		
		this.searchCriteria = searchCriteria;
		searchCriteriaChanged();
	}

	/**
	 * Check if record number is empty
	 * @return
	 */
	public boolean isRecordNumberEmpty() {
		return ((recordNumber == null) || (recordNumber.isEmpty()));
	}


	/**
	 * Return the patient record number entered in the field recordNumber in the format nnnnn-nn
	 * @return
	 */
	public Integer getPatientRecordNumber() {
		if (isRecordNumberEmpty())
			return null;
		
//		if (defaultWorkspace == null)
//			defaultWorkspace = (Workspace)Component.getInstance("defaultWorkspace");
		
		if (DisplayCaseNumber.USER_DEFINED.equals(getDefaultWorkspace().getConfirmedCaseNumber()))
			return null;

		return getNumberFromString(recordNumber, 0);
	}


	/**
	 * If patient search number is by its registration code, then return the record number given by the user
	 * @return
	 */
	public String getRegistrationCode() {
//		if (defaultWorkspace == null)
//			defaultWorkspace = (Workspace)Component.getInstance("defaultWorkspace");

		if (DisplayCaseNumber.USER_DEFINED.equals(getDefaultWorkspace().getConfirmedCaseNumber()))
			 return recordNumber;
		else return (getNumberFromString(recordNumber, 0) != null? null: recordNumber);
	}


	/**
	 * Return the case number being the number after the character '-' entered in the field record number 
	 * in the format NNNN-NN
	 * @return
	 */
	public Integer getCaseNumber() {
		if ((isRecordNumberEmpty()) || (DisplayCaseNumber.USER_DEFINED.equals(getDefaultWorkspace().getConfirmedCaseNumber())))
			return null;

		return getNumberFromString(recordNumber, 1);
	}


	protected Integer getNumberFromString(String val, int index) {
		if (val == null)
			return null;
		
		String[] vals = val.split("-");
		if (vals.length > index) {
			try {
				return Integer.parseInt(vals[index]);
			} catch (Exception e) {
				return null;
			}
		}
		else return null;		
	}


	/**
	 * Return the list of classifications available in the workspace
	 * @return
	 */
	public ItemSelectList<CaseClassification> getClassifications() {
		if (classifications == null) {
			GlobalLists globalLists = GlobalLists.instance();
			classifications = new ItemSelectList<CaseClassification>(globalLists.getUserCaseClassifications());
			classifications.selectAll();
		}
		return classifications;
	}

	/**
	 * @return the caseView
	 */
/*	public CaseView getCaseView() {
		switch (caseTab) {
		case 2: return CaseView.EXAMS;
		case 3: return CaseView.TREATMENT;
		case 4: return CaseView.MEDEXAMS;
		case 5: return CaseView.ADDINFO;
		case 6: return CaseView.RESUME;
		}
		return CaseView.DATA;
	}

	public void setCaseView(CaseView view) {
		if (view == null)
			view = CaseView.DATA;

		switch (view) {
		case DATA: caseTab = 1;
			break;
		case EXAMS: caseTab = 2;
			break;
		case TREATMENT: caseTab = 3;
			break;
		case MEDEXAMS: caseTab = 4;
			break;
		case ADDINFO: caseTab = 5;
			break;
		case RESUME: caseTab = 6;
		}
	}
*/
	/**
	 * @return the tagid
	 */
	public Integer getTagid() {
		return tagid;
	}

	/**
	 * @param tagid the tagid to set
	 */
	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}

	/**
	 * @return the caseTab
	 */
	public int getCaseTab() {
		return caseTab;
	}

	/**
	 * @param caseTab the caseTab to set
	 */
	public void setCaseTab(int caseTab) {
		this.caseTab = caseTab;
	}

	public String getUnitRegCode() {
		return unitRegCode;
	}

	public void setUnitRegCode(String unitRegCode) {
		this.unitRegCode = unitRegCode;
	}

	
	public Workspace getDefaultWorkspace() {
		if (defaultWorkspace == null)
			defaultWorkspace = UserSession.getWorkspace();
		return defaultWorkspace;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Medicine getPrescribedMedicine() {
		return prescribedMedicine;
	}

	public void setPrescribedMedicine(Medicine prescribedMedicine) {
		this.prescribedMedicine = prescribedMedicine;
	}

	/**
	 * @return the birthYear
	 */
	public Integer getBirthYear() {
		return birthYear;
	}

	/**
	 * @param birthYear the birthYear to set
	 */
	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}

	

	public static CaseFilters instance() {
		return (CaseFilters)App.getComponent("caseFilters");
	}

	/**
	 * @return the stateIndex
	 */
	public Integer getStateIndex() {
		return stateIndex;
	}

	/**
	 * @param stateIndex the stateIndex to set
	 */
	public void setStateIndex(Integer stateIndex) {
		this.stateIndex = stateIndex;
	}



	/**
	 * @return the adminUnit
	 */
	public AdministrativeUnit getAdminUnit() {
		return adminUnit;
	}



	/**
	 * @param adminUnit the adminUnit to set
	 */
	public void setAdminUnit(AdministrativeUnit adminUnit) {
		this.adminUnit = adminUnit;
	}



	/**
	 * @return the tbunit
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}



	/**
	 * @param tbunit the tbunit to set
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}

}
