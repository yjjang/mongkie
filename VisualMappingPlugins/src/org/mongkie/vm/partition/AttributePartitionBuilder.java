/*
 *  This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 *  Copyright (C) 2011 Korean Bioinformation Center(KOBIC)
 * 
 *  MONGKIE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  MONGKE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.vm.partition;

import java.awt.Color;
import java.util.*;
import kobic.prefuse.Config;
import static kobic.prefuse.Constants.EDGES;
import static kobic.prefuse.Constants.NODES;
import static org.mongkie.visualmap.VisualMapping.EDGE_ELEMENT;
import static org.mongkie.visualmap.VisualMapping.NODE_ELEMENT;
import org.mongkie.visualmap.partition.PartitionModel;
import org.mongkie.visualmap.spi.partition.Part;
import org.mongkie.visualmap.spi.partition.Partition;
import org.mongkie.visualmap.spi.partition.PartitionBuilder;
import org.openide.util.lookup.ServiceProvider;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.ColorLib;
import prefuse.util.DataLib;
import prefuse.visual.VisualItem;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = PartitionBuilder.class)
public class AttributePartitionBuilder implements PartitionBuilder {

    public AttributePartitionBuilder() {
    }

    @Override
    public Partition[] buildPartitions(PartitionModel model) {
        List<Partition> partitions = new ArrayList<Partition>();
        Graph graph = model.getDisplay().getGraph();
        //Nodes
        Table nodeTable = graph.getNodeTable();
        for (String column : DataLib.getColumnNames(nodeTable)) {
            if (nodeTable.getColumnType(column).isArray()) {
                continue;
            }
            AttributePartition partition = new AttributePartition(NODE_ELEMENT, column, model);
            partitions.add(partition);
        }
        //Edges
        Table edgeTable = graph.getEdgeTable();
        for (String column : DataLib.getColumnNames(edgeTable)) {
            if (edgeTable.getColumnType(column).isArray()) {
                continue;
            }
            AttributePartition partition = new AttributePartition(EDGE_ELEMENT, column, model);
            partitions.add(partition);
        }
        //Sort attributes by alphabetical order
        Partition[] partitionsArray = partitions.toArray(new Partition[0]);
        Arrays.sort(partitionsArray, new Comparator<Partition>() {
            @Override
            public int compare(Partition a, Partition b) {
                return (a.getName().compareTo(b.getName()));
            }
        });
        return partitionsArray;
    }

    @Override
    public Partition refreshPartition(Partition partition) {
        if (partition == null) {
            throw new NullPointerException();
        }
        if (partition instanceof AttributePartition) {
            return ((AttributePartition) partition).clone();
        } else {
            throw new IllegalArgumentException("Partition must be an AttributePartition");
        }
    }

    public static class AttributePartition implements Partition {

        private final String elementType;
        private final String column;
        private final PartitionModel model;
        private final List<Part> parts = new ArrayList<Part>();

        public AttributePartition(String elementType, String column, PartitionModel model) {
            this.elementType = elementType;
            this.column = column;
            this.model = model;
            initializeParts();
        }

        private void initializeParts() {
            Table table;
            String group;
            if (elementType.equals(NODE_ELEMENT)) {
                table = model.getDisplay().getGraph().getNodeTable();
                group = NODES;
            } else if (elementType.equals(EDGE_ELEMENT)) {
                table = model.getDisplay().getGraph().getEdgeTable();
                group = EDGES;
            } else {
                throw new IllegalArgumentException("Element type must be Nodes or Edges");
            }
            Map<Object, Set<Tuple>> value2Tuples = table.groupBy(column);
            for (Object val : value2Tuples.keySet()) {
                AttributePart p = new AttributePart(val, this);
                for (Tuple tuple : value2Tuples.get(val)) {
                    p.addItem(model.getDisplay().getVisualization().getVisualItem(group, tuple));
                }
                p.setPortion((p.size() / (double) table.getTupleCount()) * 100);
                parts.add(p);
            }
            Collections.sort(parts, new Comparator<Part>() {
                @Override
                public int compare(Part p1, Part p2) {
                    return Double.compare(p2.getPortion(), p1.getPortion());
                }
            });
        }

        @Override
        public Object getValue(VisualItem item) {
            return item.get(column);
        }

        @Override
        public String getElementType() {
            return elementType;
        }

        @Override
        public Graph getGraph() {
            return model.getDisplay().getGraph();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public String getName() {
            return column;
        }

        @Override
        public String toString() {
            return getDisplayName();
        }

        @Override
        protected AttributePartition clone() {
            return new AttributePartition(elementType, column, model);
        }

        @Override
        public List<Part> getParts() {
            return Collections.unmodifiableList(parts);
        }

        public static class AttributePart implements Part {

            private final Object value;
            private final Partition partition;
            private double portion = .0D;
            private final List<VisualItem> items = new ArrayList<VisualItem>();
            private Color color = ColorLib.getColor(Config.COLOR_DEFAULT_AGGR_FILL);

            public AttributePart(Object value, Partition partition) {
                this.value = value;
                this.partition = partition;
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public Partition getPartition() {
                return partition;
            }

            protected void addItem(VisualItem item) {
                items.add(item);
            }

            @Override
            public List<VisualItem> getVisualItems() {
                return Collections.unmodifiableList(items);
            }

            @Override
            public double getPortion() {
                return portion;
            }

            private void setPortion(double portion) {
                this.portion = portion;
            }

            @Override
            public Color getColor() {
                return color;
            }

            @Override
            public void setColor(Color color) {
                this.color = color;
            }

            @Override
            public int size() {
                return items.size();
            }
        }
    }
}
