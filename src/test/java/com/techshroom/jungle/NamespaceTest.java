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

import java.util.function.Function;

import org.junit.Test;

public class NamespaceTest {

    private <NS extends Namespace<? extends ConfigOption<String>, String>>
            NS assertNormalNamespaceFunctionality(String name, Function<String, NS> create) {
        NS ns = create.apply(name);
        assertEquals(name, ns.getName());
        return ns;
    }

    @Test
    public void envNamespace() throws Exception {
        EnvNamespace<String> ns = assertNormalNamespaceFunctionality("test", x -> EnvNamespace.create(x));
        assertEquals("test_foo", ns.create("foo", Loaders.forString(), "").getEnvironmentVariableName());
        assertEquals("test_foo", ns.subspace("foo").getName());
    }

    @Test
    public void propNamespace() throws Exception {
        SysPropNamespace<String> ns = assertNormalNamespaceFunctionality("test", x -> SysPropNamespace.create(x));
        assertEquals("test.foo", ns.create("foo", Loaders.forString(), "").getSystemPropertyName());
        assertEquals("test.foo", ns.subspace("foo").getName());
    }

    @Test
    public void propOrEnvNamespace() throws Exception {
        PropOrEnvNamespace<String> ns = assertNormalNamespaceFunctionality("test", x -> PropOrEnvNamespace.create(x));
        assertEquals("test", ns.getPropName());
        assertEquals("TEST", ns.getEnvName());
        PropOrEnvNamespace<String> subspace = ns.subspace("foo");
        assertEquals("test.foo", subspace.getName());
        assertEquals("TEST_FOO", subspace.getEnvName());
        assertEquals("test.foo", subspace.getPropName());
        PropOrEnvConfigOption<String> opt = ns.create("foo", Loaders.forString(), "");
        assertEquals("TEST_FOO", opt.getEnvironmentVariableName());
        assertEquals("test.foo", opt.getSystemPropertyName());
    }

}
