# Nature-Inspired-Optimization
Solutions to exercises done as part of one of my university third year modules.

## Assignment 1
This assignment aims to assess how you implement and experimentally compare different natureinspired
algorithms. It aims at giving you some first hands-on experience of applying a natureinspired
algorithm in a given context and drawing conclusions about some operators from a set
of experimental results. We consider different mutation operators in the context of continuous
function optimisation.

## Assignment 2
The problem is first studied by Achille Messac at Syracuse University [1] (paper can be found here).
The problem aims to design a pinned-pinned sandwich beam using three materials to support a
motor as shown in Figure 1. The beam is of lenth L, of with b, and symmetrical about its midplane.
As illustrated in Figure 1, the variables d1 and d2 locate the contact of materials one and two, and
two and three, respectively. The variable d3 locates the top of the beam. The mass density ρ,
Young’s modulus E, and cost per unit volume c for materials one, two and three are denoted
by the triplets (ρ1, E1, c1), (ρ2, E2, c2) and (ρ3, E3, c3), which is listed in Table 1. A vibratory
disturbance (at v Hz) is imparted from the motor onto the bean. The problem is to choose a set
of optimal design (decision) variables x = [d1, d2, d3, b, L] in order to (a) minimise the fundamental
frequency of the vibratory disturbance; and (b) minimise the total cost of building the beam
