package iti.eventdriven.analytics_service.controller;


import iti.eventdriven.analytics_service.dto.AnalyticsSummary;
import iti.eventdriven.analytics_service.dto.GameDifficulty;
import iti.eventdriven.analytics_service.dto.GameSummary;
import iti.eventdriven.analytics_service.dto.PageResponse;
import iti.eventdriven.analytics_service.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummary> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @GetMapping("/summary/{difficulty}")
    public ResponseEntity<AnalyticsSummary> getSummaryByDifficulty(@PathVariable GameDifficulty difficulty) {
        return ResponseEntity.ok(analyticsService.getSummaryByDifficulty(difficulty));
    }
    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getGameById(@PathVariable String gameId) {
        var game = analyticsService.getGameById(gameId);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/games")
    public ResponseEntity<PageResponse<GameSummary>> getAllGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(analyticsService.getAllGames(page, size));
    }
}