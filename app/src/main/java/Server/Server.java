package Server;

import com.google.android.gms.common.api.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

public class Server {

    public static void main(String[] args) {

        // Connect to database
        String hostName = "masevo.database.windows.net";
        String dbName = "MASEVO_TEST_01";
        String user = "MASEVO_ADMIN";
        String password = "M4s3v0_4dm1n";
        String url = String.format("jdbc:sqlserver://%s:1433;" +
                "database=%s;" +
                "user=%s;" +
                "password=%s;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;", hostName, dbName, user, password);
        Connection connection;


        try {
            System.out.println("Connecting to SQL Server ... ");
            connection = DriverManager.getConnection(url);
            System.out.println("Done.");

            System.out.println("Query data example:");
            System.out.println("=========================================");

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT TOP 20 pc.Name as CategoryName, p.name as ProductName "
                    + "FROM [SalesLT].[ProductCategory] pc "
                    + "JOIN [SalesLT].[Product] p ON pc.productcategoryid = p.productcategoryid";

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSql);
                System.out.println("Top 20 Categories:");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + " " +
                    resultSet.getString(2));
                }
                connection.close();
            } catch (SQLTimeoutException sqlte) {
                System.out.println("Could not execute the query within the timeout value specified" +
                        " by setQueryTimeout.");
                sqlte.printStackTrace();

            } catch (SQLException sqle) {
                System.out.println("Could not create statement in connection. Either the " +
                        "connection is already closed or access was denied to the database.");
                sqle.printStackTrace();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}