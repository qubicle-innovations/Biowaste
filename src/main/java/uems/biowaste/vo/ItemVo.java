package uems.biowaste.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Date;
	private String Month ;
	private String TotalWeight ;
	private String CreatedBy ;
	private  String ItemID ;
	private String TotalPatients;

	private String NoOfHaulage ;
	private String HualageCharge ;
	private  String DisposalFee ;
	private String TotalDisposalFee;

	private String CartonBox;
	private String Paper;
	private String Cans;
	private String Plastic;


	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getMonth() {
		return Month;
	}

	public void setMonth(String month) {
		Month = month;
	}

	public String getTotalWeight() {
		return TotalWeight;
	}

	public void setTotalWeight(String totalWeight) {
		TotalWeight = totalWeight;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

	public String getItemID() {
		return ItemID;
	}

	public void setItemID(String itemID) {
		ItemID = itemID;
	}

	public String getTotalPatients() {
		return TotalPatients;
	}

	public void setTotalPatients(String totalPatients) {
		TotalPatients = totalPatients;
	}

	public String getNoOfHaulage() {
		return NoOfHaulage;
	}

	public void setNoOfHaulage(String noOfHaulage) {
		NoOfHaulage = noOfHaulage;
	}

	public String getHualageCharge() {
		return HualageCharge;
	}

	public void setHualageCharge(String hualageCharge) {
		HualageCharge = hualageCharge;
	}

	public String getDisposalFee() {
		return DisposalFee;
	}

	public void setDisposalFee(String disposalFee) {
		DisposalFee = disposalFee;
	}

	public String getTotalDisposalFee() {
		return TotalDisposalFee;
	}

	public void setTotalDisposalFee(String totalDisposalFee) {
		TotalDisposalFee = totalDisposalFee;
	}

	public String getCartonBox() {
		return CartonBox;
	}

	public void setCartonBox(String cartonBox) {
		CartonBox = cartonBox;
	}

	public String getPaper() {
		return Paper;
	}

	public void setPaper(String paper) {
		Paper = paper;
	}

	public String getCans() {
		return Cans;
	}

	public void setCans(String cans) {
		Cans = cans;
	}

	public String getPlastic() {
		return Plastic;
	}

	public void setPlastic(String plastic) {
		Plastic = plastic;
	}
}
