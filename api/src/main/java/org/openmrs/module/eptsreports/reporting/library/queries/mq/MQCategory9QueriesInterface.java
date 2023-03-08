package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface MQCategory9QueriesInterface {

  class QUERY {

    public static final String findPatientsWhoArePregnantDuringInclusionPeriod =
        "Select p.patient_id from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=1982 and e.encounter_datetime between :startInclusionDate and :endInclusionDate and pe.gender='F' ";

    public static final String findPatientsWhoAreBreastfeedingDuringInclusionPeriod =
        "Select p.patient_id from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=6332 and e.encounter_datetime between :startInclusionDate and :endInclusionDate and pe.gender='F' ";

    public static final String findPatientsWhoArePregnantDuringPreviousPeriod =
        "select pregnat.patient_id from (  "
            + "Select p.patient_id, min(e.encounter_datetime) from person pe  "
            + "inner join patient p on pe.person_id=p.patient_id  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  "
            + "o.concept_id=1982 and o.value_coded=1065 and e.encounter_datetime between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 12 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 9 MONTH) "
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
  }
}
