package rx.whh.aoo.utils;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

/**
 * Created by whh on 2015/9/12.
 * email:wuhuihao@qq.com
 */
public class OfficeConvertToPDFUtil {
    public static final String OFFICE_DOC = "doc";
    public static final String OFFICE_DOCX = "docx";
    public static final String OFFICE_XLS = "xls";
    public static final String OFFICE_XLSX = "xlsx";
    public static final String OFFICE_PPT = "ppt";
    public static final String OFFICE_PPTX = "pptx";
    public static final String OFFICE_TO_PDF = "pdf";
    public static Logger logger = LoggerFactory.getLogger(OfficeConvertToPDFUtil.class);

    public OfficeConvertToPDFUtil() {

    }

    /**
     * 取得OpenOffice配置
     * @return
     */
    public static OfficeManager getOfficeManager() {
        try {
            DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
            String officeHome = ToolUtil.getOfficeHome();
            config.setOfficeHome(officeHome);
            OfficeManager officeManager = config.buildOfficeManager();
            officeManager.start();
            return officeManager;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 转换功能实现
     * @param inputFile
     * @param outputFilePath_end
     * @param inputFilePath
     * @param converter
     * @throws OfficeException
     */
    public static void converterFile(File inputFile, String outputFilePath_end, String inputFilePath, OfficeDocumentConverter converter) throws OfficeException {
        File outputFile = new File(outputFilePath_end);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        converter.convert(inputFile, outputFile);
        logger.info("文件：" + inputFilePath + "\n转换为\n目标文件：" + outputFile + "\n成功！");
    }

    /**
     * 转换为pdf文件
     * @param inputFilePath
     * @param outputFilePath
     * @return
     * @throws OfficeException
     */
    public static boolean convertToPdf(String inputFilePath, String outputFilePath) throws OfficeException {
        boolean flag = false;
        //启动openoffice
        OfficeManager officeManager = getOfficeManager();
        if (officeManager != null) {
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            long begin_time = (new Date()).getTime();
            if (null != inputFilePath) {
                File inputFile = new File(inputFilePath);
                if (null == outputFilePath) {
                    String outputFilePath_end = getOutputFilePath(inputFilePath);
                    if (inputFile.exists()) {
                        converterFile(inputFile, outputFilePath_end, inputFilePath, converter);
                        flag = true;
                    }
                } else if (inputFile.exists()) {
                    converterFile(inputFile, outputFilePath, inputFilePath, converter);
                    flag = true;
                }
                //关闭openoffice
                officeManager.stop();
            } else {
                logger.info("源文件不存在");
            }

            long end_time = (new Date()).getTime();
            logger.info("文件转换耗时：[" + (end_time - begin_time) + "]ms");
        } else {
            logger.info("OpenOffice 连接失败");
        }

        return flag;
    }

    /**
     * 添加pdf文件后缀
     * @param inputFilePath
     * @return
     */
    public static String getOutputFilePath(String inputFilePath) {
        return inputFilePath.replaceAll("." + getPostfix(inputFilePath), ".pdf");
    }


    /**
     * 取得文件后缀名称
     * @param inputFilePath 文件名称
     * @return
     */
    public static String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }
}
