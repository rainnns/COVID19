package domain;
/*新的省及城市数据格式*/
public class Cities2_1 {
    private String name;
    private int confirm_sum;
    private int  cured_sum;
    private int dead_sum;
    private int latitude;
    private int longitude;

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

    public int getCured_sum() {
        return cured_sum;
    }

    public void setCured_sum(int cured_sum) {
        this.cured_sum = cured_sum;
    }

    public int getDead_sum() {
        return dead_sum;
    }

    public void setDead_sum(int dead_sum) {
        this.dead_sum = dead_sum;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Cities2_1{" +
                "name='" + name + '\'' +
                ", confirm_sum=" + confirm_sum +
                ", cured_sum=" + cured_sum +
                ", dead_sum=" + dead_sum +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
