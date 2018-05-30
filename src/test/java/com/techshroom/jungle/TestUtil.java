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

import static org.junit.Assert.fail;

import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Throwables;

public class TestUtil {

    public static String fuzz(String str) {
        return str + ThreadLocalRandom.current().nextInt();
    }

    @FunctionalInterface
    public interface ThrowingCode {

        void run() throws Throwable;

    }

    public static <TH extends Throwable> TH expect(Class<TH> throwableClass, ThrowingCode code) throws Exception {
        try {
            code.run();
            fail("Expected an exception of type " + throwableClass.getName());
            throw new AssertionError("Unreachable.");
        } catch (Throwable thrown) {
            if (throwableClass.isInstance(thrown)) {
                return throwableClass.cast(thrown);
            }
            Throwables.throwIfUnchecked(thrown);
            Throwables.throwIfInstanceOf(thrown, Exception.class);
            throw new RuntimeException(thrown);
        }
    }

}
