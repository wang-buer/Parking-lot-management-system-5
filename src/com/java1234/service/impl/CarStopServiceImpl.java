package com.java1234.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.java1234.dao.CarStopDao;
import com.java1234.model.CarStop;
import com.java1234.model.PageBean;
import com.java1234.service.CarStopService;

@Service("carStopService")
public class CarStopServiceImpl implements CarStopService{

	@Resource
	private CarStopDao carStopDao;
	
	@Override
	public List<CarStop> find(PageBean pageBean, CarStop carStop) {
		return carStopDao.find(pageBean, carStop);
	}

	@Override
	public int count(CarStop carStop) {
		return carStopDao.count(carStop);
	}

	@Override
	public void add(CarStop carStop) {
		carStopDao.add(carStop);
	}

	@Override
	public void update(CarStop carStop) {
		carStopDao.update(carStop);
	}

	@Override
	public void delete(int id) {
		carStopDao.delete(id);
	}

	@Override
	public CarStop loadById(int id) {
		return carStopDao.loadById(id);
	}
	
	@Override
	public CarStop getFee(int id,String p_endTime) {
		return carStopDao.getFee(id,p_endTime);
	}

	@Override
	public void export(String[] titles, ServletOutputStream out) {                
	       try{
	           // 第一步，创建一个workbook，对应一个Excel文件
	           HSSFWorkbook workbook = new HSSFWorkbook();
	           // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	           HSSFSheet hssfSheet = workbook.createSheet("sheet1");
	           // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	           HSSFRow hssfRow = hssfSheet.createRow(0);
	           // 第四步，创建单元格，并设置值表头 设置表头居中
	           HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
	           //居中样式
	           hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	           HSSFCell hssfCell = null;
	           for (int i = 0; i < titles.length; i++) {
	               hssfCell = hssfRow.createCell(i);//列索引从0开始
	               hssfCell.setCellValue(titles[i]);//列名1
	               hssfCell.setCellStyle(hssfCellStyle);//列居中显示                
	           }
	           
	           // 第五步，写入实体数据 
	           List<CarStop> carStopList = carStopDao.exportCarStop();            

	           SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	           if(carStopList != null && !carStopList.isEmpty()){
	               for (int i = 0; i < carStopList.size(); i++) {
	                   hssfRow = hssfSheet.createRow(i+1);                
	                   CarStop carStop = carStopList.get(i);
	                   
	                   // 第六步，创建单元格，并设置值
	                   //Integer id, 
	                   //String carNumber , 
	                   //String carTypeName, 
	                   //String carPositionNo, 
	                   //String startTime, 
	                   //String endTime,
	       			   //Double price, 
	                   //Double stopDuration, Double stopCost
	                  int id = 0;
	                   if(carStop.getId() != 0){
	                	   id = carStop.getId();
	                   }
	                   hssfRow.createCell(0).setCellValue(id);
	                   
	                   String carNumber = "";
	                   if(carStop.getCarNumber() != null){
	                	   carNumber = carStop.getCarNumber();
	                   }
	                   hssfRow.createCell(1).setCellValue(carNumber);
	                   
	                   String carTypeName = "";
	                   if(carStop.getCarTypeName() != null){
	                	   carTypeName = carStop.getCarTypeName();
	                   }
	                   hssfRow.createCell(2).setCellValue(carTypeName);
	                   
	                   String carPositionNo = "";
	                   if(carStop.getCarPositionNo() != null){
	                	   carPositionNo = carStop.getCarPositionNo();
	                   }
	                   hssfRow.createCell(3).setCellValue(carPositionNo);
	                   
	                   String startTime = "";
	                   if(carStop.getCarPositionNo() != null){
	                	   startTime = carStop.getStartTime();
	                   }
	                   hssfRow.createCell(4).setCellValue(startTime);
	                   
	                   String endTime = "";
	                   if(carStop.getEndTime() != null){
	                	   endTime = carStop.getEndTime();
	                   }
	                   hssfRow.createCell(5).setCellValue(endTime);
	                   
	                   Double price = 0.0;
	                   if(carStop.getPrice() != null){
	                	   price = carStop.getPrice();
	                   }
	                   hssfRow.createCell(6).setCellValue(price);
	                   
	                   Double stopDuration = 0.0;
	                   if(carStop.getStopDuration() != null){
	                	   stopDuration = carStop.getStopDuration();
	                   }
	                   hssfRow.createCell(7).setCellValue(stopDuration);
	                   
	                   Double stopCost = 0.0;
	                   if(carStop.getStopCost() != null){
	                	   stopCost = carStop.getStopCost();
	                   }
	                   hssfRow.createCell(8).setCellValue(stopCost);
	               }
	           }
	           
	           // 第七步，将文件输出到客户端浏览器
	           try {
	               workbook.write(out);
	               out.flush();
	               out.close();

	           } catch (Exception e) {
	               e.printStackTrace();
	           }
	       }catch(Exception e){
	           e.printStackTrace();
	           try {
				throw new Exception("导出信息失败！");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       }    
	  }

	@Override
	public boolean existCarStopByCarTypeId(int carTypeId) {
		return carStopDao.existCarStopByCarTypeId(carTypeId);
	}

	@Override
	public boolean existCarStopByCarPositionId(int carPositionId) {
		return carStopDao.existCarStopByCarPositionId(carPositionId);
	}

	@Override
	public void updateCarPositionId(CarStop carStop) {
		carStopDao.updateCarPositionId(carStop);
	}

}
