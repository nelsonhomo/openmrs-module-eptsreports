package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

import org.springframework.stereotype.Component;

@Component
public class EC26Queries {

  public static String getEc26CombinedQuery() {
    String query =
        "SELECT  "
            + "pe.person_id As patient_id,  "
            + "pid.identifier AS NID,  "
            + "concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) AS Name,  "
            + "DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate,  "
            + "IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob,  "
            + "pe.gender AS Sex, DATE_FORMAT(pe.date_created, '%d-%m-%Y') AS First_entry_date,  "
            + "DATE_FORMAT(pe.date_changed, '%d-%m-%Y') AS Last_updated,  "
            + "DATE_FORMAT(programState.date_enrolled, '%d-%m-%Y') AS date_enrolled,  "
            + "case  "
            + "when programState.state = 9 then 'DROPPED FROM TREATMENT'  "
            + "when programState.state = 6 then 'ACTIVE ON PROGRAM'  "
            + "when programState.state = 10 then 'PATIENT HAS DIED'  "
            + "when programState.state = 8 then 'SUSPENDED TREATMENT'  "
            + "when programState.state = 7 then 'TRANSFERED OUT TO ANOTHER FACILITY'  "
            + "when programState.state = 29 then 'TRANSFERRED FROM OTHER FACILTY'  "
            + "end AS state,  "
            + "DATE_FORMAT(programState.start_date, '%d-%m-%Y') AS state_date, "
            + "final.data_inh as data_tpt, "
            + "DATE_FORMAT(final.data_inh, '%d-%m-%Y') AS date_created, "
            + "if(final.tpt_inh=23954,'3HP','ISONIAZIDA') as proflaxia_tpt, "
            + "if(final.tpt_3hp=23955,'DT-INH','DT-3HP') as outras_prescricoes, "
            + "l.name AS location_name "
            + "FROM person pe  "
            + "inner join ( "
            + "select final.patient_id,final.data_inh,final.data_3hp,final.tpt_inh,final.tpt_3hp from ( "
            + "select * from  ( "
            + "select inh.patient_id,inh.data_inh,3hp.data_3hp,inh.tpt_inh,3hp.tpt_3hp from  (  "
            + "select p.patient_id,e.encounter_datetime data_inh, profilaxiaINH.value_coded as tpt_inh from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656    "
            + "and e.encounter_type in (6) and e.location_id in(:location)  "
            + "and e.encounter_datetime <=curdate() "
            + ") inh "
            + "inner join ( "
            + "select 3hp.patient_id,3hp.data_3hp,3hp.tpt_3hp from  (  "
            + "select p.patient_id,e.encounter_datetime data_3hp, profilaxiaINH.value_coded as tpt_3hp from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 1719  and profilaxiaINH.value_coded = 165307    "
            + "and e.encounter_type in (6) and e.location_id in(:location) "
            + "and e.encounter_datetime<=curdate() "
            + ") 3hp "
            + ")3hp on inh.patient_id=3hp.patient_id  "
            + "where inh.data_inh=3hp.data_3hp "
            + ")Inh3hp "
            + "union "
            + "select * from  ( "
            + "select 3hp.patient_id,3hp.data_3hp,inh.data_inh,3hp.tpt_3hp,inh.tpt_inh from (  "
            + "select p.patient_id,e.encounter_datetime data_3hp,profilaxiaINH.value_coded  as tpt_3hp from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 23954    "
            + "and e.encounter_type in (6) and e.location_id in(:location)  "
            + ") 3hp "
            + "inner join ( "
            + "select inh.patient_id,inh.data_inh,inh.tpt_inh from  (  "
            + "select p.patient_id,e.encounter_datetime data_inh,profilaxiaINH.value_coded as tpt_inh from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 1719  and profilaxiaINH.value_coded = 23955    "
            + "and e.encounter_type in (6) and e.location_id in(:location)  "
            + ") inh "
            + ")inh on inh.patient_id=3hp.patient_id  "
            + "where inh.data_inh=3hp.data_3hp "
            + ")3hpInh  "
            + ")final "
            + ")final on final.patient_id=pe.person_id "
            + "INNER JOIN  ( "
            + "select pn1.*  "
            + "from person_name pn1  "
            + "inner join (  "
            + "select person_id, min(person_name_id) id  from person_name  "
            + "where voided = 0  "
            + "group by person_id  "
            + ") pn2  "
            + "where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id  "
            + ") pn on pn.person_id = pe.person_id  "
            + "INNER JOIN (  "
            + "select pid1.*  "
            + "from patient_identifier pid1  "
            + "inner join  (  "
            + "select patient_id, min(patient_identifier_id) id  from patient_identifier  "
            + "where voided = 0  "
            + "group by patient_id  "
            + ") pid2  "
            + "where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id  "
            + ") pid on pid.patient_id = pe.person_id  "
            + "INNER JOIN location l ON l.location_id = pid.location_id  "
            + "LEFT JOIN ( "
            + "SELECT pg.patient_id, pg.date_enrolled, ps.state, max(ps.start_date) AS start_date  FROM patient_program pg  "
            + "INNER JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id  "
            + "AND ps.start_date IS NOT NULL  "
            + "AND ps.end_date IS NULL  "
            + "AND pg.program_id = 2  "
            + "and ps.voided = 0 "
            + "and pg.voided = 0 "
            + "AND pg.location_id  in (:location) "
            + "GROUP BY pg.patient_id  "
            + ") AS programState ON pe.person_id = programState.patient_id ";

    return query;
  }

  public static String getEc26Total() {
    String query =
        "SELECT  "
            + "pe.person_id As patient_id  "
            + "FROM person pe  "
            + "inner join ( "
            + "select final.patient_id,final.data_inh,final.data_3hp,final.tpt_inh,final.tpt_3hp from ( "
            + "select * from  ( "
            + "select inh.patient_id,inh.data_inh,3hp.data_3hp,inh.tpt_inh,3hp.tpt_3hp from  (  "
            + "select p.patient_id,e.encounter_datetime data_inh, profilaxiaINH.value_coded as tpt_inh from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656    "
            + "and e.encounter_type in (6) and e.location_id in(:location)  "
            + "and e.encounter_datetime <=curdate() "
            + ") inh "
            + "inner join "
            + "( "
            + "select 3hp.patient_id,3hp.data_3hp,3hp.tpt_3hp from  (  "
            + "select p.patient_id,e.encounter_datetime data_3hp, profilaxiaINH.value_coded as tpt_3hp from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 1719  and profilaxiaINH.value_coded = 165307    "
            + "and e.encounter_type in (6) and e.location_id in(:location) "
            + "and e.encounter_datetime<=curdate() "
            + ") 3hp "
            + ")3hp on inh.patient_id=3hp.patient_id  "
            + "where inh.data_inh=3hp.data_3hp "
            + ")Inh3hp "
            + "union "
            + "select * from  ( "
            + "select 3hp.patient_id,3hp.data_3hp,inh.data_inh,3hp.tpt_3hp,inh.tpt_inh from (  "
            + "select p.patient_id,e.encounter_datetime data_3hp,profilaxiaINH.value_coded  as tpt_3hp from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 23954    "
            + "and e.encounter_type in (6) and e.location_id in(:location)  "
            + ") 3hp "
            + "inner join ( "
            + "select inh.patient_id,inh.data_inh,inh.tpt_inh from  (  "
            + "select p.patient_id,e.encounter_datetime data_inh,profilaxiaINH.value_coded as tpt_inh from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0    "
            + "and  profilaxiaINH.concept_id = 1719  and profilaxiaINH.value_coded = 23955    "
            + "and e.encounter_type in (6) and e.location_id in(:location)  "
            + ") inh "
            + ")inh on inh.patient_id=3hp.patient_id  "
            + "where inh.data_inh=3hp.data_3hp "
            + ")3hpInh  "
            + ")final "
            + ")final on final.patient_id=pe.person_id "
            + "INNER JOIN  ( "
            + "select pn1.*  "
            + "from person_name pn1  "
            + "inner join (  "
            + "select person_id, min(person_name_id) id  from person_name  "
            + "where voided = 0  "
            + "group by person_id  "
            + ") pn2  "
            + "where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id  "
            + ") pn on pn.person_id = pe.person_id  "
            + "INNER JOIN (  "
            + "select pid1.*  "
            + "from patient_identifier pid1  "
            + "inner join  (  "
            + "select patient_id, min(patient_identifier_id) id  from patient_identifier  "
            + "where voided = 0  "
            + "group by patient_id  "
            + ") pid2  "
            + "where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id  "
            + ") pid on pid.patient_id = pe.person_id  "
            + "INNER JOIN location l ON l.location_id = pid.location_id  "
            + "LEFT JOIN ( "
            + "SELECT pg.patient_id, pg.date_enrolled, ps.state, max(ps.start_date) AS start_date  FROM patient_program pg  "
            + "INNER JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id  "
            + "AND ps.start_date IS NOT NULL  "
            + "AND ps.end_date IS NULL  "
            + "and ps.voided = 0 "
            + "and pg.voided = 0 "
            + "AND pg.program_id = 2  "
            + "AND pg.location_id  in (:location) "
            + "GROUP BY pg.patient_id  "
            + ") AS programState ON pe.person_id = programState.patient_id ";

    return query;
  }
}
