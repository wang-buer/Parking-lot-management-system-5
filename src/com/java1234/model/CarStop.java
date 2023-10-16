package com.java1234.model;

public class CarStop {
	private Integer id;
	private String carNumber;
	private Integer carTypeId;
	private String startTime;
	private String endTime;
	private Double stopDuration;
	private Double stopCost;
	private Double price;
	private String carTypeName;
	private Integer carPositionId;
	private String carPositionNo;
	
	public Integer getCarPositionId() {
		return carPositionId;
	}
	public void setCarPositionId(Integer carPositionId) {
		this.carPositionId = carPositionId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCarPositionNo() {
		return carPositionNo;
	}
	public void setCarPositionNo(String carPositionNo) {
		this.carPositionNo = carPositionNo;
	}
	public String getCarTypeName() {
		return carTypeName;
	}
	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public Integer getCarTypeId() {
		return carTypeId;
	}
	public void setCarTypeId(Integer carTypeId) {
		this.carTypeId = carTypeId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Double getStopDuration() {
		return stopDuration;
	}
	public void setStopDuration(Double stopDuration) {
		this.stopDuration = stopDuration;
	}
	public Double getStopCost() {
		return stopCost;
	}
	public void setStopCost(Double stopCost) {
		this.stopCost = stopCost;
	}
	public CarStop(Integer id, String carNumber , String carTypeName, String carPositionNo, String startTime, String endTime,
			Double price, Double stopDuration, Double stopCost) {
		super();
		this.id = id;
		this.carNumber = carNumber;
		this.carTypeName = carTypeName;
		this.carPositionNo = carPositionNo;
		this.startTime = startTime;
		this.endTime = endTime;
		this.price = price;
		this.stopDuration = stopDuration;
		this.stopCost = stopCost;
	}
	
	public CarStop(){
		
	}
	
	
}
