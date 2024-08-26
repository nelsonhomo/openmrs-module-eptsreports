package org.openmrs.module.eptsreports.reporting.library.datasets.midatasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory13P1_1CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.mqdatasets.MQAbstractDataSet;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P1_1DataSet extends MQAbstractDataSet {

  @Autowired private MICategory13P1_1CohortQueries mICategory13P1_1CohortQueries;
  private final boolean EXCLUDE_TB_ACTIVE_DIAGNOSTIC = true;

  public void constructTMiDatset(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addColumn(
        "CAT13CV13_1PLUS13_4DENOMINATOR",
        "13.1: # de adultos (15/+anos)  em TARV (na 1ª(13.1) ou 2ª(13.4) linha) com consulta clínica no período de avaliação, elegíveis ao pedido de CV Denominador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries
                    .findAdultsPatientsInFirstOrSecondLineTARVDenominator13_1Plus13_4Denominator(
                        EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV13_1PLUS13_4DENOMINATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV13_1PLUS13_4NUMERATOR",
        "13.1: # de adultos (15/+anos)  em TARV (na 1ª(13.1) ou 2ª(13.4) linha) com consulta clínica no período de avaliação, elegíveis ao pedido de CV Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries
                    .findAdultsPatientsInFirstOrSecondLineTARVDenominator13_1Plus13_4Numerator(),
                "CAT13CV13_1PLUS13_4NUMERATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV13_8PLUS13_13DENOMINATOR",
        "13.8: # de crianças (10-14 anos de idade) na 1ª(13.8) ou 2ª(13.13) linha de TARV que tiveram consulta clínica no período de avaliação, eram elegíveis ao pedido de CV Denominador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries
                    .findChildrenPatientsOnFirstOrSecondLineARV13_8Plus13_13Denominator(),
                "CAT13CV13_8PLUS13_13DENOMINATOR",
                mappings),
            mappings),
        "age=10-14");

    dataSetDefinition.addColumn(
        "CAT13CV13_8PLUS13_13NUMERATOR",
        "13.8: # de crianças (10-14 anos de idade) na 1ª(13.8) ou 2ª(13.13) linha de TARV que tiveram consulta clínica no período de avaliação, eram elegíveis ao pedido de CV Numerador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries
                    .findChildrenPatientsOnFirstOrSecondLineARV13_8Plus13_13Numerator(),
                "CAT13CV13_8PLUS13_13NUMERATOR",
                mappings),
            mappings),
        "age=10-14");

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSDENOMINATOR",
        "13.1: Adultos (15/+anos) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Denominador ",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findDenominatorCategory13SectionIB(
                    EXCLUDE_TB_ACTIVE_DIAGNOSTIC),
                "CAT13CV15PLUSDENOMINATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV15PLUSNUMERATOR",
        "13.1: Adultos (15/+anos) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findFinalNumeratorCategory13SectionIC(),
                "CAT13CV15PLUSNUMERATOR",
                mappings),
            mappings),
        "age=15PlusOrBreastfeeding");

    dataSetDefinition.addColumn(
        "CAT13CV04DENOMINATOR",
        "13.6: Crianças (0-4 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findDenominatorCategory13SectionIBChildrens(),
                "CAT13CV04DENOMINATOR",
                mappings),
            mappings),
        "age=0-4");

    dataSetDefinition.addColumn(
        "CAT13CV59DENOMINATOR",
        "13.7: Crianças (5-9 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findDenominatorCategory13SectionIBChildrens(),
                "CAT13CV59DENOMINATOR",
                mappings),
            mappings),
        "age=5-9");

    dataSetDefinition.addColumn(
        "CAT13CV1014DENOMINATOR",
        "13.8: Crianças (10-14 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Denominador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                mICategory13P1_1CohortQueries.findDenominatorCategory13SectionIBChildrens(),
                "CAT13CV1014DENOMINATOR",
                mappings),
            mappings),
        "age=10-14");

    dataSetDefinition.addColumn(
        "CAT13CV04NUMERATOR",
        "13.6: Crianças (0-4 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findFinalNumeratorCategory13SectionICChildrens(),
                "CAT13CV04NUMERATOR",
                mappings),
            mappings),
        "age=0-4");

    dataSetDefinition.addColumn(
        "CAT13CV59NUMERATOR",
        "13.7: Crianças (5-9 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findFinalNumeratorCategory13SectionICChildrens(),
                "CAT13CV59NUMERATOR",
                mappings),
            mappings),
        "age=5-9");

    dataSetDefinition.addColumn(
        "CAT13CV1014NUMERATOR",
        "13.8: Crianças (10-14 anos de idade) na 1a linha de TARV que tiveram consulta clínica no período de revisão e que eram elegíveis ao pedido de CV Numerador",
        EptsReportUtils.map(
            this.setIndicatorWithAllParameters(
                this.mICategory13P1_1CohortQueries.findFinalNumeratorCategory13SectionICChildrens(),
                "CAT13CV1014NUMERATOR",
                mappings),
            mappings),
        "age=10-14");
  }
}
