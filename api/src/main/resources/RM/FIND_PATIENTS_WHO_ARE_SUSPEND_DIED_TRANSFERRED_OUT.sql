select
	obitoSuspenso.patient_id
from
	(
	select
		saida.patient_id,
		saida.data_estado
	from
		(
		select
			patient_id,
			max(data_estado) data_estado
		from
			(
			select
				maxEstado.patient_id,
				maxEstado.data_transferidopara data_estado
			from
				(
				select
					pg.patient_id,
					max(ps.start_date) data_transferidopara
				from
					patient p
				inner join patient_program pg on
					p.patient_id = pg.patient_id
				inner join patient_state ps on
					pg.patient_program_id = ps.patient_program_id
				where
					pg.voided = 0
					and ps.voided = 0
					and p.voided = 0
					and 
pg.program_id = 2
					and ps.start_date <= :endDate
					and pg.location_id =:location
				group by
					p.patient_id 
) maxEstado
			inner join patient_program pg2 on
				pg2.patient_id = maxEstado.patient_id
			inner join patient_state ps2 on
				pg2.patient_program_id = ps2.patient_program_id
			where
				pg2.voided = 0
				and ps2.voided = 0
				and pg2.program_id = 2
				and 
ps2.start_date = maxEstado.data_transferidopara
				and pg2.location_id =:location
				and ps2.state in (8,10)
		union
			select
				p.patient_id,
				max(o.obs_datetime) data_estado
			from
				patient p
			inner join encounter e on
				p.patient_id = e.patient_id
			inner join obs o on
				e.encounter_id = o.encounter_id
			where
				e.voided = 0
				and o.voided = 0
				and p.voided = 0
				and e.encounter_type in (53, 6)
				and o.concept_id in (6272, 6273)
				and o.value_coded in (1366, 1709)
				and o.obs_datetime <= :endDate
				and e.location_id =:location
			group by
				p.patient_id
		union
			select
				person_id as patient_id,
				death_date as data_estado
			from
				person
			where
				dead = 1
				and death_date is not null
				and death_date <= :endDate 
) allSaida
		group by
			patient_id 
) saida
	inner join
(
		select
			patient_id,
			max(encounter_datetime) encounter_datetime
		from
			(
			select
				p.patient_id,
				max(e.encounter_datetime) encounter_datetime
			from
				patient p
			inner join encounter e on
				e.patient_id = p.patient_id
			where
				p.voided = 0
				and e.voided = 0
				and e.encounter_datetime <= :endDate
				and e.location_id =:location
				and e.encounter_type in (18)
			group by
				p.patient_id
		union
			Select
				p.patient_id,
				max(value_datetime) encounter_datetime
			from
				patient p
			inner join encounter e on
				p.patient_id = e.patient_id
			inner join obs o on
				e.encounter_id = o.encounter_id
			where
				p.voided = 0
				and e.voided = 0
				and o.voided = 0
				and e.encounter_type = 52
				and o.concept_id = 23866
				and o.value_datetime is not null
				and o.value_datetime <= :endDate
				and e.location_id =:location
			group by
				p.patient_id 
) consultaLev
		group by
			patient_id 
) consultaOuARV on
		saida.patient_id = consultaOuARV.patient_id
	where
		consultaOuARV.encounter_datetime <= saida.data_estado
		and saida.data_estado between :startDate AND :endDate 
)obitoSuspenso
union
select
	transferidopara.patient_id
from
	(
	select
		patient_id,
		max(data_transferidopara) data_transferidopara
	from
		(
		select
			maxEstado.patient_id,
			maxEstado.data_transferidopara
		from
			(
			select
				pg.patient_id,
				max(ps.start_date) data_transferidopara
			from
				patient p
			inner join patient_program pg on
				p.patient_id = pg.patient_id
			inner join patient_state ps on
				pg.patient_program_id = ps.patient_program_id
			where
				pg.voided = 0
				and ps.voided = 0
				and p.voided = 0
				and pg.program_id = 2
				and ps.start_date <= :endDate
				and pg.location_id =:location
			group by
				p.patient_id
      )maxEstado
		inner join patient_program pg2 on
			pg2.patient_id = maxEstado.patient_id
		inner join patient_state ps2 on
			pg2.patient_program_id = ps2.patient_program_id
		where
			pg2.voided = 0
			and ps2.voided = 0
			and pg2.program_id = 2
			and ps2.start_date = maxEstado.data_transferidopara
			and pg2.location_id =:location
			and ps2.state = 7
	union
		select
			p.patient_id,
			max(o.obs_datetime) data_transferidopara
		from
			patient p
		inner join encounter e on
			p.patient_id = e.patient_id
		inner join obs o on
			o.encounter_id = e.encounter_id
		where
			e.voided = 0
			and p.voided = 0
			and o.obs_datetime <= :endDate
			and o.voided = 0
			and o.concept_id = 6272
			and o.value_coded = 1706
			and e.encounter_type = 53
			and e.location_id =:location
		group by
			p.patient_id
	union
		select
			p.patient_id,
			max(e.encounter_datetime) data_transferidopara
		from
			patient p
		inner join encounter e on
			p.patient_id = e.patient_id
		inner join obs o on
			o.encounter_id = e.encounter_id
		where
			e.voided = 0
			and p.voided = 0
			and e.encounter_datetime <= :endDate
			and o.voided = 0
			and o.concept_id = 6273
			and o.value_coded = 1706
			and e.encounter_type = 6
			and e.location_id =:location
		group by
			p.patient_id
   )
   transferido
	group by
		patient_id
)transferidopara
inner join
(
	select
		*
	from
		(
		select
			patient_id,
			max(data_ultimo_levantamento) data_ultimo_levantamento
		from
			(
			select
				ultimo_fila.patient_id,
				date_add(obs_fila.value_datetime , interval 1 day ) data_ultimo_levantamento
			from
				(
				select
					p.patient_id,
					max(encounter_datetime) data_fila
				from
					patient p
				inner join person pe on
					pe.person_id = p.patient_id
				inner join encounter e on
					e.patient_id = p.patient_id
				where
					p.voided = 0
					and pe.voided = 0
					and e.voided = 0
					and e.encounter_type = 18
					and e.location_id =:location
					and e.encounter_datetime <= :endDate
				group by
					p.patient_id
         )ultimo_fila
			left join encounter ultimo_fila_data_criacao on
				ultimo_fila_data_criacao.patient_id = ultimo_fila.patient_id
				and ultimo_fila_data_criacao.voided = 0
				and ultimo_fila_data_criacao.encounter_type = 18
				and date(ultimo_fila_data_criacao.encounter_datetime) = date(ultimo_fila.data_fila)
					and ultimo_fila_data_criacao.location_id =:location
				left join 
	 obs obs_fila on
					obs_fila.person_id = ultimo_fila.patient_id
					and obs_fila.voided = 0
					and (date(obs_fila.obs_datetime)= date(ultimo_fila.data_fila)
						or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created)
							and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id ))
					and obs_fila.concept_id = 5096
					and obs_fila.location_id =:location
			union
				select
					p.patient_id,
					date_add( max(value_datetime), interval 31 day ) data_ultimo_levantamento
				from
					patient p
				inner join person pe on
					pe.person_id = p.patient_id
				inner join encounter e on
					p.patient_id = e.patient_id
				inner join obs o on
					e.encounter_id = o.encounter_id
				where
					p.voided = 0
					and pe.voided = 0
					and e.voided = 0
					and o.voided = 0
					and e.encounter_type = 52
					and o.concept_id = 23866
					and o.value_datetime is not null
					and e.location_id =:location
					and o.value_datetime <= :endDate
				group by
					p.patient_id
      )ultimo_levantamento
		group by
			patient_id
   )final
	where
		final.data_ultimo_levantamento <= :endDate
)TR_OUT on
	TR_OUT.patient_id = transferidopara.patient_id
inner join
(
	select
		patient_id,
		max(encounter_datetime) encounter_datetime
	from
		(
		select
			p.patient_id,
			max(e.encounter_datetime) encounter_datetime
		from
			patient p
		inner join encounter e on
			e.patient_id = p.patient_id
		where
			p.voided = 0
			and e.voided = 0
			and e.encounter_datetime <= :endDate
			and e.location_id =:location
			and e.encounter_type = 18
		group by
			p.patient_id
   )consultaLev
	group by
		patient_id
)consultaOuARV on
	transferidopara.patient_id = consultaOuARV.patient_id
where
	consultaOuARV.encounter_datetime <= transferidopara.data_transferidopara
	and TR_OUT.data_ultimo_levantamento between :startDate AND :endDate