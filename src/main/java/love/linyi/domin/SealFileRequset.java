package love.linyi.domin;

import java.time.LocalDateTime;

// 定义一个数据传输对象来接收 JSON 数据
public class SealFileRequset {
    private String filepath;
    private String filename;
    private LocalDateTime sealTime;

    // Getters 和 Setters
    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getSealTime() {
        return sealTime;
    }

    public void setSealTime(LocalDateTime sealTime) {
        this.sealTime = sealTime;
    }
}