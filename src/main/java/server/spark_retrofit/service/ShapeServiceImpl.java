package server.spark_retrofit.service;

import client.gui.ClientFrame;
import client.models.Shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeServiceImpl implements ShapeService {

	public ArrayList<Shape> shapes;
	public ClientFrame clientFrame;

	public ShapeServiceImpl(ClientFrame clientFrame) {
		this.clientFrame = clientFrame;
		this.shapes = clientFrame.shapes;
	}

	@Override
	public boolean addShape(Shape shape) {
		int oldSize = shapes.size();
		shapes.add(shape);
		int newSize = shapes.size();
		return newSize > oldSize;
	}

	@Override
	public ArrayList<Shape> getAll() {
		return shapes;
	}

	@Override
	public List<String> getAllNames() {
		return shapes.stream()
			.map(Shape::toString)
			.toList();
	}

	@Override
	public Shape getById(int id) {
		return shapes.get(id);
	}

	@Override
	public boolean deleteById(int id) {
		int oldSize = shapes.size();
		shapes.remove(id);
		int newSize = shapes.size();
		return newSize < oldSize;
	}
}
