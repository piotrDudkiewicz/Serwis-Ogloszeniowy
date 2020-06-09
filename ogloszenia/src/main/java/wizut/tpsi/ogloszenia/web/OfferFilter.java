/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia.web;

/**
 *
 * @author Pioterk
 */
public class OfferFilter {
    private Integer  manufacturerId;
    private Integer modelId;
    private Integer yearFrom;
    private Integer yearTo;
    private String  sortOption;
    private Integer fuelTypeId;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

    public String getSortOption() {
        return sortOption;
    }

    public void setSortOption(String sortOption) {
        this.sortOption = sortOption;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(Integer yearFrom) {
        this.yearFrom = yearFrom;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    public void setYearTo(Integer yearTo) {
        this.yearTo = yearTo;
    }

    public Integer getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(Integer fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }
}
