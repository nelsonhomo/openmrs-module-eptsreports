package org.openmrs.module.eptsreports.reporting.library.queries;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.EncounterType;

public class TXTBQueries {

  /**
   * Copied straight from INICIO DE TRATAMENTO ARV - NUM PERIODO: INCLUI TRANSFERIDOS DE COM DATA DE
   * INICIO CONHECIDA (SQL) SqlCohortDefinition#91787a86-0362-4820-a4ee-025d5501198b in backup
   *
   * @return sql
   */
  public static String arvTreatmentIncludesTransfersFromWithKnownStartData(
      Integer arvPlanConceptId,
      Integer startDrugsConceptId,
      Integer historicalDrugsStartDateConceptId,
      Integer artProgramId,
      Integer pharmacyEncounterTypeId,
      Integer artAdultFollowupEncounterTypeId,
      Integer artPedFollowupEncounterTypeId) {
    String encounterTypeIds =
        StringUtils.join(
            Arrays.asList(
                pharmacyEncounterTypeId,
                artAdultFollowupEncounterTypeId,
                artPedFollowupEncounterTypeId),
            ",");
    return String.format(
        "SELECT patient_id FROM (SELECT patient_id, Min(data_inicio) data_inicio "
            + "FROM (SELECT p.patient_id, Min(e.encounter_datetime) data_inicio FROM patient p "
            + "INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + "INNER JOIN obs o ON o.encounter_id = e.encounter_id "
            + "WHERE e.voided = 0 AND o.voided = 0 AND p.voided = 0 AND e.encounter_type IN ( %s ) "
            + "AND o.concept_id = %s AND o.value_coded = %s AND e.encounter_datetime <= :endDate "
            + "AND e.location_id = :location GROUP BY p.patient_id "
            + "UNION SELECT p.patient_id, Min(value_datetime) data_inicio FROM patient p "
            + "INNER JOIN encounter e ON p.patient_id = e.patient_id INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + "WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type IN ( %s ) "
            + "AND o.concept_id = %s AND o.value_datetime IS NOT NULL AND o.value_datetime <= :endDate AND e.location_id = :location "
            + "GROUP BY p.patient_id UNION SELECT pg.patient_id, date_enrolled data_inicio FROM patient p "
            + "INNER JOIN patient_program pg ON p.patient_id = pg.patient_id WHERE pg.voided = 0 AND p.voided = 0 "
            + "AND program_id = %s AND date_enrolled <= :endDate AND location_id = :location "
            + "UNION SELECT e.patient_id, Min(e.encounter_datetime) AS data_inicio FROM patient p "
            + "INNER JOIN encounter e ON p.patient_id = e.patient_id WHERE p.voided = 0 AND e.encounter_type = %s "
            + "AND e.voided = 0 AND e.encounter_datetime <= :endDate AND e.location_id = :location GROUP BY p.patient_id) inicio_real "
            + "GROUP BY patient_id)inicio WHERE data_inicio BETWEEN :startDate AND :endDate ",
        encounterTypeIds,
        arvPlanConceptId,
        startDrugsConceptId,
        encounterTypeIds,
        historicalDrugsStartDateConceptId,
        artProgramId,
        pharmacyEncounterTypeId);
  }

  public static String inTBProgramWithinReportingPeriodAtLocation(Integer tbProgramId) {
    return String.format(
        "select pg.patient_id from patient p inner join patient_program pg on "
            + " p.patient_id=pg.patient_id where pg.voided=0 and "
            + " p.voided=0 and program_id=%s "
            + "and date_enrolled between :startDate and :endDate and location_id=:location",
        tbProgramId);
  }

  public static String inTBProgramWithinPreviousReportingPeriodAtLocation(Integer tbProgramId) {
    return String.format(
        "select pg.patient_id from patient p inner join patient_program pg on "
            + " p.patient_id=pg.patient_id where pg.voided=0 and "
            + " p.voided=0 and program_id=%s "
            + "and date_enrolled between date_add(:startDate, interval -6 MONTH) and date_add(:startDate, interval -1 DAY) and location_id=:location",
        tbProgramId);
  }

  public static String dateObs(
      Integer questionId, List<Integer> encounterTypeIds, boolean startDate) {
    String sql =
        String.format(
            "select obs.person_id from obs inner join encounter on encounter.encounter_id = obs.encounter_id "
                + " where obs.concept_id = %s and encounter.encounter_type in(%s) and obs.location_id = :location and obs.voided=0 and ",
            questionId, StringUtils.join(encounterTypeIds, ","));
    if (startDate) {
      sql += "obs.value_datetime >= :startDate and obs.value_datetime <= :endDate";
    } else {
      sql += "obs.value_datetime <= :endDate";
    }
    return sql;
  }

  public static String dateObsForPreviousReportingPeriod(
      Integer questionId, List<Integer> encounterTypeIds, boolean startDate) {
    String sql =
        String.format(
            "select obs.person_id from obs inner join encounter on encounter.encounter_id = obs.encounter_id "
                + " where obs.concept_id = %s and encounter.encounter_type in(%s) and obs.location_id = :location and obs.voided=0 and ",
            questionId, StringUtils.join(encounterTypeIds, ","));
    if (startDate) {
      sql +=
          "obs.value_datetime >= date_add(:startDate, interval -6 MONTH) and obs.value_datetime <= date_add(:startDate, interval -1 DAY)";
    } else {
      sql += "obs.value_datetime <= date_add(:startDate, interval -1 DAY)";
    }
    return sql;
  }

  public static String dateObsByObsDateTimeClausule(
      Integer conceptQuestionId, Integer conceptAnswerId, Integer encounterId) {
    String sql =
        String.format(
            "select distinct obs.person_id from obs "
                + "    inner join encounter on encounter.encounter_id = obs.encounter_id "
                + "  where obs.concept_id =%s and obs.value_coded =%s and encounter.encounter_type =%s "
                + "  and obs.location_id =:location and obs.obs_datetime >=:startDate and obs.obs_datetime <=:endDate and obs.voided=0",
            conceptQuestionId, conceptAnswerId, encounterId);

    return sql;
  }

  public static String dateObsByObsDateTimeClausuleInPreviousReportingPeriod(
      Integer conceptQuestionId, Integer conceptAnswerId, Integer encounterId) {
    String sql =
        String.format(
            "select distinct obs.person_id from obs "
                + "    inner join encounter on encounter.encounter_id = obs.encounter_id "
                + "  where obs.concept_id =%s and obs.value_coded =%s and encounter.encounter_type =%s "
                + "  and obs.location_id =:location and obs.obs_datetime >=date_add(:startDate, interval -6 MONTH) and obs.obs_datetime <=date_add(:startDate, interval -1 DAY) and obs.voided=0",
            conceptQuestionId, conceptAnswerId, encounterId);

    return sql;
  }

  public static String dateObsByObsValueDateTimeClausule(
      Integer conceptQuestionId, Integer conceptAnswerId, Integer encounterId) {
    String sql =
        String.format(
            "select distinct obs.person_id from obs "
                + "   inner join encounter on encounter.encounter_id = obs.encounter_id "
                + "where obs.concept_id =%s and obs.value_coded =%s and encounter.encounter_type =%s "
                + "  and obs.location_id =:location and obs.value_datetime >=:startDate and obs.value_datetime <=:endDate and obs.voided=0",
            conceptQuestionId, conceptAnswerId, encounterId);

    return sql;
  }

  public static String findTBScreeningResultInvestigationBkOrRX() {
    String query =
        "SELECT pat.patient_id FROM patient pat  "
            + "JOIN encounter enc ON pat.patient_id=enc.patient_id  "
            + "JOIN obs obNegative ON enc.encounter_id=obNegative.encounter_id  "
            + "JOIN obs obYesNo ON enc.encounter_id=obYesNo.encounter_id "
            + "WHERE pat.voided=0 AND enc.voided=0 AND obNegative.voided=0 AND obYesNo.voided=0 AND enc.location_id=:location  "
            + "AND enc.encounter_type in(6,9)  AND obNegative.concept_id=6277 AND obNegative.value_coded=664 "
            + "AND obYesNo.concept_id=6257 AND obYesNo.value_coded IN(1065,1066) "
            + "AND enc.encounter_datetime>=:startDate and enc.encounter_datetime<=:endDate ";
    return query;
  }

  public static String findTBScreeningFcMasterCard() {
    String query =
        "Select  p.patient_id  from  patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where   e.voided=0 and o.voided=0 and p.voided=0 "
            + "and e.encounter_type in (6) and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id=:location  "
            + "AND e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate ";
    return query;
  }

  public static String findPatientWhoAreTransferedOut() {
    String query =
        "        select transferidopara.patient_id from  ( "
            + "            select patient_id,max(data_transferidopara) data_transferidopara from  ( "
            + "            select maxEstado.patient_id,maxEstado.data_transferidopara from  ( "
            + "            select pg.patient_id,max(ps.start_date) data_transferidopara  from patient p "
            + "            inner join patient_program pg on p.patient_id=pg.patient_id "
            + "            inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "            where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "            pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location group by p.patient_id "
            + "            ) maxEstado "
            + "            inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "            inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "            where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "            ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state=7 "
            + "            union "
            + "            select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate "
            + "            and e.encounter_type=21 and o.voided=0 and o.concept_id=2016 and o.value_coded in(1706,23863)  and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            union "
            + "            select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on o.encounter_id=e.encounter_id "
            + "            where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 "
            + "            and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            union "
            + "            select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and "
            + "            o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and  e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) transferido "
            + "            group by patient_id "
            + "            ) transferidopara "
            + " "
            + "            inner join "
            + "            ( "
            + " "
            + "            select final.patient_id, final.data_proximo_levantamento as data_transferidopara from ( "
            + "            select * from ( "
            + "            select * from ( "
            + "            select fila.patient_id,fila.data_levantamento, date_add(max(obs_fila.value_datetime), INTERVAL 1 DAY) data_proximo_levantamento from "
            + "            ( "
            + "            select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p "
            + "            inner join encounter e on p.patient_id=e.patient_id "
            + "            where encounter_type=18 and e.encounter_datetime <=:endDate  and e.location_id=:location and e.voided=0 and p.voided=0 "
            + "            group by p.patient_id "
            + "            )fila "
            + "            inner join "
            + "            obs obs_fila on obs_fila.person_id=fila.patient_id "
            + "            and obs_fila.voided=0  and obs_fila.obs_datetime=fila.data_levantamento "
            + "            and obs_fila.concept_id=5096   and obs_fila.location_id=:location "
            + "            group by obs_fila.person_id "
            + "            ) fila "
            + "            union "
            + "            select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 31 DAY) data_proximo_levantamento "
            + "            from patient p "
            + "            inner join encounter e on p.patient_id = e.patient_id "
            + "            inner join obs o on o.encounter_id = e.encounter_id "
            + "            where  e.voided = 0  and p.voided = 0 "
            + "            and o.value_datetime <= :endDate "
            + "            and o.voided = 0 "
            + "            and o.concept_id = 23866 "
            + "            and e.encounter_type=52 "
            + "            and e.location_id=:location "
            + "            group by p.patient_id "
            + "            ) final where final.data_proximo_levantamento between :startDate and :endDate "
            + "            )final "
            + "            ) TR_OUT on TR_OUT.patient_id=transferidopara.patient_id "
            + "            inner join  ( "
            + "            select patient_id,max(encounter_datetime) encounter_datetime from  ( "
            + "            select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "            inner join encounter e on e.patient_id=p.patient_id where  p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "            group by p.patient_id "
            + "            ) consultaLev "
            + "            group by patient_id "
            + "            ) consultaOuARV on transferidopara.patient_id=consultaOuARV.patient_id "
            + "            where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara "
            + "            and transferidopara.data_transferidopara between :startDate AND :endDate ";
    return query;
  }

  public static String dateObsForEncounterAndQuestionAndAnswers(
      Integer encounterId, List<Integer> questionConceptIds, List<Integer> answerConceptIds) {
    String sql =
        String.format(
            "select distinct obs.person_id from obs "
                + "	 inner join encounter on encounter.encounter_id = obs.encounter_id where encounter.encounter_type = %s "
                + "  and obs.concept_id in (%s) and obs.value_coded in (%s) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location "
                + "  and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <=:endDate",
            encounterId,
            StringUtils.join(questionConceptIds, ","),
            StringUtils.join(answerConceptIds, ","));

    return sql;
  }

  public static String dateObsWithinXMonthsBeforeStartDate(
      Integer questionId, List<Integer> encounterTypeIds, Integer xMonths) {
    return String.format(
        "select obs.person_id from obs inner join encounter on encounter.encounter_id = obs.encounter_id "
            + " where obs.concept_id = %s and encounter.encounter_type in(%s) and obs.location_id = :location "
            + " and obs.value_datetime >= DATE_SUB(:startDate, INTERVAL "
            + "%s MONTH) "
            + " and obs.value_datetime < :startDate and obs.voided=0",
        questionId, StringUtils.join(encounterTypeIds, ","), xMonths);
  }

  public static String encounterObs(Integer encounterTypeId) {
    return String.format(
        "select distinct patient_id from encounter where encounter_type =%s and location_id = :location and encounter_datetime <= :endDate and voided=0;",
        encounterTypeId);
  }

  public static String patientWithFirstDrugPickupEncounterInReportingPeriod(
      Integer encounterTypeId) {
    return String.format(
        "SELECT p.patient_id "
            + "FROM patient p "
            + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
            + "WHERE p.voided=0 AND e.encounter_type=%s AND e.voided=0 AND e.encounter_datetime>=:startDate AND e.encounter_datetime<=:endDate AND e.location_id=:location GROUP BY p.patient_id",
        encounterTypeId);
  }

  public static String findNegativeInvestigationResultAndAnyResultForTBScreening(
      EncounterType artAdultFollowupEncounterType,
      EncounterType artPedInicioEncounterType,
      Concept screeningForTBConcept,
      Concept yesConcept,
      Concept noConcept,
      Concept resultForResearchInvestigationConcept,
      Concept negativeResultConcept) {

    return String.format(
        "select p.patient_id from patient p inner join encounter e on p.patient_id=e.patient_id"
            + "	  inner join obs oRastreio on e.encounter_id=oRastreio.encounter_id and oRastreio.concept_id=6257 and oRastreio.voided=0 and oRastreio.value_coded in (1065,1066)"
            + "	  inner join obs oInvestigacao on e.encounter_id=oInvestigacao.encounter_id and oInvestigacao.concept_id=6277 and oInvestigacao.voided=0 and oInvestigacao.value_coded=664"
            + "	 where e.encounter_type in (6,9) and e.voided=0 and e.location_id=:location and e.encounter_datetime between :startDate and :endDate group by p.patient_id",
        screeningForTBConcept.getId(),
        Arrays.asList(yesConcept.getId(), noConcept.getId()),
        resultForResearchInvestigationConcept.getId(),
        negativeResultConcept.getId(),
        Arrays.asList(artAdultFollowupEncounterType.getId(), artPedInicioEncounterType.getId()));
  }

  public static class AbandonedWithoutNotificationParams {
    protected Integer programId;
    protected Integer returnVisitDateConceptId;
    protected Integer returnVisitDateForARVDrugConceptId;
    protected Integer pharmacyEncounterTypeId;
    protected Integer artAdultFollowupEncounterTypeId;
    protected Integer artPedInicioEncounterTypeId;
    protected Integer transferOutStateId;
    protected Integer treatmentSuspensionStateId;
    protected Integer treatmentAbandonedStateId;
    protected Integer deathStateId;

    public AbandonedWithoutNotificationParams programId(Integer programId) {
      this.programId = programId;
      return this;
    }

    public AbandonedWithoutNotificationParams returnVisitDateConceptId(
        Integer returnVisitDateConceptId) {
      this.returnVisitDateConceptId = returnVisitDateConceptId;
      return this;
    }

    public AbandonedWithoutNotificationParams returnVisitDateForARVDrugConceptId(
        Integer returnVisitDateForARVDrugConceptId) {
      this.returnVisitDateForARVDrugConceptId = returnVisitDateForARVDrugConceptId;
      return this;
    }

    public AbandonedWithoutNotificationParams pharmacyEncounterTypeId(
        Integer pharmacyEncounterTypeId) {
      this.pharmacyEncounterTypeId = pharmacyEncounterTypeId;
      return this;
    }

    public AbandonedWithoutNotificationParams artAdultFollowupEncounterTypeId(
        Integer artAdultFollowupEncounterTypeId) {
      this.artAdultFollowupEncounterTypeId = artAdultFollowupEncounterTypeId;
      return this;
    }

    public AbandonedWithoutNotificationParams artPedInicioEncounterTypeId(
        Integer artPedInicioEncounterTypeId) {
      this.artPedInicioEncounterTypeId = artPedInicioEncounterTypeId;
      return this;
    }

    public AbandonedWithoutNotificationParams transferOutStateId(Integer transferOutStateId) {
      this.transferOutStateId = transferOutStateId;
      return this;
    }

    public AbandonedWithoutNotificationParams treatmentSuspensionStateId(
        Integer treatmentSuspensionStateId) {
      this.treatmentSuspensionStateId = treatmentSuspensionStateId;
      return this;
    }

    public AbandonedWithoutNotificationParams treatmentAbandonedStateId(
        Integer treatmentAbandonedStateId) {
      this.treatmentAbandonedStateId = treatmentAbandonedStateId;
      return this;
    }

    public AbandonedWithoutNotificationParams deathStateId(Integer deathStateId) {
      this.deathStateId = deathStateId;
      return this;
    }
  }
}
