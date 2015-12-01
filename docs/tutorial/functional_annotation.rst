**********************************
Functional annotation of a cluster
**********************************

In this section, you will perform a GO :doc:`../network_analysis/enrichment_analysis` for genes in ``Cluster 3`` and visually annotate the cluster with representative functions via the :doc:`Visual Editor UI <../network_visualization/visual_editing>`.

* Make sure you select the **Network** tab of ``Cluster 3``.

 .. image:: ../images/tp53_network_log2fc.png

* Select the :menuselection:`Window --> Enrichment Analysis`.

 .. image:: ../images/enrich_analysis_win.png

* Before going to remaining steps, you need to import :download:`symbol2uniprot_all.csv </assets/symbol2uniprot_all.csv>` into the **node table**. The reason is that our in-house GO over-representation analysis tool expects IDs of gene set to be UniProt Accessions.

  #. Select :menuselection:`File --> Import --> Attributes from CSV File` as described in previous section.
  #. In the **Import Attributes** dialog,
  
    * Choose a **CSV file to import**: :download:`symbol2uniprot_all.csv </assets/symbol2uniprot_all.csv>`
    * **Key Column in Annotation File:** ``HGNC.symbol``
    * **Key Attribute in Network:** ``hgnc_symbol``
    
     .. image:: ../images/import_uniprot.png

* In the **Enrichment Analysis** window,

  1. Choose ``Gene Ontology`` in the drop-down list.
  2. Set **Gene ID column** to ``UniProt.Ac``.
  3. Set the multiple testing **Correction method** to ``Bonferroni``.
  4. Click the **Run** button.
  
   .. image:: ../images/enrich_analysis_run.png

* After a little, the result is shown in the **Enrichment** window at the bottom of main application.

  * You can see that GO terms of ``cell cycle checkpoint`` and ``DNA damage checkpoint`` are listed in first and second rows respectively.
  * Select the ``cell cycle checkpoint`` GO term, the click |info-icon| icon to see the details about the term.
  
   .. image:: ../images/enrich_analysis_result.png

* Next you will visually annotate the group node of ``Cluster 3`` with two representative functions.
  
  .. caution:: Now be sure to switch to the original network tab named ``tcga_gbm_edges``.
  
  * Click the group node of cluster ``3`` in the original GBM-altered network.
  * In the **Editor** window,
  * Click the ``...`` icon in **Name** property to edit a group name.
  * Enter ``DNA damage response`` and ``Cell Cycle``.
  * Set the **Font** property to ``Droid Serif 80 Bold`` or what you want. (You can open the font chooser by clicking ``...`` button in the **Font** property)
  
   .. image:: ../images/group_name_edit.png

* The result is shown below:

 .. image:: ../images/group_name_result.png

.. tip:: How to define a group node that contains nodes (genes or proteins) with a same function after GO over-representation analysis.
  
  After GO over-representation analysis of ``Cluster 3``:
  
  1. Select the **TreeTable view** tab in the **Enrichment** result window.
  2. Select a GO term named **kinase binding** at ``molecular_function > binding > protein binding > enzyme binding > kinase binding``.
  3. Click the |group-icon| button.
  
   .. image:: ../images/kinase_binding_tree.png
  
  4. After clicking the group node named ``kinase binding``, edit its visual properties in the **Editor** window.
  
    * Set **Font** to ``Droid Serif 80 Bold``.
    * Set **Text Color** to ``Orange``.
    * Set **Shape** to ``Convex hull of the lines``.
    
     .. image:: ../images/kinase_binding_edit.png
  
  5. Manually adjust the positions of nodes like below:
  
   .. image:: ../images/GO_kinase_binding.png
  
  6. Select a GO term named **cell cycle** at ``biological_process > cellular process > single-organism cellular process > cell cycle``.
  
  7. Click the |group-icon| button.
  
   .. image:: ../images/cell_cycle_tree.png
  
  8. After clicking the group node named ``cell cycle``, edit its visual properties in the **Editor** window.
  
    * Set **Font** to ``Droid Serif 100 Bold``.
    * Set **Text Color** to ``Blue``.
    * Set **Shape** to ``Rectangle``.
    
     .. image:: ../images/cell_cycle_edit.png
  
  9. Now you can see that ``cell cycle`` contains all genes in ``kinase binding``. The final result looks like below:
  
   .. image:: ../images/GO_cell_cycle.png

.. |info-icon| image:: ../images/info_icon.png
.. |group-icon| image:: ../images/group_icon.png

