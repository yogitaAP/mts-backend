package groots.sad.project.MassTransitSystem.controller;

import groots.sad.project.MassTransitSystem.EventSimulator;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mts/files")
public class FileUploadController {

    public static final String DISTIBUTION = "distibution";
    private static String UPLOADED_FOLDER = System.getProperty("user.dir") + "/mtsfiles/";


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] uploadFiles){

        if (uploadFiles.length > 2) {
            return new ResponseEntity("Cannot upload more than two files at a time", HttpStatus.BAD_REQUEST);
        }

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
        readDataFromFiles(uploadFiles);
        cleanTheDirectory();
        return new ResponseEntity("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);
    }

    private void cleanTheDirectory() {

        try {
            FileUtils.cleanDirectory(new File(UPLOADED_FOLDER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromFiles(MultipartFile[] uploadFiles) {

        EventSimulator eventSimulator = EventSimulator.getInstance();
        List<MultipartFile> scannedList = new ArrayList<>();
        if (uploadFiles.length == 2) {
            if (uploadFiles[0].getOriginalFilename().contains(DISTIBUTION)) {
                scannedList.add(uploadFiles[1]);
                scannedList.add(uploadFiles[0]);
            } else {
                scannedList.add(uploadFiles[0]);
                scannedList.add(uploadFiles[1]);
            }
        }
        for (MultipartFile file : scannedList) {
            String fileName = file.getOriginalFilename();
            System.out.println("Reading data from file" + fileName);
            if (fileName.contains(DISTIBUTION)) {
                eventSimulator.readPassengerFrequencyData(fileName);
            } else {
                eventSimulator.readData(fileName);
            }
        }
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
