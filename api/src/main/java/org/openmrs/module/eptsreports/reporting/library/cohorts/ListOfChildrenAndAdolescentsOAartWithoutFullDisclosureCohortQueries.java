package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries {

  private static final String DISCLOSURE_LIST_MARKED_N =
      "LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE/LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE_N.sql";
  private static final String DISCLOSURE_LIST_MARKED_T =
      "LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE/LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE_T.sql";
  private static final String DISCLOSURE_LIST_MARKED_P =
      "LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE/LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE_P.sql";

  private static final String DISCLOSURE_LIST_MARKED_B =
      "LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE/LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE_B.sql";

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_N() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(DISCLOSURE_LIST_MARKED_N);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_T() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(DISCLOSURE_LIST_MARKED_T);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_P() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(DISCLOSURE_LIST_MARKED_P);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_B() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(DISCLOSURE_LIST_MARKED_B);

    definition.setQuery(query);

    return definition;
  }
}
