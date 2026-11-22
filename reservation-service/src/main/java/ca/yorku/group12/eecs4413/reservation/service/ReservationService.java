package ca.yorku.group12.eecs4413.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ca.yorku.group12.eecs4413.reservation.dto.MachineReservationDTO;
import ca.yorku.group12.eecs4413.reservation.entity.Reservation;
import ca.yorku.group12.eecs4413.reservation.entity.ReservationStatus;
import ca.yorku.group12.eecs4413.reservation.mapper.ReservationMapper;
import ca.yorku.group12.eecs4413.reservation.repository.ReservationRepository;

@Service
public class ReservationService {
	
	private final ReservationRepository reserveRepo;
	private final ReservationMapper reserveMap;

	public ReservationService(ReservationRepository reserveRepo, ReservationMapper reserveMap) {
		this.reserveRepo = reserveRepo;
		this.reserveMap = reserveMap;
	}
	
	public List<MachineReservationDTO> getConfirmedReservationsByMachine(int machineId) {
		List<Reservation> reservations = reserveRepo.findByMachineIdAndStatusOrderByStartTimeDesc(machineId, ReservationStatus.CONFIRMED);

		return reserveMap.machineReservetoDtoList(reservations);
	}
	
}
