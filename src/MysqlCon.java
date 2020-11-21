import java.sql.*;

class MysqlCon {
    public static void main(String args[]) {

        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", "localhost", 3306, "servidor");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, "root", "diciembre17");
            // here servidor is database name, root is username and password
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from lecturas");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}