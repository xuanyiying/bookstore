package com.xuanyiying.bookstore.data.store;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Repository;

import com.xuanyiying.bookstore.data.parse.po.BaseInfo;

@Slf4j
@Repository("writer")
public class DefaultDataWriter implements DataWriter{
    @Override
    public void writeToDatabase(List <?> data) {

    }

    @Override
    public boolean writeToExcel(String fileName, List <?> data) {
    	boolean success = false;
        // turn off auto-flushing and accumulate all rows in memory
        SXSSFWorkbook wb = new SXSSFWorkbook(-1);
        Sheet sh = wb.createSheet();
        try{
            int rowTotal = data.size() + 1;
            Row topRow = sh.createRow(0);
            Field[] properties = data.get(0).getClass().getDeclaredFields();
            int cellTotal = properties.length; 
            // first row, create and set column name
            String name;
            Cell topCell;
    		for (int cellNum = 0; cellNum < cellTotal; cellNum++) {
    			if(!properties[cellNum].getType().equals(BaseInfo.class)) {
    				name = properties[cellNum].getName();
    	            topCell = topRow.createCell(cellNum);
    	            topCell.setCellValue(name);
    			}  
            }
    		 // others row, set data
		    Row dataRow;
		    Field[] fields;
		    Object obj;
            for(int rowNum = 1; rowNum < rowTotal; rowNum++){
            	obj = data.get(rowNum-1);
            	fields = obj.getClass().getDeclaredFields();
                dataRow = sh.createRow(rowNum);
                Cell cell;
                String value;
                for(int cellNum = 0; cellNum < fields.length; cellNum++){
                	if(!properties[cellNum].getType().equals(BaseInfo.class)) {
                		fields[cellNum].setAccessible(true);
                        cell = dataRow.createCell(cellNum);
        				value = (String)fields[cellNum].get(obj);
                        cell.setCellValue(value);
        			}                	
                }      
            }
            ((SXSSFSheet) sh).flushRows();
            success = true;
        } catch (Exception e){
        	log.error("Create workbook met exception, error info:{}",e);
        }
        File file = new File(fileName);
        if(!file.exists()) {
        	file.getParentFile().mkdirs();
		} 
        try ( FileOutputStream out = new FileOutputStream(fileName)){
            wb.write(out);
        } catch (IOException e){
        	log.error("Write data to disk met exception, error info:{}",e);
        }
        // dispose of temporary files backing this workbook on disk
        wb.dispose();     
        try {
			wb.close();
		} catch (IOException e) {
			log.error("Close workbook met exception, error info:{}",e);
		}
        return success;
    }

    @Override
    public boolean writeToCSV(String fileName, List <?> data) {
    	boolean success = this.writeToExcel(fileName, data);
    	if (success) {
    		ToCSV converter = new ToCSV();
        	File file = new File(fileName);
        	try {
    			converter.convertExcelToCSV(fileName, file.getParent());
    			success = true;
    		} catch (IllegalArgumentException | IOException e) {
    			log.error("Convert excel to CSV met exception, error info: {}",e);
    			success = false;
    		}
		}
		return success;  	
    }
}
