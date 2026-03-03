import java.io.*;
import java.util.*;

/**
 * Thread-safe, file-backed appointment store.
 * All reads and writes are synchronised so multiple clients
 * can use the server at the same time without corrupting data.
 */
public class AppointmentDatabase {

    private static final String DATA_FILE = "appointments.dat";
    private final List<Appointment> appointments = new ArrayList<>();

    public AppointmentDatabase() {
        load();
    }

    /** Load appointments from the data file into memory. */
    private synchronized void load() {
        appointments.clear();
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Appointment a = Appointment.fromFileLine(line);
                    if (a != null) appointments.add(a);
                }
            }
        } catch (IOException e) {
            // silently continue with empty list - no console output during demo
        }
    }

    /** Persist the current in-memory list to disk. */
    private synchronized void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Appointment a : appointments) {
                pw.println(a.toFileLine());
            }
        } catch (IOException e) {
            // silently ignore - no console output during demo
        }
    }

    /** Add a new appointment and persist. */
    public synchronized void add(Appointment a) {
        appointments.add(a);
        save();
    }

    /** Return all appointments (a snapshot copy). */
    public synchronized List<Appointment> getAll() {
        return new ArrayList<>(appointments);
    }

    /**
     * Search appointments whose date, person or notes contain the query
     * (case-insensitive).
     */
    public synchronized List<Appointment> search(String query) {
        String q = query.toLowerCase();
        List<Appointment> results = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getDate().contains(q)
                    || a.getTime().contains(q)
                    || a.getPerson().toLowerCase().contains(q)
                    || a.getNotes().toLowerCase().contains(q)) {
                results.add(a);
            }
        }
        return results;
    }

    /**
     * Delete the appointment at the given 1-based index
     * (as shown to the user in the list).
     * Returns true on success, false if index is out of range.
     */
    public synchronized boolean delete(int oneBasedIndex) {
        if (oneBasedIndex < 1 || oneBasedIndex > appointments.size()) return false;
        appointments.remove(oneBasedIndex - 1);
        save();
        return true;
    }

    public synchronized int size() {
        return appointments.size();
    }
}