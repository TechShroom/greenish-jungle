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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class PropOrEnvTest {

    public static final class EnvSide extends EnvTest {

        @Override
        protected EnvConfigOption<String> option(String name, String defaultValue) {
            return PropOrEnvConfigOption.create(name, name, Loaders.forString(), defaultValue);
        }

    }

    public static final class PropSide extends SysPropTest {

        @Override
        protected SysPropConfigOption<String> option(String name, String defaultValue) {
            return PropOrEnvConfigOption.create(name, name, Loaders.forString(), defaultValue);
        }

    }

    protected PropOrEnvConfigOption<String> option(String name, String defaultValue) {
        return PropOrEnvConfigOption.create(name, name, Loaders.forString(), defaultValue);
    }

    @BeforeClass
    public static void overrideEnvironment() {
        ValueAccess.environment = new ConcurrentHashMap<>();
    }

    @Test
    public void noNullEnvName() throws Exception {
        TestUtil.expect(IllegalArgumentException.class,
                () -> PropOrEnvConfigOption.create(null, TestUtil.fuzz("name"), Loaders.forString(), TestUtil.fuzz("default")));
    }

    @Test
    public void noNullPropName() throws Exception {
        TestUtil.expect(IllegalArgumentException.class,
                () -> PropOrEnvConfigOption.create(TestUtil.fuzz("name"), null, Loaders.forString(), TestUtil.fuzz("default")));
    }

    @Test
    public void noNullLoader() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> PropOrEnvConfigOption.create("test", TestUtil.fuzz("name"), null, TestUtil.fuzz("default")));
    }

    @Test
    public void prefersPropToEnv() throws Exception {
        PropOrEnvConfigOption<String> opt = option(TestUtil.fuzz("name"), TestUtil.fuzz("default"));
        String prop = TestUtil.fuzz("prop");
        System.setProperty(opt.getSystemPropertyName(), prop);
        ValueAccess.environment.put(opt.getEnvironmentVariableName(), TestUtil.fuzz("env"));
        assertEquals(prop, opt.get());
    }

}
