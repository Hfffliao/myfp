package love.linyi.service.infra.file;

import java.io.IOException;

public interface ReNameFileOrFolderOnSystem {

        void reName(String filePath,String oldName,String newName) throws IOException;
}
