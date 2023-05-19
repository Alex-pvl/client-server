package server.spark_retrofit;

import client.gui.ClientFrame;
import client.models.Image;
import client.models.Shape;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.spark_retrofit.service.ShapeService;
import server.spark_retrofit.service.ShapeServiceImpl;
import server.spark_retrofit.utils.ShapeAdapter;
import server.spark_retrofit.utils.SimpleResponse;
import spark.Spark;

import java.io.FileWriter;
import java.io.IOException;

public class SparkServer {

	public ClientFrame clientFrame;
	public ShapeService shapeService;
	Gson gson;

	public SparkServer(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
		shapeService = new ShapeServiceImpl(clientFrame);
		gson = new GsonBuilder().registerTypeAdapter(Shape.class, new ShapeAdapter()).create();
	}

	public void startListening() {
		Spark.port(8081);

		Spark.get("/api/shapes", (req, res) -> {
			res.type("application/json");
			var list = shapeService.getAll();
			return list.stream().map(s -> gson.toJson(s, Shape.class)).toList();
		});
		Spark.get("/api/shapes/names", (req, res) -> {
			res.type("application/json");
			var list = shapeService.getAll();
			return list.stream()
				.map(Shape::getName)
				.map(s -> gson.toJson(s, String.class))
				.toList();
		});
		Spark.post("/api/shapes", (req, res) -> {
			res.type("application/json");
			Shape s = gson.fromJson(req.body(), Shape.class);
//			shapeService.addShape(s);
			clientFrame.shapes.add(s);
			if (s instanceof Image image) {
				image.loadImage();
			}
			s.setComponent(clientFrame.drawingPanel);
			clientFrame.drawingPanel.repaint();
			return gson.toJson(s, Shape.class);
		});
		Spark.get("/api/shapes/:id", (req, res) -> {
			res.type("application/json");
			int id = Integer.parseInt(req.params(":id"));
			var s = shapeService.getById(id);
			return gson.toJson(s, Shape.class);
		});
		Spark.delete("/api/shapes/:id", (req, res) -> {
			res.type("application/json");
			int id = Integer.parseInt(req.params(":id"));
			//var isSuccess = shapeService.deleteById(id);
			clientFrame.shapes.remove(id);
			clientFrame.drawingPanel.repaint();
			return "Success";
		});
	}
}
