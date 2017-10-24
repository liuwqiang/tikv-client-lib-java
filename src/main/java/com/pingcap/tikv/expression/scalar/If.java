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

package com.pingcap.tikv.expression.scalar;

import com.pingcap.tidb.tipb.ExprType;
import com.pingcap.tidb.tipb.ScalarFuncSig;
import com.pingcap.tikv.expression.TiExpr;
import com.pingcap.tikv.types.DataType;
import com.pingcap.tikv.types.IntegerType;
import com.pingcap.tikv.util.ScalarFuncInfer;

import static com.pingcap.tidb.tipb.ScalarFuncSig.*;

public class If extends ScalarFunction {
  public If(TiExpr... arg) {
    super(arg);
  }

  @Override
  ScalarFuncSig getSignature() {
    return ScalarFuncInfer.infer(
            getArgType(),
            IfInt,
            IfDecimal,
            IfReal,
            IfDuration,
            IfTime,
            IfString
    );
  }

  @Override
  protected ExprType getExprType() {
    return ExprType.If;
  }

  @Override
  public String getName() {
    return "If";
  }

  @Override
  protected void validateArguments(TiExpr... args) throws RuntimeException {
  }

  @Override
  public DataType getType() {
    return IntegerType.DEF_BOOLEAN_TYPE;
  }
}
