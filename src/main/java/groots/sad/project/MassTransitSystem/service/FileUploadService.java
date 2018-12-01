package groots.sad.project.MassTransitSystem.service;

import groots.sad.project.MassTransitSystem.EventSimulator;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    public static final String PASSENGER_INFO = "passengerInfo";
    private static String UPLOADED_FOLDER = System.getProperty("user.dir") + "/mtsfiles/";

    public ResponseEntity<?> uploadFile(MultipartFile uploadFile, String fileType) {

        File directory = new File(UPLOADED_FOLDER);
        if(!directory.exists()){
            directory.mkdir();
        }
        try {
            saveUploadedFile(uploadFile);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        readDataFromFile(uploadFile, fileType);
        cleanTheDirectory();
        return new ResponseEntity(HttpStatus.OK);
    }

    private void saveUploadedFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return;
        }
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);
    }

    private void cleanTheDirectory() {

        try {
            FileUtils.cleanDirectory(new File(UPLOADED_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromFile(MultipartFile file, String fileType) {

        EventSimulator eventSimulator = EventSimulator.getInstance();
        String fileName = file.getOriginalFilename();
        System.out.println("Reading data from file" + fileName);
        if (fileType.equals(PASSENGER_INFO)) {
            eventSimulator.readPassengerFrequencyData(fileName);
        } else {
            eventSimulator.readData(fileName);
        }

    }
}
