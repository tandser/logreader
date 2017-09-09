package ru.tandser.logreader;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;

import static com.google.common.io.Resources.getResource;

public class LogReaderTest {

    @Test
    public void nextCorrectAccessLog() throws Exception {
        long numberOfRequests = 0;

        try (LogReader logReader = new LogReader(new FileReader(getResource("correct_access.log").getFile()))) {
            while (logReader.isMore()) {
                if (logReader.next() != null) {
                    numberOfRequests++;
                }
            }
        }

        Assert.assertTrue(numberOfRequests == 20);
    }

    @Test
    public void nextIncorrectAccessLog() throws Exception {
        long numberOfRequests = 0;

        try (LogReader logReader = new LogReader(new FileReader(getResource("incorrect_access.log").getFile()))) {
            while (logReader.isMore()) {
                if (logReader.next() != null) {
                    numberOfRequests++;
                }
            }
        }

        Assert.assertTrue(numberOfRequests == 15);
    }
}
