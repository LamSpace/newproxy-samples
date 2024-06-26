/*
 * Copyright 2024 the original author, Lam Tong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lamspace.newproxy.classes.extra;

public class Something {

    private final String s;

    private final int x;

    public Something() {
        this.s = "Hello, World";
        this.x = 1;
    }

    public Something(String s) {
        this.s = s;
        this.x = 1;
    }

    public Something(String s, int x) {
        this.s = s;
        this.x = x;
    }

    public String repeat() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < x; i++) {
            sb.append(s).append(i == x - 1 ? "" : " -> ");
        }
        return sb.toString();
    }

}
