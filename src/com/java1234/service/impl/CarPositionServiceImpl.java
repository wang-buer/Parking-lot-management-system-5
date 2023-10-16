package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java1234.dao.CarPositionDao;
import com.java1234.model.CarPosition;
import com.java1234.model.PageBean;
import com.java1234.service.CarPositionService;

@Service("carPositionService")
public class CarPositionServiceImpl implements CarPositionService{

	@Resource
	private CarPositionDao carPositionDao;
	
	@Override
	public List<CarPosition> find(PageBean pageBean, CarPosition carPosition) {
		return carPositionDao.find(pageBean, carPosition);
	}

	@Override
	public int count(CarPosition carPosition) {
		return carPositionDao.count(carPosition);
	}

	@Override
	public void add(CarPosition carPosition) {
		carPositionDao.add(carPosition);
	}

	@Override
	public void update(CarPosition carPosition) {
		carPositionDao.update(carPosition);
	}

	@Override
	public void delete(int id) {
		carPositionDao.delete(id);
	}

	@Override
	public CarPosition loadById(int id) {
		return carPositionDao.loadById(id);
	}

	@Override
	public boolean existCarPositionByAreaId(int areaId) {
		return carPositionDao.existCarPositionByAreaId(areaId);
	}
	
	@Override
	public void updateState(CarPosition carPosition) {
		carPositionDao.updateState(carPosition);
	}

	@Override
	public boolean existCarPositionIsactive(String carPositionId) {
		return carPositionDao.existCarPositionIsactive(carPositionId);
	}

	@Override
	public CarPosition loadByPositionNo(String positionNo) {
		// TODO Auto-generated method stub
		return carPositionDao.loadByPositionNo(positionNo);
	}

}
