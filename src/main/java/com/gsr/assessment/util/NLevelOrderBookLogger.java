package com.gsr.assessment.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class NLevelOrderBookLogger {
    public static void display(List<Float> asks, List<Float> bids) {
        log.info("- ask --- bid -");
        int depth = Math.max(asks.size(), bids.size());
        for (int i = 0; i < depth; i ++) {
            log.info("" + asks.get(i) + "\t" + bids.get(i) );
        }
    }
}
