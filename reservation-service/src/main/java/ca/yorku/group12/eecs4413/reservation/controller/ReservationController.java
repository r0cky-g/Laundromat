package ca.yorku.group12.eecs4413.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.yorku.group12.eecs4413.reservation.dto.MachineReservationsDTO;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	@GetMapping("/machine/{machineId}")
	public ResponseEntity<MachineReservationsDTO> getReservationsByMachine(@PathVariable int machineId) {
		return ResponseEntity.notFound().build();
	}
	
}
