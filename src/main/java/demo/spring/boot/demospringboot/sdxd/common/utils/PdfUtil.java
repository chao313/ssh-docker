package demo.spring.boot.demospringboot.sdxd.common.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * ***************************************************************************** <p> 功能名
 * ：demo.spring.boot.demospringboot.sdxd.common.api.monitor.utils 系统名           ：PDF文件操作 <p> *****************************************************************************
 * Modification History <p> Date        Name                    Reason for Change ----------
 * ----------------------  ----------------------------------------- 2017/11/10   wenzhou.xu
 * Created
 */
public class PdfUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(PdfUtil.class);

    private PDF pdf;

    public static PdfUtil getInstance() {
        return new PdfUtil();
    }

    /**
     * pdf文件初始化
     *
     * @param file 创建的pdf文件
     */
    public PdfUtil init(File file) {
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            pdf = new PDF();
            pdf.setDocument(new Document(new Rectangle(PageSize.A4), 10, 10, 10, 10));
            pdf.setChineseFont(bfChinese);
            pdf.setTitleFont(new Font(bfChinese, 28, Font.NORMAL));
            pdf.setBodyFont(new Font(bfChinese, 12, Font.NORMAL));
            pdf.setFos(new FileOutputStream(file));
            PdfWriter.getInstance(pdf.getDocument(), pdf.getFos());
        } catch (Exception e) {
            LOGGER.error("创建PDF init 异常!", e);
        }
        pdf.getDocument().open();
        return this;
    }

    /**
     * 设置pdf文件的标题
     */
    public PdfUtil title(String title) {
        // 设置标题
        Paragraph pdfTitle = new Paragraph(title, pdf.getTitleFont());
        pdfTitle.setAlignment(Paragraph.TITLE);
        try {
            pdf.getDocument().add(pdfTitle);
        } catch (Exception e) {
            LOGGER.error("创建PDF title 异常!", e);
        }
        return this;
    }

    /**
     * 设置pdf文件内容
     */
    public PdfUtil body(String body) {
        // 设置标题
        Paragraph pdfBody = new Paragraph(body, pdf.getBodyFont());
        pdfBody.setAlignment(Paragraph.ALIGN_MIDDLE);
        try {
            pdf.getDocument().add(pdfBody);
        } catch (Exception e) {
            LOGGER.error("创建PDF body 异常!", e);
        }
        return this;
    }

    /**
     * 初始化table格式
     */
    public PdfUtil table(int column) {
        PdfPTable table = new PdfPTable(column);
        table.setWidthPercentage(100f);
        table.setTotalWidth(560f);
        float[] floats = new float[column];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = table.getTotalWidth() / column;
        }
        try {
            table.setTotalWidth(floats);
        } catch (Exception e) {
            LOGGER.error("创建PDF table 异常!", e);
        }
        table.setLockedWidth(true);//固定宽度
        pdf.setTable(table);
        return this;
    }

    /**
     * 插入行 row.size = column
     */
    public PdfUtil row(List<Text> row) {
        if (row.size() != pdf.getTable().getNumberOfColumns()) {
            LOGGER.error("创建PDF table 异常! row.size : {}, column : {}", row.size(), pdf.getTable().getNumberOfColumns());
            return this;
        }
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setMinimumHeight(25);
        for (Text cell : row) {
            Font font = new Font(pdf.getChineseFont(), 8, Font.NORMAL);
            font.setColor(cell.getColor());
            font.setSize(cell.getSize() == null ? font.getSize() : cell.getSize());
            pdfPCell.getColumn().setText(new Paragraph(cell.getContent(), font));

            pdf.getTable().addCell(pdfPCell);
        }
        return this;
    }

    /**
     * 向document中添加复杂元素：table
     */
    public PdfUtil add() {
        try {
            if (pdf.getTable() != null)
                pdf.getDocument().add(pdf.getTable());
        } catch (Exception e) {
            LOGGER.error("创建PDF add 异常!", e);
        }
        return this;
    }

    /**
     * 关闭pdf文件
     */
    public void close() {
        // 关闭文档
        pdf.getDocument().close();
        try {
            pdf.getFos().close();
        } catch (Exception e) {
            LOGGER.error("创建PDF close 异常!", e);
        }
    }

    private class PDF {
        private Document document;

        private FileOutputStream fos;

        private BaseFont chineseFont;

        private Font titleFont;

        private Font bodyFont;

        private PdfPTable table;

        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
        }

        public FileOutputStream getFos() {
            return fos;
        }

        public void setFos(FileOutputStream fos) {
            this.fos = fos;
        }

        public BaseFont getChineseFont() {
            return chineseFont;
        }

        public void setChineseFont(BaseFont chineseFont) {
            this.chineseFont = chineseFont;
        }

        public Font getTitleFont() {
            return titleFont;
        }

        public void setTitleFont(Font titleFont) {
            this.titleFont = titleFont;
        }

        public Font getBodyFont() {
            return bodyFont;
        }

        public void setBodyFont(Font bodyFont) {
            this.bodyFont = bodyFont;
        }

        public PdfPTable getTable() {
            return table;
        }

        public void setTable(PdfPTable table) {
            this.table = table;
        }
    }

    public static class Text {
        private String content;
        private BaseColor color;
        private Integer size;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public BaseColor getColor() {
            return color;
        }

        public void setColor(BaseColor color) {
            this.color = color;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }
    }
}
