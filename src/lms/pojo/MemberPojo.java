/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lms.pojo;

/**
 *
 * @author SRUN VANNARA
 */
public class MemberPojo {
    private String id, name, latin, gender, bd, village, commune, district, province, phone, lateReturn;

    public MemberPojo(String id, String name, String latin, String gender, String bd, String village, String commune, String district, String province, String phone) {
        this.id = id;
        this.name = name;
        this.latin = latin;
        this.gender = gender;
        this.bd = bd;
        this.village = village;
        this.commune = commune;
        this.district = district;
        this.province = province;
        this.phone = phone;
    }

    public MemberPojo(String id, String name, String latin, String gender, String phone) {
        this.id = id;
        this.name = name;
        this.latin = latin;
        this.gender = gender;
        this.phone = phone;
    }

    public MemberPojo(String id, String name, String latin, String gender, String phone, String lateReturn) {
        this.id = id;
        this.name = name;
        this.latin = latin;
        this.gender = gender;
        this.phone = phone;
        this.lateReturn = lateReturn;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the latin
     */
    public String getLatin() {
        return latin;
    }

    /**
     * @param latin the latin to set
     */
    public void setLatin(String latin) {
        this.latin = latin;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the bd
     */
    public String getBd() {
        return bd;
    }

    /**
     * @param bd the bd to set
     */
    public void setBd(String bd) {
        this.bd = bd;
    }

    /**
     * @return the village
     */
    public String getVillage() {
        return village;
    }

    /**
     * @param village the village to set
     */
    public void setVillage(String village) {
        this.village = village;
    }

    /**
     * @return the commune
     */
    public String getCommune() {
        return commune;
    }

    /**
     * @param commune the commune to set
     */
    public void setCommune(String commune) {
        this.commune = commune;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLateReturn() {
        return lateReturn;
    }

    public void setLateReturn(String lateReturn) {
        this.lateReturn = lateReturn;
    }

    
}
