/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.avg;
import static org.jooq.impl.Factory.avgDistinct;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.countDistinct;
import static org.jooq.impl.Factory.cumeDist;
import static org.jooq.impl.Factory.denseRank;
import static org.jooq.impl.Factory.firstValue;
import static org.jooq.impl.Factory.groupConcat;
import static org.jooq.impl.Factory.lag;
import static org.jooq.impl.Factory.lead;
import static org.jooq.impl.Factory.listAgg;
import static org.jooq.impl.Factory.max;
import static org.jooq.impl.Factory.maxDistinct;
import static org.jooq.impl.Factory.median;
import static org.jooq.impl.Factory.min;
import static org.jooq.impl.Factory.minDistinct;
import static org.jooq.impl.Factory.ntile;
import static org.jooq.impl.Factory.percentRank;
import static org.jooq.impl.Factory.rank;
import static org.jooq.impl.Factory.regrAvgX;
import static org.jooq.impl.Factory.regrAvgY;
import static org.jooq.impl.Factory.regrCount;
import static org.jooq.impl.Factory.regrIntercept;
import static org.jooq.impl.Factory.regrR2;
import static org.jooq.impl.Factory.regrSXX;
import static org.jooq.impl.Factory.regrSXY;
import static org.jooq.impl.Factory.regrSYY;
import static org.jooq.impl.Factory.regrSlope;
import static org.jooq.impl.Factory.rowNumber;
import static org.jooq.impl.Factory.stddevPop;
import static org.jooq.impl.Factory.stddevSamp;
import static org.jooq.impl.Factory.sum;
import static org.jooq.impl.Factory.sumDistinct;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.varPop;
import static org.jooq.impl.Factory.varSamp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class AggregateWindowFunctionTests<
    A    extends UpdatableRecord<A>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public AggregateWindowFunctionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testSelectCountQuery() throws Exception {
        assertEquals(4, create().selectCount().from(TBook()).fetchOne(0));
        assertEquals(2, create().selectCount().from(TAuthor()).fetchOne(0));
    }

    @Test
    public void testAggregateFunctions() throws Exception {

        // Standard aggregate functions, available in all dialects:
        // --------------------------------------------------------
        Field<BigDecimal> median = median(TBook_ID());

        // Some dialects don't support a median function or a simulation thereof
        // Use AVG instead, as in this example the values of MEDIAN and AVG
        // are the same
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case INGRES:
            case MYSQL:
            case SQLITE:

            // TODO [#871] This could be simulated
            case SQLSERVER:
            case POSTGRES:
            case DB2:
                median = avg(TBook_ID());
                break;
        }

        Result<Record> result = create()
            .select(
                TBook_AUTHOR_ID(),
                count(),
                count(TBook_ID()),
                countDistinct(TBook_AUTHOR_ID()),
                sum(TBook_ID()),
                avg(TBook_ID()),
                min(TBook_ID()),
                max(TBook_ID()),
                median)
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID())
            .orderBy(TBook_AUTHOR_ID())
            .fetch();

        assertEquals(2, (int) result.get(0).getValue(1, Integer.class));
        assertEquals(2, (int) result.get(0).getValue(2, Integer.class));
        assertEquals(1, (int) result.get(0).getValue(3, Integer.class));
        assertEquals(3d, result.get(0).getValue(4, Double.class));
        assertEquals(1, (int) result.get(0).getValue(6, Integer.class));
        assertEquals(2, (int) result.get(0).getValue(7, Integer.class));

        assertEquals(2, (int) result.get(1).getValue(1, Integer.class));
        assertEquals(2, (int) result.get(1).getValue(2, Integer.class));
        assertEquals(1, (int) result.get(1).getValue(3, Integer.class));
        assertEquals(7d, result.get(1).getValue(4, Double.class));
        assertEquals(3, (int) result.get(1).getValue(6, Integer.class));
        assertEquals(4, (int) result.get(1).getValue(7, Integer.class));

        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(result.get(0).getValue(5, Double.class)));
        assertTrue(asList(1.0, 1.5, 2.0).contains(result.get(0).getValue(8, Double.class)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result.get(1).getValue(5, Double.class)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result.get(1).getValue(8, Double.class)));

        // [#1042] DISTINCT keyword
        // ------------------------

        // DB2 doesn't support multiple DISTINCT keywords in the same query...
        int distinct1 = create().select(countDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct2 = create().select(minDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct3 = create().select(maxDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct4 = create().select(sumDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        double distinct5 = create().select(avgDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Double.class);

        assertEquals(2, distinct1);
        assertEquals(1, distinct2);
        assertEquals(2, distinct3);
        assertEquals(3, distinct4);
        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(distinct5));

        // Statistical aggregate functions, available in some dialects:
        // ------------------------------------------------------------
        switch (getDialect()) {
            case DERBY:
            case FIREBIRD:
            case SQLITE:
                log.info("SKIPPING", "Statistical aggregate functions");
                break;

            default: {
                result = create()
                    .select(
                        TBook_AUTHOR_ID(),
                        stddevPop(TBook_ID()),
                        stddevSamp(TBook_ID()),
                        varPop(TBook_ID()),
                        varSamp(TBook_ID()))
                    .from(TBook())
                    .groupBy(TBook_AUTHOR_ID())
                    .orderBy(TBook_AUTHOR_ID())
                    .fetch();

                assertEquals(0.5, result.get(0).getValue(1, Double.class));
                assertEquals(0.25, result.get(0).getValue(3, Double.class));
                assertEquals(0.5, result.get(1).getValue(1, Double.class));
                assertEquals(0.25, result.get(1).getValue(3, Double.class));

                // DB2 only knows STDDEV_POP / VAR_POP
                if (getDialect() != SQLDialect.DB2) {
                    assertEquals("0.707", result.get(0).getValue(2, String.class).substring(0, 5));
                    assertEquals(0.5, result.get(0).getValue(4, Double.class));
                    assertEquals("0.707", result.get(1).getValue(2, String.class).substring(0, 5));
                    assertEquals(0.5, result.get(1).getValue(4, Double.class));
                }
            }
        }

        // [#873] Duplicate functions
        // --------------------------
        result =
        create().select(
                    TBook_AUTHOR_ID(),
                    max(TBook_ID()),
                    max(TBook_ID()))
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetch();

        assertEquals(2, (int) result.get(0).getValue(1, Integer.class));
        assertEquals(2, (int) result.get(0).getValue(2, Integer.class));
        assertEquals(4, (int) result.get(1).getValue(1, Integer.class));
        assertEquals(4, (int) result.get(1).getValue(2, Integer.class));
    }

    @Test
    public void testCountDistinct() throws Exception {

        // [#1728] COUNT(DISTINCT expr1, expr2, ...)
        // -----------------------------------------
        if (asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, INGRES, ORACLE, POSTGRES, SQLITE, SQLSERVER, SYBASE).contains(getDialect())) {
            log.info("SKIPPING", "Multi-expression COUNT(DISTINCT) test");
        }
        else {
            assertEquals(3, (int)
            create().select(countDistinct(TBook_AUTHOR_ID(), TBook_LANGUAGE_ID()))
                    .from(TBook())
                    .fetchOne(0, Integer.class));
        }
    }

    @Test
    public void testLinearRegressionFunctions() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "Skipping linear regression function tests");
                return;
        }

        // [#600] As aggregate functions
        Record record =
        create().select(
                    regrAvgX(TBook_ID(), TBook_AUTHOR_ID()),
                    regrAvgY(TBook_ID(), TBook_AUTHOR_ID()),
                    regrCount(TBook_ID(), TBook_AUTHOR_ID()),
                    regrIntercept(TBook_ID(), TBook_AUTHOR_ID()),
                    regrR2(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSlope(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSXX(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSXY(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSYY(TBook_ID(), TBook_AUTHOR_ID()))
                .from(TBook())
                .fetchOne();

        List<String> values = Arrays.asList("1.5", "2.5", "4.0", "-0.5", "0.8", "2.0", "1.0", "2.0", "5.0");
        assertEquals(values, Arrays.asList(roundStrings(1, record.into(String[].class))));

        switch (getDialect()) {
            case DB2:
                log.info("SKIPPING", "Skipping linear regression window function tests");
                return;
        }

        // [#600] As window functions
        Result<Record> result =
        create().select(
                    regrAvgX(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrAvgY(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrCount(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrIntercept(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrR2(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSlope(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSXX(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSXY(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSYY(TBook_ID(), TBook_AUTHOR_ID()).over())
                .from(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(values, Arrays.asList(roundStrings(1, result.get(0).into(String[].class))));
        assertEquals(values, Arrays.asList(roundStrings(1, result.get(1).into(String[].class))));
        assertEquals(values, Arrays.asList(roundStrings(1, result.get(2).into(String[].class))));
        assertEquals(values, Arrays.asList(roundStrings(1, result.get(3).into(String[].class))));
    }

    @Test
    public void testWindowFunctions() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case FIREBIRD:
            case INGRES:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "Window function tests");
                return;
        }

        switch (getDialect()) {
            case DERBY:
            case H2:
            case HSQLDB:

                // [#1535] TODO: Move this out of the switch statement. Oracle
                // and other databases should be able to support ORDER-BY-less
                // OVER() clauses for ranking functions

                // [#1523] Derby, H2 now support the ROW_NUMBER() OVER() window function
                // without any window clause, though. HSQLDB can simulate it using ROWNUM()
                List<Integer> rows =
                create().select(rowNumber().over()).from(TBook()).orderBy(TBook_ID()).fetch(0, Integer.class);
                assertEquals(asList(1, 2, 3, 4), rows);

                log.info("SKIPPING", "Advanced window function tests");
                return;
        }

        int column = 0;

        // ROW_NUMBER()
        Result<Record> result =
        create().select(TBook_ID(),
// [#1535] TODO:        rowNumber().over(),

                        // [#1958] Check if expressions in ORDER BY clauess work
                        // correctly for all databases
                        rowNumber().over()
                                   .partitionByOne()
                                   .orderBy(
                                       TBook_ID().mul(2).desc(),
                                       TBook_ID().add(1).desc()),
                        rowNumber().over()
                                   .partitionBy(TBook_AUTHOR_ID())
                                   .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

//        // [#1535] No ORDER BY clause
//        column++;
//        assertEquals(BOOK_IDS, result.getValues(column));

        // Ordered ROW_NUMBER()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered ROW_NUMBER()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        column = 0;

        // COUNT()
        result =
        create().select(TBook_ID(),
                        count().over(),
                        count().over().partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Partitioned and ordered COUNT()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(4), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(2), result.getValue(3, column));

        column = 0;

        // RANK(), DENSE_RANK()
        result =
        create().select(TBook_ID(),
                        rank().over().orderBy(TBook_ID().desc()),
                        rank().over().partitionBy(TBook_AUTHOR_ID())
                                     .orderBy(TBook_ID().desc()),
                        denseRank().over().orderBy(TBook_ID().desc()),
                        denseRank().over().partitionBy(TBook_AUTHOR_ID())
                                          .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Ordered RANK()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered RANK()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Ordered DENSE_RANK()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered DENSE_RANK()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        switch (getDialect()) {
            case DB2:
            case SQLSERVER:
                log.info("SKIPPING", "PERCENT_RANK() and CUME_DIST() window function tests");
                break;

            default: {
                column = 0;

                // PERCENT_RANK() and CUME_DIST()
                result =
                create().select(TBook_ID(),
                                percentRank().over().orderBy(TBook_ID().desc()),
                                percentRank().over().partitionBy(TBook_AUTHOR_ID())
                                                    .orderBy(TBook_ID().desc()),
                                cumeDist().over().orderBy(TBook_ID().desc()),
                                cumeDist().over().partitionBy(TBook_AUTHOR_ID())
                                                 .orderBy(TBook_ID().desc()))
                        .from(TBook())
                        .orderBy(TBook_ID().asc())
                        .fetch();

                // Ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class));
                assertEquals("0.6", result.get(1).getValue(column, String.class).substring(0, 3));
                assertEquals("0.3", result.get(2).getValue(column, String.class).substring(0, 3));
                assertEquals("0", result.get(3).getValue(column, String.class));

                // Partitioned and ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class));
                assertEquals("0", result.get(1).getValue(column, String.class));
                assertEquals("1", result.get(2).getValue(column, String.class));
                assertEquals("0", result.get(3).getValue(column, String.class));

                // Ordered CUME_DIST()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class));
                assertEquals("0.75", result.get(1).getValue(column, String.class));
                assertEquals("0.5", result.get(2).getValue(column, String.class));
                assertEquals("0.25", result.get(3).getValue(column, String.class));

                // Partitioned and ordered CUME_DIST()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class));
                assertEquals("0.5", result.get(1).getValue(column, String.class));
                assertEquals("1", result.get(2).getValue(column, String.class));
                assertEquals("0.5", result.get(3).getValue(column, String.class));

                break;
            }
        }

        column = 0;

        // MAX()
        result =
        create().select(TBook_ID(),
                        max(TBook_ID()).over()
                                       .partitionByOne(),
                        max(TBook_ID()).over()
                                       .partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall MAX()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(4), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        // Partitioned MAX()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        column = 0;

        // STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        result =
        create().select(TBook_ID(),
                        stddevPop(TBook_ID()).over().partitionByOne(),
                        stddevSamp(TBook_ID()).over().partitionByOne(),
                        varPop(TBook_ID()).over().partitionByOne(),
                        varSamp(TBook_ID()).over().partitionByOne(),

                        stddevPop(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        stddevSamp(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        varPop(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        varSamp(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals("1.118", result.get(0).getValue(1, String.class).substring(0, 5));
        assertEquals(1.25, result.get(0).getValue(3, Double.class));

        // Partitioned STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals(0.5, result.get(0).getValue(5, Double.class));
        assertEquals(0.25, result.get(0).getValue(7, Double.class));

        // DB2 only knows STDDEV_POP / VAR_POP
        if (getDialect() != SQLDialect.DB2) {
            assertEquals("1.290", result.get(0).getValue(2, String.class).substring(0, 5));
            assertEquals("1.666", result.get(0).getValue(4, String.class).substring(0, 5));
            assertEquals("0.707", result.get(0).getValue(6, String.class).substring(0, 5));
            assertEquals(0.5, result.get(0).getValue(8, Double.class));
        }

        // NTILE()
        if (asList(SYBASE, DB2).contains(getDialect())) {
            log.info("SKIPPING", "NTILE tests");
        }
        else {
            result =
            create().select(TBook_ID(),
                            ntile(1).over().orderBy(TBook_ID()),
                            ntile(1).over().partitionBy(TBook_AUTHOR_ID()).orderBy(TBook_ID()),
                            ntile(2).over().orderBy(TBook_ID()),
                            ntile(2).over().partitionBy(TBook_AUTHOR_ID()).orderBy(TBook_ID()))
                    .from(TBook())
                    .orderBy(TBook_ID().asc())
                    .fetch();

            assertEquals(BOOK_IDS, result.getValues(0));
            assertEquals(nCopies(4, 1), result.getValues(1));
            assertEquals(nCopies(4, 1), result.getValues(2));
            assertEquals(asList(1, 1, 2, 2), result.getValues(3));
            assertEquals(asList(1, 2, 1, 2), result.getValues(4));
        }

        column = 0;
        if (getDialect() == SQLDialect.SQLSERVER) {
            log.info("SKIPPING", "ROWS UNBOUNDED PRECEDING and similar tests");
            return;
        }

        // SUM()
        result =
        create().select(TBook_ID(),
                        sum(TBook_ID()).over().partitionByOne(),
                        sum(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        sum(TBook_ID()).over().orderBy(TBook_ID().asc())
                                              .rowsBetweenUnboundedPreceding()
                                              .andPreceding(1))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall SUM()
        column++;
        assertEquals(new BigDecimal("10"), result.getValue(0, column));
        assertEquals(new BigDecimal("10"), result.getValue(1, column));
        assertEquals(new BigDecimal("10"), result.getValue(2, column));
        assertEquals(new BigDecimal("10"), result.getValue(3, column));

        // Partitioned SUM()
        column++;
        assertEquals(new BigDecimal("3"), result.getValue(0, column));
        assertEquals(new BigDecimal("3"), result.getValue(1, column));
        assertEquals(new BigDecimal("7"), result.getValue(2, column));
        assertEquals(new BigDecimal("7"), result.getValue(3, column));

        // Ordered SUM() with ROWS
        column++;
        assertEquals(null, result.getValue(0, column));
        assertEquals(new BigDecimal("1"), result.getValue(1, column));
        assertEquals(new BigDecimal("3"), result.getValue(2, column));
        assertEquals(new BigDecimal("6"), result.getValue(3, column));

        column = 0;

        // FIRST_VALUE()
        result =
        create().select(TBook_ID(),
                        firstValue(TBook_ID()).over()
                                              .partitionBy(TBook_AUTHOR_ID())
                                              .orderBy(TBook_PUBLISHED_IN().asc())
                                              .rowsBetweenUnboundedPreceding()
                                              .andUnboundedFollowing())
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Partitioned and ordered FIRST_VALUE() with ROWS
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(3), result.getValue(2, column));
        assertEquals(Integer.valueOf(3), result.getValue(3, column));

        switch (getDialect()) {
            case POSTGRES:
                log.info("SKIPPING", "FIRST_VALUE(... IGNORE NULLS) window function test");
                break;

            default: {
                column = 0;

                // FIRST_VALUE(... IGNORE NULLS)
                result = create().select(TBook_ID(),
                                         firstValue(TBook_ID()).ignoreNulls()
                                                               .over()
                                                               .partitionBy(TBook_AUTHOR_ID())
                                                               .orderBy(TBook_PUBLISHED_IN().asc())
                                                               .rowsBetweenUnboundedPreceding()
                                                               .andUnboundedFollowing())
                                 .from(TBook())
                                 .orderBy(TBook_ID().asc())
                                 .fetch();

                // Partitioned and ordered FIRST_VALUE(... IGNORE NULLS) with ROWS
                column++;
                assertEquals(Integer.valueOf(2), result.getValue(0, column));
                assertEquals(Integer.valueOf(2), result.getValue(1, column));
                assertEquals(Integer.valueOf(3), result.getValue(2, column));
                assertEquals(Integer.valueOf(3), result.getValue(3, column));

                break;
            }
        }

        switch (getDialect()) {
            case SYBASE:
                log.info("SKIPPING", "LEAD/LAG tests");
                break;

            default: {
                column = 0;

                // LEAD() and LAG()
                result =
                create().select(TBook_ID(),
                                lead(TBook_ID()).over()
                                                .partitionByOne()
                                                .orderBy(TBook_ID().asc()),
                                lead(TBook_ID()).over()
                                                .partitionBy(TBook_AUTHOR_ID())
                                                .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2).over()
                                                   .partitionByOne()
                                                   .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2).over()
                                                   .partitionBy(TBook_AUTHOR_ID())
                                                   .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2, 55).over()
                                                       .partitionByOne()
                                                       .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2, 55).over()
                                                       .partitionBy(TBook_AUTHOR_ID())
                                                       .orderBy(TBook_ID().asc()),

                                lag(TBook_ID()).over()
                                               .partitionByOne()
                                               .orderBy(TBook_ID().asc()),
                                lag(TBook_ID()).over()
                                               .partitionBy(TBook_AUTHOR_ID())
                                               .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2).over()
                                                  .partitionByOne()
                                                  .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2).over()
                                                  .partitionBy(TBook_AUTHOR_ID())
                                                  .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2, val(55)).over()
                                                           .partitionByOne()
                                                           .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2, val(55)).over()
                                                           .partitionBy(TBook_AUTHOR_ID())
                                                           .orderBy(TBook_ID().asc()))
                        .from(TBook())
                        .orderBy(TBook_ID().asc())
                        .fetch();

                // Overall LEAD()
                column++;
                assertEquals(2, result.getValue(0, column));
                assertEquals(3, result.getValue(1, column));
                assertEquals(4, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Partitioned LEAD()
                column++;
                assertEquals(2, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(4, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LEAD(2)
                column++;
                assertEquals(3, result.getValue(0, column));
                assertEquals(4, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Partitioned LEAD(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LEAD(2, 55)
                column++;
                assertEquals(3, result.getValue(0, column));
                assertEquals(4, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));

                // Partitioned LEAD(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));


                // Overall LAG()
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(1, result.getValue(1, column));
                assertEquals(2, result.getValue(2, column));
                assertEquals(3, result.getValue(3, column));

                // Partitioned LAG()
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(1, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(3, result.getValue(3, column));

                // Overall LAG(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(1, result.getValue(2, column));
                assertEquals(2, result.getValue(3, column));

                // Partitioned LAG(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LAG(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(1, result.getValue(2, column));
                assertEquals(2, result.getValue(3, column));

                // Partitioned LAG(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));

                break;
            }
        }
    }

    @Test
    public void testListAgg() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case INGRES:
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "LISTAGG tests");
                return;
        }

        Result<?> result1 = create().select(
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME(),
                listAgg(TBook_ID(), ", ")
                    .withinGroupOrderBy(TBook_ID().desc())
                    .as("books1"),
                groupConcat(TBook_ID())
                    .orderBy(TBook_ID().desc())
                    .separator(", ")
                    .as("books2"))
            .from(TAuthor())
            .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
            .groupBy(
                TAuthor_ID(),
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(2, result1.size());
        assertEquals(AUTHOR_FIRST_NAMES, result1.getValues(TAuthor_FIRST_NAME()));
        assertEquals(AUTHOR_LAST_NAMES, result1.getValues(TAuthor_LAST_NAME()));
        assertEquals("2, 1", result1.getValue(0, "books1"));
        assertEquals("2, 1", result1.getValue(0, "books2"));
        assertEquals("4, 3", result1.getValue(1, "books1"));
        assertEquals("4, 3", result1.getValue(1, "books2"));

        switch (getDialect()) {
            case CUBRID:
            case DB2:
            case H2:
            case HSQLDB:
            case MYSQL:
            case POSTGRES:
            case SYBASE:
                log.info("SKIPPING", "LISTAGG window function tests");
                return;
        }

        Result<?> result2 = create().select(
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME(),
                listAgg(TBook_TITLE())
                   .withinGroupOrderBy(TBook_ID().asc())
                   .over().partitionBy(TAuthor_ID()))
           .from(TAuthor())
           .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
           .orderBy(TBook_ID())
           .fetch();

        assertEquals(4, result2.size());
        assertEquals(BOOK_FIRST_NAMES, result2.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result2.getValues(TAuthor_LAST_NAME()));
        assertEquals("1984Animal Farm", result2.getValue(0, 2));
        assertEquals("1984Animal Farm", result2.getValue(1, 2));
        assertEquals("O AlquimistaBrida", result2.getValue(2, 2));
        assertEquals("O AlquimistaBrida", result2.getValue(3, 2));
    }
}
