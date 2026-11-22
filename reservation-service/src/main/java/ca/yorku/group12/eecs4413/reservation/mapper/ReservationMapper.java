package ca.yorku.group12.eecs4413.reservation.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ca.yorku.group12.eecs4413.reservation.dto.MachineReservationsDTO;
import ca.yorku.group12.eecs4413.reservation.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	 MachineReservationsDTO machineReserveToDto(Reservation reservation);

	 List<MachineReservationsDTO> machineReservetoDtoList(List<Reservation> reservations);
	
}
