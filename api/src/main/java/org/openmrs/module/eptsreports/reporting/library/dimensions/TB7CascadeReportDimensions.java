package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB7AdvancedDiseaseAndTBCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB7CascadeReportDimensions {

  @Autowired private TB7AdvancedDiseaseAndTBCohortQueries tb7CohortQueries;

  public CohortDefinitionDimension getCascadeOneIndicatorOneDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("cascadeOneIndicatorOneDimension");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    dim.addCohortDefinition(
        "artInitiation",
        EptsReportUtils.map(
            this.tb7CohortQueries.findPatientsWhoAreNewlyEnrolledOnArt(), mappings));

    dim.addCohortDefinition(
        "pregnant", Mapped.mapStraightThrough(this.tb7CohortQueries.getPatientsWhoArePregnant()));

    dim.addCohortDefinition(
        "consecutiveHighVL",
        EptsReportUtils.map(
            this.tb7CohortQueries.findPatientsWithConsecutiveViralLoadResults(), mappings));

    dim.addCohortDefinition(
        "artRestart",
        EptsReportUtils.map(
            this.tb7CohortQueries.findPatientsWhoReinitiatedARTTreatment(), mappings));

    return dim;
  }

  public CohortDefinitionDimension getCascadeOneIndicatorTwoDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("cascadeOneIndicatorTwoDimension");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    dim.addCohortDefinition(
        "artInitiation",
        EptsReportUtils.map(
            this.tb7CohortQueries.findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33Days(),
            mappings));

    dim.addCohortDefinition(
        "pregnant",
        Mapped.mapStraightThrough(this.tb7CohortQueries.getPatientsWhoArePregnantsWithCountCD4()));

    dim.addCohortDefinition(
        "consecutiveHighVL",
        EptsReportUtils.map(
            this.tb7CohortQueries.findPatientsWithConsecutiveViralLoadResultsWithCD4Count(),
            mappings));

    dim.addCohortDefinition(
        "artRestart",
        EptsReportUtils.map(
            this.tb7CohortQueries.findPatientsWhoReinitiatedARTTreatmentAndHaveCD4Count(),
            mappings));

    return dim;
  }

  public CohortDefinitionDimension getCascadeOneIndicatorThreeDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("cascadeOneIndicatorThreeDimension");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    dim.addCohortDefinition(
        "artInitiation",
        EptsReportUtils.map(
            this.tb7CohortQueries
                .findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressions(),
            mappings));

    dim.addCohortDefinition(
        "pregnant",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries
                .getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppression()));

    dim.addCohortDefinition(
        "consecutiveHighVL",
        EptsReportUtils.map(
            this.tb7CohortQueries
                .findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressions(),
            mappings));

    dim.addCohortDefinition(
        "artRestart",
        EptsReportUtils.map(
            this.tb7CohortQueries
                .findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppression(),
            mappings));

    return dim;
  }

  public CohortDefinitionDimension getCascadeOneIndicatorFourDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("cascadeOneIndicatorFourDimension");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    dim.addCohortDefinition(
        "artInitiation",
        EptsReportUtils.map(
            this.tb7CohortQueries
                .findPatientsWhoAreNewlyEnrolledOnArtWithEligibleCD4Within33DaysWithSevereImmunoSuppressionsWithTBLAMResults(),
            mappings));

    dim.addCohortDefinition(
        "pregnant",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries
                .getPatientsWhoArePregnantsWithCountCD4WithSevereImmunoSuppressionWithTBLAMResults()));

    dim.addCohortDefinition(
        "consecutiveHighVL",
        EptsReportUtils.map(
            this.tb7CohortQueries
                .findPatientsWithConsecutiveViralLoadResultsWithCD4CountWithSevereImmunoSuppressionsWithTBLAMResults(),
            mappings));

    dim.addCohortDefinition(
        "artRestart",
        EptsReportUtils.map(
            this.tb7CohortQueries
                .findPatientsWhoReinitiatedARTTreatmentAndHaveCD4CountAndSevereImmunoSuppressionWithTBLAMResults(),
            mappings));

    return dim;
  }

  public CohortDefinitionDimension getTBLAMGradeLevelDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("tblam-grade-level");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${endDate-2m+1d},endDate=${endDate-1m},location=${location}";

    dim.addCohortDefinition(
        "four",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "three",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "two",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "one",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries.getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "no-level",
        Mapped.mapStraightThrough(
            this.tb7CohortQueries.getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults()));

    return dim;
  }
}
