package uems.biowaste.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BioWasteItemVo implements Serializable {

    /**
     *
     *
     * "{
     *     ""ListGetBiowasteDetails"": [
     *         {
     *             ""Date"": ""15/05/2019"",
     *             ""Month"": ""May"",
     *             ""TotalCost"": ""110.00"",
     *             ""CreatedBy"": ""liyana"",
     *             ""TotalBin"": ""22"",
     *             ""CytotoxicWaste"": ""4"",
     *             ""RadioactiveWaste"": ""5"",
     *             ""ChemicalWaste"": ""6"",
     *             ""OtherWaste"": ""7"",
     *             ""CytotoxicWasteCost"": ""5.00"",
     *             ""RadioactiveWasteCost"": ""5.00"",
     *             ""ChemicalWasteCost"": ""5.00"",
     *             ""OtherWasteCost"": ""5.00"",
     *             ""CytotoxicWasteTotal"": ""20.00"",
     *             ""RadioactiveWasteTotal"": ""25.00"",
     *             ""ChemicalWasteTotal"": ""30.00"",
     *             ""OtherWasteTotal"": ""35.00""
     *         }
     *     ]
     * }"
     */
    private static final long serialVersionUID = 1L;
    private String Date;
    private String Month;
    private String TotalCost;
    private String CreatedBy;
    private String ItemID;
    private String TotalBin;
    private String CytotoxicWaste;
    private String RadioactiveWaste;
    private String ChemicalWaste;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCytotoxicWaste() {
        return CytotoxicWaste;
    }

    public void setCytotoxicWaste(String cytotoxicWaste) {
        CytotoxicWaste = cytotoxicWaste;
    }

    public String getRadioactiveWaste() {
        return RadioactiveWaste;
    }

    public void setRadioactiveWaste(String radioactiveWaste) {
        RadioactiveWaste = radioactiveWaste;
    }

    public String getChemicalWaste() {
        return ChemicalWaste;
    }

    public void setChemicalWaste(String chemicalWaste) {
        ChemicalWaste = chemicalWaste;
    }

    public String getOtherWaste() {
        return OtherWaste;
    }

    public void setOtherWaste(String otherWaste) {
        OtherWaste = otherWaste;
    }

    public String getCytotoxicWasteCost() {
        return CytotoxicWasteCost;
    }

    public void setCytotoxicWasteCost(String cytotoxicWasteCost) {
        CytotoxicWasteCost = cytotoxicWasteCost;
    }

    public String getRadioactiveWasteCost() {
        return RadioactiveWasteCost;
    }

    public void setRadioactiveWasteCost(String radioactiveWasteCost) {
        RadioactiveWasteCost = radioactiveWasteCost;
    }

    public String getChemicalWasteCost() {
        return ChemicalWasteCost;
    }

    public void setChemicalWasteCost(String chemicalWasteCost) {
        ChemicalWasteCost = chemicalWasteCost;
    }

    public String getOtherWasteCost() {
        return OtherWasteCost;
    }

    public void setOtherWasteCost(String otherWasteCost) {
        OtherWasteCost = otherWasteCost;
    }

    public String getCytotoxicWasteTotal() {
        return CytotoxicWasteTotal;
    }

    public void setCytotoxicWasteTotal(String cytotoxicWasteTotal) {
        CytotoxicWasteTotal = cytotoxicWasteTotal;
    }

    public String getRadioactiveWasteTotal() {
        return RadioactiveWasteTotal;
    }

    public void setRadioactiveWasteTotal(String radioactiveWasteTotal) {
        RadioactiveWasteTotal = radioactiveWasteTotal;
    }

    public String getChemicalWasteTotal() {
        return ChemicalWasteTotal;
    }

    public void setChemicalWasteTotal(String chemicalWasteTotal) {
        ChemicalWasteTotal = chemicalWasteTotal;
    }

    public String getOtherWasteTotal() {
        return OtherWasteTotal;
    }

    public void setOtherWasteTotal(String otherWasteTotal) {
        OtherWasteTotal = otherWasteTotal;
    }

    private String OtherWaste;
    private String CytotoxicWasteCost;
    private String RadioactiveWasteCost;
    private String ChemicalWasteCost;
    private String OtherWasteCost;
    private String CytotoxicWasteTotal;
    private String RadioactiveWasteTotal;
    private String ChemicalWasteTotal;
    private String OtherWasteTotal;
    private String BiohazardWaste;
    private String BiohazardWasteCost;
    private String BiohazardWasteTotal;

    public String getBiohazardWaste() {
        return BiohazardWaste;
    }

    public void setBiohazardWaste(String biohazardWaste) {
        BiohazardWaste = biohazardWaste;
    }

    public String getBiohazardWasteCost() {
        return BiohazardWasteCost;
    }

    public void setBiohazardWasteCost(String biohazardWasteCost) {
        BiohazardWasteCost = biohazardWasteCost;
    }

    public String getBiohazardWasteTotal() {
        return BiohazardWasteTotal;
    }

    public void setBiohazardWasteTotal(String biohazardWasteTotal) {
        BiohazardWasteTotal = biohazardWasteTotal;
    }

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

    public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String totalCost) {
        TotalCost = totalCost;
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

    public String getTotalBin() {
        return TotalBin;
    }

    public void setTotalBin(String totalBin) {
        TotalBin = totalBin;
    }
}
