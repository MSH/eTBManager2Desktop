package org.msh.etbm.services.misc;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.common.SelectItem;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.*;
import org.msh.etbm.services.login.UserSession;
import org.msh.utils.date.Month;
import org.springframework.stereotype.Component;

/**
 * Contains common arrays used in several parts of the application
 * 
 * @author Ricardo Memória
 *  
 */
@Component
public class GlobalLists {

//	private List<SelectItem> numberOfAFBs;

	private static final PatientType patientTypes[] = {
			PatientType.NEW,
			PatientType.TRANSFER_IN,
			PatientType.RELAPSE,
			PatientType.AFTER_DEFAULT,
			PatientType.FAILURE_FT,
			PatientType.FAILURE_RT,
			PatientType.RESISTANCE_PATTERN_CHANGED,
			PatientType.OTHER
	};

	private static final PatientType patientTypesDRTB[] = {
			PatientType.NEW,
			PatientType.RELAPSE,
			PatientType.AFTER_DEFAULT,
			PatientType.FAILURE_FT,
			PatientType.FAILURE_RT,
			PatientType.OTHER
	};

	private static final PatientType patientTypesTB[] = {
			PatientType.NEW,
			PatientType.PREVIOUSLY_TREATED,
			PatientType.UNKNOWN_PREVIOUS_TB_TREAT
	};

	private static final PatientType patientTypesALL[] = {
			PatientType.NEW,
			PatientType.PREVIOUSLY_TREATED,
			PatientType.UNKNOWN_PREVIOUS_TB_TREAT,
			PatientType.RELAPSE,
			PatientType.AFTER_DEFAULT,
			PatientType.FAILURE_FT,
			PatientType.FAILURE_RT,
			PatientType.OTHER
	};

	private static final PatientType prevTreatedTypes[] = {
			PatientType.RELAPSE,
			PatientType.TREATMENT_AFTER_FAILURE,
			PatientType.TREATMENT_AFTER_LOSS_FOLLOW_UP,
			PatientType.OTHER_PREVIOUSLY_TREATED
	};
	
	private static final CaseState caseStates[] = {
		CaseState.WAITING_TREATMENT,
		CaseState.ONTREATMENT,
		CaseState.TRANSFERRING,
		CaseState.CURED,
		CaseState.TREATMENT_COMPLETED,
		CaseState.FAILED,
		CaseState.DEFAULTED,
		CaseState.DIED,
		CaseState.TRANSFERRED_OUT,
		CaseState.DIAGNOSTIC_CHANGED,
		CaseState.OTHER,
		CaseState.MDR_CASE,
		CaseState.TREATMENT_INTERRUPTION,
		CaseState.NOT_CONFIRMED, 
		CaseState.DIED_NOTTB
	};
	

	private static final PrevTBTreatmentOutcome prevTBTreatmentOutcomes[] = {
		PrevTBTreatmentOutcome.CURED,
		PrevTBTreatmentOutcome.COMPLETED,
		PrevTBTreatmentOutcome.FAILURE,
		PrevTBTreatmentOutcome.DEFAULTED,
		PrevTBTreatmentOutcome.SCHEME_CHANGED,
		PrevTBTreatmentOutcome.TRANSFERRED_OUT,
		PrevTBTreatmentOutcome.UNKNOWN
	};

	private static final TbField tbfields[] = {
		TbField.COMORBIDITY,
		TbField.SIDEEFFECT,
		TbField.DST_METHOD,
		TbField.CULTURE_METHOD,
		TbField.CONTACTCONDUCT,
		TbField.CONTACTTYPE,
		TbField.XRAYPRESENTATION,
		TbField.PULMONARY_TYPES,
		TbField.EXTRAPULMONARY_TYPES,
		TbField.ADJUSTMENT
	};
	
	private static final TbCategory tbcategories[] = {
		TbCategory.CATEGORY_I,
		TbCategory.CATEGORY_II,
		TbCategory.CATEGORY_III
	};
	
	private static final CultureResult cultureResults[] = {
		CultureResult.NEGATIVE,
		CultureResult.POSITIVE,
		CultureResult.PLUS,
		CultureResult.PLUS2,
		CultureResult.PLUS3,
		CultureResult.CONTAMINATED
	};
	
	private static final MicroscopyResult microscopyResults[] = {
		MicroscopyResult.NEGATIVE,
		MicroscopyResult.POSITIVE,
		MicroscopyResult.PLUS,
		MicroscopyResult.PLUS2,
		MicroscopyResult.PLUS3,
		MicroscopyResult.PENDING
	};
	
	private static final CultureResult cultureOptionsNotif[] = {
		CultureResult.NOTDONE,
		CultureResult.NEGATIVE,
		CultureResult.POSITIVE,
		CultureResult.PLUS,
		CultureResult.PLUS2,
		CultureResult.PLUS3,
		CultureResult.CONTAMINATED
	};

	private static final MicroscopyResult microscopyNotifOptions[] = {
		MicroscopyResult.NOTDONE,
		MicroscopyResult.NEGATIVE,
		MicroscopyResult.POSITIVE,
		MicroscopyResult.PLUS,
		MicroscopyResult.PLUS2,
		MicroscopyResult.PLUS3
	};

	private CaseClassification caseClassifications[] = {
		CaseClassification.TB,
		CaseClassification.DRTB,
		CaseClassification.NTM
	};

	private static final HIVResult hivResults[] = {
		HIVResult.ONGOING,
		HIVResult.NEGATIVE,
		HIVResult.POSITIVE
	};
	
	private static final DstResult dstResults[] = {
		DstResult.NOTDONE,
		DstResult.SUSCEPTIBLE,
		DstResult.RESISTANT,
		DstResult.CONTAMINATED,
		DstResult.ONGOING
	};

	
	private static final DrugResistanceType drugResistanceTypes[] = {
		DrugResistanceType.MONO_RESISTANCE,
		DrugResistanceType.MONO_RESISTANCE_RIF,
		DrugResistanceType.POLY_RESISTANCE,
		DrugResistanceType.POLY_RESISTANCE_RIF,
		DrugResistanceType.MULTIDRUG_RESISTANCE,
		DrugResistanceType.EXTENSIVEDRUG_RESISTANCE
	};

	private static final SampleType microscopySampleTypes[] = {
			SampleType.SPUTUM,
			SampleType.OTHER
	};

	private static final VisualAppearance microscopyVisualAppearence[] = {
			VisualAppearance.BLOOD_STAINED,
			VisualAppearance.MUCOPURULENT,
			VisualAppearance.SALIVA
	};

	private static final ExamStatus examStatusOptions[] = {
			ExamStatus.REQUESTED,
			ExamStatus.ONGOING,
			ExamStatus.PERFORMED
	};

	private static final Month months[] = {
			Month.JANUARY,
			Month.FEBRUARY,
			Month.MARCH,
			Month.APRIL,
			Month.MAY,
			Month.JUNE,
			Month.JULY,
			Month.AUGUST,
			Month.SEPTEMBER,
			Month.OCTOBER,
			Month.NOVEMBER,
			Month.DECEMBER
	};

	/**
	 * Return a value of a property of a component called enumList + workspace extension
	 * @param <E>
	 * @param propertyName
	 * @param type
	 * @param result
	 * @return
	 */
	protected <E> E getComponentValueWorkspace(String propertyName, Class<E> type, Object result) {
		Workspace defaultWorkspace = UserSession.getWorkspace();
		
		if ((defaultWorkspace == null) || (defaultWorkspace.getExtension() == null))
			return (E)result;

		try {
			String s = "globalLists_" + defaultWorkspace.getExtension();
			Object obj = App.getComponent(s);

			E val = (E)PropertyUtils.getProperty(obj, propertyName);
			return (val == null? (E)result: val);
		} catch (Exception e) {
			return (E)result;
		}
	}
	

	/**
	 * List of sample types (sputum or others), used in microscopy and culture exams 
	 * @return
	 */
	public SampleType[] getSampleTypes() {
		return SampleType.values();
	}

	
	/**
	 * Return the validation states used in cases flow
	 * @return
	 */
	public ValidationState[] getValidationState() {
		return ValidationState.values();
	}


	/**
	 * Return dispensing frequencies
	 * @return Array of {@link DispensingFrequency}
	 */
	public DispensingFrequency[] createDispensingFrequencies() {
		return DispensingFrequency.values();
	}


	/**
	 * Returns regimen phases (intensive or continuous)
	 * @return - array of RegimenPhase enumerations
	 */
	public RegimenPhase[] getRegimenPhase() {
		return RegimenPhase.values();
	}


	public CaseState[] getCaseStates() {
		return (CaseState[])getComponentValueWorkspace("caseStates", CaseState[].class, caseStates);
	}


	/**
	 * Returns options for health unit filter in case searching 
	 * @return - array of FilterHealthUnit enumerations
	 */
/*	public FilterHealthUnit[] getFilterHealthUnit() {
		return FilterHealthUnit.values();
	}
*/

	/**
	 * Returns list of TB fields available for drop down menus
	 * @return array of TbField enum
	 */
	public TbField[] getTbFields() {
		return getComponentValueWorkspace("tbFields", TbField[].class, tbfields);
	}


	/**
	 * Returns an array of available gender enumerations
	 * @return array of the enum Gender
	 */
	public Gender[] getGenders() {
		return Gender.values();
	}


	/**
	 * Returns the localities type (urban or rural)
	 * @return array of enumeration LocalityType
	 */
	public LocalityType[] getLocalityTypes() {
		return LocalityType.values();
	}


	public ForecastNewCaseFreq[] getForecastNewCaseFreqs() {
		return ForecastNewCaseFreq.values();
	}
	
	/**
	 * Returns an array of available gender enumerations
	 * @return array of the enum Gender
	 */
	public MedicineLine[] getMedicineLines() {
		return MedicineLine.values();
	}

	
	/**
	 * Returns an array of weekly frequencies in the format n/7 to be selected in a JSF component like selectOneMenu
	 * @return List of SelectItem objects containing the weekly frequency
	 */
/*	@Factory("frequencies")
	public List<SelectItem> getFrequencies() {
		List<SelectItem> lst = new ArrayList<SelectItem>();
		for (int i = 1; i <= 7; i++) {
			SelectItem it = new SelectItem();
			it.setLabel(Integer.toString(i) + "/7");
			it.setValue((Integer)i);
			lst.add(it);
		}
		return lst;
	}
*/
	private static final Nationality nationalities[] = {
		Nationality.NATIVE,
		Nationality.FOREIGN
	};

	public static final InfectionSite infectionSite[] =  {
			InfectionSite.PULMONARY,
			InfectionSite.EXTRAPULMONARY,
			InfectionSite.BOTH
	};

	private static final XpertResult xpertResults[] = {
			XpertResult.TB_DETECTED,
			XpertResult.TB_NOT_DETECTED,
			XpertResult.INVALID_NORESULT_ERROR
	};
	
	public Nationality[] getNationalities() {
		return nationalities;
	}
	
	public InfectionSite[] getInfectionSite() {
		return getComponentValueWorkspace("infectionSite", InfectionSite[].class, infectionSite);
	}

	
	public CaseClassification[] getCaseClassifications() {
		return getComponentValueWorkspace("caseClassifications", CaseClassification[].class, caseClassifications);
		/*if (caseClassifications == null) {
			List lst = getUserCaseClassifications();
			caseClassifications = (CaseClassification[])lst.toArray(new CaseClassification[lst.size()]);
		}
		return caseClassifications;*/
	}
	
	public List<CaseClassification> getUserCaseClassifications() {
		ArrayList<CaseClassification> lst = new ArrayList<CaseClassification>();
		UserSession session = UserSession.instance();
		for (CaseClassification cla: CaseClassification.values())
			if (session.hasRole(cla.toString() + "_CASE_VIEW"))
				lst.add(cla);
		return lst;
	}


	public PrevTBTreatmentOutcome[] getPrevTBTreatmentOutcomes() {
		return getComponentValueWorkspace("prevTBTreatmentOutcomes", PrevTBTreatmentOutcome[].class, prevTBTreatmentOutcomes);
	}
	
	public ExtraOutcomeInfo[] getExtraOutcomesInfo() {
		return ExtraOutcomeInfo.values();
	}
	
	public MicroscopyResult[] getMicroscopyResults() {
		return microscopyResults;
	}
	
	public CaseState[] getCaseState() {
		return CaseState.values();
	}
	
	public HIVResult[] getHIVResults() {
		return getComponentValueWorkspace("hivResults", HIVResult[].class, hivResults);
	}
	
	public CultureResult[] getCultureResults() {
		return getComponentValueWorkspace("cultureResults", CultureResult[].class, cultureResults);
	}
	
	public XRayResult[] getXRayResults() {
		return XRayResult.values();
	}
	
	public BufferStockMeasure[] getBufferStockMeasures() {
		return BufferStockMeasure.values();
	}
	
	public DstResult[] getDstResults() {
		return getComponentValueWorkspace("dstResults", DstResult[].class, dstResults);
	}
	
	public MedAppointmentType[] getMedAppointmentTypes() {
		return MedAppointmentType.values();
	}
	
/*	public List<SelectItem> getWeekFrequencies() {
		List<SelectItem> lst = new ArrayList<SelectItem>();
		
		SelectItem si = new SelectItem();
		si.setValue(null);
		si.setLabel("-");
		lst.add(si);
		
		for (int i = 1; i <= 7; i++) {
			si = new SelectItem();
			si.setValue(i);
			si.setLabel(i + "/7");
			lst.add(si);
		}
		
		return lst;
	}
*/	
	public YesNoType[] getYesNoTypeList() {
		return YesNoType.values();
	}
	
/*	public List<SelectItem> getChartTypes() {
		Map<String, String> messages = Messages.instance();

		List<SelectItem> lst = new ArrayList<SelectItem>();
		lst.add(new SelectItem(1, messages.get("charts.hbar")));
		lst.add(new SelectItem(2, messages.get("charts.pie")));
		return lst;
	}
*/	
	public XRayBaseline[] getXRayBaselines() {
		return XRayBaseline.values();
	}
	
	public XRayEvolution[] getXRayEvolutions() {
		return XRayEvolution.values();
	}

	/**
	 * Return array of user states
	 * @return Array of {@link UserState}
	 */
	public UserState[] getUserStates() {
		return UserState.values();
	}
	
	/**
	 * Return array of visions in the country available for a user 
	 * @return Array of {@link UserView}
	 */
	public UserView[] getUserViews() {
		return UserView.values();
	}
	
	
	public NameComposition[] getNamesComposition() {
		return NameComposition.values();
	}
	
	public TbCategory[] getTbCategories() {
		return tbcategories;
	}
	
	public DiagnosisType[] getDiagnosisTypes() {
		return DiagnosisType.values();
	}
	
	//@Factory("drugResistanceTypes")
	//public DrugResistanceType[] getDrugResistanceTypes() {
	//	return DrugResistanceType.values();
	//}
	
	public DrugResistanceType[] getDrugResistanceTypes() {
		return getComponentValueWorkspace("drugResistanceTypes", DrugResistanceType[].class, drugResistanceTypes);
	}
	
	public DisplayCaseNumber[] getDisplayCaseNumbers() {
		return DisplayCaseNumber.values();
	}
	
	public CultureResult[] getCultureNotifOptions() {
		return cultureOptionsNotif;
	}
	
	public MicroscopyResult[] getMicroscopyNotifOptions() {
		return microscopyNotifOptions;
	}

	public SampleType[] getMicroscopySampleTypes() {
		return getComponentValueWorkspace("microscopySampleTypes", SampleType[].class, microscopySampleTypes);
	}

	public VisualAppearance[] getMicroscopyVisualAppearence() {
		return getComponentValueWorkspace("microscopyVisualAppearence", VisualAppearance[].class, microscopyVisualAppearence);
	}

	public ExamStatus[] getExamStatusOptions() {
		return examStatusOptions;
	}

	public static GlobalLists instance(){
		return (GlobalLists)App.getComponent(GlobalLists.class);
	}

	public PatientType[] getPatientTypes() {
		return getComponentValueWorkspace("patientTypes", PatientType[].class, patientTypes);
	}

	public PatientType[] getPatientTypesTB() {
		return getComponentValueWorkspace("patientTypesTB", PatientType[].class, patientTypesTB);
	}

	public PatientType[] getPatientTypesDRTB() {
		return getComponentValueWorkspace("patientTypesDRTB", PatientType[].class, patientTypesDRTB);
	}

	public PatientType[] getPatientTypesALL() {
		return getComponentValueWorkspace("patientTypesALL", PatientType[].class, patientTypesALL);
	}

	public PatientType[] getPrevTreatedTypes() {
		return getComponentValueWorkspace("prevTreatedTypes", PatientType[].class, prevTreatedTypes);
	}

	public XpertResult[] getXpertResults(){
		return getComponentValueWorkspace("xpertResults", XpertResult[].class, xpertResults);
	}

	public Month[] getMonths() {
		return getComponentValueWorkspace("months", Month[].class, months);
	}

}
