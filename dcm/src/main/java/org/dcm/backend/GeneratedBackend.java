/*
 * Copyright © 2018-2020 VMware, Inc. All Rights Reserved.
 *
 * SPDX-License-Identifier: BSD-2
 */

package org.dcm.backend;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import org.dcm.IRContext;
import org.dcm.IRTable;
import org.dcm.ModelException;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record15;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Generated("org.dcm.backend.OrToolsSolver")
@SuppressWarnings("all")
public final class GeneratedBackend implements IGeneratedBackend {
  public Map<IRTable, Result<? extends Record>> solve(final IRContext context) {
    // Create the model.
    final long startTime = System.nanoTime();
    final CpModel model = new CpModel();
    final StringEncoding encoder = new StringEncoding();
    final Ops o = new Ops(model, encoder, false);


    /* Table "INTER_POD_ANTI_AFFINITY_MATCHES_PENDING" */
    final List<Record3<String, Object[], Integer>> interPodAntiAffinityMatchesPending = (List<Record3<String, Object[], Integer>>) context.getTable("INTER_POD_ANTI_AFFINITY_MATCHES_PENDING").getCurrentData();

    /* Table "INTER_POD_AFFINITY_MATCHES_SCHEDULED" */
    final List<Record4<String, String, String, Integer>> interPodAffinityMatchesScheduled = (List<Record4<String, String, String, Integer>>) context.getTable("INTER_POD_AFFINITY_MATCHES_SCHEDULED").getCurrentData();

    /* Table "INTER_POD_AFFINITY_MATCHES_PENDING" */
    final List<Record3<String, Object[], Integer>> interPodAffinityMatchesPending = (List<Record3<String, Object[], Integer>>) context.getTable("INTER_POD_AFFINITY_MATCHES_PENDING").getCurrentData();

    /* Table "NODES_THAT_HAVE_TOLERATIONS" */
    final List<Record1<String>> nodesThatHaveTolerations = (List<Record1<String>>) context.getTable("NODES_THAT_HAVE_TOLERATIONS").getCurrentData();

    /* Table "PODS_TO_ASSIGN" */
    final List<Record15<String, String, String, String, Integer, Integer, Integer, Integer, String, String, Boolean, Boolean, Boolean, Integer, String>> podsToAssign = (List<Record15<String, String, String, String, Integer, Integer, Integer, Integer, String, String, Boolean, Boolean, Boolean, Integer, String>>) context.getTable("PODS_TO_ASSIGN").getCurrentData();
    final IntVar[] podsToAssignControllableNodeName = new IntVar[podsToAssign.size()];
    final Map<String, IntVar>  podsToAssignControllableNodeNameChannel = new HashMap<>();
    for (int i = 0; i < podsToAssign.size(); i++) {
      podsToAssignControllableNodeName[i] = INT_VAR_NO_BOUNDS(model, "CONTROLLABLE__NODE_NAME");
      podsToAssignControllableNodeNameChannel.put(podsToAssign.get(i).get("POD_NAME", String.class), podsToAssignControllableNodeName[i]);
    }

    /* Table "PODS_THAT_TOLERATE_NODE_TAINTS" */
    final List<Record2<String, String>> podsThatTolerateNodeTaints = (List<Record2<String, String>>) context.getTable("PODS_THAT_TOLERATE_NODE_TAINTS").getCurrentData();

    /* Table "POD_NODE_SELECTOR_MATCHES" */
    final List<Record2<String, String>> podNodeSelectorMatches = (List<Record2<String, String>>) context.getTable("POD_NODE_SELECTOR_MATCHES").getCurrentData();

    /* Table "SPARE_CAPACITY_PER_NODE" */
    final List<Record4<String, Integer, Integer, Integer>> spareCapacityPerNode = (List<Record4<String, Integer, Integer, Integer>>) context.getTable("SPARE_CAPACITY_PER_NODE").getCurrentData();
    System.out.println("Array declarations: we are at " + (System.nanoTime() - startTime));;

    /* Constraint view constraint_pod_affinity */
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      final var i0 = podsToAssign.get(podsToAssignIter).get("HAS_POD_AFFINITY_REQUIREMENTS", Boolean.class);
      final var i1 = podsToAssignControllableNodeName[podsToAssignIter];
      final var i2 = (o.eq(i0, false));

      /* Non-constraint view subquery1 */
      final List<IntVar> listOfi10 = new java.util.ArrayList<>();
      final java.util.List<Tuple5<IntVar, String, String, Object[], String>> subquery1 = new java.util.ArrayList<>();
      for (int interPodAffinityMatchesPendingIter = 0; interPodAffinityMatchesPendingIter < interPodAffinityMatchesPending.size(); interPodAffinityMatchesPendingIter++) {
        for (int bIter = 0; bIter < podsToAssign.size(); bIter++) {
          final var i3 = podsToAssignControllableNodeName[bIter];
          final var i4 = interPodAffinityMatchesPending.get(interPodAffinityMatchesPendingIter).get("POD_NAME", String.class);
          final var i5 = podsToAssign.get(podsToAssignIter).get("POD_NAME", String.class);
          final var i6 = interPodAffinityMatchesPending.get(interPodAffinityMatchesPendingIter).get("MATCHES", Object[].class);
          final var i7 = podsToAssign.get(bIter).get("POD_NAME", String.class);
          final var i8 = (o.eq(i4, i5));
          final var i9 = o.in(i7, i6);
          if (!(i8 
              && i9)) {
            continue;
          }
          final Tuple5<IntVar, String, String, Object[], String> t = new Tuple5<>(
                  i3 /* CONTROLLABLE__NODE_NAME */,
                  i4 /* POD_NAME */,
                  i5 /* POD_NAME */,
                  i6 /* MATCHES */,
                  i7 /* POD_NAME */
                  );
          subquery1.add(t);
          final var i10 = t.value0();
          listOfi10.add(i10);
        }
      }
      final var i11 = listOfi10;
      final var i12 = o.inIntVar(i1, i11);
      final var i13 = o.or(i2, i12);

      /* Non-constraint view subquery2 */
      final List<String> listOfi18 = new java.util.ArrayList<>();
      final java.util.List<Tuple3<String, String, String>> subquery2 = new java.util.ArrayList<>();
      for (int interPodAffinityMatchesScheduledIter = 0; interPodAffinityMatchesScheduledIter < interPodAffinityMatchesScheduled.size(); interPodAffinityMatchesScheduledIter++) {
        final var i14 = interPodAffinityMatchesScheduled.get(interPodAffinityMatchesScheduledIter).get("NODE_NAME", String.class);
        final var i15 = podsToAssign.get(podsToAssignIter).get("POD_NAME", String.class);
        final var i16 = interPodAffinityMatchesScheduled.get(interPodAffinityMatchesScheduledIter).get("POD_NAME", String.class);
        final var i17 = (o.eq(i15, i16));
        if (!(i17)) {
          continue;
        }
        final Tuple3<String, String, String> t = new Tuple3<>(
                i14 /* NODE_NAME */,
                i15 /* POD_NAME */,
                i16 /* POD_NAME */
                );
        subquery2.add(t);
        final var i18 = t.value0();
        listOfi18.add(i18);
      }
      final var i19 = listOfi18;
      final var i20 = o.inString(i1, i19);
      final var i21 = o.or(i13, i20);
      model.addEquality(i21, 1);
    }

    /* Constraint view tmp0 */
    final Map<Tuple1<Integer>, java.util.List<Tuple1<IntVar>>> tmp0 = new java.util.HashMap<>();
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      final var i22 = podsToAssignControllableNodeName[podsToAssignIter];
      final Tuple1<IntVar> t = new Tuple1<>(
              i22 /* CONTROLLABLE__NODE_NAME */
              );
      final Tuple1<Integer> groupByTuple = new Tuple1<>(
              podsToAssign.get(podsToAssignIter).get("EQUIVALENCE_CLASS", Integer.class)
              );
      tmp0.computeIfAbsent(groupByTuple, (k) -> new java.util.ArrayList<>()).add(t);
    }

    System.out.println("Group-by intermediate view: we are at " + (System.nanoTime() - startTime));;
    /* Constraint view constraintSymmetryBreaking */
    for (final java.util.Map.Entry<Tuple1<Integer>, List<Tuple1<IntVar>>> entry: tmp0.entrySet()) {
      final Tuple1<Integer> group = entry.getKey();
      final List<Tuple1<IntVar>> data = entry.getValue();
      final List<IntVar> listOfi23 = new java.util.ArrayList<>(data.size());
      for (final Tuple1<IntVar> t: data) {
        final var i23 = t.value0();
        listOfi23.add(i23);
      }
      o.increasing(listOfi23);
      final var i24 = model.newConstant(1);
      final var i25 = o.eq(i24, true);
      model.addEquality(i25, 1);
    }
    System.out.println("Group-by final view: we are at " + (System.nanoTime() - startTime));

    final Map<String, Record3<String, Object[], Integer>> interPodAntiAffinityMatchesPendingIndex = new HashMap<>();
    interPodAntiAffinityMatchesPending.forEach(
            e -> interPodAntiAffinityMatchesPendingIndex.put(e.get("POD_NAME", String.class), e)
    );

    /* Constraint view constraint_pod_anti_affinity_pending */
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      final var i26 = podsToAssign.get(podsToAssignIter).get("HAS_POD_ANTI_AFFINITY_REQUIREMENTS", Boolean.class);
      final var i27 = podsToAssignControllableNodeName[podsToAssignIter];
      final var i28 = (o.eq(i26, false));

      if (i28) {
        continue;
      }
      /* Non-constraint view subquery3 */
      final List<IntVar> listOfi36 = new java.util.ArrayList<>();
      final java.util.List<Tuple5<IntVar, String, String, Object[], String>> subquery3 = new java.util.ArrayList<>();
      final var i31 = podsToAssign.get(podsToAssignIter).get("POD_NAME", String.class);
      final var i32 = interPodAntiAffinityMatchesPendingIndex.get(i31);
      if (i32 == null) {
        continue;
      }
      final var i33 = i32.get("MATCHES", Object[].class);
      for (int i33Iter = 0; i33Iter < i33.length; i33Iter++) {
        final var i34 = podsToAssignControllableNodeNameChannel.get((String) i33[i33Iter]);
        listOfi36.add(i34);
      }
      final var i37 = listOfi36;
      o.notInIntVarHalf(i27, i37);
    }

    /* Constraint view constraint_controllable_node_name_domain */
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      final var i41 = podsToAssignControllableNodeName[podsToAssignIter];

      /* Non-constraint view subquery4 */
      final List<String> listOfi43 = new java.util.ArrayList<>();
      final java.util.List<Tuple1<String>> subquery4 = new java.util.ArrayList<>();
      for (int spareCapacityPerNodeIter = 0; spareCapacityPerNodeIter < spareCapacityPerNode.size(); spareCapacityPerNodeIter++) {
        final var i42 = spareCapacityPerNode.get(spareCapacityPerNodeIter).get("NAME", String.class);
        final Tuple1<String> t = new Tuple1<>(
                i42 /* NAME */
                );
        subquery4.add(t);
        final var i43 = t.value0();
        listOfi43.add(i43);
      }
      final var i44 = listOfi43;
      final var i45 = o.inString(i41, i44);
      model.addEquality(i45, 1);
    }
    final List<IntVar> listOfi46 = new java.util.ArrayList<>();
    final List<String> listOfi47 = new java.util.ArrayList<>();
    final List<Integer> listOfi48 = new java.util.ArrayList<>();
    final List<Integer> listOfi49 = new java.util.ArrayList<>();
    final List<Integer> listOfi50 = new java.util.ArrayList<>();
    final List<Integer> listOfi51 = new java.util.ArrayList<>();
    final List<Integer> listOfi52 = new java.util.ArrayList<>();
    final List<Integer> listOfi53 = new java.util.ArrayList<>();
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      final var i46 = podsToAssignControllableNodeName[podsToAssignIter];
      listOfi46.add(i46);
      final var i48 = podsToAssign.get(podsToAssignIter).get("CPU_REQUEST", Integer.class);
      listOfi48.add(i48);
      final var i50 = podsToAssign.get(podsToAssignIter).get("MEMORY_REQUEST", Integer.class);
      listOfi50.add(i50);
      final var i52 = podsToAssign.get(podsToAssignIter).get("PODS_REQUEST", Integer.class);
      listOfi52.add(i52);
    }
    for (int spareCapacityPerNodeIter = 0; spareCapacityPerNodeIter < spareCapacityPerNode.size(); spareCapacityPerNodeIter++) {
      final var i47 = spareCapacityPerNode.get(spareCapacityPerNodeIter).get("NAME", String.class);
      listOfi47.add(i47);
      final var i49 = spareCapacityPerNode.get(spareCapacityPerNodeIter).get("CPU_REMAINING", Integer.class);
      listOfi49.add(i49);
      final var i51 = spareCapacityPerNode.get(spareCapacityPerNodeIter).get("MEMORY_REMAINING", Integer.class);
      listOfi51.add(i51);
      final var i53 = spareCapacityPerNode.get(spareCapacityPerNodeIter).get("PODS_REMAINING", Integer.class);
      listOfi53.add(i53);
    }
    o.capacityConstraint(listOfi46, listOfi47, java.util.List.of(listOfi48, listOfi50, listOfi52), java.util.List.of(listOfi49, listOfi51, listOfi53));
    /* Constraint view constraint_node_taints */
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      for (int nodesThatHaveTolerationsIter = 0; nodesThatHaveTolerationsIter < nodesThatHaveTolerations.size(); nodesThatHaveTolerationsIter++) {
        final var i54 = podsToAssignControllableNodeName[podsToAssignIter];
        final var i55 = nodesThatHaveTolerations.get(nodesThatHaveTolerationsIter).get("NODE_NAME", String.class);
        final var i56 = o.eq(i54, i55);
        final var i57 = o.and(i56, i56);

        /* Non-constraint view subquery5 */
        final List<IntVar> listOfi64 = new java.util.ArrayList<>();
        final java.util.List<Tuple4<String, IntVar, String, String>> subquery5 = new java.util.ArrayList<>();
        for (int aIter = 0; aIter < podsThatTolerateNodeTaints.size(); aIter++) {
          final var i58 = podsThatTolerateNodeTaints.get(aIter).get("NODE_NAME", String.class);
          final var i59 = podsThatTolerateNodeTaints.get(aIter).get("POD_NAME", String.class);
          final var i60 = podsToAssign.get(podsToAssignIter).get("POD_NAME", String.class);
          final var i61 = (o.eq(i59, i60));
          if (!(i61)) {
            continue;
          }
          final Tuple4<String, IntVar, String, String> t = new Tuple4<>(
                  i58 /* NODE_NAME */,
                  i54 /* CONTROLLABLE__NODE_NAME */,
                  i59 /* POD_NAME */,
                  i60 /* POD_NAME */
                  );
          subquery5.add(t);
          final var i62 = t.value0();
          final var i63 = t.value1();
          final var i64 = o.eq(i62, i63);
          listOfi64.add(i64);
        }
        final var i65 = listOfi64;
        final var i66 = o.exists(i65);
        final var i67 = o.eq(i66, true);
        model.addImplication(i57, i67);
      }
    }

    /* Constraint view constraint_node_selector */
    for (int podsToAssignIter = 0; podsToAssignIter < podsToAssign.size(); podsToAssignIter++) {
      final var i68 = podsToAssign.get(podsToAssignIter).get("HAS_NODE_SELECTOR_LABELS", Boolean.class);
      final var i69 = podsToAssignControllableNodeName[podsToAssignIter];
      final var i70 = (o.eq(i68, false));

      /* Non-constraint view subquery6 */
      final List<String> listOfi75 = new java.util.ArrayList<>();
      final java.util.List<Tuple3<String, String, String>> subquery6 = new java.util.ArrayList<>();
      for (int podNodeSelectorMatchesIter = 0; podNodeSelectorMatchesIter < podNodeSelectorMatches.size(); podNodeSelectorMatchesIter++) {
        final var i71 = podNodeSelectorMatches.get(podNodeSelectorMatchesIter).get("NODE_NAME", String.class);
        final var i72 = podsToAssign.get(podsToAssignIter).get("POD_NAME", String.class);
        final var i73 = podNodeSelectorMatches.get(podNodeSelectorMatchesIter).get("POD_NAME", String.class);
        final var i74 = (o.eq(i72, i73));
        if (!(i74)) {
          continue;
        }
        final Tuple3<String, String, String> t = new Tuple3<>(
                i71 /* NODE_NAME */,
                i72 /* POD_NAME */,
                i73 /* POD_NAME */
                );
        subquery6.add(t);
        final var i75 = t.value0();
        listOfi75.add(i75);
      }
      final var i76 = listOfi75;
      final var i77 = o.inString(i69, i76);
      final var i78 = o.or(i70, i77);
      model.addEquality(i78, 1);
    }

    // Start solving
    System.out.println("Model creation: we are at " + (System.nanoTime() - startTime));;
    final CpSolver solver = new CpSolver();
    solver.getParameters().setLogSearchProgress(true);
    solver.getParameters().setCpModelProbingLevel(0);
    solver.getParameters().setNumSearchWorkers(4);
    solver.getParameters().setMaxTimeInSeconds(1);
    final CpSolverStatus status = solver.solve(model);
    if (status == CpSolverStatus.FEASIBLE || status == CpSolverStatus.OPTIMAL) {
      final Map<IRTable, Result<? extends Record>> result = new HashMap<>();
      final Object[] obj = new Object[1]; // Used to update controllable fields;
      final Result<? extends Record> tmp2 = context.getTable("PODS_TO_ASSIGN").getCurrentData();
      for (int i = 0; i < podsToAssign.size(); i++) {
        obj[0] = encoder.toStr(solver.value(podsToAssignControllableNodeName[i]));
        tmp2.get(i).from(obj, "CONTROLLABLE__NODE_NAME");
      }
      result.put(context.getTable("PODS_TO_ASSIGN"), tmp2);
      return result;
    }
    throw new ModelException("Could not solve " + status);
  }

  private IntVar INT_VAR_NO_BOUNDS(final CpModel model, final String name) {
    return model.newIntVar(Integer.MIN_VALUE, Integer.MAX_VALUE, name);
  }

  private static final class Tuple1<T0> {
    private final T0 t0;

    private Tuple1(final T0 t0) {
      this.t0 = t0;
    }

    T0 value0() {
      return t0;
    }

    @Override
    public String toString() {
      return String.format(("%s"), t0);
    }

    @Override
    public int hashCode() {
      return Objects.hash(t0);
    }

    @Override
    public boolean equals(final Object other) {
      if (other == this) {
        return true;
      }
      if (!(other instanceof Tuple1)) {
        return false;
      }
      final Tuple1 that = (Tuple1) other;
      return this.value0().equals(that.value0());
    }
  }

  private static final class Tuple2<T0, T1> {
    private final T0 t0;

    private final T1 t1;

    private Tuple2(final T0 t0, final T1 t1) {
      this.t0 = t0;
      this.t1 = t1;
    }

    T0 value0() {
      return t0;
    }

    T1 value1() {
      return t1;
    }

    @Override
    public String toString() {
      return String.format(("%s,%s"), t0, t1);
    }

    @Override
    public int hashCode() {
      return Objects.hash(t0, t1);
    }

    @Override
    public boolean equals(final Object other) {
      if (other == this) {
        return true;
      }
      if (!(other instanceof Tuple2)) {
        return false;
      }
      final Tuple2 that = (Tuple2) other;
      return this.value0().equals(that.value0()) && this.value1().equals(that.value1());
    }
  }

  private static final class Tuple3<T0, T1, T2> {
    private final T0 t0;

    private final T1 t1;

    private final T2 t2;

    private Tuple3(final T0 t0, final T1 t1, final T2 t2) {
      this.t0 = t0;
      this.t1 = t1;
      this.t2 = t2;
    }

    T0 value0() {
      return t0;
    }

    T1 value1() {
      return t1;
    }

    T2 value2() {
      return t2;
    }

    @Override
    public String toString() {
      return String.format(("%s,%s,%s"), t0, t1, t2);
    }

    @Override
    public int hashCode() {
      return Objects.hash(t0, t1, t2);
    }

    @Override
    public boolean equals(final Object other) {
      if (other == this) {
        return true;
      }
      if (!(other instanceof Tuple3)) {
        return false;
      }
      final Tuple3 that = (Tuple3) other;
      return this.value0().equals(that.value0()) && this.value1().equals(that.value1()) && this.value2().equals(that.value2());
    }
  }

  private static final class Tuple4<T0, T1, T2, T3> {
    private final T0 t0;

    private final T1 t1;

    private final T2 t2;

    private final T3 t3;

    private Tuple4(final T0 t0, final T1 t1, final T2 t2, final T3 t3) {
      this.t0 = t0;
      this.t1 = t1;
      this.t2 = t2;
      this.t3 = t3;
    }

    T0 value0() {
      return t0;
    }

    T1 value1() {
      return t1;
    }

    T2 value2() {
      return t2;
    }

    T3 value3() {
      return t3;
    }

    @Override
    public String toString() {
      return String.format(("%s,%s,%s,%s"), t0, t1, t2, t3);
    }

    @Override
    public int hashCode() {
      return Objects.hash(t0, t1, t2, t3);
    }

    @Override
    public boolean equals(final Object other) {
      if (other == this) {
        return true;
      }
      if (!(other instanceof Tuple4)) {
        return false;
      }
      final Tuple4 that = (Tuple4) other;
      return this.value0().equals(that.value0()) && this.value1().equals(that.value1()) && this.value2().equals(that.value2()) && this.value3().equals(that.value3());
    }
  }

  private static final class Tuple5<T0, T1, T2, T3, T4> {
    private final T0 t0;

    private final T1 t1;

    private final T2 t2;

    private final T3 t3;

    private final T4 t4;

    private Tuple5(final T0 t0, final T1 t1, final T2 t2, final T3 t3, final T4 t4) {
      this.t0 = t0;
      this.t1 = t1;
      this.t2 = t2;
      this.t3 = t3;
      this.t4 = t4;
    }

    T0 value0() {
      return t0;
    }

    T1 value1() {
      return t1;
    }

    T2 value2() {
      return t2;
    }

    T3 value3() {
      return t3;
    }

    T4 value4() {
      return t4;
    }

    @Override
    public String toString() {
      return String.format(("%s,%s,%s,%s,%s"), t0, t1, t2, t3, t4);
    }

    @Override
    public int hashCode() {
      return Objects.hash(t0, t1, t2, t3, t4);
    }

    @Override
    public boolean equals(final Object other) {
      if (other == this) {
        return true;
      }
      if (!(other instanceof Tuple5)) {
        return false;
      }
      final Tuple5 that = (Tuple5) other;
      return this.value0().equals(that.value0()) && this.value1().equals(that.value1()) && this.value2().equals(that.value2()) && this.value3().equals(that.value3()) && this.value4().equals(that.value4());
    }
  }
}

