package mainpackage.interstore.model.util;

import mainpackage.interstore.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String PRODUCT_IMAGES = "src/main/resources/static/product_images";

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

    public static void saveProductImages(Product product, List<MultipartFile> multipartFileList) throws Exception {
        String mainCategory = product.getNestedCategory().getSubcategory().getMainCategory().getName();
        String subCategory = product.getNestedCategory().getSubcategory().getName();
        String nestedCategory = product.getNestedCategory().getName();
        List<String> images = new ArrayList<>();

        String categoryPath = PRODUCT_IMAGES + File.separator + mainCategory + File.separator + subCategory + File.separator + nestedCategory;
        System.out.println("Category path: " + categoryPath);
        Path categoryDir = Paths.get(categoryPath);
        if (Files.notExists(categoryDir)) {
            Files.createDirectories(categoryDir);
        }
        for (MultipartFile file : multipartFileList) {
            String fileName = file.getOriginalFilename();
            //UUID на стороне 1C
            Path filePath = Paths.get(categoryDir.toString(), fileName);
            System.out.println(filePath);
            file.transferTo(filePath.toFile());
            String imagePath = mainCategory + File.separator + subCategory + File.separator + nestedCategory + File.separator + fileName;
            images.add(imagePath);
        }
        product.setProductImages(images);
        if (product.getProductImages() == null || product.getProductImages().isEmpty()) {
            throw new Exception("ImageList is empty");
        }
    }
}
