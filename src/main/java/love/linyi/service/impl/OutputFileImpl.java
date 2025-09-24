package love.linyi.service.impl;
import love.linyi.controller.Code;
import love.linyi.controller.UploadProgressListener;
import love.linyi.service.OutputFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
@Service
public class OutputFileImpl implements OutputFile {
    private static final String UPLOAD_DIRECTORY = "siwei";
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 2; // 2MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 500; // 500MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 500; // 500MB
    @Override
    public String getFilename() {
        return "";
   }
    @Override
    public boolean outputFile(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("people",true);

        boolean flag=true;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 构建上传路径
        String uploadPath = request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        System.out.println("uploadPath: " + uploadPath);
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(1000*60*60*24);
        UploadProgressListener listener = new UploadProgressListener(request);
        asyncContext.addListener(listener);
        System.out.println("asyncContext: " + asyncContext);
        // 注册 ProgressListener
        upload.setProgressListener(listener);
        System.out.println("Progress: " + upload);
        asyncContext.start(() -> {
             String filename="";
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
//                                item.write(storeFile);
                            }
                        }
                    }
                }
                System.out.println("File uploaded successfully: " + request.getContextPath() + "siwei/" + filename);
//                response.getWriter().write("File uploaded successfully: " + Code.host + "siwei/" + getFilename());
                asyncContext.complete();

            } catch (Exception e) {
                System.out.println("outputfilimpl File upload failed: " + e.getMessage());
                throw new RuntimeException(e);

            }
        });
        request.getSession().setAttribute("people",false);
        return flag;

    }
}
