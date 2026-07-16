package com.slotcentral.gameengine.reels;

import org.springframework.stereotype.Component;

@Component
public class Reel3 extends AbstractReel {
    public Reel3(Reel3Properties props) {
        super(props.getStrip());
    }
}
