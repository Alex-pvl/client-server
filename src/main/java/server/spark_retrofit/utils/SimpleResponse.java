package server.spark_retrofit.utils;

public enum SimpleResponse {
	SUCCESS("Success"),
	ERROR("Error");

	private final String status;

	SimpleResponse(String status) {
		this.status = status;
	}
}
