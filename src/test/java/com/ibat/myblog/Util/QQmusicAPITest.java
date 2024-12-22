package com.ibat.myblog.Util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class QQmusicAPITest {
    @Test
    public void ces() {
            MusicCoverFetcher fetcher = new MusicCoverFetcher();
            try {
                String coverUrl = fetcher.getCoverUrl("BB‘s theme’’", "Ludvig Forssell");
                System.out.println("封面地址: " + coverUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}


