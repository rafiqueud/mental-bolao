package bolao.models;

import bolao.dao.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team extends Entity {

    private String name;
    private String flag;
    private String country;
    private Integer cupsWins;
    private String continent;

    public Team() {
    }

    public Team(String name, String flag, String country, Integer cupsWins, String continent) {
        this.name = name;
        this.flag = flag;
        this.country = country;
        this.cupsWins = cupsWins;
        this.continent = continent;
    }

    public String getName() {
        if (name != null) {
            return name.trim();
        }
        return null;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFlag() {
        if (flag != null) {
            return flag.trim();
        }
        return null;
    }

    public void setFlag(final String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        if (country != null) {
            return country.trim();
        }
        return null;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public Integer getCupsWins() {
        return cupsWins;
    }

    public void setCupsWins(final Integer cupsWins) {
        this.cupsWins = cupsWins;
    }

    public String getContinent() {
        if (continent != null) {
            return continent.trim();
        }
        return null;
    }

    public void setContinent(final String continent) {
        this.continent = continent;
    }

}
