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

/**
 * Namespace for {@link PropOrEnvConfigOption}. Will automatically capitalize
 * environment variable names.
 */
public class PropOrEnvNamespace<T> implements Namespace<PropOrEnvConfigOption<T>, T> {

    public static <T> PropOrEnvNamespace<T> create(String name) {
        return new PropOrEnvNamespace<>(name.toUpperCase(), name);
    }

    private final String envName;
    private final String propName;

    private PropOrEnvNamespace(String envName, String propName) {
        this.envName = envName;
        this.propName = propName;
    }

    @Override
    public String getName() {
        return propName;
    }

    public String getEnvName() {
        return envName;
    }

    public String getPropName() {
        return propName;
    }

    @Override
    public PropOrEnvNamespace<T> subspace(String name) {
        return new PropOrEnvNamespace<>(envName(name), propName(name));
    }

    private String envName(String name) {
        return envName.toUpperCase() + "_" + name.toUpperCase();
    }

    private String propName(String name) {
        return propName + "." + name;
    }

    @Override
    public PropOrEnvConfigOption<T> create(String name, Function<String, Optional<T>> loader, T defaultValue) {
        return PropOrEnvConfigOption.create(envName(name), propName(name), loader, defaultValue);
    }

}
