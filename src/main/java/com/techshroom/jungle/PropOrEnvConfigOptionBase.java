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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;
import java.util.function.Function;

public class PropOrEnvConfigOptionBase<T> extends LoadingConfigOption<T> implements PropOrEnvConfigOption<T> {

    private final String environmentVariableName;
    private final String systemPropertyName;
    private final Function<String, Optional<T>> loader;

    protected PropOrEnvConfigOptionBase(String environmentVariableName, String systemPropertyName, Function<String, Optional<T>> loader, T defaultValue) {
        super(defaultValue);
        checkArgument(environmentVariableName != null, "Null environment name");
        checkArgument(systemPropertyName != null, "Null property name");
        checkArgument(loader != null, "Null loader");
        this.environmentVariableName = environmentVariableName;
        this.systemPropertyName = systemPropertyName;
        this.loader = loader;
    }

    @Override
    public String getEnvironmentVariableName() {
        return environmentVariableName;
    }

    @Override
    public String getSystemPropertyName() {
        return systemPropertyName;
    }

    @Override
    protected Optional<T> load() {
        Optional<String> env = ValueAccess.getEnv(environmentVariableName);
        Optional<String> prop = ValueAccess.getProp(systemPropertyName);
        return (prop.isPresent() ? prop : env)
                .flatMap(loader);
    }

}
