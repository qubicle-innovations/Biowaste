package uems.biowaste.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ItemID;
	private String Itemname ;
	private String Size ;
	private String Norm ;
	private String Qty ;
	private String Balance;
	private String LinenTypeID ;
	private String LinenCategoryID ;
	private String LinenAttributeID ;
	private String LocationId;

	public String getItemID() {
		return ItemID;
	}

	public void setItemID(String itemID) {
		ItemID = itemID;
	}

	public String getItemname() {
		return Itemname;
	}

	public void setItemname(String itemname) {
		Itemname = itemname;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getNorm() {
		return Norm;
	}

	public void setNorm(String norm) {
		Norm = norm;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String qty) {
		Qty = qty;
	}

	public String getLinenTypeID() {
		return LinenTypeID;
	}

	public void setLinenTypeID(String linenTypeID) {
		LinenTypeID = linenTypeID;
	}

	public String getLinenCategoryID() {
		return LinenCategoryID;
	}

	public void setLinenCategoryID(String linenCategoryID) {
		LinenCategoryID = linenCategoryID;
	}

	public String getLinenAttributeID() {
		return LinenAttributeID;
	}

	public void setLinenAttributeID(String linenAttributeID) {
		LinenAttributeID = linenAttributeID;
	}

	public String getBalance() {
		return Balance;
	}

	public void setBalance(String balance) {
		Balance = balance;
	}

	public String getLocationId() {
		return LocationId;
	}

	public void setLocationId(String locationId) {
		LocationId = locationId;
	}
}
