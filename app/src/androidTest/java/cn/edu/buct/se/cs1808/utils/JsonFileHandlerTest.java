package cn.edu.buct.se.cs1808.utils;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;

public class JsonFileHandlerTest extends TestCase {

    public void testRead() {
    }

    public void testReadJsonObject() {
    }

    public void testReadJsonArray() {
    }

    public void testWrite() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        assertTrue(JsonFileHandler.write(ctx, "test.json", "hello world!", StandardCharsets.UTF_8, Context.MODE_PRIVATE));
    }
}
