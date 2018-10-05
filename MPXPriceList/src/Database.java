import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
	private Connection con;
	public Database() {
		// TODO Auto-generated method stub
				try {
					try {
						System.out.println("Connecting to MPX PriceList DB");
						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String url = "jdbc:sqlserver://localhost:1433;databaseName=MPXPriceList";
					con = DriverManager.getConnection(url, "testdummy", "testdummy");
					System.out.println("Login Successful");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	private void UpdatePricing(int contactchannelid) {
		String updatequery = "UPDATE PPO SET offerenddate = GETDATE() FROM [dbo].t_PhonePriceOverride PPO	WHERE ContactChannelID in (?)	and OfferEndDate >= GETDATE() and ([Comments] NOT like '%AUTO CREATED%' or comments is null)";
		try {
			PreparedStatement preparedStmt = con.prepareStatement(updatequery);
			preparedStmt.setInt(1, contactchannelid);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void UpdateHoldback(int contactchannelid) {
		String updatequery = "UPDATE t_ModelHoldback set ValidTo = GETDATE() where ContactChannelID in (?) and ValidTo >= GETDATE() ";
		try {
			PreparedStatement preparedStmt = con.prepareStatement(updatequery);
			preparedStmt.setInt(1, contactchannelid);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void UpdatePricing(int contactchannelid,String enddate) {
		String updatequery = "UPDATE PPO SET offerenddate = GETDATE() FROM [dbo].t_PhonePriceOverride PPO	WHERE ContactChannelID in (?)	and OfferEndDate >= '?' and ([Comments] NOT like '%AUTO CREATED%' or comments is null)";
		try {
			PreparedStatement preparedStmt = con.prepareStatement(updatequery);
			preparedStmt.setString(1, enddate);
			preparedStmt.setInt(2, contactchannelid);
			preparedStmt.setString(3, enddate);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void UpdateHoldback(int contactchannelid, String enddate) {
		String updatequery = "UPDATE t_ModelHoldback set ValidTo = ? where ContactChannelID in (?) and ValidTo >= ? ";
		try {
			PreparedStatement preparedStmt = con.prepareStatement(updatequery);
			preparedStmt.setString(1, enddate);
			preparedStmt.setInt(2, contactchannelid);
			preparedStmt.setString(3, enddate);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void CurrentPricingInsertQuery(int contactchannelid,int phonemodelid,String offerenddate, int price) {
		//USE BBTISweden (use in the connection string)
		
	String insertquery = "INSERT INTO [dbo].[t_PhonePriceOverride] ([ContactChannelID],[PhoneModelID],[OverridePriceFinal],[OfferStartDate],[OfferEndDate],[Comments],[GradeID]) VALUES(?,?,ROUND(?,0),GETDATE(),?,'MANUALLY CREATED','C')";  
		try {
			PreparedStatement preparedStmt = con.prepareStatement(insertquery);
			preparedStmt.setInt(1, contactchannelid);
			preparedStmt.setInt(2, phonemodelid);
			preparedStmt.setInt(3, price);
			preparedStmt.setString(4, offerenddate);
			preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void CurrentHoldbackInsertQuery(int contactchannelid,int phonemodelid, String offerenddate, int holdbackprice) {
		//USE BBTISweden (use in the connection string)
		
	String insertquery = "INSERT INTO [dbo].[t_ModelHoldback] ([ContactChannelID],[ModelID],[ModelHoldback],[ValidFrom],[ValidTo]) VALUES(?,?,ROUND(cast(? as money),2),GETDATE(),?)";   
	try {
		PreparedStatement preparedStmt = con.prepareStatement(insertquery);
		preparedStmt.setInt(1, contactchannelid);
		preparedStmt.setInt(2, phonemodelid);
		preparedStmt.setInt(3, holdbackprice);
		preparedStmt.setString(4, offerenddate);
		preparedStmt.execute();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public void FuturePricingInsertQuery(int contactchannelid,int phonemodelid,String offerstartdate, String offerenddate, int price) {
		//USE BBTISweden (use in the connection string)

		String insertquery = "INSERT INTO [dbo].[t_PhonePriceOverride] ([ContactChannelID],[PhoneModelID],[OverridePriceFinal],[OfferStartDate],[OfferEndDate],[Comments],[GradeID]) VALUES(?,?,ROUND(?,0),?,?,'MANUALLY CREATED','C')";  
		try {
			PreparedStatement preparedStmt = con.prepareStatement(insertquery);
			preparedStmt.setInt(1, contactchannelid);
			preparedStmt.setInt(2, phonemodelid);
			preparedStmt.setInt(3, price);
			preparedStmt.setString(4, offerstartdate);
			preparedStmt.setString(5, offerenddate);
			preparedStmt.execute();
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	public void FutureHoldbackInsertQuery(int contactchannelid,int phonemodelid,String offerstartdate, String offerenddate, int holdbackprice) {
		//USE BBTISweden (use in the connection string)
		
		String insertquery = "INSERT INTO [dbo].[t_ModelHoldback] ([ContactChannelID],[ModelID],[ModelHoldback],[ValidFrom],[ValidTo]) VALUES(?,?,ROUND(cast(? as money),2),?,?)";   
		try {
			PreparedStatement preparedStmt = con.prepareStatement(insertquery);
			preparedStmt.setInt(1, contactchannelid);
			preparedStmt.setInt(2, phonemodelid);
			preparedStmt.setInt(3, holdbackprice);
			preparedStmt.setString(4, offerstartdate);
			preparedStmt.setString(5, offerenddate);
			preparedStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
