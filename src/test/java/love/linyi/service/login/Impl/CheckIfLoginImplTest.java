package love.linyi.service.login.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CheckIfLoginImplTest {
    @ExtendWith(MockitoExtension.class)
    @InjectMocks
    private CheckIfLoginImpl checkIfLogin;
    @Test
    void checkIfLogin_sessionIsEmpty() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(null);
        when(session.getAttribute("id")).thenReturn(null);
        checkIfLogin.checkIfLogin(session);
        // Act
        boolean result = checkIfLogin.checkIfLogin(session);
        // Assert
        assertFalse(result);
    }
    @Test
    void checkIfLogin_sessionNotEmpty() {
        // Arrange
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn("3390351358");
        when(session.getAttribute("id")).thenReturn(2);
        checkIfLogin.checkIfLogin(session);
        // Act
        boolean result = checkIfLogin.checkIfLogin(session);
        // Assert
        assertTrue(result);
    }
}