package com.mamatha.mammusupermarket;

public class StockItem {

	private String ItemName;
	private  int Price;
	private   int Quantity;
	private String Category;
	private String Brand;
	
	
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getBrand() {
		return Brand;
	}
	public void setBrand(String brand) {
		Brand = brand;
	}
	
	public String toString() {
		return  ItemName+" "+Price+" "+Quantity+" "+Category+" "+Brand;
	}
	 
		
	}


