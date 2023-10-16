package com.java1234.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.java1234.dao.CarStopDao;
import com.java1234.model.CarStop;
import com.java1234.model.PageBean;
import com.java1234.util.StringUtil;

@Repository("carStopDao")
public class CarStopDaoImpl implements CarStopDao{

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<CarStop> find(PageBean pageBean, CarStop carStop) {
		StringBuffer sb=new StringBuffer("select * from t_carstop t1,t_cartype t2,t_carposition t3 where t1.carTypeId=t2.id and t1.carPositionId=t3.id");
		if(carStop!=null){
			if(StringUtil.isNotEmpty(carStop.getCarNumber())){
				sb.append(" and t1.carNumber like '%"+carStop.getCarNumber()+"%'");
			}
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		final List<CarStop> carStopList=new ArrayList<CarStop>();
		jdbcTemplate.query(sb.toString()/*.replaceFirst("and", "where")*/, new Object[]{}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				CarStop carStop=new CarStop();
				carStop.setId(rs.getInt("id"));
				carStop.setCarNumber(rs.getString("carNumber"));
				carStop.setStartTime(rs.getString("startTime"));
				carStop.setEndTime(rs.getString("endTime"));
				carStop.setStopDuration(rs.getDouble("stopDuration"));
				carStop.setStopCost(rs.getDouble("stopCost"));
				carStop.setCarTypeId(rs.getInt("carTypeId"));
				carStop.setCarTypeName(rs.getString("carTypeName"));
				carStop.setCarPositionNo(rs.getString("positionNo"));
				carStopList.add(carStop);
			}
		});
		return carStopList;
	}

	@Override
	public int count(CarStop carStop) {
		StringBuffer sb=new StringBuffer("select count(*) from t_carstop t1,t_cartype t2,t_carposition t3 where t1.carTypeId=t2.id and t1.carPositionId=t3.id");
		if(carStop!=null){
			if(StringUtil.isNotEmpty(carStop.getCarNumber())){
				sb.append(" and t1.carNumber like '%"+carStop.getCarNumber()+"%'");
			}
		}
		return jdbcTemplate.queryForObject(sb.toString()/*.replaceFirst("and", "where")*/, Integer.class);
	}

	@Override
	public void add(CarStop carStop) {
		String sql="insert into t_carstop(id,carNumber,startTime,endTime,stopDuration,stopCost,carTypeId,carPositionId) values(null,?,now(),?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{carStop.getCarNumber(),carStop.getEndTime(),carStop.getStopDuration(),carStop.getStopCost(),carStop.getCarTypeId(),carStop.getCarPositionId()});
	}

	@Override
	public void update(CarStop carStop) {
		String sql="update t_carstop set carNumber=?,endTime=?,stopDuration=?,stopCost=?,carPositionNo=?,price=? where id=?";
		jdbcTemplate.update(sql, new Object[]{carStop.getCarNumber(),carStop.getEndTime(),carStop.getStopDuration(),carStop.getStopCost(),carStop.getCarPositionNo(),carStop.getPrice(),carStop.getId()});
		//String sql="update t_carstop set carNumber=?,stopDuration=?,stopCost=?,carTypeId=?,carPositionNo=? where id=?";
		//jdbcTemplate.update(sql, new Object[]{carStop.getCarNumber(),carStop.getStopDuration(),carStop.getStopCost(),carStop.getCarTypeId(),carStop.getCarPositionNo(),carStop.getId()});
	}

	@Override
	public void delete(int id) {
		String sql="delete from t_carstop where id=?";
		jdbcTemplate.update(sql, new Object[]{id});
	}

	@Override
	public CarStop loadById(int id) {
		String sql="select * from t_carstop t1,t_cartype t2,t_carposition t3 where t1.carTypeId=t2.id and t1.carPositionId=t3.id and t1.id=?";
		final CarStop carStop=new CarStop();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				carStop.setId(rs.getInt("id"));
				carStop.setCarNumber(rs.getString("carNumber"));
				carStop.setStartTime(rs.getString("startTime"));
				carStop.setEndTime(rs.getString("endTime"));
				carStop.setStopDuration(rs.getDouble("stopDuration"));
				carStop.setStopCost(rs.getDouble("stopCost"));
				carStop.setCarTypeId(rs.getInt("carTypeId"));
				carStop.setCarTypeName(rs.getString("carTypeName"));
				carStop.setCarPositionId(rs.getInt("carPositionId"));
				carStop.setCarPositionNo(rs.getString("positionNo"));
			}
		});
		return carStop;
	}

	@Override
	public CarStop getFee(int id, String endTime) {
		String sql="SELECT t3.price price,round((unix_timestamp('"+endTime+"') - unix_timestamp(t1.startTime))/ 3600,2) stopDuration,t3.price * round((unix_timestamp('"+endTime+"') - unix_timestamp(t1.startTime)) / 3600,2) stopCost FROM t_carstop t1,t_carposition t2,t_cararea t3 WHERE t1.carPositionId = t2.id AND t2.areaId = t3.id AND t1.id = ?";
		final CarStop carStop=new CarStop();
		jdbcTemplate.query(sql, new Object[]{id}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				carStop.setPrice(rs.getDouble("price"));
				carStop.setStopDuration(rs.getDouble("stopDuration"));
				carStop.setStopCost(rs.getDouble("stopCost"));
			}
		});
		return carStop;
	}

	@Override
	public List<CarStop> exportCarStop() {
	       return this.jdbcTemplate.query("select t.id,t.carNumber,t2.carTypeName,t3.positionNo,t.startTime,t.endTime,t.price,t.stopDuration,t.stopCost from t_carstop t,t_cartype t2,t_carposition t3 where t.carTypeId=t2.id and t3.id=t.carPositionId and t.stopCost is not null",
	               new RowMapper<CarStop>() {
	                   public CarStop mapRow(ResultSet rs, int arg1)
	                           throws SQLException {
	                       return new CarStop(rs.getInt("id"), rs.getString("carNumber"), rs.getString("carTypeName"), rs.getString("positionNo"), rs.getString("startTime"), rs.getString("endTime"), rs.getDouble("price"), rs.getDouble("stopDuration"), rs.getDouble("stopCost"));
	                   }
	               });
	   }

	@Override
	public boolean existCarStopByCarTypeId(int carTypeId) {
		String sql="select count(*) from t_carstop t where t.carTypeId=?";
		int result=jdbcTemplate.queryForObject(sql,new Object[]{carTypeId},Integer.class);
		if(result>0){
			return true;
		}else{
			return false;			
		}
	}

	@Override
	public boolean existCarStopByCarPositionId(int carPositionId) {
		String sql="select count(*) from t_carstop t where t.carPositionId=?";
		int result=jdbcTemplate.queryForObject(sql,new Object[]{carPositionId},Integer.class);
		if(result>0){
			return true;
		}else{
			return false;			
		}
	}

	@Override
	public void updateCarPositionId(CarStop carStop) {
		String sql="update t_carstop set carPositionId=? where carPositionNo=?";
		jdbcTemplate.update(sql, new Object[]{carStop.getCarPositionId(),carStop.getCarPositionNo()});
	}
	
}
