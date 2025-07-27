package online_shop.controller;

import lombok.RequiredArgsConstructor;
import online_shop.service.MinIOService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final MinIOService minioService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        minioService.uploadFile(file.getOriginalFilename(), file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(minioService.downloadFile(fileName));
    }
}
