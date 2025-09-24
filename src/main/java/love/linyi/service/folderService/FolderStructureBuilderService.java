package love.linyi.service.folderService;

import com.fasterxml.jackson.core.JsonProcessingException;
import love.linyi.domin.UserFolder;

import java.util.List;

public interface FolderStructureBuilderService {
    public String buildFileStructure(List<UserFolder> userFolders) throws JsonProcessingException;
}
