package org.openmrs.module.eptsreports.reporting.library.datasets.midatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory13P1_2CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets.MQAbstractDataSet;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P1_2Dataset extends MQAbstractDataSet {

  @Autowired private MICategory13P1_2CohortQueries mICategory13P1_2CohortQueries;
  private final boolean EXCLUDE_TB_ACTIVE_DIAGNOSTIC = true;

  public void constructTMiDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13TB_HIV_ADULTS_13_4DENOMINATOR",
        "13.4: % de adultos (15/+anos) coinfectados TB/HIV com consulta clínica no período de avaliação, elegíveis ao pedido de CV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsDenominator13_4(),
                "CAT13TB_HIV_ADULTS_13_4DENOMINATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13TB_HIV_ADULTS_13_4NUMERATOR",
        "13.4: % de adultos (15/+anos) coinfectados TB/HIV com consulta clínica no período de avaliação, elegíveis ao pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsNumerator13_4(),
                "CAT13TB_HIV_ADULTS_13_4NUMERATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV24DENOMINATOR_SECTION1_2",
        "13.13: Crianças na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries.findDenominatorCategory13SectionIIB(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV24DENOMINATOR_SECTION1_2",
                mappings),
            mappings),
        "age=2-14");

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSNUMERATOR_SECTION1_2",
        "13.4: Adultos (15/+anos) na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries.findFinalNumeratorCategory13SectionIIC(),
                "CAT13CV15PLUSNUMERATOR_SECTION1_2",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV24NUMERATOR_SECTION1_2",
        "13.13: Crianças na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries.findFinalNumeratorCategory13SectionIIC(),
                "CAT13CV24NUMERATOR_SECTION1_2",
                mappings),
            mappings),
        "age=2-14");

    dataSetDefinition.addColumn(
        "CAT13TB_HIV_ADULTS_13_13DENOMINATOR",
        "13.13: % de crianças (0-14 anos) coinfectados TB/HIV com consulta clínica no período de avaliação, elegíveis ao pedido de CV e com registo de pedido de CV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsDenominator13_4(),
                "CAT13TB_HIV_ADULTS_13_13DENOMINATOR",
                mappings),
            mappings),
        "age=0-14");

    dataSetDefinition.addColumn(
        "CAT13TB_HIV_ADULTS_13_13NUMERATOR",
        "13.13: % de crianças (0-14 anos) coinfectados TB/HIV com consulta clínica no período de avaliação, elegíveis ao pedido de CV e com registo de pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_2CohortQueries
                    .findCoinfectedWithTBAndHIVAdultsPatientsNumerator13_4(),
                "CAT13TB_HIV_ADULTS_13_13NUMERATOR",
                mappings),
            mappings),
        "age=0-14");
  }
}
