package server.spark_retrofit.service;

import client.models.Shape;

import java.util.ArrayList;
import java.util.List;

public interface ShapeService {
	boolean addShape(Shape shape);
	ArrayList<Shape> getAll();
	List<String> getAllNames();
	Shape getById(int id);
	boolean deleteById(int id);
}
