package love.linyi.service.folderUtilService.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReNameFileOrFolderOnSystemImplTest {

    private final ReNameFileOrFolderOnSystemImpl reNameService = new ReNameFileOrFolderOnSystemImpl();

    @Test
    void reName_file_success(@TempDir Path tempDir) throws IOException {
        Path oldFile = tempDir.resolve("old.txt");
        Files.createFile(oldFile);

        assertDoesNotThrow(() -> reNameService.reName(tempDir.toString(), "old.txt", "new.txt"));

        assertTrue(Files.exists(tempDir.resolve("new.txt")));
        assertTrue(!Files.exists(oldFile));
    }

    @Test
    void reName_folder_success(@TempDir Path tempDir) throws IOException {
        Path oldFolder = tempDir.resolve("old_folder");
        Files.createDirectory(oldFolder);

        assertDoesNotThrow(() -> reNameService.reName(tempDir.toString(), "old_folder", "new_folder"));

        assertTrue(Files.exists(tempDir.resolve("new_folder")));
        assertTrue(!Files.exists(oldFolder));
    }

    @Test
    void reName_sourceNotExist_throwsIOException(@TempDir Path tempDir) {
        IOException exception = assertThrows(IOException.class,
                () -> reNameService.reName(tempDir.toString(), "not_exist.txt", "new.txt"));

        assertTrue(exception.getMessage().contains("文件或文件夹不存在"));
    }

    @Test
    void reName_targetAlreadyExist_throwsIOException(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("old.txt"));
        Files.createFile(tempDir.resolve("new.txt"));

        IOException exception = assertThrows(IOException.class,
                () -> reNameService.reName(tempDir.toString(), "old.txt", "new.txt"));

        assertTrue(exception.getMessage().contains("目标文件或文件夹已存在"));
    }

    @Test
    void reName_withNestedPath_success(@TempDir Path tempDir) throws IOException {
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectory(subDir);
        Path oldFile = subDir.resolve("file.txt");
        Files.createFile(oldFile);

        assertDoesNotThrow(() -> reNameService.reName(subDir.toString(), "file.txt", "renamed.txt"));

        assertTrue(Files.exists(subDir.resolve("renamed.txt")));
        assertTrue(!Files.exists(oldFile));
    }
}
