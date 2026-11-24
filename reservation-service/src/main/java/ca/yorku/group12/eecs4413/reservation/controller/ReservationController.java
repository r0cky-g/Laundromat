package ca.yorku.group12.eecs4413.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.yorku.group12.eecs4413.reservation.dto.MachineReservationDTO;
import ca.yorku.group12.eecs4413.reservation.service.ReservationService;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	private final ReservationService reserveService;
	
	public ReservationController(ReservationService reserveService) {
		this.reserveService = reserveService;
	}

	@GetMapping("/machine/{machineId}/schedule")
	public ResponseEntity<List<MachineReservationDTO>> getMachineSchedule(@PathVariable @Min(1) int machineId) {
		return ResponseEntity.ok(reserveService.getMachineSchedule(machineId));
	}
	
}
