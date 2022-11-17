package bolao.models;

import java.util.Objects;

public class Bet {

    private String matchId;
    private Integer team1Goals;
    private Integer team2Goals;


    public Bet(String matchId, Integer team1Goals, Integer team2Goals) {
        this.matchId = matchId;
        this.team1Goals = team1Goals;
        this.team2Goals = team2Goals;
    }

    public Bet() {
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Integer getTeam1Goals() {
        return team1Goals;
    }

    public void setTeam1Goals(Integer team1Goals) {
        this.team1Goals = team1Goals;
    }

    public Integer getTeam2Goals() {
        return team2Goals;
    }

    public void setTeam2Goals(Integer team2Goals) {
        this.team2Goals = team2Goals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return Objects.equals(matchId, bet.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId);
    }
}
