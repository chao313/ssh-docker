package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.spreadsheet.Spreadsheet;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ProcessBizException;

import info.monitorenter.cpdetector.io.*;

import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo.ErrorCode.SystemError.SERVER_INTERNAL_ERROR;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.util
 * 系统名           ：文件处理工具类
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/8/8    wenzhou.xu              Created
 */
public class CommonFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFileUtil.class);

    /* 上传类型为文件 */
    private static final String FILE = "file";

    /* 上传类型为附件*/
    private static final String ATTACHFILE = "attachFile";

    /**
     * 获取文件流，并生成excel实例
     *
     * @param request
     * @return
     * @throws ProcessBizException
     */
    public static Spreadsheet getExcel(HttpServletRequest request) throws ProcessBizException {
        MultipartFile file = getMultipartFile(request, FILE);
        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            return new Spreadsheet(is, Spreadsheet.Type.forType(prefix));
        } catch (Exception e) {
            throw new ProcessBizException(SERVER_INTERNAL_ERROR);
        }
    }

    public static MultipartFile getMultipartFile(HttpServletRequest request, String type) {
        return ((MultipartHttpServletRequest) request).getFile(type);
    }

    public static <T> List<T> readCsv(HttpServletRequest request, Class t) throws ProcessBizException {
        List<T> list = new ArrayList<>();
        MultipartFile file = getMultipartFile(request, FILE);
        try {
            //BOM头输入流，默认去掉Bom头
            InputStream bomInput = new BOMInputStream(file.getInputStream());
            //用于解析的输入流
            InputStream is = new BOMInputStream(file.getInputStream());
            InputStreamReader isr = new InputStreamReader(is, getEncode(bomInput));
            ICsvBeanReader reader = new CsvBeanReader(isr, CsvPreference.EXCEL_PREFERENCE);

            //获取头部信息
            String[] headers = reader.getHeader(true);

            //下划线转驼峰
            List<String> headerList = Arrays.stream(headers).map(HttpUtil::toCamelCase).collect(Collectors.toList());
            headers = headerList.toArray(new String[headerList.size()]);

            //获取数据部分
            while (true) {
                T temp = (T) reader.read(t, headers);
                if (temp == null)
                    break;
                list.add(temp);
            }
            return list;
        } catch (IOException e) {
            throw new ProcessBizException(SERVER_INTERNAL_ERROR);
        }
    }

    /**
     * 读取文件的编码格式
     *
     * @param path
     * @return
     */
    public static String getEncode(String path) {
        try {
            return getEncode(new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            LOGGER.error("file not found :", e);
            return null;
        }
    }

    /**
     * 读取输入流的编码格式
     *
     * @param is
     * @return
     */
    public static String getEncode(InputStream is) {
        CodepageDetectorProxy detector = getCodepageDetectorProxy();
        try {
            return detector.detectCodepage(new BufferedInputStream(is), Integer.MAX_VALUE).name();
        } catch (Exception ex) {
            LOGGER.error("getEncode fail :", ex);
            return "UTF-8";
        }
    }

    private static CodepageDetectorProxy getCodepageDetectorProxy() {
    /*
     * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
     * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
     * JChardetFacade、ASCIIDetector、UnicodeDetector。
     * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
     * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
     * cpDetector是基于统计学原理的，不保证完全正确。
     */
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        /*
         * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
         * 指示是否显示探测过程的详细信息，为false不显示。
         */
        detector.add(new ParsingDetector(false));
        /*
         * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
         * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
         * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
         */
        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());
        return detector;
    }
}
