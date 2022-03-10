package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory15CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory15QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory15CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;

  @Autowired private ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired private MICategory15CohortQueries mICategory15CohortQueries;

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_1")
  public CohortDefinition getNumeratorCategory15_Indicator_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));
    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_1(), mappings));

    definition.setCompositionString("G2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTandGaacWithRequestForVLCategory15H1")
  public CohortDefinition findPatientsWithDTandGaacWithRequestForVLCategory15H1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTandGaacWithRequestForVLCategory15H1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTandGaacWithRequestForVLCategory15H2")
  public CohortDefinition findPatientsWithDTandGaacWithRequestForVLCategory15H2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTandGaacWithRequestForVLCategory15H2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTandGaacWithCV1000Category15I")
  public CohortDefinition findPatientsWithDTandGaacWithCV1000Category15I() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTandGaacWithCV1000Category15I;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_2")
  public CohortDefinition getNumeratorCategory15_Indicator_2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_2");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H1",
        EptsReportUtils.map(
            this.findPatientsWithDTandGaacWithRequestForVLCategory15H1(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_2_and_3_And_4(), mappings));

    definition.setCompositionString("H1 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_3")
  public CohortDefinition getNumeratorCategory15_Indicator_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_3");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H2",
        EptsReportUtils.map(
            this.findPatientsWithDTandGaacWithRequestForVLCategory15H2(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_2_and_3_And_4(), mappings));

    definition.setCompositionString("H2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_4")
  public CohortDefinition getNumeratorCategory15_Indicator_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "I", EptsReportUtils.map(this.findPatientsWithDTandGaacWithCV1000Category15I(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_2_and_3_And_4(), mappings));

    definition.setCompositionString("I AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_5")
  public CohortDefinition getNumeratorCategory15_Indicator_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_5");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_5(), mappings));

    definition.setCompositionString("G2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_6")
  public CohortDefinition getNumeratorCategory15_Indicator_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_6");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_6(), mappings));

    definition.setCompositionString("G2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_7")
  public CohortDefinition getNumeratorCategory15_Indicator_7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_7");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H1",
        EptsReportUtils.map(
            this.findPatientsWithDTandGaacWithRequestForVLCategory15H1(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_7_And_9_And_11(), mappings));

    definition.setCompositionString("H1 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_8")
  public CohortDefinition getNumeratorCategory15_Indicator_8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_8");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H1",
        EptsReportUtils.map(
            this.findPatientsWithDTandGaacWithRequestForVLCategory15H1(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_8_And_10_And_12(), mappings));

    definition.setCompositionString("H1 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_9")
  public CohortDefinition getNumeratorCategory15_Indicator_9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_9");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H2",
        EptsReportUtils.map(
            this.findPatientsWithDTandGaacWithRequestForVLCategory15H2(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_7_And_9_And_11(), mappings));

    definition.setCompositionString("H2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_10")
  public CohortDefinition getNumeratorCategory15_Indicator_10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_10");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H2",
        EptsReportUtils.map(
            this.findPatientsWithDTandGaacWithRequestForVLCategory15H2(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_8_And_10_And_12(), mappings));

    definition.setCompositionString("H2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_11")
  public CohortDefinition getNumeratorCategory15_Indicator_11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_11");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "I", EptsReportUtils.map(this.findPatientsWithDTandGaacWithCV1000Category15I(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_7_And_9_And_11(), mappings));

    definition.setCompositionString("I AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_12")
  public CohortDefinition getNumeratorCategory15_Indicator_12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_12");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "I", EptsReportUtils.map(this.findPatientsWithDTandGaacWithCV1000Category15I(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_8_And_10_And_12(), mappings));

    definition.setCompositionString("I AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_1")
  public CohortDefinition getDenominatorCategory15_Indicator_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A(),
            mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("A NOT (C OR D OR F)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_2_and_3_And_4")
  public CohortDefinition getDenominatorCategory15_Indicator_2_and_3_And_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_2_and_3_And_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("(A AND G2) NOT (C OR D OR F)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_5")
  public CohortDefinition getDenominatorCategory15_Indicator_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_5");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2(),
            mappingEndRevisionDate));

    definition.addSearch(
        "A3",
        EptsReportUtils.map(
            findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3(),
            mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("(A2 OR A3) NOT (C OR D OR F)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_7_And_9_And_11")
  public CohortDefinition getDenominatorCategory15_Indicator_7_And_9_And_11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_7_And_9_And_11");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2(),
            mappingEndRevisionDate));

    definition.addSearch(
        "A3",
        EptsReportUtils.map(
            findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("((A2 OR A3) AND G2) NOT (C OR D OR F)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_6")
  public CohortDefinition getDenominatorCategory15_Indicator_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_6");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2(),
            mappingEndRevisionDate));

    definition.addSearch(
        "A3",
        EptsReportUtils.map(
            findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3(),
            mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("(A2 OR A3) NOT (C OR D OR F)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_8_And_10_And_12")
  public CohortDefinition getDenominatorCategory15_Indicator_8_And_10_And_12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_8_And_10_And_12");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A2",
        EptsReportUtils.map(
            findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2(),
            mappingEndRevisionDate));

    definition.addSearch(
        "A3",
        EptsReportUtils.map(
            findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.setCompositionString("((A2 OR A3) AND G2) NOT (C OR D OR F)");

    return definition;
  }

  @DocumentedDefinition(value = "PatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A")
  private CohortDefinition findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ - PatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A");
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1")
  private CohortDefinition
      findPatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1");
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWithLastGaacOrLastDispensaTrimestralRegisteredInFichaClinicaWithinRevisionPeriodB1;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantSpecificForCategory15")
  private CohortDefinition findPatientsWhoArePregnantSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoArePregnantSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnant9MonthsSpecificForCategory15")
  private CohortDefinition findPatientsWhoArePregnant9MonthsSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoArePregnant9MonthsSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeeding18MonthsSpecificForCategory15")
  private CohortDefinition findPatientsWhoAreBreastfeeding18MonthsSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWhoAreBreastfeeding18MonthsSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingSpecificForCategory15")
  private CohortDefinition findPatientsWhoAreBreastfeedingSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoAreBreastfeedingSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value = "PatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2")
  private CohortDefinition
      findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2");
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value = "PatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3")
  private CohortDefinition
      findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3");
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "PatientsWhoAreNewlyEnrolledOnARTByAgeUsingAgeRange")
  private CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAgeUsingAgeRange(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        String.format(
            "MQ - PatientsWhoAreNewlyEnrolledOnARTByAgeUsingAgeRange %s To %s ", startAge, endAge));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTByAgeBetweenAgeRange(
            startAge, endAge);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS")
  private CohortDefinition findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        String.format("MQ - findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS %s To %s "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS")
  private CohortDefinition findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        String.format("MQ - findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS %s To %s "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_13")
  public CohortDefinition
      findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_13() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWithClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithClinicalConsultationDuringRevisionPeriodAndARTStartDateBiggerThanThreeMonths(),
            mappings));

    definition.addSearch(
        "E",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithRegularClinicalConsultationOrRegularArtPickUpInTheLastThreeMonths(),
            mappings));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnant9MonthsSpecificForCategory15(), mappings));

    definition.addSearch(
        "D",
        EptsReportUtils.map(
            this.findPatientsWhoAreBreastfeeding18MonthsSpecificForCategory15(), mappings));

    definition.addSearch(
        "F",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithTheLastCD4LessThan200OrEqualInClinicalConsultation(),
            mappings));

    definition.addSearch(
        "G",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithTheLastCargaViralGreaterOrEqualThan1000InClinicalConsultation(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD(), mappings));

    definition.setCompositionString("(A AND B1 AND E) NOT (C OR D OR F OR G OR J)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_13")
  public CohortDefinition
      findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_13() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_13");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},,endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-13",
        EptsReportUtils.map(
            this
                .findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_13(),
            mappings));

    definition.addSearch(
        "K",
        EptsReportUtils.map(this.findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS(), mappings));

    definition.setCompositionString("(DENOMINATOR-15-13 AND K)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_14")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_14() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},,endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWithClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD(), mappings));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithTheLastCargaViralGreaterOrEqualThan1000RegisteredInTheLastClinicalConsultation(),
            mappings));

    definition.setCompositionString("(A AND J AND H)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS_Numerator_15_14")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS_Numerator_15_14() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},,endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-14",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_14(),
            mappings));

    definition.addSearch(
        "L",
        EptsReportUtils.map(this.findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS(), mappings));

    definition.setCompositionString("(DENOMINATOR-15-14 AND L)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_15")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_15() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},,endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWithClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWhoAreActiveOnArtAndInAtleastOneDSD(), mappings));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyOneMonths(),
            mappings));

    definition.addSearch(
        "P",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLastThreeMonths(),
            mappings));

    definition.setCompositionString("(A AND J AND B2) NOT P");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_15")
  public CohortDefinition
      findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_15() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_15");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endRevisionDate},,endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-15",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_15(),
            mappings));

    definition.addSearch(
        "I",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000(),
            mappings));

    definition.setCompositionString("(DENOMINATOR-15-15 AND I)");

    return definition;
  }
}
