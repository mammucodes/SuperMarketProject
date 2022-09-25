package com.mamatha.mammusupermarket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	static SuperMarket sm = null;

	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);

		try {

//			
//			List<ItemDetails> itemsDetail  = sm.getSuperMarketStock();
//			
//					for(ItemDetails item :itemsDetail)
//					{
//						System.out.println(item.getItemName()+" , "+item.getPrice()+" ,"+item.getQuantity()+" , "+item.getCategory());
//					}
//			
//

//			for(StockItem tempStock : newStockList) {
//				
//			
//		  boolean inserted = 	db.insertStockItem(tempStock);
//			
//		if(inserted)
//			System.out.println("inserted sucessfully");
//		else
//			System.out.println("failed to insert");
//		
//			}

			System.out.println("enter the below  required  option to get details ");
			System.out.println(
					"Press 1 :To getSuperMarketTotalStock Details\n Press 2 :To getStockDetailsByname\n Press 3 : TO  updateStockListByBulk \n Press 4 : To printBillDetailsUsingName\n Press 5 : To getStock Details By Category info\n press 6 :getStock Details by Name and Brand\nPress 7: To checkoutAndPrintBillDetailsByNameAndBrand");

			int input = sc.nextInt();
			sc.nextLine();

			sm = new SuperMarket();
			switch (input) {
			
			case 1:
				sm.getSuperMarketStock();
				break;
			case 2:
				getStockDetailsByname();
				break;
			case 3:
				updateStockListByBulk();

				break;
//			case 3:
//				System.out.println("enter iteam name you want to search");
//				String iteamName = sc.next();
//				sm.getStockDetailByIteamName(iteamName);
//				return;
			case 4:
				printBillDetailsUsingName();
				break;
			case 5:
				getStockDetailsByCategoryInfo("Dairy");
				break;
			case 6:
				getStockDetailsByNameAndBrand();
				break;
			case 7:
				checkoutAndPrintBillDetailsByNameAndBrand();
				break;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			sm.close();
		}

	}

	public static void getStockDetailsByname() {

		List<String> itemNames = new ArrayList<>();

		String Apple = "Apple";
		itemNames.add(Apple);
		itemNames.add("Orange");
		itemNames.add("Hatsun Ghee");
		System.out.println("testing");
		List<StockItem> stockDetails = sm.getStockByListOfNames(itemNames);
		for (StockItem temp : stockDetails) {
			System.out.println(temp);
		}
	}

	public static void updateStockListByBulk() throws SQLException {

		StockItem tu = new StockItem();
		tu.setItemName("ThumbsUp");
		tu.setPrice(25);
		tu.setQuantity(200);
		tu.setCategory("Driqwernks");

		StockItem oil = new StockItem();
		oil.setItemName("Oil");
		oil.setPrice(199);
		oil.setQuantity(100);
		oil.setCategory("CooklkjhgfdingOils");

		StockItem apple = new StockItem();
		apple.setItemName("Apple");
		apple.setPrice(123);
		apple.setQuantity(30);
		apple.setCategory("Fruits");
		
		StockItem orange = new StockItem();
		orange.setItemName("Orange");
		orange.setPrice(66);
		orange.setQuantity(50);
		orange.setCategory("Fruits");
		orange.setBrand("jammuOrange");

		StockItem yogurt = new StockItem();
		yogurt.setItemName( "Milky&Misty Yogurt");
		yogurt.setPrice(50);
		yogurt.setQuantity(5);
		yogurt.setCategory("Dairy");
		yogurt.setBrand("Milky&Misty");

		

		StockItem ghee = new StockItem();
		ghee.setItemName("Hatsun Ghee");
		ghee.setPrice(224);
		ghee.setQuantity(5);
		ghee.setCategory("Dairy");
		ghee.setBrand("Hatsun");
		
		StockItem corn = new StockItem();
		corn.setItemName("Sweet Corn");
		corn.setPrice(30);
		corn.setQuantity(5);
		corn.setCategory("Vegtables");
		corn.setBrand("IndianCorn");

		List<StockItem> newStockList = new ArrayList();
		// newStockList.add(tu);
		// newStockList.add(oil);
		// newStockList.add(apple);
		 newStockList.add(orange);
		newStockList.add(yogurt);
		newStockList.add(ghee);
		newStockList.add(corn);

		int totalItemsUpdated = sm.updateBulkStock(newStockList);
		System.out.println(totalItemsUpdated + " Stock Items updated ");
	}

	public static void getStockDetailsByNameAndBrand() throws SQLException {

		ToBillItems milk = new ToBillItems();
		milk.setIteamName("TonedMilk");
		milk.setBrand("Amul");

		ToBillItems curd = new ToBillItems();
		curd.setIteamName("Curd");
		curd.setBrand("Arogya");
		List<ToBillItems> data = new ArrayList();
		data.add(milk);
		data.add(curd);

		List<StockItem> st = sm.getProductDetailsByNameAndBrand(data);
		for (StockItem temp : st) {
			System.out.println(temp);
		}
	}

	public static void getStockDetailsByCategoryInfo(String category) {

		try {
			System.out.println("Product present in " + category + " are :");
			List<StockItem> stockDetails = sm.getStockDetailsByCategoryName(category);

			for (StockItem temp : stockDetails) {

				System.out.println(temp);
			}
		} catch (SQLException e) {
			System.out.println("failed to get data ");
			e.printStackTrace();
		}

	}

	public static void printBillDetailsUsingName() {

		List<ToBillItems> billforproducts = new ArrayList<ToBillItems>();
		ToBillItems applefru = new ToBillItems();
		applefru.setIteamName("Apple");
		applefru.setQuantity(180);

		ToBillItems orangefru = new ToBillItems();
		orangefru.setIteamName("Orange");
		orangefru.setQuantity(4);
		ToBillItems chips = new ToBillItems();
		chips.setIteamName("Lays Chips");
		chips.setQuantity(3);

		billforproducts.add(applefru);
		billforproducts.add(orangefru);
		billforproducts.add(chips);
		List<BilledItem> finalbill = sm.printBillDetails(billforproducts);
		for (BilledItem temp : finalbill) {
			System.out.println(temp);
		}
	}

	public static void checkoutAndPrintBillDetailsByNameAndBrand() {
		ToBillItems milk = new ToBillItems();
		milk.setIteamName("TonedMilk");
		milk.setBrand("Amul");
		milk.setQuantity(3);
		ToBillItems curd = new ToBillItems();
		curd.setIteamName("Curd");
		curd.setBrand("Hatsun");
		curd.setQuantity(2);

		ToBillItems fullcreammilk = new ToBillItems();
		fullcreammilk.setIteamName("FullCreamMilk");
		fullcreammilk.setBrand("Hatsun");
		fullcreammilk.setQuantity(400);

		ToBillItems junk = new ToBillItems();
		junk.setIteamName("junk");
		junk.setBrand("Hatsun");
		junk.setQuantity(4);

		List<ToBillItems> data = new ArrayList<>();
		data.add(milk);
		data.add(curd);
		data.add(fullcreammilk);
		data.add(junk);
		List<BilledItem> finalbill = sm.CheckoutByNameAndBrand(data);
		for (BilledItem temp : finalbill) {
			System.out.println(temp);
		}

	}

}
