package school.hei.haapi.service;

import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.BoundedPageSize;
import school.hei.haapi.model.EventParticipant;
import school.hei.haapi.model.PageFromOne;
import school.hei.haapi.repository.EventParticipantRepository;
import school.hei.haapi.service.AWS.RekognitionService;

@Service
@AllArgsConstructor
public class EventParticipantService {
    private final EventParticipantRepository eventParticipantRepository;
    private final RekognitionService rekognitionService;

    public List<EventParticipant> getAll(PageFromOne page,
                                         BoundedPageSize pageSize, String eventId, String ref,
                                         String status) {
        Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
        return eventParticipantRepository.findByEvent_IdContainingIgnoreCaseAndIdContainingIgnoreCaseAndStatus(eventId,
                ref,
                EventParticipant.StatusEnum.valueOf(status), pageable);
    }

    public EventParticipant getById(String participantId) {
        return eventParticipantRepository.getById(participantId);
    }

    public List<EventParticipant> saveAll(List<EventParticipant> toCreate) {
        return eventParticipantRepository.saveAll(toCreate);
    }

    @Transactional
    public String checkAttendance(String event_id, byte[] toCompare) {
        List<EventParticipant> eventParticipantList =
                getAll(new PageFromOne(1), new BoundedPageSize(500), event_id, "",
                        EventParticipant.StatusEnum.EXPECTED.name());

        for (EventParticipant participant : eventParticipantList) {
            if (rekognitionService.compareFaces(toCompare, participant.getUser().getPicture())
                    .getFaceMatches().get(0).getSimilarity() >=
                    RekognitionService.SIMILARITY_THRESHOLD) {
                participant.setStatus(EventParticipant.StatusEnum.HERE);
            }
            participant.setStatus(EventParticipant.StatusEnum.MISSING);
        }
        return "" ;
    }
}
