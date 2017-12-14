/*
 * Copyright 2017 PingCAP, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pingcap.tikv.util;

import static com.pingcap.tikv.value.Key.toKey;
import static org.junit.Assert.assertTrue;

import com.google.protobuf.ByteString;
import com.pingcap.tikv.types.DataType;
import com.pingcap.tikv.types.IntegerType;
import com.pingcap.tikv.value.Key;
import com.pingcap.tikv.value.TypedLiteral;
import java.util.function.Function;
import org.junit.Test;

public class KeyTest {
  @Test
  public void toKeyTest() throws Exception {
    // compared as unsigned
    testBytes(new byte[] {1, 2, -1, 10}, new byte[] {1, 2, 0, 10}, x -> x > 0);
    testBytes(new byte[] {1, 2, 0, 10}, new byte[] {1, 2, 0, 10}, x -> x == 0);
    testBytes(new byte[] {1, 2, 0, 10}, new byte[] {1, 2, 1, 10}, x -> x < 0);
    testBytes(new byte[] {1, 2, 0, 10}, new byte[] {1, 2, 0}, x -> x > 0);

    testLiteral(1, 2, IntegerType.DEF_LONG_TYPE, x -> x < 0);
    testLiteral(13, 13, IntegerType.DEF_LONG_TYPE, x -> x == 0);
    testLiteral(13, 2, IntegerType.DEF_LONG_TYPE, x -> x > 0);
    testLiteral(-1, 2, IntegerType.DEF_LONG_TYPE, x -> x < 0);
  }

  private void testBytes(byte[] lhs, byte[] rhs, Function<Integer, Boolean> tester) {
    ByteString lhsBS = ByteString.copyFrom(lhs);
    ByteString rhsBS = ByteString.copyFrom(rhs);

    Key lhsComp = toKey(lhsBS);
    Key rhsComp = toKey(rhsBS);

    assertTrue(tester.apply(lhsComp.compareTo(rhsComp)));

    lhsComp = toKey(lhs);
    rhsComp = toKey(rhs);

    assertTrue(tester.apply(lhsComp.compareTo(rhsComp)));
  }

  private void testLiteral(Object lhs, Object rhs, DataType type, Function<Integer, Boolean> tester) {
    Key lhsComp = TypedLiteral.create(lhs, type);
    Key rhsComp = TypedLiteral.create(rhs, type);

    assertTrue(tester.apply(lhsComp.compareTo(rhsComp)));
  }
}