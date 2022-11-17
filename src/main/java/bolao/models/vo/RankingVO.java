package bolao.models.vo;

import java.util.Objects;

public class RankingVO {

    private String username;
    private int points;
    private int perfectMatches;
    private int wins;
    private int errors;

    private int winsPercent;

    public RankingVO(String username, int points, int perfectMatches, int wins, int errors, final int winsPercent) {
        this.username = username;
        this.points = points;
        this.perfectMatches = perfectMatches;
        this.wins = wins;
        this.errors = errors;
        this.winsPercent = winsPercent;
    }

    public RankingVO() {
    }

    public String getUsername() {
        return username.toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPerfectMatches() {
        return perfectMatches;
    }

    public void setPerfectMatches(int perfectMatches) {
        this.perfectMatches = perfectMatches;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getWinsPercent() {
        return winsPercent;
    }

    public void setWinsPercent(int winsPercent) {
        this.winsPercent = winsPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RankingVO rankingVO = (RankingVO) o;
        return Objects.equals(username.toLowerCase(), rankingVO.username.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username.toLowerCase());
    }

}
