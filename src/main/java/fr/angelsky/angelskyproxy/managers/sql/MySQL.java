package fr.angelsky.angelskyproxy.managers.sql;

import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQL {

    private final String user,password,url;

    public MySQL(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //UPDATE /QUERY
    public void update(String qry) {
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry)) {
            s.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }


    public Object query(String qry, Function<ResultSet, Object> consumer) {
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry);
             ResultSet rs = s.executeQuery()) {

            return consumer.apply(rs);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void query(String qry, Consumer<ResultSet> consumer){
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry);
             ResultSet rs = s.executeQuery()){
            consumer.accept(rs);
        }catch (SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }


}
