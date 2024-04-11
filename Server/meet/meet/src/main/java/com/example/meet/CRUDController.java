package com.example.meet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
@RestController
public class CRUDController {
    @Autowired
    public CRUDService crudService ;

    public CRUDController(CRUDService crudService){
        this.crudService = crudService;
    }

    @PostMapping("/create")
    public String createCRUD(@RequestBody CRUD crud) throws InterruptedException, ExecutionException{
        return crudService.createCRUD(crud);
    }

    @GetMapping("/get")
    public CRUD getCRUD(@RequestParam String name) throws InterruptedException, ExecutionException{
        return crudService.getCRUD(name);
    }

    @PutMapping("/updatec")
    public String updateCRUD(@RequestBody CRUD crud) throws InterruptedException, ExecutionException{
        return crudService.updateCRUD(crud);
    }

    @DeleteMapping("/delete")
    public String deleteCRUD(@RequestParam String documentId) throws InterruptedException, ExecutionException{
        return crudService.deleteCRUD(documentId);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working!");
    }

}