import java.io.*;
import java.util.*;

public class EventService implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Event> events = new ArrayList<>();
    private static final String FILE_NAME = "events.ser";

    // For ID generation
    private int counter = 1;

    private transient List<ServiceListener> listeners = new ArrayList<>();

    public EventService() {
        listeners = new ArrayList<>();
    }

    // Listener management
    public void addListener(ServiceListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (ServiceListener l : listeners) {
            l.onDataChanged();
        }
    }

    // ===== ID Generator =====
    public String generateId() {
        String id = String.format("E%03d", counter++);
        return id;
    }

    // CRUD
    public void addEvent(Event e) {
        events.add(e);
        saveEvents();
        notifyListeners();
    }

    public void deleteEvent(String id) {
        events.removeIf(event -> event.getId().equals(id));
        saveEvents();
        notifyListeners();
    }

    public ArrayList<Event> getAllEvents() {
        return events;
    }

    public Event getEventById(String id) {
        for (Event e : events)
            if (e.getId().equals(id))
                return e;
        return null;
    }

    public Event getEventByName(String name) {
        for (Event e : events)
            if (e.getName().equalsIgnoreCase(name))
                return e;
        return null;
    }

    public String[] getEventNames() {
        return events.stream().map(Event::getName).toArray(String[]::new);
    }

    public String getEventIdByName(String name) {
        for (Event e : events)
            if (e.getName().equals(name))
                return e.getId();
        return null;
    }

    // Persistence
    public void saveEvents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(events);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadEvents() {
        try {
            File f = new File(FILE_NAME);
            if (f.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                events = (ArrayList<Event>) in.readObject();
                in.close();

                int maxId = events.stream()
                        .map(Event::getId)
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