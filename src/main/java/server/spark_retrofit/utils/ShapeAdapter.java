package server.spark_retrofit.utils;

import client.models.Image;
import client.models.Polygon;
import client.models.Shape;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapeAdapter implements JsonSerializer<Shape>, JsonDeserializer<Shape> {
	@Override
	public Shape deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		final ObjectMapper objectMapper = new ObjectMapper();
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String name = jsonObject.get("name").getAsString();
		double x = jsonObject.get("x").getAsDouble();
		double y = jsonObject.get("y").getAsDouble();
		int width = jsonObject.get("width").getAsInt();
		int height = jsonObject.get("height").getAsInt();
		int color = jsonObject.get("color").getAsInt();
		Shape shape = null;
		if (name.equals("Polygon")) {
			int n = jsonObject.get("n").getAsInt();
			int radius = jsonObject.get("radius").getAsInt();
			var xList = jsonObject.getAsJsonArray("xPoints");
			var yList = jsonObject.getAsJsonArray("yPoints");
			shape = new Polygon();
			shape.setX(x);
			shape.setY(y);
			shape.setWidth(width);
			shape.setHeight(height);
			shape.setColor(new Color(color));
			((Polygon) shape).setN(n);
			((Polygon) shape).setRadius(radius);
			try {
				var xPoints = objectMapper.readValue(xList.toString(), int[].class);
				((Polygon) shape).setxPoints(xPoints);
				var yPoints = objectMapper.readValue(yList.toString(), int[].class);
				((Polygon) shape).setyPoints(yPoints);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		} else if (name.equals("Image")) {
			String path = jsonObject.get("path").getAsString();
			shape = new Image();
			shape.setX(x);
			shape.setY(y);
			shape.setWidth(width);
			shape.setHeight(height);
			shape.setColor(new Color(color));
			((Image) shape).setPath(path);
		}
		return shape;
	}

	@Override
	public JsonElement serialize(Shape shape, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("x", shape.getX());
		jsonObject.addProperty("y", shape.getY());
		jsonObject.addProperty("width", shape.getWidth());
		jsonObject.addProperty("height", shape.getHeight());
		jsonObject.addProperty("color", shape.getColor().getRGB());

		if (shape instanceof Polygon p) {
			var xList = new JsonArray();
			for (int x : p.getxPoints()) {
				xList.add(x);
			}
			var yList = new JsonArray();
			for (int y : p.getyPoints()) {
				yList.add(y);
			}

			jsonObject.addProperty("name", "Polygon");
			jsonObject.addProperty("n", p.getN());
			jsonObject.addProperty("radius", p.getRadius());
			jsonObject.add("xPoints", xList);
			jsonObject.add("yPoints", yList);
		} else if (shape instanceof Image) {
			jsonObject.addProperty("name", "Image");
			jsonObject.addProperty("path", ((Image) shape).getPath());
		}

		return jsonObject;
	}
}
