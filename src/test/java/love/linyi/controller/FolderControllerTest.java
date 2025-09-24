//package love.linyi.controller;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import javax.servlet.http.HttpSession;
//
//import love.linyi.controller.fold.FolderController;
//import love.linyi.service.UserFolderService;
//import love.linyi.service.login.CheckIfLogin;
//import love.linyi.service.folderService.FolderStructureBuilderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import java.util.HashMap;
//import java.util.Map;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FolderControllerTest {
//@ExtendWith(MockitoExtension.class)
//    @Mock
//    private HttpSession session;
//
//    @Mock
//    private UserFolderService userFolderService;
//
//    @Mock
//    private CheckIfLogin checkIfLogin;
//
//    @Mock
//    private FolderStructureBuilderService folderStructureBuilder;
//
//    @InjectMocks
//    private FolderController folderController;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    void testGetFolder_UserNotLoggedIn() throws JsonProcessingException {
//        // Arrange
//
//        when(checkIfLogin.checkIfLogin(session)).thenReturn(false);
//
//        // Act
////        String result = folderController.getfolder(session);
//
//        // Assert
//        Map<String, Object> expectedResponse = new HashMap<>();
//        String expected = String.valueOf(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(expectedResponse));
//        assertEquals(expected, result);
//        verify(checkIfLogin).checkIfLogin(session);
//        verifyNoInteractions(userFolderService);
//        verifyNoInteractions(folderStructureBuilder);
//    }
//    @Test
//    void testifUpload_sizeTobig() {
//        // Arrange
//        long totalSize = 100;
//        when(checkIfLogin.checkIfLogin(session)).thenReturn(true);
//        // Act
//        ResponseEntity<Map<String, Object>> response = folderController.ifUpload(totalSize);
//        // Assert
//        Map<String, Object> expectedResponse = new HashMap<>();
//        assertEquals(ResponseEntity.status(HttpStatus.OK).body(expectedResponse), response);
//    }
//}