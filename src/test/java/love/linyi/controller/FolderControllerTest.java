package love.linyi.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import love.linyi.controller.fold.FolderController;
import love.linyi.domin.UserProfit;
import love.linyi.service.UserFolderService;
import love.linyi.service.UserProfitService;
import love.linyi.service.folderService.HandleMultipartService;
import love.linyi.service.login.CheckIfLogin;
import love.linyi.service.folderService.FolderStructureBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FolderControllerTest {
@ExtendWith(MockitoExtension.class)
    @Mock
    private HttpSession session;

    @Mock
    private UserFolderService userFolderService;
    @Mock
    private UserProfitService userProfitService;
    @Mock
    private FolderStructureBuilderService folderStructureBuilder;
    @Mock
    public HandleMultipartService handleMultipartService;
    @InjectMocks
    private FolderController folderController;

    @BeforeEach
    void setUp() {
    }

//    @Test
//    void testGetFolder_UserNotLoggedIn() throws JsonProcessingException {
//        // Arrange
//
//        when(checkIfLogin.checkIfLogin(session)).thenReturn(false);
//
//         //Act
//        ResponseEntity<String> result = folderController.getfolder(session);
//
//        // Assert
//        Map<String, Object> expectedResponse = new HashMap<>();
//        String expected = String.valueOf(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(expectedResponse));
//        assertEquals(expected, result);
//        verify(checkIfLogin).checkIfLogin(session);
//        verifyNoInteractions(userFolderService);
//        verifyNoInteractions(folderStructureBuilder);
//    }
    @Test
    void testifUpload_sizeTobig() {
        // Arrange
        long totalSize = 100;
        when(userProfitService.getuserprofit(61))
                .thenReturn(new UserProfit(61,50,100,50));
        when(session.getAttribute("id")).thenReturn(61);
        // Act
        ResponseEntity<Map<String, Object>> response = folderController.ifUpload(totalSize,session);

        // Assert
        Map<String, Object> responseBody = response.getBody();
        assertEquals(false,responseBody.get("ifupload") );
    }
    @Test
    void testifUpload_size_right() {
        // Arrange
        long totalSize = 25;
        when(userProfitService.getuserprofit(61))
                .thenReturn(new UserProfit(61,50,100,50));
        when(session.getAttribute("id")).thenReturn(61);
        // Act
        ResponseEntity<Map<String, Object>> response = folderController.ifUpload(totalSize,session);

        // Assert
        Map<String, Object> responseBody = response.getBody();
        assertEquals(true,responseBody.get("ifupload") );
    }
}