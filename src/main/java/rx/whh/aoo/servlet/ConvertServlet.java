package rx.whh.aoo.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.whh.aoo.utils.OfficeConvertToPDFUtil;
import rx.whh.aoo.utils.PdfConvertToSwfUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by whh on 2015/9/12.
 * email:wuhuihao@qq.com
 */
public class ConvertServlet extends HttpServlet {

    Logger logger = LoggerFactory.getLogger(getClass());
    private ServletContext sc;

    public ConvertServlet() {
    }

    public void init(ServletConfig config) {
        this.sc = config.getServletContext();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List list = upload.parseRequest(request);
            Iterator itr = list.iterator();

            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.getName() != null && !item.getName().equals("")) {
                    File tempFile = new File(item.getName());
                    String fileName = tempFile.getName();
                    String simpleFileName = fileName.split("\\.")[0];
                    String savePath = this.sc.getRealPath("/") + "upload/";
                    String pdfOutputFile = savePath + simpleFileName + ".pdf";
                    String swfOutputFile = savePath + simpleFileName + ".swf";
                    File savePathFile = new File(savePath);
                    if (!savePathFile.exists()) {
                        savePathFile.mkdir();
                    }
                    File saveFile = new File(savePath + tempFile.getName());
                    item.write(saveFile);
                    OfficeConvertToPDFUtil.convertToPdf(savePath + tempFile.getName(), pdfOutputFile);
                    PdfConvertToSwfUtil.pdf2swf(pdfOutputFile, swfOutputFile);
                    request.setAttribute("message", "上传文件成功！");
                } else {
                    request.setAttribute("message", "没有选择上传文件！");
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            request.setAttribute("message", "上传文件失败！");
        }

        request.getRequestDispatcher("uploadResult.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
