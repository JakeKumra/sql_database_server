package edu.uob;

import java.io.File;
import java.io.IOException;

import static edu.uob.SQLKeywords.isKeyword;

public class DeleteCMD extends DBcmd {
    private String tableName;
    private Condition condition;
    public DeleteCMD() {
        super();
        this.tableName = null;
        this.condition = null;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    @Override
    public String query(DBServer s) {
        if (parseError) {
            return errorMessage;
        } else if (isKeyword(tableName)) {
            return "[ERROR] reserved SQL keyword used";
        }
        try {
            if (s.getCurrentDatabase() == null) {
                return "[ERROR] no database has been selected";
            } else if (!s.getTableNames().contains(this.tableName)) {
                return "[ERROR] Table " + this.tableName + " does not exist in the database";
            }
            Table tableFromFile = new FileManager().parseFileToTable(tableName, s.getCurrDbName());
            // Delete rows that match the condition (if present)
            int rowsDeleted = 0;
            rowsDeleted = tableFromFile.deleteRows(this.condition);

            FileManager FM2 = new FileManager();
            String path = FM2.getDbPath() + File.separator + s.getCurrDbName() + File.separator + tableName;
            FM2.parseTableToFile(tableFromFile, path);
            // Format the result string
            String result = "[OK] " + rowsDeleted + " rows deleted";
            return result.trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "[ERROR] Failed to retrieve data from database";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "[ERROR] has occurred";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "[ERROR] has occurred";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "[ERROR] has occurred";
        }
    }
}
