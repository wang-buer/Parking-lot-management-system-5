package com.java1234.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.java1234.dao.CarPositionDao;
import com.java1234.model.CarPosition;
import com.java1234.model.PageBean;
import com.java1234.util.StringUtil;

@Repository("carPositionDao")
public class CarPositionDaoImpl implements CarPositionDao{

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<CarPosition> find(PageBean pageBean, CarPosition carPosition) {
		StringBuffer sb=new StringBuffer("select * from t_carposition t1,t_cararea t2 where t1.areaId=t2.id");
		if(carPosition!=null){
			if(StringUtil.isNotEmpty(carPosition.getPositionNo())){
				sb.append(" and t1.positionNo like '%"+carPosition.getPositionNo()+"%'");
			}
			if(StringUtil.isNotEmpty(carPosition.getIsactive())){
				sb.append(" and t1.isactive = '"+carPosition.getIsactive()+"'");
			}
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		final List<CarPosition> carPositionList=new ArrayList<CarPosition>();
		jdbcTemplate.query(sb.toString(), new Object[]{}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				CarPosition carPosition=new CarPosition();
				carPosition.setId(rs.getInt("id"));
				carPosition.setPositionNo(rs.getString("positionNo"));
				carPosition.setAreaId(rs.getInt("areaId"));
				carPosition.setAreaName(rs.getString("areaName"));
				carPosition.setAddTime(rs.getTimestamp("addTime"));
				carPosition.setIsactive(rs.getString("isactive"));
				carPositionList.add(carPosition);
			}
		});
		return carPositionList;
	}
	

	@Override
	public int count(CarPosition carPosition) {
		StringBuffer sb=new StringBuffer("select count(*) from t_carposition");
		if(carPosition!=null){
			if(StringUtil.isNotEmpty(carPosition.getPositionNo())){
				sb.append(" and positionNo like '%"+carPosition.getPositionNo()+"%'");
			}
			if(StringUtil.isNotEmpty(carPosition.getIsactive())){
				sb.append(" and isactive = '"+carPosition.getIsactive()+"'");
			}
		}
		return jdbcTemplate.queryForObject(sb.toString().replaceFirst("and", "where"), Integer.class);
	}

	@Override
	public void add(CarPosition carPosition) {
		String sql="insert into t_carposition values(null,?,?,now(),'0')";
		jdbcTemplate.update(sql, new Object[]{carPosition.getAreaId(),carPosition.getAreaName()+carPosition.getPositionNo()});
	}

	@Override
	public void update(CarPosition carPosition) {
		String sql="update t_carposition set areaId=?,positionNo=? where id=?";
		jdbcTemplate.update(sql, new Object[]{carPosition.getAreaId(),carPosition.getPositionNo(),carPosition.getId()});
	}

	@Override
	public void delete(int id) {
		String sql="delete from t_carposition where id=?";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	@Override
	public CarPosition loadById(int id) {
		String sql="select * from t_carposition t1,t_cararea t2 where t1.areaId=t2.id and t1.id=?";
		final CarPosition carPosition=new CarPosition();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				carPosition.setId(rs.getInt("id"));
				carPosition.setPositionNo(rs.getString("positionNo"));
				carPosition.setAreaId(rs.getInt("areaId"));
				carPosition.setAreaName(rs.getString("areaName"));
				carPosition.setAddTime(rs.getTimestamp("addTime"));
				carPosition.setIsactive(rs.getString("isactive"));
			}
		});
		return carPosition;
	}
	
	@Override
	public boolean existCarPositionByAreaId(int areaId) {
		String sql="select count(*) from t_carposition where areaId=?";
		int result=jdbcTemplate.queryForObject(sql,new Object[]{areaId},Integer.class);
		if(result>0){
			return true;
		}else{
			return false;			
		}
	}
	
	@Override
	public void updateState(CarPosition carPosition) {
		String sql="update t_carposition set isactive=? where id=?";
		jdbcTemplate.update(sql, new Object[]{carPosition.getIsactive(),carPosition.getId()});
	}
	
	@Override
	public boolean existCarPositionIsactive(String carPositionId) {
		String sql="select count(*) from t_carposition t where t.isactive='0' and t.id=?";
		int result=jdbcTemplate.queryForObject(sql,new Object[]{carPositionId},Integer.class);
		if(result>0){
			return true;
		}else{
			return false;			
		}
	}
	
	@Override
	public CarPosition loadByPositionNo(String positionNo) {
		String sql="select * from t_carposition t where t.positionNo=?";
		final CarPosition carPosition=new CarPosition();
		jdbcTemplate.query(sql, new Object[]{positionNo}, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				carPosition.setId(rs.getInt("id"));
				carPosition.setAreaId(rs.getInt("areaId"));
				carPosition.setPositionNo(rs.getString("positionNo"));
				carPosition.setAddTime(rs.getTimestamp("addTime"));
				carPosition.setIsactive(rs.getString("isactive"));
			}
		});
		return carPosition;
	}

}
