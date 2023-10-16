package com.java1234.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.java1234.dao.CarTypeDao;
import com.java1234.model.CarType;
import com.java1234.model.PageBean;
import com.java1234.util.StringUtil;

@Repository("carTypeDao")
public class CarTypeDaoImpl implements CarTypeDao{

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<CarType> find(PageBean pageBean, CarType carType) {
		StringBuffer sb=new StringBuffer("select * from t_cartype");
		if(carType!=null){
			if(StringUtil.isNotEmpty(carType.getCarTypeName())){
				sb.append(" and carTypeName like '%"+carType.getCarTypeName()+"%'");
			}
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		final List<CarType> carTypeList=new ArrayList<CarType>();
		jdbcTemplate.query(sb.toString().replaceFirst("and", "where"), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				CarType carType=new CarType();
				carType.setId(rs.getInt("id"));
				carType.setCarTypeName(rs.getString("carTypeName"));
				carTypeList.add(carType);
			}
		});
		return carTypeList;
	}

	@Override
	public int count(CarType carType) {
		StringBuffer sb=new StringBuffer("select count(*) from t_cartype");
		if(carType!=null){
			if(StringUtil.isNotEmpty(carType.getCarTypeName())){
				sb.append(" and carTypeName like '%"+carType.getCarTypeName()+"%'");
			}
		}
		return jdbcTemplate.queryForObject(sb.toString().replaceFirst("and", "where"), Integer.class);
	}

	@Override
	public void add(CarType carType) {
		String sql="insert into t_cartype values(null,?)";
		jdbcTemplate.update(sql, new Object[]{carType.getCarTypeName()});
	}

	@Override
	public void update(CarType carType) {
		String sql="update t_cartype set carTypeName=? where id=?";
		jdbcTemplate.update(sql, new Object[]{carType.getCarTypeName(),carType.getId()});
	}

	@Override
	public void delete(int id) {
		String sql="delete from t_cartype where id=?";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	@Override
	public CarType loadById(int id) {
		String sql="select * from t_cartype where id=?";
		final CarType carType=new CarType();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				carType.setId(rs.getInt("id"));
				carType.setCarTypeName(rs.getString("carTypeName"));
			}
		});
		return carType;
	}

}
