package mainpackage.interstore.model.util;

import mainpackage.interstore.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String PRODUCT_IMAGES = "/Users/rusleak/IdeaDoNotDeleteProjects/InterStore/product_images";

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

    public static void saveProductImages123(Product product, List<MultipartFile> multipartFileList) throws Exception {
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
            file.transferTo(filePath.toFile());
            System.out.println(fileName);
            String imagePath = mainCategory + File.separator + subCategory + File.separator + nestedCategory + File.separator + fileName;
            images.add(imagePath);
        }
        product.setProductImages(images);
        if (product.getProductImages() == null || product.getProductImages().isEmpty()) {
            throw new Exception("ImageList is empty");
        }
    }
    //TODO на стороне романа обязательно файл нейм должен становится UUID
    public static void saveProductImages(Product product, List<MultipartFile> multipartFileList, List<String> dtoImages) throws Exception {
        String mainCategory = product.getNestedCategory().getSubcategory().getMainCategory().getName();
        String subCategory = product.getNestedCategory().getSubcategory().getName();
        String nestedCategory = product.getNestedCategory().getName();
        List<String> images = new ArrayList<>();


        String categoryPath = PRODUCT_IMAGES + File.separator + mainCategory + File.separator + subCategory + File.separator + nestedCategory;
        Path categoryDir = Paths.get(categoryPath);

        // Проверяем, существует ли директория и можем ли в нее записывать
        File categoryDirFile = categoryDir.toFile();
        if (!categoryDirFile.exists()) {
            Files.createDirectories(categoryDir);
        }
        if (!categoryDirFile.canWrite()) {
            throw new Exception("Cannot write to directory: " + categoryDir);
        }

        List<String> currentImages = product.getProductImages() != null ? new ArrayList<>(product.getProductImages()) : new ArrayList<>();
        for (String imagePath : currentImages) {
            if (!dtoImages.contains(imagePath)) {
                Path fileToDelete = Paths.get(PRODUCT_IMAGES,imagePath);
                System.out.println("Path to delete = " + fileToDelete);
                File file = fileToDelete.toFile();
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + fileToDelete);
                    } else {
                        System.out.println("Failed to delete file: " + fileToDelete);
                    }
                }
            }
        }

        // Обрабатываем каждый файл
        for (MultipartFile file : multipartFileList) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(categoryDir.toString(), fileName);
            file.transferTo(filePath.toFile());

            // Добавляем путь к изображению
            String imagePath = mainCategory + File.separator + subCategory + File.separator + nestedCategory + File.separator + fileName;
            images.add(imagePath);
        }


        // Проверка, что изображения были добавлены
        product.setProductImages(images);
        System.out.println("new images : " + product.getProductImages());
        if (product.getProductImages() == null || product.getProductImages().isEmpty()) {
            throw new Exception("ImageList is empty");
        }
    }
}
