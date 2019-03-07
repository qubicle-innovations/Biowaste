package uems.biowaste.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ReturnID;
	private String IssueID;
	private String Location ;
	private String Date ;
	private String TrollyNo;
	private String CreatedBy ;
	private String LocationID ;
	private List<GWasteItemVo> ListItems ;

	public String getReturnID() {
		return ReturnID;
	}

	public void setReturnID(String returnID) {
		ReturnID = returnID;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

	public String getLocationID() {
		return LocationID;
	}

	public void setLocationID(String locationID) {
		LocationID = locationID;
	}

	public List<GWasteItemVo> getListItems() {
		return ListItems;
	}

	public void setListItems(List<GWasteItemVo> listItems) {
		ListItems = listItems;
	}

	public String getTrollyNo() {
		return TrollyNo;
	}

	public void setTrollyNo(String trollyNo) {
		TrollyNo = trollyNo;
	}

	public String getIssueID() {
		return IssueID;
	}

	public void setIssueID(String issueID) {
		IssueID = issueID;
	}
}
