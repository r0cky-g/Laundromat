package ca.yorku.group12.eecs4413.reservation.service;

import org.springframework.stereotype.Service;
import ca.yorku.group12.eecs4413.reservation.repository.ReservationRepository;

@Service
public class ReservationService {
	
	private ReservationRepository reserveRepo;

	public ReservationService(ReservationRepository reserveRepo) {
		this.reserveRepo = reserveRepo;
	}
	
	
	
}
