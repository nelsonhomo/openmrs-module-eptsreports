     	  select distinct ccr.patient_id
          from (
     	   select patient_id, min(data_inicio) data_inicio from (
         select  pg.patient_id,min(date_enrolled) data_inicio                                
         from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
         where   pg.voided=0 and p.voided=0 and program_id=6 and date_enrolled between :startDate and :endDate and location_id= :location       
         group by pg.patient_id  
         
       	 union
       	 
      	 	 select  p.patient_id,min(e.encounter_datetime) data_inicio                                
          from  patient p                                                 
              inner join encounter e on p.patient_id=e.patient_id                                                   
          where   p.voided=0 and e.voided=0 and e.encounter_type = 92 and                                      
              e.encounter_datetime between :startDate and :endDate and e.location_id= :location                              
          group by p.patient_id 
          ) ccr group by ccr.patient_id
          ) ccr
           