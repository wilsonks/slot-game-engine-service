package com.slotcentral.gameengine.reels;

import org.springframework.stereotype.Component;

@Component
public class Reel2 extends AbstractReel {
    public Reel2(Reel2Properties props) {
        super(props.getStrip());
    }
}
