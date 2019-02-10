package sample.core.helpers;

import java.util.List;

/*
 *   Allows to keep information in one object for import/export purposes using Json.
 */

public class GsonHelper {
    private int recordID;
    private String recordName;
    private List<?> records;

    public void setRecords(List<?> records) {
        this.records = records;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }
}
