package domain;

import java.util.Date;

public class Predict {
    private Date time;
    private int confirm_now;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getConfirm_now() {
        return confirm_now;
    }

    public void setConfirm_now(int confirm_now) {
        this.confirm_now = confirm_now;
    }

    @Override
    public String toString() {
        return "Predict{" +
                "time=" + time +
                ", confirm_now=" + confirm_now +
                '}';
    }
}
