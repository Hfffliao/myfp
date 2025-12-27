package love.linyi.service.folderUtilService;

import org.springframework.web.multipart.MultipartFile;

public interface HandleMultipartService {
    public void handleMultipart(MultipartFile[] file,int id,String filestructure,String username,String relativePath);
}
