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

			StockItem newTemp = isNewItemPresentInExStock(temp, existingStock);
			
			if(newTemp != null)
				updateStkList.add(newTemp); // present in exStock so adding newTemp returned from method
			else{
				System.out.println("new item came today!! " + temp);
				insertStkList.add(temp); // because it is not present in exStock so adding temp from passed StockItem
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
	public StockItem isNewItemPresentInExStock(StockItem temp, List<StockItem> existingStock) {
	
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

				
				return temp;
			}

		}
		return null;
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


	public void close() {
		if (dbConnection != null)
			dbConnection.close();
	}
}
