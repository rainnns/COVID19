package domain;

import java.util.ArrayList;
import java.util.List;

/*新的省及城市数据格式*/
public class Cities2 {
    private String province;
    private int confirm_sum;
    private int  cured_sum;
    private int dead_sum;
    private List<Cities2_1> cities;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public List<Cities2_1> getCities() {
        return cities;
    }

    public void setCities(List<Cities2_1> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Cities2{" +
                "province='" + province + '\'' +
                ", confirm_sum=" + confirm_sum +
                ", cured_sum=" + cured_sum +
                ", dead_sum=" + dead_sum +
                ", cities=" + cities +
                '}';
    }
}
