package com.booleanuk.api.controller;

import com.booleanuk.api.repository.BorrowedGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("borrow")
public class BorrowedGameController {
    @Autowired
    BorrowedGameRepository repository;
}
