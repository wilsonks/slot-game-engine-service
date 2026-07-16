package com.slotcentral.gameengine.reels;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "reel1")
public class Reel1Properties {
    private List<String> strip;

    public List<String> getStrip() {
        return strip;
    }

    public void setStrip(List<String> strip) {
        this.strip = strip;
    }
}
