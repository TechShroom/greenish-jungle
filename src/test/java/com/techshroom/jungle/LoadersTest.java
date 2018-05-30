/*
 * This file is part of greenish-jungle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshroom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.jungle;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.junit.Test;

public class LoadersTest {

    @Test
    public void cannotBeConstructed() throws Exception {
        Constructor<Loaders> cstr = Loaders.class.getDeclaredConstructor();
        cstr.setAccessible(true);
        InvocationTargetException reflectEx = TestUtil.expect(InvocationTargetException.class, cstr::newInstance);
        TestUtil.expect(AssertionError.class, () -> {
            throw reflectEx.getCause();
        });
    }

    @Test
    public void forInt() throws Exception {
        assertEquals(null, Loaders.forInt().apply("a").orElse(null));
        assertEquals((Object) 0, Loaders.forInt().apply("0").orElse(null));
        assertEquals((Object) 5, Loaders.forInt().apply("5").orElse(null));
    }

    @Test
    public void forIntRange() throws Exception {
        assertEquals(null, Loaders.forIntInRange(1, 10).apply("0").orElse(null));
        assertEquals((Object) 1, Loaders.forIntInRange(1, 10).apply("1").orElse(null));
        assertEquals((Object) 5, Loaders.forIntInRange(1, 10).apply("5").orElse(null));
        assertEquals(null, Loaders.forIntInRange(1, 10).apply("10").orElse(null));
    }

    @Test
    public void forLong() throws Exception {
        assertEquals(null, Loaders.forLong().apply("a").orElse(null));
        assertEquals((Object) 0L, Loaders.forLong().apply("0").orElse(null));
        assertEquals((Object) 5L, Loaders.forLong().apply("5").orElse(null));
    }

    @Test
    public void forLongRange() throws Exception {
        assertEquals(null, Loaders.forLongInRange(1, 10).apply("0").orElse(null));
        assertEquals((Object) 1L, Loaders.forLongInRange(1, 10).apply("1").orElse(null));
        assertEquals((Object) 5L, Loaders.forLongInRange(1, 10).apply("5").orElse(null));
        assertEquals(null, Loaders.forLongInRange(1, 10).apply("10").orElse(null));
    }

    @Test
    public void forDouble() throws Exception {
        assertEquals(null, Loaders.forDouble().apply("a").orElse(null));
        assertEquals((Object) 0d, Loaders.forDouble().apply("0").orElse(null));
        assertEquals((Object) 8d, Loaders.forDouble().apply("8").orElse(null));
    }

    @Test
    public void forDoubleRange() throws Exception {
        assertEquals(null, Loaders.forDoubleInRange(1, 16).apply("0").orElse(null));
        assertEquals((Object) 1d, Loaders.forDoubleInRange(1, 16).apply("1").orElse(null));
        assertEquals((Object) 8d, Loaders.forDoubleInRange(1, 16).apply("8").orElse(null));
        assertEquals(null, Loaders.forDoubleInRange(1, 16).apply("16").orElse(null));
    }

    @Test
    public void forBoolean() throws Exception {
        Stream.of("1", "y", "Y", "yes", "YES", "true", "TRUE")
                .forEach(v -> assertEquals(Boolean.TRUE,
                        Loaders.forBoolean().apply(v).orElse(null)));
        Stream.of("0", "n", "N", "no", "NO", "false", "FALSE", "garbage", "etc")
                .forEach(v -> assertEquals(Boolean.FALSE,
                        Loaders.forBoolean().apply(v).orElse(null)));
    }

    @Test
    public void forString() throws Exception {
        assertEquals("input", Loaders.forString().apply("input").orElse(null));
        assertEquals("random", Loaders.forString().apply("random").orElse(null));
    }

    @Test
    public void forStringMatching() throws Exception {
        assertEquals("input", Loaders.forStringMatching(s -> s.contains("t")).apply("input").orElse(null));
        assertEquals(null, Loaders.forStringMatching(s -> s.contains("t")).apply("random").orElse(null));
    }

}
