package com.github.bfulton.alpnrt;

import com.github.bfulton.alpnrt.helper.GlobalCounter;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class AlpnRuntimeTest {
    @Test
    public void testRunIsCalled() {
        assertEquals(0, GlobalCounter.val.get());
        AlpnRuntime.run("com.github.bfulton.alpnrt.helper.SomeClass");
        assertEquals(1, GlobalCounter.val.get());
    }
}
