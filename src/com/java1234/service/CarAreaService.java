package com.java1234.service;

import java.util.List;

import com.java1234.model.CarArea;
import com.java1234.model.PageBean;

public interface CarAreaService {

	public List<CarArea> find(PageBean pageBean,CarArea carArea);
	
	public int count(CarArea carArea);
	
	public void add(CarArea carArea);
	
	public void update(CarArea carArea);
	
	public void delete(int id);
	
	public CarArea loadById(int id);
}
