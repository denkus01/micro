package com.clrk.lambda.trydynamodb.parser;

import com.clrk.lambda.trydynamodb.dto.TelephoneRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

@Slf4j
@Service
public class XlsParser {

    public List<TelephoneRecord> parse(InputStream inputStream, String key) throws IOException {

        log.info("Start parsing file {}", key);
        final HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        int countOfSheet = workbook.getNumberOfSheets();

        List<TelephoneRecord> telephoneRecords = new ArrayList<>();
        for (int i = 0; i < countOfSheet; i++) {

            final HSSFSheet sheet = workbook.getSheetAt(i);
            final List<TelephoneRecord> record = processSheet(sheet);
            telephoneRecords.addAll(record);
        }
        log.info("Amount of records {}", telephoneRecords.size());
        return telephoneRecords;
    }

    private List<TelephoneRecord> processSheet(HSSFSheet sheet) {
        final Iterator<Row> rowIterator = sheet.iterator();
        final List<TelephoneRecord> telephoneRecords = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();
            if (currentRow.getRowNum() >= 1) {
                final TelephoneRecord telephoneRecord = processRow(currentRow);
                telephoneRecords.add(telephoneRecord);
            }
        }
        return telephoneRecords;
    }

    private TelephoneRecord processRow(Row currentRow) {
        final TelephoneRecord.TelephoneRecordBuilder telephoneRecordBuilder = TelephoneRecord.builder();
        for (ColumnOrder value : ColumnOrder.values()) {
            final int column = value.getColumn();
            switch (value) {
                case NAME:
                    final String name = getStringCellValue(currentRow, column);
                    telephoneRecordBuilder.name(name);
                    break;
                case SURNAME:
                    final String surName = getStringCellValue(currentRow, column);
                    telephoneRecordBuilder.surname(surName);
                    break;
                case TELEPHONE:
                    final String telephoneNumber = getStringCellValue(currentRow, column);
                    telephoneRecordBuilder.telephone(telephoneNumber).build();
                    break;
                default:
                    throw new ParseCellValueException("Illegal type of cell");
            }
        }
        return telephoneRecordBuilder.build();
    }

    private String getStringCellValue(Row currentRow, int column) {
        final Cell cell = currentRow.getCell(column);
        try {
            switch (cell.getCellType()) {
                case CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case CELL_TYPE_ERROR:
                    return String.valueOf(cell.getErrorCellValue());
                default:
                    throw new ParseCellValueException(String.format("Can't getObjectContent string value from Cell:%s,Row:%s", cell.getColumnIndex(), currentRow.getPhysicalNumberOfCells()));
            }

        } catch (Exception e) {
            throw new ParseCellValueException(String.format("Can't getObjectContent string value from Cell:%s,Row:%s", cell.getColumnIndex(), currentRow.getPhysicalNumberOfCells()), e);
        }
    }

}

