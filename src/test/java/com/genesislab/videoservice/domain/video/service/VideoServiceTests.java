package com.genesislab.videoservice.domain.video.service;


import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class VideoServiceTests {

    @Test
    public void 비디오_목록을_찾기() throws Exception {

        //given





        //when

        //then
     }


    @Test
    public void video_영상파일을_쓴다() throws Exception {

        Path path = Paths.get("/Users/limjun-young/workspace/privacy/dev/test/video/video/output.txt");

        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            String data = "NIO Channel을 이용해서 파일에 데이터를 써보겠습니다.";
            Charset charset = Charset.defaultCharset();
            ByteBuffer buffer = charset.encode(data);
            ch.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void video_영상파일을_읽어온다() {

        //given
        Path path = Paths.get("/Users/limjun-young/workspace/privacy/dev/test/video/video/temp.txt");

        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 채널로부터 파일을 읽은 후 버퍼로 저장함, 쓰기 작업인 경우에는 버퍼에서 읽어서 채널이 파일에 쓴다.
            ch.read(buffer);

            buffer.flip();
            Charset charset = Charset.defaultCharset();
            String inputData = charset.decode(buffer).toString();
            System.out.println("print: " + inputData);

            //flip 메서드 호출하면 buffer point는 0, limit은 point가 가르켰던 값으로 이동 함. 파일 크기만큼 버퍼에서 읽어드릴 수 있는 장점이 있음.
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            // buffer에서 바이트 값을 읽어서 byte 배열에 저장함.
            buffer.get(data);
            System.out.println(new String(data));
            buffer.clear();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("파일 작업 실패");
        }
    }

    @Test
    public void video_영상파일을_읽_쓰다() {

        // File 객체 생성
        File file = new File("/Users/limjun-young/workspace/privacy/dev/test/video/video/temp.txt");

        // Finally 문에 닫아주기 위해 밖에 생성
        RandomAccessFile randomAccessFile = null;

        // 채널 열기
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            // 해당 파일에 대한 채널 통로를 생성한다고 보면 됩니다.
            FileChannel ch = randomAccessFile.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(100);

            String str = "NIO를 배워보자!!!!!";
            Charset charset = Charset.defaultCharset();
            buffer = charset.encode(str);
            // 버퍼를 읽거나 쓸 경우에도 버퍼 크기만큼 버퍼 포인트가 이동합니다.
            ch.write(buffer);

            // 파일 읽기
            String inputStr = "";
            randomAccessFile.seek(0); // 파일 포인터를 가장 처음으로 옮겨줌 위에서 파일을 썼기 때문에 파일 포인터는 현재 EOF위치여서 다시 0으로 초기화 해준 부분입니다.
            randomAccessFile.write((byte) 'n'); // 첫 글자를 소문자로 바꿈 (포인터 이동)
            randomAccessFile.seek(31); // 파일 포인터를 다시 마지으로 옮겨줌
            randomAccessFile.write((byte) '@');
            randomAccessFile.seek(0); // 파일 포인트를 0으로 초기화 해줌
            // 버퍼를 읽고 쓰는 범위는 전체 (capacity)중 limit - position 범위입니다.
            buffer.clear();

            ch.read(buffer);
            // 만약 clear를 사용하면 버퍼안의 문자열 이외의 바이트 값은 0으로 출력이 됩니다. 그래서 flip을 사용하는게 더 효율적인것 같습니다.
            buffer.flip();
            // charset에서 버퍼에 있는 바이너리 값을 인코딩하여 문자열로 리턴함.
            inputStr = charset.decode(buffer).toString();
            System.out.println(inputStr);

        } catch (Exception e) {
            System.out.println("파일 작업 실패");
        } finally {
            try {
                randomAccessFile.close();
            } catch (Exception a) {
                System.out.println("파일 닫기 실패");
            }
        }
    }

    @Test
    public void 논블로킹_파일을_읽어본다() throws Exception {
        Path path = Paths.get("/Users/limjun-young/workspace/privacy/dev/test/video/video/output.txt");

        try {
            AsynchronousFileChannel ch = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            ch.read(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 읽은 바이트 수를 리턴함.
                    System.out.println("result = " + result);

                    attachment.flip();
                    byte[] data = new byte[attachment.limit()];
                    attachment.get(data);
                    System.out.println(new String(data));
                    attachment.clear();

                    if (ch != null || ch.isOpen()) {
                        try {
                            ch.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("파일 읽기 실패");
                    exc.printStackTrace();
                }
            });
            System.out.println("Non-Blocking 중이니?");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
