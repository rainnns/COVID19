package domain;

import java.util.Date;

public class GlobalCountry_19 {
    private String name;
    private Date time;
    private int confirm_sum;
    private int confirm_add;
    private int confirm_now;

    public GlobalCountry_19() {
    }

    public GlobalCountry_19(String name, Date time, int confirm_sum, int confirm_add, int confirm_now) {
        this.name = name;
        this.time = time;
        this.confirm_sum = confirm_sum;
        this.confirm_add = confirm_add;
        this.confirm_now = confirm_now;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "GlobalCountry_19{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", confirm_sum=" + confirm_sum +
                ", confirm_add=" + confirm_add +
                ", confirm_now=" + confirm_now +
                '}';
    }
}




