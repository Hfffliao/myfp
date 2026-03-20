package love.linyi.service.folderUtilService.impl;
 
import love.linyi.service.infra.file.impl.AsyncFileDecodingTaskImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
 
@ExtendWith(MockitoExtension.class)
class AsyncFileDecodingTaskImplTest {
 
    @InjectMocks
    AsyncFileDecodingTaskImpl asyncFileDecodingTask;
 
    @Test
    /**
     * 测试正常情况下的文件解码功能
     * 测试目的：验证 AsyncFileDecodingTaskImpl 能够正确处理正常的 Base64 编码内容
     * 测试步骤：
     * 1. 准备测试数据：将字符串编码为 Base64
     * 2. 创建临时文件路径
     * 3. 调用异步解码方法，设置 100 毫秒延迟
     * 4. 等待 200 毫秒确保任务完成
     * 5. 验证解码后的文件是否存在
     * 6. 验证文件内容是否与原始内容一致
     * 7. 验证任务是否已添加到映射中
     * 8. 清理临时文件
     * 预期结果：
     * - 解码后的文件存在
     * - 文件内容与原始内容一致
     * - 任务已添加到映射中
     */
    void decodeFileAsync_standard() throws Exception {
        // 准备测试数据
        String testContent = "Hello, World! This is a test for file decoding.";
        byte[] encodedBytes = Base64.getEncoder().encode(testContent.getBytes());
        String base64 = new String(encodedBytes);
        
        // 创建临时文件路径
        String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "test_decoded_" + System.currentTimeMillis() + ".txt";
        File tempFile = new File(tempFilePath);
        
        // 确保测试前文件不存在
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        // 开始测试
        asyncFileDecodingTask.decodeFileAsync(base64, tempFilePath, 100, TimeUnit.MILLISECONDS);
        
        // 等待任务完成（稍长于延迟时间）
        Thread.sleep(200);
        
        // 检测结果
        System.out.println("Decoded file path: " + tempFilePath);
        assertTrue(tempFile.exists(), "解码后的文件应该存在");
        
        // 验证文件内容是否正确
        String fileContent = Files.readString(tempFile.toPath());
        assertEquals(testContent, fileContent, "文件内容应该与原始内容一致");
        
        // 验证任务是否已添加到映射中
        Field tasksField = AsyncFileDecodingTaskImpl.class.getDeclaredField("tasks");
        tasksField.setAccessible(true);
        ConcurrentHashMap<String, ?> tasks = (ConcurrentHashMap<String, ?>) tasksField.get(asyncFileDecodingTask);
        assertTrue(tasks.containsKey(tempFilePath), "任务应该已添加到映射中");
        
        // 清理临时文件
        tempFile.delete();
    }
 
    @Test
    /**
     * 测试空内容的文件解码功能
     * 测试目的：验证 AsyncFileDecodingTaskImpl 能够正确处理空内容的 Base64 编码
     * 测试步骤：
     * 1. 准备测试数据：将空字符串编码为 Base64
     * 2. 创建临时文件路径
     * 3. 调用异步解码方法，设置 50 毫秒延迟
     * 4. 等待 100 毫秒确保任务完成
     * 5. 验证解码后的文件是否存在
     * 6. 验证文件内容是否为空
     * 7. 清理临时文件
     * 预期结果：
     * - 解码后的文件存在
     * - 文件内容为空
     */
    void decodeFileAsync_emptyContent() throws Exception {
        // 准备测试数据
        String testContent = "";
        byte[] encodedBytes = Base64.getEncoder().encode(testContent.getBytes());
        String base64 = new String(encodedBytes);
        
        // 创建临时文件路径
        String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "test_decoded_empty_" + System.currentTimeMillis() + ".txt";
        File tempFile = new File(tempFilePath);
        
        // 确保测试前文件不存在
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        // 开始测试
        asyncFileDecodingTask.decodeFileAsync(base64, tempFilePath, 50, TimeUnit.MILLISECONDS);
        
        // 等待任务完成
        Thread.sleep(100);
        
        // 检测结果
        System.out.println("Empty content decoded file path: " + tempFilePath);
        assertTrue(tempFile.exists(), "解码后的文件应该存在");
        
        // 验证文件内容是否为空
        String fileContent = Files.readString(tempFile.toPath());
        assertEquals(testContent, fileContent, "文件内容应该为空");
        
        // 清理临时文件
        tempFile.delete();
    }
}