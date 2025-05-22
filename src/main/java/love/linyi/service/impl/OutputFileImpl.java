package love.linyi.service.impl;
import love.linyi.service.OutputFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
@Service
public class OutputFileImpl implements OutputFile {
    private static final String UPLOAD_DIRECTORY = "siwei";
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 2; // 2MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 50; // 50MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    private static String filename;
    public String getFilename() {
        return filename;
    }
    @Override
    public boolean outputFile(HttpServletRequest request, HttpServletResponse response) {
        boolean flag=true;        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 构建上传路径
        String uploadPath = request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求，获取文件项列表
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 处理每一个文件项
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        filename=fileName;
                        if (fileName != null && !fileName.isEmpty()) {
                            String filePath = uploadPath + File.separator + fileName;
                            File storeFile = new File(filePath);
                            item.write(storeFile);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return flag;
    }
}
