package org.openmrs.module.eptsreports.reporting.library.queries.data.quality.duplicate.ficharesumo;

public interface EC3DuplicateFichaResumoQueries {
  class QUERY {

    public static String findPatientsWithMoreThanOneFCInTheSameDate =
        "SELECT   "
            + "pe.person_id As patient_id,   "
            + "pid.identifier AS NID,   "
            + "concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) AS Name,  "
            + "DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate,   "
            + "IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob,   "
            + "pe.gender AS Sex, DATE_FORMAT(pe.date_created, '%d-%m-%Y') AS First_entry_date,   "
            + "DATE_FORMAT(pe.date_changed, '%d-%m-%Y') AS Last_updated,   "
            + "DATE_FORMAT(programState.date_enrolled, '%d-%m-%Y') AS date_enrolled,   "
            + "case   "
            + "when programState.state = 9 then 'DROPPED FROM TREATMENT'   "
            + "when programState.state = 6 then 'ACTIVE ON PROGRAM'   "
            + "when programState.state = 10 then 'PATIENT HAS DIED'   "
            + "when programState.state = 8 then 'SUSPENDED TREATMENT'   "
            + "when programState.state = 7 then 'TRANSFERED OUT TO ANOTHER FACILITY'   "
            + "when programState.state = 29 then 'TRANSFERRED FROM OTHER FACILTY'   "
            + "end AS state,   "
            + "DATE_FORMAT(programState.start_date, '%d-%m-%Y') AS state_date,  "
            + "final.encounter_datetime_primeira, "
            + "final.encounter_datetime_qualquer, "
            + "final.date_created, "
            + "l.name AS location_name  "
            + "FROM person pe   "
            + "inner join  "
            + "( "
            + "select "
            + "primeira.patient_id, "
            + "primeira.encounter_datetime_primeira, "
            + "primeira.date_created, "
            + "qualquer.encounter_datetime_qualquer, "
            + "primeira.encounter_type_primeira, "
            + "qualquer.encounter_type_qualquer "
            + "from "
            + "( "
            + "SELECT p.patient_id,date(e.encounter_datetime) encounter_datetime_primeira,e.encounter_type encounter_type_primeira,e.encounter_id encounter_id_primeira, e.location_id location_id_primeira, e.date_created "
            + "FROM patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and date(e.encounter_datetime)>='2019-11-01'and date(e.encounter_datetime)<=curdate() and e.location_id in (:location) "
            + ")primeira "
            + "inner join "
            + "( "
            + "SELECT p.patient_id,date(e.encounter_datetime) encounter_datetime_qualquer, e.encounter_type encounter_type_qualquer,e.encounter_id encounter_id_qualquer,e.location_id location_id_qualquer "
            + "FROM patient p inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and date(e.encounter_datetime)>='2019-11-01' and date(e.encounter_datetime)<=curdate() and e.location_id in (:location) "
            + ")qualquer on qualquer.patient_id=primeira.patient_id and date(qualquer.encounter_datetime_qualquer)=date(primeira.encounter_datetime_primeira) and primeira.encounter_id_primeira<>qualquer.encounter_id_qualquer "
            + "where primeira.location_id_primeira=qualquer.location_id_qualquer "
            + "group by primeira.encounter_datetime_primeira,qualquer.encounter_datetime_qualquer  "
            + "order by primeira.encounter_datetime_primeira,qualquer.encounter_datetime_qualquer desc "
            + ")final on final.patient_id=pe.person_id  "
            + "INNER JOIN  "
            + "(  "
            + "select pn1.*   "
            + "from person_name pn1   "
            + "inner join (   "
            + "select person_id, min(person_name_id) id  from person_name   "
            + "where voided = 0   "
            + "group by person_id   "
            + ") pn2   "
            + "where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id   "
            + ") pn on pn.person_id = pe.person_id   "
            + "INNER JOIN (   "
            + "select pid1.*   from patient_identifier pid1   "
            + "inner join  (   "
            + "select patient_id, min(patient_identifier_id) id  from patient_identifier   "
            + "where voided = 0   and location_id in(:location) "
            + "group by patient_id   "
            + ") pid2   "
            + "where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id   "
            + ") pid on pid.patient_id = pe.person_id   "
            + "INNER JOIN location l ON l.location_id = pid.location_id   "
            + "LEFT JOIN (  "
            + "SELECT pg.patient_id, pg.date_enrolled, ps.state, max(ps.start_date) AS start_date  FROM patient_program pg   "
            + "INNER JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id   "
            + "AND ps.start_date IS NOT NULL   "
            + "AND ps.end_date IS NULL   "
            + "AND pg.program_id = 2   "
            + "AND pg.location_id  in (:location) "
            + "GROUP BY pg.patient_id   "
            + ") AS programState ON pe.person_id = programState.patient_id ";

    public static String getEc3Total =
        "SELECT   "
            + "pe.person_id As patient_id   "
            + "FROM person pe   "
            + "inner join  "
            + "( "
            + "select "
            + "primeira.patient_id, "
            + "primeira.encounter_datetime_primeira, "
            + "primeira.date_created, "
            + "qualquer.encounter_datetime_qualquer, "
            + "primeira.encounter_type_primeira, "
            + "qualquer.encounter_type_qualquer "
            + "from "
            + "( "
            + "SELECT p.patient_id,date(e.encounter_datetime) encounter_datetime_primeira,e.encounter_type encounter_type_primeira,e.encounter_id encounter_id_primeira, e.location_id location_id_primeira, e.date_created "
            + "FROM patient p inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and date(e.encounter_datetime)>='2019-11-01' and date(e.encounter_datetime)<=curdate() and e.location_id in (:location)  "
            + ")primeira "
            + "inner join "
            + "( "
            + "SELECT p.patient_id,date(e.encounter_datetime) encounter_datetime_qualquer, e.encounter_type encounter_type_qualquer,e.encounter_id encounter_id_qualquer,e.location_id location_id_qualquer "
            + "FROM patient p inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and date(e.encounter_datetime)>='2019-11-01' and date(e.encounter_datetime)<=curdate() and e.location_id in (:location) "
            + ")qualquer on qualquer.patient_id=primeira.patient_id and date(qualquer.encounter_datetime_qualquer)=date(primeira.encounter_datetime_primeira) and primeira.encounter_id_primeira<>qualquer.encounter_id_qualquer "
            + "where primeira.location_id_primeira=qualquer.location_id_qualquer "
            + "group by primeira.encounter_datetime_primeira,qualquer.encounter_datetime_qualquer  "
            + "order by primeira.encounter_datetime_primeira,qualquer.encounter_datetime_qualquer desc "
            + ")final on final.patient_id=pe.person_id  "
            + "INNER JOIN  "
            + "(  "
            + "select pn1.*   "
            + "from person_name pn1   "
            + "inner join (   "
            + "select person_id, min(person_name_id) id  from person_name   "
            + "where voided = 0   "
            + "group by person_id   "
            + ") pn2   "
            + "where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id   "
            + ") pn on pn.person_id = pe.person_id   "
            + "INNER JOIN (   "
            + "select pid1.*   from patient_identifier pid1   "
            + "inner join  (   "
            + "select patient_id, min(patient_identifier_id) id  from patient_identifier   "
            + "where voided = 0 and location_id in(:location)  "
            + "group by patient_id   "
            + ") pid2   "
            + "where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id   "
            + ") pid on pid.patient_id = pe.person_id   "
            + "INNER JOIN location l ON l.location_id = pid.location_id   "
            + "LEFT JOIN "
            + "(  "
            + "SELECT pg.patient_id, pg.date_enrolled, ps.state, max(ps.start_date) AS start_date  FROM patient_program pg   "
            + "INNER JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id   "
            + "AND ps.start_date IS NOT NULL   "
            + "AND ps.end_date IS NULL   "
            + "AND pg.program_id = 2 "
            + "AND pg.location_id  in (:location)   "
            + "GROUP BY pg.patient_id   "
            + ") AS programState ON pe.person_id = programState.patient_id ";
  }
}
