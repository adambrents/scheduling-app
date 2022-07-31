package main;

import database.JDBC;

public class Main {
    public static void main(String[] args) {
        JDBC.openConnection();
        JDBC.closeConnection();
    }
}