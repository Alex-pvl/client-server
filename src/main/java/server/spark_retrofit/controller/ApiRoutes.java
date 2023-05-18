package server.spark_retrofit.controller;

import client.models.Shape;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.List;

public interface ApiRoutes {
	@GET("/api/shapes")
	Call<ArrayList<Shape>> getShapes();

	@POST("/api/shapes")
	Call<ResponseBody> addShape(@Body Shape shape);

	@GET("/api/shapes/{id}")
	Call<Shape> getShape(@Path("id") int id);

	@DELETE("/api/shapes/{id}")
	Call<ResponseBody> deleteShape(@Path("id") int id);

	@GET("/api/shapes/names")
	Call<List<String>> getShapeNames();
}
