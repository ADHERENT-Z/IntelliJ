package zip;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {
    public static void main(String[] args) throws IOException{
        String zipname = args[0];
        showContents(zipname);
        System.out.println("----");
        showContents2(zipname);
    }

    private static void showContents2(String zipname) throws IOException {
        FileSystem fs = FileSystems.newFileSystem(Paths.get(zipname), null);
        Files.walkFileTree(fs.getPath("/"), new SimpleFileVisitor<Path>(){
            public FileVisitResult fileVisitResult(Path path, BasicFileAttributes attrs)
                    throws IOException{
                System.out.println(path);
                for (String line : Files.readAllLines(path, Charset.forName("UTF-8")))
                    System.out.println(" " + line);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void showContents(String zipname) throws IOException {
        // here, we use the classic zip API
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipname));
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null){
                System.out.println(entry.getName());
                Scanner in = new Scanner(zin, String.valueOf(StandardCharsets.UTF_8));
                while (in.hasNextLine()) {
                    System.out.println(" " + in.nextLine());
                    // Do not close in
                    zin.closeEntry();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
