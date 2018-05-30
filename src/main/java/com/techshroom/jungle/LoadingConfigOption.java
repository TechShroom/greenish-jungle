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

/**
 * Config option that loads the initial value automatically.
 */
public abstract class LoadingConfigOption<T> extends ConfigOptionBase<T> {

    private boolean set;

    protected LoadingConfigOption(T defaultValue) {
        super(defaultValue);
    }

    @Override
    public T get() {
        loadIfNeeded();
        return super.get();
    }

    @Override
    public void set(T value) {
        lock.writeLock().lock();
        try {
            super.set(value);
            set = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void loadIfNeeded() {
        if (!set) {
            lock.writeLock().lock();
            try {
                if (!set) {
                    set(load().orElse(getDefault()));
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    protected abstract Optional<T> load();

}
