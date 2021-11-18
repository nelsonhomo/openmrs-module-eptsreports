package org.openmrs.module.eptsreports.reporting.calculation.defaulters;

import java.util.List;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.library.queries.SurveyDefaultQueries;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class SurveyDefaultersCalculation extends BaseFghCalculation {

  public ListMap<Integer, Object[]> getResutls(EvaluationContext context) {

    SqlQueryBuilder sqlQuery =
        new SqlQueryBuilder(
            SurveyDefaultQueries.getPatientsWhoHaveViralLoadNotSupresed(),
            context.getParameterValues());

    List<Object[]> queryResult =
        Context.getRegisteredComponents(EvaluationService.class)
            .get(0)
            .evaluateToList(sqlQuery, context);

    ListMap<Integer, Object[]> obsForPatients = new ListMap<Integer, Object[]>();

    for (Object[] row : queryResult) {

      Object[] obj = {row[1], row[2], row[3], row[4], row[5]};

      obsForPatients.putInList((Integer) row[0], obj);
    }
    return obsForPatients;
  }

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    ListMap<Integer, Object[]> resutls = this.getResutls(context);

    for (Integer patientId : resutls.keySet()) {
      List<Object[]> objects = resutls.get(patientId);
      if (!objects.isEmpty()) {
        Object[] obj1 = objects.get(0);
        if (obj1[2] == null && obj1[3] != null) {
          resultMap.put(patientId, new BooleanResult(true, this));
        }
      }
    }
    return resultMap;
  }
}
