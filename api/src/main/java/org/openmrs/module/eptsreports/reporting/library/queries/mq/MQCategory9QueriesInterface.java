package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface MQCategory9QueriesInterface {

  class QUERY {
    public static final String findPatientsFirstConsultationOnInclusionDate =
        "select firstConsultation.patient_id from (  "
            + "Select p.patient_id, min(e.encounter_datetime) from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location and  "
            + "e.encounter_datetime between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH) "
            + "group by p.patient_id "
            + ")firstConsultation  ";

    public static final String findPatientsWhoArePregnantDuringInclusionPeriod =
        "Select p.patient_id from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=1982 and e.encounter_datetime between  DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH) and pe.gender='F' ";

    public static final String findPatientsWhoAreBreastfeedingDuringInclusionPeriod =
        "Select p.patient_id from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=6332 and e.encounter_datetime between  DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH) and pe.gender='F' ";

    public static final String findPatientsWhoArePregnantDuringPreviousPeriod =
        "select pregnat.patient_id from (  "
            + "Select p.patient_id, min(e.encounter_datetime) from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=1982 and o.value_coded=1065 and e.encounter_datetime between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH) "
            + "group by p.patient_id "
            + ")pregnat  "
            + "where pregnat.patient_id not in( "
            + "Select p.patient_id from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=1982 and o.value_coded=1065  "
            + "and e.encounter_datetime>=DATE_SUB(DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH),INTERVAL 1 DAY), INTERVAL 9 MONTH) and e.encounter_datetime<DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH),INTERVAL 1 DAY) "
            + "group by p.patient_id "
            + ") ";

    public static final String
        findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 = 
                "select firstClinica.patient_id  from  (  "
                + "select p.patient_id,min(e.encounter_datetime) encounter_datetime  from  patient p  "
                + "inner join encounter e on e.patient_id=p.patient_id  "
                + "where p.voided=0 and e.voided=0 and e.location_id=399 and e.encounter_type=6 and e.location_id=:location "
                + "group by p.patient_id  "
                + ") firstClinica  "
                + "inner join obs obsCD4 on obsCD4.person_id=firstClinica.patient_id  "
                + "where obsCD4.obs_datetime=firstClinica.encounter_datetime  "
                + "and obsCD4.concept_id=23722 and obsCD4.value_coded=1695  and obsCD4.voided=0  "
                + "and firstClinica.encounter_datetime between  DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH)  "
                + "and obsCD4.location_id=:location  "
                + "group by obsCD4.person_id  ";


    public static final String
        findPatientsWhithCD4On33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 =
            "select firstClinica.patient_id  from  ( "
                + "select p.patient_id,min(e.encounter_datetime) encounter_datetime  from  patient p "
                + "inner join encounter e on e.patient_id=p.patient_id "
                + "where p.voided=0 and e.voided=0  "
                + "and e.location_id=:location and e.encounter_type=6 "
                + "group by p.patient_id "
                + ") firstClinica "
                + "inner join obs obsCD4 on obsCD4.person_id=firstClinica.patient_id "
                + "where firstClinica.encounter_datetime between  DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH) and "
                + "obsCD4.obs_datetime > firstClinica.encounter_datetime and obsCD4.obs_datetime <=  DATE_ADD(firstClinica.encounter_datetime, INTERVAL 33 DAY) "
                + "and obsCD4.concept_id=23722 and obsCD4.value_coded=1695  and obsCD4.voided=0 "
                + "and obsCD4.location_id = :location ";
  }
}
