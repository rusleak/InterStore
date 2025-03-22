package mainpackage.interstore.model.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public class MultipartProcesses {
    public static void processMultipartFile(MultipartFile file, String path) throws IOException {
        String fileName = file.getOriginalFilename();
        file.transferTo(Path.of(path + fileName));
    }
}
