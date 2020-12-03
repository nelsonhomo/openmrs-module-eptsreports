/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

public interface IMR1Queries {

  public class QUERY {

    public static final String findPatientsEnrolledInArtCareDuringReportingPeriod =
        "select art_care.patient_id from( "
            + "select artEnrolled.patient_id,min(artEnrolled.art_enrolled_date) from(             "
            + "select patient.patient_id, min(obs.value_datetime) art_enrolled_date from patient  "
            + "join encounter on encounter.patient_id = patient.patient_id            			 "
            + "join obs on obs.encounter_id = encounter.encounter_id								 "
            + "where patient.voided =0 and encounter.voided = 0 and obs.voided = 0 				    "
            + "and encounter.encounter_type = 53 and obs.concept_id =23808 and obs.value_datetime and "
            + "obs.value_datetime is not null 														"
            + "and obs.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and encounter.location_id =:location "
            + "group by patient.patient_id																   "
            + "union                                                                                        "
            + "select patient.patient_id, min(patient_program.date_enrolled) art_enrolled_date from patient "
            + "join patient_program on patient_program.patient_id = patient.patient_id                      "
            + "join program on program.program_id = patient_program.program_id	  "
            + "where patient.voided = 0 and patient_program.voided = 0 and program.retired =0 "
            + "and program.program_id = 1 and patient_program.date_enrolled is not null       "
            + "and patient_program.date_enrolled between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH)		 "
            + "and patient_program.location_id =:location											 "
            + "group by patient.patient_id) artEnrolled group by artEnrolled.patient_id ) art_care ";

    public static final String findPatientsWhoAreBreastfeeding =
        "select patient_id from ( select inicio_real.patient_id,gravida_real.data_gravida, lactante_real.data_parto,"
            + "if(max(gravida_real.data_gravida) is null and max(lactante_real.data_parto) is null,null, "
            + "if(max(gravida_real.data_gravida) is null,1, "
            + "if(max(lactante_real.data_parto) is null,2, "
            + "if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),1,2)))) decisao from ("
            + "select p.patient_id "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (5,7) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id = :location "
            + "union "
            + "select pg.patient_id from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "where pg.voided=0 and p.voided=0 and program_id in (1,2) and date_enrolled between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + "union "
            + "Select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23891 and o.value_datetime is not null and o.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location )inicio_real "
            + "left join ( "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select 	p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp "
            + "where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and pp.location_id=:location "
            + "union "
            + "Select p.patient_id,obsART.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and "
            + "e.encounter_type=53 and obsART.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "Select p.patient_id,o.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1465 and e.encounter_type=6 and o.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + ") gravida_real on gravida_real.patient_id=inicio_real.patient_id "
            + "left join ( "
            + "Select p.patient_id,o.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and "
            + "e.encounter_type in (5,6) and o.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, obsART.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=:location and "
            + "obsART.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and "
            + "e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pg.patient_id,ps.start_date data_parto from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27 and ps.end_date is null and ps.start_date between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + ") lactante_real on lactante_real.patient_id=inicio_real.patient_id "
            + "where lactante_real.data_parto is not null or gravida_real.data_gravida is not null "
            + "group by inicio_real.patient_id ) gravidaLactante "
            + "inner join person pe on pe.person_id=gravidaLactante.patient_id "
            + "where decisao=1 and pe.voided=0 and pe.gender='F' ";

    public static final String findPatientsWhoArePregnantInAPeriod =
        "select patient_id from ( select inicio_real.patient_id,gravida_real.data_gravida, lactante_real.data_parto,"
            + "if(max(gravida_real.data_gravida) is null and max(lactante_real.data_parto) is null,null, "
            + "if(max(gravida_real.data_gravida) is null,1, "
            + "if(max(lactante_real.data_parto) is null,2, "
            + "if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),1,2)))) decisao from ("
            + "select p.patient_id "
            + "from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (5,7) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id = :location "
            + "union "
            + "select pg.patient_id from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "where pg.voided=0 and p.voided=0 and program_id in (1,2) and date_enrolled between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + "union "
            + "Select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23891 and o.value_datetime is not null and o.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location )inicio_real "
            + "left join ( "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select 	p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp "
            + "where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and pp.location_id=:location "
            + "union "
            + "Select p.patient_id,obsART.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and "
            + "e.encounter_type=53 and obsART.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "Select p.patient_id,o.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1465 and e.encounter_type=6 and o.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + ") gravida_real on gravida_real.patient_id=inicio_real.patient_id "
            + "left join ( "
            + "Select p.patient_id,o.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and "
            + "e.encounter_type in (5,6) and o.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "Select p.patient_id, obsART.value_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=:location and "
            + "obsART.value_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and obsART.concept_id=1190 and obsART.voided=0 "
            + "union "
            + "Select p.patient_id, e.encounter_datetime data_parto from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and "
            + "e.encounter_type in (5,6) and e.encounter_datetime between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and e.location_id=:location "
            + "union "
            + "select pg.patient_id,ps.start_date data_parto from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27 and ps.end_date is null and ps.start_date between (:endDate - INTERVAL 4 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH) and location_id=:location "
            + ") lactante_real on lactante_real.patient_id=inicio_real.patient_id "
            + "where lactante_real.data_parto is not null or gravida_real.data_gravida is not null "
            + "group by inicio_real.patient_id ) gravidaLactante "
            + "inner join person pe on pe.person_id=gravidaLactante.patient_id "
            + "where decisao=2 and pe.voided=0 and pe.gender='F' ";

    public static final String findPatiensTransferredInByEndDate =
        "select distinct patient_id from (                   "
            + "	select patient.patient_id from patient           "
            + "		join patient_program on patient_program.patient_id = patient.patient_id "
            + "		join program on program.program_id = patient_program.program_id  "
            + "		join patient_state on patient_state.patient_program_id = patient_program.patient_program_id  "
            + "	where patient.voided =0 and patient_program.voided =0 and patient_state.voided = 0 and program.retired =0 "
            + "		and program.program_id = 1 and patient_state.state = 28 and patient_state.start_date <=:endDate and patient_program.location_id =:location "
            + "	group by patient.patient_id 																								  "
            + "	union                                                                                                                    	  "
            + "	select patient.patient_id from patient                                                                                        "
            + "		join encounter on encounter.patient_id = patient.patient_id                                                               "
            + "		join obs obsTransf on obsTransf.encounter_id = encounter.encounter_id                                                     "
            + "		join obs obsTypTransf on obsTypTransf.encounter_id = encounter.encounter_id                                               "
            + "		join obs obsOpenDate on obsOpenDate.encounter_id = encounter.encounter_id                                                 "
            + "	where patient.voided = 0 and encounter.voided =0 and obsTransf.voided =0 and obsTypTransf.voided =0 and obsOpenDate.voided =0 "
            + "		and encounter.encounter_type =53 and obsTransf.concept_id = 1369 and obsTransf.value_coded = 1065                         "
            + "		and obsTypTransf.concept_id = 6300 and obsTypTransf.value_coded in (6275,6276)                                            "
            + "		and obsOpenDate.concept_id = 23891 and obsOpenDate.value_datetime is not null and obsOpenDate.value_datetime <=:endDate and encounter.location_id=:location  "
            + "	group by patient.patient_id ) transferred_in                                                                                   ";

    public static final String findPatientsWhoTestedPositiveInCommunityByEndReportingPeriod =
        "select patient.patient_id from patient                         "
            + " join encounter on encounter.patient_id = patient.patient_id  "
            + " join obs on obs.encounter_id = encounter.encounter_id        "
            + "where patient.voided =0 and encounter.voided = 0 and obs.voided =0  "
            + " and encounter.encounter_type =53 and obs.concept_id = 23884 and obs.value_coded = 6245 "
            + " and encounter.encounter_datetime <= :endDate and encounter.location_id =:location        ";

    public static final String findPatientsWhoHaveTestedPositiveOnHivByEndOfReportingPeriod =
        "select patient.patient_id from patient                                                                          "
            + " join encounter on encounter.patient_id = patient.patient_id                                                   "
            + " join obs obsTypeTest on obsTypeTest.encounter_id = encounter.encounter_id                                     "
            + " join obs obsDataInicio on obsDataInicio.encounter_id = encounter.encounter_id                                 "
            + "where patient.voided = 0 and encounter.voided =0 and obsTypeTest.voided = 0 and obsDataInicio.voided =0        "
            + " and encounter.encounter_type = 53 and obsTypeTest.concept_id =22772 and obsTypeTest.value_coded in(1040,1030) "
            + " and obsDataInicio.concept_id =23891 and obsDataInicio.value_datetime is not null "
            + " and obsDataInicio.value_datetime <= (:endDate - INTERVAL 1 MONTH) and encounter.location_id =:location ";
  }
}
