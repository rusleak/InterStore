package mainpackage.interstore.model.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    public static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);

        try {
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                System.out.println("Файл успешно удалён: " + filePath);
            } else {
                System.out.println("Файл не найден: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при удалении файла " + filePath + ": " + e.getMessage());
        }
    }
    public static void transferMultipartFile(MultipartFile file, Path path) throws IOException {
        file.transferTo(Path.of(path + "/"+ file.getOriginalFilename()));
    }
}
