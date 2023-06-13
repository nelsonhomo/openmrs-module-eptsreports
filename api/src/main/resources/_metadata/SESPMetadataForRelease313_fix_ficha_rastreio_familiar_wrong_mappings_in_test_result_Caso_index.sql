update obs inner join encounter using(encounter_id)
set value_coded = 1
where obs.concept_id = 165337 and obs.value_coded =1169 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146; 
  	
update obs inner join encounter using(encounter_id)
set value_coded = 2
where obs.concept_id = 165337 and obs.value_coded = 1366 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146; 
  	 
update obs inner join encounter using(encounter_id)
set value_coded = 3
where obs.concept_id = 165337 and obs.value_coded = 1704 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146;   

update obs inner join encounter using(encounter_id)
set value_coded = 4
where obs.concept_id = 165337 and obs.value_coded = 6275 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146;   
	
update obs inner join encounter using(encounter_id)
set value_coded = 5
where obs.concept_id = 165337 and obs.value_coded = 6276 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146;   
 	
update obs set value_coded = 1704, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 1169 => 1704) ' where concept_id = 165337 and value_coded = 1;

update obs set value_coded = 1169, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 1366 => 1169) ' where concept_id = 165337 and value_coded = 2;

update obs set value_coded = 6275, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 1704 => 6275) ' where concept_id = 165337 and value_coded = 3;

update obs set value_coded = 6276, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 6275 => 6276) ' where concept_id = 165337 and value_coded = 4;

update obs set value_coded = 1366, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 6276 => 1366) ' where concept_id = 165337 and value_coded = 5;