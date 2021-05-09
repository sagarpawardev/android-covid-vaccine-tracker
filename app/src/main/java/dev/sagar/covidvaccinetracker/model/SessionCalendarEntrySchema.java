package dev.sagar.covidvaccinetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.List;

import dev.sagar.covidvaccinetracker.enums.FeeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionCalendarEntrySchema {

    @JsonProperty("center_id")
    private int centerId;
    private String name;

    @JsonProperty("name_l")
    private String nameL;
    private String address;

    @JsonProperty("address_l")
    private String addressL;

    @JsonProperty("state_name")
    private String stateName;

    @JsonProperty("state_name_l")
    private String stateNameL;

    @JsonProperty("district_name")
    private String districtName;

    @JsonProperty("district_name_l")
    private String districtNameL;

    @JsonProperty("block_name")
    private String blockName;

    @JsonProperty("block_name_l")
    private String blockNameL;
    private String pincode;
    private double lat;

    @JsonProperty("long")
    private double lng;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime from;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime to;

    @JsonProperty("fee_type")
    private FeeType feeType;

    @JsonProperty("vaccine_fees")
    private List<VaccineFeeSchema> vaccineFees;
    private List<VaccineSession> sessions;

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameL() {
        return nameL;
    }

    public void setNameL(String nameL) {
        this.nameL = nameL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressL() {
        return addressL;
    }

    public void setAddressL(String addressL) {
        this.addressL = addressL;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateNameL() {
        return stateNameL;
    }

    public void setStateNameL(String stateNameL) {
        this.stateNameL = stateNameL;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictNameL() {
        return districtNameL;
    }

    public void setDistrictNameL(String districtNameL) {
        this.districtNameL = districtNameL;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockNameL() {
        return blockNameL;
    }

    public void setBlockNameL(String blockNameL) {
        this.blockNameL = blockNameL;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public List<VaccineFeeSchema> getVaccineFees() {
        return vaccineFees;
    }

    public void setVaccineFees(List<VaccineFeeSchema> vaccineFees) {
        this.vaccineFees = vaccineFees;
    }

    public List<VaccineSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<VaccineSession> sessions) {
        this.sessions = sessions;
    }
}
