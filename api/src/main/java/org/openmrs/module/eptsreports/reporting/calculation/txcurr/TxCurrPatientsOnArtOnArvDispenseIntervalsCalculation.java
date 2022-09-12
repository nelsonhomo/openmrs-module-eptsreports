package org.openmrs.module.eptsreports.reporting.calculation.txcurr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.LastFilaProcessor;
import org.openmrs.module.eptsreports.reporting.utils.EptsListUtils;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public abstract class TxCurrPatientsOnArtOnArvDispenseIntervalsCalculation
    extends BaseFghCalculation {

  private static final int CHUNK_SIZE = 1000;

  private static int CONCEPT_QUARTERLY = 23720;
  private static int CONCEPT_QUARTERLY_DISPENSATION = 23730;
  private static int CONCEPT_SEMESTER_ARV_PICKUP = 23888;
  private static int CONCEPT_ANNUAL_ARV_PICKUP = 165314;

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    return calculateDisagregration(context);
  }

  private CalculationResultMap calculateDisagregration(EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    Map<Integer, PatientDisaggregated> resultDisaggregated = new HashMap<>();

    Map<Integer, Date> mapFichaClinicaSource =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastMarkedInModelosDiferenciadosDeCuidadosOrTipoDeLevantamentoOnFichaClinicaMasterCard(
                context);

    addSemiAnnualValues(context, resultDisaggregated, mapFichaClinicaSource);
    addQuarterlyValues(context, resultDisaggregated, mapFichaClinicaSource);
    addMonthlyValues(context, resultDisaggregated);

    this.evaluateDisaggregatedPatients(context, resultMap, resultDisaggregated);

    return resultMap;
  }

  protected abstract void evaluateDisaggregatedPatients(
      EvaluationContext context,
      CalculationResultMap resultMap,
      Map<Integer, PatientDisaggregated> resultDisaggregated);

  protected void addMonthlyValues(
      EvaluationContext context, Map<Integer, PatientDisaggregated> resultDisaggregated) {

    Map<Integer, Date> mapMonthlyFichaClinicaSource =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastTipoDeLevantamentoMensalOnFichaClinicaMasterCard(context);

    Map<Integer, Date> mapMonthlyFilaSource =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastLevantamentoOnFilaForInterval(context, DisaggregationInterval.MONTHLY);

    List<Integer> allPatientIds = new ArrayList<Integer>(mapMonthlyFichaClinicaSource.keySet());
    allPatientIds.addAll(mapMonthlyFilaSource.keySet());

    List<Integer> cohortFichaClinica = new ArrayList<Integer>();

    for (Integer patientId : allPatientIds) {

      Date fichaClinicaDate = mapMonthlyFichaClinicaSource.get(patientId);
      Date filaDate = mapMonthlyFilaSource.get(patientId);

      if (Objects.nonNull(fichaClinicaDate) && Objects.nonNull(filaDate)) {
        if (fichaClinicaDate.compareTo(filaDate) > 0) {
          cohortFichaClinica.add(patientId);
        } else {
          resultDisaggregated.put(
              patientId, new DispensaMensalPatientDisaggregated(patientId, filaDate));
        }
        continue;
      }

      if (Objects.nonNull(fichaClinicaDate) && Objects.isNull(filaDate)) {
        cohortFichaClinica.add(patientId);
        continue;
      }

      if (Objects.isNull(fichaClinicaDate) && Objects.nonNull(filaDate)) {
        resultDisaggregated.put(
            patientId, new DispensaMensalPatientDisaggregated(patientId, filaDate));
        continue;
      }
    }
    Map<Integer, Date> monthlyValues = new HashMap<Integer, Date>();
    final int slices = EptsListUtils.listSlices(cohortFichaClinica, CHUNK_SIZE);
    for (int position = 0; position < slices; position++) {

      final List<Integer> patientIds =
          cohortFichaClinica.subList(
              position * CHUNK_SIZE,
              (((position * CHUNK_SIZE) + CHUNK_SIZE) < cohortFichaClinica.size()
                  ? (position * CHUNK_SIZE) + CHUNK_SIZE
                  : cohortFichaClinica.size()));

      monthlyValues.putAll(
          Context.getRegisteredComponents(LastFilaProcessor.class)
              .get(0)
              .getValuesForMensalOnFichaClinicaMasterCard(context, patientIds));
    }

    for (Entry<Integer, Date> iter : monthlyValues.entrySet()) {
      resultDisaggregated.put(
          iter.getKey(), new DispensaMensalPatientDisaggregated(iter.getKey(), iter.getValue()));
    }
  }

  protected void addQuarterlyValues(
      EvaluationContext context,
      Map<Integer, PatientDisaggregated> resultDisaggregated,
      Map<Integer, Date> mapFichaClinicaSource) {

    Map<Integer, Date> mapQuarterlFilaSource =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastLevantamentoOnFilaForInterval(context, DisaggregationInterval.QUARTERLY);

    List<Integer> allPatientIds = new ArrayList<Integer>(mapFichaClinicaSource.keySet());
    allPatientIds.addAll(mapQuarterlFilaSource.keySet());

    List<Integer> cohortFichaClinica = new ArrayList<Integer>();

    for (Integer patientId : allPatientIds) {

      Date fichaClinicaDate = mapFichaClinicaSource.get(patientId);
      Date filaDate = mapQuarterlFilaSource.get(patientId);

      if (Objects.nonNull(fichaClinicaDate) && Objects.nonNull(filaDate)) {
        if (fichaClinicaDate.compareTo(filaDate) > 0) {
          cohortFichaClinica.add(patientId);
        } else {
          resultDisaggregated.put(
              patientId, new DispensaTrimestralPatientDisaggregated(patientId, filaDate));
        }
        continue;
      }

      if (Objects.nonNull(fichaClinicaDate) && Objects.isNull(filaDate)) {
        cohortFichaClinica.add(patientId);
        continue;
      }

      if (Objects.isNull(fichaClinicaDate) && Objects.nonNull(filaDate)) {
        resultDisaggregated.put(
            patientId, new DispensaTrimestralPatientDisaggregated(patientId, filaDate));
        continue;
      }
    }
    Map<Integer, Date> fichaClinicaResults =
        this.findAndAddFromFichaClinica(
            context,
            cohortFichaClinica,
            CONCEPT_QUARTERLY,
            Arrays.asList(CONCEPT_QUARTERLY_DISPENSATION));

    for (Entry<Integer, Date> iter : fichaClinicaResults.entrySet()) {
      resultDisaggregated.put(
          iter.getKey(),
          new DispensaTrimestralPatientDisaggregated(iter.getKey(), iter.getValue()));
    }
  }

  protected void addSemiAnnualValues(
      EvaluationContext context,
      Map<Integer, PatientDisaggregated> resultDisaggregated,
      Map<Integer, Date> mapFichaClinicaSource) {

    Map<Integer, Date> mapSemiAnnnualFilaSource =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastLevantamentoOnFilaForInterval(context, DisaggregationInterval.SEMI_ANNUAL);

    List<Integer> allPatientIds = new ArrayList<Integer>(mapFichaClinicaSource.keySet());
    allPatientIds.addAll(mapSemiAnnnualFilaSource.keySet());

    List<Integer> cohortFichaClinica = new ArrayList<Integer>();

    for (Integer patientId : allPatientIds) {

      Date fichaClinicaDate = mapFichaClinicaSource.get(patientId);
      Date filaDate = mapSemiAnnnualFilaSource.get(patientId);

      if (Objects.nonNull(fichaClinicaDate) && Objects.nonNull(filaDate)) {
        if (fichaClinicaDate.compareTo(filaDate) > 0) {
          cohortFichaClinica.add(patientId);
        } else {
          resultDisaggregated.put(
              patientId, new DispensaSemestralPatientDisaggregated(patientId, filaDate));
        }
        continue;
      }

      if (Objects.nonNull(fichaClinicaDate) && Objects.isNull(filaDate)) {
        cohortFichaClinica.add(patientId);
        continue;
      }

      if (Objects.isNull(fichaClinicaDate) && Objects.nonNull(filaDate)) {
        resultDisaggregated.put(
            patientId, new DispensaSemestralPatientDisaggregated(patientId, filaDate));
        continue;
      }
    }
    Map<Integer, Date> fichaClinicaResults =
        this.findAndAddFromFichaClinica(
            context,
            cohortFichaClinica,
            CONCEPT_SEMESTER_ARV_PICKUP,
            Arrays.asList(CONCEPT_ANNUAL_ARV_PICKUP, CONCEPT_SEMESTER_ARV_PICKUP));

    for (Entry<Integer, Date> iter : fichaClinicaResults.entrySet()) {
      resultDisaggregated.put(
          iter.getKey(), new DispensaSemestralPatientDisaggregated(iter.getKey(), iter.getValue()));
    }
  }

  private Map<Integer, Date> findAndAddFromFichaClinica(
      EvaluationContext context,
      List<Integer> cohortFichaClinica,
      Integer valueCodedTipoDeLevantamento,
      List<Integer> valueCodedMDCs) {

    Map<Integer, Date> fetchedValues = new HashMap<Integer, Date>();
    final int slices = EptsListUtils.listSlices(cohortFichaClinica, CHUNK_SIZE);
    for (int position = 0; position < slices; position++) {

      final List<Integer> patientIds =
          cohortFichaClinica.subList(
              position * CHUNK_SIZE,
              (((position * CHUNK_SIZE) + CHUNK_SIZE) < cohortFichaClinica.size()
                  ? (position * CHUNK_SIZE) + CHUNK_SIZE
                  : cohortFichaClinica.size()));

      fetchedValues.putAll(
          Context.getRegisteredComponents(LastFilaProcessor.class)
              .get(0)
              .getValuesForModelosDiferenciadosDeCuidadosOrTipoDeLevantamentoOnFichaClinicaMasterCard(
                  context, patientIds, valueCodedTipoDeLevantamento, valueCodedMDCs));
    }
    return fetchedValues;
  }

  public enum DisaggregationInterval {
    MONTHLY,

    QUARTERLY,

    SEMI_ANNUAL
  }

  public abstract class PatientDisaggregated {

    private Integer patientId;
    private Date date;

    public PatientDisaggregated(Integer patientId, Date date) {
      this.patientId = patientId;
      this.date = date;
    }

    public Integer getPatientId() {
      return patientId;
    }

    public Date getDate() {
      return date;
    }

    public abstract DisaggregationInterval getDisaggregationInterval();
  }

  public class DispensaMensalPatientDisaggregated extends PatientDisaggregated {
    public DispensaMensalPatientDisaggregated(Integer patientId, Date date) {
      super(patientId, date);
    }

    @Override
    public DisaggregationInterval getDisaggregationInterval() {
      return DisaggregationInterval.MONTHLY;
    }
  }

  public class DispensaTrimestralPatientDisaggregated extends PatientDisaggregated {
    public DispensaTrimestralPatientDisaggregated(Integer patientId, Date date) {
      super(patientId, date);
    }

    @Override
    public DisaggregationInterval getDisaggregationInterval() {
      return DisaggregationInterval.QUARTERLY;
    }
  }

  public class DispensaSemestralPatientDisaggregated extends PatientDisaggregated {
    public DispensaSemestralPatientDisaggregated(Integer patientId, Date date) {
      super(patientId, date);
    }

    @Override
    public DisaggregationInterval getDisaggregationInterval() {
      return DisaggregationInterval.SEMI_ANNUAL;
    }
  }
}
