package org.example.jdbc.services;

import org.example.jdbc.util.ComboPooledDS;
import org.example.jdbc.util.ReadingAFunctionFromDatabase;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeletingAllFunctionsTest {

    private List<String> functionList;

    ReadingAFunctionFromDatabase functionsFromDatabase = new ReadingAFunctionFromDatabase();

    @Before
    public void getAllFunctions() {
        functionList = functionsFromDatabase.getFunctionList();
    }

    @Test
    public void deletingAllProcedures() throws Exception {
        int actualDeletedCount = 0;
        try (Connection conn = ComboPooledDS.getDatasource().getConnection()) {
            for (String s : functionList) {
                String dropFunction = String.format("drop function %s", s);
                try (PreparedStatement pr = conn.prepareStatement(dropFunction)) {
                    actualDeletedCount += pr.executeUpdate();
                }
            }
        }

        assertEquals(functionsFromDatabase.getFunctionList().size(), actualDeletedCount);
    }
}
