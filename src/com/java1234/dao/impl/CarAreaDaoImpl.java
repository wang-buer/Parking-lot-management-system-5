package com.java1234.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.java1234.dao.CarAreaDao;
import com.java1234.model.CarArea;
import com.java1234.model.PageBean;
import com.java1234.util.StringUtil;

@Repository("carAreaDao")
public class CarAreaDaoImpl implements CarAreaDao{

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<CarArea> find(PageBean pageBean, CarArea carArea) {
		StringBuffer sb=new StringBuffer("select * from t_cararea");
		if(carArea!=null){
			if(StringUtil.isNotEmpty(carArea.getAreaName())){
				sb.append(" and areaName like '%"+carArea.getAreaName()+"%'");
			}
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		final List<CarArea> carAreaList=new ArrayList<CarArea>();
		jdbcTemplate.query(sb.toString().replaceFirst("and", "where"), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				CarArea carArea=new CarArea();
				carArea.setId(rs.getInt("id"));
				carArea.setAreaName(rs.getString("areaName"));
				carArea.setPrice(rs.getDouble("price"));
				carAreaList.add(carArea);
			}
		});
		return carAreaList;
	}

	@Override
	public int count(CarArea carArea) {
		StringBuffer sb=new StringBuffer("select count(*) from t_cararea");
		if(carArea!=null){
			if(StringUtil.isNotEmpty(carArea.getAreaName())){
				sb.append(" and areaName like '%"+carArea.getAreaName()+"%'");
			}
		}
		return jdbcTemplate.queryForObject(sb.toString().replaceFirst("and", "where"), Integer.class);
	}

	@Override
	public void add(CarArea carArea) {
		String sql="insert into t_cararea values(null,?,?)";
		jdbcTemplate.update(sql, new Object[]{carArea.getAreaName(),carArea.getPrice()});
	}

	@Override
	public void update(CarArea carArea) {
		String sql="update t_cararea set areaName=?,price=? where id=?";
		jdbcTemplate.update(sql, new Object[]{carArea.getAreaName(),carArea.getPrice(),carArea.getId()});
	}

	@Override
	public void delete(int id) {
		String sql="delete from t_cararea where id=?";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	@Override
	public CarArea loadById(int id) {
		String sql="select * from t_cararea where id=?";
		final CarArea carArea=new CarArea();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				carArea.setId(rs.getInt("id"));
				carArea.setAreaName(rs.getString("areaName"));
				carArea.setPrice(rs.getDouble("price"));
			}
		});
		return carArea;
	}

}
