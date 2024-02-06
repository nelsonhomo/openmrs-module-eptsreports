select patient_id from
(
Select  p.patient_id,o.value_datetime data_tratamento                                                           
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id                                                           
where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
      e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location 
     and  o.value_datetime  between :startDate and :endDate
union
select  pg.patient_id,date_enrolled data_tratamento                                                             
from    patient p 
        inner join patient_program pg on p.patient_id=pg.patient_id                                       
where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  
        and date_enrolled between :startDate and :endDate
union

Select  p.patient_id,o.obs_datetime data_tratamento                                                           
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id                                                           
where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
      e.encounter_type=53 and o.concept_id=1406 and o.value_coded=42  and e.location_id= :location 
     and  o.obs_datetime  between :startDate and :endDate
union
Select  p.patient_id,o.obs_datetime data_tratamento                                                           
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id                                                           
where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
      e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location 
     and  o.obs_datetime  between :startDate and :endDate
)TBTretment