package vn.giabaoblog.giabaoblogserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.DuplicateException;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.ScoreRecord;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.repository.LeaderboardRepository;

import java.util.Optional;

@Service
@Slf4j
public class LeaderboardService {

    @Autowired
    public final LeaderboardRepository leaderboardRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public StandardResponse<ScoreRecord> createRecord(String scoreName, Long score) {
        Optional<ScoreRecord> recordOpt = leaderboardRepository.findByName(scoreName);
        if (recordOpt.isPresent())
            throw new DuplicateException("Score name already exist");

        ScoreRecord newRecord = new ScoreRecord();
        newRecord.setName(scoreName);
        newRecord.setScore(score);
        newRecord.setUserId("BOT");
        leaderboardRepository.save(newRecord);
        redisTemplate.opsForValue().set(scoreName, score);
        return StandardResponse.create("201", "Record created", newRecord);
    }

    public StandardResponse<ScoreRecord> getRecord(String scoreName) {
        long startTime = System.nanoTime();
        Long scoreFromRedis = (Long) redisTemplate.opsForValue().get(scoreName);

        if (scoreFromRedis != null) {
            ScoreRecord cachedRecord = new ScoreRecord();
            cachedRecord.setName(scoreName);
            cachedRecord.setScore(scoreFromRedis);
            cachedRecord.setUserId("BOT");
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("Duration query: " + duration);
            return StandardResponse.create("200", "Record retrieved from Redis", cachedRecord);
        }

        ScoreRecord record = leaderboardRepository.findByName(scoreName)
                .orElseThrow(() -> new NotFoundException("Record not found"));

        redisTemplate.opsForValue().set(scoreName, record.getScore());
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Duration query: " + duration);
        return StandardResponse.create("200", "Record retrieved from database", record);
    }

    public StandardResponse<ScoreRecord> editRecord(String scoreName, Long newScore) {
        ScoreRecord record = leaderboardRepository.findByName(scoreName)
                .orElseThrow(() -> new NotFoundException("Record not found"));
        record.setScore(newScore);
        leaderboardRepository.save(record);
        redisTemplate.opsForValue().set(scoreName, newScore);
        return StandardResponse.create("200", "Record edited", record);
    }

    public StandardResponse<String> deleteRecord(String scoreName) {
        ScoreRecord record = leaderboardRepository.findByName(scoreName)
                .orElseThrow(() -> new NotFoundException("Record not found"));
        leaderboardRepository.delete(record);
        redisTemplate.delete(scoreName);
        return StandardResponse.create("200", "Record deleted");
    }

}
