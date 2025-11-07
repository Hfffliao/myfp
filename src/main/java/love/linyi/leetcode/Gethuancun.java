//package love.linyi.leetcode;
//
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//
//import java.io.File;
//
//
//public class Gethuancun {
//    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 2; // 2MB
//    public static void main(String[] args) {
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        factory.setSizeThreshold(MEMORY_THRESHOLD);
//        File tempDir = new File(System.getProperty("java.io.tmpdir"));
//        // 设置临时文件存储目录
//        factory.setRepository(tempDir);
//
//        // 获取临时目录的可用空间（单位：字节）
//        long freeSpace = tempDir.getFreeSpace();
//        System.out.println("临时目录可用空间: " + freeSpace + " 字节");
//        System.out.println("临时目录可用空间: " + (freeSpace / (1024 * 1024)) + " MB");
//    }
//}
