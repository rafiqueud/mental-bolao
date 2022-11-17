package bolao.models;

import bolao.dao.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Objects;
import java.util.TimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match extends Entity {

    private String team1Id;
    private String team2Id;
    private Integer team1Goals;
    private Integer team2Goals;
    private Long matchDate;
    private String stepId;

    public Match(String team1Id, String team2Id, Integer team1Goals, Integer team2Goals, Long matchDate, String stepId) {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.team1Goals = team1Goals;
        this.team2Goals = team2Goals;
        this.matchDate = matchDate;
        this.stepId = stepId;
    }

    public Match() {
    }

    public String getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(String team1Id) {
        this.team1Id = team1Id;
    }

    public String getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(String team2Id) {
        this.team2Id = team2Id;
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

    public Long getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Long matchDate) {
        this.matchDate = matchDate;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String retrieveDate() {
        final var date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
        final var instant = Instant.ofEpochMilli(getMatchDate());
        return date.format(Date.from(instant));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
