import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * ========================================================
 * CLASS - RoomAllocationService
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class is responsible for confirming
 * booking requests and assigning rooms.
 *
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 *
 * @version 6.0
 */
class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRoomIds;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /**
     * Initializes allocation tracking structures.
     */
    public RoomAllocationService() {
        allocatedRoomIds    = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * a unique room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory   centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Check if the requested room type has availability
        if (!availability.containsKey(roomType) || availability.get(roomType) <= 0) {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName()
                    + " - No availability for room type: " + roomType);
            return;
        }

        // Generate a unique room ID for this room type
        String roomId = generateRoomId(roomType);

        // Register the room ID globally to prevent reuse
        allocatedRoomIds.add(roomId);

        // Track the assignment under its room type
        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Decrement inventory immediately after allocation
        inventory.updateAvailability(roomType, availability.get(roomType) - 1);

        // Confirm the booking
        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        // Count how many rooms of this type have already been assigned
        int count = assignedRoomsByType.containsKey(roomType)
                ? assignedRoomsByType.get(roomType).size() + 1
                : 1;
        return roomType + "-" + count;
    }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase6RoomAllocation
 * ========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class demonstrates how booking
 * requests are confirmed and rooms
 * are allocated safely.
 *
 * It consumes booking requests in FIFO
 * order and updates inventory immediately.
 *
 * @version 6.0
 */
public class UseCase6RoomAllocation {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Display application header
        System.out.println("Room Allocation Processing");

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create and enqueue booking requests (FIFO order)
        bookingQueue.addRequest(new Reservation("Abhi",     "Single"));
        bookingQueue.addRequest(new Reservation("Subha",    "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Initialize room allocation service
        RoomAllocationService allocationService = new RoomAllocationService();

        // Process each booking request in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation current = bookingQueue.getNextRequest();
            allocationService.allocateRoom(current, inventory);
        }
    }
}

// ========================================================
// ABSTRACT CLASS - Room   @version 2.1
// ========================================================
abstract class Room {

    protected int    numberOfBeds;
    protected int    squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds  = numberOfBeds;
        this.squareFeet    = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: "            + numberOfBeds);
        System.out.println("Size: "            + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

// ========================================================
// CLASS - SingleRoom   @version 2.1
// ========================================================
class SingleRoom extends Room {
    public SingleRoom() { super(1, 250, 1500.0); }
}

// ========================================================
// CLASS - DoubleRoom   @version 2.1
// ========================================================
class DoubleRoom extends Room {
    public DoubleRoom() { super(2, 400, 2500.0); }
}

// ========================================================
// CLASS - SuiteRoom   @version 2.1
// ========================================================
class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 750, 5000.0); }
}

// ========================================================
// CLASS - RoomInventory   @version 3.1
// ========================================================
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite",  2);
    }

    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    public void updateAvailability(String roomType, int count) { roomAvailability.put(roomType, count); }
}

// ========================================================
// CLASS - Reservation   @version 5.0
// ========================================================
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType  = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType()  { return roomType;  }
}

// ========================================================
// CLASS - BookingRequestQueue   @version 5.0
// ========================================================
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() { requestQueue = new LinkedList<>(); }

    public void addRequest(Reservation reservation)  { requestQueue.offer(reservation); }
    public Reservation getNextRequest()              { return requestQueue.poll();       }
    public boolean hasPendingRequests()              { return !requestQueue.isEmpty();   }
}
