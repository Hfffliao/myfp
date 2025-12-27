package love.linyi.service.folderUtilService.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import love.linyi.service.impl.UserFolderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
    class HandleMultipartServiceImplTest {

        @Mock
        private UserFolderServiceImpl userFolderService;
        @InjectMocks
        private HandleMultipartServiceImpl handleMultipartService;
        @InjectMocks
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
//            objectMapper = new ObjectMapper();
//            handleMultipartService.objectMapper = objectMapper;
        }
        @Test
        void testHandleMultipart_NormalCase() throws Exception {
            // 模拟 MultipartFile
            MultipartFile mockFile = org.mockito.Mockito.mock(MultipartFile.class);
            MultipartFile[] files = {mockFile};

            int userId = 1;
            String fileStructureJson = "[{\"name\":\"dev\",\"type\":\"folder\",\"children\":[{\"name\":\"fd\",\"type\":\"file\"},{\"name\":\"stderr\",\"type\":\"file\"},{\"name\":\"stdin\",\"type\":\"file\"},{\"name\":\"stdout\",\"type\":\"file\"},{\"name\":\"shm\",\"type\":\"folder\",\"children\":[{\"name\":\"新建 XLS 工作表.xls\",\"type\":\"file\"},{\"name\":\"新建文本文档.txt\",\"type\":\"file\"}]}]}]";

            // 调用真实方法
            handleMultipartService.handleMultipart(files, userId, fileStructureJson, "testUser", "css");

            // 验证 UserFolderService 的 save 方法被调用
            verify(userFolderService, times(1)).save(anyList());
        }
    @Test
    void testHandleMultipart_NullMultiCase() throws Exception {
        // 模拟 MultipartFile
        MultipartFile mockFile = org.mockito.Mockito.mock(MultipartFile.class);
        MultipartFile[] files = {mockFile};

        int userId = 1;
        String fileStructureJson = "[{\"name\":\"dev\",\"type\":\"folder\",\"children\":[{\"name\":\"fd\",\"type\":\"file\"},{\"name\":\"stderr\",\"type\":\"file\"},{\"name\":\"stdin\",\"type\":\"file\"},{\"name\":\"stdout\",\"type\":\"file\"},{\"name\":\"shm\",\"type\":\"folder\",\"children\":[{\"name\":\"新建 XLS 工作表.xls\",\"type\":\"file\"},{\"name\":\"新建文本文档.txt\",\"type\":\"file\"}]}]}]";

        // 调用真实方法
        handleMultipartService.handleMultipart(files, userId, fileStructureJson, "testUser", "");

        // 验证 UserFolderService 的 save 方法被调用
        verify(userFolderService, times(1)).save(anyList());
    }
    @Test
    void testHandleMultipart_NullFileStructrueCase() throws Exception {
        // 模拟 MultipartFile
        MultipartFile mockFile = org.mockito.Mockito.mock(MultipartFile.class);
        MultipartFile[] files = {mockFile};

        int userId =1;
        String fileStructureJson = null;

        // 调用真实方法
        handleMultipartService.handleMultipart(files, userId, fileStructureJson, "testUser", "");

        // 验证 UserFolderService 的 save 方法被调用
        verify(userFolderService, times(0)).save(anyList());
    }
    @Test
    void testHandleMultipart_unableFileStructrueCase() throws Exception {
        // 模拟 MultipartFile
        MultipartFile mockFile = org.mockito.Mockito.mock(MultipartFile.class);
        MultipartFile[] files = {mockFile};
        int userId =1;
        String fileStructureJson = "fdsfds";
        // 调用真实方法
        handleMultipartService.handleMultipart(files, userId, fileStructureJson,    "testUser", "");
        // 验证 UserFolderService 的 save 方法被调用
        verify(userFolderService, times(0)).save(anyList());
    }

}