package rx.whh.aoo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by whh on 2015/9/12.
 * email:wuhuihao@qq.com
 */
public class PdfConvertToSwfUtil {
    public static Logger logger = LoggerFactory.getLogger(PdfConvertToSwfUtil.class);
    public static final String winSwftoolspath = "D:/SWFTools/pdf2swf.exe";
    public static final String linuxSwftoolspath = "/etc/WFTools/pdf2swf";

    /**
     * 将文件转换为SWF
     *
     * @param inputFilePath
     * @param outputFilePath
     * @return
     */
    public static boolean pdf2swf(String inputFilePath, String outputFilePath) {
        boolean flag = false;

        File pdfFile = new File(inputFilePath);
        File swfFile = new File(outputFilePath);
        if (!swfFile.exists()) {
            if (pdfFile.exists()) {
                String toolPath = ToolUtil.getEnvironment().equals("win") ? winSwftoolspath : linuxSwftoolspath;
                try {
                    execProcess(toolPath, pdfFile.getPath(), swfFile.getPath());
                    flag = true;
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            } else {
                logger.error("pdf不存在,无法转换");
            }
        } else {
            logger.error("swf已经存在不需要转换");
        }
        return flag;
    }

    /**
     * 转换功能实现
     *
     * @param toolpath
     * @param inputFilePath
     * @param outPutFilePath
     * @throws IOException
     */
    public static void execProcess(String toolpath, String inputFilePath, String outPutFilePath) throws IOException {
        Runtime r = Runtime.getRuntime();
        Process process = r.exec(toolpath + " " + inputFilePath + " -o " + outPutFilePath + " -T 9");
        logger.info(loadStream(process.getInputStream()));
        logger.error(loadStream(process.getErrorStream()));
        logger.info("swf转换成功，文件输出：" + outPutFilePath);
    }


    /**
     * 获取处理信息
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String loadStream(InputStream in) throws IOException {
        boolean ptr = false;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        int c;
        while ((c = bufferedInputStream.read()) != -1) {
            buffer.append((char) c);
        }
        return buffer.toString();
    }
}
