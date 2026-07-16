package com.slotcentral.gameengine.secondary;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "one-of-three")
public class OneOfThreeConfig {
    private List<Integer> options;

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }
}
