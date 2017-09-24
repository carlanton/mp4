/*
 * Copyright 2012 Sebastian Annies, Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mp4parser.tools;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Converts <code>byte[]</code> -&gt; <code>String</code> and vice versa.
 */
public final class Utf8 {
    public static byte[] convert(String s) {
        return s != null ? s.getBytes(UTF_8) : null;
    }

    public static String convert(byte[] b) {
        return b != null ? new String(b, UTF_8) : null;
    }

    public static int utf8StringLengthInBytes(String utf8) {
        byte[] bytes = convert(utf8);
        return bytes != null ? bytes.length : 0;
    }
}
