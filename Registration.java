import java.io.Serializable;

public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String participantId;
    private String eventId;
    private String eventName;

    public Registration(String id, String participantId, String eventId, String eventName) {
        this.id = id;
        this.participantId = participantId;
        this.eventId = eventId;
        this.eventName = eventName;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getParticipantId() {
        return participantId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    // Setters
    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}