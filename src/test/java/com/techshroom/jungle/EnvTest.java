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

public class EnvTest {

    @BeforeClass
    public static void overrideEnvironment() {
        ValueAccess.environment = new ConcurrentHashMap<>();
    }

    protected EnvConfigOption<String> option(String name, String defaultValue) {
        return EnvConfigOption.create(name, Loaders.forString(), defaultValue);
    }

    @Test
    public void noNullName() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> option(null, TestUtil.fuzz("default")));
    }

    @Test
    public void noNullLoader() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> EnvConfigOption.create("test", null, TestUtil.fuzz("default")));
    }

    @Test
    public void defaultIsFromConstructor() throws Exception {
        String def = TestUtil.fuzz("default");
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), def);
        assertEquals(def, opt.getDefault());
    }

    @Test
    public void noNullDefault() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> option("test", null));
    }

    @Test
    public void getUsesDefaultIfNoEnv() throws Exception {
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), TestUtil.fuzz("default"));
        ValueAccess.environment.remove(opt.getEnvironmentVariableName());
        assertEquals(opt.getDefault(), opt.get());
    }

    @Test
    public void defaultIsRetainedOnSet() throws Exception {
        String def = TestUtil.fuzz("default");
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), def);
        ValueAccess.environment.remove(opt.getEnvironmentVariableName());
        opt.set(TestUtil.fuzz("set"));
        assertEquals(def, opt.getDefault());
    }

    @Test
    public void setChangesValue() throws Exception {
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), TestUtil.fuzz("default"));
        ValueAccess.environment.remove(opt.getEnvironmentVariableName());
        String set = TestUtil.fuzz("set");
        opt.set(set);
        assertEquals(opt.get(), set);
    }

    @Test
    public void noNullSet() throws Exception {
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), TestUtil.fuzz("default"));
        ValueAccess.environment.remove(opt.getEnvironmentVariableName());
        TestUtil.expect(IllegalArgumentException.class, () -> opt.set(null));
    }

    @Test
    public void getUsesEnvIfPresent() throws Exception {
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), TestUtil.fuzz("default"));
        String env = TestUtil.fuzz("env");
        ValueAccess.environment.put(opt.getEnvironmentVariableName(), env);
        assertEquals(env, opt.get());
    }

    @Test
    public void setOverridesEnv() throws Exception {
        EnvConfigOption<String> opt = option(TestUtil.fuzz("test"), TestUtil.fuzz("default"));
        ValueAccess.environment.put(opt.getEnvironmentVariableName(), TestUtil.fuzz("env"));
        String set = TestUtil.fuzz("set");
        opt.set(set);
        assertEquals(set, opt.get());
    }

    @Test
    public void valueIsNotSetAtConstruction() throws Exception {
        String name = TestUtil.fuzz("test");
        ValueAccess.environment.put(name, TestUtil.fuzz("env"));
        EnvConfigOption<String> opt = option(name, TestUtil.fuzz("default"));
        String env = TestUtil.fuzz("env-2");
        ValueAccess.environment.put(opt.getEnvironmentVariableName(), env);
        assertEquals(env, opt.get());
    }

}
