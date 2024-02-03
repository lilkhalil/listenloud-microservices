package com.lilkhalil.file.controller;

import com.lilkhalil.file.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.save(file);
    }

    @GetMapping("/{id}")
    public void getSource(@PathVariable("id") String id, HttpServletResponse response) {
        fileService.get(id, response);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable("id") String id) {
        fileService.delete(id);
    }

}
