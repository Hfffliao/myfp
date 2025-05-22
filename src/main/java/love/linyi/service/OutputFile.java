package love.linyi.service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface OutputFile {
    public boolean outputFile(HttpServletRequest request, HttpServletResponse response);

    String getFilename();
}
