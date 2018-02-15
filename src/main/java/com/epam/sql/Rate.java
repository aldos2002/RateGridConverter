package com.epam.sql;

/**
 * Created by Almas_Doskozhin
 * on 2/12/2018.
 */
public class Rate implements Cloneable{
    private String oid;
    private String update_user;
    private String code;
    private String description;
    private String end_date;

    private String start_date;
    private String biz_version;
    private String log_route_ref;
    private String grid_ref;
    private String service_ref;

    private String rate_formula_ref;
    private String currency_ref;
    private String deletion_date;
    private String creation_date;
    private String update_date;

    private String prev_version_id;
    private String original_version_id;
    private String tech_version;
    private String archived;
    private String record_version;

    private String owner_id;
    private String srv_time_qty;
    private String srv_time_unit;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getBiz_version() {
        return biz_version;
    }

    public void setBiz_version(String biz_version) {
        this.biz_version = biz_version;
    }

    public String getLog_route_ref() {
        return log_route_ref;
    }

    public void setLog_route_ref(String log_route_ref) {
        this.log_route_ref = log_route_ref;
    }

    public String getGrid_ref() {
        return grid_ref;
    }

    public void setGrid_ref(String grid_ref) {
        this.grid_ref = grid_ref;
    }

    public String getService_ref() {
        return service_ref;
    }

    public void setService_ref(String service_ref) {
        this.service_ref = service_ref;
    }

    public String getRate_formula_ref() {
        return rate_formula_ref;
    }

    public void setRate_formula_ref(String rate_formula_ref) {
        this.rate_formula_ref = rate_formula_ref;
    }

    public String getCurrency_ref() {
        return currency_ref;
    }

    public void setCurrency_ref(String currency_ref) {
        this.currency_ref = currency_ref;
    }

    public String getDeletion_date() {
        return deletion_date;
    }

    public void setDeletion_date(String deletion_date) {
        this.deletion_date = deletion_date;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getPrev_version_id() {
        return prev_version_id;
    }

    public void setPrev_version_id(String prev_version_id) {
        this.prev_version_id = prev_version_id;
    }

    public String getOriginal_version_id() {
        return original_version_id;
    }

    public void setOriginal_version_id(String original_version_id) {
        this.original_version_id = original_version_id;
    }

    public String getTech_version() {
        return tech_version;
    }

    public void setTech_version(String tech_version) {
        this.tech_version = tech_version;
    }

    public String getArchived() {
        return archived;
    }

    public void setArchived(String archived) {
        this.archived = archived;
    }

    public String getRecord_version() {
        return record_version;
    }

    public void setRecord_version(String record_version) {
        this.record_version = record_version;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getSrv_time_qty() {
        return srv_time_qty;
    }

    public void setSrv_time_qty(String srv_time_qty) {
        this.srv_time_qty = srv_time_qty;
    }

    public String getSrv_time_unit() {
        return srv_time_unit;
    }

    public void setSrv_time_unit(String srv_time_unit) {
        this.srv_time_unit = srv_time_unit;
    }

    @Override
    public String toString() {
        return "Insert into RATE(OID,UPDATE_USER,CODE,DESCRIPTION,END_DATE," +
                "START_DATE,BIZ_VERSION,LOG_ROUTE_REF,GRID_REF,SERVICE_REF," +
                "RATE_FORMULA_REF,CURRENCY_REF,DELETION_DATE,CREATION_DATE,UPDATE_DATE," +
                "PREV_VERSION_ID,ORIGINAL_VERSION_ID,TECH_VERSION,ARCHIVED,RECORD_VERSION," +
                "OWNER_ID,SRV_TIME_QTY,SRV_TIME_UNIT) " +
                "" +
                "values ("
                + oid +","+update_user+","+code+","+description+"," + end_date +"," +
                "" +
                start_date+","+biz_version+","+log_route_ref+","+grid_ref+","+service_ref+"," +
                rate_formula_ref+","+currency_ref+","+deletion_date+","+ creation_date +","+update_date+"," +
                prev_version_id+","+original_version_id+","+tech_version+","+archived+","+record_version+"," +
                owner_id+","+srv_time_qty+","+srv_time_unit+");";
    }

    public Rate clone() throws CloneNotSupportedException {
        return (Rate)super.clone();
    }
}
