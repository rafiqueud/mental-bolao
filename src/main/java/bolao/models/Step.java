package bolao.models;

import bolao.dao.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Step extends Entity {
    private String name; // ex: Final/Semi-Final
    private String extra1;
    private String extra2;
    private String extra3;
    private String extra4;

    public Step(String name, String extra1, String extra2, String extra3, String extra4) {
        this.name = name;
        this.extra1 = extra1;
        this.extra2 = extra2;
        this.extra3 = extra3;
        this.extra4 = extra4;
    }

    public Step() {
    }

    public String getName() {
        if (name != null) {
            return name.trim();
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra1() {
        if (extra1 != null) {
            return extra1.trim();
        }
        return null;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        if (extra2 != null) {
            return extra2.trim();
        }
        return null;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

}
