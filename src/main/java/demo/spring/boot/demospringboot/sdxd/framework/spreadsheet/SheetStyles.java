package demo.spring.boot.demospringboot.sdxd.framework.spreadsheet;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

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
 * 2017/10/25     melvin                 Created
 */
public class SheetStyles {

    public static CellStyle newDateStyle(Spreadsheet spreadsheet, String format) {
        short dateFormat = spreadsheet.newDataFormat(format);
        return spreadsheet.newStyle(cellStyle -> {
            cellStyle.setDataFormat(dateFormat);
            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setFont(spreadsheet.getDefaultSheetStyles().bodyFont);
        });
    }

    private Spreadsheet spreadsheet;

    private Font bodyFont;
    private short dateFormat;

    private CellStyle headStyle;
    private CellStyle bodyStyle;
    private CellStyle dateStyle;

    public SheetStyles(Spreadsheet spreadsheet) {
        this.spreadsheet = spreadsheet;

        this.bodyFont = spreadsheet.newFont(font -> {
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 10);
        });
        this.dateFormat = spreadsheet.newDataFormat("yyyy-mm-dd hh:mm:ss");
        this.headStyle = spreadsheet.newStyle(cellStyle -> {
            XSSFColor foregroundColor = spreadsheet.newColor(242, 242, 242);
            ((XSSFCellStyle) cellStyle).setFillForegroundColor(foregroundColor);
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cellStyle.setFont(bodyFont);
            cellStyle.setWrapText(true);
        });
        this.bodyStyle = spreadsheet.newStyle(cellStyle -> {
            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setFont(bodyFont);
        });
        this.dateStyle = spreadsheet.newStyle(cellStyle -> {
            cellStyle.setDataFormat(dateFormat);
            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setFont(bodyFont);
        });
    }

    public Font getBodyFont() {
        return bodyFont;
    }

    public short getDateFormat() {
        return dateFormat;
    }

    public CellStyle getHeadStyle() {
        return headStyle;
    }

    public CellStyle getBodyStyle() {
        return bodyStyle;
    }

    public CellStyle getDateStyle() {
        return dateStyle;
    }
}
