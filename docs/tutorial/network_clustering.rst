******************
Network clustering
******************

You imported the GBM-altered network into MONGKIE, now you will apply the MCL :doc:`../network_analysis/network_clustering` algorithm to identify network modules representing genes with topological proximity and correlated expression.

.. hint::
  In the :abbr:`edge table (Located in the Data Table window at the bottom of main application)`, you can see that the ``i.weight`` column contains expression correlations.
  
  .. image:: ../images/edge_table_iweight.png
  
  MONGKIE internally assigns the values of ``i.weight`` column in a edge table to the weights of edges for clustering. Therfore, to give weights to edges in your network, you should set the name of the column to ``i.weight``.

* In the **Clustering** window at the top-left of main application

  1. Choose the ``MCL`` algorithm in the drop-down list.
  2. Click the |run-button| button to start the algorithm.
  3. After a little, the identified clusters will be listed.
  
   .. image:: ../images/mcl_clustering.png

* You can define these clusters as group nodes on the network.

  1. Select top 5 largest clusters by clicking ``Cluster 1`` and ``Cluster 5`` holding down the :kbd:`Shift` key.
  2. Right-click on the selection will show up a pop-up menu. Click the **Group** menu item.
  
   .. image:: ../images/mcl_grouping.png

* Now you see that group nodes on the network are too large or a lot of overlapping with each other. You can use the force-directed algorithm to lay out them with more optimized size and position.

  1. In the **Layout** window at the bottom-left of main application, choose the ``Force Directed`` algorithm.
  2. Click the |run-button| bottom to start the algorithm.
  3. Set ``Spring Coefficient`` to the minimu value ``4.0E-5`` by dragging the slider tick to the left-most.
  4. When being satisfied with the result, you can stop the running of algorithm by clicking the |stop-button| button.
  
   .. image:: ../images/mcl_layout.png

* The final network looks like the following:

 .. image:: ../images/mcl_network.png

.. tip:: How to change default shape of group nodes in the network.
  
  In the **Display Options** panel, you can **globally** set various visualization options for nodes, edges, and groups.
  
  #. To open the **Display Options** panel, click the |arrowup-icon| button at the bottom-right of a network.
  #. Select the **Group** tab.
  #. Choose one of supported group shapes.
  
   .. image:: ../images/display_options_group.png
  
  For editing a individual component, you can use the :doc:`Visual Editor UI <../network_visualization/visual_editing>`.

.. tip:: How to change colors of clusters in the **Clustering** window.
  
  Initially the colors of clusters are assigned randomly. You can regenerate random colors for all clusters:
  
   .. image:: ../images/regen_cluster_colors.png
  
  Or, you can manually set the color of individual cluster.
  
  1. **Press and holding down** the mouse left-button on the color box on the left of a cluster name.
  2. While holding the mouse button down, move the pointer in the color panel.
  
    * Press ``ALT`` key down to show constant colors.
    * Press ``SHIFT`` key down to show desaturated colors.
    * Press both ``CTRL`` and ``ALT`` keys down to show recent colors.
  
  3. When you release the mouse button, a color under the pointer is set to the cluster's color.
  
   .. image:: ../images/change_cluster_colors.png
  
  Of course, you can use the :doc:`Visual Editor UI <../network_visualization/visual_editing>` for changing the color of group node.

.. tip:: By double-clicking the name of a cluster listed in the **Clustering** window, you can edit name of the cluster.

.. |run-button| image:: ../images/run_button.png
.. |stop-button| image:: ../images/stop_button.png
.. |arrowup-icon| image:: ../images/arrowup_icon.png

