package com.slotcentral.gameengine.reels;

import org.springframework.stereotype.Component;

@Component
public class Reel1 extends AbstractReel {
    public Reel1(Reel1Properties props) {
        super(props.getStrip());
    }
}
