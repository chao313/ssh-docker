package demo.spring.boot.demospringboot.sdxd.framework.spreadsheet;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.util.Assert;

import java.util.List;

import static org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide.*;
import static org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide.BOTTOM;
import static org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide.LEFT;
import static org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide.RIGHT;
import static org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide.TOP;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.spreadsheet
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/11     melvin                 Created
 */
public class SheetBuilder {

    private Spreadsheet spreadsheet;

    private Sheet sheet;

    private int rowIndex;
    private int currentRowIndex;

    private int maxColumnCount;
    private CellStyle errorStyle;

    public SheetBuilder(Spreadsheet spreadsheet, Sheet sheet) {
        Assert.notNull(spreadsheet, "Spreadsheet can not be null");
        Assert.notNull(sheet, "Sheet can not be null");
        this.spreadsheet = spreadsheet;
        this.sheet = sheet;
        this.rowIndex = 0;
        this.currentRowIndex = 0;
        this.maxColumnCount = 0;

        Font errorFont = spreadsheet.newFont(font -> {
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight((short) 5);
        });

        this.errorStyle = spreadsheet.newStyle(cellStyle -> {
            java.awt.Color red = java.awt.Color.RED;
            XSSFColor borderColor = spreadsheet.newColor(red.getRed(), red.getGreen(), red.getBlue());
            ((XSSFCellStyle) cellStyle).setFillForegroundColor(borderColor);
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setBorderBottom(CellStyle.BORDER_DOUBLE);
            cellStyle.setBorderLeft(CellStyle.BORDER_DOUBLE);
            cellStyle.setBorderRight(CellStyle.BORDER_DOUBLE);
            cellStyle.setBorderTop(CellStyle.BORDER_DOUBLE);
            ((XSSFCellStyle) cellStyle).setBorderColor(TOP, borderColor);
            ((XSSFCellStyle) cellStyle).setBorderColor(BOTTOM, borderColor);
            ((XSSFCellStyle) cellStyle).setBorderColor(LEFT, borderColor);
            ((XSSFCellStyle) cellStyle).setBorderColor(RIGHT, borderColor);
            cellStyle.setFont(errorFont);
            cellStyle.setWrapText(true);
        });
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public RowBuilder newRow() {
        return newRow(spreadsheet.getDefaultSheetStyles().getBodyStyle());
    }

    public RowBuilder newRow(CellStyle defaultStyle) {
        Row row = sheet.createRow(this.currentRowIndex);
        this.rowIndex = currentRowIndex;
        this.currentRowIndex ++;
        return new RowBuilder(spreadsheet, this, row, defaultStyle);
    }

    public int merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        int mergedRegionCount = sheet.getNumMergedRegions();
        return mergedRegionCount - 1;
    }

    public void sum(int mergedRegion, String columnPrefix, List<String> sumRows) {
        CellRangeAddress cellRangeAddress = sheet.getMergedRegion(mergedRegion);
        int rowIndex = cellRangeAddress.getFirstRow();
        int cellIndex = cellRangeAddress.getFirstColumn();
        sum(rowIndex, cellIndex, columnPrefix, sumRows);
    }

    public void sum(int rowIndex, int cellIndex, String columnPrefix, List<String> sumRows) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(cellIndex);
        String sumCells = spreadsheet.sumCells(columnPrefix, sumRows);
        spreadsheet.setFormula(cell, "SUM(" + sumCells + ")");
    }

    public void formula(int mergedRegion, String formula) {
        CellRangeAddress cellRangeAddress = sheet.getMergedRegion(mergedRegion);
        int rowIndex = cellRangeAddress.getFirstRow();
        int cellIndex = cellRangeAddress.getFirstColumn();
        formula(rowIndex, cellIndex, formula);
    }

    public void formula(int rowIndex, int cellIndex, String formula) {
        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(cellIndex);
        spreadsheet.setFormula(cell, formula);
    }

    public void freezeRow(int rowIndex) {
        sheet.createFreezePane(0, rowIndex);
    }

    public void autoSizeColumn(int column) {
        sheet.autoSizeColumn(column, true);
    }

    public void addError(String error) {
        Row row = sheet.createRow(this.currentRowIndex);
        this.rowIndex = currentRowIndex;
        this.currentRowIndex ++;
        RowBuilder builder = new RowBuilder(spreadsheet, this, row, errorStyle);
        builder.cell(error);
        for (int i = 1; i <= maxColumnCount - 1; i ++) {
            builder.blank();
        }
//        Observable.range(1, maxColumnCount - 1).forEach(i -> builder.blank());
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, this.maxColumnCount - 2));
    }

    int addCell(int index) {
        return addCell(index, 1);
    }

    int addCell(int index, int delta) {
        int newIndex = index + delta;
        if (newIndex > maxColumnCount) {
            maxColumnCount = newIndex;
        }
        return newIndex;
    }
}

