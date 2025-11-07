package love.linyi.service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface OutputFile {
    public boolean outputFile(HttpServletRequest request, HttpServletResponse response);

    String getFilename();
}
