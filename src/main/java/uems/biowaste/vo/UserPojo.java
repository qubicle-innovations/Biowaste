package uems.biowaste.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPojo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserVo> ListUserDetails;

	public List<UserVo> getListUserDetails() {
		return ListUserDetails;
	}

	public void setListUserDetails(List<UserVo> listUserDetails) {
		ListUserDetails = listUserDetails;
	}
}
