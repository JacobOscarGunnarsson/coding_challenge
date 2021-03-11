import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.google.gson.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The program is tested by sending JsonObjects into System.in which then are flattened. The output is then
 * captured and compared with expected data.
 */
public class FlattenJsonTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @org.junit.Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @org.junit.After
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testEmptyJsonBlob() {
        String input = "{}";
        String expected = "{}";
        assertEquals(formatExpected(expected), input);
    }

    @Test
    public void testSampleInput() {
        String input = "{\"a\": 1,\"b\": true,\"c\": {\"d\": 3,\"e\": \"test\"}}";
        String expected = "{\"a\": 1,\"b\": true,\"c.d\": 3,\"c.e\": \"test\"}";
        assertEquals(formatExpected(expected), getResult(input));
    }

    @Test
    public void testInputContainingEmptyJsonObjectInDeeperLevel() {
        String input = "{\"a\": 1,\"b\": true,\"c\": {\"d\": 3,\"e\": \"test\",\"c\": {\"d\": 3,\"f\": {},\"e\": \"test\"}},\"f\": {}}";
        String expected = "{\"a\": 1,\"b\": true,\"c.d\": 3,\"c.e\": \"test\",\"c.c.d\": 3,\"c.c.f\": {},\"c.c.e\": \"test\",\"f\": {}}";
        assertEquals(formatExpected(expected), getResult(input));
    }

    public String getResult(String data) {
        String[] args = {};
        final byte[] dataBytes = data.getBytes();
        final ByteArrayInputStream inStream = new ByteArrayInputStream(dataBytes);
        System.setIn(inStream);
        FlattenJson.main(args);
        return outputStreamCaptor.toString().trim();
    }
    
    /**
     * Constructs an arbitrary JsonObject into the string-format which is used in FlattenJson.java
     */
    private String formatExpected(String expected) {
        JsonObject jsonObject = JsonParser.parseString(expected).getAsJsonObject();
        return FlattenJson.prettifyJson(jsonObject).trim();
    }
}