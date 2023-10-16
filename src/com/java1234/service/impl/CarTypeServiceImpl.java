package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.dao.CarTypeDao;
import com.java1234.model.CarType;
import com.java1234.model.PageBean;
import com.java1234.service.CarTypeService;

@Service("carTypeService")
public class CarTypeServiceImpl implements CarTypeService{

	@Resource
	private CarTypeDao carTypeDao;
	
	@Override
	public List<CarType> find(PageBean pageBean, CarType s_carType) {
		return carTypeDao.find(pageBean, s_carType);
	}

	@Override
	public int count(CarType s_carType) {
		return carTypeDao.count(s_carType);
	}

	@Override
	public void add(CarType carType) {
		carTypeDao.add(carType);
	}

	@Override
	public void update(CarType carType) {
		carTypeDao.update(carType);
	}

	@Override
	public void delete(int id) {
		carTypeDao.delete(id);
	}

	@Override
	public CarType loadById(int id) {
		return carTypeDao.loadById(id);
	}

}
