Select  p.patient_id,e.encounter_datetime data_seguimento                                          
              from  patient p                                                                 
                  inner join encounter e on e.patient_id=p.patient_id                                             
      where       p.voided=0 and e.voided=0 and e.encounter_type in (6,9) 
                  and e.location_id=:location     
                  and e.encounter_datetime between date_sub(:endDate, INTERVAL 6 MONTH) and :endDate                                 
