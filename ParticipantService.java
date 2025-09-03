import java.io.*;
import java.util.*;

public class ParticipantService implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Participant> participants = new ArrayList<>();
    private static final String FILE_NAME = "participants.ser";

    // For ID generation
    private int counter = 1;

    private transient List<ServiceListener> listeners = new ArrayList<>();

    public ParticipantService() {
        listeners = new ArrayList<>();
    }

    // Listener
    public void addListener(ServiceListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (ServiceListener l : listeners) {
            l.onDataChanged();
        }
    }

    public String generateId() {
        String id = String.format("P%03d", counter++);
        return id;
    }

    // CRUD
    public void addParticipant(Participant p) {
        participants.add(p);
        saveParticipants();
        notifyListeners();
    }

    public void deleteParticipant(String id) {
        participants.removeIf(p -> p.getId().equals(id));
        saveParticipants();
        notifyListeners();
    }

    public void updateParticipant(String id, String name, String email, String phone, String gender, String dob) {
        for (Participant p : participants) {
            if (p.getId().equals(id)) {
                p.setName(name);
                p.setEmail(email);
                p.setPhone(phone);
                p.setGender(gender);
                p.setDob(dob);
                saveParticipants();
                notifyListeners();
                break;
            }
        }
    }

    public ArrayList<Participant> getAllParticipants() {
        return participants;
    }

    public Participant getParticipantById(String id) {
        for (Participant p : participants)
            if (p.getId().equals(id))
                return p;
        return null;
    }

    public String[] getParticipantNames() {
        return participants.stream().map(Participant::getName).toArray(String[]::new);
    }

    public String getParticipantIdByName(String name) {
        for (Participant p : participants)
            if (p.getName().equals(name))
                return p.getId();
        return null;
    }

    // Persistence
    public void saveParticipants() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(participants);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadParticipants() {
        try {
            File f = new File(FILE_NAME);
            if (f.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                participants = (ArrayList<Participant>) in.readObject();
                in.close();

                int maxId = participants.stream()
                        .map(Participant::getId)
                        .map(id -> id.replaceAll("\\D+", ""))
                        .mapToInt(Integer::parseInt)
                        .max().orElse(0);
                counter = maxId + 1;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}