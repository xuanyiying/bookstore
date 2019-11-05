package com.xuanyiying.bookstore.data.store;

import java.util.List;

public interface DataWriter {
    void writeToDatabase(List<?> data);

    boolean writeToExcel(String fileName, List<?> data);

    boolean writeToCSV(String fileName, List<?> data);
}
