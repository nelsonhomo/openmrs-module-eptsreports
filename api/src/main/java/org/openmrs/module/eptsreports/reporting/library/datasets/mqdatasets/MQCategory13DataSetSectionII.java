package org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory13Section2CohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory13DataSetSectionII extends MQAbstractDataSet {

  @Autowired private MQCategory13Section2CohortQueries mqCategory13Section2CohortQueries;
  private final boolean EXCLUDE_TB_ACTIVE_DIAGNOSTIC = true;

  public void constructTMqDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSDENOMINATOR_SECTION1_2",
        "13.4: Adultos (15/+anos) na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries.findDenominatorCategory13SectionIIB(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV15PLUSDENOMINATOR_SECTION1_2",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSNUMERATOR_SECTION1_2",
        "13.4: Adultos (15/+anos) na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries.findFinalNumeratorCategory13SectionIIC(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV15PLUSNUMERATOR_SECTION1_2",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV24DENOMINATOR_SECTION1_2",
        "13.13: Crianças na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries
                    .findDenominatorCategory13SectionIIBChildrens(),
                "CAT13CV24DENOMINATOR_SECTION1_2",
                mappings),
            mappings),
        "age=2-14");

    dataSetDefinition.addColumn(
        "CAT13CV24NUMERATOR_SECTION1_2",
        "13.13: Crianças na 2a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV - Numerator",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries
                    .findFinalNumeratorCategory13SectionIICChildrens(),
                "CAT13CV24NUMERATOR_SECTION1_2",
                mappings),
            mappings),
        "age=2-14");

    dataSetDefinition.addColumn(
        "CAT13_4TBHIVDENOMINATOR",
        "13.4: % de adultos (15/+anos) coinfectados TB/HIV com consulta clínica no período de revisão, elegíveis ao pedido de CV e com registo de pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries.findDenominatorTBAndHIVIndicator13_4(),
                "CAT13_4TBHIVDENOMINATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13_4TBHIVNUMERATOR",
        "13.4: % de adultos (15/+anos) coinfectados TB/HIV com consulta clínica no período de revisão, elegíveis ao pedido de CV e com registo de pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries.findNumeratorTBAndHIVIndicator13_4(),
                "CAT13_4TBHIVNUMERATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13_13TBHIV_DENOMINATOR",
        "13.13: % de crianças (0-14 anos) coinfectados TB/HIV com consulta clínica no período de revisão, elegíveis ao pedido de CV e com registo de pedido de CV - Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries.findDenominatorTBAndHIVIndicator13_13(),
                "CAT13_13TBHIV_DENOMINATOR",
                mappings),
            mappings),
        "age=0-14");

    dataSetDefinition.addColumn(
        "CAT13_13TBHIV_NUMERATOR",
        "13.13: % de crianças (0-14 anos) coinfectados TB/HIV com consulta clínica no período de revisão, elegíveis ao pedido de CV e com registo de pedido de CV - Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mqCategory13Section2CohortQueries.findNumeratorTBAndHIVIndicator13_13(),
                "CAT13_13TBHIV_NUMERATOR",
                mappings),
            mappings),
        "age=0-14");
  }
}
