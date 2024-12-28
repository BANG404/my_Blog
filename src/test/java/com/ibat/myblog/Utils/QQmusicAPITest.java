package com.ibat.myblog.Utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

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


