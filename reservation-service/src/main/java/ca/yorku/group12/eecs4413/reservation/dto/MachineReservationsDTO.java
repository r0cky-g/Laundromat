package ca.yorku.group12.eecs4413.reservation.dto;

import java.time.OffsetDateTime;

public class MachineReservationsDTO {
	
	private OffsetDateTime startTime;
	private OffsetDateTime endime;
	
	public OffsetDateTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
	}
	
	public OffsetDateTime getEndime() {
		return endime;
	}
	
	public void setEndime(OffsetDateTime endime) {
		this.endime = endime;
	}
}
