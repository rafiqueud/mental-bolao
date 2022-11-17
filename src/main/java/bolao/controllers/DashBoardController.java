package bolao.controllers;

import bolao.dao.MongoHelper;
import bolao.models.Bet;
import bolao.models.Match;
import bolao.models.Step;
import bolao.models.Team;
import bolao.models.User;
import bolao.models.vo.BetVO;
import bolao.models.vo.MatchVO;
import bolao.repository.Repository;
import bolao.service.CalculateService;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.bson.types.ObjectId;

import static bolao.repository.Repository.engine;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class DashBoardController {

    private static final List<String> admins = List.of(
            "rafael.argenta@contaquanto.com.br",
            "kathia.oliveira@contaquanto.com.br"
    );

    public static void handleRoutes(final Router router) {
        router.get("/dashboard").handler(DashBoardController::renderDashboardPage);

        //Teams
        router.get("/dashboard/teams").handler(DashBoardController::renderDashboardTeamsPage);
        router.post("/dashboard/teams").handler(DashBoardController::renderDashboardTeamsPage);
        router.get("/dashboard/teams/:id").handler(DashBoardController::renderDashboardTeamPage);

        //Step
        router.get("/dashboard/steps").handler(DashBoardController::renderDashboardStepsPage);
        router.post("/dashboard/steps").handler(DashBoardController::renderDashboardStepsPage);
        router.get("/dashboard/steps/:id").handler(DashBoardController::renderDashboardStepPage);

        //Step
        router.get("/dashboard/matches").handler(DashBoardController::renderDashboardMatchesPage);
        router.post("/dashboard/matches").handler(DashBoardController::renderDashboardMatchesPage);
        router.get("/dashboard/matches/:id").handler(DashBoardController::renderDashboardMatchPage);

        //Bet
        router.get("/dashboard/bets").handler(DashBoardController::renderDashboardBetsPage);
        router.post("/dashboard/bets").handler(DashBoardController::renderDashboardBetsPage);
        router.get("/dashboard/bet/:id").handler(DashBoardController::renderDashboardBetPage);
    }

    public static void renderDashboardPage(final RoutingContext rctx) {
        Repository.mongoDaoReactive.whatDoWithAllThatList(Match.class, matchMongoResult ->
                MongoHelper.getMappedFlowabe(matchMongoResult, Match.class)
                        .toList()
                        .subscribe(matches -> Repository.mongoDaoReactive.whatDoWithAllThatList(User.class, userMongoResult -> {
                                    MongoHelper.getMappedFlowabe(userMongoResult, User.class)
                                            .toList()
                                            .subscribe(users -> {
                                                final var rankings = CalculateService.getRankings(matches, users);
                                                rctx.put("rankings", rankings);
                                                engine.render(rctx.data(), "templates/dashboard/index.peb", res -> rctx.response().end(res.result()));
                                            });
                                })
                        )
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void renderDashboardTeamsPage(final RoutingContext rctx) {


        saveTeam(rctx);

        Repository.mongoDaoReactive.whatDoWithAllThatList(Team.class, matchMongoResult ->
                MongoHelper.getMappedFlowabe(matchMongoResult, Team.class)
                        .toList()
                        .subscribe(teams -> {
                                    rctx.put("teams", teams);
                                    engine.render(rctx.data(), "templates/dashboard/teams.peb", res -> rctx.response().end(res.result()));
                                }
                        ));
    }


    private static void renderDashboardTeamPage(final RoutingContext rctx) {
        final var id = rctx.request().getParam("id");
        Repository.mongoDaoReactive.whatDoWithThatList(Team.class, new JsonObject().put("_id", id), matchMongoResult ->
                MongoHelper.getMappedFlowabe(matchMongoResult, Team.class)
                        .toList()
                        .subscribe(teams -> {
                                    if (teams != null && !teams.isEmpty()) {
                                        rctx.put("team", teams.get(0));
                                    }
                                    engine.render(rctx.data(), "templates/dashboard/team.peb", res -> rctx.response().end(res.result()));
                                }
                        ));
    }

    private static void renderDashboardStepsPage(final RoutingContext rctx) {

        saveStep(rctx);

        Repository.mongoDaoReactive.whatDoWithAllThatList(Step.class, matchMongoResult ->
                MongoHelper.getMappedFlowabe(matchMongoResult, Step.class)
                        .toList()
                        .subscribe(steps -> {
                                    rctx.put("steps", steps);
                                    engine.render(rctx.data(), "templates/dashboard/steps.peb", res -> rctx.response().end(res.result()));
                                }
                        ));
    }

    private static void renderDashboardMatchesPage(final RoutingContext rctx) {

        saveMatch(rctx);

        Repository.mongoDaoReactive.whatDoWithAllThatList(Match.class, matchMongoResult ->
                MongoHelper.getMappedFlowabe(matchMongoResult, Match.class)
                        .toList()
                        .subscribe(matches -> {
                                    Repository.mongoDaoReactive.whatDoWithAllThatList(Team.class, teamMongoResult -> {
                                        MongoHelper.getMappedFlowabe(teamMongoResult, Team.class)
                                                .toList()
                                                .subscribe(teams -> {
                                                    Repository.mongoDaoReactive.whatDoWithAllThatList(Step.class, stepMongoResult -> {
                                                        MongoHelper.getMappedFlowabe(stepMongoResult, Step.class)
                                                                .toList()
                                                                .subscribe(steps -> {
                                                                    final var matchVOS = getMatches(steps, teams, matches);
                                                                    rctx.put("matches", matchVOS);
                                                                    engine.render(rctx.data(), "templates/dashboard/matches.peb", res -> rctx.response().end(res.result()));
                                                                });

                                                    });
                                                });
                                    });
                                }
                        ));

    }

    private static void renderDashboardBetsPage(final RoutingContext rctx) {

        final var principal = rctx.user().principal();
        final var userQuery = new JsonObject()
                .put("_id", principal.getString("_id"));

        final var promiseSave = saveBet(rctx);

        promiseSave.future().onComplete(event ->
                Repository.mongoDaoReactive.whatDoWithThatList(User.class, userQuery, userMongoResult ->
                        MongoHelper.getMappedFlowabe(userMongoResult, User.class)
                                .toList()
                                .subscribe(users -> {
                                            final var bets = users.get(0).getBets();
                                            Repository.mongoDaoReactive.whatDoWithAllThatList(Match.class, matchMongoResult -> {
                                                MongoHelper.getMappedFlowabe(matchMongoResult, Match.class)
                                                        .toList()
                                                        .subscribe(matches -> {
                                                            Repository.mongoDaoReactive.whatDoWithAllThatList(Step.class, stepMongoResult -> {
                                                                MongoHelper.getMappedFlowabe(stepMongoResult, Step.class)
                                                                        .toList()
                                                                        .subscribe(steps -> {
                                                                            Repository.mongoDaoReactive.whatDoWithAllThatList(Team.class, teamMongoResult -> {
                                                                                MongoHelper.getMappedFlowabe(teamMongoResult, Team.class)
                                                                                        .toList()
                                                                                        .subscribe(teams -> {
                                                                                            final var betsVO = matches.stream().map(match -> new BetVO(bets, match, teams, steps)).toList();
                                                                                            rctx.put("bets", betsVO);
                                                                                            engine.render(rctx.data(), "templates/dashboard/bets.peb", res -> rctx.response().end(res.result()));
                                                                                        });
                                                                            });
                                                                        });
                                                            });
                                                        });
                                            });
                                        }
                                )));
    }

    private static List<MatchVO> getMatches(final List<Step> steps, final List<Team> teams, final List<Match> matches) {
        final var matchVOS = new ArrayList<MatchVO>();
        for (final var match : matches) {
            final var matchVO = new MatchVO();
            matchVO.setMatchDate(match.getMatchDate().toString());
            matchVO.setMatchId(match.getId().toString());
            matchVO.setDate(getDate(match.getMatchDate()));
            matchVO.setStep(getStepName(match.getStepId(), steps));
            matchVO.setTeam1(getTeamName(match.getTeam1Id(), teams));
            matchVO.setTeam1Flag(getTeamFlag(match.getTeam1Id(), teams));
            matchVO.setTeam2(getTeamName(match.getTeam2Id(), teams));
            matchVO.setTeam2Flag(getTeamFlag(match.getTeam1Id(), teams));
            matchVO.setGoalsTeam1(match.getTeam1Goals());
            matchVO.setGoalsTeam2(match.getTeam2Goals());
            matchVO.setStep(getStepName(match.getStepId(), steps));
            matchVOS.add(matchVO);
        }
        return matchVOS;
    }

    private static String getDate(final Long matchDate) {
        final var instant = Instant.ofEpochMilli(matchDate);

        final var date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
        return date.format(Date.from(instant));
    }

    private static String getTeamName(final String teamId, final List<Team> teams) {
        return teams.stream().filter(team -> team.getId().toString().equals(teamId))
                .map(Team::getName)
                .findFirst().orElse(null);
    }

    private static String getTeamFlag(final String teamId, final List<Team> teams) {
        return teams.stream().filter(team -> team.getId().toString().equals(teamId))
                .map(Team::getFlag)
                .findFirst().orElse(null);
    }

    private static String getStepName(final String stepId, final List<Step> steps) {
        return steps.stream().filter(step -> step.getId().toString().equals(stepId))
                .map(Step::getName)
                .findFirst().orElse(null);
    }


    private static void renderDashboardStepPage(final RoutingContext rctx) {
        final var id = rctx.request().getParam("id");
        Repository.mongoDaoReactive.whatDoWithThatList(Step.class, new JsonObject().put("_id", id), stepMongoResult ->
                MongoHelper.getMappedFlowabe(stepMongoResult, Step.class)
                        .toList()
                        .subscribe(steps -> {
                                    if (steps != null && !steps.isEmpty()) {
                                        rctx.put("step", steps.get(0));
                                    }
                                    engine.render(rctx.data(), "templates/dashboard/step.peb", res -> rctx.response().end(res.result()));
                                }
                        ));
    }

    private static void renderDashboardMatchPage(final RoutingContext rctx) {
        final var id = rctx.request().getParam("id");
        Repository.mongoDaoReactive.whatDoWithThatList(Match.class, new JsonObject().put("_id", id), matchMongoResult ->
                MongoHelper.getMappedFlowabe(matchMongoResult, Match.class)
                        .toList()
                        .subscribe(matches -> {
                                    if (matches != null && !matches.isEmpty()) {
                                        rctx.put("match", matches.get(0));
                                    }
                                    Repository.mongoDaoReactive.whatDoWithAllThatList(Team.class, teamMongoResult -> {
                                        MongoHelper.getMappedFlowabe(teamMongoResult, Team.class)
                                                .toList()
                                                .subscribe(teams -> {
                                                    rctx.put("teams", teams);
                                                    Repository.mongoDaoReactive.whatDoWithAllThatList(Step.class, stepMongoResult -> {
                                                        MongoHelper.getMappedFlowabe(stepMongoResult, Step.class)
                                                                .toList()
                                                                .subscribe(steps -> {
                                                                    rctx.put("steps", steps);
                                                                    engine.render(rctx.data(), "templates/dashboard/match.peb", res -> rctx.response().end(res.result()));
                                                                });
                                                    });
                                                });
                                    });
                                }
                        ));
    }

    private static void renderDashboardBetPage(final RoutingContext rctx) {
        final var id = rctx.request().getParam("id");
        final var principal = rctx.user().principal();
        final var userQuery = new JsonObject()
                .put("_id", principal.getString("_id"));
        Repository.mongoDaoReactive.whatDoWithThatList(User.class, userQuery, userMongoResult ->
                MongoHelper.getMappedFlowabe(userMongoResult, User.class)
                        .toList()
                        .subscribe(users -> {
                            Repository.mongoDaoReactive.whatDoWithThatList(Match.class, new JsonObject().put("_id", id), matchMongoResult ->
                                    MongoHelper.getMappedFlowabe(matchMongoResult, Match.class)
                                            .toList()
                                            .subscribe(matches -> {
                                                        final var match = matches.get(0);
                                                        Repository.mongoDaoReactive.whatDoWithAllThatList(Team.class, teamMongoResult -> {
                                                            MongoHelper.getMappedFlowabe(teamMongoResult, Team.class)
                                                                    .toList()
                                                                    .subscribe(teams -> {
                                                                        Repository.mongoDaoReactive.whatDoWithAllThatList(Step.class, stepMongoResult -> {
                                                                            MongoHelper.getMappedFlowabe(stepMongoResult, Step.class)
                                                                                    .toList()
                                                                                    .subscribe(steps -> {
                                                                                        rctx.put("matchDate", match.retrieveDate());
                                                                                        rctx.put("team1Name", getTeamName(teams, match.getTeam1Id()));
                                                                                        rctx.put("team2Name", getTeamName(teams, match.getTeam2Id()));
                                                                                        rctx.put("step", getStepName(steps, match.getStepId()));
                                                                                        rctx.put("bet", getBetFromUser(users.get(0), match.getId().toString()));
                                                                                        rctx.put("available", isBetAvailable(match));
                                                                                        engine.render(rctx.data(), "templates/dashboard/bet.peb", res -> rctx.response().end(res.result()));
                                                                                    });
                                                                        });
                                                                    });
                                                        });
                                                    }
                                            ));
                        }));
    }

    public static boolean isBetAvailable(final Match match) {
        final var now = Instant.now().atOffset(ZoneOffset.ofHours(-3)).toInstant();
        final var matchDate = match.getMatchDate();
        final var matchDateInstant = Instant.ofEpochMilli(matchDate);

        final var duration = Duration.between(now, matchDateInstant);
        return duration.getSeconds() > 3600;
    }

    private static Bet getBetFromUser(final User user, final String matchId) {
        for (Bet bet : user.getBets()) {
            if (bet.getMatchId().equals(matchId)) {
                return bet;
            }
        }
        final var newBet = new Bet();
        newBet.setMatchId(matchId);
        return newBet;
    }

    private static String getStepName(final List<Step> steps, final String stepId) {
        for (Step step : steps) {
            if (step.getId().toString().equals(stepId)) {
                return step.getName();
            }
        }
        return "";
    }


    private static String getTeamName(final List<Team> teams, final String teamId) {
        for (Team team : teams) {
            if (team.getId().toString().equals(teamId)) {
                return team.getName();
            }
        }
        return "";
    }

    private static void saveTeam(final RoutingContext rctx) {

        if (!isAdmin(rctx)) {
            return;
        }

        final var request = rctx.request();
        final var id = request.getParam("teamId");
        final var name = request.getParam("name");
        final var flag = request.getParam("flag");
        final var country = request.getParam("country");
        final var cupsWins = request.getParam("cupsWins");
        final var continent = request.getParam("continent");

        Team team = null;
        if (name != null) {
            team = new Team(
                    name,
                    flag,
                    country,
                    Integer.valueOf(cupsWins != null ? cupsWins : "0"),
                    continent
            );
            if (id != null && !id.equalsIgnoreCase("new") && !id.isEmpty()) {
                team.setId(new ObjectId(id));
            }
        }

        if (team != null) {
            Repository.mongoDaoReactive.save(team);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Long getDate(final String matchDate) {
        final var date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
        try {
            return date.parse(matchDate).toInstant().toEpochMilli();
        } catch (ParseException e) {
            return Instant.now().atOffset(ZoneOffset.ofHours(-3)).toInstant().toEpochMilli() + 3600;
        }


    }

    private static void saveMatch(final RoutingContext rctx) {

        if (!isAdmin(rctx)) {
            return;
        }

        final var request = rctx.request();

        final var id = request.getParam("matchId");

        final var teamIds = request.params().getAll("teamIds");

        if (teamIds == null || teamIds.isEmpty()) {
            return;
        }

        final var team1Id = teamIds.get(0);
        final var team2Id = teamIds.get(1);

        final var team1Goals = request.getParam("team1Goals");
        final var team2Goals = request.getParam("team2Goals");

        final var matchDate = getDate(request.getParam("matchDate"));

        final var stepsIds = request.params().getAll("stepIds");
        final var stepId = stepsIds.get(0);

        Match match = null;
        if (team1Id != null && team2Id != null && stepId != null) {
            match = new Match(
                    team1Id,
                    team2Id,
                    getGoals(team1Goals),
                    getGoals(team2Goals),
                    matchDate,
                    stepId
            );
            if (id != null && !id.equalsIgnoreCase("new") && !id.isEmpty()) {
                match.setId(new ObjectId(id));
            }
        }

        Repository.mongoDaoReactive.save(match);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static Integer getGoals(final String goals) {
        if (goals == null || goals.isEmpty()) {
            return null;
        }
        return Integer.parseInt(goals);
    }

    private static Promise<String> saveBet(final RoutingContext rctx) {
        final Promise<String> promise = Promise.promise();

        final var principal = rctx.user().principal();
        final var userQuery = new JsonObject()
                .put("_id", principal.getString("_id"));
        final var matchId = rctx.request().getParam("betId");
        final var team1Goals = getGoals(rctx.request().getParam("team1Goals"));
        final var team2Goals = getGoals(rctx.request().getParam("team2Goals"));

        if (matchId == null || matchId.isEmpty()) {
            promise.complete("");
        } else {
            Repository.mongoDaoReactive.whatDoWithThatList(Match.class, new JsonObject().put("_id", matchId), matchAsyncResult -> {
                MongoHelper.getMappedFlowabe(matchAsyncResult, Match.class)
                        .toList()
                        .subscribe(matches -> {
                            if (matches != null && isBetAvailable(matches.get(0))) {
                                Repository.mongoDaoReactive.whatDoWithThatList(User.class, userQuery, userMongoResult ->
                                        MongoHelper.getMappedFlowabe(userMongoResult, User.class)
                                                .toList()
                                                .subscribe(users -> {
                                                            final var newBet = new Bet(matchId, team1Goals, team2Goals);
                                                            final var user = users.get(0);
                                                            user.getBets().remove(newBet);
                                                            user.getBets().add(newBet);
                                                            Repository.mongoDaoReactive.saveAndDo(user,
                                                                    event -> promise.complete(event.result())
                                                            );
                                                        }
                                                )
                                );
                            } else {
                                promise.complete("");
                            }
                        });

            });

        }


        return promise;
    }

    private static void saveStep(final RoutingContext rctx) {

        if (!isAdmin(rctx)) {
            return;
        }

        final var request = rctx.request();
        final var id = request.getParam("stepId");
        final var name = request.getParam("name");
        final var extra1 = request.getParam("extra1");
        final var extra2 = request.getParam("extra2");
        final var extra3 = request.getParam("extra3");
        final var extra4 = request.getParam("extra4");

        Step step = null;
        if (name != null) {
            step = new Step(
                    name,
                    extra1,
                    extra2,
                    extra3,
                    extra4
            );
            if (id != null && !id.equalsIgnoreCase("new") && !id.isEmpty()) {
                step.setId(new ObjectId(id));
            }
        }

        if (step != null) {
            Repository.mongoDaoReactive.save(step);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isAdmin(final RoutingContext routingContext) {
        try {
            final var username = routingContext.user().principal().getString("username");
            return admins.contains(username);
        } catch (final Exception ex) {
            return false;
        }

    }


}