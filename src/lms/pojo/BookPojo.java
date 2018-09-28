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
public class BookPojo {
    private String id, title, subTitle, category, author, printYear, bookNum, other, issueDate, returnDate;

    public BookPojo(String id, String title, String subTitle, String category, String author, String printYear, String bookNum, String other) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.category = category;
        this.author = author;
        this.printYear = printYear;
        this.bookNum = bookNum;
        this.other = other;
    }

    public BookPojo(String id, String title, String subTitle, String category, String author) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.category = category;
        this.author = author;
    }

    public BookPojo(String id, String title, String issueDate, String returnDate) {
        this.id = id;
        this.title = title;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrintYear() {
        return printYear;
    }

    public void setPrintYear(String printYear) {
        this.printYear = printYear;
    }

    public String getBookNum() {
        return bookNum;
    }

    public void setBookNum(String bookNum) {
        this.bookNum = bookNum;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

}
