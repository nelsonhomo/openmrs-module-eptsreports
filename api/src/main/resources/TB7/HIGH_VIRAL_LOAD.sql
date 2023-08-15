select primeiraCarga.patient_id from 
              (
              select cargaAnterior.patient_id,cargaAnterior.data_carga_anterior, cargaAnterior.encounter_id,o.value_numeric,if(o.value_numeric>1000,1,null) decisao
               from
               (
				select p.patient_id,max(date(e.encounter_datetime)) data_carga_anterior,e.encounter_id 
                  from patient p
                inner join encounter e on e.patient_id=p.patient_id
                where e.voided=0 and e.encounter_type in (13,51)
                and  e.location_id=:location and e.encounter_datetime<:startDate
                group by p.patient_id
                )cargaAnterior
                inner join obs o on o.encounter_id=cargaAnterior.encounter_id and date(o.obs_datetime)=date(cargaAnterior.data_carga_anterior)
                where o.concept_id=856 and o.voided=0 
                ) cargaAnterior
                inner join
				(
                Select p.patient_id,min(date(o.obs_datetime)) data_carga_1, o.value_numeric as vl1 
                from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs o on e.encounter_id=o.encounter_id 
                where p.voided=0 and e.voided=0 and o.voided=0 
                and e.encounter_type in (13,51) 
                and  o.concept_id in (856) 
                and  date(o.obs_datetime) between :startDate and:endDate 
                and e.location_id=:location 
                and o.value_numeric>1000
                group by p.patient_id 
                )primeiraCarga on primeiraCarga.patient_id=cargaAnterior.patient_id
                where  cargaAnterior.decisao=1
