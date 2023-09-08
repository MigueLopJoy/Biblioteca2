package com.miguel.library.controller;


import com.miguel.library.model.BookCopy;
import com.miguel.library.model.BookWork;
import com.miguel.library.services.IBookWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/titles-catalog")
public class BookWorkController {

    @Autowired
    private IBookWorkService bookWorkService;


    @PostMapping("save-title")
    public ResponseEntity<BookWork> saveNewTitle(
            @RequestBody BookWork bookWork
    ) {
        return ResponseEntity.ok(bookWorkService.saveNewBookWork(bookWork));
    }
}
