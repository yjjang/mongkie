/*
 * This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 * Copyright (C) 2011 Korean Bioinformation Center(KOBIC)
 *
 * MONGKIE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MONGKIE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.ui.kopath.search;

import java.util.Collection;
import java.util.List;
import org.mongkie.kopath.Pathway;
import org.mongkie.kopath.spi.PathwayDatabase;
import org.mongkie.kopath.viz.PathwayNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 * 
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class SearchedPathwayChildFactory extends ChildFactory<Pathway> {

    private final PathwayDatabase db;
    private final String pathway;
    private final String[] genes;

    public SearchedPathwayChildFactory(PathwayDatabase db, String... genes) {
        this.db = db;
        this.genes = genes;
        this.pathway = null;
    }

    public SearchedPathwayChildFactory(PathwayDatabase db, String pathway) {
        this.db = db;
        this.genes = null;
        this.pathway = pathway;
    }

    @Override
    protected boolean createKeys(List<Pathway> toPopulate) {
        Collection<Pathway> pathways = genes != null ? db.searchPathway(genes) : db.searchPathway(pathway, true);
        for (Pathway p : pathways) {
            toPopulate.add(p);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Pathway key) {
        return new PathwayNode(key);
    }
}
