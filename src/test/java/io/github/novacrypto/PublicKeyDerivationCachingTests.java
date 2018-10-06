/*
 *  BIP32derivation
 *  Copyright (C) 2017-2018 Alan Evans, NovaCrypto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *  Original source: https://github.com/NovaCrypto/BIP32derivation
 *  You can contact the authors via github issues.
 */

package io.github.novacrypto;

import io.github.novacrypto.bip32.derivation.CkdFunction;
import io.github.novacrypto.bip32.derivation.CkdFunctionResultCacheDecorator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class PublicKeyDerivationCachingTests {

    class Node {

    }

    class MyCkdFunction implements io.github.novacrypto.bip32.derivation.CkdFunction<Node> {
        int invocations;
        Node lastParent;
        Node lastResult;
        int lastChildIndex;

        @Override
        public Node deriveChildKey(Node parent, int childIndex) {
            invocations++;
            lastParent = parent;
            lastChildIndex = childIndex;
            lastResult = new Node();
            return lastResult;
        }
    }

    private final MyCkdFunction ckdFunction = new MyCkdFunction();
    private final CkdFunction<Node> nodeCkdFunction = CkdFunctionResultCacheDecorator.newCacheOf(ckdFunction);

    @Test
    public void cacheMiss() {
        final Node parent = new Node();
        final Node childKey = nodeCkdFunction.deriveChildKey(parent, 123);
        assertEquals(1, ckdFunction.invocations);
        assertSame(childKey, ckdFunction.lastResult);
        assertSame(parent, ckdFunction.lastParent);
        assertEquals(123, ckdFunction.lastChildIndex);
    }

    @Test
    public void cacheMissSameParentDifferentIndex() {
        final Node parent = new Node();
        nodeCkdFunction.deriveChildKey(parent, 123);
        final Node childKey = nodeCkdFunction.deriveChildKey(parent, 456);
        assertEquals(2, ckdFunction.invocations);
        assertSame(childKey, ckdFunction.lastResult);
        assertSame(parent, ckdFunction.lastParent);
        assertEquals(456, ckdFunction.lastChildIndex);
    }

    @Test
    public void cacheMissDifferentParentSameIndex() {
        nodeCkdFunction.deriveChildKey(new Node(), 123);
        final Node parent = new Node();
        final Node childKey = nodeCkdFunction.deriveChildKey(parent, 123);
        assertEquals(2, ckdFunction.invocations);
        assertSame(childKey, ckdFunction.lastResult);
        assertSame(parent, ckdFunction.lastParent);
        assertEquals(123, ckdFunction.lastChildIndex);
    }

    @Test
    public void cacheHit() {
        final Node parent = new Node();
        nodeCkdFunction.deriveChildKey(parent, 123);
        final Node childKey = nodeCkdFunction.deriveChildKey(parent, 123);
        assertEquals(1, ckdFunction.invocations);
        assertSame(childKey, ckdFunction.lastResult);
        assertSame(parent, ckdFunction.lastParent);
    }
}