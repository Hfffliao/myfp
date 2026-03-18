package love.linyi.service.folderUtilService;

import java.io.IOException;

public interface ReNameFileOrFolderOnSystem {

        void reName(String filePath,String oldName,String newName) throws IOException;
}
