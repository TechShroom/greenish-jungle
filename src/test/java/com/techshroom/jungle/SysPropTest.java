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

import org.junit.Test;

public class SysPropTest {

    protected SysPropConfigOption<String> option(String name, String defaultValue) {
        return SysPropConfigOption.create(name, Loaders.forString(), defaultValue);
    }

    @Test
    public void noNullName() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> option(null, TestUtil.fuzz("default")));
    }

    @Test
    public void noNullLoader() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> SysPropConfigOption.create("test", null, TestUtil.fuzz("default")));
    }

    @Test
    public void defaultIsFromConstructor() throws Exception {
        String def = TestUtil.fuzz("default");
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), def);
        assertEquals(def, opt.getDefault());
    }

    @Test
    public void noNullDefault() throws Exception {
        TestUtil.expect(IllegalArgumentException.class, () -> option("test", null));
    }

    @Test
    public void getUsesDefaultIfNoProp() throws Exception {
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), TestUtil.fuzz("default"));
        System.clearProperty(opt.getSystemPropertyName());
        assertEquals(opt.getDefault(), opt.get());
    }

    @Test
    public void defaultIsRetainedOnSet() throws Exception {
        String def = TestUtil.fuzz("default");
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), def);
        System.clearProperty(opt.getSystemPropertyName());
        opt.set(TestUtil.fuzz("set"));
        assertEquals(def, opt.getDefault());
    }

    @Test
    public void setChangesValue() throws Exception {
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), TestUtil.fuzz("default"));
        System.clearProperty(opt.getSystemPropertyName());
        String set = TestUtil.fuzz("set");
        opt.set(set);
        assertEquals(opt.get(), set);
    }

    @Test
    public void noNullSet() throws Exception {
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), TestUtil.fuzz("default"));
        System.clearProperty(opt.getSystemPropertyName());
        TestUtil.expect(IllegalArgumentException.class, () -> opt.set(null));
    }

    @Test
    public void getUsesPropIfPresent() throws Exception {
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), TestUtil.fuzz("default"));
        String prop = TestUtil.fuzz("prop");
        System.setProperty(opt.getSystemPropertyName(), prop);
        assertEquals(prop, opt.get());
    }

    @Test
    public void setOverridesProp() throws Exception {
        SysPropConfigOption<String> opt = option(TestUtil.fuzz("propertyName"), TestUtil.fuzz("default"));
        System.setProperty(opt.getSystemPropertyName(), TestUtil.fuzz("prop"));
        String set = TestUtil.fuzz("set");
        opt.set(set);
        assertEquals(set, opt.get());
    }

    @Test
    public void valueIsNotSetAtConstruction() throws Exception {
        String name = TestUtil.fuzz("propertyName");
        System.setProperty(name, TestUtil.fuzz("prop"));
        SysPropConfigOption<String> opt = option("test", TestUtil.fuzz("default"));
        String prop = TestUtil.fuzz("prop-2");
        System.setProperty(opt.getSystemPropertyName(), prop);
        assertEquals(prop, opt.get());
    }

}
