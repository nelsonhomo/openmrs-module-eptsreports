package org.openmrs.module.eptsreports.reporting.calculation.dsd;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.generic.TxRttNextFilaUntilEndDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.txml.TxMLPatientsWhoMissedNextApointmentCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.CalculationProcessorUtils;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class DSDPatientsWhoExperiencedIITCalculation extends BaseFghCalculation {

	@Override
	public CalculationResultMap evaluate(Map<String, Object> parameterValues, EvaluationContext context) {
		CalculationResultMap resultMap = new CalculationResultMap();

		Location location = (Location) context.getParameterValues().get("location");
		Date endDate = (Date) context.getParameterValues().get("endDate");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("endDate", endDate);
		parameters.put("location", location);

		EvaluationContext newContext = TxMLPatientsWhoMissedNextApointmentCalculation
				.getNewEvaluationContext(parameters);

		CalculationResultMap inicioRealResult = Context
				.getRegisteredComponents(OnArtInitiatedArvDrugsMisaDefinitionCalculation.class).get(0)
				.evaluate(parameterValues, context);

		Set<Integer> cohort = inicioRealResult.keySet();

		CalculationResultMap lastFilaCalculationResult = Context.getRegisteredComponents(DSDLastFilaCalculation.class)
				.get(0).evaluate(cohort, parameterValues, newContext);

		DSDLastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation = Context
				.getRegisteredComponents(DSDLastRecepcaoLevantamentoCalculation.class).get(0);

		CalculationResultMap lastRecepcaoLevantamentoResult = lastRecepcaoLevantamentoCalculation.evaluate(cohort,
				parameterValues, newContext);

		DSDLastRecepcaoLevantamentoToBeIncludeCalculation DSDLastRecepcaoLevantamentoToBeIncludeCalculation = Context
				.getRegisteredComponents(DSDLastRecepcaoLevantamentoToBeIncludeCalculation.class).get(0);

		CalculationResultMap DSDLastRecepcaoLevantamentoToBeIncludeCalculationResult = DSDLastRecepcaoLevantamentoToBeIncludeCalculation
				.evaluate(cohort, parameterValues, newContext);

		CalculationResultMap nextFilaResult = Context
				.getRegisteredComponents(TxRttNextFilaUntilEndDateCalculation.class).get(0)
				.evaluate(lastFilaCalculationResult.keySet(), parameterValues, newContext);

		return this.evaluateUsingCalculationRules(cohort, endDate, resultMap, lastFilaCalculationResult, nextFilaResult,
				lastRecepcaoLevantamentoResult, lastRecepcaoLevantamentoCalculation,
				DSDLastRecepcaoLevantamentoToBeIncludeCalculation);
	}

	protected CalculationResultMap evaluateUsingCalculationRules(Set<Integer> cohort, Date endDate,
			CalculationResultMap resultMap, CalculationResultMap lastFilaCalculationResult,
			CalculationResultMap nextFilaResult, CalculationResultMap lastRecepcaoLevantamentoResult,
			DSDLastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation,
			DSDLastRecepcaoLevantamentoToBeIncludeCalculation dsdLastRecepcaoLevantamentoToBeIncludeCalculation) {

		for (Integer patientId : cohort) {
			Date maxNextDate = CalculationProcessorUtils.getMaxDate(patientId, nextFilaResult,
					this.getLastRecepcaoLevantamentoPlus30(patientId, lastRecepcaoLevantamentoResult,
							lastRecepcaoLevantamentoCalculation));

			if (maxNextDate != null) {
				Date nextDatePlus59 = CalculationProcessorUtils.adjustDaysInDate(maxNextDate, 59);

				if (nextDatePlus59.compareTo(endDate) < 0) {
					resultMap.put(patientId, new SimpleResult(maxNextDate, this));
				}
			} else {
				this.checkConsultationsOrFilaWithoutNextConsultationDate(patientId, resultMap,
						lastFilaCalculationResult, nextFilaResult, lastRecepcaoLevantamentoResult);
			}
		}
		return resultMap;
	}

	public CalculationResultMap getLastRecepcaoLevantamentoPlus30(Integer patientId,
			CalculationResultMap lastRecepcaoLevantamentoResult,
			DSDLastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation) {

		CalculationResultMap lastRecepcaoLevantamentoPlus30 = new CalculationResultMap();
		CalculationResult maxRecepcao = lastRecepcaoLevantamentoResult.get(patientId);
		if (maxRecepcao != null) {
			lastRecepcaoLevantamentoPlus30.put(patientId,
					new SimpleResult(CalculationProcessorUtils.adjustDaysInDate((Date) maxRecepcao.getValue(), 30),
							lastRecepcaoLevantamentoCalculation));
		}
		return lastRecepcaoLevantamentoPlus30;
	}

	protected void checkConsultationsOrFilaWithoutNextConsultationDate(Integer patientId,
			CalculationResultMap resultMap, CalculationResultMap lastFilaCalculationResult,
			CalculationResultMap nextFilaCalculationResult,
			CalculationResultMap lastRecepcaoLevantamentoCalculationResult) {

		CalculationResult lastFilaResult = lastFilaCalculationResult.get(patientId);
		CalculationResult nextFilaResult = nextFilaCalculationResult.get(patientId);
		CalculationResult lastRecepcaoResult = lastRecepcaoLevantamentoCalculationResult.get(patientId);

		if (lastFilaCalculationResult != null && lastFilaResult != null && nextFilaResult == null
				&& lastRecepcaoResult == null) {
			resultMap.put(patientId, new BooleanResult(true, this));

		} else if (lastFilaResult == null && lastRecepcaoResult == null) {
			resultMap.put(patientId, new BooleanResult(true, this));
		}
	}
}
