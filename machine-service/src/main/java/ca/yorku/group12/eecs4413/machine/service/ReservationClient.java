package ca.yorku.group12.eecs4413.machine.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationClient {

	public boolean checkReservation(
			int machineId,
			LocalDateTime startTime,
			LocalDateTime endTime) {
		// TODO: Call reservation-service REST API
		return true;
	}

	public void notifyReservationUpdate(int machineId, String status) {
		// TODO: Notify reservation-service
	}
}
