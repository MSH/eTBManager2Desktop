package org.msh.etbm.custom.bd;

import org.msh.etbm.custom.bd.entities.enums.*;
import org.msh.etbm.entities.enums.*;
import org.springframework.stereotype.Component;
import org.msh.utils.date.DateUtils;

import java.util.ArrayList;
import java.util.List;


@Component("globalLists_bd")
public class GlobalLists {

	public BiopsyResult[] getBiopsyResult() {
		return BiopsyResult.values();
	}	

	public SkinTestResult[] getSkinTestResult() {
		return SkinTestResult.values();
	}		

	public SalaryRange[] getSalaryRangeList() {
		return SalaryRange.values();
	}

	public Occupation[] getOccupationList() {
		return Occupation.values();
	}


	public DotProvider[] getDotProvTypeList() {
		return DotProvider.values();
	}	

	public PulmonaryTypesBD[]  getPulmonaryTypesBD(){
		return PulmonaryTypesBD.values();
	}

	private static final ReferredTo referredToTypes[] = {
		ReferredTo.PP,
		ReferredTo.GFS,
		ReferredTo.NON_PP,
		ReferredTo.SS,
		ReferredTo.VD, 
		ReferredTo.CV,
		ReferredTo.GOV,
		ReferredTo.PRIVATE_HOSP,
		ReferredTo.TB_PATIENT,
		ReferredTo.OTHER
	};		
	
	private static final TbField tbfields[] = {
		TbField.COMORBIDITY,
		TbField.SIDEEFFECT,
		TbField.DST_METHOD,
		TbField.CULTURE_METHOD,
		TbField.BIOPSY_METHOD,
		TbField.CONTACTCONDUCT,
		TbField.CONTACTTYPE,
		TbField.XRAYPRESENTATION,
		TbField.PULMONARY_TYPES,
		TbField.EXTRAPULMONARY_TYPES,
		TbField.ADJUSTMENT,
        TbField.MEDEXAM_DOTTYPE,
        TbField.MEDEXAM_REFTOTYPE
    };
	
	private static final SampleType sampleType[] = {
		SampleType.SPUTUM,
		SampleType.PUS,
		SampleType.CSF,
		SampleType.URINE,
		SampleType.STOOL,
		SampleType.TISSUE,
		SampleType.OTHER
	};
	
	private static final DrugResistanceType drugResistanceType[] = {
		DrugResistanceType.MONO_RESISTANCE,
		DrugResistanceType.POLY_RESISTANCE,
		DrugResistanceType.MULTIDRUG_RESISTANCE,
		DrugResistanceType.EXTENSIVEDRUG_RESISTANCE,
		DrugResistanceType.UNKNOWN,
	};
	
	private static final QuarterMonths quarter[] = {
		QuarterMonths.FIRST,
		QuarterMonths.SECOND,
		QuarterMonths.THIRD,
		QuarterMonths.FOURTH,
	};
	
	private static final SideEffectGrading sideEffectGradings[] = {
		SideEffectGrading.MILD,
		SideEffectGrading.MODERATE, 
		SideEffectGrading.SEVERE,
	};
	
	private static final SideEffectSeriousness sideEffectSeriousnesses[] = {
		SideEffectSeriousness.NONE,
		SideEffectSeriousness.HOSPITALIZED,
		SideEffectSeriousness.DEAD,
		SideEffectSeriousness.CONGENITAL_ANOMALY,
		SideEffectSeriousness.DISABILITY,
		SideEffectSeriousness.LIFE_THREATNING,
		SideEffectSeriousness.OTHER,
	};
	
	private static final SideEffectAction sideEffectActions[] = {
		SideEffectAction.NONE,
		SideEffectAction.DISCONTINUED,
		SideEffectAction.REDUCED,
		SideEffectAction.SWITCH,
		SideEffectAction.RE_CHALLENGE,
		SideEffectAction.OTHER,
	};
	
	private static final SideEffectOutcome sideEffectOutcomes[] = {
		SideEffectOutcome.UNKNOWN,
		SideEffectOutcome.RESOLVED,
		SideEffectOutcome.RESOLVING,
		SideEffectOutcome.SEQUEALE,
		SideEffectOutcome.NOT_RESOLVED,
		SideEffectOutcome.DEATH,
	};

    private static final PrevTBTreatmentOutcome prevTBTreatmentOutcomes[] = {
            PrevTBTreatmentOutcome.CURED,
            PrevTBTreatmentOutcome.COMPLETED,
            PrevTBTreatmentOutcome.FAILURE,
            PrevTBTreatmentOutcome.DEFAULTED,
			PrevTBTreatmentOutcome.NOT_EVALUATED,
            PrevTBTreatmentOutcome.SCHEME_CHANGED,
            PrevTBTreatmentOutcome.TRANSFERRED_OUT,
            PrevTBTreatmentOutcome.DELAYED_CONVERTER,
            PrevTBTreatmentOutcome.UNKNOWN,
            PrevTBTreatmentOutcome.OTHER
    };

	private static final PatientType patientTypesDRTB[] = {
			PatientType.CATI_NON_CONVERTER,
			PatientType.CATI_FAILURE,
			PatientType.CATI_TREATMENT_AFTER_LOSS_FOLLOW_UP,
			PatientType.CATI_RELAPSE,
			PatientType.CATII_NON_CONVERTER,
			PatientType.CATII_FAILURE,
			PatientType.CATII_TREATMENT_AFTER_LOSS_FOLLOW_UP,
			PatientType.CATII_RELAPSE,
			PatientType.DRTB_TRANSFER_IN,
			PatientType.DRTB_CLOSE_CONTACT_WITHSS_UNKNOWN_HISTORY,
			PatientType.DRTB_CLOSE_CONTACT_WITHSS_NEW,
			PatientType.DRTB_CLOSE_CONTACT_WITHSS_PREVIOUSLY_TREATED,
			PatientType.HIV_INFECTED_WITHSS_UNKNOWN_HISTORY,
			PatientType.HIV_INFECTED_WITHSS_NEW,
			PatientType.HIV_INFECTED_WITHSS_PREVIOUSLY_TREATED,
			PatientType.DRTB_OTHER_UNKNOWN_HISTORY,
			PatientType.DRTB_OTHER_NEW,
			PatientType.DRTB_OTHER_PREVIOUSLY_TREATED
	};

	private static final PatientType patientTypesALL[] = {
			PatientType.NEW,
			PatientType.PREVIOUSLY_TREATED,
			PatientType.UNKNOWN_PREVIOUS_TB_TREAT,
			PatientType.CATI_NON_CONVERTER,
			PatientType.CATI_FAILURE,
			PatientType.CATI_TREATMENT_AFTER_LOSS_FOLLOW_UP,
			PatientType.CATI_RELAPSE,
			PatientType.CATII_NON_CONVERTER,
			PatientType.CATII_FAILURE,
			PatientType.CATII_TREATMENT_AFTER_LOSS_FOLLOW_UP,
			PatientType.CATII_RELAPSE,
			PatientType.DRTB_TRANSFER_IN,
			PatientType.DRTB_CLOSE_CONTACT_WITHSS_UNKNOWN_HISTORY,
			PatientType.DRTB_CLOSE_CONTACT_WITHSS_NEW,
			PatientType.DRTB_CLOSE_CONTACT_WITHSS_PREVIOUSLY_TREATED,
			PatientType.HIV_INFECTED_WITHSS_UNKNOWN_HISTORY,
			PatientType.HIV_INFECTED_WITHSS_NEW,
			PatientType.HIV_INFECTED_WITHSS_PREVIOUSLY_TREATED,
			PatientType.DRTB_OTHER_UNKNOWN_HISTORY,
			PatientType.DRTB_OTHER_NEW,
			PatientType.DRTB_OTHER_PREVIOUSLY_TREATED
	};
	
	public TbField[] getTbFields() {
		return tbfields;
	}

	public ReferredTo[] getReferredtotypes() {
		return referredToTypes;
	}	
	
	
	public static final InfectionSite infectionSite[] =  {
		InfectionSite.PULMONARY,
		InfectionSite.EXTRAPULMONARY
	};

	public InfectionSite[] getInfectionSite() {
		return  infectionSite;
	}

	public static SampleType[] getSampletype() {
		return sampleType;
	}

	public static DrugResistanceType[] getDrugresistancetype() {
		return drugResistanceType;
	}

	public static QuarterMonths[] getQuarter(){
		return quarter;
	}

	public static List<Integer> getQuarterYears(){
		int currYear = DateUtils.yearOf(DateUtils.getDate());
		List<Integer> years;
		
		years = new ArrayList<Integer>();
		for(int i = currYear ; i >= 2010 ; i--){
			years.add(i);
		}
	
		return years;
	}
	
	public static SideEffectGrading[] getSideEffectGradings(){
		return sideEffectGradings;
	}
	
	public static SideEffectSeriousness[] getSideEffectSeriousnesses(){
		return sideEffectSeriousnesses;
	}
	
	public static SideEffectAction[] getSideEffectActions(){
		return sideEffectActions;
	}

    public static SideEffectOutcome[] getSideEffectOutcomes(){
        return sideEffectOutcomes;
    }

    public static PrevTBTreatmentOutcome[] getPrevTBTreatmentOutcomes(){
        return prevTBTreatmentOutcomes;
    }

	public PatientType[] getPatientTypesDRTB() {
		return patientTypesDRTB;
	}

	public PatientType[] getPatientTypesALL() {
		return patientTypesALL;
	}
}
