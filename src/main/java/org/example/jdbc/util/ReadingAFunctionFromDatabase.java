package org.example.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadingAFunctionFromDatabase {

    private final String query = "SELECT format('%I(%s)', p.proname, oidvectortypes(p.proargtypes)) as function\n" +
            "FROM pg_proc p INNER JOIN pg_namespace ns ON (p.pronamespace = ns.oid)\n" +
            "WHERE ns.nspname = 'public';";

    //CREATE EXTENSION pgcrypto;  creates many functions. My functions Beginning in 37 position.
    private final int beginningOfMyFunction = 36;

    public List<String> getFunctionList() {
        List<String> functionList = new ArrayList<>();
        try (Connection con = ComboPooledDS.getDatasource().getConnection();
             PreparedStatement pr = con.prepareStatement(query);
             ResultSet rs = pr.executeQuery()) {

            while (rs.next()) {
                if (rs.getRow() > beginningOfMyFunction) {
                    functionList.add(rs.getString("function"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return functionList;
    }
}
