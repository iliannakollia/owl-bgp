/* Copyright 2011 by the Oxford University Computing Laboratory

   This file is part of OWL-BGP.

   OWL-BGP is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   OWL-BGP is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with OWL-BGP. If not, see <http://www.gnu.org/licenses/>.
 */
package  org.semanticweb.HermiT;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.hierarchy.InstanceStatistics;
import org.semanticweb.HermiT.hierarchy.InstanceStatistics.RoleInstanceStatistics;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.sparql.arq.OWLOntologyGraph;
import org.semanticweb.sparql.bgpevaluation.CostEstimationVisitor;
import org.semanticweb.sparql.owlbgp.model.Atomic;
import org.semanticweb.sparql.owlbgp.model.Variable;
import org.semanticweb.sparql.owlbgp.model.classexpressions.ClassExpression;
import org.semanticweb.sparql.owlbgp.model.classexpressions.Clazz;
import org.semanticweb.sparql.owlbgp.model.individuals.Individual;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectProperty;
import org.semanticweb.sparql.owlbgp.model.properties.ObjectPropertyExpression;

public class HermiTCostEstimationVisitor extends CostEstimationVisitor {

    protected double POSSIBLE_INSTANCE_SUCCESS=0.5;
    
    protected final OWLBGPHermiT m_hermit;
    protected final InstanceStatistics m_instanceStatistics;
    protected final double m_numDisjunctions;
    protected Integer m_classHierarchyDepth;
    protected Integer m_opHierarchyDepth;
    int r;
    public HermiTCostEstimationVisitor(OWLOntologyGraph graph, Map<Variable,Integer> bindingPositions, List<Atomic[]> candidateBindings) {
        super(graph, bindingPositions, candidateBindings);
        r=0;
        if (m_reasoner instanceof OWLBGPHermiT) {
            m_hermit=(OWLBGPHermiT)m_reasoner;
            m_instanceStatistics=m_hermit.getInstanceStatistics();
            double numDisjunctions=0;
            for (DLClause clause : m_hermit.getDLOntology().getDLClauses())
                if (clause.getHeadLength()>1) {
                    numDisjunctions+=clause.getHeadLength();
//                  System.out.println(clause);
                }   
            m_numDisjunctions=numDisjunctions;
        } else 
            throw new IllegalArgumentException("Error: The HermiT cost estimator can only be instantiated with a graph that has a (HermiT) Reasoner instance attached to it.");
    }
    protected double[] getClassAssertionCost(ClassExpression ce, Individual ind, Set<Variable> unbound, Variable indVar) {
    	double cost=0;
//        if (ce instanceof Atomic && (m_instanceStatistics==null || !m_instanceStatistics.areClassesInitialised()))
//            cost+=(m_classCount*m_indCount*COST_LOOKUP+COST_ENTAILMENT); // initialization required
        if (unbound.size()==0 && ce instanceof Atomic){//C(a)       		
        	boolean[] result=m_instanceStatistics.isKnownOrPossibleInstance((OWLClass)ce.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind.asOWLAPIObject(m_dataFactory)); 
            if (result[0])
            	return new double[] { cost+COST_LOOKUP, 1 };
            else if (result[1])
                return new double[] { cost+((double)0.5*COST_ENTAILMENT*m_instanceStatistics.getClassHierarchyDepth()), 1*POSSIBLE_INSTANCE_SUCCESS };
            else return new double[] { cost+COST_LOOKUP, 0};                 
        } else if (unbound.size()==1 && unbound.contains(indVar) && ce instanceof Atomic) {// C(?x) 
        	int[] estimate=m_instanceStatistics.getNumberOfInstances((OWLClass)ce.asOWLAPIObject(m_dataFactory));
 //             System.out.println("known "+ ce.toString()+ " instances: " +estimate[0]+"  possible: "+estimate[1]);
            return new double[] { cost + estimate[0]*COST_LOOKUP+estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getClassHierarchyDepth(), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
        } else if (unbound.size()==1 && ce instanceof Variable && !unbound.contains(ce)) {// ?x(:a)
            int[] estimate=m_instanceStatistics.getNumberOfTypes((OWLNamedIndividual)ind.asOWLAPIObject(m_dataFactory));
            return new double[] { cost + estimate[0]*COST_LOOKUP+estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getClassHierarchyDepth(), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) };
        } else {//?x(?y)
        	double[] costMatrix={0.0,0.0};
        	for (Clazz clas : m_graph.getClassesInSignature()) {
        		int[] estimate=m_instanceStatistics.getNumberOfInstances((OWLClass)clas.asOWLAPIObject(m_dataFactory));
        		costMatrix[0]+=estimate[0]*COST_LOOKUP+estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getClassHierarchyDepth();
                costMatrix[1]+=estimate[0]+POSSIBLE_INSTANCE_SUCCESS*estimate[1];
            }
        	return costMatrix;
        }
    }
    protected double[] getObjectPropertyAssertionCost(ObjectPropertyExpression op, Individual ind1, Individual ind2, Set<Variable> unbound, Variable opVar) {
        double cost=0;
        if (unbound.size()==0) {//r(:a :b)
        	boolean[] result=m_instanceStatistics.hasSuccessor((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind1.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind2.asOWLAPIObject(m_dataFactory));
            if (result[0])
            	return new double[] { cost+COST_LOOKUP, 1 };
            else if (result[1])
                return new double[] { cost+((double)0.5*COST_ENTAILMENT*m_instanceStatistics.getObjectPropertyHierarchyDepth()), 1*POSSIBLE_INSTANCE_SUCCESS };
            else return new double[] {cost+COST_LOOKUP, 0 }; 
        } else if (unbound.size()==1 && opVar!=null) {// ?x(:a :b) 
        	double[] costMatrix={0.0,0.0};
        	for (ObjectProperty prop : m_graph.getObjectPropertiesInSignature()) {
        		boolean[] result=m_instanceStatistics.hasSuccessor((OWLObjectProperty)prop.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind1.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind2.asOWLAPIObject(m_dataFactory));
                if (result[0]) {
                	costMatrix[1]+=COST_LOOKUP;
                	costMatrix[2]++;
                }	
                else if (result[1]) {
                	costMatrix[1]+=(double)0.5*COST_ENTAILMENT*m_instanceStatistics.getObjectPropertyHierarchyDepth();
                	costMatrix[2]=1*POSSIBLE_INSTANCE_SUCCESS; 
                }
                else {
                	costMatrix[1]+=COST_LOOKUP;
                	costMatrix[2]=0;
                }
        	}
            return costMatrix;
        } else if (unbound.size()==1 && opVar==null) {// op(:a ?x) or op(?x :a)
        	int[] estimate;
            if (ind2 instanceof Variable)
            	estimate=m_instanceStatistics.getNumberOfSuccessors((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind1.asOWLAPIObject(m_dataFactory));
            else
                estimate=m_instanceStatistics.getNumberOfPredecessors((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind2.asOWLAPIObject(m_dataFactory));
                //double[] estimate=m_instanceStatistics.getAverageNumberOfSuccessors(AtomicRole.create(op.getIRIString()));
            return new double[] { cost + estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth()), estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]) }; 
        } else if (unbound.size()==2 && opVar!=null) {// ?x(:a ?y) or ?x(?y :a)
        	int[] estimate;
        	double[] costMatrix={0.0,0.0};
            for (ObjectProperty prop : m_graph.getObjectPropertiesInSignature()) {
            	if (ind2 instanceof Variable) {
                	estimate=m_instanceStatistics.getNumberOfSuccessors((OWLObjectProperty)prop.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind1.asOWLAPIObject(m_dataFactory));
            	}
            	else {
                    estimate=m_instanceStatistics.getNumberOfPredecessors((OWLObjectProperty)prop.asOWLAPIObject(m_dataFactory), (OWLNamedIndividual)ind2.asOWLAPIObject(m_dataFactory));
            	}
                costMatrix[0]+=estimate[0]*COST_LOOKUP+(estimate[1]*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth());
                costMatrix[1]+=estimate[0]+(POSSIBLE_INSTANCE_SUCCESS*estimate[1]);
            }
            return costMatrix;
            
        } else if (unbound.size()==2 && opVar==null) {// op(?x ?y)
        	RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
                //int[] estimate=m_instanceStatistics.getNumberOfPropertyInstances((OWLObjectProperty)op.asOWLAPIObject(m_dataFactory));
//                System.out.println("known "+ op.toString()+ " instances: " +estimate[0]+"  possible: "+estimate[1]);
            return new double[] { cost + roleStatistics.getNumberOfKnownInstances()*COST_LOOKUP+(roleStatistics.getNumberOfPossibleInstances()*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth()), roleStatistics.getNumberOfKnownInstances()+(POSSIBLE_INSTANCE_SUCCESS*roleStatistics.getNumberOfPossibleInstances()) };
        }
        // ?x(?y ?z)
        else {
            double[] costMatrix={0.0,0.0};
        	for (ObjectProperty prop : m_graph.getObjectPropertiesInSignature()) {
            	RoleInstanceStatistics roleStatistics=m_instanceStatistics.getRoleInstanceStatistics((OWLObjectProperty)prop.asOWLAPIObject(m_dataFactory));
                costMatrix[0]+=roleStatistics.getNumberOfKnownInstances()*COST_LOOKUP+roleStatistics.getNumberOfPossibleInstances()*COST_ENTAILMENT*0.5*m_instanceStatistics.getObjectPropertyHierarchyDepth();
                costMatrix[1]+=roleStatistics.getNumberOfKnownInstances()+POSSIBLE_INSTANCE_SUCCESS*roleStatistics.getNumberOfPossibleInstances();
            }
            return costMatrix;
        }
    }
}