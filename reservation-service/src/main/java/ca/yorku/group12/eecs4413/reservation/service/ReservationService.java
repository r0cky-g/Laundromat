package ca.yorku.group12.eecs4413.reservation.service;

import org.springframework.stereotype.Service;
import ca.yorku.group12.eecs4413.reservation.mapper.ReservationMapper;
import ca.yorku.group12.eecs4413.reservation.repository.ReservationRepository;

@Service
public class ReservationService {
	
	private final ReservationRepository reserveRepo;
	private final ReservationMapper reserveMapper;

	public ReservationService(ReservationRepository reserveRepo, ReservationMapper reserveMapper) {
		this.reserveRepo = reserveRepo;
		this.reserveMapper = reserveMapper;
	}
	
	
	
}
