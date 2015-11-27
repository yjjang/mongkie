******************
Network clustering
******************

You imported the GBM-altered network into MONGKIE, now you will apply the MCL :doc:`../network_analysis/network_clustering` algorithm to identify network modules representing genes with topological proximity and correlated expression.

.. important:: In the :abbr:`edge table (Located in the Data Table window at the bottom of main application)`, you can see that the ``i.weight`` column contains expression correlations.
  MONGKIE internally will assign the values of ``i.weight`` column in a edge table to the weights of edges for clustering. If you want to give weights to edges in your network, you should set the name of column containing weight values to ``i.weight``.

* In the **Clustering** window at the top-left of main application

  1. Choose the ``MCL`` algorithm in the drop-down list.
  2. Click the |run-button| button to start the algorithm.
  3. After a little, the identified clusters will be listed.

.. image:: ../images/mcl_clustering.png

* You can define these clusters as group nodes on the network.

  1. Select top 5 largest clusters by clicking ``Cluster 1`` and ``Cluster 5`` holding down the :kbd:`Shift` key.
  2. Right-click on the selection will show up a pop-up menu. Click the **Group** menu item.

.. image:: ../images/mcl_grouping.png

* Now you see that group nodes on the network are too large or a lot of overlappping with each other. You can use the force-directed algorithm to lay out them with more optimized size and position.

  1. In the **Layout** window at the bottom-left of main application, choose the ``Force Directed`` algorithm.
  2. Click the |run-button| buttton to start the algorithm.
  3. Set ``Spring Coefficient`` to the minimu value ``4.0E-5`` by dragging the slider tick to the left-most.
  4. When being satisfied with the result, you can stop the runnig of algorithm by clicking the |stop-button| button.

.. image:: ../images/mcl_layout.png

* The final network looks like the following:

.. image:: ../images/mcl_network.png

.. tip:: How to change colors of clusters in the **Clustering** window.
  Work in progress

.. |run-button| image:: ../images/run_button.png
.. |stop-button| image:: ../images/stop_button.png

