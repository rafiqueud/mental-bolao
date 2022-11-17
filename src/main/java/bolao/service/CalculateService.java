package bolao.service;

import bolao.models.Bet;
import bolao.models.Match;
import bolao.models.User;
import bolao.models.vo.RankingVO;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.bson.types.ObjectId;

public abstract class CalculateService {

    public static List<RankingVO> getRankings(final List<Match> matches, final List<User> users) {
        final var rankingVOS = new LinkedHashSet<RankingVO>();

        users.forEach(user -> {
            final var points = new AtomicInteger(0);
            final var perfectMatches = new AtomicInteger(0);
            final var wins = new AtomicInteger(0);
            final var erros = new AtomicInteger(0);

            for (final var bet : user.getBets()) {
                final var match = findMatch(bet.getMatchId(), matches);
                if (match == null) {
                    continue;
                }
                final var count = contabilize(bet, match);

                if (count == 3) {
                    perfectMatches.getAndIncrement();
                } else if (count == 1) {
                    wins.getAndIncrement();
                } else {
                    erros.getAndIncrement();
                }
                points.addAndGet(count);
            }

            rankingVOS.add(
                    new RankingVO(
                            user.getUsername(),
                            points.get(),
                            perfectMatches.get(),
                            wins.get(),
                            erros.get(),
                            calculateWinPercentual(perfectMatches, wins, erros)
                    )
            );
        });

        return rankingVOS.stream().sorted(Comparator.comparing(RankingVO::getPoints)).toList();

    }

    private static int calculateWinPercentual(final AtomicInteger perfectMatches, final AtomicInteger wins, final AtomicInteger errors) {
        final var total = perfectMatches.get() + wins.get() + errors.get();
        final var acceptances = perfectMatches.get() + wins.get();
        final var toPercent = acceptances * 100.0;
        if (toPercent <= 0.0) {
            return 0;
        }
        return Double.valueOf(toPercent / total).intValue();
    }

    public static int contabilize(final Bet bet, final Match match) {

        if (match.getTeam2Goals() == null || match.getTeam1Goals() == null) {
            return 0;
        }

        if (bet == null || bet.getTeam1Goals() == null && bet.getTeam2Goals() == null) {
            return 0;
        }

        // Assertou o placar, ganha 3 pontos.
        if (Objects.equals(bet.getTeam1Goals(), match.getTeam1Goals())
                && Objects.equals(bet.getTeam2Goals(), match.getTeam2Goals())) {
            return 3;
        }

        // assertou que o time1 ganha do time2, ganha 1 ponto.
        if ((bet.getTeam1Goals() > bet.getTeam2Goals())
                &&
                (match.getTeam1Goals() > match.getTeam2Goals())) {
            return 1;
        }

        // assertou que o time2 ganha do time 1, ganha 1 ponto
        if ((bet.getTeam2Goals() > bet.getTeam1Goals())
                &&
                (match.getTeam2Goals() > bet.getTeam2Goals())) {
            return 1;
        }

        return 0;
    }

    private static Match findMatch(final String matchId, final List<Match> matches) {

        return matches.stream().filter(match -> match.getId().equals(new ObjectId(matchId)))
                .findFirst()
                .filter(match -> match.getTeam1Goals() != null && match.getTeam2Goals() != null)
                .orElse(null);
    }

}
