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
public class MaterialPojo {

    int id;
    private String name, price, num, donate, other;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDonate() {
        return donate;
    }

    public void setDonate(String donate) {
        this.donate = donate;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public MaterialPojo(int id, String name, String price, String num, String donate, String other) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.num = num;
        this.donate = donate;
        this.other = other;
    }

}
