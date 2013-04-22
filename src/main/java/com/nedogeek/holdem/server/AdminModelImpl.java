package com.nedogeek.holdem.server;

import com.nedogeek.holdem.Game;
import com.nedogeek.holdem.GameImpl;
import com.nedogeek.holdem.GameSettings;
import com.nedogeek.holdem.bot.Bots;

/**
 * User: Konstantin Demishev
 * Date: 12.04.13
 * Time: 2:52
 */
public class AdminModelImpl implements AdminModel {
    static final String DEFAULT_PASSWORD = "1234";

    private String adminPassword = DEFAULT_PASSWORD;

    private Game game = GameImpl.getInstance();

    @Override
    public void addBot(Bots botType, String botName) {
        game.addBot(botType, botName);
    }

    @Override
    public void kick(String playerName) {
        game.removePlayer(playerName);
    }

    @Override
    public void setInitialCoins(int coinsCount) {
        GameSettings.setCoinsAtStart(coinsCount);
    }

    @Override
    public void setMinimumBlind(int minimumBlind) {
        GameSettings.setSmallBlind(minimumBlind);
    }

    @Override
    public void setGameDelay(int gameDelay) {
        GameSettings.setGameDelayValue(gameDelay);
    }

    @Override
    public void setEndGameDelay(int endGameDelay) {
        GameSettings.setEndGameDelayValue(endGameDelay);
    }

    @Override
    public void start() {
        game.start();
    }

    @Override
    public void stop() {
        game.stop();
    }

    @Override
    public void pause() {
        game.pause();
    }

    @Override
    public void changePassword(String newPassword) {
        adminPassword = newPassword;
    }

    @Override
    public GameDataBean getGameData() {
        GameDataBean gameDataBean = new GameDataBean();
        gameDataBean.setBotTypes(Bots.getBotTypes());

        gameDataBean.setCoinsAtStart(GameSettings.getCoinsAtStart());
        gameDataBean.setMinimumBind(GameSettings.getSmallBlind());

        gameDataBean.setGameDelay(GameSettings.getGameDelayValue());
        gameDataBean.setEndGameDelay(GameSettings.getEndGameDelayValue());

        gameDataBean.setGameStatus(game.getGameStatus());

        gameDataBean.setPlayers(game.getPlayers());

        return gameDataBean;
    }

    @Override
    public boolean passwordCorrect(String password) {
        return adminPassword.equals(password);
    }
}