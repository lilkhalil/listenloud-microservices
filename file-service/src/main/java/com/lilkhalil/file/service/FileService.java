package com.lilkhalil.file.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    @SneakyThrows
    public String save(MultipartFile request) {
        ObjectId id = template.store(
                request.getInputStream(),
                request.getOriginalFilename(),
                request.getContentType()
        );
        return id.toString();
    }

    @SneakyThrows
    public void get(String id, HttpServletResponse response) {
        GridFSFile file = Optional
                .ofNullable(template.findOne(query(where("_id").is(id))))
                .orElseThrow(() -> new FileNotFoundException("File with this id not found!"));
        FileCopyUtils.copy(operations.getResource(file).getInputStream(), response.getOutputStream());
    }

    public void delete(String id) {
        template.delete(Query.query(Criteria.where("_id").is(id)));
    }

}
