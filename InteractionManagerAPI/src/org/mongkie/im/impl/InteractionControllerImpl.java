/*
 * This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 * Visit <http://www.mongkie.org> for details about MONGKIE.
 * Copyright (C) 2012 Korean Bioinformation Center (KOBIC)
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
package org.mongkie.im.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import kobic.prefuse.Constants;
import kobic.prefuse.data.Attribute;
import kobic.prefuse.data.Schema;
import kobic.prefuse.data.io.GraphIO;
import org.mongkie.im.InteractionController;
import org.mongkie.im.SourceModel;
import org.mongkie.im.SourceModelChangeListener;
import org.mongkie.im.spi.Interaction;
import org.mongkie.im.spi.Interaction.Interactor;
import org.mongkie.im.spi.InteractionAction;
import org.mongkie.im.spi.InteractionSource;
import org.mongkie.longtask.LongTask;
import org.mongkie.longtask.progress.Progress;
import org.mongkie.longtask.progress.ProgressTicket;
import org.mongkie.util.Persistence;
import org.mongkie.util.UIUtilities;
import org.mongkie.visualization.MongkieDisplay;
import org.mongkie.visualization.VisualizationController;
import org.mongkie.visualization.util.LayoutService.ExpandingLayout;
import org.mongkie.visualization.util.VisualStyle;
import org.mongkie.visualization.workspace.WorkspaceListener;
import org.openide.ErrorManager;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import prefuse.Visualization;
import static prefuse.Visualization.*;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.column.Column;
import prefuse.util.DataLib;
import prefuse.util.StringLib;
import prefuse.util.TypeLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = InteractionController.class)
public class InteractionControllerImpl implements InteractionController {

    private final Map<String, List<InteractionSource>> sourcesByCategory;
    private final Map<InteractionSource, SourceModelImpl> models =
            Collections.synchronizedMap(new HashMap<InteractionSource, SourceModelImpl>());
    private final Map<InteractionSource, Cache> caches = new HashMap<InteractionSource, Cache>();

    public InteractionControllerImpl() {
        Lookup.getDefault().lookup(VisualizationController.class).addWorkspaceListener(
                new WorkspaceListener() {
                    @Override
                    public void displaySelected(MongkieDisplay display) {
                        assert models.isEmpty();
                        Collection<? extends SourceModelImpl> modelImpls = display.getLookup().lookupAll(SourceModelImpl.class);
                        if (modelImpls.isEmpty()) {
                            for (InteractionSource is : Lookup.getDefault().lookupAll(InteractionSource.class)) {
                                SourceModelImpl m = new SourceModelImpl(display, is);
                                display.add(m);
                                models.put(is, m);
                            }
                            // Initialize models for others
                            for (InteractionSource is : getInteractionSources(CATEGORY_OTHERS)) {
                                SourceModelImpl m = new SourceModelImpl(display, is);
                                display.add(m);
                                models.put(is, m);
                            }
                        } else {
                            for (SourceModelImpl m : modelImpls) {
                                models.put(m.getInteractionSource(), m);
                            }
                            // Create models of newly added graph sources
                            for (InteractionSource is : getInteractionSources(CATEGORY_OTHERS)) {
                                if (models.keySet().contains(is)) {
                                    continue;
                                }
                                SourceModelImpl m = new SourceModelImpl(display, is);
                                display.add(m);
                                models.put(is, m);
                                if (listeners.containsKey(display)) {
                                    for (SourceModelChangeListener l : listeners.get(display)) {
                                        l.modelAdded(m);
                                    }
                                }
                            }
                            // Remove models of deleted graph sources
                            List<InteractionSource> sources = new ArrayList<InteractionSource>(models.keySet());
                            sources.removeAll(getInteractionSources(CATEGORY_OTHERS));
                            for (InteractionSource s : sources) {
                                if (s instanceof GraphSource) {
                                    SourceModelImpl m = models.remove(s);
                                    assert m != null;
                                    display.remove(m);
                                    if (listeners.containsKey(display)) {
                                        for (SourceModelChangeListener l : listeners.get(display)) {
                                            l.modelRemoved(m);
                                        }
                                    }
                                    m.dispose();
                                }
                            }
                        }
                    }

                    @Override
                    public void displayDeselected(MongkieDisplay display) {
                        models.clear();
                    }

                    @Override
                    public void displayClosed(MongkieDisplay display) {
                    }

                    @Override
                    public void displayClosedAll() {
                    }
                });
        MongkieDisplay d = Lookup.getDefault().lookup(VisualizationController.class).getDisplay();
        assert d != null && d.getLookup().lookupAll(SourceModelImpl.class).isEmpty();
        for (InteractionSource is : Lookup.getDefault().lookupAll(InteractionSource.class)) {
            SourceModelImpl m = new SourceModelImpl(d, is);
            d.add(m);
            models.put(is, m);
            // Initialize cache per source
            caches.put(is, new Cache());
        }
        sourcesByCategory = new LinkedHashMap<String, List<InteractionSource>>();
        for (InteractionSource is : Lookup.getDefault().lookupAll(InteractionSource.class)) {
            String c = is.getCategory();
            List<InteractionSource> sources = sourcesByCategory.get(c);
            if (sources == null) {
                sources = new ArrayList<InteractionSource>();
                sourcesByCategory.put(c, sources);
            }
            sources.add(is);
        }
        // Others
        sourcesByCategory.put(CATEGORY_OTHERS, new ArrayList<InteractionSource>(GraphSource.getPersistence().getValues()));
        for (InteractionSource is : sourcesByCategory.get(CATEGORY_OTHERS)) {
            SourceModelImpl m = new SourceModelImpl(d, is);
            d.add(m);
            models.put(is, m);
            caches.put(is, new Cache());
        }
    }

    @Override
    public VisualStyle<EdgeItem> getEdgeVisualStyle(InteractionSource is) {
        return ((SourceModelImpl) getModel(is)).getEdgeVisualStyle();
    }

    @Override
    public VisualStyle<NodeItem> getNodeVisualStyle(InteractionSource is) {
        return ((SourceModelImpl) getModel(is)).getNodeVisualStyle();
    }

    @Override
    public Iterator<EdgeItem> getEdgeItems(InteractionSource is) {
        final SourceModelImpl model = models.get(is);
        final Iterator<Edge> edges = model.getEdges().iterator();
        return new Iterator<EdgeItem>() {
            @Override
            public boolean hasNext() {
                return edges.hasNext();
            }

            @Override
            public EdgeItem next() {
                return (EdgeItem) model.getDisplay().getVisualization().getVisualItem(Constants.EDGES, edges.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported operation.");
            }
        };
    }

    @Override
    public String[] getCategories() {
        return sourcesByCategory.keySet().toArray(new String[]{});
    }

    @Override
    public List<InteractionSource> getInteractionSources(String category) {
        return sourcesByCategory.get(category);
    }

    @Override
    public SourceModel getModel(InteractionSource is) {
        return models.get(is);
    }

    @Override
    public <K> void executeExpand(InteractionSource<K> is, K... keys) {
        SourceModelImpl m = (SourceModelImpl) getModel(is);
        String keyField = m.getKeyField();
        if (keyField != null) {
            Expand<K> expand = new Expand<K>(m, keyField, keys);
            m.getExpandExecutor().execute(expand, expand);
        }
    }

    @Override
    public <K> void executeLink(InteractionSource<K> is) {
        SourceModelImpl m = (SourceModelImpl) getModel(is);
        String keyField = m.getKeyField();
        if (keyField != null) {
            Link<K> link = new Link<K>(m, keyField);
            m.getLinkExecutor().execute(link, link);
        }
    }

    @Override
    public void executeUnlink(final InteractionSource is) {
        final SourceModelImpl m = (SourceModelImpl) getModel(is);
        final MongkieDisplay d = m.getDisplay();
        final Graph g = d.getGraph();
        try {
            m.setUnlinking(true);
            d.getVisualization().process(new Runnable() {
                @Override
                public void run() {
//                List<Integer> edges = new ArrayList<Integer>();
//                for (Iterator<Integer> edgeIter =
//                        DataLib.rows(g.getEdgeTable(), FIELD_INTERACTION_SOURCE, is.getName());
//                        edgeIter.hasNext();) {
//                    edges.add(edgeIter.next());
//                }
//                for (int e : edges) {
//                    g.removeEdge(e);
//                }
//                m.clearInteractions();
                    for (Edge e : m.clearInteractions()) {
                        g.removeEdge(e);
                    }
                }
            });
            // Remove attribute columns also...
            d.getVisualization().process(new Runnable() {
                @Override
                public void run() {
                    Schema as = is.getAnnotationSchema();
                    Table nodeTable = g.getNodeTable();
                    for (int i = 0; i < as.getColumnCount(); i++) {
                        String col = getAttributeName(as.getColumnName(i), is.getName());
                        if (nodeTable.getColumnNumber(col) < 0) {
                            continue;
                        }
                        nodeTable.removeColumn(col);
                    }
                    Schema es = is.getInteractionSchema();
                    Table edgeTable = g.getEdgeTable();
                    for (int i = 0; i < es.getColumnCount(); i++) {
                        String col = getAttributeName(es.getColumnName(i), null);
                        if (edgeTable.getColumnNumber(col) < 0) {
                            continue;
                        }
                        edgeTable.removeColumn(col);
                    }
                    if (DataLib.uniqueCount(edgeTable.tuples(), InteractionSource.FIELD) == 0) {
                        edgeTable.removeColumn(InteractionSource.FIELD);
                    }
                }
            });
        } finally {
            m.setUnlinking(false);
        }
        d.getVisualization().repaint();
        d.fireGraphChangedEvent();
        m.fireUnlinkedEvent();
    }

    @Override
    public boolean setKeyField(InteractionSource is, String key) {
        return ((SourceModelImpl) getModel(is)).setKeyField(key);
    }

    private class Expand<K> extends Query<K> {

        private final Set<K> keys;
        private final boolean linked;

        public Expand(SourceModelImpl m, String keyField, K... keys) {
            super(m, keyField);
            this.keys = new HashSet<K>(Arrays.asList(keys));
            linked = (keys.length == DataLib.uniqueCount(m.getDisplay().getGraph().nodes(), keyField));
        }

        @Override
        protected Set<K> getQueryKeys() {
            return keys;
        }

        @Override
        protected Set<Interaction<K>> query(InteractionSource<K> is, Set<K> keys) throws Exception {
            Set<Interaction<K>> interactions = super.query(is, keys);
            Set<K> _expandedKeys = new HashSet<K>();
// Ignore direction TODO: need to digg further
//            if (is.isDirected()) {
                for (Interaction<K> i : interactions) {
                    K sourceKey = i.getSourceKey();
                    if (!keys.contains(sourceKey)) {
                        _expandedKeys.add(sourceKey);
                    }
                    K targetKey = i.getTargetKey();
                    if (!keys.contains(targetKey)) {
                        _expandedKeys.add(targetKey);
                    }
                }
//            } else {
//                for (Interaction<K> i : interactions) {
//                    K targetKey = i.getTargetKey();
//                    if (!keys.contains(targetKey)) {
//                        _expandedKeys.add(targetKey);
//                    }
//                }
//            }
            interactions.addAll(super.query(is, _expandedKeys));
            Set<K> existingKeys = getAllNodeKeys();
            for (Iterator<Interaction<K>> interactionIter = interactions.iterator(); interactionIter.hasNext();) {
                Interaction<K> i = interactionIter.next();
                K sourceKey = i.getSourceKey();
                K targetKey = i.getTargetKey();
                if ((!existingKeys.contains(sourceKey) && !_expandedKeys.contains(sourceKey))
                        || (!existingKeys.contains(targetKey) && !_expandedKeys.contains(targetKey))) {
                    interactionIter.remove();
                } else if (existingKeys.contains(sourceKey) && !existingKeys.contains(targetKey)) {
                    List<K> _keys = expandedKeys.get(sourceKey);
                    if (_keys == null) {
                        expandedKeys.put(sourceKey, _keys = new ArrayList<K>());
                    }
                    _keys.add(targetKey);
                } else if (!existingKeys.contains(sourceKey) && existingKeys.contains(targetKey)) {
                    List<K> _keys = expandedKeys.get(targetKey);
                    if (_keys == null) {
                        expandedKeys.put(targetKey, _keys = new ArrayList<K>());
                    }
                    _keys.add(sourceKey);
                }
            }
            return interactions;
        }
        private final Map<K, List<K>> expandedKeys = new HashMap<K, List<K>>();

        @Override
        protected void addNodes(Set<Interaction<K>> interactions, Graph g) {
            for (Interaction<K> i : interactions) {
                if (DataLib.get(g.getNodeTable(), keyField, i.getSourceKey()) < 0) {
                    Node n = g.addNode(); // Expanded node
                    n.set(keyField, i.getSourceKey());
                }
                if (DataLib.get(g.getNodeTable(), keyField, i.getTargetKey()) < 0) {
                    Node n = g.addNode(); // Expanded node
                    n.set(keyField, i.getTargetKey());
                }
            }
        }

        @Override
        protected void queryFinished(boolean success) {
            super.queryFinished(success);
            m.setLinked(success && linked);
            if (success && !expandedKeys.isEmpty()) {
                Lookup.getDefault().lookup(ExpandingLayout.class).layout(m.getDisplay(), getLayoutReferers());
            }
            expandedKeys.clear();
        }

        private Map<NodeItem, List<NodeItem>> getLayoutReferers() {
            Map<NodeItem, List<NodeItem>> referers = new HashMap<NodeItem, List<NodeItem>>();
            Visualization v = m.getDisplay().getVisualization();
            Graph g = m.getDisplay().getGraph();
            for (K refererKey : expandedKeys.keySet()) {
                NodeItem referer = (NodeItem) v.getVisualItem(Constants.NODES, g.getNode(DataLib.get(g.getNodeTable(), keyField, refererKey)));
                List<NodeItem> nodes = new ArrayList<NodeItem>();
                for (K k : expandedKeys.get(refererKey)) {
                    nodes.add((NodeItem) v.getVisualItem(Constants.NODES, g.getNode(DataLib.get(g.getNodeTable(), keyField, k))));
                }
                referers.put(referer, nodes);
            }
            return referers;
        }
    }

    private class Link<K> extends Query<K> {

        Link(SourceModelImpl m, String keyField) {
            super(m, keyField);
        }

        @Override
        protected Set<K> getQueryKeys() {
            return getAllNodeKeys();
        }

        @Override
        protected Set<Interaction<K>> query(InteractionSource<K> is, Set<K> keys) throws Exception {
            Set<Interaction<K>> interactions = super.query(is, keys);
            for (Iterator<Interaction<K>> interactionIter = interactions.iterator(); interactionIter.hasNext();) {
                Interaction<K> i = interactionIter.next();
                // Keys are keys of all nodes
                if (!keys.contains(i.getSourceKey()) || !keys.contains(i.getTargetKey())) {
                    interactionIter.remove();
                }
            }
            return interactions;
        }

        @Override
        protected void queryFinished(boolean success) {
            super.queryFinished(success);
            m.setLinked(success);
        }

        @Override
        protected void addNodes(Set<Interaction<K>> interactions, Graph g) {
            // Nothing to do on linking
        }
    }

    private abstract class Query<K> implements LongTask, Runnable {

        protected final SourceModelImpl m;
        protected final String keyField;
        private ProgressTicket progressTicket;

        Query(SourceModelImpl m, String keyField) {
            this.m = m;
            this.keyField = keyField;
        }

        @Override
        public boolean cancel() {
            return false;
        }

        @Override
        public void setProgressTicket(ProgressTicket progressTicket) {
            this.progressTicket = progressTicket;
        }

        private void annotateNodes(Set<K> keys) throws Exception {
            final Graph g = m.getDisplay().getGraph();
            final InteractionSource<K> is = m.getInteractionSource();
            addAttributeColumns(g.getNodeTable(), is.getAnnotationSchema(), is.getName());
            final Map<K, Attribute.Set> results = caches.get(is).annotate(keys);
            keys.removeAll(results.keySet());
            Map<K, Attribute.Set> qResults = is.annotate(keys.toArray((K[]) Array.newInstance(is.getKeyType(), 0)));
            for (K k : qResults.keySet()) {
                Attribute.Set attributes = qResults.get(k);
                results.put(k, attributes);
                caches.get(is).put(k, attributes);
                keys.remove(k);
            }
            // Caching keys with no attributes
            for (K k : keys) {
                caches.get(is).put(k, NO_ATTRIBUTES);
            }
            m.getDisplay().getVisualization().process(new Runnable() {
                @Override
                public void run() {
                    for (K k : results.keySet()) {
                        for (Iterator<Integer> nodeIter =
                                DataLib.rows(g.getNodeTable(), keyField, k); nodeIter.hasNext();) {
                            Node n = g.getNode(nodeIter.next());
                            Attribute.Set attributes = results.get(k);
                            for (Attribute a : attributes) {
                                String name = getAttributeName(a.getName(), is.getName());
                                if (n.getColumnIndex(name) < 0) {
                                    Logger.getLogger(getClass().getName()).log(Level.WARNING,
                                            "Annotation schema of {0} does not contain the attribute name: {1}", new String[]{is.getName(), a.getName()});
                                    continue;
                                }
                                n.set(name, a.getValue());
                            }
                            // Update label fields of expanded nodes
                            String graphLabel = g.getNodeLabelField();
                            String sourceLabel = is.getAnnotationSchema().getLabelField();
                            if (sourceLabel != null && graphLabel != null && n.getString(graphLabel) == null) {
                                if (g.getNodeTable().getColumnType(graphLabel) == String.class) {
                                    n.setString(graphLabel, attributes.getValue(sourceLabel).toString());
                                } else if (g.getNodeTable().getColumnType(graphLabel) == is.getAnnotationSchema().getColumnType(sourceLabel)) {
                                    n.set(graphLabel, attributes.getValue(sourceLabel));
                                }
                            }
                        }
                    }
                }
            });
        }
        protected final Attribute.Set NO_ATTRIBUTES = new Attribute.Set();

        protected abstract Set<K> getQueryKeys();

        protected final Set<K> getAllNodeKeys() {
            Set<K> keys = new HashSet<K>();
            for (Iterator<Node> nodeIter = m.getDisplay().getGraph().nodes(); nodeIter.hasNext();) {
                keys.add((K) nodeIter.next().get(keyField));
            }
            return keys;
        }

        /**
         * Returns an already existing edge of the interaction source for some
         * reasons. ex) A graph which is exported with contents provided by the
         * interaction source
         *
         * @param g
         * @param i
         * @param source
         * @param target
         * @return an already existing edge for the interaction
         */
        private Edge getExistingEdge(Graph g, Interaction i, Node source, Node target) {
            for (Edge e : g.getEdges(source, target)) {
                if (i.identicalWith(e)) {
                    return e;
                }
            }
            if (!i.getInteractionSource().isDirected()) {
                for (Edge e : g.getEdges(target, source)) {
                    if (i.identicalWith(e)) {
                        return e;
                    }
                }
            }
            return null;
        }

        protected Set<Interaction<K>> query(InteractionSource<K> is, Set<K> keys) throws Exception {
            Map<K, Set<Interaction<K>>> results = caches.get(is).query(keys);
            List<K> _keys = new ArrayList<K>(keys);
            _keys.removeAll(results.keySet());
            Map<K, Set<Interaction<K>>> qResults = is.query(_keys.toArray((K[]) Array.newInstance(is.getKeyType(), _keys.size())));
            for (K k : qResults.keySet()) {
                Set<Interaction<K>> result = qResults.get(k);
                results.put(k, Collections.unmodifiableSet(result));
                caches.get(is).put(k, result);
                _keys.remove(k);
            }
            // Caching keys with no interactions
            for (K k : _keys) {
                results.put(k, NO_INTERACTIONS);
                caches.get(is).put(k, NO_INTERACTIONS);
            }
            // Remove duplicated interactions using equals() and hash()
            Set<Interaction<K>> interactions = new HashSet<Interaction<K>>();
            for (K k : results.keySet()) {
                interactions.addAll(results.get(k));
            }
            // Remove already added interactions
            interactions.removeAll(m.getInteractions());
            return interactions;
        }
        protected final Set<Interaction<K>> NO_INTERACTIONS = Collections.unmodifiableSet(new HashSet<Interaction<K>>());

        protected abstract void addNodes(Set<Interaction<K>> interactions, Graph g);

        @Override
        public void run() {
            Progress.setDisplayName(progressTicket, "Querying interactions from " + m.getInteractionSource().getName());
            Progress.start(progressTicket);
            try {
                final InteractionSource<K> is = m.getInteractionSource();
                final Set<Interaction<K>> interactions = query(is, getQueryKeys());
                final Visualization v = m.getDisplay().getVisualization();
                v.process(new Runnable() {
                    @Override
                    public void run() {
                        Graph g = m.getDisplay().getGraph();
                        addNodes(interactions, g);
                        // Add columns for attributes of the interaction
                        if (g.getEdgeTable().getColumn(InteractionSource.FIELD) == null) {
                            g.getEdgeTable().addColumn(InteractionSource.FIELD, String.class, null);
                        }
                        addAttributeColumns(g.getEdgeTable(), is.getInteractionSchema(), null);
                        for (Interaction<K> i : interactions) {
                            for (Iterator<Integer> sourceIter =
                                    DataLib.rows(g.getNodeTable(), keyField, i.getSourceKey()); sourceIter.hasNext();) {
                                Node source = g.getNode(sourceIter.next());
                                for (Iterator<Integer> targetIter =
                                        DataLib.rows(g.getNodeTable(), keyField, i.getTargetKey()); targetIter.hasNext();) {
                                    Node target = g.getNode(targetIter.next());
//                                    if (interactor.hasAttributes()) {
//                                        for (Attribute a : interactor.getAttributes()) {
//                                            String name = getAttributeName(a.getName(), is.getName());
//                                            if (target.getColumnIndex(name) < 0) {
//                                                Logger.getLogger(getClass().getName()).log(Level.WARNING,
//                                                        "Annotation schema of {0} does not contain the attribute name: {1}", new String[]{is.getName(), a.getName()});
//                                                continue;
//                                            }
//                                            target.set(name, a.getValue());
//                                        }
//                                    }
                                    Edge e = getExistingEdge(g, i, source, target);
                                    if (e == null) {
                                        e = g.addEdge(source, target);
                                        addedEdgeItems.add((EdgeItem) v.getVisualItem(Constants.EDGES, e));
                                    }
                                    m.addInteraction(i, e);
                                    for (Attribute a : i.getAttributeSet().getList()) {
                                        String name = getAttributeName(a.getName(), null);
                                        if (e.getColumnIndex(name) < 0) {
                                            Logger.getLogger(getClass().getName()).log(Level.WARNING,
                                                    "Interaction schema of {0} does not contain the attribute name: {1}", new String[]{is.getName(), name});
                                            continue;
                                        }
                                        if (a.getType() == String[].class) {
                                            //Multi-valued column
                                            e.setString(getAttributeName(a.getName(), null),
                                                    StringLib.concatStringArray((String[]) a.getValue(), Column.MULTI_VAL_SEPARATOR));
                                        } else {
                                            e.set(getAttributeName(a.getName(), null), a.getValue());
                                        }
                                    }
                                    e.setString(InteractionSource.FIELD, is.getName());
                                }
                            }
                        }
                    }
                });
                annotateNodes(getAllNodeKeys());
                m.getDisplay().fireGraphChangedEvent();
                queryFinished(true);
            } catch (Exception ex) {
                Logger.getLogger(Link.class.getName()).log(Level.SEVERE, null, ex);
                ErrorManager.getDefault().notify(ex);
                queryFinished(false);
            } finally {
                Progress.finish(progressTicket);
            }
        }

        protected void queryFinished(boolean success) {
            VisualStyle<EdgeItem> edgeStyle = getEdgeVisualStyle(m.getInteractionSource());
            for (EdgeItem e : addedEdgeItems) {
                for (String field : VisualStyle.FIELDS) {
                    // Just assign the value without repainting
                    edgeStyle.assign(field, e);
                }
            }
            addedEdgeItems.clear();
            m.getDisplay().getVisualization().rerun(DRAW);
        }
        private final List<EdgeItem> addedEdgeItems = new ArrayList<EdgeItem>();
    }

    private void addAttributeColumns(Table into, Schema s, String prefix) {
        if (s == null) {
            return;
        }
        for (int i = 0; i < s.getColumnCount(); i++) {
            String name = getAttributeName(s.getColumnName(i), prefix);
            if (into.getColumn(name) == null) {
                Class type = s.getColumnType(i);
                try {
                    type = TypeLib.getPrimitiveType(type);
                } catch (IllegalArgumentException ex) {
                    //type is not a wrapper type.
                }
                // Multi-value column
                if (type == String[].class) {
                    type = String.class;
                }
                into.addColumn(name, type);
            }
        }
    }

    private String getAttributeName(String name, String prefix) {
        return (prefix == null || prefix.length() == 0) ? name : prefix + "_" + name;
    }

    private class Cache<K> {

        Map<K, Set<Interaction<K>>> interactionLookup = new HashMap<K, Set<Interaction<K>>>();
        Map<K, Attribute.Set> attributeLookup = new HashMap<K, Attribute.Set>();

        public boolean put(K k, Set<Interaction<K>> interactions) {
            if (interactionLookup.containsKey(k)) {
                return false;
            }
            interactionLookup.put(k, interactions);
            return true;
        }

        public boolean put(K k, Attribute.Set attributes) {
            if (attributeLookup.containsKey(k)) {
                return false;
            }
            attributeLookup.put(k, attributes);
            return true;
        }

        public Map<K, Set<Interaction<K>>> query(Set<K> keys) {
            Map<K, Set<Interaction<K>>> results = new HashMap<K, Set<Interaction<K>>>();
            for (K key : keys) {
                if (interactionLookup.containsKey(key)) {
                    results.put(key, interactionLookup.get(key));
                }
            }
            return results;
        }

        public Map<K, Attribute.Set> annotate(Set<K> keys) {
            Map<K, Attribute.Set> results = new HashMap<K, Attribute.Set>();
            for (K key : keys) {
                if (attributeLookup.containsKey(key)) {
                    results.put(key, attributeLookup.get(key));
                }
            }
            return results;
        }

        public void clear() {
            interactionLookup.clear();
            attributeLookup.clear();
        }
    }

    @Override
    public InteractionSource getInteractionSource(String name) {
        for (InteractionSource is : models.keySet()) {
            if (name.equalsIgnoreCase(is.getName())) {
                return is;
            }
        }
        return null;
    }

    @Override
    public void addGraphInteractionSource(Graph g, String name, String nodeKeyCol) {
        GraphSource gs = new GraphSource(g, name, nodeKeyCol);
        MongkieDisplay d = Lookup.getDefault().lookup(VisualizationController.class).getDisplay();
        SourceModelImpl m = new SourceModelImpl(d, gs);
        d.add(m);
        models.put(gs, m);
        caches.put(gs, new Cache());
        sourcesByCategory.get(CATEGORY_OTHERS).add(gs);
        GraphSource.getPersistence().save(gs);
        if (listeners.containsKey(d)) {
            for (SourceModelChangeListener l : listeners.get(d)) {
                l.modelAdded(m);
            }
        }
    }

    private boolean removeGraphInteractionSource(GraphSource gs) {
        if (!GraphSource.getPersistence().delete(gs.getName())) {
            return false;
        }
        SourceModelImpl model = models.remove(gs);
        assert model != null;
        caches.remove(gs).clear();
        assert sourcesByCategory.get(CATEGORY_OTHERS).remove(gs);
        MongkieDisplay display = model.getDisplay();
        display.remove(model);
        if (listeners.containsKey(display)) {
            for (SourceModelChangeListener l : listeners.get(display)) {
                l.modelRemoved(model);
            }
        }
        model.dispose();
        return true;
    }

    @Override
    public boolean addModelChangeListener(MongkieDisplay d, SourceModelChangeListener l) {
        List<SourceModelChangeListener> ls = listeners.get(d);
        if (ls == null) {
            ls = new ArrayList<SourceModelChangeListener>();
            listeners.put(d, ls);
        }
        return !ls.contains(l) && ls.add(l);
    }

    @Override
    public boolean removeModelChangeListener(MongkieDisplay d, SourceModelChangeListener l) {
        return listeners.containsKey(d) && listeners.get(d).remove(l);
    }
    private final Map<MongkieDisplay, List<SourceModelChangeListener>> listeners =
            new HashMap<MongkieDisplay, List<SourceModelChangeListener>>();

    static class GraphSource implements InteractionSource, Persistence.Value {

        @Override
        public SettingUI getSettingUI() {
            return null;
        }

        @Override
        public InteractionAction[] getActions() {
            return new InteractionAction[]{
                        new InteractionAction<GraphSource>() {
                            @Override
                            public String getName() {
                                return "Delete";
                            }

                            @Override
                            public String getDescription() {
                                return "Delete the interaction source";
                            }

                            @Override
                            public Icon getIcon() {
                                return new ImageIcon(getClass().getResource("/org/mongkie/im/resources/delete.png"));
                            }

                            @Override
                            public boolean isEnabled(GraphSource gs) {
                                return true;
                            }

                            @Override
                            public void execute(GraphSource gs) {
                                if (Lookup.getDefault().lookup(UIUtilities.class).notifyConfirmation("Are you sure you want to delete " + gs.getName() + "?")) {
                                    ((InteractionControllerImpl) Lookup.getDefault().lookup(InteractionController.class)).removeGraphInteractionSource(gs);
                                }
                            }
                        }
                    };
        }

        @Override
        public RichDescription getRichDescription() {
            return null;
        }

        static final class PersistenceImpl extends Persistence.Values<GraphSource> {

            private static final String GRAPH = "graph";
            private static final String NODE_KEYCOL = "nodeKeyCol";

            private PersistenceImpl() {
            }

            @Override
            protected GraphSource load(Preferences node) {
                byte[] bytes = node.getByteArray(GRAPH, null);
                if (bytes != null) {
                    Graph g = GraphIO.readSerializableGraph(new ByteArrayInputStream(bytes));
                    return new GraphSource(g, node.name(), node.get(NODE_KEYCOL, g.getNodeKeyField()));
                }
                return null;
            }

            @Override
            protected void store(Preferences node, GraphSource gs) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                GraphIO.writeSerializableGraph(gs.g, bos);
                node.putByteArray(GRAPH, bos.toByteArray());
                node.put(NODE_KEYCOL, gs.nodeKeyCol);
            }

            @Override
            protected String getRootName() {
                return "GraphSources";
            }

            private static class Holder {

                private static final PersistenceImpl INSTANCE = new PersistenceImpl();
            }
        }
        final Graph g;
        final Schema is, as;
        String name;
        final String nodeKeyCol;

        GraphSource(Graph g, String name, String nodeKeyCol) {
            this.g = g;
            g.getNodeTable().index(nodeKeyCol);
            is = Schema.valueOf(g.getEdgeTable().getSchema());
            if (g.getEdgeLabelField() != null) {
                is.setLabelField(g.getEdgeLabelField());
            }
            as = Schema.valueOf(g.getNodeTable().getSchema());
            as.setKeyField(nodeKeyCol);
            if (g.getNodeLabelField() != null) {
                as.setLabelField(g.getNodeLabelField());
            }
            this.name = name;
            this.nodeKeyCol = nodeKeyCol;
        }

        static PersistenceImpl getPersistence() {
            return PersistenceImpl.Holder.INSTANCE;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getCategory() {
            return CATEGORY_OTHERS;
        }

        @Override
        public Schema getInteractionSchema() {
            return is;
        }

        @Override
        public Schema getAnnotationSchema() {
            return as;
        }

        @Override
        public boolean isDirected() {
            return g.isDirected();
        }

        @Override
        public Map<Object, Set<GraphInteraction>> query(Object... keys) throws Exception {
            Map<Object, Set<GraphInteraction>> results = new HashMap<Object, Set<GraphInteraction>>();
            for (Object key : keys) {
                int n = g.getNodeTable().getIndex(nodeKeyCol).get(key);
                if (n < 0) {
                    continue;
                }
                Set<GraphInteraction> interactions = new LinkedHashSet<GraphInteraction>();
                for (Iterator<Edge> edges = g.edges(g.getNode(n)); edges.hasNext();) {
                    interactions.add(new GraphInteraction(edges.next()));
                }
                results.put(key, interactions);
            }
            return results;
        }

        @Override
        public Map<Object, Attribute.Set> annotate(Object... keys) throws Exception {
            Map<Object, Attribute.Set> results = new HashMap<Object, Attribute.Set>();
            if (keys.length > 0) {
                for (Object key : keys) {
                    int n = g.getNodeTable().getIndex(nodeKeyCol).get(key);
                    if (n < 0) {
                        continue;
                    }
                    Node node = g.getNode(n);
                    Attribute.Set attributes = new Attribute.Set();
                    for (int i = 0; i < node.getColumnCount(); i++) {
                        attributes.add(new Attribute(node.getColumnName(i), node.get(i)));
                    }
                    results.put(key, attributes);
                }
            }
            return results;
        }

        @Override
        public Class getKeyType() {
            return g.getNodeTable().getColumnType(nodeKeyCol);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GraphSource other = (GraphSource) obj;
            return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
        }

        class GraphInteraction implements Interaction {

            final Object sourceKey, targetKey;
            final Edge e;
            final Interactor interactor;
            final Attribute.Set attributes;

            GraphInteraction(Edge e) {
                this.e = e;
                this.sourceKey = e.getSourceNode().get(nodeKeyCol);
                interactor = new Interactor(targetKey = e.getTargetNode().get(nodeKeyCol), new Attribute.Set());
                attributes = new Attribute.Set();
                for (int i = 0; i < e.getColumnCount(); i++) {
                    String col = e.getColumnName(i);
                    if (is.getColumnIndex(col) < 0) {
                        continue;
                    }
                    attributes.add(new Attribute(col, e.get(i)));
                }
            }

            @Override
            public Object getSourceKey() {
                return sourceKey;
            }

            @Override
            public Object getTargetKey() {
                return targetKey;
            }

            @Override
            public boolean isDirected() {
                return e.isDirected();
            }

            @Override
            public Interactor getInteractor() {
                return interactor;
            }

            @Override
            public Attribute.Set getAttributeSet() {
                return attributes;
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 97 * hash + (this.e != null ? this.e.hashCode() : 0);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final GraphInteraction other = (GraphInteraction) obj;
                return this.e == other.e || (this.e != null && this.e.equals(other.e));
            }

            @Override
            public GraphSource getInteractionSource() {
                return GraphSource.this;
            }

            @Override
            public boolean identicalWith(Edge e) {
                return name.equals(e.getString(FIELD));
            }
        }
    }
}
