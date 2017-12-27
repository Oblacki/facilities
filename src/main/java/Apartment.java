import java.util.List;

/**
 * @author Matej Dolenc
 */
public class Apartment {

    private String id;
    private int numOfBeds;
    private String customerId;
    private List<String> guestIds;
    private double pricePerNight;
    private List<String> facilities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumOfBeds() {
        return numOfBeds;
    }

    public void setNumOfBeds(int numOfBeds) {
        this.numOfBeds = numOfBeds;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getGuestIds() {
        return guestIds;
    }

    public void setGuestIds(List<String> guestIds) {
        this.guestIds = guestIds;
    }

    public void setPricePerNight (double price) { this.pricePerNight = price; }

    public double getPricePerNight() { return this.pricePerNight; }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

}
