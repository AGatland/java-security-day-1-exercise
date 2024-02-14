package com.booleanuk.api.controller;

import com.booleanuk.api.dto.BorrowedGameDto;
import com.booleanuk.api.exception.ApiException;
import com.booleanuk.api.model.BorrowedGame;
import com.booleanuk.api.model.Game;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.BorrowedGameRepository;
import com.booleanuk.api.repository.GameRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("borrow")
public class BorrowedGameController {
    @Autowired
    BorrowedGameRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Response<List<BorrowedGameDto>>> getAllForUser(@PathVariable int userId) {
        return ResponseEntity.ok(new Response<>(this.repository.findBorrowedGamesByUserId(userId)));
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<Response<List<BorrowedGameDto>>> getAllForGame(@PathVariable int gameId) {
        return ResponseEntity.ok(new Response<>(this.repository.findBorrowedGamesByGameId(gameId)));
    }

    @PostMapping("/users/{userId}/games/{gameId}")
    public ResponseEntity<Response<BorrowedGameDto>> lendGame(@PathVariable int userId, @PathVariable int gameId) {
        // Check if game is currently lent out
        if (!this.repository.findIsBorrowedByGameId(gameId).isEmpty()) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Game is already lent out");
        }
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user not found"));
        Game game = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "game not found"));
        BorrowedGame createdBorrowedGame = this.repository.save(new BorrowedGame(user, game));

        return ResponseEntity.ok(new Response<>(this.translateToDto(createdBorrowedGame)));
    }

    public BorrowedGameDto translateToDto(BorrowedGame borrowedGame) {
        return new BorrowedGameDto(borrowedGame.getId(), borrowedGame.getGame().getTitle(), borrowedGame.getUser().getUsername(), borrowedGame.getBorrowDate(), borrowedGame.getReturnDate());
    }
}
