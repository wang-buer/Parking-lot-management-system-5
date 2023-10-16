package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.dao.CarAreaDao;
import com.java1234.model.CarArea;
import com.java1234.model.PageBean;
import com.java1234.service.CarAreaService;

@Service("carAreaService")
public class CarAreaServiceImpl implements CarAreaService{

	@Resource
	private CarAreaDao carAreaDao;
	
	@Override
	public List<CarArea> find(PageBean pageBean, CarArea s_carArea) {
		return carAreaDao.find(pageBean, s_carArea);
	}

	@Override
	public int count(CarArea s_carArea) {
		return carAreaDao.count(s_carArea);
	}

	@Override
	public void add(CarArea carArea) {
		carAreaDao.add(carArea);
	}

	@Override
	public void update(CarArea carArea) {
		carAreaDao.update(carArea);
	}

	@Override
	public void delete(int id) {
		carAreaDao.delete(id);
	}

	@Override
	public CarArea loadById(int id) {
		return carAreaDao.loadById(id);
	}

}
