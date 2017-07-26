package net.riking.config.enums;

public enum ExportType {
	Big("大额交易"), Susp("可疑交易"), Both("大额和可疑交易");
	private final String name;
	private ExportType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
