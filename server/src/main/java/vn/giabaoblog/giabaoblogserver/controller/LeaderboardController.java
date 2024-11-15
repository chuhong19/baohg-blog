package vn.giabaoblog.giabaoblogserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.data.domains.ScoreRecord;
import vn.giabaoblog.giabaoblogserver.data.dto.request.CreateRecordRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.ScoreNameDTO;
import vn.giabaoblog.giabaoblogserver.services.LeaderboardService;

@RestController
@RequestMapping(value = "/leaderboard")
public class LeaderboardController {

    @Autowired
    public LeaderboardService leaderboardService;

    @PostMapping("/create")
    public StandardResponse<ScoreRecord> createRecord(@RequestBody CreateRecordRequest request) {
        return leaderboardService.createRecord(request.getScoreName(), request.getScore());
    }

    @GetMapping("/get")
    public StandardResponse<ScoreRecord> getRecord(@RequestBody ScoreNameDTO scoreName) {
        return leaderboardService.getRecord(scoreName.getScoreName());
    }

    @PutMapping("/edit")
    public StandardResponse<ScoreRecord> editRecord(@RequestBody CreateRecordRequest request) {
        return leaderboardService.editRecord(request.getScoreName(), request.getScore());
    }

    @DeleteMapping("/delete")
    public StandardResponse<String> deleteRecord(@RequestBody ScoreNameDTO scoreName) {
        return leaderboardService.deleteRecord(scoreName.getScoreName());
    }

}
