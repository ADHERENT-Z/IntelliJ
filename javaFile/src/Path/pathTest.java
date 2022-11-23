package Path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class pathTest {

    public static void main(String[] args) {
        // 通过连接给定的字符串创建一个路径
        Path absolute = Paths.get("/home", "harry"); // \home\harry
        Path relative = Paths.get("myprog", "conf", "user.properties");

        // 创建文件和目录

    }
}