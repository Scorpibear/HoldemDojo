package com.nedogeek.holdem.dealer;

import com.nedogeek.holdem.GameSettings;
import com.nedogeek.holdem.PlayerStatus;
import com.nedogeek.holdem.gamingStuff.Player;
import com.nedogeek.holdem.gamingStuff.PlayerAction;
import com.nedogeek.holdem.gamingStuff.PlayersList;

/**
 * User: Konstantin Demishev
 * Date: 21.11.12
 * Time: 23:26
 */
public class MoveManager {
    private final Dealer dealer;
    private final PlayersList playersManager;

    MoveManager(Dealer dealer, PlayersList playersManager) {
        this.dealer = dealer;
        this.playersManager = playersManager;
    }

    void makeMove(Player mover) {
        PlayerAction playerMove = mover.getMove();
        playersManager.playerMoved(mover);
        switch (playerMove.getActionType()) {
            case Fold:
                makeFold(mover);
                break;
            case Check:
                break;
            case Call:
                makeCall(mover);
                break;
            case Rise:
                makeRise(mover);
                break;
            case AllIn:
                makeAllIn(mover);
                break;
            default:
                makeFold(mover);
        }
    }

    private void trySendToPot(Player player, int betValue) {
        final int playerAmount = player.getBalance();

        if (playerAmount <= betValue) {
            sendToPot(player, playerAmount);
            player.setStatus(PlayerStatus.AllIn);
        } else {
            sendToPot(player, betValue);
        }
    }

    private void sendToPot(Player player, int chips) {
        final int playerAmount = player.getBalance();
        final int playerBet = chips + player.getBet();

        player.setBet(playerBet);
        dealer.addToPot(chips);
        player.setBalance(playerAmount - chips);
        checkCallValue(playerBet);
    }


    private void checkCallValue(int playerBet) {
        if (dealer.getCallValue() < playerBet) {
            dealer.setCallValue(playerBet);
        }
    }

    private void makeFold(Player player) {
        System.out.println(player + " making fold.");

        player.setStatus(PlayerStatus.Fold);
    }

    private void makeCall(Player player) {
        System.out.println(player + " making call.");

        player.setStatus(PlayerStatus.Call);
        trySendToPot(player, dealer.getCallValue() - player.getBet());
    }

    private void makeAllIn(Player player) {
        System.out.println(player + " making AllIn.");

        trySendToPot(player, player.getBalance());
    }

    private void makeRise(Player player) {
        System.out.println(player + " making rise");

        int riseValue = calculateRiseValue(player);
        player.setStatus(PlayerStatus.Rise);
        trySendToPot(player, riseValue);
    }

    private int calculateRiseValue(Player player) {
        int playerWantRise = player.getMove().getRiseAmount();
        int minimumRiseValue = dealer.getCallValue() + 2 * GameSettings.SMALL_BLIND;

        return (playerWantRise < minimumRiseValue) ? minimumRiseValue : playerWantRise;
    }

    public void makeSmallBlind(Player player) {
        player.setStatus(PlayerStatus.SmallBLind);

        trySendToPot(player, GameSettings.SMALL_BLIND);
    }

    public void makeBigBlind(Player player) {
        player.setStatus(PlayerStatus.BigBlind);

        trySendToPot(player, 2 * GameSettings.SMALL_BLIND);
    }
}
