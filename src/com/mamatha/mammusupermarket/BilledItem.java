package com.mamatha.mammusupermarket;

public class BilledItem {
	private String iteamName;
	private   int price;
	private int totalPrice;
	private int quantity;
	private String brand;
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getIteamName() {
		return iteamName;
	}
	public void setIteamName(String iteamName) {
		this.iteamName = iteamName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "BilledItem [iteamName=" + iteamName + ", price=" + price + ", totalPrice=" + totalPrice + ", quantity="
				+ quantity +", Brand="+brand+ "]";
	}
	

}
