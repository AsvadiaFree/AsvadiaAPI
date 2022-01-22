package fr.asvadia.api.common.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private String host;
    private String user;
    private String password;
    private String dbName;
    private int port;

    private String url;

    private Connection connection;


    public SQLConnection(String host, int port, String user, String password, String dbName) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
        this.url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbName;
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error connexion to " + dbName);
        }
    }

    public void close() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if(connection != null && ! connection.isClosed()) {
                return connection;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connect();
        return connection;
    }




}
