package vn.giabaoblog.giabaoblogserver.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.AccessException;
import vn.giabaoblog.giabaoblogserver.config.exception.DuplicateException;
import vn.giabaoblog.giabaoblogserver.config.exception.NotEnoughException;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.Meeting;
import vn.giabaoblog.giabaoblogserver.data.domains.MeetingParticipant;
import vn.giabaoblog.giabaoblogserver.data.domains.MeetingRating;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.mix.MeetingIdDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.request.CreateMeetingRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.RateMeetingRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.MeetingDTO;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.MeetingParticipantDTO;
import vn.giabaoblog.giabaoblogserver.data.enums.MeetingStatus;
import vn.giabaoblog.giabaoblogserver.data.repository.MeetingParticipantRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.MeetingRatingRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.MeetingRepository;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class MeetingService {

    @Autowired
    public final MeetingRepository meetingRepository;

    @Autowired
    public final MeetingParticipantRepository meetingParticipantRepository;

    @Autowired
    public final MeetingRatingRepository meetingRatingRepository;

    public MeetingService(MeetingRepository meetingRepository, MeetingParticipantRepository meetingParticipantRepository, MeetingRatingRepository meetingRatingRepository) {
        this.meetingRepository = meetingRepository;
        this.meetingParticipantRepository = meetingParticipantRepository;
        this.meetingRatingRepository = meetingRatingRepository;
    }

    public MeetingDTO createMeeting(CreateMeetingRequest request) {
        if (request.getTimeStart().before(new Date())) {
            throw new IllegalArgumentException("Time start not valid");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        String username = principal.getUsername();
        Meeting meeting = new Meeting();
        meeting.setMeetingName(request.getMeetingName());
        meeting.setMeetingDescription(request.getMeetingDescription());
        meeting.setHostId(userId);
        meeting.setHostName(username);
        meeting.setMaxParticipant(request.getMaxParticipant());
        meeting.setCountParticipant(0L);
        meeting.setTimeStart(request.getTimeStart());
        meeting.setDuration(request.getDuration());
        meeting.setRatingCount(0L);
        meeting.setStatus(MeetingStatus.SCHEDULED);
        meetingRepository.save(meeting);
        MeetingDTO meetingDTO = new MeetingDTO(meeting);
        return meetingDTO;
    }

    public void cancelMeeting(MeetingIdDTO request) {
        Optional<Meeting> meetingOpt = meetingRepository.findById(request.meetingId);
        if (!meetingOpt.isPresent()) {
            throw new NotFoundException("Meeting not found with this id");
        }
        Meeting meeting = meetingOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        if (!meeting.getHostId().equals(userId)) {
            throw new AccessException("Only host can cancel this meeting");
        }
        meetingRepository.delete(meeting);
    }

    public MeetingParticipantDTO joinMeeting(MeetingIdDTO request) {
        Long meetingId = request.meetingId;
        Optional<Meeting> meetingOpt = meetingRepository.findById(request.meetingId);
        if (!meetingOpt.isPresent()) {
            throw new NotFoundException("Meeting not found with this id");
        }
        Meeting meeting = meetingOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        // find exist meetingId and userId
        Optional<MeetingParticipant> meetingParticipantOpt = meetingParticipantRepository.findByMeetingIdAndParticipantId(meetingId, userId);
        if (meetingParticipantOpt.isPresent()) {
            throw new DuplicateException("You have already joined this meeting");
        }
        // check max participant
        if (meeting.getCountParticipant() >= meeting.getMaxParticipant()) {
            throw new NotEnoughException("This meeting doesn't have any more slot");
        }
        // increase count participant
        meeting.setCountParticipant(meeting.getCountParticipant()+1);
        meetingRepository.save(meeting);
        // add new record
        MeetingParticipant meetingParticipant = new MeetingParticipant();
        meetingParticipant.setMeetingId(meetingId);
        meetingParticipant.setHostId(meeting.getHostId());
        meetingParticipant.setParticipantId(userId);
        meetingParticipantRepository.save(meetingParticipant);
        return new MeetingParticipantDTO(meetingParticipant);
    }

    public void leaveMeeting(MeetingIdDTO request) {
        Long meetingId = request.meetingId;
        Optional<Meeting> meetingOpt = meetingRepository.findById(meetingId);
        if (!meetingOpt.isPresent()) {
            throw new NotFoundException("Meeting not found with this id");
        }
        Meeting meeting = meetingOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        // find exist meetingId and userId
        Optional<MeetingParticipant> meetingParticipantOpt = meetingParticipantRepository.findByMeetingIdAndParticipantId(meetingId, userId);
        if (!meetingParticipantOpt.isPresent()) {
            throw new DuplicateException("You haven't already joined this meeting");
        }
        // delete record
        MeetingParticipant meetingParticipant = meetingParticipantOpt.get();
        meetingParticipantRepository.delete(meetingParticipant);
        // decrease count participant
        meeting.setCountParticipant(meeting.getCountParticipant() - 1);
        meetingRepository.save(meeting);
    }

    public MeetingDTO rateMeeting(RateMeetingRequest request) {
        // check meeting id
        Long meetingId = request.meetingId;
        Optional<Meeting> meetingOpt = meetingRepository.findById(meetingId);
        if (!meetingOpt.isPresent()) {
            throw new NotFoundException("Meeting not found with this id");
        }
        // check join
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();
        Optional<MeetingParticipant> meetingParticipantOpt = meetingParticipantRepository.findByMeetingIdAndParticipantId(meetingId, userId);
        if (!meetingParticipantOpt.isPresent()) {
            throw new DuplicateException("You have to join this meeting to rate");
        }
        // check already rating
        Optional<MeetingRating> meetingRatingOpt = meetingRatingRepository.findByMeetingIdAndUserId(meetingId, userId);
        if (meetingRatingOpt.isPresent()) {
            throw new DuplicateException("You can only rate this meeting once");
        }
        // check finish

        // create record
        Long userRating = request.getRating();
        MeetingRating meetingRating = new MeetingRating();
        meetingRating.setMeetingId(meetingId);
        meetingRating.setUserId(userId);
        meetingRating.setRating(userRating);
        meetingRating.setComment(request.getComment());
        meetingRatingRepository.save(meetingRating);
        // change average rating
        Meeting meeting = meetingOpt.get();
        Double currentRating = meeting.getRating();
        Long currentRatingCount = meeting.getRatingCount();
        // cong thuc thay doi avg.rating
        Double newRating = (currentRating * currentRatingCount + userRating) / (++currentRatingCount);
        meeting.setRatingCount(currentRatingCount);
        meeting.setRating(newRating);
        meetingRepository.save(meeting);
        // return DTO
        return new MeetingDTO(meeting);
    }


}
