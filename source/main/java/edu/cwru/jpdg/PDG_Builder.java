package edu.cwru.jpdg;

/* Tim Henderson (tadh@case.edu)
 *
 * This file is part of jpdg a library to generate Program Dependence Graphs
 * from JVM bytecode.
 *
 * Copyright (c) 2014, Tim Henderson, Case Western Reserve University
 *   Cleveland, Ohio 44106
 *   All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor,
 *   Boston, MA  02110-1301
 *   USA
 * or retrieve version 2.1 at their website:
 *   http://www.gnu.org/licenses/lgpl-2.1.html
 */ 

import java.util.*;

import soot.util.Chain;

import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.pdg.EnhancedBlockGraph;
import soot.toolkits.graph.ExpandedBlockGraph;

import edu.cwru.jpdg.graph.Graph;
import edu.cwru.jpdg.label.LabelMaker;

public class PDG_Builder {

    LabelMaker lm;
    Graph g = new Graph();
    Chain<soot.SootClass> classes;

    public static Graph build(LabelMaker lm, Chain<soot.SootClass> classes) {
        PDG_Builder self = new PDG_Builder(lm, classes);
        self.build_PDG();
        return self.g;
    }

    private PDG_Builder(LabelMaker lm, Chain<soot.SootClass> classes) {
        this.lm = lm;
        this.classes = classes;
    }

    void build_PDG() {
        for (soot.SootClass c : classes) {
            process_class(c);
        }
    }

    void process_class(soot.SootClass c) {
        for (soot.SootMethod m : c.getMethods()) {
            try {
                soot.Body body = m.retrieveActiveBody();
                BlockGraph ebg = new ExpandedBlockGraph(body);
                pDG_Builder.build(lm, g, c, m, body, ebg);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
