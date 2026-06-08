package iti.eventdriven.game_service.controller;

import iti.eventdriven.game_service.dto.GameDifficulty;
import iti.eventdriven.game_service.dto.GameState;
import iti.eventdriven.game_service.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // POST /api/game/start?humanSymbol=X&difficulty=Easy
    @PostMapping("/start")
    public ResponseEntity<GameState> startGame(
            @RequestParam(defaultValue = "X") char humanSymbol,
            @RequestParam(defaultValue = "Easy") GameDifficulty difficulty) {

        GameState state = gameService.startNewGame(humanSymbol, difficulty);
        return ResponseEntity.ok(state);
    }

    // GET /api/game/{gameId}
    @GetMapping("/{gameId}")
    public ResponseEntity<GameState> getGame(@PathVariable String gameId) {
        GameState state = gameService.getGame(gameId);
        return ResponseEntity.ok(state);
    }

    // POST /api/game/{gameId}/move?row=1&col=1
    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameState> makePlayerMove(
            @PathVariable String gameId,
            @RequestParam int row,
            @RequestParam int col) {

        // 1. apply human move
        GameState state = gameService.playHumanMove(gameId, row, col);

        return ResponseEntity.ok(state);
    }

    @PostMapping("/{gameId}/ai-move")
    public ResponseEntity<GameState> makeAIMove(@PathVariable String gameId) {
        // 2. apply AI move
        GameState state = gameService.playAIMove(gameId);
        return ResponseEntity.ok(state);
    }
}