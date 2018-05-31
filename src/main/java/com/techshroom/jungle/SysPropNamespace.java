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

import java.util.Optional;
import java.util.function.Function;

public class SysPropNamespace extends NamespaceBase {

    public static SysPropNamespace create(String name) {
        return new SysPropNamespace(name);
    }

    protected SysPropNamespace(String name) {
        super(name);
    }

    @Override
    public SysPropNamespace subspace(String name) {
        return new SysPropNamespace(subName(name));
    }

    private String subName(String name) {
        return getName() + "." + name;
    }

    @Override
    public <T> SysPropConfigOption<T> create(String name, Function<String, Optional<T>> loader, T defaultValue) {
        return SysPropConfigOption.create(subName(name), loader, defaultValue);
    }

}
