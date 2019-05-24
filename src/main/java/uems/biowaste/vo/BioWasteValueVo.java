package uems.biowaste.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BioWasteValueVo  implements Serializable {

    private static final long serialVersionUID = 1L;
    String BiowasteValue;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBiowasteValue() {
        return BiowasteValue;
    }

    public void setBiowasteValue(String biowasteValue) {
        BiowasteValue = biowasteValue;
    }

}
