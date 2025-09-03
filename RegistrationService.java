import java.io.*;
import java.util.*;

public class RegistrationService implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Registration> registrations = new ArrayList<>();
    private static final String FILE_NAME = "registrations.ser";

    private int counter = 0;

    private transient List<ServiceListener> listeners = new ArrayList<>();

    public void addListener(ServiceListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (ServiceListener l : listeners)
            l.onDataChanged();
    }

    public String peekNextId() {
        return String.format("R%03d", counter);
    }

    public String generateId() {
        String id = String.format("R%03d", counter++);
        return id;
    }

    public void addRegistration(Registration r) {
        registrations.add(r);
        saveRegistrations();
        notifyListeners();
    }

    public void deleteRegistration(String id) {
        registrations.removeIf(r -> r.getId().equals(id));
        saveRegistrations();
        notifyListeners();
    }

    public ArrayList<Registration> getAllRegistrations() {
        return registrations;
    }

    public void saveRegistrations() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(registrations);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadRegistrations() {
        try {
            File f = new File(FILE_NAME);
            if (f.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                registrations = (ArrayList<Registration>) in.readObject();
                in.close();

                int maxId = registrations.stream()
                        .map(Registration::getId)
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