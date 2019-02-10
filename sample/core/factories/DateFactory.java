package sample.core.factories;

import javafx.scene.control.DatePicker;

import java.util.List;

/*
 *   An interface that contains date methods that are implemented in EditInformationController.
 */

public interface DateFactory {
    List<String> getOldDateFormat(String connectionUrl, String tableName);

    List<String> dateParser(List<String> dates);

    String getCorrectDateFormat(DatePicker datePicker);
}
