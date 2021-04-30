package com.genesislab.videoservice.domain.video.file;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsTests {

    @Test
    public void 파일경로를_합친다() throws Exception {
        String storageDir = "/Users/limjun-young/workspace/privacy/dev/test";
        String fileName = "test.mp4";

        Path path = Paths.get(storageDir, fileName);
        System.out.println(path.toAbsolutePath().toString());
     }
}
