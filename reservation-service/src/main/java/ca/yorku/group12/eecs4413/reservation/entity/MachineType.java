package ca.yorku.group12.eecs4413.reservation.entity;

public enum MachineType {
	WASHER("W"),
	DRYER("D");

	private final String code;

	MachineType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static MachineType fromCode(String code) {
		for (MachineType type : values()) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		
		throw new IllegalArgumentException("Unknown machine type code: " + code);
	}
}
