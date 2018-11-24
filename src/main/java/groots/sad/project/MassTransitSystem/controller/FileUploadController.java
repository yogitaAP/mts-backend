package groots.sad.project.MassTransitSystem.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mts/files")
public class FileUploadController {

    private static String UPLOADED_FOLDER = System.getProperty("user.dir") + "/mtsfiles/";


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] uploadFiles){

        String uploadedFileName = Arrays.stream(uploadFiles)
                                        .map(MultipartFile::getOriginalFilename)
                                        .filter(x -> !StringUtils.isEmpty(x))
                                        .collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadFiles));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);
    }

    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; //next file pls
            }
            byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }

    }
}
