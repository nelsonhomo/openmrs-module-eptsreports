select inicioDAH.patient_id from(
    select p.patient_id from patient p 
    inner join encounter e on p.patient_id=e.patient_id 
    where e.voided=0 and p.voided=0 and  e.encounter_type =90 
    and e.encounter_datetime between :startDate and :endDate and e.location_id=:location 
	)inicioDAH
 left join( 
	select p.patient_id from patient p 
	inner join encounter e on p.patient_id=e.patient_id 
	where e.voided=0 and p.voided=0 and  e.encounter_type =90 
	and e.encounter_datetime >= :startDate - interval 12 month 
	and  e.encounter_datetime < :startDate and e.location_id=:location 

	union


select saidas.patient_id
from
    (
        select saidas_por_transferencia.patient_id, max(data_estado) data_estado
        from
            (
                select distinct max_estado.patient_id, max_estado.data_estado 
                from 
                    (                                                                
                       select pg.patient_id, max(ps.start_date) data_estado                                                                                          
                       from patient p                                                                                                               
                            inner join person pe on pe.person_id = p.patient_id                                                                         
                            inner join patient_program pg on p.patient_id = pg.patient_id                                                               
                            inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
                       where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
                            and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           
                    ) max_estado                                                                                                                        
                        inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
                        inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                    where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location                 
              
                    union                                                                                                                               
                    
                    select  p.patient_id,max(o.obs_datetime) data_estado                                                                                             
                    from patient p                                                                                                                   
                        inner join person pe on pe.person_id = p.patient_id                                                                         
                        inner join encounter e on p.patient_id=e.patient_id                                                                         
                        inner join obs  o on e.encounter_id=o.encounter_id                                                                          
                    where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
                        and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                         
                        and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
                        group by p.patient_id                                                                                                                                                               
            ) saidas_por_transferencia 
        group by patient_id 
    ) saidas
  left join
        (
       select lastFila.patient_id, encounter_datetime maxFila from (
		select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p 
         inner join encounter e on e.patient_id=p.patient_id 
         where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate 
         and e.location_id=:location and e.encounter_type=18
         group by p.patient_id 
       ) lastFila
  inner join
    (  
          select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
          from
          (
            select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
                from patient p                                                                                                                                   
                    inner join encounter e on e.patient_id= p.patient_id 
                    inner join obs o on o.encounter_id = e.encounter_id                                                                                        
                where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
                    and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
                    group by p.patient_id 
    
            union
    
            select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
            from patient p                                                                                                                                   
            inner join person pe on pe.person_id = p.patient_id                                                                                         
               inner join encounter e on p.patient_id=e.patient_id                                                                                         
               inner join obs o on e.encounter_id=o.encounter_id                                                                                           
            where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
               and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
            group by p.patient_id
            ) ultimo_levantamento group by patient_id
        ) ultimo_levantamento on lastFila.patient_id = ultimo_levantamento.patient_id 
        where ultimo_levantamento.data_ultimo_levantamento <= :endDate
        ) maxFila on maxFila.patient_id =  saidas.patient_id
        where saidas.data_estado >= maxFila.maxFila or maxFila.patient_id is null

    ) transferidoPara on transferidoPara.patient_id = inicioDAH.patient_id 
    where transferidoPara.patient_id is null 
    group by  inicioDAH.patient_id