package bolao.models.vo;

import bolao.models.Bet;
import bolao.models.Match;
import bolao.models.Step;
import bolao.models.Team;
import bolao.service.CalculateService;
import java.util.List;

public class BetVO {

    private String matchId;

    private Long matchDateMillis;

    private String matchDate;
    private String team1Name;

    private String team1Flag;

    private String team2Name;

    private String team2Flag;

    private String stepName;
    private Integer team1Goals;
    private Integer team2Goals;
    private Integer betTeam1Goals;
    private Integer betTeam2Goals;
    private Integer points;


    public BetVO(final List<Bet> bets, final Match match, final List<Team> teams, final List<Step> steps) {
        Bet bet = new Bet();
        if (bets != null && !bets.isEmpty()) {
            bet = bets.stream().filter(b -> b.getMatchId().equals(match.getId().toString())).findFirst()
                    .orElse(bet);
        }
        for (final var team : teams) {
            if (match.getTeam1Id().equals(team.getId().toString())) {
                team1Name = team.getName();
                team1Flag = team.getFlag();
                team1Goals = match.getTeam1Goals();
            }
            if (match.getTeam2Id().equals(team.getId().toString())) {
                team2Name = team.getName();
                team2Flag = team.getFlag();
                team2Goals = match.getTeam2Goals();
            }
            if (team1Name != null && team2Name != null) {
                break;
            }
        }

        for (final var step : steps) {
            if (match.getStepId().equals(step.getId().toString())) {
                stepName = step.getName();
                break;
            }
        }

        points = CalculateService.contabilize(bet, match);
        betTeam1Goals = bet.getTeam1Goals();
        betTeam2Goals = bet.getTeam2Goals();

        matchDate = match.retrieveDate();
        matchId = match.getId().toString();

        matchDateMillis = match.getMatchDate();

    }

    public BetVO() {
    }

    public BetVO(String matchDate, String team1Name, String team2Name, String stepName, Integer team1Goals, Integer team2Goals, Integer betTeam1Goals, Integer betTeam2Goals, Integer points) {
        this.matchDate = matchDate;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.stepName = stepName;
        this.team1Goals = team1Goals;
        this.team2Goals = team2Goals;
        this.betTeam1Goals = betTeam1Goals;
        this.betTeam2Goals = betTeam2Goals;
        this.points = points;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
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

    public Integer getBetTeam1Goals() {
        return betTeam1Goals;
    }

    public void setBetTeam1Goals(Integer betTeam1Goals) {
        this.betTeam1Goals = betTeam1Goals;
    }

    public Integer getBetTeam2Goals() {
        return betTeam2Goals;
    }

    public void setBetTeam2Goals(Integer betTeam2Goals) {
        this.betTeam2Goals = betTeam2Goals;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getMatchId() {
        return matchId;
    }


    public Long getMatchDateMillis() {
        return matchDateMillis;
    }

    public void setMatchDateMillis(Long matchDateMillis) {
        this.matchDateMillis = matchDateMillis;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getTeam1Flag() {
        return team1Flag;
    }

    public void setTeam1Flag(String team1Flag) {
        this.team1Flag = team1Flag;
    }

    public String getTeam2Flag() {
        return team2Flag;
    }

    public void setTeam2Flag(String team2Flag) {
        this.team2Flag = team2Flag;
    }
}
