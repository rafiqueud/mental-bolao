package bolao.models;

import bolao.dao.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Entity {

    private String username;
    private String password;

    private List<Bet> bets;

    public User() {
    }

    public User(String username, String password, List<Bet> bets) {
        this.username = username;
        this.password = password;
        this.bets = bets;
    }

    public String getUsername() {
        return username.toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Bet> getBets() {
        if (bets == null) {
            bets = new ArrayList<>();
        }
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }
}
