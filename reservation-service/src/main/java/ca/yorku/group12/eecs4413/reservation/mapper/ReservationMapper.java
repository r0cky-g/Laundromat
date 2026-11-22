package ca.yorku.group12.eecs4413.reservation.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ca.yorku.group12.eecs4413.reservation.dto.MachineReservationDTO;
import ca.yorku.group12.eecs4413.reservation.entity.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	 MachineReservationDTO machineReserveToDto(Reservation reservation);

	 List<MachineReservationDTO> machineReservetoDtoList(List<Reservation> reservations);
	
}
