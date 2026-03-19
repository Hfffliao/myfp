package love.linyi.service.impl;

import love.linyi.dao.UserFolderDao;
import love.linyi.domin.UserFolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFolderServiceImplTest {

    @Mock
    private UserFolderDao userFolderDao;

    @InjectMocks
    private UserFolderServiceImpl userFolderService;

    @Test
    void reNameFileOrFolder_renameFile_success() {
        int id = 1;
        int userId = 100;
        String oldName = "old.txt";
        String newName = "new.txt";
        UserFolder folder = new UserFolder(id, oldName, "/docs", "file", userId);

        when(userFolderDao.getFolderByIdAndUserId(id, userId)).thenReturn(folder);
        when(userFolderDao.countByPathAndUserId("/docs", userId, "file", newName)).thenReturn(0);
        when(userFolderDao.updateFolderNameAndPath(id, newName, "/docs", userId)).thenReturn(1);

        String result = userFolderService.reNameFileOrFolder(id, newName, userId);

        assertEquals(oldName, result);
        verify(userFolderDao).updateFolderNameAndPath(id, newName, "/docs", userId);
        verify(userFolderDao, never()).updateChildrenPaths(anyString(), anyString(), anyInt());
    }

    @Test
    void reNameFileOrFolder_renameFolder_updateChildrenPaths() {
        int id = 1;
        int userId = 100;
        String oldName = "folder1";
        String newName = "folder2";
        UserFolder folder = new UserFolder(id, oldName, "/parent", "folder", userId);

        when(userFolderDao.getFolderByIdAndUserId(id, userId)).thenReturn(folder);
        when(userFolderDao.countByPathAndUserId("/parent", userId, "folder", newName)).thenReturn(0);
        when(userFolderDao.updateFolderNameAndPath(id, newName, "/parent", userId)).thenReturn(1);
        when(userFolderDao.updateChildrenPaths("/parent/folder1", "/parent/folder2", userId)).thenReturn(1);

        String result = userFolderService.reNameFileOrFolder(id, newName, userId);

        assertEquals(oldName, result);
        verify(userFolderDao).updateChildrenPaths("/parent/folder1", "/parent/folder2", userId);
    }

    @Test
    void reNameFileOrFolder_folderNotFound_throwsException() {
        int id = 999;
        int userId = 100;

        when(userFolderDao.getFolderByIdAndUserId(id, userId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userFolderService.reNameFileOrFolder(id, "newName", userId));

        assertEquals("文件夹不存在或无权限访问", exception.getMessage());
    }

    @Test
    void reNameFileOrFolder_sameName_throwsException() {
        int id = 1;
        int userId = 100;
        String name = "same.txt";
        UserFolder folder = new UserFolder(id, name, "", "file", userId);

        when(userFolderDao.getFolderByIdAndUserId(id, userId)).thenReturn(folder);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userFolderService.reNameFileOrFolder(id, name, userId));

        assertEquals("新名称与旧名称相同", exception.getMessage());
    }

    @Test
    void reNameFileOrFolder_nameAlreadyExists_throwsException() {
        int id = 1;
        int userId = 100;
        String oldName = "old.txt";
        String newName = "existing.txt";
        UserFolder folder = new UserFolder(id, oldName, "/docs", "file", userId);

        when(userFolderDao.getFolderByIdAndUserId(id, userId)).thenReturn(folder);
        when(userFolderDao.countByPathAndUserId("/docs", userId, "file", newName)).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userFolderService.reNameFileOrFolder(id, newName, userId));

        assertEquals("同名文件或文件夹已存在", exception.getMessage());
    }

    @Test
    void reNameFileOrFolder_rootLevelFile_success() {
        int id = 1;
        int userId = 100;
        String oldName = "root.txt";
        String newName = "new_root.txt";
        UserFolder folder = new UserFolder(id, oldName, "", "file", userId);

        when(userFolderDao.getFolderByIdAndUserId(id, userId)).thenReturn(folder);
        when(userFolderDao.countByPathAndUserId("", userId, "file", newName)).thenReturn(0);
        when(userFolderDao.updateFolderNameAndPath(id, newName, "", userId)).thenReturn(1);

        String result = userFolderService.reNameFileOrFolder(id, newName, userId);

        assertEquals(oldName, result);
        verify(userFolderDao).updateFolderNameAndPath(id, newName, "", userId);
    }
}
