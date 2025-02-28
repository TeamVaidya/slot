package com.spring.vaidya.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Request payload for creating a slot")
public class SlotRequest {

    @Schema(description = "Start time of the slot", example = "09:00:00")
    private LocalTime startTime;

    @Schema(description = "End time of the slot", example = "09:30:00")
    private LocalTime endTime;

    @Schema(description = "Duration or time range of the slot", example = "30 minutes")
    private String slotRange;

    @Schema(description = "ID of the doctor associated with the slot", example = "101")
    private Long userId;

    @Schema(description = "Date for which the slot is available", example = "2024-02-25")
    private LocalDate date;

    
    
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public String getSlotRange() {
		return slotRange;
	}
	public void setSlotRange(String slotRange) {
		this.slotRange = slotRange;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "SlotRequest [startTime=" + startTime + ", endTime=" + endTime + ", slotRange=" + slotRange + ", userId="
				+ userId + ", date=" + date + "]";
	}
	public SlotRequest(LocalTime startTime, LocalTime endTime, String slotRange, Long userId, LocalDate date) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.slotRange = slotRange;
		this.userId = userId;
		this.date = date;
	}
	public SlotRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
