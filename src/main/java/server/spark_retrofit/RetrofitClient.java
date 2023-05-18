package server.spark_retrofit;

import client.models.Shape;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import server.spark_retrofit.controller.ApiRoutes;
import server.spark_retrofit.utils.ShapeAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RetrofitClient {
	private ApiRoutes apiRoutes;
	public ArrayList<Shape> list;
	Gson gson;

	public RetrofitClient() {
		list = new ArrayList<>();
		gson = new GsonBuilder().registerTypeAdapter(Shape.class, new ShapeAdapter()).create();

		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("http://localhost:8081")
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build();

		apiRoutes = retrofit.create(ApiRoutes.class);
	}

	public void start() {

	}

	public ArrayList<Shape> getAll() {
		var call = apiRoutes.getShapes();
		call.enqueue(new Callback<ArrayList<Shape>>() {
			@Override
			public void onResponse(Call<ArrayList<Shape>> call, Response<ArrayList<Shape>> response) {
				System.out.println("Call request" + call.request());
				System.out.println("Call request header" + call.request().headers());
				System.out.println("Response raw header"+ response.headers());
				System.out.println("Response raw"+ String.valueOf(response.raw().body()));
				System.out.println("Response code"+ String.valueOf(response.code()));

				if(response.isSuccessful()) {
					//the response-body is already parseable to your ResponseBody object
					ArrayList<Shape> responseBody =  response.body();
					list = responseBody;
					//you can do whatever with the response body now...
					System.out.println("Response body"+ responseBody);
				}
				else  {
					System.out.println("Response errorBody"+ String.valueOf(response.errorBody()));
				}
			}

			@Override
			public void onFailure(Call<ArrayList<Shape>> call, Throwable throwable) {

			}
		});
		return list;
	}

	public Shape getShapeById(int id) {
		try {
			var shape = apiRoutes.getShape(id).execute().body();
			System.out.println(shape);
			return shape;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getShapeNames() {
		try {
			return apiRoutes.getShapeNames().execute().body();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void postShape(Shape shape) {
		try {
			apiRoutes.addShape(shape).execute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteShape(int id) {
		try {
			apiRoutes.deleteShape(id).execute();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
