package com.slotcentral.gameengine.paytable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "paytable")
public class PaytableConfig {
    private List<Integer> betsValues;
    private List<Integer> denomValues;
    private int defaultBetIndex;
    private int defaultDenomIndex;
    private List<List<Integer>> betValues;
    private double jackpotContributionRate;

    public List<Integer> getBetsValues() {
        return betsValues;
    }

    public void setBetsValues(List<Integer> betsValues) {
        this.betsValues = betsValues;
    }

    public List<Integer> getDenomValues() {
        return denomValues;
    }

    public void setDenomValues(List<Integer> denomValues) {
        this.denomValues = denomValues;
    }

    public int getDefaultBetIndex() {
        return defaultBetIndex;
    }

    public void setDefaultBetIndex(int defaultBetIndex) {
        this.defaultBetIndex = defaultBetIndex;
    }

    public int getDefaultDenomIndex() {
        return defaultDenomIndex;
    }

    public void setDefaultDenomIndex(int defaultDenomIndex) {
        this.defaultDenomIndex = defaultDenomIndex;
    }

    public List<List<Integer>> getBetValues() {
        return betValues;
    }

    public void setBetValues(List<List<Integer>> betValues) {
        this.betValues = betValues;
    }

    public double getJackpotContributionRate() {
        return jackpotContributionRate;
    }

    public void setJackpotContributionRate(double jackpotContributionRate) {
        this.jackpotContributionRate = jackpotContributionRate;
    }
}
