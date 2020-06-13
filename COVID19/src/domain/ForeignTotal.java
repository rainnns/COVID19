package domain;

public class ForeignTotal {
    private String name;
    private int confirm_now;
    private int confirm_sum;
    private int cured_sum;
    private int dead_sum;

    private int confirm_now_add;
    private int confirm_add;
    private int cured_add;
    private int dead_add;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfirm_now() {
        return confirm_now;
    }

    public void setConfirm_now(int confirm_now) {
        this.confirm_now = confirm_now;
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

    public int getConfirm_now_add() {
        return confirm_now_add;
    }

    public void setConfirm_now_add(int confirm_now_add) {
        this.confirm_now_add = confirm_now_add;
    }

    public int getConfirm_add() {
        return confirm_add;
    }

    public void setConfirm_add(int confirm_add) {
        this.confirm_add = confirm_add;
    }

    public int getCured_add() {
        return cured_add;
    }

    public void setCured_add(int cured_add) {
        this.cured_add = cured_add;
    }

    public int getDead_add() {
        return dead_add;
    }

    public void setDead_add(int dead_add) {
        this.dead_add = dead_add;
    }

    @Override
    public String toString() {
        return "ForeignTotal{" +
                "name='" + name + '\'' +
                ", confirm_now=" + confirm_now +
                ", confirm_sum=" + confirm_sum +
                ", cured_sum=" + cured_sum +
                ", dead_sum=" + dead_sum +
                ", confirm_now_add=" + confirm_now_add +
                ", confirm_add=" + confirm_add +
                ", cured_add=" + cured_add +
                ", dead_add=" + dead_add +
                '}';
    }
}
