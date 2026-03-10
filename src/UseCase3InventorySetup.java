import java.util.HashMap;
import java.util.Map;

/**
 * ========================================================
 * CLASS - RoomInventory
 * ========================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class acts as the single source of truth
 * for room availability in the hotel.
 *
 * Room pricing and characteristics are obtained
 * from Room objects, not duplicated here.
 *
 * This avoids multiple sources of truth and
 * keeps responsibilities clearly separated.
 *
 * @version 3.1
 */
class RoomInventory {

    /**
     * Stores available room count for each room type.
     *
     * Key   -> Room type name
     * Value -> Available room count
     */
    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes the inventory
     * with default availability values.
     */
    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    /**
     * Initializes room availability data.
     *
     * This method centralizes inventory setup
     * instead of using scattered variables.
     */
    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite",  2);
    }

    /**
     * Returns the current availability map.
     *
     * @return map of room type to available count
     */
    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    /**
     * Updates availability for a specific room type.
     *
     * @param roomType the room type to update
     * @param count    new availability count
     */
    public void updateAvailability(String roomType, int count) { roomAvailability.put(roomType, count); }
}

/**
 * ========================================================
 * MAIN CLASS - UseCase3InventorySetup
 * ========================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class demonstrates how room availability
 * is managed using a centralized inventory.
 *
 * Room objects are used to retrieve pricing
 * and room characteristics.
 *
 * No booking or search logic is introduced here.
 *
 * @version 3.1
 */
public class UseCase3InventorySetup {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Initialize room domain objects
        // (Room, SingleRoom, DoubleRoom, SuiteRoom defined below)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom  = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display header
        System.out.println("Hotel Room Inventory Status");

        // Display Single Room details + availability from inventory
        System.out.println();
        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getRoomAvailability().get("Single"));

        // Display Double Room details + availability from inventory
        System.out.println();
        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getRoomAvailability().get("Double"));

        // Display Suite Room details + availability from inventory
        System.out.println();
        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getRoomAvailability().get("Suite"));
    }
}

// ========================================================
// ABSTRACT CLASS - Room
// ========================================================
//
// Use Case 2 (reused): Basic Room Types & Static Availability
//
// @version 2.1
//
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
// CLASS - SuiteRoom    @version 2.1
// ========================================================
class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 750, 5000.0); }
}
