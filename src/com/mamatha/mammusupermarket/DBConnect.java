package com.mamatha.mammusupermarket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {
	
	private String DB_NAME = "mammusupermarket.db";
	private String  Url = "jdbc:sqlite:C:\\Users\\91779\\OneDrive\\Desktop\\NewSuperMarket\\mammusupermarket.db";
	
	private Connection con;
	private String Select_query = "SELECT ITEAM_NAME,PRICE,QUANTITY,CATEGORY From STOCK";
	private String updateStock_query = "UPDATE STOCK SET PRICE = ?, QUANTITY = ?, CATEGORY = ? WHERE ITEAM_NAME = ? and brand = ? ";
	
	//private String getStockDetailsByName_query= "select price,quantity,category from stock where ITEAM_NAME in(?, ?,?)"; // try to write in clause
	private String insertStockItem_query = "insert into stock(iteam_name,price,quantity,category) values(?,?,?,?)"; // if we dont mention column names it takes 
	private String getStockDetailsByCategoryName_query = " SELECT ITEAM_NAME,PRICE,QUANTITY,CATEGORY ,Brand From STOCK WHERE CATEGORY =?";
	//private String getProductDetailsByNameAndBrand_query= "select iteam_name,price,quantity,category from stock where ITEAM_NAME  =? and Brand = ?)";
	
	private String updateStock_Quantity_qyeryByIteamNameAndBrand = "UPDATE STOCK   Set QUANTITY = ? WHERE ITEAM_NAME = ? and brand = ? ";
	private Statement selectStatement;
	private PreparedStatement  queryStockInfoStmt;
	private PreparedStatement insertStockInfoStmt;
	private PreparedStatement  updateStockInfoStmt;

	private PreparedStatement getStockDetailsByCategory_Stmt;
	private PreparedStatement updateStockQunatityByNameAndBrand_Stmt;
	
	private PreparedStatement getItemDetailsBYNameAndBrand_Stmt;
	

	public  DBConnect() throws SQLException {
		
     con = DriverManager.getConnection(Url);
     selectStatement = con.createStatement();
     //queryStockInfoStmt =  con.prepareStatement(getStockDetailsByName_query);
     insertStockInfoStmt = con.prepareStatement( insertStockItem_query);
     updateStockInfoStmt = con.prepareStatement(updateStock_query);
     getStockDetailsByCategory_Stmt = con.prepareStatement(getStockDetailsByCategoryName_query);
   //  getItemDetailsBYNameAndBrand_Stmt = con.prepareStatement(getProductDetailsByNameAndBrand_query);
     updateStockQunatityByNameAndBrand_Stmt = con.prepareStatement(updateStock_Quantity_qyeryByIteamNameAndBrand);
     
   boolean auto =  con.getAutoCommit();
   System.out.println("auto commit :"+auto);

	}
	public List<StockItem>  getStock()
	{List<StockItem> itemsInStock = new ArrayList<>();
		try {
		
			ResultSet rs = selectStatement.executeQuery(Select_query)	;
			
			while(rs.next())
			{
				StockItem temp = new StockItem();
				temp.setItemName(rs.getString("ITEAM_NAME"));
				temp.setPrice(rs.getInt("PRICE"));
				temp.setQuantity(rs.getInt("QUANTITY"));
				temp.setCategory(rs.getString("CATEGORY"));
				itemsInStock.add(temp);
				
			//	System.out.println(rs.getString("ITEAM_NAME")+"\t"+rs.getInt("PRICE")+"\t"+rs.getInt("QUANTITY")+"\t"+rs.getString("CATEGORY"));
			}
			
			 rs.close();
			 
//			 selectStatement.close();
//			  con.close();
			 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 return itemsInStock;
	}
	
	
	
	
	
	

	
		
	
	public boolean insertBulkStockItem(List<StockItem> lsItem)
	{
		
		try {
			for(StockItem Item : lsItem)
			{
			insertStockInfoStmt.setString(1,Item.getItemName() );
			insertStockInfoStmt.setInt(2, Item.getPrice());
			insertStockInfoStmt.setInt(3, Item.getQuantity());
			insertStockInfoStmt.setString(4, Item.getCategory());
			insertStockInfoStmt.addBatch();
		 
		}
			int[] result = insertStockInfoStmt.executeBatch();
			 System.out.println("The number of rows inserted: "+ result.length);
			 return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	
	}
	public boolean updateBulkStock(List<StockItem> itemLs) {
		try {
			for(StockItem temp : itemLs) {
		
				updateStockInfoStmt.setInt(1, temp.getPrice());
				updateStockInfoStmt.setInt(2, temp.getQuantity());
				updateStockInfoStmt.setString(3, temp.getCategory());
				updateStockInfoStmt.setString(4, temp.getItemName());
				updateStockInfoStmt.setString(5, temp.getBrand());
				updateStockInfoStmt.addBatch();
		}
			int[] result = updateStockInfoStmt.executeBatch();
			System.out.println("The num of rows updated "+result.length);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean updateStockQuantityBYItemNameAndBrand(List<StockItem> itemLs)
	{
		try {
			
		
		for(StockItem temp : itemLs) {
			
		
			updateStockQunatityByNameAndBrand_Stmt.setInt(1, temp.getQuantity());
			updateStockQunatityByNameAndBrand_Stmt.setString(2, temp.getItemName());
			updateStockQunatityByNameAndBrand_Stmt.setString(3, temp.getBrand());
			
			
			updateStockQunatityByNameAndBrand_Stmt.addBatch();
	}
		int[] result = updateStockQunatityByNameAndBrand_Stmt.executeBatch();
		System.out.println("The num of rows updated "+result.length);
		return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
	
	}
	
	
	
	public List<StockItem> getStockItemDetailByNameAndBrand(List<ToBillItems> nameAndBrand) {
		int length = nameAndBrand.size();
		
		
		//sb.append("");

		StringBuilder querySb = new StringBuilder();
		
		querySb.append("select iteam_name,price,quantity,category,brand from stock  where ");
		
	//	StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= length; i++) {
			querySb.append("(ITEAM_NAME =? and BRAND = ?) or");

		}
		String query = querySb.substring(0, querySb.length() - 2);

		//querySb.append(str);

		System.out.println("printing query" + query);
	//	String query = querySb.toString();
		
		
		try {
			getItemDetailsBYNameAndBrand_Stmt = con.prepareStatement(query);
			int j=0;
			for(int i=0;i<nameAndBrand.size();i++) {
			
			
				ToBillItems temp = nameAndBrand.get(i);
				getItemDetailsBYNameAndBrand_Stmt.setString(++j, temp.getIteamName());
				getItemDetailsBYNameAndBrand_Stmt.setString(++j, temp.getBrand());
				
			} 
		
			ResultSet rs= getItemDetailsBYNameAndBrand_Stmt.executeQuery();
			List<StockItem> stockListWithBrand = new ArrayList<>();
		StockItem item = null;
			while(rs.next())
			{
				item= new StockItem();
				item.setItemName(rs.getString("Iteam_Name"));
				item.setPrice(rs.getInt("Price"));
				item.setQuantity(rs.getInt("Quantity"));
				item.setCategory(rs.getString("category"));
				item.setBrand(rs.getString("Brand"));
				stockListWithBrand.add(item);
				
			}
         rs.close();
         return stockListWithBrand;
         
		} catch(SQLException e)
		{
			System.out.println("failed to fetch data with brand name");
			e.printStackTrace();
			return null;
			
		}
	}

	
		public List<StockItem> getStockDetailsByCategory(String categoryName) throws SQLException{
			
			getStockDetailsByCategory_Stmt.setString(1, categoryName);
			ResultSet rs = getStockDetailsByCategory_Stmt.executeQuery();
			List<StockItem> categoryProducts = new ArrayList();
			StockItem itemDetails = null;
			while(rs.next())
			{
				itemDetails = new StockItem();
				itemDetails.setItemName(rs.getString("ITEAM_NAME"));
				itemDetails.setPrice(rs.getInt("PRICE"));
				itemDetails.setQuantity(rs.getInt("QUANTITY"));
				itemDetails.setCategory(rs.getString("CATEGORY"));
				itemDetails.setBrand(rs.getString("BRAND"));
			 categoryProducts.add(itemDetails);
			}
			return categoryProducts;
		}
		
		
		
			
		
		
	
		
	
 public void close()
{
	
	 try {
		 if(selectStatement!=null) {
	 
		 selectStatement.close();
	 }
		 if(queryStockInfoStmt!=null)
			 queryStockInfoStmt.close();
		 
		 if(insertStockInfoStmt!=null)
			 insertStockInfoStmt.close();
		 
		 if(updateStockInfoStmt!=null)
			 updateStockInfoStmt.close();
		 
		 if(getStockDetailsByCategory_Stmt!=null)
			 getStockDetailsByCategory_Stmt.close();
		 
		 if(getItemDetailsBYNameAndBrand_Stmt!=null)
			 getItemDetailsBYNameAndBrand_Stmt.close();
		 
	    if(con!=null)
	    	
     	con.close();
		} catch (SQLException e) {
			System.out.println("failed to close connections");
			e.printStackTrace();
		}
	}
	
}
	
