package domain;

public class HotMap {
    private String name;
    private int confirm_sum;
    private int confirm_add;
    private int confirm_now;
    private double longitude;
    private double latitude;

    public HotMap() {
    }

    public HotMap(String name, int confirm_sum, int confirm_add, int confirm_now, double longitude, double latitude) {
        this.name = name;
        this.confirm_sum = confirm_sum;
        this.confirm_add = confirm_add;
        this.confirm_now = confirm_now;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfirm_sum() {
        return confirm_sum;
    }

    public void setConfirm_sum(int confirm_sum) {
        this.confirm_sum = confirm_sum;
    }

    public int getConfirm_add() {
        return confirm_add;
    }

    public void setConfirm_add(int confirm_add) {
        this.confirm_add = confirm_add;
    }

    public int getConfirm_now() {
        return confirm_now;
    }

    public void setConfirm_now(int confirm_now) {
        this.confirm_now = confirm_now;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "HotMap{" +
                "name='" + name + '\'' +
                ", confirm_sum=" + confirm_sum +
                ", confirm_add=" + confirm_add +
                ", confirm_now=" + confirm_now +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
