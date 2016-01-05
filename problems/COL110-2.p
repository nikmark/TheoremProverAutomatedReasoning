%------------------------------------------------------------------------------
% File     : COL110-2 : TPTP v5.4.0. Released v3.2.0.
% Domain   : Combinatory Logic
% Problem  : Problem about combinators
% Version  : [Pau06] axioms : Reduced > Especial.
% English  :

% Refs     : [Pau06] Paulson (2006), Email to G. Sutcliffe
% Source   : [Pau06]
% Names    :

% Status   : Unsatisfiable
% Rating   : 0.00 v5.4.0, 0.11 v5.3.0, 0.15 v5.2.0, 0.00 v3.2.0
% Syntax   : Number of clauses     :    8 (   0 non-Horn;   2 unit;   8 RR)
%            Number of atoms       :   15 (   0 equality)
%            Maximal clause size   :    3 (   2 average)
%            Number of predicates  :    1 (   0 propositional; 3-3 arity)
%            Number of functors    :   13 (   8 constant; 0-4 arity)
%            Number of variables   :   18 (  16 singleton)
%            Maximal term depth    :    3 (   2 average)
% SPC      : CNF_UNS_RFO_NEQ_HRN

% Comments : The problems in the [Pau06] collection each have very many axioms,
%            of which only a small selection are required for the refutation.
%            The mission is to find those few axioms, after which a refutation
%            can be quite easily found. This version has only the necessary
%            axioms.
%------------------------------------------------------------------------------
cnf(cls_conjecture_2,negated_conjecture,
    ( c_in(c_Pair(v_x,v_yb,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_conjecture_3,negated_conjecture,
    ( c_in(c_Pair(v_z,v_wa,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_conjecture_4,negated_conjecture,
    ( ~ c_in(c_Pair(c_Comb_Ocomb_Oop_A_D_D(v_yb,v_wa),V_U,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | ~ c_in(c_Pair(c_Comb_Ocomb_Oop_A_D_D(v_ya,v_w),V_U,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_conjecture_5,negated_conjecture,
    ( c_in(c_Pair(v_ya,v_xb(V_U),tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | ~ c_in(c_Pair(v_x,V_U,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_conjecture_6,negated_conjecture,
    ( c_in(c_Pair(V_U,v_xb(V_U),tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | ~ c_in(c_Pair(v_x,V_U,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_conjecture_7,negated_conjecture,
    ( c_in(c_Pair(v_w,v_xaa(V_U),tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | ~ c_in(c_Pair(v_z,V_U,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_conjecture_8,negated_conjecture,
    ( c_in(c_Pair(V_U,v_xaa(V_U),tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | ~ c_in(c_Pair(v_z,V_U,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

cnf(cls_Comb_Oparcontract_Ointros__4_0,axiom,
    ( ~ c_in(c_Pair(V_z,V_w,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | ~ c_in(c_Pair(V_x,V_y,tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb))
    | c_in(c_Pair(c_Comb_Ocomb_Oop_A_D_D(V_x,V_z),c_Comb_Ocomb_Oop_A_D_D(V_y,V_w),tc_Comb_Ocomb,tc_Comb_Ocomb),c_Comb_Oparcontract,tc_prod(tc_Comb_Ocomb,tc_Comb_Ocomb)) )).

%------------------------------------------------------------------------------
