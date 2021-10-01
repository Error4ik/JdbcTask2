package org.example.jdbc.services;

import org.example.jdbc.util.ComboPooledDS;
import org.example.jdbc.util.ReadingAFunctionFromDatabase;
import org.example.jdbc.util.ReadingFunctionFromFile;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateFunctions {

    private final ReadingFunctionFromFile readingFunctionFromFile = new ReadingFunctionFromFile();
    ReadingAFunctionFromDatabase functionsFromDatabase = new ReadingAFunctionFromDatabase();
    private List<String> functionsList;

    @Before
    public void init() {
        functionsList = readingFunctionFromFile.readFunctionFromFile();
    }

    @Test
    public void createFunctions() throws Exception {
        try (Connection conn = ComboPooledDS.getDatasource().getConnection()) {
            for (String s : functionsList) {
                try (PreparedStatement pr = conn.prepareStatement(s)) {
                    pr.executeUpdate();
                }
            }
        }

        assertEquals(functionsList.size(), functionsFromDatabase.getFunctionList().size());
    }
}
