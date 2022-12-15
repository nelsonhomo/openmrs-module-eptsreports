package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientWithHighViralLoadDataSet extends BaseDataSet {

  private static final String FIND_PATIENTS_WITH_HIGH_VL_LIST =
      "PATIENTS_WITH_HIGH_VL/FIND_PATIENTS_WITH_HIGH_VL_LIST.sql";

  private static final String FIND_PATIENTS_WITH_HIGH_VL_ALERTS =
      "PATIENTS_WITH_HIGH_VL/FIND_PATIENTS_WITH_HIGH_VL_ALERTS.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Lista de Seguimento De Pacientes Com Carga Viral nao Suprimida");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_HIGH_VL_LIST));
    return dsd;
  }

  public DataSetDefinition getAlertMessagesForListsPatientsEligibleViralLoadDataset(
      List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName(
        "Mensagens de alerta - Lista de Seguimento De Pacientes Com Carga Viral nao Suprimida");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_HIGH_VL_ALERTS));
    return dsd;
  }
}
