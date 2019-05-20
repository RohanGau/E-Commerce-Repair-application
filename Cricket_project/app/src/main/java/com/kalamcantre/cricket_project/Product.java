package com.kalamcantre.cricket_project;

public class Product {
   private String pname,description,date,price,time,pid,image;

   public Product(){

   }

   public Product(String pname, String description, String date, String price, String time, String pid, String image) {
      this.pname = pname;
      this.description = description;
      this.date = date;
      this.price = price;
      this.time = time;
      this.pid = pid;
      this.image = image;
   }

   public String getPname() {
      return pname;
   }

   public void setPname(String pname) {
      this.pname = pname;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getPrice() {
      return price;
   }

   public void setPrice(String price) {
      this.price = price;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getPid() {
      return pid;
   }

   public void setPid(String pid) {
      this.pid = pid;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }
}
