package com.mamatha.mammusupermarket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuperMarket {

	private DBConnect dbConnection;

	public SuperMarket() throws SQLException {

		dbConnection = new DBConnect();

	}

	public List<StockItem> getSuperMarketStock() {

		return dbConnection.getStock();

	}

	//male it as private so no once access directly  thz is for testing without brand name now it is not required
	public List<StockItem> getStockByListOfNames(List<String> itemsName) {

		return dbConnection.getStockDetailsByListOfNames(itemsName);

	}

	int updateBulkStock(List<StockItem> newStock) throws SQLException {

		int countSucess = 0;
		List<StockItem> insertStkList = new ArrayList();
		List<StockItem> updateStkList = new ArrayList();
		boolean realNewItem = true;
		
		//we have passed data into ToBillItem List from StockItem list using extracted() method
	    List<ToBillItems> lstData = extractedDataFromStockItemListtoTOBillItemList(newStock);

		List<StockItem> existingStock = dbConnection.getStockItemDetailByNameAndBrand(lstData);
		// StockItem exstock ;

		for (StockItem temp : newStock) {
			realNewItem = true;

			for (StockItem exstock : existingStock) {

				if ((temp.getItemName().equals(exstock.getItemName()))
						&& (temp.getBrand().equals(exstock.getBrand()))) {

					System.out.println("old item came today!! " + exstock.getItemName());
					System.out.println("exisitng stock of item " + existingStock); // we have to write toString()

					int newQunatity = temp.getQuantity();
					temp.setQuantity(exstock.getQuantity() + newQunatity);
					if (temp.getPrice() == 0)
						temp.setPrice(exstock.getPrice());

					if (exstock.getCategory() != null)
						temp.setCategory(exstock.getCategory());
					if(exstock.getBrand()!=null)
						temp.setCategory(exstock.getBrand());

					updateStkList.add(temp);
					realNewItem = false;
					break;

				}

			}

			if (realNewItem) {
				System.out.println("new item came today!! " + temp);

				insertStkList.add(temp);
			}

		}

		boolean updated = dbConnection.updateBulkStock(updateStkList);
		if (updated) {

			System.out.println("updated sucessfully : " + updateStkList);
			countSucess++;

			// return true;
		} else {

			System.out.println("failed to update : " + updateStkList);
			// return falseupdateStkList
		}

		boolean bulkItemsAdded = dbConnection.insertBulkStockItem(insertStkList);
		if (bulkItemsAdded) {
			System.out.println("inserted Sucessfully");
			countSucess++;
		} else

			System.out.println("Failed to insert");
		// if it does not enter for loop return false;

		return countSucess;

	}

	private List<ToBillItems> extractedDataFromStockItemListtoTOBillItemList(List<StockItem> newStock) {
		List<ToBillItems> lstData = new ArrayList<ToBillItems>();
		for (StockItem stItem : newStock) {
			ToBillItems temp  = new ToBillItems();
			temp.setIteamName(stItem.getItemName());
			temp.setBrand(stItem.getBrand());

		lstData.add(temp);

		}
		return lstData;
	}

	public List<BilledItem> printBillDetails(List<ToBillItems> itemsLs) {// by using  item name
		boolean itemExist = false;
		List<String> itemsNames = new ArrayList();
		List<BilledItem> totalBillDetails = new ArrayList();
		List<StockItem> updateStockList = new ArrayList();

		for (ToBillItems temp : itemsLs) {
			itemsNames.add(temp.getIteamName());
		}

		List<StockItem> existingStockList = dbConnection.getStockDetailsByListOfNames(itemsNames); // checking only with item name
		for (ToBillItems temp : itemsLs) {
			itemExist = false;
			for (StockItem exstock : existingStockList) {
				System.out.println("In for loop");
				if (temp.getIteamName().equals(exstock.getItemName())) {

					BilledItem billInfo = new BilledItem(); // if we write thz outside for loop it will create object
															// only once and it repalces all data with last added
															// product
					System.out.println("item present please give  bill to user");
					int pricePerUnit = exstock.getPrice();
					int noOfQuantitesrequired = temp.getQuantity();
					int totalPrice = noOfQuantitesrequired * pricePerUnit;
					billInfo.setIteamName(temp.getIteamName());
					billInfo.setPrice(pricePerUnit);
					billInfo.setTotalPrice(totalPrice);

					if (noOfQuantitesrequired <= exstock.getQuantity()) {
						billInfo.setQuantity(noOfQuantitesrequired);

						int dbQuantityUpdate = exstock.getQuantity() - noOfQuantitesrequired;

						exstock.setQuantity(dbQuantityUpdate);

						updateStockList.add(exstock);

						totalBillDetails.add(billInfo);
					} else {
						System.out.println(" i  dont have enough stock you asked for  ");
						System.out.println(" i have only " + exstock.getQuantity() + " Quantities");
					}

					itemExist = true;
					break;

				}
			}

			if (!itemExist) {
				System.out.println("i dont have that product in my supermarket");
			}

		}
		boolean updatedQuantity = dbConnection.updateBulkStock(updateStockList);
		if (updatedQuantity)
			System.out.println("data updated sucessfully in database");
		else
			System.out.println("Failed to update the quantity");

		return totalBillDetails;
	}

	public List<StockItem> getStockDetailsByCategoryName(String category) throws SQLException {

		return dbConnection.getStockDetailsByCategory(category);
	}

	public List<StockItem> getProductDetailsByNameAndBrand(List<ToBillItems> itemLst) {

		return dbConnection.getStockItemDetailByNameAndBrand(itemLst);

	}

	public List<BilledItem> CheckoutByNameAndBrand(List<ToBillItems> itemList) {

		List<StockItem> updateStockList = new ArrayList();
		List<BilledItem> totalBillDetails = new ArrayList();
		List<StockItem> existingStock = dbConnection.getStockItemDetailByNameAndBrand(itemList);
		boolean itemExist = false;
		for (ToBillItems item : itemList) {
			itemExist = false;
			for (StockItem exstock : existingStock) {
				if ((item.getIteamName().equals(exstock.getItemName()))
						&& (item.getBrand().equals(exstock.getBrand()))) {

					BilledItem billData = new BilledItem();
					System.out.println("item present please give  bill to user "+item.getIteamName());
					int pricePerUnit = exstock.getPrice();
					int noOfQuantitesrequired = item.getQuantity();
					int totalPrice = noOfQuantitesrequired * pricePerUnit;
				//	String brand = item.getBrand();
					billData.setIteamName(item.getIteamName());
					billData.setPrice(pricePerUnit);
					billData.setTotalPrice(totalPrice);
					billData.setBrand(item.getBrand());

					if (noOfQuantitesrequired <= exstock.getQuantity()) {
						billData.setQuantity(noOfQuantitesrequired);

						int dbQuantityUpdate = exstock.getQuantity() - noOfQuantitesrequired;

						exstock.setQuantity(dbQuantityUpdate);
						updateStockList.add(exstock);

						totalBillDetails.add(billData);
					} else {
						System.out.println(" i  dont have enough stock you asked for  ");
						System.out.println(" i have only " + exstock.getQuantity() + " Quantities");
					}

					itemExist = true;
					break;

				}
			}

			if (!itemExist) {
				System.out.println("i dont have that product in my supermarket");
			}

		}
		boolean updatedQuantity = dbConnection.updateStockQuantityBYItemNameAndBrand(updateStockList);
		if (updatedQuantity)
			System.out.println("data updated sucessfully in database");
		else
			System.out.println("Failed to update the quantity");

		return totalBillDetails;

	}

//	public StockItem getStockDetailByIteamName(String itemName)
//	{
//		try {
//			 StockItem  gotStock =  dbConnection.getStockDetailsByName(itemName);
//			System.out.println("got stock details with iteam Name : "+itemName+" ::"+gotStock);
//			return gotStock;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("failed to get stock detail by name" +itemName);
//			return null;
//		}
//	}

	public void close() {
		if (dbConnection != null)
			dbConnection.close();
	}
}
