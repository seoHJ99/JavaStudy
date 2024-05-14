package IO;

import java.io.*;

public class IOTimeTest {

    private static final String DUMMY_FILE_PATH = "C:\\Users\\hojun\\Desktop\\Git\\JavaStudy\\IOAndNIO\\testDummy.txt";
    private static final String COPY_FILE_PATH = "C:\\Users\\hojun\\Desktop\\Git\\JavaStudy\\IOAndNIO\\CopyDummy.txt";

    public static void main(String[] args) {

        long time1 = measureRunTime(IOTimeTest::copyDummyWithIO);
        System.out.println("버퍼를 사용하지 않을 경우 처리 시간 : " +time1 + " milli seconds");
        long time2 = measureRunTime(IOTimeTest::copyDummyWithBufferIO);
        System.out.println("I/O 버퍼를 사용한 처리 시간 : " + time2 + " milli seconds");
        long time3 = measureRunTime(IOTimeTest::copyDummyWithBufferIOWithBufferSize);
        System.out.println("I/O 버퍼값을 정해준 처리 시간 : " + time3 + " milli seconds");

    }



    /**
     * 함수를 매개변수로 받아 실행 시간 측정하는 함수
     * */
    public static long measureRunTime(Runnable task){
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        return endTime-startTime;
    }


    /**
     * i/o api 를 이용해서 10mb 의 더미 파일을 복사
     * flush() 를 사용하지는 않음으로 내용은 복사되지 않고 파일만 생성됨
     * */
    public static void copyDummyWithIO() {
        try (InputStream in = new FileInputStream(DUMMY_FILE_PATH);
             OutputStream out = new FileOutputStream(COPY_FILE_PATH)) {

            while (true) {
                int byteDate = in.read();
                if (byteDate == -1) {
                    break;
                }
//                out.write(byteDate);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDummyWithBufferIO() {
        try (BufferedInputStream in = new BufferedInputStream( new FileInputStream(DUMMY_FILE_PATH), 1100);
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(COPY_FILE_PATH))) {

            while (true) {
                int byteDate = in.read();
                if (byteDate == -1) {
                    break;
                }
//                out.write(byteDate);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyDummyWithBufferIOWithBufferSize() {
        try (BufferedInputStream in = new BufferedInputStream( new FileInputStream(DUMMY_FILE_PATH));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(COPY_FILE_PATH))) {

            int available = in.available();
            byte[] bufferSize = new byte[available];
            while (true) {
                int byteDate = in.read(bufferSize);
                if (byteDate == -1) {
                    break;
                }
//                out.write(byteDate);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}